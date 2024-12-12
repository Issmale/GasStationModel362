public class TestAddCustomer {
    public static void main(String[] args) {
        CustomerDataSystem customerDataSystem = new CustomerDataSystem("customer_data.csv");
        customerDataSystem.addCustomer("Alice Johnson", "Ankeny", "Art");
    }
}
