/**
 * 
 */
package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @author sudheer
 * Description :This class contains method which read data and write data into excel files
 *
 */
public class ExcelUtil 
{
	private Workbook myWorkbook ;
	
	private Sheet mySheet;
	
	protected Row row;
	
	protected Cell cell;
	
	//Constructor
	
	public  ExcelUtil(String filePath,String fileName,String sheetName) throws IOException
	{
		try
		{
		//Create an object of File class to open xlsx file

	    File file =    new File(filePath+"\\"+fileName);

	    //Create an object of FileInputStream class to read excel file

	    FileInputStream inputStream = new FileInputStream(file);

	   //Find the file extension by splitting file name in substring  and getting only extension name

	    String fileExtensionName = fileName.substring(fileName.indexOf("."));

	    //Check condition if the file is xlsx file

	    if(fileExtensionName.equals(".xlsx"))
	    {

	    //If it is xlsx file then create object of XSSFWorkbook class

	    myWorkbook = new XSSFWorkbook(inputStream);

	    }

	    //Check condition if the file is xls file

	    else if(fileExtensionName.equals(".xls")){

	        //If it is xls file then create object of XSSFWorkbook class
	    	myWorkbook = new HSSFWorkbook(inputStream);
	       
	    }
	    	mySheet = myWorkbook.getSheet(sheetName);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	    
	}
	public String getCellData(int rownum,int columnum)
	{
		try
		{
			row=mySheet.getRow(rownum);
			String getCellData=row.getCell(columnum).getStringCellValue();
			return getCellData;
		}
		catch(NullPointerException N)
		{
			System.out.println("No data found");
			return null;
		}
	}


}
