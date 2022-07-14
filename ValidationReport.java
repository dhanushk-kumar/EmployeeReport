package EmployeePackage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

public class ValidationReport {
	
	
	public void validation()
	{
		File success_file = new File("C:\\Users\\dhanushkumarkum\\Downloads\\success.csv");
		File failure_file = new File("C:\\Users\\dhanushkumarkum\\Downloads\\failure.csv");
		try
		{
			List<ReportAttributes> success_report = DBConnection.getReport(true);
			List<ReportAttributes> failed_report = DBConnection.getReport(false);
			
			//List<EmployeeReport> result = success_report.stream().filter(s->(s.getEmployee_Id().)).collect(Collectors.toList());
			//System.out.println("Result->"+result.size());
			
			BufferedWriter out = new BufferedWriter(new FileWriter(success_file));
			out.write("EmployeeID,Name,Email,Department Name,Manager Name");
			out.newLine();
			for(ReportAttributes er : success_report)
			{
				out.write(er.getEmployee_Id()+","+er.getEmployee_Name()+","+er.getEmail()+","+er.getDepartment_Name()+","+er.getManager_Name());
				out.newLine();
			}
			out.close();
			System.out.println("Success Report generated!");
			
			out = new BufferedWriter(new FileWriter(failure_file));
			out.write("EmployeeID,Name,Email,Department Name,Manager Name");
			out.newLine();
			for(ReportAttributes er : failed_report)
			{
				out.write(er.getEmployee_Id()+","+er.getEmployee_Name()+","+er.getEmail()+","+er.getDepartment_Name()+","+er.getManager_Name());
				out.newLine();
			}
			out.close();
			System.out.println("Failure Report generated!");
			
		}
		catch(Exception e)
		{
			System.out.println("Exception->"+e);
		}
    }
}
