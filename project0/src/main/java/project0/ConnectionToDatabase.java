package project0;

import java.sql.*;

public class ConnectionToDatabase {
	
	public ConnectionToDatabase() {
	
	try(final Connection connection =
			DriverManager.getConnection("jdbc:postgresql://localhost:5432/dvdrental", "postgres", "2724Colin")) {

}catch (SQLException e) {
    System.out.println("Connection failure.");
    e.printStackTrace();
}
	}
}

