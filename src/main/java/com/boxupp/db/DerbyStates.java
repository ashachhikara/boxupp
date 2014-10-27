package com.boxupp.db;

import java.sql.SQLException;

public class DerbyStates {
	public static boolean tableAlreadyExists(SQLException e){
		return e.getSQLState().equalsIgnoreCase("X0Y32");
	}
}
