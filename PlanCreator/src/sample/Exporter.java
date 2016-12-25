package sample;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import sample.model.BusinessKind;
import sample.model.BusinessPlan;
import sample.model.Cost;

import java.io.*;
import java.util.Iterator;

/**
 * Created by vladstarikov on 12/25/16.
 */
public class Exporter {

    public static void saveTemplate(BusinessPlan bp, BusinessKind bk, String orgPath, String newPath) {
        final int sumRow = 12;
        final short defRowHeight = 250;

        try {
            File xls = new File(orgPath);

            FileInputStream inputStream = new FileInputStream(xls);

            byte[] image = new byte[(int) xls.length()];

            try {
                if (inputStream.read(image) != image.length)
                    throw new IOException("Could not completely read file " + xls.getName());

                inputStream.close();

                FileOutputStream outputStream = new FileOutputStream(newPath);

                outputStream.write(image);

                outputStream.close();

                HSSFWorkbook workBook = new HSSFWorkbook(new FileInputStream(newPath));

                Sheet sheet = workBook.getSheetAt(0);

                //***       currency, units, and sales writing

                sheet.getRow(1).getCell(1).setCellValue(bp.currency);
                sheet.getRow(3).getCell(1).setCellValue(bp.unitOfMeasurement);
                sheet.getRow(5).getCell(1).setCellValue(bp.onePeacePrice);

                sheet.getRow(5).getCell(2).setCellFormula("B2&\"/\"&B4");

                sheet.getRow(4).getCell(5).setCellValue(bp.monthlySales);

                sheet.getRow(0).getCell(1).setCellValue(bp.businessName);
                sheet.getRow(0).getCell(8).setCellValue(bk.name);

                //***       last row parameters saving

                String contentConst = sheet.getRow(sumRow).getCell(0).getStringCellValue();

                String contentVar = sheet.getRow(sumRow).getCell(2).getStringCellValue();

                String contentCount = sheet.getRow(12).getCell(5).getStringCellValue();
                String maxCost = sheet.getRow(14).getCell(5).getStringCellValue();

                sheet.getRow(sumRow).removeCell(sheet.getRow(sumRow).getCell(0));
                sheet.getRow(sumRow).removeCell(sheet.getRow(sumRow).getCell(1));
                sheet.getRow(sumRow).removeCell(sheet.getRow(sumRow).getCell(2));
                sheet.getRow(sumRow).removeCell(sheet.getRow(sumRow).getCell(3));

                //***       const. & var. costs

                Iterator<Cost> constCost = bp.constantCosts.iterator();
                Iterator<Cost> varCost = bp.variableCosts.iterator();

                int currRow = sumRow;

                Cost curr;

                while (constCost.hasNext() && varCost.hasNext()) {
                    curr = constCost.next();

                    sheet.createRow(currRow);
                    sheet.getRow(currRow).createCell(0);
                    sheet.getRow(currRow).createCell(1);
                    sheet.getRow(currRow).createCell(2);
                    sheet.getRow(currRow).createCell(3);

                    sheet.getRow(currRow).getCell(0).setCellValue(curr.name);
                    sheet.getRow(currRow).getCell(1).setCellValue(curr.value);

                    curr = varCost.next();

                    sheet.getRow(currRow).getCell(2).setCellValue(curr.name);
                    sheet.getRow(currRow).getCell(3).setCellValue(curr.value);

                    currRow++;
                }

                sheet.createRow(currRow);
                sheet.getRow(currRow).createCell(0);
                sheet.getRow(currRow).createCell(1);
                sheet.getRow(currRow).createCell(2);
                sheet.getRow(currRow).createCell(3);

                sheet.getRow(currRow).getCell(0).setCellValue(contentConst);
                sheet.getRow(currRow).getCell(1).setCellFormula("SUM(B13:B" + String.valueOf(currRow - 1) + ")");

                sheet.getRow(currRow).getCell(2).setCellValue(contentVar);
                sheet.getRow(currRow).getCell(3).setCellFormula("SUM(D13:D" + String.valueOf(currRow - 1) + ")");

                sheet.getRow(currRow).setHeight((short) (defRowHeight * 2));

                //***       graphic table formulas

                for (int index = 5; index < 11; index++)
                    sheet.getRow(index).getCell(5).setCellFormula("F" + String.valueOf(index) + "+$F$5");

                for (int index = 4; index < 11; index++)
                    sheet.getRow(index).getCell(6).setCellFormula("F" + String.valueOf(index + 1) + "+$B$6");

                for (int index = 4; index < 11; index++)
                    sheet.getRow(index).getCell(7).setCellFormula("$B$" + String.valueOf(currRow + 1));

                for (int index = 4; index < 11; index++)
                    sheet.getRow(index).getCell(8).setCellFormula("$D$" + String.valueOf(currRow + 1) + "*F" + String.valueOf(index + 1));

                for (int index = 4; index < 11; index++)
                    sheet.getRow(index).getCell(9).setCellFormula("H" + String.valueOf(index + 1) + "+I" + String.valueOf(index + 1));

                for (int index = 4; index < 11; index++)
                    sheet.getRow(index).getCell(10).setCellFormula("G" + String.valueOf(index + 1) + "-J" + String.valueOf(index + 1));

                for (int index = 4; index < 11; index++)
                    sheet.getRow(index).getCell(11).setCellFormula(
                            "IF(F" + String.valueOf(index + 1) + "=0,0,$B$" +
                                    String.valueOf(currRow + 1) + "/F" +
                                    String.valueOf(index + 1) + "+$D$" +
                                    String.valueOf(currRow + 1) + ")"
                    );

                for (int index = 4; index < 11; index++)
                    sheet.getRow(index).getCell(12).setCellFormula(
                            "IF(J" + String.valueOf(index + 1) + "=0,0,(G" +
                                    String.valueOf(index + 1) + "-J" + String.valueOf(index + 1) +
                                    ")/J" + String.valueOf(index + 1) + ")"
                    );
                sheet.getRow(12).createCell(5);
                sheet.getRow(12).getCell(5).setCellValue(contentCount);
                sheet.getRow(14).createCell(5);
                sheet.getRow(14).getCell(5).setCellValue(maxCost);

                sheet.getRow(12).createCell(11);
                sheet.getRow(12).getCell(11).setCellFormula(
                        "IF(B6=D" + String.valueOf(currRow + 1) +
                                ",0,(B" + String.valueOf(currRow + 1) +
                                "/(B6-D" + String.valueOf(currRow + 1) +
                                ")))"
                );

                sheet.getRow(12).createCell(12);
                sheet.getRow(12).getCell(12).setCellFormula("B4");

                sheet.getRow(14).createCell(11);
                sheet.getRow(14).getCell(11).setCellFormula("L13*B6");
                sheet.getRow(14).createCell(12);
                sheet.getRow(14).getCell(12).setCellFormula("B2");

                workBook.write(new FileOutputStream(newPath));
                workBook.close();
            } catch (IOException err) {
                err.printStackTrace();
            }
        } catch (FileNotFoundException err) {
            err.printStackTrace();
        }
    }
}