package project0;

public class Customer extends User {

	private boolean checkingAccountCreated = false;
	private boolean savingAccountCreated = false;
	
	private double checkingAccountValue;
	private double savingAccountValue;

	public Customer() { super(); }
 
	public void createCheckingAccount(boolean canCreate, double value ) 
	{
		if(canCreate)
		{
			this.checkingAccountValue = value;
			this.checkingAccountCreated = true;
		}
		
		else
		{
			System.out.println("You have a Checking account already");
		}
	}
	
	public void createSavingAccount(boolean canCreate, double value) 
	{
		if(canCreate) 
		{
			this.savingAccountValue = value;
			this.savingAccountCreated = true;
		}
		
		else
		{
			System.out.println("You have a savings account already");
		}
	}
	
	public void addToChecking(double value) 
	{
		if(value < 0) 
		{
			System.out.println("You can't deposit a negative value.");
		}
		
		else 
		{
			this.checkingAccountValue += value;	
			System.out.println("You deposited :" + value + " to your checking account");
		}
	}
	
	public void addToSavings(double value) 
	{
		
		if(value < 0) 
		{
			System.out.println("You can't deposit a negative value");
		}
		else 
		{
			this.savingAccountValue += value;
			System.out.println("You deposited :" + value + " to your checking account");
		}
	}
	
	public boolean withdrawalFromChecking(double value) 
	{
		
		if(!checkingAccountCreated) 
		{
			System.out.println("You don't have a checking account");
			return false;
		}
		
		else if(value < 0) 
		{
			System.out.println("You can't withdrawal a negative value");
			return false;
		}
	
		else if((this.checkingAccountValue - value) < 0) 
		{
			return false;
			//System.out.println("You can't withdrawal :" + value +" becuase you only have: " + this.checkingAccountValue );
		}
		else 
		{
			this.checkingAccountValue -= value;
			return true;		
		}
		
	}
	
	public boolean withdrawalFromSavings(double value) 
	{
		//boolean returnBool;
		if(!savingAccountCreated) 
		{
			System.out.println("You don't have a savings account");
			return false;
		}
		
		else if(value < 0) 
		{
			System.out.println("You can't withdrawal a negative value");
			return false;
		}
		
		else if((this.savingAccountValue - value) < 0) 
		{
			System.out.println("You can't withdrawal that much from your savings");
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
	
	public void depositToChecking(double value){
		
		this.checkingAccountValue += value;
		
	}
	
	public void depositToSavings(double value){
		
		this.savingAccountValue += value;
		
	}
	// Region getters and setters
public double getCheckingAccountValue() 
{
		return checkingAccountValue;
	}

public double getSavingAccountValue() 
{
		return savingAccountValue;
	}
	
public boolean getCheckingAccountCreated() 
{
		return checkingAccountCreated;
	}
	
public boolean getSavingAccountCreated() 
{
		return savingAccountCreated;
	}

	// EndRegion

}
