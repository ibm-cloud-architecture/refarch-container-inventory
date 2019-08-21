package ibm.gse.kc.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ibm.gse.kc.model.Container;
import ibm.gse.service.ApplicationException;

public class ContainerRepository implements ContainerDAO {
	private static final Logger logger = LoggerFactory.getLogger(ContainerRepository.class);


	protected Connection conn;
	protected String SQL_INSERT = "INSERT INTO CONTAINERS (CONTAINERID,TYPE,STATUS,BRAND,CAPACITY) VALUES (?,?,?,?,?)";
	protected DBConfiguration config;
	
	public ContainerRepository() {
		
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		config=  DBConfiguration.instance();
	    try {
			conn = DriverManager.getConnection(config.getUrl(), config.getDbuser(), config.getDbpwd());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public Collection<Container> getContainers() {
		Collection<Container> l = new ArrayList<Container>();
		try {
	      Statement st = conn.createStatement();
	      ResultSet rs = st.executeQuery("SELECT * FROM CONTAINERS");
	      while (rs.next())
	      { 
	    	  Container c = new Container(rs.getString("containerid"),		  
	    		  rs.getString("brand"),
	    		  rs.getString("type"),
	    		  rs.getInt("capacity"));
	    	c.setStatus(rs.getString("status"));
	       logger.info(c.toString());
	        l.add(c);
	      }
		} catch (Exception e) {
	    	 return l;
	     }
		return l;
	}

	@Override
	public Container save(Container c) throws ApplicationException  {
		if (c.getContainerID() == null) {
			c.setContainerID(UUID.randomUUID().toString().substring(0, 19));
		}
		try {
			Statement st = conn.createStatement();
			PreparedStatement psInsert = conn.prepareStatement(SQL_INSERT);
			psInsert.setString(1, c.getContainerID());
			psInsert.setString(2, c.getType());
			psInsert.setString(3, c.getStatus());
			psInsert.setString(4, c.getBrand());
			psInsert.setInt(5, c.getCapacity());
			psInsert.execute();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 
		return c;
	}

	@Override
	public Container update(Container c) throws ApplicationException  {
		try {
			Statement st = conn.createStatement();
			st.executeUpdate("UPDATE CONTAINERS WHERE \"containerid\" = '" 
			+ c.getContainerID() + "' VALUES(" 
			+ c.getContainerID() + ","
			+ c.getType() + ","
			+ c.getStatus() + ","
			+ c.getBrand() + ","
			+ c.getCapacity() + ")"
					);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		 
		return c;
	}

	@Override
	public Container getById(String containerID) {
		try {
		      Statement st = conn.createStatement();
		      ResultSet rs = st.executeQuery("SELECT * FROM CONTAINERS WHERE \"CONTAINERID\" = '" + containerID + "'");
		      while (rs.next())
		      { 
		    	  Container c = new Container(rs.getString("containerid"),		  
		    		  rs.getString("brand"),
		    		  rs.getString("type"),
		    		  rs.getInt("capacity"));
		    	c.setStatus(rs.getString("status"));
		        System.out.println(c.toString());
		        return c;
		      }
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
		return null;
	}


	@Override
	public void stop() {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void delete(String cid) {
		try {
		      Statement st = conn.createStatement();
		      st.executeUpdate("DELETE FROM CONTAINERS WHERE \"CONTAINERID\" = '" 
		  			+ cid + "'" );
		} catch (SQLException e) {
			e.printStackTrace();
			
		}
	}

}
