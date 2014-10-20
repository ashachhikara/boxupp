package com.paxcel.trial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;


public class HSQLTrial {
	public static void main(String args[]){
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver" );
			Connection c = DriverManager.getConnection("jdbc:hsqldb:file:D:\\opt\\db\\testdb", "SA", "");
			
			ResultSet rs = c.createStatement().executeQuery("SELECT * from BOOKMARKS");
			while(rs.next()){
				System.out.println(rs.getString("title"));
			}
			
			} catch (Exception e) {
			System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
			e.printStackTrace();
			return;
			}
		
		
	}
}
