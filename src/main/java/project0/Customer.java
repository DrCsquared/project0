package project0;

public class Customer extends User {

	private double checkingAccountValue;
	private double savingAccountValue;

	public Customer() { super(); }
 
	public Customer(double checkingAccountValue,double savingAccountValue) 
	{ 
		 this.checkingAccountValue = checkingAccountValue; 
		 this.savingAccountValue = savingAccountValue; 
	}
	
	public void addToChecking(double value) 
	{
		this.checkingAccountValue += value;
		
	}
	
	public void addToSavings(double value) 
	{
		this.savingAccountValue += value;
	}
	
	public boolean withdrawalFromChecking(double value) 
	{
		
		boolean returnBool;
		if((this.checkingAccountValue - value) < 0) 
		{
			returnBool = false;
			//System.out.println("You can't withdrawal :" + value +" becuase you only have: " + this.checkingAccountValue );
		}
		else {
			
			this.checkingAccountValue -= value;
			returnBool = true;
			
		}
		return returnBool;
	}
	
	public boolean withdrawalFromSavings(double value) 
	{
		//boolean returnBool;
		if((this.savingAccountValue - value) < 0) 
		{
			//returnBool = false;
			return false;
			//System.out.println("You can't withdrawal :" + value +" becuase you only have: " + this.savingAccountValue );
		}
		else 
		{
			this.savingAccountValue -= value;	
			//returnBool = true;
			return true;
		}
		//return returnBool;
	}
	// Region getters and setters
	public double getCheckingAccountValue() {
		return checkingAccountValue;
	}

	public double getSavingAccountValue() {
		return savingAccountValue;
	}

	// EndRegion

}
