package com.pknu.bbs.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class OracleDBConnector {
	private static OracleDBConnector oracleDBConnector = new OracleDBConnector();
	Connection con;
	
	private OracleDBConnector(){}
	
	public static OracleDBConnector getInstance(){
		if(oracleDBConnector==null){
			oracleDBConnector = new OracleDBConnector();
		}
		return oracleDBConnector;
	}
	
	public Connection getConnection(){
		try{
			Class.forName("core.log.jdbc.driver.OracleDriver");
			String url="jdbc:oracle:thin:@127.0.0.1:1521:XE";
			con=DriverManager.getConnection(url, "human", "1234");					
		}catch(Exception e){
			e.printStackTrace();
		}
		return con;	
	}	

}
