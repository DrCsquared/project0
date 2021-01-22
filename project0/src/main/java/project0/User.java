package project0;

public class User {
	
	private long userID;
	private String userName;
	private String password;
	private String first_name;
	private String last_name;
	private long phoneNumber;
	
	public User() {
		super();
	}

	public User(long userID,String userName, String password ) 
	{
		super();
		this.userID = userID;
		this.userName = userName;
		this.password = password;
	}
}
