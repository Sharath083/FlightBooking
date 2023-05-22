package Dao;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import entity.Employee;



public class AllEmployeeDao {
	

	
	private final String insertQuery ="INSERT INTO new_table(employeeId, salary, employeeName,employeeType) VALUES (?, ?, ?,?)";
	private final String updateQuery="UPDATE new_table SET   employeeName=? ,salary=? , employeeType=? where employeeId=?";
	private final String jasonQuery = "Select * from new_table";
	private final String queryCheck = "Select employeeType from new_table WHERE employeeId = ?";
	
	static Logger log = Logger.getLogger("Connector");
    static FileHandler fh; 
    
    
    public void logMethod() {
		try {
			fh = new FileHandler("D:\\logging.txt");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        log.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();  
        fh.setFormatter(formatter);

    }
    
	public void createDetails(Employee e,Connection dbConnection)   {
		PreparedStatement pstmt=null;

		try {
			pstmt = dbConnection.prepareStatement(insertQuery);
		    pstmt.setInt(1, e.getEmployeeId());
		    pstmt.setInt(2, e.getSalary());
		    pstmt.setString(3, e.getEmployeeName());
		    pstmt.setString(4, e.getEmployeeType());
		    pstmt.execute();		    
		    subTables(e.getEmployeeType(), e.getEmployeeId(),dbConnection);	
		    logMethod();
		    log.info("Details of ID "+e.getEmployeeId()+" are added to database");
		}catch (Exception e1) {
			logMethod();
			log.info("This Employee with ID "+e.getEmployeeId()+ " Already Exist in DataBase");
			System.out.println("Enter New ID");
		}
		finally {
			try {
				pstmt.close();
 
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	
	public void updateDetails(Employee e,Connection dbConnection)    {

		 try{
			String queryChec = "SELECT  employeeType from new_table WHERE employeeId = ?";

			PreparedStatement pst = dbConnection.prepareStatement(queryChec);
			pst.setInt(1, e.getEmployeeId());		
			ResultSet resultSet = pst.executeQuery();
			String typeB = null;
			if(resultSet.next()) {
				typeB=resultSet.getString("employeeType");
			
			System.out.println("Update query");
			PreparedStatement pstmt1 = dbConnection.prepareStatement(updateQuery);
			try {
					
				pstmt1.setString(1, e.getEmployeeName());
				pstmt1.setInt(2, e.getSalary());
				pstmt1.setString(3, e.getEmployeeType());
				pstmt1.setInt(4, e.getEmployeeId());

				pstmt1.execute();
					
				delete(typeB,e.getEmployeeId(),dbConnection);
				String type=e.getEmployeeType();								
				subTables( type,e.getEmployeeId(),dbConnection);

					
					
					
				logMethod();
				log.info("Details of "+e.getEmployeeId()+" are updated in database");
			} 
			catch (Exception e2) {
				System.out.println("in Exception e2");
				System.out.println(e2);
			}
			finally {
				pstmt1.close();
				pst.close();
				resultSet.close();
 

			}
		}
			else {
				logMethod();
				log.info("The id "+e.getEmployeeId()+" you have entered doesnot Exist to update");
			}
		} catch (SQLException e1) {
			System.out.println("in e1");
			System.out.println(e1);
		}										   
	}	
	
	
	public void subTables(String type,int id,Connection dbConnection)  {

		try {
			if(type.equals("parttime")) {

				String partQ="INSERT INTO parttime(employeeId,salary,employeeName)SELECT employeeId,salary, employeeName FROM new_table WHERE employeeId=?";
				PreparedStatement part1=dbConnection.prepareStatement(partQ);
				part1.setInt(1, id);
				part1.execute();
				part1.close();
				System.out.println("Deleted part");
			}
			else if(type.equals("contract")) {
				String conQ="INSERT INTO contract(employeeId,salary,employeeName)SELECT employeeId,salary, employeeName FROM new_table WHERE employeeId=?";
				PreparedStatement con1=dbConnection.prepareStatement(conQ);
				con1.setInt(1, id);
				con1.execute();	
				con1.close();

			}
			else  {
				System.out.println("perm");
				System.out.println("bfhahjf");
				String permQ="INSERT INTO permenant(employeeId,salary,employeeName)SELECT employeeId,salary, employeeName FROM new_table WHERE employeeId=?";
				PreparedStatement per1=dbConnection.prepareStatement(permQ);
				per1.setInt(1,id);
				per1.execute();
				per1.close();
				System.out.println("Deleted perm");

			}					

		} catch (Exception e) {
			System.out.println(e);
				
		}

		


	}
	
	public void delete(String s,int id,Connection dbConnection)  {

		PreparedStatement part=null;
		PreparedStatement con=null;
		PreparedStatement per=null;

		try {
			if(s.equals("parttime")) {
				String ptQuery="DELETE from parttime where employeeId=?";
				part=dbConnection.prepareStatement(ptQuery);
				part.setInt(1, id);
				part.execute();
				part.close();
				System.out.println("Deleted part");
			}
			else if(s.equals("contract")) {
				String cQuery="DELETE from contract where employeeId=?";
				con=dbConnection.prepareStatement(cQuery);
				con.setInt(1, id);
				con.execute();	
				con.close();
				System.out.println("Deleted contract");

			}
			else  {
				String pnQuery="DELETE from permenant where employeeId=?";
				per=dbConnection.prepareStatement(pnQuery);
				per.setInt(1, id);
				per.execute();
				per.close();
				System.out.println("Deleted perm");

			}					

		}catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}

	}
	
	public void deleteDetails(int id,Connection dbConnection)  {

		try {
			System.out.println("in try 1");

			PreparedStatement ps = dbConnection.prepareStatement(queryCheck);
			System.out.println("ps1");
			String deleteQuery="DELETE from new_table where employeeId=?";
			PreparedStatement pstmtDel = dbConnection.prepareStatement(deleteQuery);
			ps.setInt(1, id);
			pstmtDel.setInt(1, id);
			ResultSet resultSet = ps.executeQuery();
			try {
				System.out.println("in try 2");
				String s = null;				
				if(resultSet.next()) {
					s=resultSet.getString("employeeType");
					System.out.println("got "+s);
					pstmtDel.execute();
					delete(s, id, dbConnection);
					logMethod();
					log.info("The id "+id+"  in Database are delete");
				}
				else {
					logMethod();
					log.info("The id "+id+" doesnot exists in Database to delete");
				}				
			}finally {
				ps.close();
				resultSet.close();
				pstmtDel.close();
 

			}

		}
		 catch (Exception e) {
			 System.out.println("delete details");
				
		}							
	}

	
	
	
	
	
	
	
	public List<Employee> displayJason(Connection dbConnection)  {

		PreparedStatement pstmt=null;
		ResultSet rs=null;
		List<Employee> list = new ArrayList<Employee>();
		try {
			
		    pstmt=dbConnection.prepareStatement(jasonQuery);
			rs = pstmt.executeQuery();
	        while(rs.next()) {
	        	
	        	Employee e = new Employee(rs.getInt("employeeId"),rs.getString("employeeName"),rs.getInt("salary"),rs.getString("employeeType"));
	        	list.add(e);        	
	        }
	        return list;
		}catch(Exception ex) {
			System.out.println(ex);
		}
		finally {
			try {
				rs.close();
			 	pstmt.close();
 

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return list;
	}
	
  	public List<Employee> displayEmp(int id,Connection dbConnection)  {

		PreparedStatement pstmt=null;
		ResultSet rs=null;
		List<Employee> list = new ArrayList<Employee>();
		try {
			String query="Select * from new_table where employeeId=?";
		    pstmt=dbConnection.prepareStatement(query);
		    pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
	        while(rs.next()) {
	        	
	        	Employee e = new Employee(rs.getInt("employeeId"),rs.getString("employeeName"),rs.getInt("salary"),rs.getString("employeeType"));
	        	list.add(e);        	
	        }
	        return list;
		}catch(Exception ex) {
			System.out.println(ex);
		}
		finally {
			try {
				rs.close();
			 	pstmt.close();
				 

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return list;
	}
	
	
	public boolean present(int id,Connection dbConnection) {

		String check="Select * from new_table where employeeId=?";
		PreparedStatement ps=null;
		ResultSet rs=null;
		try {
			ps=dbConnection.prepareStatement(check);
			ps.setInt(1, id);
			rs=ps.executeQuery();
			if(rs.next()) {
				return true;
			}
			else {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				ps.close();
				rs.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	
	
	public Hashtable<Integer,Employee> displayJasonHash(Connection dbConnection)  {
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		Hashtable<Integer, Employee> hashTable=new Hashtable<Integer, Employee>();
		try {
			
		    pstmt=dbConnection.prepareStatement(jasonQuery);
			rs = pstmt.executeQuery();
	        while(rs.next()) {
	        	
	        	Employee e = new Employee(rs.getInt("employeeId"),rs.getString("employeeName"),rs.getInt("salary"),rs.getString("employeeType"));
	        	hashTable.put(rs.getInt("employeeId"),e);        	
	        }
	        return hashTable;
		}catch(Exception ex) {
			System.out.println(ex);
		}
		finally {
			try {
				rs.close();
			 	pstmt.close();
 

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return hashTable;
	}
	
	public Hashtable<Integer,Employee> displayJason1(int id,Connection dbConnection)  {

		PreparedStatement pstmt=null;
		ResultSet rs=null;
		Hashtable<Integer, Employee> hashTable=new Hashtable<Integer, Employee>();
		try {
			String query="Select * from new_table where employeeId=?";
		    pstmt=dbConnection.prepareStatement(query);
		    pstmt.setInt(1, id);
			rs = pstmt.executeQuery();

	        while(rs.next()) {	        	
	        	Employee e = new Employee(rs.getInt("employeeId"),rs.getString("employeeName"),rs.getInt("salary"),rs.getString("employeeType"));
	        	hashTable.put(rs.getInt("employeeId"),e);        	
	        }
	        return hashTable;
		}catch(Exception ex) {
			System.out.println(ex);
		}
		finally {
			try {
				rs.close();
			 	pstmt.close();
 

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return hashTable;
	}
	

	public List<Employee> empFilter(Object object,Connection dbConnection) {

		String query="select * from new_table where employeeType=?";
		List<Employee> list=new ArrayList<Employee>();

		try {
			PreparedStatement ps=dbConnection.prepareStatement(query);
			ps.setString(1, (String) object);
			ResultSet rs=ps.executeQuery();
			while(rs.next()) {
	        	Employee e = new Employee(rs.getInt("employeeId"),rs.getString("employeeName"),rs.getInt("salary"),rs.getString("employeeType"));
	        	list.add(e);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				return list;
		
	}
	
	
	
	
}










