package project0;

public class User {
	
	private long userID;
	private String userName;
	private String password;
	private String address;
	private String first_name;
	private String last_name;
	private long phoneNumber;
	
	public User() {
		
	}

	public User(long userID,String userName, String password, String first_name, String last_name, String address, long phoneNumber) 
	{
		this.userID = userID;
		this.userName = userName;
		this.password = password;
		this.first_name = first_name;
		this.last_name = last_name;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}
	
	//Region getter and setters
	
	public long getUserID() 
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	//EndRegion
	
}
