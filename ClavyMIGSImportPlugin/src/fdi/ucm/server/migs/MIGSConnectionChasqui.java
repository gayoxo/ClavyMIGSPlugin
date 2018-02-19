package fdi.ucm.server.migs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Clase que define la conexion a una base de datos mySQL para chasqui.
 * @author Joaquin Gayoso-Cabada
 *
 */
public class MIGSConnectionChasqui {
	
	
	public enum DB {DBaseServer,DBaseLocal};
	
	private MIGSConnectionChasqui instance;
	private Connection conexion;
	private static String DBaseServerUnknow;
	
	private static String DBSelected;
	
	private static final String DriverDatabase="com.mysql.jdbc.Driver";
	private static final String ErrorMySQLConnection="Error en driver de conexion al mySQL";
	private static final String ErrorCOnexionDB="Error en conexion a base de datos";
	private static final String ErrorUpdate="Error ejecutando Update Querry: ";
	private static final String ErrorSelect="Error ejecutando Querry: ";
	


	public MIGSConnectionChasqui(String dbNameIP,String database,int Port, String user, String password) {
		try {
			Class.forName(DriverDatabase);
			InicializacionAnonima(dbNameIP,database,Port,user,password); 
			instance=this;
		} catch (ClassNotFoundException e) {
			System.err.println(ErrorMySQLConnection);
			e.printStackTrace();
		} catch (SQLException e) {
			System.err.println(ErrorCOnexionDB);
			e.printStackTrace();

		}
	}

	private void InicializacionAnonima(String dbNameIP,String database, int port, String user, String password) throws SQLException {
		DBaseServerUnknow="jdbc:mysql://"+dbNameIP+":"+port+"/"+database;
		conexion = DriverManager.getConnection(DBaseServerUnknow, user, password);	
		if (conexion==null) throw new SQLException();
		DBSelected=DBaseServerUnknow;
		
	}
	

	public void RunQuerryUPDATE(String querry)
	{		
		try {
			Statement st = instance.conexion.createStatement();
			st.executeUpdate(querry);
		} catch (SQLException e) {
			System.err.println(ErrorUpdate + querry);
			e.printStackTrace();
		}
	}
	
	public ResultSet RunQuerrySELECT(String querry)
	{		
		try {
			Statement st = instance.conexion.createStatement();
			ResultSet rs = st.executeQuery(querry);
			return rs;
		} catch (SQLException e) {
			System.err.println(ErrorSelect + querry);
			e.printStackTrace();
			return null;
		}
	}
	 
public static String getDBSelected() {
	return DBSelected;
}


}
