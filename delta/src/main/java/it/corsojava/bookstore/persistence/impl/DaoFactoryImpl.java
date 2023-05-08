package it.corsojava.bookstore.persistence.impl;

import it.corsojava.bookstore.persistence.DbConfig;
import it.corsojava.bookstore.persistence.dao.DaoFactory;
import it.corsojava.bookstore.persistence.dao.ProdottoDao;
import it.corsojava.bookstore.persistence.dao.UtenteDao;

public class DaoFactoryImpl implements DaoFactory {

	private DbConfig connector;
	
	public DaoFactoryImpl(DbConfig connector) {
		this.connector=connector;
	}

	@Override
	public ProdottoDao createProdottoDao() {
		return new ProdottoDaoImpl(connector);
	}

	@Override
	public UtenteDao createUtenteDao() {
		return new UtenteDaoImpl(connector);
	}

}
