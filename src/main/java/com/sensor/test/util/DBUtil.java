package com.sensor.test.util;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBUtil {

	private BasicDataSource ds;

	private static DBUtil instance = new DBUtil();

	private DBUtil() {
//
//		Logger logger = Logger.getLogger(DBUtil.class.getName());
//		logger.log(Level.INFO, "DBUtil c'tor");

		Context initContext;

		try {
			initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:comp/env");
			ds = (BasicDataSource) envContext.lookup("jdbc/sensor_data");
			if (ds == null) {
//				logger.log(Level.SEVERE, "ds lookup returned null <--------------");
			}
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static DBUtil getInstance() {
//		Logger logger = Logger.getLogger(DBUtil.class.getName());
//		logger.log(Level.INFO, "DBUtil getInstance");
		if (instance == null) {
//			logger.log(Level.INFO, "creating instance");
			instance = new DBUtil();
		}
		return instance;
	}

	public static Connection getConnection() {
//		Logger logger = Logger.getLogger(DBUtil.class.getName());
//		logger.log(Level.INFO, "getConnection");
		try {
			return DBUtil.getInstance().ds.getConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static void closeConnection(Connection toBeClosed) {
		if (toBeClosed == null)
			return;

		try {
			toBeClosed.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
