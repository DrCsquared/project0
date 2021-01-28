package project0;

public class Employee extends User {
	
	private String employeeID;

	
	public Employee() {
		super();
	}

	public Employee(String employeeID) {
		super();
		this.employeeID = employeeID;
	}
	
	public void reviewAccount(Customer customer) 
	{
		System.out.println("Username: " + customer.getUserName());
		System.out.println("First name: " + customer.getFirst_name());
		System.out.println("Last name: " + customer.getLast_name());
		System.out.println("Checking Account : " + customer.getCheckingAccountValue());
		System.out.println("Savings Account : " + customer.getSavingAccountValue());	
	}
	
	
}
