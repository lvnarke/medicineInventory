package com.sample;//package com.sample;



import org.apache.poi.EncryptedDocumentException;
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


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        final String api = req.getServletPath();




        switch (api){
            case "/submit":{

                System.out.println("Inside submit servlet");



                String medicineName = req.getParameter("medicinename");
                String medicineId = req.getParameter("id");
                String unitValue = req.getParameter("unitValue");
                Integer number = Integer.parseInt(req.getParameter("number")+"");

                try {

                    FileInputStream file = new FileInputStream(new File("medicineInventory.xlsx"));
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

                    file.close();

                    FileOutputStream outputStream = new FileOutputStream("medicineInventory.xlsx");
                    workbook.write(outputStream);

                    outputStream.close();
                }
                 catch (IOException | EncryptedDocumentException
                         ex) {
                    ex.printStackTrace();
                }





            }
            break;
            case "/view":{

                System.out.println("Inside view servlet");

                try {
                    FileInputStream file = new FileInputStream(new File("medicineInventory.xlsx"));

                    // Create Workbook instance holding reference to .xlsx file
                    XSSFWorkbook workbook = new XSSFWorkbook(file);

                    // Get first/desired sheet from the workbook
                    XSSFSheet sheet = workbook.getSheetAt(0);

                    // Iterate through each rows one by one
                    Iterator<Row> rowIterator = sheet.iterator();
                    while (rowIterator.hasNext()) {
                        Row row = rowIterator.next();
                        // For each row, iterate through all the columns
                        Iterator<Cell> cellIterator = row.cellIterator();

                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();
                            // Check the cell type and format accordingly
                            switch (cell.getCellType()) {
                                case Cell.CELL_TYPE_NUMERIC:
                                    System.out.print(cell.getNumericCellValue() + "t");

                                    break;
                                case Cell.CELL_TYPE_STRING:
                                    System.out.print(cell.getStringCellValue() + "t");
                                    break;
                            }
                        }
                        System.out.println("");
                    }
                    file.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }


            }
            break;

            case "/update" :{

            }

        }


    }
}


