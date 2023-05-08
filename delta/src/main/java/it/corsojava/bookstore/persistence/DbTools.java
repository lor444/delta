package it.corsojava.bookstore.persistence;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import it.corsojava.bookstore.persistence.dao.DaoFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

/**
 * Strumenti di utilita' per la gestione delle operazioni di persistenza 
 */
public class DbTools {
			
	public static final String SALT_BASIC="r4a6n3d7o6m4s5a9l0t";
	private static final String ATTR_DAO_FACTORY="DaoFactory";
	
	public static final String PARAM_DB_DRIVER_CLASS="db.driver.class";
	public static final String PARAM_DB_URL="db.url";
	public static final String PARAM_DB_USER="db.user";
	public static final String PARAM_DB_PASS="db.pass";
	public static final String PARAM_DAO_FACTORY_CLASS="dao.factory.class";
	
	private static Object daoFactory=null;
	
	public static DaoFactory getDaoFactory() {
		if(daoFactory==null) {
			InputStream in = DbTools.class.getResourceAsStream("/../web.xml");
			try {
				JAXBContext ctx = JAXBContext.newInstance(WebApp.class);
				Unmarshaller u =ctx.createUnmarshaller();
				WebApp wa=(WebApp)u.unmarshal(in);
				Map<String,String> settings = new HashMap<String,String>();
				for(ContextParam p  : wa.params) {
					settings.put(p.getParamName(),p.getParamValue());
				}
							
				String dbUrl=settings.get(PARAM_DB_URL);
				String dbUser=settings.get(PARAM_DB_USER);
				String dbPass=settings.get(PARAM_DB_PASS);
				String dbDriver=settings.get(PARAM_DB_DRIVER_CLASS);
				try {
					Class.forName(dbDriver);
				} catch (ClassNotFoundException e) {
					// TODO Trasferire su eventuale sistema di log
					e.printStackTrace();
				}
				DbConfig config=new DbConfig(dbUrl,dbUser,dbPass);
				try {
					// caricamento per "reflection"
					String daoFactoryClassName=settings.get(PARAM_DAO_FACTORY_CLASS);
					Class daoFactoryClass=Class.forName(daoFactoryClassName);
					Constructor constructor=daoFactoryClass.getConstructor(DbConfig.class);
					DaoFactory factory = (DaoFactory)constructor.newInstance(config);
					DbTools.daoFactory=factory;
					return factory;
				}catch(Exception ex) {
					ex.printStackTrace();
					return null;
				}
			} catch (JAXBException e) {
				e.printStackTrace();
				return null;
			}
		}else {
			return (DaoFactory)daoFactory;
		}
	}
	
	/**
	 * Imposta (se non ancora fatto) e restituisce la DaoFactory attraverso cui 
	 * creare gli oggetti Dao necessari alla resa persistente delle informazioni
	 * @param request
	 * @return
	 */
	public static DaoFactory getDaoFactory(HttpServletRequest request) {
		Object o = request.getServletContext().getAttribute(ATTR_DAO_FACTORY);
		if(o==null) {
			String dbUrl=request.getServletContext().getInitParameter(PARAM_DB_URL);
			String dbUser=request.getServletContext().getInitParameter(PARAM_DB_USER);
			String dbPass=request.getServletContext().getInitParameter(PARAM_DB_PASS);
			String dbDriver=request.getServletContext().getInitParameter(PARAM_DB_DRIVER_CLASS);
			try {
				Class.forName(dbDriver);
			} catch (ClassNotFoundException e) {
				// TODO Trasferire su eventuale sistema di log
				e.printStackTrace();
			}
			DbConfig config=new DbConfig(dbUrl,dbUser,dbPass);
			try {
				// caricamento per "reflection"
				String daoFactoryClassName=request.getServletContext().getInitParameter(PARAM_DAO_FACTORY_CLASS);
				Class daoFactoryClass=Class.forName(daoFactoryClassName);
				Constructor constructor=daoFactoryClass.getConstructor(DbConfig.class);
				DaoFactory factory = (DaoFactory)constructor.newInstance(config);
				request.getServletContext().setAttribute(ATTR_DAO_FACTORY, factory);
				return factory;
			}catch(Exception ex) {
				// TODO Trasferire su eventuale sistema di log
				ex.printStackTrace();
				return null;
			}
		}else {
			return (DaoFactory)o;
		}
	}
	
	/** 
	 * <p>Restituisce una sequenza di caratteri trattati con crittografia hash irreversibile 
	 * basata su algoritmo PBKDF2 con uso di parametro "salt" </p>
	 * @param text Il testo da crittografare
	 * @param salt Il parametro salt di rafforzamento
	 * @return Un oggetto String contenente la sequenza crittografata
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */ 
	public static String getHashed(String text, String salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
    	if(text==null)  
    		throw new IllegalArgumentException("Invalid text");
    	if(salt ==null)
    		throw new IllegalArgumentException("Invalid salt");
    	KeySpec spec = new PBEKeySpec(text.toCharArray(), salt.getBytes(), 65536, 128);
    	SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    	byte[] hash=factory.generateSecret(spec).getEncoded();    	
    	return new String(hash);
    }
	
	/**
	 * Ricerca l'id con valore più alto nella colonna chiave di una tabella
	 * La colonna chiave si presume contenga numeri interi 
	 * @param cn La connessione su cui agire (anche se in transaction)
	 * @param tableName Il nome della tabella di cui ricercare il prossimo id
	 * @param idColumnName Il nome della colonna chiave primaria che contiene gli id
	 * @return Il valore int immediatamente successivo al più alto id presente nella colonna chiave della tabella
	 * @throws SQLException
	 */
	public static int getNextIntId(Connection cn, String tableName, String idColumnName) throws SQLException {
		tableName=tableName.replace("'","''").replace(";","");
		idColumnName=idColumnName.replace("'","''").replace(";","");
		String sql="SELECT MAX("+idColumnName+") as maxId FROM "+tableName;
		Statement ps = cn.createStatement();
		ResultSet rs= ps.executeQuery(sql);
		int currentId=0;
		if(rs.next())
			currentId= rs.getInt(1);
		return (currentId+1);
	}

		
}
