package com.paxcel.boxupp.db;

import java.sql.SQLException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.boxupp.db.beans.ProjectBean;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class CreateDB {
	
	
	private static Logger logger = LogManager.getLogger(CreateDB.class.getName());
	
	
	public static void main(String args[]) throws ClassNotFoundException, SQLException {
		
			ConnectionSource connectionSource = new JdbcConnectionSource(DerbyConfig.JDBC_URL);
			Dao<ProjectBean, Integer> projectDao = DaoManager.createDao(connectionSource, ProjectBean.class);
//			ProjectBean bean = new ProjectBean("Trial","Akshay");
//			bean.setCreationTime(Date.valueOf("2014-09-17"));
//			projectDao.create(bean);
			connectionSource.close();
			TableUtils.createTable(connectionSource, ProjectBean.class);
			System.out.println(projectDao.queryForAll().size());
		
	}
}
