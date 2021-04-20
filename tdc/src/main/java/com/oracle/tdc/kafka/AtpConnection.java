package com.oracle.tdc.kafka;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.oracle.tdc.msg.Customer;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;


public class AtpConnection
{
	final static String DB_URL="jdbc:oracle:thin:@tdcdb_high";
	final static String DB_USER = "TDC";
	final static String DB_PASSWORD = "##SRcrise2020";


	private PreparedStatement statement;
	private boolean estadoOperacao;

	public String insert(Customer cus) throws SQLException, ClassNotFoundException {
		
		estadoOperacao = false;

		System.setProperty("oracle.net.tns_admin","C:\\oci\\Wallet_TDCDB");
		System.setProperty("oracle.jdbc.fanEnabled","false");

		Class.forName("oracle.jdbc.driver.OracleDriver");
		OracleDataSource ods = new OracleDataSource();
		ods.setURL(DB_URL);
		ods.setUser(DB_USER);
		ods.setPassword(DB_PASSWORD);

		// With AutoCloseable, the connection is closed automatically.
		OracleConnection connection = (OracleConnection)
				ods.getConnection();
		// Get the JDBC driver name and version
		DatabaseMetaData dbmd = connection.getMetaData();
		System.out.println("Driver Name: " + dbmd.getDriverName());
		System.out.println("Driver Version: " +
				dbmd.getDriverVersion());
		System.out.println("Database Username is: " +
				connection.getUserName());



		try {
			PreparedStatement stmt = connection.prepareStatement("INSERT INTO CUSTOMER(NAME, AGE, CPF, STATUS) VALUES(?, ?, ?, ?)");

				stmt.setString(1, cus.getName());
				stmt.setInt(2, cus.getAge());
				stmt.setString(3, cus.getCpf());
				stmt.setString(4, cus.getStatus());
				stmt.execute();
				System.out.println("Record inserted");
			stmt.close();
			connection.close();
		}
		catch(Exception e){
			System.err.println(e);
			e.printStackTrace();
		}
	
		return "Sucesso!";
	}
}



