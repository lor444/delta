package it.corsojava.bookstore.persistence.dao;

import it.corsojava.bookstore.beans.Utente;
import it.corsojava.bookstore.exceptions.UtenteDaoException;

public interface UtenteDao {

	public Utente getByLogin(String userName, String userPassword) throws UtenteDaoException;
	public void addUtente(Utente u) throws UtenteDaoException;
	public void updateUtente(Utente u) throws UtenteDaoException;
		
}
