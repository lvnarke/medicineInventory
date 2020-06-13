package com.sample;//package com.sample;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;


@WebServlet(urlPatterns = {"/submit","/update","/view"})

public class inventoryServlet extends HttpServlet{
    static ArrayList<Float> values = new ArrayList<>();

    static int counter = 3;

    Map<String, Object[]> data = new TreeMap<String, Object[]>();
    void excelSetup(){


    }
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

                FileInputStream file = new FileInputStream(new File("medicineInventory.xlsx"));
                XSSFWorkbook workbook = new XSSFWorkbook(file);
                // Create a blank sheet
                XSSFSheet sheet = workbook.getSheet("medicineDetails");

                data.put("1",new Object[]{medicineName,medicineId,unitValue,number});

                Set<String> keyset = data.keySet();
                int rownum = 0;
                for (String key : keyset) {
                    // this creates a new row in the sheet
                    Row row = sheet.createRow(rownum++);
                    Object[] objArr = data.get(key);
                    int cellnum = 0;
                    for (Object obj : objArr) {
                        // this line creates a cell in the next column of that row
                        Cell cell = row.createCell(cellnum++);
                        if (obj instanceof String)
                            cell.setCellValue((String)obj);
                        else if (obj instanceof Integer)
                            cell.setCellValue((Integer)obj);
                    }
                }
                try {
                    // this Writes the workbook gfgcontribute
                    FileOutputStream out = new FileOutputStream(new File("medicineInventory.xlsx"));
                    workbook.write(out);
                    out.close();
                    System.out.println("medicineInventory.xlsx written successfully on disk.");
                }
                catch (Exception e) {
                    e.printStackTrace();
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

        }


    }
}


