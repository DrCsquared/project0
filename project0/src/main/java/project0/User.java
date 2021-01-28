package project0;

public class User {
	
	private int userID;
	private String userName;
	private String password;
	private String first_name;
	private String last_name;

	
	public User() {
		
	}

	public User(int userID,String userName, String password, String first_name, String last_name) 
	{
		this.userID = userID;
		this.userName = userName;
		this.password = password;
		this.first_name = first_name;
		this.last_name = last_name;
	}
	
	//Region getter and setters
	
	public int getUserID() 
	{
		return userID;
	}
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	//EndRegion
	
}
