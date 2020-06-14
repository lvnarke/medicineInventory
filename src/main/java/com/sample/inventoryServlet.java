package com.sample;//package com.sample;



import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;


@WebServlet(urlPatterns = {"/submit","/update","/view"})

public class inventoryServlet extends HttpServlet{
    String filePath="";


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        final String api = req.getServletPath();




        switch (api){
            case "/submit":{

                System.out.println("Inside submit servlet");
                String absoluteDiskPath = getServletContext().getRealPath("medicineInventory.xlsx");
                filePath=absoluteDiskPath;



                String medicineName = req.getParameter("medicinename");
                String medicineId = req.getParameter("id");
                String unitValue = req.getParameter("unitValue");
                Integer number = Integer.parseInt(req.getParameter("number")+"");

                try {

                    FileInputStream file = new FileInputStream(new File(filePath));
                    XSSFWorkbook workbook = new XSSFWorkbook(file);
                    XSSFSheet sheet = workbook.getSheet("medicineDetails");
                    Object[][] bookData = {

                            {medicineName, medicineId, unitValue, number},
                    };

                    int rowCount = sheet.getLastRowNum();

                    for (Object[] aBook : bookData) {
                        Row row = sheet.createRow(++rowCount);

                        int columnCount = 0;

                        Cell cell = row.createCell(columnCount);
                        cell.setCellValue(rowCount);

                        for (Object field : aBook) {
                            cell = row.createCell(++columnCount);
                            if (field instanceof String) {
                                cell.setCellValue((String) field);
                            } else if (field instanceof Integer) {
                                cell.setCellValue((Integer) field);
                            }
                        }

                    }



                    FileOutputStream outputStream = new FileOutputStream(filePath);
                    workbook.write(outputStream);
                    outputStream.flush();
                    outputStream.close();
                    file.close();
                }
                catch (IOException | EncryptedDocumentException
                        ex) {
                    ex.printStackTrace();
                }
                System.out.println("Done Adding new Row");




            }
            break;

            case "/update" :{

                System.out.println("Inside update servlet");
                String medicineName = req.getParameter("medicinename");
                String medicineId = req.getParameter("id");
                String addOrSold = req.getParameter("addorsold");
                Integer number = Integer.parseInt(req.getParameter("number")+"");

                Integer updatedNumber = 0;

                if(addOrSold.equals("-1")){
                        number = -number;

                }

                try {
                    String absoluteDiskPath = getServletContext().getRealPath("medicineInventory.xlsx");
                    filePath = absoluteDiskPath;
                    FileInputStream file = new FileInputStream(new File(filePath));

                    XSSFWorkbook workbook = new XSSFWorkbook(file);
                    XSSFSheet sheet = workbook.getSheetAt(0);
                    Cell cell = null;


                    cell = sheet.getRow(3).getCell(3);
                    String cellVal = cell.getStringCellValue();
                    System.out.println("Cellvalue is "+cellVal);

                    if(cellVal.equals(medicineId)){

                        Integer existingNumber = Integer.parseInt(sheet.getRow(3).getCell(4)+"");
                        updatedNumber = existingNumber + number;
                        cell = sheet.getRow(3).getCell(4);
                        cell.setCellValue(updatedNumber);
                    }

                    file.close();

                    FileOutputStream outFile = new FileOutputStream(new File(filePath));
                    workbook.write(outFile);
                    outFile.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

        }


    }
}