package EmployeePackage;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainClass {
	
	public static void main(String arge[]) throws FileNotFoundException
	{
		DBConnection.cleanDB();
		BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\dhanushkumarkum\\Downloads\\employee.csv"));
		Scanner scan = new Scanner(br);
		while (scan.hasNextLine())
		{
			validate(scan.nextLine());
		}
		
		ValidationReport report = new ValidationReport();
		report.validation();
		
	}
	
	public static void validate(String employee)
	{
		employee = employee.replaceAll(",,",",-,");
		employee = employee.replaceAll(",,",",-,");
		if(employee.charAt(employee.length()-1)==',')
		{
			employee = employee+"-";
		}
		
		String employees[] = employee.split(",");
		EmployeeAttributes success = new EmployeeAttributes();
		EmployeeAttributes failure = new EmployeeAttributes();
		
		if(isNullorEmpty(employees[0]))
		{
			
			//inserts complete record into failure
			failure.setEmployeeId(employees[0]);
			failure.setFirstName(employees[1]);
			failure.setLastName(employees[2]);
			failure.setEmail(employees[3]);
			failure.setPhone(employees[4]);
			failure.setHireDate(employees[5]);
			failure.setJobId(employees[6]);
			failure.setSalary(employees[7]);
			failure.setCommissionPCT(employees[8]);
			failure.setManagerId(employees[9]);
			failure.setDepartmentId(employees[10]);
			DBConnection.insertIntoDB(failure, false);
		}
		else
		{
			System.out.println("if condition executed");
			success.setEmployeeId(employees[0]);
			failure.setEmployeeId(employees[0]);
		
			//FirstName
			if(isAlphabet(employees[1]))
			{
				if(isNullorEmpty(employees[1]))
				{
					success.setFirstName(employees[1]);
					failure.setFirstName("-");
				}
				else
				{
					success.setFirstName(employees[1]+" "+employees[2]);
					failure.setFirstName("-");
				}
			}
			else
			{
				failure.setFirstName(employees[1]+" "+employees[2]);
			}
			
			//LasttName
			if(isAlphabet(employees[2]))
			{
				if(isNullorEmpty(employees[2]))
				{
					success.setLastName(employees[2]);
					failure.setLastName("-");
				}
				else
				{
					success.setLastName(employees[1]+" "+employees[2]);
					failure.setLastName("-");
				}
			}
			else
			{
				failure.setFirstName(employees[1]+" "+employees[2]);
			}
			
			//Email
			String email = checkEmail(employees[3]);
			if(email.equalsIgnoreCase("false"))
			{
				failure.setEmail(employees[3]);
				success.setEmail("-");
			}
			else
			{
				success.setEmail(email);
				failure.setEmail("-");
			}
			
			//PhoneNumber
			if(checkPhoneNumber(employees[4]))
			{
				success.setPhone(employees[4]);
				failure.setPhone(employees[4]);
			}
			else
			{
				success.setPhone("-");
				failure.setPhone(employees[4]);
			}
			
			//HireDate
			if(checkDate(employees[5]))
			{
				success.setHireDate(employees[5]);
				failure.setHireDate("-");
			}
			else
			{
				success.setHireDate("-");
				failure.setHireDate(employees[5]);
			}
			
			//JOBID
			if(isNullorEmpty(employees[6]))
			{
				success.setJobId("-");
				failure.setJobId("-");
			}
			else
			{
				success.setJobId(employees[6]);
				failure.setJobId(employees[6]);
			}
			
			//Salary
			if(checkSalary(employees[7]))
			{
				success.setSalary(employees[7]);
				failure.setSalary("-");
			}
			else
			{
				failure.setSalary(employees[7]);
				success.setSalary("-");
			}
			
			//COMMISSION_PCT
			success.setCommissionPCT(employees[8]);
			failure.setCommissionPCT("-");
			
			//MANAGER_ID
			if(isNullorEmpty(employees[9]))
			{
				success.setManagerId(employees[0]);
				failure.setManagerId(employees[0]);
			}
			else
			{
				success.setManagerId(employees[9]);
				failure.setManagerId(employees[9]);
			}
			
			//DEPARTMENT_ID
			if(isNullorEmpty(employees[10]))
			{
				success.setDepartmentId("-");
				failure.setDepartmentId("-");
			}
			else
			{
				success.setDepartmentId(employees[10]);
				failure.setDepartmentId(employees[10]);
			}
			if(!(failure.getFirstName().equalsIgnoreCase("-") && failure.getLastName().equalsIgnoreCase("-") && failure.getEmail().equalsIgnoreCase("-")  && 
					failure.getPhone().equalsIgnoreCase("-") && failure.getHireDate().equalsIgnoreCase("-")
					 && failure.getSalary().equalsIgnoreCase("-")))
			{
				//Insert Failure record
				DBConnection.insertIntoDB(failure, false);
			}
			//Insert Success record
			DBConnection.insertIntoDB(success, true);
		
		}
		
	}
	
	public static boolean isNullorEmpty(String value)
	{
		if(value.trim().equalsIgnoreCase("")||value.equals("null")||value.equals(null)||value.trim().equalsIgnoreCase("-"))
		{
			return true;
		}
		return false;
	}
	
	
	public static String checkEmail(String value)
    {
		if(isNullorEmpty(value))
		{
			return "false";
		}
		else
		{
			if(value.length()<=50)
			{
				if((!value.equals("")) && (value != null) && (value.matches("^[a-zA-Z ]*$")))
				{
					return value+"@abc.com";
				}
		        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
		                            "[a-zA-Z0-9_+&*-]+)*@" +
		                            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
		                            "A-Z]{2,7}$";
		                              
		        Pattern pat = Pattern.compile(emailRegex);
		        if(pat.matcher(value).matches())
		        {
		        	return value;
		        }
		        else
		        {
		        	return "false";
		        }
			}
			else
			{
				return "false";
			}
		}
    }
	
	public static boolean checkPhoneNumber(String value)
	{
		if(isNullorEmpty(value))
		{
			return false;
		}
		else
		{
			if(value.length()==12)
			{
				//"\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}" pattern for the complete phoneNumber
				if(value.charAt(3)=='.' && value.charAt(7)=='.')
				{
					String phoneRegex ="^[\\.0-9]*$";
					Pattern pat = Pattern.compile(phoneRegex);
					return pat.matcher(value).matches();
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
	}
	
	public static boolean checkDate(String value)
    {
        String regex = "([0-2][0-9]||3[0-1])-(0[0-9]||1[0-2])-((19|20)\\d\\d)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher((CharSequence)value);
        return matcher.matches();
    }
	
	public static boolean checkSalary(String value)
    {
		if(isNullorEmpty(value))
		{
			return false;
		}
		else
		{
		String regex = "^([0-9]*)(.[[0-9]+]?)?$";
		Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher((CharSequence)value);
        return matcher.matches();
		}
    }
	
	public static boolean isAlphabet(String value) 
	{
	    return ((!value.equals("")) && (value != null) && (value.matches("^[a-zA-Z ]*$")));
	}
}
