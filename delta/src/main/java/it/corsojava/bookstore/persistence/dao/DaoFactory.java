package it.corsojava.bookstore.persistence.dao;

public interface DaoFactory {
	
	public  ProdottoDao createProdottoDao() ;
	public UtenteDao createUtenteDao();
	
}
