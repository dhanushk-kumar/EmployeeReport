package EmployeePackage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {
	
	//static String url = "jdbc:sqlserver://localhost:1433;databaseName=mavdb;user=sa;password=test@123D";
	
	static String url = "jdbc:sqlserver://MAVCHN1121262;databaseName=mavdb;integratedSecurity=true;encrypt=false;";

	public static void insertIntoDB(EmployeeAttributes emp,boolean report)
	{
		try
		{
			System.out.println("Insering method : " +report);
			String query="";
			if(report==true)
			{
				query = "Insert into EmployeeDetails values('"+emp.getEmployeeId()+"','"+emp.getFirstName()+"','"+emp.getLastName()+"','"+emp.getEmail()+"','"+emp.getPhone()+"','"+emp.getHireDate()+"','"+emp.getJobId()+"','"+emp.getSalary()+"','"+emp.getCommissionPCT()+"','"+emp.getManagerId()+"','"+emp.getDepartmentId()+"')";
			}
			else
			{
				query = "Insert into EmployeeFailure values('"+emp.getEmployeeId()+"','"+emp.getFirstName()+"','"+emp.getLastName()+"','"+emp.getEmail()+"','"+emp.getPhone()+"','"+emp.getHireDate()+"','"+emp.getJobId()+"','"+emp.getSalary()+"','"+emp.getCommissionPCT()+"','"+emp.getManagerId()+"','"+emp.getDepartmentId()+"')";
			}
			System.out.println("Insert Query ->"+query);
			try(Connection conn = DriverManager.getConnection(url))
				{
				PreparedStatement stmt = conn.prepareStatement(query);
				stmt.executeUpdate();
				System.out.println("Insertion Successfull!");
				conn.close();
				}
			catch(SQLException e)
			{
			System.out.println("Connection failed!");
			e.printStackTrace();
			}
			
		}
		catch(Exception e)
		{
		System.out.println("Connection failed!");
		e.printStackTrace();
		}
	}
	
	
	public static List<ReportAttributes> getReport(boolean flag) {
		List<ReportAttributes> erList = new ArrayList<ReportAttributes>();
		try
		{
			System.out.println("Inside the get report");
			String query="";
			if(flag==true)
			{
				query = "SELECT  emp.employee_Id,emp.first_Name,emp.email,empj.job_Descrption,[dbo].[get_manager_name](emp.manager_Id) as ManagerName,"
					+ "dept.department_Descrption\r\n"
					+ "   FROM EmployeeDetails emp"
					+ "   LEFT JOIN Employee_Job empj ON emp.job_Id = empj.job_Id"
					+ "   Left Join Employee_Department dept on emp.department_Id=dept.department_Id";
			}
			else
			{
				System.out.println("Inside else condition");
				query = "SELECT  emp.employee_Id,[dbo].[get_emp_name](emp.employee_Id) as EmployeeName,emp.email,[dbo].[get_manager_name](emp.manager_Id) as ManagerName,"
						+ "dept.department_Descrption\r\n"
						+ "   FROM EmployeeFailure emp\r\n"
						+ "   left join EmployeeDetails empf on emp.employee_Id=empf.employee_Id"
						+ "   LEFT JOIN Employee_Job empj ON emp.job_Id = empj.job_Id"
						+ "   Left Join Employee_Department dept on emp.department_Id=dept.department_Id";
			}
			try(Connection conn = DriverManager.getConnection(url))
				{
				
				Statement stmt = conn.createStatement();
				ResultSet result = stmt.executeQuery(query);
				while(result.next())
				{
					ReportAttributes report = new ReportAttributes();
					report.setEmployee_Id(result.getString(1));
					report.setEmployee_Name(result.getString(2));
					report.setEmail(result.getString(3));
					report.setManager_Name(result.getString(4));
					report.setDepartment_Name(result.getString(5));
					erList.add(report);
				}
				
				conn.close();
				}
		
		}
		catch(SQLException e)
		{
		System.out.println("Connection failed!");
		e.printStackTrace();
		}
		return erList;
	} 
	
	public static void cleanDB()
	{
		String query1 = "Delete from EmployeeDetails";
		String query2 = "Delete from EmployeeFailure";
		try(Connection conn = DriverManager.getConnection(url))
		{
			PreparedStatement stmt = conn.prepareStatement(query1);
			stmt.executeUpdate();
			System.out.println("Old Records from EmployeeDetails deleted!");
			stmt = conn.prepareStatement(query2);
			stmt.executeUpdate();
			System.out.println("Old Records from EmployeeDetails_failed deleted!");
			conn.close();
		}
		catch(SQLException e)
		{
			System.out.println("Connection failed! Not cleared");
			e.printStackTrace();
		}
	}
}
