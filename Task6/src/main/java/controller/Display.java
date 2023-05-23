package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.Hashtable;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import Dao.AllEmployeeDao;
import Dao.DataBaseUtil;
import entity.Employee;
public class Display extends HttpServlet {
	private static final long serialVersionUID = 1L;
   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		AllEmployeeDao dao=new AllEmployeeDao();
		
		ServletContext sc=request.getServletContext();
		DataBaseUtil baseUtil=(DataBaseUtil) sc.getAttribute("sqlEmployee");
		Connection connection=baseUtil.getConnector();

		
		if(connection==null) {
			System.out.println("no conne");
		}

		Gson gson = new Gson();
		
		Employee emp = gson.fromJson(request.getReader(), Employee.class);

		Hashtable<Integer,Employee> e = dao.displayJason1(emp.getEmployeeId(),connection);
		String s = gson.toJson(e);
		
		PrintWriter out = response.getWriter();

		if(!e.isEmpty()) {
			try {
				response.setContentType("application/JSON");
				response.setCharacterEncoding("UTF-8");
				out.println(s);
			}
			finally {
				out.close();
			}
		}
		else {
			out.println("The employe with "+emp.getEmployeeId()+" is not present");
			out.close();

		}
//		baseUtil.dbClose();
		ServletContext scd=request.getServletContext();
		DataBaseUtil baseUtil1= (DataBaseUtil) scd.getAttribute("closeConnection");
		baseUtil1.dbClose();


	}


}



