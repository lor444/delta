package it.corsojava.bookstore.persistence.impl;

import it.corsojava.bookstore.persistence.DbConfig;

public abstract class BasicDaoImpl {
	
	private DbConfig connector;
	
	public BasicDaoImpl(DbConfig connector) {
		this.connector=connector;
	}
		
	public DbConfig getConnector() {
		return this.connector;
	}
	

}
