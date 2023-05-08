package it.corsojava.bookstore.persistence.impl;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.corsojava.bookstore.beans.Utente;
import it.corsojava.bookstore.exceptions.UtenteDaoException;
import it.corsojava.bookstore.exceptions.UtenteDaoValidationException;
import it.corsojava.bookstore.persistence.DbConfig;
import it.corsojava.bookstore.persistence.DbTools;
import it.corsojava.bookstore.persistence.dao.UtenteDao;

/**
 * A causa del fatto che il campo password viene crittografato prima di venire salvato sul database
 * Nelle operazioni di lettura l'oggetto Utente viene caricato dal Database senza leggere il valore 
 * della password, e quindi il metodo "getUserPass" restituisce "null" per impostazione di default.
 * Nelle operazioni di "update" il campo viene ignorato a meno che non sia valorizzato. 
 * Poichè una sua valorizzazione può dipendere solo dalla esigenza di "cambiare" la password, nelle 
 * operaizoni di uptate la password viene aggiornata (con crittografia) solo se impostata
 * ad un valore diverso da null
 *  *
 */
public class UtenteDaoImpl extends BasicDaoImpl implements UtenteDao {

	public UtenteDaoImpl(DbConfig connector) {
		super(connector);
	}

	public void validateUtente(Utente u) throws UtenteDaoValidationException {
		if(u==null)
			throw new UtenteDaoValidationException("L'oggetot Utente non e' impostato (null)");
		if(u.getUsername()==null || u.getUsername().length()<1)
			throw new UtenteDaoValidationException("Username e' un'informazione obbligatoria");
		if(u.getProvincia()!=null && u.getProvincia().length()!=2)
			throw new UtenteDaoValidationException("La provincia va indicata con due caratteri");
	}

	/** 
	 * Warn: the password passed as argument is the unencrypted version of the password.
	 * To be compared with the db password it must be encrypted using same algorithm
	 */
	@Override
	public Utente getByLogin(String userName, String userPassword) throws UtenteDaoException {
		try(Connection conn = getConnector().getConnection()) {
			String sql ="SELECT * FROM utenti WHERE username=?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, userName);
			ResultSet rs = stmt.executeQuery();
			if(rs.next()) {
				String pass = rs.getString("userpass");
				String comparePass=DbTools.getHashed(userPassword, DbTools.SALT_BASIC);
				if(pass.compareTo(comparePass)==0){
					return getFromResultSet(rs);
				}
			}
		}catch(SQLException ex) {
			throw new UtenteDaoException("Impossibile effettuare il login.",ex);
		} catch (NoSuchAlgorithmException|InvalidKeySpecException e) {
			throw new UtenteDaoException("Impossibile effettuare il login. Impossibile confrontare la password crittografata.",e);
		}
		return null;
	}

	/**
	 * Registra un nuovo utente
	 */
	@Override
	public void addUtente(Utente u) throws UtenteDaoException {
		validateUtente(u);
		if(u.getIdUtente()>0)
			throw new UtenteDaoException("L'utente da creare esiste gia'");
		
		try(Connection conn = getConnector().getConnection()) {
			StringBuilder sql=new StringBuilder();
			sql.append("INSERT INTO utenti (idUtente, cognome,nome, ");
			sql.append("codiceFiscale,partitaIva, mail, indirizzo, ");
			sql.append("cap,citta,provincia, nazione, telefono , ");
			sql.append("username, userpass,privacy) VALUES (" );
			sql.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			PreparedStatement pst=conn.prepareStatement(sql.toString());
			int nextId=-1;
			try{
				nextId = DbTools.getNextIntId(conn , "utenti", "idUtente");
			}catch( SQLException sqle){
				throw new UtenteDaoException("Unable to save utente",sqle);
			}
			pst.setInt(1, nextId);
			pst.setString(2, u.getCognome());
			pst.setString(3, u.getNome());
			pst.setString(4, u.getCodiceFiscale());
			pst.setString(5, u.getPartitaIva());
			pst.setString(6, u.getMail());
			pst.setString(7, u.getIndirizzo());
			pst.setString(8, u.getCap());
			pst.setString(9, u.getCitta());
			pst.setString(10, u.getProvincia());
			pst.setString(11, u.getNazione());
			pst.setString(12, u.getTelefono());
			pst.setString(13, u.getUsername());
			String encPasswd=DbTools.getHashed(u.getUserPass(), DbTools.SALT_BASIC) ;
			pst.setString(14, encPasswd);
			pst.setInt(15, u.getPrivacy());
	
			if(pst.executeUpdate()<1) {
				throw new UtenteDaoException("Impossibile rendere persistente il profilo utente");
			}
		}	catch(SQLException sqle) {
			throw new UtenteDaoException("Impossibile registrare il profilo utente sul database",sqle);
		}	catch(Exception ex) {
			throw new UtenteDaoException("Impossibile salvare il profilo utente",ex);
		}
	}
	
	@Override
	public void updateUtente(Utente u) throws UtenteDaoException {
		validateUtente(u);
		if(u.getIdUtente()<=0)
			throw new UtenteDaoException("Impossibile aggiornare i dati di un utente non ancora registrato");
		try(Connection conn = getConnector().getConnection()) {
			StringBuilder sql=new StringBuilder();
			sql.append("UPDATE utenti set cognome=? ");
			sql.append(", nome=? ");
			sql.append(", codiceFiscale=? ");
			sql.append(", partitaIva=? ");
			sql.append(", mail=? ");
			sql.append(", indirizzo=? ");
			sql.append(", cap=? ");
			sql.append(", citta=? ");
			sql.append(", provincia=? ");
			sql.append(", nazione=? ");
			sql.append(", telefono=? ");
			sql.append(", username=? ");
			sql.append(", privacy=? ");
			if(u.getUserPass()!=null)
				sql.append(", password=? ");
			sql.append("WHERE idUtente=?");
			PreparedStatement pst=conn.prepareStatement(sql.toString());
			
			pst.setString(1, u.getCognome());
			pst.setString(2, u.getNome());
			pst.setString(3, u.getCodiceFiscale());
			pst.setString(4, u.getPartitaIva());
			pst.setString(5, u.getMail());
			pst.setString(6, u.getIndirizzo());
			pst.setString(7, u.getCap());
			pst.setString(8, u.getCitta());
			pst.setString(9, u.getProvincia());
			pst.setString(10, u.getNazione());
			pst.setString(11, u.getTelefono());
			pst.setString(12, u.getUsername());
			pst.setInt(13, u.getPrivacy());
			if(u.getUserPass()!=null) {
				String encPasswd=DbTools.getHashed(u.getUserPass(), DbTools.SALT_BASIC) ;
				pst.setString(14, encPasswd);
				pst.setInt(15, u.getIdUtente());
			}else {
				pst.setInt(14, u.getIdUtente());
			}
			
			if(!pst.execute()) 
				throw new UtenteDaoException("Impossibile salvare le modifiche all'utente");
			
		}catch(SQLException sqle) {
			throw new UtenteDaoException("Impossibile registrare le modifiche all'utente sul database",sqle);
		}	catch(Exception ex) {
			throw new UtenteDaoException("Impossibile salvare le modifiche ai dati utente",ex);
		}
	}

	/**
	 * Carica un oggetto Utente da un ResultSet
	 * @param rs
	 * @return
	 * @throws UtenteDaoException
	 */
	private Utente getFromResultSet(ResultSet rs) throws UtenteDaoException {
		try {
			Utente u = new Utente();
			u.setIdUtente(rs.getInt("idUtente"));
			u.setCognome(rs.getString("cognome"));
			u.setNome(rs.getString("nome"));
			u.setCap(rs.getString("cap"));
			u.setCitta(rs.getString("citta"));
			u.setCodiceFiscale(rs.getString("codiceFiscale"));
			u.setIndirizzo(rs.getString("indirizzo"));
			u.setMail(rs.getString("mail"));
			u.setNazione(rs.getString("nazione"));
			u.setPartitaIva(rs.getString("partitaIva"));
			u.setPrivacy(rs.getInt("privacy"));
			u.setProvincia(rs.getString("provincia"));
			u.setTelefono(rs.getString("telefono"));
			u.setUsername(rs.getString("userName"));
			u.setUserPass(null); // si omette password. Vedi dichiaraizone classe
			return u;
		}catch(SQLException ex) {
			throw new UtenteDaoException("Impossibile caricare i dati dell'utente dal database",ex);
		}
	}
	
}
