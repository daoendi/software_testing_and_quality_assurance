package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class PriceTesting {

    public static void main(String[] args) {
        String excelFilePath = "src/main/resources/c2_testing.xlsx";

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
                    int tuoi = 0;
                    Cell tuoiCell = row.getCell(1);
                    if (tuoiCell != null && tuoiCell.getCellType() == CellType.NUMERIC) {
                        tuoi = (int) tuoiCell.getNumericCellValue();
                    }

                    String ngay = null;
                    Cell ngayCell = row.getCell(2);
                    if (ngayCell != null) {
                        ngay = ngayCell.toString().trim();
                    }

                    boolean laHocSinh = false;
                    Cell hsCell = row.getCell(3);
                    if (hsCell != null) {
                        String val = hsCell.toString().trim();
                        laHocSinh = val.equalsIgnoreCase("TRUE") || val.equals("1");
                    }

                    boolean laThanhVien = false;
                    Cell tvCell = row.getCell(4);
                    if (tvCell != null) {
                        String val = tvCell.toString().trim();
                        laThanhVien = val.equalsIgnoreCase("TRUE") || val.equals("1");
                    }

                    int expected = 0;
                    Cell expCell = row.getCell(5);
                    if (expCell != null && expCell.getCellType() == CellType.NUMERIC) {
                        expected = (int) expCell.getNumericCellValue();
                    }

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

            try (FileOutputStream fos = new FileOutputStream("src/main/resources/c2_testing_result.xlsx")) {
                workbook.write(fos);
            }

            System.out.println("Kết quả đã được ghi vào src/main/resources/c2_testing_result.xlsx");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
