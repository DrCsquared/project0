package project0;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class main {
	
	public static char tempChar;
	public static User tempUser;
	public static double tempDouble;
	public static boolean moveOn = true;
	
	public static void main(String[] args) 
	{
		
		boolean moveOn = true;
		
		//startMenu();
		//Scanner input = new Scanner(System.in);
		do {
		
		tempChar = Character.toUpperCase(startMenu());
		switch(tempChar) 
		{ 
		case 'L':
			System.out.println("You started to log in.");
			tempUser = userLogin();
			if(tempUser != null) 
			{
				System.out.println("You've logged in.");
				moveOn = false;
			}
			else 
			{
					System.out.println("Your username and password don't match anything in our database.");
			}
			break;
		case 'C' :
			System.out.println("You started to create an account");
			createUser();
			moveOn = false;
			break;
		case 'E' :
			System.out.println("You exited the code.");
			System.exit(0);
			//moveOn = false;
			break;
		default : 
			System.out.println("You didn't enter a valid input.");	
		}
			
		} while(moveOn);
		
		if(tempChar == 'L') 
		{
			do {
			if(isEmployee(tempUser)) 
			{
				tempChar = Character.toUpperCase(employeeMenu(tempUser));
				switch (tempChar) 
				{
				case 'U' :
					userApprovalMenu();
					moveOn = true;
					break;
				case 'A' :
					accountApprovalMenu();
					moveOn = true;
					break;
				case 'T' :
					System.out.println("These are all the transactions between customers");
					System.out.println();
					getAllTransactions();
					System.out.println();
					moveOn = true;
					break;
				case 'E' :
					System.out.println("You exited the code.");
					System.exit(0);
					break;
				default :
					System.out.println("You didn't enter a valid input.");
					moveOn = true;
					break;
				}
			}
			else 
			{
				tempChar = Character.toUpperCase(customerAccountMenu(tempUser));
				
				switch (tempChar) 
				{
				case 'A' :
					customerAccounts(tempUser);
					moveOn = true;
					break;
				case 'P' :
					customerPendingTransactions(tempUser);
					moveOn = true;
					break;
				case 'T' :
					customerTransfer(tempUser);
					moveOn= true;
					break;
					
				case 'C' :
					tempChar = Character.toUpperCase(createAccountMenu(tempUser));
					
					switch (tempChar) 
					{
					case 'C' :
						System.out.println("Enter an amount you want to start with your Checking Account.");
						tempDouble = ScannerInput.input.nextDouble();
						createCheckingAccountForApproval(tempUser, tempDouble);
						tempChar = 'L';
						moveOn = true;
						break;
					case 'S' :
						System.out.println("Enter an amount you want to start with your Savings Account.");
						tempDouble = ScannerInput.input.nextDouble();
						createSavingAccountForApproval(tempUser, tempDouble);
						tempChar = 'L';
						moveOn = true;
						break;
					case 'B' :
						moveOn = true;
						break;
					default :
						System.out.println("You didn't enter a valid input.");
						moveOn = true;
						tempChar = 'L';
						break;
						
					}
					
					break;
				case 'E' :
					System.out.println("You exited the code.");
					System.exit(0);
					break;
				default :
					System.out.println("You didn't enter a valid input.");
					moveOn = true;
					break;
				}
			}
			//customerAccountMenu(tempUser,'L');
			//checkIfEmployee();
		}while(moveOn);
		
		if(tempChar == 'C') 
		{
			
		}
	}
}
	
	public static void getAllTransactions() 
	{
		try(Connection connection = 
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
		
		PreparedStatement statement = connection.prepareStatement("select * from all_transactions");
		
		
		ResultSet resultSet = statement.executeQuery();
		if(!resultSet.next())
		{
			System.out.println("There are all the transactions at this time.");
			
		}
		else 
		{
			do 
			{	System.out.print("Transaction Number: " + resultSet.getInt("transaction_ID") + " - ");
				System.out.print("From User: " + resultSet.getInt("from_user") + " - ");
				System.out.print("To User: " + resultSet.getInt("to_user") +" - ");
				System.out.print("From Account: " + resultSet.getString("account_type_from") + " - ");
				System.out.print("To Account: " + resultSet.getString("account_type_to") +" - ");
				System.out.println("Amount: " + resultSet.getDouble("amount"));
				
			}while(resultSet.next());
		}
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void customerTransfer(User user)
	{
		char tempChar;
		char fromAccount;
		char toAccount;
		int tempID = user.getUserID();
		int tempID2;
		boolean tempBool = true;
		boolean internalTransferBool = true;
		boolean haveCheckingAccount = true;
		boolean haveSavingsAccount = true;
		double savingsAccountValue = -1.0;
		double checkingAccountValue = -1.0;
		double tempDouble = 0;
		int tempInt = 0;
		boolean moveOn = false;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
		do {
		System.out.println("(I)nternal transfer.");
		System.out.println("(E)xternal transfer.");
		System.out.println("(B)ack to customer menu.");
		
		tempChar = ScannerInput.input.next().charAt(0);
		tempChar = Character.toUpperCase(tempChar);
		//haveCheckingAccount = true;
		//haveCheckingAccount = false;
		switch(tempChar) 
		{
		case 'I' :
		{
			PreparedStatement statement = connection.prepareStatement("select * from checking_accounts where user_id = ?");
			statement.setInt(1, tempID);
		
			ResultSet resultSet = statement.executeQuery();
			
			if(!resultSet.next()) 
			{
				haveCheckingAccount = false;
			}
			else
			{
					checkingAccountValue = resultSet.getDouble("account_value");
					System.out.println("Checking Account balance: " + checkingAccountValue);
					System.out.println();
			}
			
			PreparedStatement statement2 = connection.prepareStatement("select * from saving_accounts where user_id = ?");
			statement2.setInt(1, tempID);
		
			ResultSet resultSet2 = statement2.executeQuery();
			
			if(!resultSet2.next()) 
			{
				haveSavingsAccount = false;
			}
			else
			{
				savingsAccountValue = resultSet2.getDouble("account_value");
					System.out.println("Savings Account balance: " + savingsAccountValue);
					System.out.println();
			}
			
			if(!haveSavingsAccount || !haveCheckingAccount ) 
			{
				if(!haveSavingsAccount) 
				{
					System.out.println("You only have a Checking Account.");
				}
				else
					System.out.println("You only have a Savings Account");
			}
			else 
			{
			
			do 
			{
			{
			   System.out.println("Select account from: (C) or (S)");
			
				tempChar = ScannerInput.input.next().charAt(0);
				
				
				switch(Character.toUpperCase(tempChar)) 
				{
				case 'C' :
					do {
					System.out.println("How much do you want to transfer to Savings from Checking?");
					{
						tempDouble = ScannerInput.input.nextDouble();
						if(tempDouble > checkingAccountValue) 
						{
							System.out.println("You can't transfer that much from your Checking?");
						}
						
						else if(tempDouble <= 0) 
						{
							System.out.println("You can't transfer negative from your Checking?");
						}
						else 
						{
							addToSavingsAccount(user, tempDouble, savingsAccountValue);
							//addToCheckingAccount(user,tempDouble);
							deductFromCheckingAccount( user ,tempDouble , checkingAccountValue);
							haveCheckingAccount = false;
							internalTransferBool = false;
							
							PreparedStatement iCtoS = connection.prepareStatement("insert into self_transfers (user_id, account_to, account_from, amount) values(?,?,?,?)");
							iCtoS.setInt(1, tempID);
							iCtoS.setString(2, "S");
							iCtoS.setString(3, "C");
							iCtoS.setDouble(4, tempDouble);
							
							iCtoS.executeUpdate();
						}
					}
					}while(haveCheckingAccount);
					break;
					
				case 'S' :
					do {
						System.out.println("How much do you want to transfer to Checking from Savings?");
						{
							tempDouble = ScannerInput.input.nextDouble();
							if(tempDouble > savingsAccountValue) 
							{
								System.out.println("You can't transfer that much from your Savings?");
							}
							else if(tempDouble <= 0) 
							{
								System.out.println("You can't transfer negative from your Savings?");
							}
							else 
							{
								tempDouble = tempDouble + checkingAccountValue;
								addToCheckingAccount(user, tempDouble, checkingAccountValue);
								//addToCheckingAccount(user,tempDouble);
								deductFromSavingsAccount( user ,tempDouble, savingsAccountValue);
								haveCheckingAccount = false;
								internalTransferBool = false;
								

								PreparedStatement iCtoS2 = connection.prepareStatement("insert into self_transfers (user_id, account_to, account_from, amount) values(?,?,?,?)");
								iCtoS2.setInt(1, tempID);
								iCtoS2.setString(2, "C");
								iCtoS2.setString(3, "S");
								iCtoS2.setDouble(4, tempDouble);
								
								iCtoS2.executeUpdate();
							}
						}
						}while(haveCheckingAccount);
						break;
				default :
					System.out.println("You didn't enter a valid input.");
					internalTransferBool = true;
					break;
				}
			}
			}while(internalTransferBool);
			}
		}
			break;
			
		case 'E' :
		{
			System.out.println("Which Account do you want to transfer from (C) or (S)?");

			tempChar = ScannerInput.input.next().charAt(0);
			
			switch(Character.toUpperCase(tempChar)) 
			{
			
			case 'C' :
				
				PreparedStatement statement = connection.prepareStatement("select * from checking_accounts where user_id = ?");
				statement.setInt(1, tempID);
			
				ResultSet resultSet = statement.executeQuery();
				
				if(!resultSet.next()) 
				{
					haveCheckingAccount = false;
				}
				else
				{
						checkingAccountValue = resultSet.getDouble("account_value");
						System.out.println("Checking Account balance: " + checkingAccountValue);
						System.out.println();
				}
				
				if(!haveCheckingAccount) 
				{
					System.out.println("You don't have a Checking Account");
				}
				else 
				{
					System.out.println("Enter the user_id number you which to transfter to");
					fromAccount= 'C';
					tempInt = ScannerInput.input.nextInt();
					
					tempBool = checkIfUserExists(tempInt);
					
					
					if(tempBool) 
					{
						
						if(checkIfUserHasCheckingAccount(tempInt) && checkIfUserHasSavingsAccount(tempInt)) 
						{
							do {
							System.out.println("Do you want to transfer to there (C) or (S)?");
								 tempChar = ScannerInput.input.next().charAt(0);
								 switch(Character.toUpperCase(tempChar)) 
								{
								 case 'C' :
								 
									 do {
									 System.out.println("How much do you want to transfer to user " + tempInt +" from your Checking?");
										
											tempDouble = ScannerInput.input.nextDouble();
											if(tempDouble > checkingAccountValue) 
											{
												System.out.println("You can't transfer that much from your Checking?");
											}
											
											else if(tempDouble <= 0) 
											{
												System.out.println("You can't transfer negative from your Checking?");
											}
											else 
											{
												sendTransactionToPending(tempID, tempInt, fromAccount, tempChar, tempDouble);
												tempBool = false;
											}
											}while(tempBool);
									
									 break;
								 case 'S' :
									 
									 do {
										 System.out.println("How much do you want to transfer to user " + tempInt +" to your Checking?");
											
												tempDouble = ScannerInput.input.nextDouble();
												if(tempDouble > checkingAccountValue) 
												{
													System.out.println("You can't transfer that much from your Checking?");
												}
												
												else if(tempDouble <= 0) 
												{
													System.out.println("You can't transfer negative from your Checking?");
												}
												else 
												{
													sendTransactionToPending(tempID, tempInt, fromAccount, tempChar, tempDouble);
													tempBool = false;
												}
												}while(tempBool);
									 break;
									 
								default :
									System.out.println("You didn't enter a valid input.");
									tempBool = true;
									break;
									}
								 
							}
							while(tempBool);
									
							
						}
						else if(checkIfUserHasSavingsAccount(tempInt) && !checkIfUserHasCheckingAccount(tempInt)) {
							do {
						
							System.out.println("User only has a (S)");
							System.out.println("How much do you want to transfer to user " + tempInt +" from your Checking?");
							
							tempDouble = ScannerInput.input.nextDouble();
							if(tempDouble > checkingAccountValue) 
							{
								System.out.println("You can't transfer that much from your Checking?");
							}
							
							else if(tempDouble <= 0) 
							{
								System.out.println("You can't transfer negative from your Checking?");
							}
							else 
							{
								sendTransactionToPending(tempID, tempInt, fromAccount, tempChar, tempDouble);
							}
							}while(tempBool);
						
							
						}
						else if(checkIfUserHasCheckingAccount(tempInt) && !checkIfUserHasSavingsAccount(tempInt)) {
							System.out.println("User only has a (C)");
							
							 do {
								 System.out.println("How much do you want to transfer to user " + tempInt +" from your Checking?");
									
										tempDouble = ScannerInput.input.nextDouble();
										if(tempDouble > checkingAccountValue) 
										{
											System.out.println("You can't transfer that much from your Checking?");
										}
										
										else if(tempDouble <= 0) 
										{
											System.out.println("You can't transfer negative from your Checking?");
										}
										else 
										{
											sendTransactionToPending(tempID, tempInt, fromAccount, tempChar, tempDouble);
											tempBool = false;
										}
										}while(tempBool);
						}
					}
					else 
					{
							System.out.println("User doesn't have accounts with this bank.");
					}
					
					
						
				}
				break;
				
			case'S' :
				
				PreparedStatement statement2 = connection.prepareStatement("select * from checking_accounts where user_id = ?");
				statement2.setInt(1, tempID);
			
				ResultSet resultSet2 = statement2.executeQuery();
				
				if(!resultSet2.next()) 
				{
					haveCheckingAccount = false;
				}
				else
				{
						checkingAccountValue = resultSet2.getDouble("account_value");
						System.out.println("Checking Account balance: " + checkingAccountValue);
						System.out.println();
				}
				
				if(!haveCheckingAccount) 
				{
					System.out.println("You don't have a Checking Account");
				}
				else 
				{
					System.out.println("Enter the user_id number you which to transfter to");
					fromAccount= 'S';
					tempInt = ScannerInput.input.nextInt();
					
					tempBool = checkIfUserExists(tempInt);
					
					
					if(tempBool) 
					{
						
						if(checkIfUserHasCheckingAccount(tempInt) && checkIfUserHasSavingsAccount(tempInt)) 
						{
							do {
							System.out.println("Do you want to transfer to there (C) or (S)?");
								 tempChar = ScannerInput.input.next().charAt(0);
								 switch(Character.toUpperCase(tempChar)) 
								{
								 case 'C' :
								 
									 do {
									 System.out.println("How much do you want to transfer to user " + tempInt +" from your Savings?");
										
											tempDouble = ScannerInput.input.nextDouble();
											if(tempDouble > checkingAccountValue) 
											{
												System.out.println("You can't transfer that much from your Savings?");
											}
											
											else if(tempDouble <= 0) 
											{
												System.out.println("You can't transfer negative from your Savings?");
											}
											else 
											{
												sendTransactionToPending(tempID, tempInt, fromAccount, tempChar, tempDouble);
												tempBool = false;
											}
											}while(tempBool);
									
									 break;
								 case 'S' :
									 
									 do {
										 System.out.println("How much do you want to transfer to user " + tempInt +" to your Savings?");
											
												tempDouble = ScannerInput.input.nextDouble();
												if(tempDouble > checkingAccountValue) 
												{
													System.out.println("You can't transfer that much from your Savings?");
												}
												
												else if(tempDouble <= 0) 
												{
													System.out.println("You can't transfer negative from your Savings?");
												}
												else 
												{
													sendTransactionToPending(tempID, tempInt, fromAccount, tempChar, tempDouble);
													tempBool = false;
												}
												}while(tempBool);
									 break;
									 
								default :
									System.out.println("You didn't enter a valid input.");
									tempBool = true;
									break;
									}
								 
							}
							while(tempBool);
									
							
						}
						else if(checkIfUserHasSavingsAccount(tempInt) && !checkIfUserHasCheckingAccount(tempInt)) {
							do {
						
							System.out.println("User only has a (S)");
							tempChar = 'S';
							System.out.println("How much do you want to transfer to user " + tempInt +" from your savings?");
							
							tempDouble = ScannerInput.input.nextDouble();
							if(tempDouble > checkingAccountValue) 
							{
								System.out.println("You can't transfer that much from your Savings?");
							}
							
							else if(tempDouble <= 0) 
							{
								System.out.println("You can't transfer negative from your Savings?");
							}
							else 
							{
								sendTransactionToPending(tempID, tempInt, fromAccount, tempChar, tempDouble);
							}
							}while(tempBool);
						
							
						}
						else if(checkIfUserHasCheckingAccount(tempInt) && !checkIfUserHasSavingsAccount(tempInt)) {
							System.out.println("User only has a (C)");
							tempChar = 'C';
							 do {
								 System.out.println("How much do you want to transfer to user " + tempInt +" from your Savings?");
									
										tempDouble = ScannerInput.input.nextDouble();
										if(tempDouble > checkingAccountValue) 
										{
											System.out.println("You can't transfer that much from your Savings?");
										}
										
										else if(tempDouble <= 0) 
										{
											System.out.println("You can't transfer negative from your Savings?");
										}
										else 
										{
											sendTransactionToPending(tempID, tempInt, fromAccount, tempChar, tempDouble);
											tempBool = false;
										}
										}while(tempBool);
						}
					}
					else 
					{
							System.out.println("User doesn't have accounts with this bank.");
					}
					
					
						
				}
				break;
			
				
			}
			break;
		}	
		case 'B' :
			moveOn = true;
	       break;
	    default :
	    	System.out.println("You didn't enter a valid input.");
	    	break;
		}
		
		}while(!moveOn);
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
	
	}
	
	public static void sendTransactionToPending(int from_user_id, int to_user_id, char from_account_type, char to_account_type,double amount)
	{
		String typeString_to = Character.toString(to_account_type);
		String typeString_from = Character.toString(from_account_type);
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
		PreparedStatement statement3 = connection.prepareStatement("insert into pending_transactions (from_user, to_user ,account_type_from, account_type_to, amount) values (?,?,?,?,?)");
		
		statement3.setInt(1,from_user_id);
		statement3.setInt(2, to_user_id);
		statement3.setString(3, typeString_from);
		statement3.setString(4, typeString_to);
		statement3.setDouble(5, amount);
	
		statement3.executeUpdate();
		
		System.out.println("You have sent your transaction to be accepted by user " + to_user_id);
		System.out.println();
		
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public static boolean checkIfUserExists(int user_id) 
	{
		
		boolean tempBool = true;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
			
			PreparedStatement statement = connection.prepareStatement("select * from users where user_id = ?");
			statement.setInt(1, user_id);
			
			ResultSet resultSet = statement.executeQuery();
			
			if(!resultSet.next()) 
			{
				tempBool = false;
			}
			else
			{
				
				tempBool = true;
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
			return tempBool;
	}
	
	public static boolean checkIfUserHasCheckingAccount(int user_id) 
	{
		
		boolean tempBool = true;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
			
			PreparedStatement statement = connection.prepareStatement("select * from checking_accounts where user_id = ?");
			statement.setInt(1, user_id);
			
			ResultSet resultSet = statement.executeQuery();
			
			if(!resultSet.next()) 
			{
				tempBool = false;
			}
			else
			{
				tempBool = true;
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
			return tempBool;
		
	}
	public static boolean checkIfUserHasSavingsAccount(int user_id) 
	{
		
		boolean tempBool = true;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
			
			PreparedStatement statement = connection.prepareStatement("select * from saving_accounts where user_id = ?");
			statement.setInt(1, user_id);
			
			ResultSet resultSet = statement.executeQuery();
			
			if(!resultSet.next()) 
			{
				tempBool = false;
			}
			else
			{
				tempBool = true;
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
			return tempBool;
	}
	public static void addToCheckingAccount(User user, double addValue, double checkingAccountValue)
	{
		int tempID = user.getUserID();
		double tempValue = addValue + checkingAccountValue;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
		PreparedStatement statement = connection.prepareStatement("update checking_accounts set account_value = ? where user_id = ? ");
		
		
		statement.setDouble(1, tempValue);
		statement.setInt(2, tempID);
		
		statement.executeUpdate();
		
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void addToCheckingAccount(int user, double addValue)
	{
		double tempValue;
		double regularValue = 0;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
			
			PreparedStatement statement2 = connection.prepareStatement("Select * from checking_accounts where user_id =?");
			statement2.setInt(1,user);
			
			ResultSet resultSet = statement2.executeQuery();
			
			
				regularValue = resultSet.getDouble("account_value");
			
			tempValue = regularValue + addValue;
				
			
		PreparedStatement statement = connection.prepareStatement("update checking_accounts set account_value = ? where user_id = ? ");
		statement.setDouble(1, tempValue);
		statement.setInt(2, user);
		
		statement.executeUpdate();
		
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public static void deductFromCheckingAccount(User user, double deductValue, double checkingAccountValue) 
	{
		
		int tempID = user.getUserID();
		double tempValue = checkingAccountValue - deductValue;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
		PreparedStatement statement = connection.prepareStatement("Update checking_accounts set account_value = ? where user_id = ? ");
		
		statement.setDouble(1, tempValue);
		statement.setInt(2, tempID);
		
		statement.executeUpdate();
		
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void deductFromCheckingAccount(int user, double deductValue) 
	{
		
		double tempValue;
		double regularValue = 0;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
			
			PreparedStatement statement2 = connection.prepareStatement("Select * from checking_accounts where user_id =?");
			statement2.setInt(1,user);
			
			ResultSet resultSet = statement2.executeQuery();
			
			regularValue = resultSet.getDouble("account_value");
				
			tempValue = regularValue - deductValue;
		PreparedStatement statement = connection.prepareStatement("Update checking_accounts set account_value = ? where user_id = ? ");
		
		statement.setDouble(1, tempValue);
		statement.setInt(2, user);
		
		statement.executeUpdate();
		
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	public static void addToSavingsAccount(User user, double addValue, double savingsAccountValue) 
	{
		
		int tempID = user.getUserID();
		double tempValue = addValue + savingsAccountValue;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
		PreparedStatement statement = connection.prepareStatement("Update saving_accounts set account_value = ? where user_id = ? ");
		
		statement.setDouble(1, tempValue);
		statement.setInt(2, tempID);
		
		statement.executeUpdate();
		
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void addToSavingsAccount(int user, double addValue) 
	{
		
		int tempID = user;
		double tempValue = 0;
		double regularValue = 0;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
			
			PreparedStatement statement2 = connection.prepareStatement("Select account_value from saving_accounts where user_id =?");
			statement2.setInt(1,user);
			
			ResultSet resultSet = statement2.executeQuery();
			
			while(resultSet.next()){
		
			regularValue = resultSet.getDouble("account_value");
			
			tempValue = regularValue + addValue;
			}
			
		PreparedStatement statement = connection.prepareStatement("Update saving_accounts set account_value = ? where user_id = ? ");
		
		statement.setDouble(1, tempValue);
		statement.setInt(2, user);
		
		statement.executeUpdate();
		
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	public static void deductFromSavingsAccount(User user, double deductValue, double savingsAccountValue) 
	{
		int tempID = user.getUserID();
		double tempValue = savingsAccountValue - deductValue;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
		PreparedStatement statement = connection.prepareStatement("Update saving_accounts set account_value = ? where user_id = ? ");
		
		statement.setDouble(1, tempValue);
		statement.setInt(2, tempID);
		
		statement.executeUpdate();
		
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void deductFromSavingsAccount(int user, double deductValue) 
	{
		int tempID = user;
		double tempValue =0;
		double regularValue = 0;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")){
			
			PreparedStatement statement2 = connection.prepareStatement("Select account_value from saving_accounts where user_id =?");
			statement2.setInt(1,user);
			
			ResultSet resultSet = statement2.executeQuery();
			
			while(resultSet.next()){
			regularValue = resultSet.getDouble("account_value");
			
			tempValue = regularValue - deductValue;
			}
			
		PreparedStatement statement = connection.prepareStatement("Update saving_accounts set account_value = ? where user_id = ? ");
		
		statement.setDouble(1, tempValue);
		statement.setInt(2, tempID);
		
		statement.executeUpdate();
		
		
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
		
	public static char createAccountMenu(User user) 
	{
		char tempChar;
		System.out.println("What type of account do you want to create");
		System.out.println("(C)hecking account.");
		System.out.println("(S)aving account.");
		System.out.println("(B)ack to customer menu.");
		
		tempChar = ScannerInput.input.next().charAt(0);
		
		return tempChar;
		
	}
	
	public static void customerAccounts(User user) 
	{
		boolean noCheckingAccount = false;
		boolean noSavingsAccount = false;
		boolean noPendingAccounts = false;
		boolean firstRunThru = true;
		
		int tempID = (int) user.getUserID();
		
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
			
			PreparedStatement statement = connection.prepareStatement("select * from checking_accounts where user_id = ?");
			statement.setInt(1, tempID);
		
			ResultSet resultSet = statement.executeQuery();
			
			if(!resultSet.next()) 
			{
				noCheckingAccount = true;
			}
			else
			{
					System.out.println("Checking Account balance: " + resultSet.getDouble("account_value"));
					System.out.println();
			}
			
			PreparedStatement statement2 = connection.prepareStatement("select * from saving_accounts where user_id = ?");
			statement2.setInt(1, tempID);
		
			ResultSet resultSet2 = statement2.executeQuery();
			
			if(!resultSet2.next()) 
			{	
				noSavingsAccount = true;
			}
			else 
			{
					System.out.println("Savings Account  balance: " + resultSet2.getDouble("account_value"));
					System.out.println();
			}
			
			PreparedStatement statement3 = connection.prepareStatement("select * from pending_accounts where user_id = ?");
			statement3.setInt(1, tempID);
		
			ResultSet resultSet3 = statement3.executeQuery();
			
			if(!resultSet3.next()) 
			{
				noPendingAccounts = true;
			}
			else 
			{
				do {
				
					if(firstRunThru) 
					{
					System.out.println("Below are the Pending Accounts");
					System.out.println();
					firstRunThru = false;
					}
					
					System.out.print("Account Type : " + resultSet3.getString("account_type") +" - ");
					System.out.println("Balance: " + resultSet3.getDouble("account_value"));
					
				}while(resultSet3.next());
			}
			if(noPendingAccounts && noCheckingAccount &&  noSavingsAccount) 
			{
				System.out.println("You don't have any accounts or pending account in the database.");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public static void accountApprovalMenu() 
	{
		boolean tempCheck = true;
		int tempID;
		
		int tempUserID = 0;
		double tempDouble = 0.0;
		String tempAT;
		String tempChar;
		
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
			
			PreparedStatement statement = connection.prepareStatement("select * from pending_accounts");
			
			
			ResultSet resultSet = statement.executeQuery();
			if(!resultSet.next())
			{
				System.out.println("There are no saving or checking accounts pending at this time.");
			}
			else 
			{
				do { 
				
					//System.out.print(resultSet.getInt(1) +" - ");
					System.out.print("User ID: " + resultSet.getInt("user_id") + " - ");
					System.out.print("Account type: " + resultSet.getString("account_type") +" - ");
					System.out.println("Amount: " + resultSet.getDouble("account_value") +" - ");
				}while(resultSet.next());
				
				do 
					{
				
						PreparedStatement statement2 = connection.prepareStatement("select * from pending_accounts where user_id = ? and account_type = ?");
						System.out.println("Enter the account you wish to approve by user_id.");
						tempID = ScannerInput.input.nextInt();
						statement2.setInt(1, tempID);
						System.out.println("Enter the account type you wish to aprove (C) or (S).");
						tempChar = ScannerInput.input.next();
						statement2.setString(2,tempChar);
			
						//statement.setString(2, tempPassword);
						//statement2.executeUpdate();
						ResultSet resultSet2 = statement2.executeQuery();
			
			if(!resultSet2.next())
			{
				System.out.println("You entered a user_id or account_type that isn't in the pending_accounts database");
				tempCheck = false;
			}
			
			else
			{
					tempUserID = resultSet2.getInt("user_id");
					tempAT = resultSet2.getString("account_type");
					tempDouble = resultSet2.getDouble("account_value");
				
				tempCheck = false;
				if(tempAT.equalsIgnoreCase("C"))
				{
					
				PreparedStatement statement3 = connection.prepareStatement("insert into checking_accounts (user_id, account_value) VALUES (?,?)");
				
				
				statement3.setInt(1, tempUserID);
				statement3.setDouble(2, tempDouble);
				
				statement3.executeUpdate();
				
				System.out.println("You've added \n user_ID " + tempUserID + "to the checking_accounts table with the amount of " + tempDouble);
				
				PreparedStatement statement4 = connection.prepareStatement("delete from pending_accounts where user_id = ? and account_type = ?");
					statement4.setInt(1,tempID);
					statement4.setString(2, tempAT);
					statement4.executeUpdate();
					
				}
				else
				{
					PreparedStatement statement3 = connection.prepareStatement("insert into saving_accounts (user_id, account_value) VALUES (?,?)");
					
					statement3.setInt(1, tempUserID);
					statement3.setDouble(2, tempDouble);
					
					statement3.executeUpdate();
					
					System.out.println("You've added \n user_ID " + tempUserID + "to the savings_accounts table with the amount of" + tempDouble);
					
					PreparedStatement statement4 = connection.prepareStatement("delete from pending_accounts where user_id = ? and account_type = ?");
						statement4.setInt(1,tempID);
						statement4.setString(2,tempAT);
						statement4.executeUpdate();
				}
			}
			}while(tempCheck);
			
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void userApprovalMenu() 
	{
		boolean tempCheck = true;
		int tempID;
		
		String tempUsername = null;
		String tempPassword = null;
		String tempFirstName = null;
		String tempLastName = null;
		try(Connection connection =
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
			
			PreparedStatement statement = connection.prepareStatement("select * from pending_users");
			
			
			ResultSet resultSet = statement.executeQuery();
			if(!resultSet.next())
			{
				System.out.println("There are no user accounts that are pending.");
			}
			else 
			{
			
			while(resultSet.next()) 
			{ 
			  System.out.print(resultSet.getInt(1) +" - ");
			  System.out.print(resultSet.getString("username") + " - ");
			  System.out.print(resultSet.getString("first_name") +" - "); 
			  System.out.println(resultSet.getString("last_name") +" - ");
			}	
				resultSet = null;
			do 
			{
				
			PreparedStatement statement2 = connection.prepareStatement("select * from pending_users where user_id = ?");
			System.out.println("Enter the account you wish to approve by user_id.");
			tempID = ScannerInput.input.nextInt();
			statement2.setInt(1, tempID);
			//statement.setString(2, tempPassword);
			//statement2.executeUpdate();
			ResultSet resultSet2 = statement2.executeQuery();
			
			if(!resultSet2.next())
			{
				System.out.println("You entered a user_id that isn't in pending accounts database");
				tempCheck = true;
			}
			
			else
			{
				
				while(resultSet2.next()) {
					
					tempUsername = resultSet2.getString("username");
					tempPassword = resultSet2.getString("password");
					tempFirstName = resultSet2.getString("first_name");
					tempLastName = resultSet2.getString("last_name");
					
				}
				tempCheck = false;
				//System.out.println("Getting to put the pending_user into the users table?");
				PreparedStatement statement3 = connection.prepareStatement("insert into users (username, password, first_name, last_name) VALUES (?,?,?,?)");
				
				
				statement3.setString(1, tempUsername);
				statement3.setString(2, tempPassword);
				statement3.setString(3, tempFirstName);
				statement3.setString(4, tempLastName);
				
				statement3.executeUpdate();
				
				System.out.println("You've added " + tempFirstName + " " + tempLastName + " to the database");
				
				PreparedStatement statement4 = connection.prepareStatement("delete from pending_users where user_id = ?");
					statement4.setInt(1,tempID);
					statement4.executeUpdate();
					
			}
			}while(tempCheck);
			
			}
		}catch(SQLException e) 
		{
			e.printStackTrace();
		}
	}
	public static char startMenu() 
	{
		char tempChar;
		
		System.out.println("Welcome to Revature Bank.");
		System.out.println("(L)ogin");
		System.out.println("(C)reate Account");
		System.out.println("(E)xit");
		
		tempChar = ScannerInput.input.next().charAt(0);
		//System.out.println(tempChar);
		//tempCharStartMenu = (char) tempString.indexOf(0);
		return tempChar;
	}
	
	public static char customerAccountMenu(User user) 
	{
		char tempChar;
		System.out.println("Welcome customer " + user.getFirst_name() + " " + user.getLast_name());
		System.out.println("(A)ccounts associated with this account");
		System.out.println("(C)reate account");
		System.out.println("(P)ending Transactions");
		System.out.println("(T)ransfer");
		System.out.println("(E)xit");
		
		tempChar = ScannerInput.input.next().charAt(0);
		
		return tempChar;
	}
	
	public static void customerPendingTransactions(User user) 
	{
			boolean doneWithTransactions = false;
			int transactionValue;
			int sendFromUser;
			int sendToUser;
			String accountTypeFrom;
			String accountTypeTo;
			double amount;
			int tempID = (int)user.getUserID();
			
			try(Connection connection = 
					DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
				
				PreparedStatement statement = connection.prepareStatement("select * from pending_transactions where to_user=?");
				
				statement.setInt(1, tempID);
				
				ResultSet resultSet = statement.executeQuery();
				if(!resultSet.next())
				{
					System.out.println("There are no pending transactions at this time.");
					doneWithTransactions = true;
					
				}
				else 
				{
					do 
					{	System.out.print("Transaction Number: " + resultSet.getInt("pending_ID") + " - ");
						System.out.print("From User: " + resultSet.getInt("from_user") + " - ");
						System.out.print("To User: " + resultSet.getInt("to_user") +" - ");
						System.out.print("From Account: " + resultSet.getString("account_type_from") + " - ");
						System.out.print("To Account: " + resultSet.getString("account_type_to") +" - ");
						System.out.println("Amount: " + resultSet.getDouble("amount"));
						
					}while(resultSet.next());
				}
				if(!doneWithTransactions) {
				
				do {
					System.out.println("Which transaction do you want to approve? (Enter the Transaction Number)");
						transactionValue = ScannerInput.input.nextInt();
					
					PreparedStatement statement2 = connection.prepareStatement("select * from pending_transactions where pending_ID= ? and to_user = ?");
						statement2.setInt(1, transactionValue);
						statement2.setInt(2, tempID);
						
						ResultSet resultSet2 = statement.executeQuery();
						if(!resultSet2.next()) 
						{
							System.out.println("You entered a wrong transaction number please try again");
							
						}
						else
						{	
								 	sendFromUser = resultSet2.getInt("from_user");
									sendToUser = resultSet2.getInt("to_user");
									accountTypeFrom = resultSet2.getString("account_type_from");
									accountTypeTo = resultSet2.getString("account_type_to");
									amount = resultSet2.getDouble("amount");
									
									if(accountTypeFrom == "C") 
									{
										if(accountTypeTo == "C") 
										{
											deductFromCheckingAccount( sendFromUser, amount);
											addToCheckingAccount(sendToUser, amount);
											
										}
										else 
										{
											deductFromCheckingAccount( sendFromUser, amount);
											addToSavingsAccount(sendToUser, amount);
										}	
									}
									else 
									{
										if(accountTypeTo == "C") 
										{
											deductFromSavingsAccount( sendFromUser, amount);
											addToCheckingAccount(sendToUser, amount);
											
										}
										else 
										{
											deductFromSavingsAccount( sendFromUser, amount);
											addToSavingsAccount(sendToUser, amount);
										}	
										
									}
									
									PreparedStatement statement4 = connection.prepareStatement("insert into all_transactions (from_user, to_user, account_type_from, account_type_to,amount) values (?,?,?,?,?)");
									statement4.setInt(1,sendFromUser);
									statement4.setInt(2,sendToUser);
									statement4.setString(3,accountTypeFrom);
									statement4.setString(4,accountTypeTo);
									statement4.setDouble(5, amount);
									statement4.executeUpdate();
									
									PreparedStatement statement3 = connection.prepareStatement("delete from pending_transactions where from_user = ? and to_user = ? and account_type_from = ? and account_type_to = ? and amount = ?");
									statement3.setInt(1,sendFromUser);
									statement3.setInt(2,sendToUser);
									statement3.setString(3,accountTypeFrom);
									statement3.setString(4,accountTypeTo);
									statement3.setDouble(5, amount);
									statement3.executeUpdate();
							
							doneWithTransactions = true;
								
						}
						
				}while(!doneWithTransactions);
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
	}
	
	public static char employeeMenu(User user) 
	{
		char tempChar;
		System.out.println("Welcome Employee " + user.getFirst_name() + " " + user.getLast_name());
		System.out.println("(U)ser Approval");
		System.out.println("(A)ccount Apporval");
		System.out.println("(T)ransfer review");
		System.out.println("(E)xit");
		
		tempChar = ScannerInput.input.next().charAt(0);
		
		return tempChar;
	}
	
	public static User userLogin() 
	{
		User tempUser = null;
		String tempName;
		String tempPassword;
		
		System.out.println("Enter Username");
		tempName = ScannerInput.input.next();
		System.out.println("Enter Password");
		tempPassword = ScannerInput.input.next();
		
		
		try(Connection connection = 
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
			
			PreparedStatement statement = connection.prepareStatement("select * from users where username=? and password = ?");
			
			statement.setString(1,tempName);
			statement.setString(2, tempPassword);
				
			ResultSet resultSet = statement.executeQuery();
			
			//if(!resultSet.next())
			//{
				
			//}
			//else {
				
			while(resultSet.next()) 
			{
				tempUser = new User(resultSet.getInt("user_id"),resultSet.getString("username"),
						resultSet.getString("password"),resultSet.getString("first_name"), resultSet.getString("last_name"));	
			}
			//}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return tempUser;
	}
	
	public static void createUser() 
	{
		String tempUsername;
		String tempPassword;
		String tempFirstName;
		String tempLastName;
		
		
		
		try(Connection connection = 
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
			
			PreparedStatement statement = connection.prepareStatement("insert into pending_users (username, password, first_name, last_name) VALUES (?,?,?,?)");
			
			
			System.out.println("Enter Username");
			tempUsername = ScannerInput.input.next();
			statement.setString(1, tempUsername);
			
			System.out.println("Enter Password");
			tempPassword = ScannerInput.input.next();
			statement.setString(2, tempPassword);
			
			System.out.println("Enter first name");
			tempFirstName = ScannerInput.input.next();
			statement.setString(3, tempFirstName);
			
			System.out.println("Enter last name"); 
			tempLastName = ScannerInput.input.next();
			statement.setString(4, tempLastName);
			
			
			statement.executeUpdate();
			
			System.out.println("User has been created. Waiting on approval.");
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void createCheckingAccountForApproval(User user , double value)
	{
		boolean tempBool = false;
		User tempUser = user;
		String tempAccount = "C";
		int tempID = user.getUserID();
		
		if(value > 0) 
		{
		try(Connection connection = 
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
			
			PreparedStatement statement = connection.prepareStatement("select * from checking_accounts where user_id=?");
			
			statement.setInt(1,tempID);
			
			ResultSet resultSet = statement.executeQuery();
			
			if(resultSet.next())
			{
				System.out.println("You already have a checking account.");
			}
			else
			{	
					PreparedStatement statement2 = connection.prepareStatement("select * from pending_accounts where user_id=? and accout_type = ?");
					
					tempID = (int)user.getUserID();
					statement2.setInt(1,tempID);		
					statement2.setString(2, tempAccount);
					ResultSet resultSet2 = statement.executeQuery();
					
					if(resultSet2.next())
					{
						System.out.println("You already have a checking account pending.");
						
					}
					else {
				PreparedStatement statement3 = connection.prepareStatement("insert into pending_accounts (user_id, account_type , account_value) values (?,?,?)");
				
				tempID = (int)user.getUserID();
				statement3.setInt(1,tempID);
				statement3.setString(2, tempAccount);
				statement3.setDouble(3, value);
			
				statement3.executeUpdate();
				
				System.out.println("You have applied for a checking account with the balance of : " + value);
				System.out.println();
				
					}
				
			}
			
			}catch(SQLException e) 
					{
					e.printStackTrace();
					}
		}
		else {
			System.out.println("You can't hava a account value 0 or lower.");
			tempBool = false;
		}
		
	}
	
	public static void createSavingAccountForApproval(User user, double value) 
	{
		User tempUser = user;
		String tempAccount = "S";
		int tempID;
		
		if(value > 0) {
		try(Connection connection = 
				DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
			
			PreparedStatement statement = connection.prepareStatement("select * from saving_accounts where user_id=?");
			tempID = (int)user.getUserID();
			statement.setInt(1,tempID);				
			ResultSet resultSet = statement.executeQuery();
			
			if(resultSet.next())
			{
				System.out.println("You already have a savings account.");
				
			}
					
			else
			{
				
				PreparedStatement statement2 = connection.prepareStatement("select * from pending_accounts where user_id=? and accout_type = ?");
				
				tempID = (int)user.getUserID();
				statement2.setInt(1,tempID);	
				statement2.setString(2, tempAccount);
				ResultSet resultSet2 = statement.executeQuery();
				
				if(resultSet2.next())
				{
					System.out.println("You already have a savings account pending.");
					
				}
				else 
				{
				PreparedStatement statement3 = connection.prepareStatement("insert into pending_accounts (user_id, account_type , account_value) values (?,?,?)");
				
				tempID = (int)user.getUserID();
				statement3.setInt(1,tempID);
				statement3.setString(2, tempAccount);
				statement3.setDouble(3, value);
			
				statement3.executeUpdate();
				
				System.out.println("You have applied for a saving account with the balance of : " + value);
				System.out.println();
			}
				
			}
			
			}catch(SQLException e) 
					{
					e.printStackTrace();
					}
		}
		else {
			System.out.println("You can't hava a account value 0 or lower.");
		}
	}
	
	
public static boolean isEmployee(User user)
{
	boolean tempCheck = false;
	int tempID;
	try(Connection connection = 
			DriverManager.getConnection("jdbc:postgresql://localhost:5432/projectZero", "postgres", "2724Colin")) {
		
		PreparedStatement statement = connection.prepareStatement("select * from employee_list where user_id = ?");
		
		tempID = (int)user.getUserID();
		statement.setInt(1, tempID);
		//statement.setString(2, tempPassword);
			
		ResultSet resultSet = statement.executeQuery();
		
		if(!resultSet.next())
		{
			tempCheck = false;
		}
		
		else {
			tempCheck = true;
		}
	}catch(SQLException e) {
		e.printStackTrace();
	}
	
	return tempCheck;
	
}
}
