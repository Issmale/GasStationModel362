package org.example.hiring;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class HiringManagementSystem {
    private static Scanner scanner = new Scanner(System.in);
    private static HRManager hrManager = new HRManager();

    public static void main(String[] args) {
        while (true) {
            displayMainMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    addJobOpening();
                    break;
                case 2:
                    viewJobOpenings();
                    break;
                case 3:
                    addCandidate();
                    break;
                case 4:
                    viewCandidates();
                    break;
                case 5:
                    manageHiringProcess();
                    break;
                case 6:
                    System.out.println("Exiting the system. Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void displayMainMenu() {
        System.out.println("\n--- Gas Station Employee Hiring Management System ---");
        System.out.println("1. Add Job Opening");
        System.out.println("2. View Job Openings");
        System.out.println("3. Add Candidate");
        System.out.println("4. View Candidates");
        System.out.println("5. Manage Hiring Process");
        System.out.println("6. Exit");
        System.out.print("Enter your choice: ");
    }

    private static int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void addJobOpening() {
        System.out.println("\n--- Add New Job Opening ---");
        System.out.print("Enter Job ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Job Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Job Description: ");
        String description = scanner.nextLine();

        try (FileWriter fw = new FileWriter("job_openings.csv", true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(String.format("%s,%s,%s\n", id, title, description));
            System.out.println("Job opening added successfully!");
        } catch (IOException e) {
            System.out.println("Error adding job opening: " + e.getMessage());
        }
    }

    private static void viewJobOpenings() {
        System.out.println("\n--- Current Job Openings ---");
        hrManager.jobPostingSystem.retrieveJobOpenings().forEach(System.out::println);
    }

    private static void addCandidate() {
        System.out.println("\n--- Add New Candidate ---");
        System.out.print("Enter Candidate ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter Candidate Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Years of Experience: ");
        int experience = Integer.parseInt(scanner.nextLine());

        try (FileWriter fw = new FileWriter("candidate_profiles.csv", true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(String.format("%s,%s,%d\n", id, name, experience));
            System.out.println("Candidate added successfully!");
        } catch (IOException e) {
            System.out.println("Error adding candidate: " + e.getMessage());
        }
    }

    private static void viewCandidates() {
        System.out.println("\n--- Current Candidates ---");
        hrManager.candidateSystem.retrieveCandidates().forEach(System.out::println);
    }

    private static void manageHiringProcess() {
        hrManager.manageHiringProcess();
    }
}

class HRManager implements IHRManager {
    IJobPostingSystem jobPostingSystem = new JobPostingSystem("job_openings.csv");
    ICandidateManagementSystem candidateSystem = new CandidateManagementSystem("candidate_profiles.csv");
    IInterviewScheduler interviewScheduler = new InterviewScheduler("interview_schedule.csv");
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void manageHiringProcess() {
        // Retrieve and display job openings
        List<JobOpening> jobOpenings = jobPostingSystem.retrieveJobOpenings();

        if (jobOpenings.isEmpty()) {
            System.out.println("No job openings available.");
            return;
        }

        System.out.println("Active Job Openings:");
        jobOpenings.forEach(System.out::println);

        // Select a job to proceed with hiring
        System.out.print("Enter Job ID to proceed with hiring: ");
        String selectedJobId = scanner.nextLine();

        JobOpening selectedJob = jobOpenings.stream()
                .filter(job -> job.getId().equals(selectedJobId))
                .findFirst()
                .orElse(null);

        if (selectedJob == null) {
            System.out.println("Invalid Job ID.");
            return;
        }

        // Retrieve and review candidates
        List<Candidate> candidates = candidateSystem.retrieveCandidates();

        if (candidates.isEmpty()) {
            System.out.println("No candidates available.");
            return;
        }

        // Shortlist candidates for this specific job
        List<Candidate> shortlistedCandidates = candidateSystem.shortlistCandidates(candidates);

        if (shortlistedCandidates.isEmpty()) {
            System.out.println("No candidates meet the minimum experience requirements.");
            return;
        }

        System.out.println("Shortlisted Candidates:");
        shortlistedCandidates.forEach(System.out::println);

        // Interactively select a candidate
        System.out.print("Enter Candidate ID to hire: ");
        String selectedCandidateId = scanner.nextLine();

        Candidate selectedCandidate = shortlistedCandidates.stream()
                .filter(candidate -> candidate.getId().equals(selectedCandidateId))
                .findFirst()
                .orElse(null);

        if (selectedCandidate == null) {
            System.out.println("Invalid Candidate ID.");
            return;
        }

        // Schedule and send interview invitation
        interviewScheduler.scheduleInterviews(Arrays.asList(selectedCandidate));
        interviewScheduler.sendInterviewInvitations(Arrays.asList(selectedCandidate));

        // Confirm hiring
        System.out.printf("Do you want to hire %s for the position %s? (yes/no): ",
                selectedCandidate.getName(), selectedJob.getTitle());
        String confirm = scanner.nextLine().toLowerCase();

        if (!"yes".equals(confirm)) {
            System.out.println("Hiring process cancelled.");
            return;
        }

        // Log hiring decision
        jobPostingSystem.logHiringDecision(selectedCandidate);

        // Remove the hired candidate from the candidate file
        removeCandidate(selectedCandidate);

        // Remove the job opening from the job openings file
        removeJobOpening(selectedJob);

        System.out.printf("Candidate %s hired for position %s%n",
                selectedCandidate.getName(), selectedJob.getTitle());
    }

    private void removeCandidate(Candidate candidate) {
        try {
            File inputFile = new File("candidate_profiles.csv");
            File tempFile = new File("candidate_profiles_temp.csv");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                // Skip the line with the hired candidate
                if (!currentLine.startsWith(candidate.getId() + ",")) {
                    writer.write(currentLine + System.lineSeparator());
                }
            }
            writer.close();
            reader.close();

            // Delete the original file
            if (!inputFile.delete()) {
                System.out.println("Could not delete original file");
                return;
            }

            // Rename the temp file to the original filename
            if (!tempFile.renameTo(inputFile)) {
                System.out.println("Could not rename temp file");
            }
        } catch (IOException e) {
            System.out.println("Error removing candidate: " + e.getMessage());
        }
    }

    private void removeJobOpening(JobOpening job) {
        try {
            File inputFile = new File("job_openings.csv");
            File tempFile = new File("job_openings_temp.csv");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String currentLine;
            while ((currentLine = reader.readLine()) != null) {
                // Skip the line with the filled job opening
                if (!currentLine.startsWith(job.getId() + ",")) {
                    writer.write(currentLine + System.lineSeparator());
                }
            }
            writer.close();
            reader.close();

            // Delete the original file
            if (!inputFile.delete()) {
                System.out.println("Could not delete original file");
                return;
            }

            // Rename the temp file to the original filename
            if (!tempFile.renameTo(inputFile)) {
                System.out.println("Could not rename temp file");
            }
        } catch (IOException e) {
            System.out.println("Error removing job opening: " + e.getMessage());
        }
    }
}

// HR Manager Interface
interface IHRManager {
    void manageHiringProcess();
}

// Job Posting System Interface
interface IJobPostingSystem {
    List<JobOpening> retrieveJobOpenings();
    void customizeAndPostJob(List<JobOpening> jobOpenings);
    void logHiringDecision(Candidate candidate);
}

// Job Posting System Implementation
class JobPostingSystem implements IJobPostingSystem {
    private String jobFile;

    public JobPostingSystem(String jobFile) {
        this.jobFile = jobFile;
    }

    @Override
    public List<JobOpening> retrieveJobOpenings() {
        List<JobOpening> jobOpenings = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(jobFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                jobOpenings.add(new JobOpening(data[0], data[1], data[2]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jobOpenings;
    }

    @Override
    public void customizeAndPostJob(List<JobOpening> jobOpenings) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter job ID to customize and post:");
        String jobId = scanner.nextLine();

        JobOpening job = jobOpenings.stream()
                .filter(j -> j.getId().equals(jobId))
                .findFirst()
                .orElse(null);

        if (job != null) {
            System.out.println("Customizing job posting for: " + job.getTitle());
            // Assume posting logic here
            System.out.println("Job posted successfully.");
        } else {
            System.out.println("Invalid job ID.");
        }
    }

    @Override
    public void logHiringDecision(Candidate candidate) {
        System.out.println("Logging hiring decision...");
        // Logic to log candidate information
    }
}

// Candidate Management System Interface
interface ICandidateManagementSystem {
    List<Candidate> retrieveCandidates();
    List<Candidate> shortlistCandidates(List<Candidate> candidates);
    Candidate finalizeHiring(List<Candidate> candidates);
}

// Candidate Management System Implementation
class CandidateManagementSystem implements ICandidateManagementSystem {
    private String candidateFile;

    public CandidateManagementSystem(String candidateFile) {
        this.candidateFile = candidateFile;
    }

    @Override
    public List<Candidate> retrieveCandidates() {
        List<Candidate> candidates = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(candidateFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                candidates.add(new Candidate(data[0], data[1], Integer.parseInt(data[2])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return candidates;
    }

    @Override
    public List<Candidate> shortlistCandidates(List<Candidate> candidates) {
        System.out.println("Shortlisting candidates...");
        return candidates.stream()
                .filter(candidate -> candidate.getExperience() >= 2)
                .collect(Collectors.toList());
    }

    @Override
    public Candidate finalizeHiring(List<Candidate> candidates) {
        System.out.println("Conducting interviews and finalizing hiring...");
        return candidates.get(0); // Assume first candidate is selected for simplicity
    }
}

// Interview Scheduler Interface
interface IInterviewScheduler {
    void scheduleInterviews(List<Candidate> candidates);
    void sendInterviewInvitations(List<Candidate> candidates);
}

// Interview Scheduler Implementation
class InterviewScheduler implements IInterviewScheduler {
    private String scheduleFile;

    public InterviewScheduler(String scheduleFile) {
        this.scheduleFile = scheduleFile;
    }

    @Override
    public void scheduleInterviews(List<Candidate> candidates) {
        System.out.println("Scheduling interviews...");
        candidates.forEach(candidate -> System.out.printf("Interview scheduled for: %s%n", candidate.getName()));
    }

    @Override
    public void sendInterviewInvitations(List<Candidate> candidates) {
        System.out.println("Sending interview invitations...");
        candidates.forEach(candidate -> System.out.printf("Invitation sent to: %s%n", candidate.getName()));
    }
}

// Supporting Classes
class JobOpening {
    private String id;
    private String title;
    private String description;

    public JobOpening(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return String.format("Job ID: %s, Title: %s, Description: %s", id, title, description);
    }
}

class Candidate {
    private String id;
    private String name;
    private int experience;

    public Candidate(String id, String name, int experience) {
        this.id = id;
        this.name = name;
        this.experience = experience;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getExperience() {
        return experience;
    }

    @Override
    public String toString() {
        return String.format("ID: %s, Name: %s, Experience: %d years", id, name, experience);
    }
}
