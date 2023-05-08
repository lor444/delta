package it.corsojava.bookstore.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConfig {

	private String dbUrl;
	private String dbUser;
	private String dbPass;
	
	public DbConfig(String dbUrl, String dbUser, String dbPass) {
		super();
		this.dbUrl = dbUrl;
		this.dbUser = dbUser;
		this.dbPass = dbPass;
	}
	public String getDbUrl() {
		return dbUrl;
	}
	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}
	public String getDbUser() { 
		return dbUser;
	}
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	public String getDbPass() {
		return dbPass;
	}
	public void setDbPass(String dbPass) {
		this.dbPass = dbPass;
	} 
	
	public Connection getConnection() throws SQLException {
		Connection cn=DriverManager.getConnection(dbUrl,dbUser,dbPass);
		return cn;
	}
	
}

