package edu.mephi.excel;

import edu.mephi.data.Calculate;
import edu.mephi.data.ImportData;
import edu.mephi.data.Result;
import edu.mephi.exceptions.WrongExcelFileException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class Excel {
  private final String exportName = "Calc.xlsx";
  private final int sheetNumber = 4;
  private FileInputStream fis;
  private FileOutputStream fos;
  private XSSFWorkbook wb;

  public Excel() {}

  public ImportData ImportFromFile(String filename)
      throws WrongExcelFileException {
    initImportWorkbook(filename);
    ImportData data = fillDataObject();
    try {
      wb.close();
      fis.close();
    } catch (IOException e) {
      throw new WrongExcelFileException("Can't close");
    }
    return data;
  }

  private void initImportWorkbook(String filename)
      throws WrongExcelFileException {
    try {
      fis = new FileInputStream(filename);
      this.wb = new XSSFWorkbook(fis);
    } catch (NullPointerException e) {
      throw new WrongExcelFileException("Excel file is not choosen");
    } catch (Exception e) {
      throw new WrongExcelFileException(e.getMessage());
    }
  }

  private ImportData fillDataObject() throws WrongExcelFileException {
    ImportData data = new ImportData();
    try {
      XSSFSheet sheet = wb.getSheetAt(sheetNumber);
      int num_rows = sheet.getPhysicalNumberOfRows();
      double[] xArray = new double[num_rows - 1];
      double[] yArray = new double[num_rows - 1];
      double[] zArray = new double[num_rows - 1];
      for (int i = 1; i < num_rows; i++) {
        xArray[i - 1] = sheet.getRow(i).getCell(0).getNumericCellValue();
        yArray[i - 1] = sheet.getRow(i).getCell(1).getNumericCellValue();
        zArray[i - 1] = sheet.getRow(i).getCell(2).getNumericCellValue();
      }
      data.setxArray(xArray);
      data.setyArray(yArray);
      data.setzArray(zArray);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      throw new WrongExcelFileException("Something wrong with choosen file");
    }

    return data;
  }

  public void ExportToFile(String folder, ImportData data)
      throws WrongExcelFileException {
    Calculate calc = new Calculate();
    try {
      initExportWorkbook(folder);
      fillCommonSheet(calc, "X", data.getxArray());
      fillCommonSheet(calc, "Y", data.getyArray());
      fillCommonSheet(calc, "Z", data.getzArray());
      fillCovarienceSheet(calc, data);
      wb.write(fos);
      wb.close();
      fos.close();
    } catch (MathIllegalArgumentException e) {
      throw new WrongExcelFileException("Wrong data in file");
    } catch (IOException e) {
      throw new WrongExcelFileException("Can't close");
    }
  }

  private void fillCommonSheet(Calculate calc, String sheetname, double[] arr) {
    XSSFSheet sheet = wb.createSheet(sheetname);
    ArrayList<Result> res = calc.calcPerArray(arr);
    int rownum = 0;
    Row row;
    Cell cell;
    for (Result r : res) {
      row = sheet.createRow(rownum);
      cell = row.createCell(0);
      cell.setCellValue(r.name);
      cell = row.createCell(1);
      cell.setCellValue(r.value.toString());
      rownum++;
    }
  }

  private void fillCovarienceSheet(Calculate calc, ImportData data) {
    XSSFSheet sheet = wb.createSheet("Covariance");
    ArrayList<Result> res =
        calc.calcCov(data.getxArray(), data.getyArray(), data.getzArray());
    int rownum = 0;
    Row row;
    Cell cell;
    for (Result r : res) {
      row = sheet.createRow(rownum);
      cell = row.createCell(0);
      cell.setCellValue(r.name);
      cell = row.createCell(1);
      cell.setCellValue(r.value.toString());
      rownum++;
    }
  }

  private void initExportWorkbook(String folder)
      throws WrongExcelFileException {
    try {
      fos = new FileOutputStream(FilenameUtils.concat(folder, exportName));
      this.wb = new XSSFWorkbook();
    } catch (NullPointerException e) {
      throw new WrongExcelFileException("Excel file is not choosen");
    } catch (SecurityException e) {
      throw new WrongExcelFileException("Write permission error");
    } catch (FileNotFoundException e) {
      throw new WrongExcelFileException("Something wrong with output file: " +
                                        e.getMessage());
    } catch (Exception e) {
      throw new WrongExcelFileException(e.getMessage());
    }
  }
}
