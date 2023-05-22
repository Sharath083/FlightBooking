package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import Dao.AllEmployeeDao;
import Dao.DataBaseUtil;
import entity.Employee;

public class Delete extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		AllEmployeeDao dao=new AllEmployeeDao();
		ServletContext sc=request.getServletContext();
		DataBaseUtil baseUtil=(DataBaseUtil) sc.getAttribute("sqlEmployee");
		Connection connection=baseUtil.getConnector();

		Gson gson=new Gson();
		Employee emp=gson.fromJson(request.getReader(), Employee.class);
		PrintWriter out = response.getWriter();

		if(dao.present(emp.getEmployeeId(), connection)) {
			dao.deleteDetails(emp.getEmployeeId(), connection);
			out.println("Details of ID "+emp.getEmployeeId()+" are deleted");


		}
		else {
			out.println("ID "+emp.getEmployeeId()+" is not present in database to delete");
		
		}
		out.close();
//		baseUtil.dbClose();
		ServletContext scd=request.getServletContext();
		DataBaseUtil baseUtil1= (DataBaseUtil) scd.getAttribute("closeConnection");
		baseUtil1.dbClose();

		
		



	}

}














//package controller;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import com.employee.methods.AllEmployeeDao;
//import com.employee.methods.Employee;
//import com.google.gson.Gson;
//
//
//public class Delete extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//       
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		AllEmployeeDao dao = new AllEmployeeDao();
//		Gson gson=new Gson();
//		Employee emp=gson.fromJson(request.getReader(), Employee.class);
//		PrintWriter out = response.getWriter();
//
//		if(dao.present(emp.getEmployeeId())) {
//			dao.deleteDetails(emp.getEmployeeId());
//			out.println("Details of ID "+emp.getEmployeeId()+" are deleted");
//			DisplayAll d=new DisplayAll();
//			d.doPost(request, response);
//
//		}
//		else {
//			out.println("ID "+emp.getEmployeeId()+" is not present in database to delete");
//		
//		}
//		out.close();
//
//		
//		
//
//
//
//	}
//
//}