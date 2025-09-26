package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class PriceTesting {

    public static void main(String[] args) {
        String excelFilePath = "src/main/resources/boundary_value_testing.xlsx";

        try (FileInputStream fis = new FileInputStream(excelFilePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            Row header = sheet.getRow(0);
            if (header.getLastCellNum() < 7) {
                Cell giaVeHeader = header.createCell(6);
                giaVeHeader.setCellValue("giaVe");
                Cell resultHeader = header.createCell(7);
                resultHeader.setCellValue("Result");
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                try {
                    int tuoi = (int) row.getCell(1).getNumericCellValue();
                    String ngay = row.getCell(2).getStringCellValue();
                    boolean laHocSinh = row.getCell(3).getBooleanCellValue();
                    boolean laThanhVien = row.getCell(4).getBooleanCellValue();
                    int expected = (int) row.getCell(5).getNumericCellValue();

                    int giaVe = MovieTicket.tinhGiaVe(tuoi, ngay, laHocSinh, laThanhVien);
                    String result = (giaVe == expected) ? "PASS" : "FAILED";

                    System.out.printf("Test case %d: tuoi=%d, ngay=%s, hocSinh=%b, thanhVien=%b => giaVe=%d, expected=%d => %s%n",
                            i, tuoi, ngay, laHocSinh, laThanhVien, giaVe, expected, result);

                    Cell giaVeCell = row.getCell(6);
                    if (giaVeCell == null) giaVeCell = row.createCell(6);
                    giaVeCell.setCellValue(giaVe);

                    Cell resultCell = row.getCell(7);
                    if (resultCell == null) resultCell = row.createCell(7);
                    resultCell.setCellValue(result);

                } catch (IllegalArgumentException ex) {
                    System.out.printf("Test case %d: lỗi từ hàm tinhGiaVe: %s%n", i, ex.getMessage());
                    Cell resultCell = row.getCell(7);
                    if (resultCell == null) resultCell = row.createCell(7);
                    resultCell.setCellValue("ERROR: " + ex.getMessage());
                } catch (Exception ex) {
                    System.out.printf("Test case %d: lỗi đọc dữ liệu: %s%n", i, ex.getMessage());
                    Cell resultCell = row.getCell(7);
                    if (resultCell == null) resultCell = row.createCell(7);
                    resultCell.setCellValue("READ ERROR: " + ex.getMessage());
                }
            }

            try (FileOutputStream fos = new FileOutputStream(excelFilePath)) {
                workbook.write(fos);
            }

            System.out.println("Kết quả đã được ghi đè vào " + excelFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
