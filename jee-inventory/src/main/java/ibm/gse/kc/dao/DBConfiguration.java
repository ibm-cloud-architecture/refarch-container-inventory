package ibm.gse.kc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class DBConfiguration {

	protected static DBConfiguration instance;
	protected static String hostname = "localhost";
	protected static String portNumber = "50000";
	protected static String dbname = "INVDB";
	protected static String dbuser = "db2inst1";
	protected static String dbpwd = "db2inst1";
	protected static String url = "jdbc:db2://" + hostname + ":" + portNumber +"/" + dbname;
	
	
	public static synchronized DBConfiguration instance() {
		if (instance == null) {
			instance = new DBConfiguration();
		}
		return instance;
	}
	
	private DBConfiguration() {
		Map<String, String> env = System.getenv();
		if (env.get("DBHOST") != null ) {
			hostname = env.get("DBHOST");
		}
		
		if (env.get("DBPORT") != null ) {
			portNumber = env.get("DBPORT");
		}
		
		if (env.get("DBNAME") != null ) {
			dbname = env.get("DBNAME");
		}
		
		if (env.get("DBUSER") != null ) {
			dbuser = env.get("DBUSER");
		}
		
		if (env.get("DBPWD") != null ) {
			dbpwd = env.get("DBPWD");
		}
	    url = "jdbc:db2://" + hostname + ":" + portNumber +"/" + dbname;
	}
	
	public  boolean validateConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url, dbuser, dbpwd);	
		} catch (SQLException e) {
			return false;
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	
	public String getHostname() {
		return hostname;
	}

	public  String getPortNumber() {
		return portNumber;
	}

	public  String getDbname() {
		return dbname;
	}

	public  String getDbuser() {
		return dbuser;
	}

	public  String getDbpwd() {
		return dbpwd;
	}

	public  String getUrl() {
		return url;
	}
	
}
