package it.corsojava.bookstore.persistence.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.corsojava.bookstore.beans.Prodotto;
import it.corsojava.bookstore.exceptions.ProdottoDaoException;
import it.corsojava.bookstore.exceptions.UtenteDaoException;
import it.corsojava.bookstore.persistence.DbConfig;
import it.corsojava.bookstore.persistence.DbTools;
import it.corsojava.bookstore.persistence.dao.ProdottoDao;

public class ProdottoDaoImpl extends BasicDaoImpl implements ProdottoDao{

	public ProdottoDaoImpl(DbConfig connector) {
		super(connector);
	}

	@Override
	public List<Prodotto> getProdotti() throws ProdottoDaoException {
		try (Connection cn= getConnector().getConnection()) {
			Statement st=cn.createStatement();
			String sql="SELECT * FROM prodotti";
			ResultSet rs = st.executeQuery(sql);
			List<Prodotto> prodotti=new ArrayList<Prodotto>();
			while(rs.next()) {
				Prodotto p =new Prodotto();
				p.setIdProdotto(rs.getInt("idProdotto"));
				p.setTitolo(rs.getString("titolo"));
				p.setSottotitolo(rs.getString("sottotitolo"));
				p.setDescrizione(rs.getString("descrizione"));
				p.setPrezzo(rs.getBigDecimal("prezzo"));
				p.setAliquota(rs.getBigDecimal("aliquota"));
				p.setNomeFile(rs.getString("nomeFile"));
				prodotti.add(p);
			}
			return prodotti;
		}catch(SQLException sqle) {
			throw new ProdottoDaoException("Si e' verificato un problema nel recuperare i prodotti dal database",sqle);
		}
	}

	@Override
	public Prodotto getProdottoById(int idProdotto) throws ProdottoDaoException{
		try (Connection cn= getConnector().getConnection()) {
			String sql="SELECT * FROM prodotti WHERE idProdotto=?";
			PreparedStatement st=cn.prepareStatement(sql);
			st.setInt(1, idProdotto);
			ResultSet rs = st.executeQuery();
			
			if(rs.next()) {
				Prodotto p =new Prodotto();
				p.setIdProdotto(rs.getInt("idProdotto"));
				p.setTitolo(rs.getString("titolo"));
				p.setSottotitolo(rs.getString("sottotitolo"));
				p.setDescrizione(rs.getString("descrizione"));
				p.setPrezzo(rs.getBigDecimal("prezzo"));
				p.setAliquota(rs.getBigDecimal("aliquota"));
				p.setNomeFile(rs.getString("nomeFile"));
				return p;
			}else {
				return null;
			}
		}catch(SQLException sqle) {
			throw new ProdottoDaoException("Si e' verificato un problema nel recuperare i prodotti dal database",sqle);
		}
	}

	@Override
	public Prodotto add(Prodotto newProduct) throws ProdottoDaoException {
		if(newProduct==null) return null;
		if(newProduct.getIdProdotto()>0)
			throw new ProdottoDaoException("Esiste gia' un prodotto con id "+newProduct.getIdProdotto());
		
		try (Connection cn= getConnector().getConnection()) {
			StringBuilder sql=new StringBuilder();
			sql.append("INSERT INTO prodotti (idProdotto,titolo,sottotitolo,");
			sql.append("descrizione,prezzo,aliquota,nomeFile) VALUES (?,?,?,?,?,?,?)");
			PreparedStatement st=cn.prepareStatement(sql.toString());
			int nextId=-1;
			try{
				nextId = DbTools.getNextIntId(cn , "prodotti", "idProdotto");
			}catch( SQLException sqle){
				throw new ProdottoDaoException("Unable to save utente",sqle);
			}
			
			st.setInt(1, nextId);
			st.setString(2, newProduct.getTitolo());
			st.setString(3, newProduct.getSottotitolo());
			st.setString(4, newProduct.getDescrizione());
			st.setBigDecimal(5, newProduct.getPrezzo());
			st.setBigDecimal(6, newProduct.getAliquota());
			st.setString(7, newProduct.getNomeFile());
			int rowsAffected = st.executeUpdate();
			if(rowsAffected>0) {
				return getProdottoById(nextId);
			}else {
				return null;
			}
		}catch(SQLException sqle) {
			throw new ProdottoDaoException("Si e' verificato un problema nella registrazione di un nuovo prodotto nel database",sqle);
		}
		
	}

	public void update(Prodotto p) throws ProdottoDaoException{
		if(p==null) 
			throw new ProdottoDaoException("Il prodotto passato come argomento non e' valorizzato");
		if(p.getIdProdotto()<1)
			throw new ProdottoDaoException("Non esiste alcun prodotto con id "+p.getIdProdotto());
		
		try (Connection cn= getConnector().getConnection()) {
			StringBuilder sql=new StringBuilder();
			sql.append("UPDATE prodotti set ");
			sql.append("titolo=?, ");
			sql.append("sottotitolo=?, ");
			sql.append("descrizione=?, ");
			sql.append("prezzo=?, ");
			sql.append("aliquota=?, ");
			sql.append("nomeFile=? ");
			sql.append("where prodotti.idProdotto=? ");
			
			PreparedStatement st=cn.prepareStatement(sql.toString());
			st.setString(1, p.getTitolo());
			st.setString(2, p.getSottotitolo());
			st.setString(3, p.getDescrizione());
			st.setBigDecimal(4, p.getPrezzo());
			st.setBigDecimal(5, p.getAliquota());
			st.setString(6, p.getNomeFile());
			st.setInt(7, p.getIdProdotto());
			int rowsAffected = st.executeUpdate();
			if(rowsAffected<1){
				throw new ProdottoDaoException("Nessun prodotto e' stato aggiornato");
			}
		}catch(SQLException sqle) {
			throw new ProdottoDaoException("Si e' verificato un problema nell'aggiornamento del prodotto sul database",sqle);
		}		
	}
	
	public void delete(Prodotto p) throws ProdottoDaoException {
		if(p==null) 
			throw new ProdottoDaoException("Il prodotto passato come argomento non e' valorizzato");
		if(p.getIdProdotto()<1)
			throw new ProdottoDaoException("Non esiste alcun prodotto con id "+p.getIdProdotto());
		
		try (Connection cn= getConnector().getConnection()) {
			StringBuilder sql=new StringBuilder();
			sql.append("DELETE FROM prodotti ");
			sql.append("where prodotti.idProdotto=? ");
			
			PreparedStatement st=cn.prepareStatement(sql.toString());
			st.setInt(1, p.getIdProdotto());
			int rowsAffected = st.executeUpdate();
			if(rowsAffected<1){
				throw new ProdottoDaoException("Nessun prodotto e' stato eliminato");
			}
		}catch(SQLException sqle) {
			throw new ProdottoDaoException("Si e' verificato un problema nell'eliminazione del prodotto dal database",sqle);
		}
	}
}
