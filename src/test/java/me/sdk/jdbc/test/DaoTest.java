package me.sdk.jdbc.test;

import java.io.File;

import org.apache.log4j.Logger;

import me.sdk.jdbc.dao.DAO;

public class DaoTest {
	
	static Logger log = Logger.getLogger(DaoTest.class);
	
	private static DAO dao;
	
	static {
		try {
			dao = new DAO();
			dao.registeConnectionPool(new File(DaoTest.class.getClassLoader()
					.getResource("test.proxool.xml").getPath()));
			log.info("加载test.proxool.xml");
		} catch (Exception e) {
			log.error("加载test.proxool.xml"+e);
			e.printStackTrace();
		}
	}
	
	public static DAO getDao() {
		return dao;
	}

	

	public static void main(String[] args) throws Exception {
		try {
			if(DaoTest.getDao().isTableExist("test", "dual")){
				System.out.println("true");
			}else{
				System.out.println("false");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		DaoTest.getDao().clear();
		System.exit(0);
	}
	
}
