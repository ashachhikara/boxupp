package com.paxcel.boxupp.db;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.boxupp.db.beans.DefaultConfigurationBean;
import com.boxupp.db.beans.DockerLinkBean;
import com.boxupp.db.beans.ForwardedPortsBean;
import com.boxupp.db.beans.GitRepoBean;
import com.boxupp.db.beans.MachineConfigurationBean;
import com.boxupp.db.beans.MachineProjectMapping;
import com.boxupp.db.beans.ProjectBean;
import com.boxupp.db.beans.ProjectProviderMappingBean;
import com.boxupp.db.beans.ProviderBean;
import com.boxupp.db.beans.PuppetModuleBean;
import com.boxupp.db.beans.ShellScriptBean;
import com.boxupp.db.beans.ShellScriptMachineConfigMapping;
import com.boxupp.db.beans.SyncFoldersBean;
import com.boxupp.db.beans.UserDetailBean;
import com.boxupp.db.beans.UserProjectMapping;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DBConnectionManager {
	private static Logger logger = LogManager.getLogger(DBConnectionManager.class.getName());
	
	private static DBConnectionManager dbManager;
	private static ConnectionSource connectionSource;
	
	public static DBConnectionManager getInstance(){
		if(dbManager == null){
			dbManager = new DBConnectionManager();
		}
		return dbManager;		
	}
	
	public DBConnectionManager(){
		initDBConnection();	
		initDB();		
	}
	
	private void initDBConnection(){
		try {
			connectionSource = new JdbcConnectionSource(DerbyConfig.JDBC_URL);
		} catch (SQLException e) {
			logger.fatal("Error fetching connection from " + DerbyConfig.JDBC_URL + " : "+e.getMessage());
		}
	}
	
	public ConnectionSource fetchDBConnection(){
		return connectionSource;
	}
	
	private boolean initDB(){
		return createDatabases();
	}
	
	private static boolean createDatabases(){
		try {
			
			TableUtils.createTable(connectionSource, ProjectBean.class);
			TableUtils.createTable(connectionSource, ProviderBean.class);
			
			TableUtils.createTable(connectionSource, ProjectProviderMappingBean.class);
			TableUtils.createTable(connectionSource, UserProjectMapping.class);

			TableUtils.createTable(connectionSource, ShellScriptBean.class);
			TableUtils.createTable(connectionSource, ShellScriptMachineConfigMapping.class);
			TableUtils.createTable(connectionSource, PuppetModuleBean.class);
			TableUtils.createTable(connectionSource, ProjectProviderMappingBean.class);
			TableUtils.createTable(connectionSource, MachineConfigurationBean.class);
			TableUtils.createTable(connectionSource, MachineProjectMapping.class);
			
			TableUtils.createTable(connectionSource, UserDetailBean.class);

			TableUtils.createTable(connectionSource, DefaultConfigurationBean.class);
			TableUtils.createTable(connectionSource, GitRepoBean.class);
			TableUtils.createTable(connectionSource, ForwardedPortsBean.class);
			TableUtils.createTable(connectionSource, DockerLinkBean.class);
			
			TableUtils.createTable(connectionSource, SyncFoldersBean.class);
			
			logger.debug("Created tables for mapping");
		} catch (SQLException e) {
			logger.debug("Error creating Tables on the database : "+e.getMessage());
			return false;
		}
		return true;
	}
		
	public boolean releaseConnection(ConnectionSource connection){
		try {
			connection.close();
		} catch (SQLException e) {
			logger.debug("Error releasing the connection");
			return false;
		}
		return true;
	}

}
