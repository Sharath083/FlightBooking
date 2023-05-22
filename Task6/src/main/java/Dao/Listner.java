package Dao;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

			



public class Listner implements ServletContextListener {

    public void contextInitialized(ServletContextEvent sce)  { 
    	ServletContext sc=sce.getServletContext();
    	DataBaseUtil baseUtil=new DataBaseUtil();
    	sc.setAttribute("sqlEmployee", baseUtil);
    	     		
    }
    public void contextDestroyed(ServletContextEvent sce)  { 
    	ServletContext scd=sce.getServletContext();
    	DataBaseUtil baseUtil=new DataBaseUtil();
    	scd.setAttribute("closeConnection", baseUtil);


   }
	
}
