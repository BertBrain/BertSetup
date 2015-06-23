package bert.utility;

import android.util.Log;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.awt.font.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bert.data.FileProvider;
import bert.data.proj.Building;
import bert.data.proj.Category;
import bert.data.proj.Project;

/**
 * Created by liamcook on 5/19/15.
 */
public class ROIExporter {

    static final short buildingOutsideBorder = CellStyle.BORDER_THICK;
    static final short buildingDividerBorder = CellStyle.BORDER_MEDIUM;
    static final short buildingInnerBorder = CellStyle.BORDER_THIN;

    static final short projectTitleColor = IndexedColors.GREEN.getIndex();
    static final short buildingTitleColor = IndexedColors.BRIGHT_GREEN.getIndex();
    static final short categoryRowColor = IndexedColors.LIGHT_GREEN.getIndex();
    static final short leftBuildingHeaderColor = IndexedColors.SEA_GREEN.getIndex();
    static final short leftBuildingTotalColor = IndexedColors.SEA_GREEN.getIndex();

    static final int categoryIDColumn = 0;
    static final int numberOfDevicesColumn = 1;
    static final int costForBertsColumn = 2;
    static final int dailyTimeOnColumn = 3;
    static final int wattageDrawColumn = 4;
    static final int kWhWithoutBertCoumn = 5;
    static final int kWhWithBertColumn = 6;
    static final int costPerYearWithBertColumn = 7;
    static final int costPerYearWithoutBertColumn = 8;
    static final int savingsPerYearCoumn = 9;
    static final int payBackTimeColumn = 10;

    static Sheet sheet;

    public static File generateROI(Project project) throws IOException {

        File exportROI = new File(FileProvider.getExportsDirectory(), project.getProjectName() + "_ROI.xls");
        exportROI.setWritable(true);

        Workbook workbook = new HSSFWorkbook();
        DataFormat format = workbook.createDataFormat();

        sheet = workbook.createSheet("Bert ROI Sheet");
        //sheet.setColumnWidth(index, width);
        sheet.setDisplayGridlines(false);

        Font projectTitleFont = workbook.createFont();
        projectTitleFont.setFontName("Arial");
        projectTitleFont.setBold(true);
        projectTitleFont.setFontHeightInPoints((short) 14);

        Font buildingHeaderFont  = workbook.createFont();
        buildingHeaderFont.setFontName("Arial");
        buildingHeaderFont.setBold(true);
        buildingHeaderFont.setFontHeightInPoints((short) 10);

        Font buildingTitleFont  = workbook.createFont();
        buildingTitleFont.setFontName("Arial");
        buildingTitleFont.setBold(true);
        buildingTitleFont.setFontHeightInPoints((short) 12);

        CellStyle buildingColumnHeaderStyleLeft = workbook.createCellStyle();
        buildingColumnHeaderStyleLeft.setBorderRight(buildingDividerBorder);
        buildingColumnHeaderStyleLeft.setBorderBottom(buildingDividerBorder);
        buildingColumnHeaderStyleLeft.setBorderLeft(buildingOutsideBorder);
        buildingColumnHeaderStyleLeft.setBorderTop(buildingOutsideBorder);
        buildingColumnHeaderStyleLeft.setFont(buildingHeaderFont);
        buildingColumnHeaderStyleLeft.setFillForegroundColor(leftBuildingHeaderColor);
        buildingColumnHeaderStyleLeft.setFillPattern(CellStyle.SOLID_FOREGROUND);

        CellStyle buildingColumnHeaderStyle = workbook.createCellStyle();
        buildingColumnHeaderStyle.setBorderRight(buildingInnerBorder);
        buildingColumnHeaderStyle.setBorderBottom(buildingDividerBorder);
        buildingColumnHeaderStyle.setBorderLeft(buildingInnerBorder);
        buildingColumnHeaderStyle.setBorderTop(buildingOutsideBorder);
        buildingColumnHeaderStyle.setFont(buildingHeaderFont);

        CellStyle buildingColumnHeaderStyleRight = workbook.createCellStyle();
        buildingColumnHeaderStyleRight.setBorderRight(buildingOutsideBorder);
        buildingColumnHeaderStyleRight.setBorderBottom(buildingDividerBorder);
        buildingColumnHeaderStyleRight.setBorderLeft(buildingInnerBorder);
        buildingColumnHeaderStyleRight.setBorderTop(buildingOutsideBorder);
        buildingColumnHeaderStyleRight.setFont(buildingHeaderFont);

        CellStyle categoryInfoStyleLeft = workbook.createCellStyle();
        categoryInfoStyleLeft.setBorderRight(buildingDividerBorder);
        categoryInfoStyleLeft.setBorderBottom(buildingInnerBorder);
        categoryInfoStyleLeft.setBorderLeft(buildingOutsideBorder);
        categoryInfoStyleLeft.setBorderTop(buildingInnerBorder);
        categoryInfoStyleLeft.setFillForegroundColor(categoryRowColor);
        categoryInfoStyleLeft.setFillPattern(CellStyle.SOLID_FOREGROUND);

        CellStyle categoryInfoStyle = workbook.createCellStyle();
        categoryInfoStyle.setBorderRight(buildingInnerBorder);
        categoryInfoStyle.setBorderBottom(buildingInnerBorder);
        categoryInfoStyle.setBorderLeft(buildingInnerBorder);
        categoryInfoStyle.setBorderTop(buildingInnerBorder);
        categoryInfoStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
        categoryInfoStyle.setFillForegroundColor(categoryRowColor);
        categoryInfoStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        categoryInfoStyle.setDataFormat(format.getFormat("0.#"));

        CellStyle categoryInfoStyleRight = workbook.createCellStyle();
        categoryInfoStyleRight.setBorderRight(buildingOutsideBorder);
        categoryInfoStyleRight.setBorderBottom(buildingInnerBorder);
        categoryInfoStyleRight.setBorderLeft(buildingInnerBorder);
        categoryInfoStyleRight.setBorderTop(buildingInnerBorder);
        categoryInfoStyleRight.setFillForegroundColor(categoryRowColor);
        categoryInfoStyleRight.setFillPattern(CellStyle.SOLID_FOREGROUND);
        categoryInfoStyle.setDataFormat(format.getFormat("0.#"));

        CellStyle buildingTotalStyleLeft = workbook.createCellStyle();
        buildingTotalStyleLeft.setBorderRight(buildingDividerBorder);
        buildingTotalStyleLeft.setBorderBottom(buildingOutsideBorder);
        buildingTotalStyleLeft.setBorderLeft(buildingOutsideBorder);
        buildingTotalStyleLeft.setBorderTop(buildingDividerBorder);
        buildingTotalStyleLeft.setFillForegroundColor(leftBuildingTotalColor);
        buildingTotalStyleLeft.setFillPattern(CellStyle.SOLID_FOREGROUND);

        CellStyle buildingTotalStyle = workbook.createCellStyle();
        buildingTotalStyle.setBorderRight(buildingInnerBorder);
        buildingTotalStyle.setBorderBottom(buildingOutsideBorder);
        buildingTotalStyle.setBorderLeft(buildingInnerBorder);
        buildingTotalStyle.setBorderTop(buildingDividerBorder);

        CellStyle buildingTotalStyleRight = workbook.createCellStyle();
        buildingTotalStyleRight.setBorderRight(buildingOutsideBorder);
        buildingTotalStyleRight.setBorderBottom(buildingOutsideBorder);
        buildingTotalStyleRight.setBorderLeft(buildingInnerBorder);
        buildingTotalStyleRight.setBorderTop(buildingDividerBorder);

        CellStyle buildingTitleStyle = workbook.createCellStyle();
        buildingTitleStyle.setFont(buildingTitleFont);
        buildingTitleStyle.setFillForegroundColor(buildingTitleColor);
        buildingTitleStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

        CellStyle projectTitleStyle = workbook.createCellStyle();
        projectTitleStyle.setFillForegroundColor(projectTitleColor);
        projectTitleStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        projectTitleStyle.setFont(projectTitleFont);

        Row titleRow = sheet.createRow(0);
        titleRow.setHeight((short) 620);
        titleRow.createCell(0).setCellValue("Bert ROI Audit For: ");
        titleRow.getCell(0).setCellStyle(projectTitleStyle);
        titleRow.createCell(1).setCellValue(project.getProjectName());
        titleRow.getCell(1).setCellStyle(projectTitleStyle);


        sheet.createRow(1);

        //create bert device type cost enetering units
        for (int i = 0; i < Category.bertTypes.size(); i++) {
            Row row = sheet.createRow(i+2);

            Log.d("ROI", "row: " + row);

            row.createCell(0).setCellValue("Cost of a " + Category.bertTypes.get(i));
            row.createCell(1).setCellValue(String.valueOf(Category.bertTypeCosts.get(i)));

        }
        addRowToSheet(sheet); //blank row

        List<Integer> buildingTotalRows = new ArrayList<>();

        for (String buildingID : project.getBuildingNames()) {
            Building building = project.getBuilding(buildingID);
            if (building.getCategoryNames().size() != 0 && project.getBertCountForBuilding(buildingID) != 0) {

                //Building Header -------
                Row buildingNameRow = addRowToSheet(sheet);
                buildingNameRow.createCell(0).setCellValue(buildingID);
                buildingNameRow.getCell(0).setCellStyle(buildingTitleStyle);

                Row electricityCostRow = addRowToSheet(sheet);
                electricityCostRow.createCell(1).setCellValue("Average Electricity Cost ($/kwh): ");
                electricityCostRow.createCell(2).setCellValue("0.1");
                String electrictiyCostCell = "C" + (sheet.getLastRowNum()+1);

                Row buidlingsTitlesRow = addRowToSheet(sheet);
                buidlingsTitlesRow.createCell(categoryIDColumn).setCellValue("Category");
                buidlingsTitlesRow.createCell(numberOfDevicesColumn).setCellValue("Number Of Devices");
                buidlingsTitlesRow.createCell(costForBertsColumn).setCellValue("Cost For Berts");
                buidlingsTitlesRow.createCell(dailyTimeOnColumn).setCellValue("Daily Time On");
                buidlingsTitlesRow.createCell(wattageDrawColumn).setCellValue("Wattage Draw \n While Off");
                buidlingsTitlesRow.createCell(kWhWithoutBertCoumn).setCellValue("Yearly kWh \n w/Out Bert");
                buidlingsTitlesRow.createCell(kWhWithBertColumn).setCellValue("Yearly kWh \n with Bert");
                buidlingsTitlesRow.createCell(costPerYearWithoutBertColumn).setCellValue("Yearly Cost \n w/Out Bert ($)");
                buidlingsTitlesRow.createCell(costPerYearWithBertColumn).setCellValue("Yearly Cost\n  With Bert ($)");
                buidlingsTitlesRow.createCell(savingsPerYearCoumn).setCellValue("Savings Per Year ($)");
                buidlingsTitlesRow.createCell(payBackTimeColumn).setCellValue("Payback Time (Years)");

                for (Cell cell : buidlingsTitlesRow){
                    cell.setCellStyle(buildingColumnHeaderStyle);
                }
                buidlingsTitlesRow.getCell(categoryIDColumn).setCellStyle( buildingColumnHeaderStyleLeft);
                buidlingsTitlesRow.getCell(payBackTimeColumn).setCellStyle(buildingColumnHeaderStyleRight);

                int firstCategoryRowIndex = sheet.getLastRowNum()+2;

                for (String categoryID : building.getCategoryNames()) {
                    Category category = building.getCategory(categoryID);

                    if (project.getBertCountForCategory(buildingID, categoryID) != 0) {

                        //Category Rows
                        Row categoryRow = addRowToSheet(sheet);
                        //categoryRow.setRowStyle(categoryInfoStyle);
                        categoryRow.createCell(categoryIDColumn).setCellValue(categoryID);
                        categoryRow.createCell(numberOfDevicesColumn).setCellValue(project.getBertCountForCategory(buildingID, categoryID));
                        categoryRow.createCell(costForBertsColumn).setCellFormula(cellNameInRow(numberOfDevicesColumn) + "*" + cellName(1, (1 + category.getBertTypeID())));
                        categoryRow.createCell(dailyTimeOnColumn).setCellValue(building.getTimeOccupied().getHour24());
                        categoryRow.createCell(wattageDrawColumn).setCellValue(category.getEstimatedLoad());
                        categoryRow.createCell(kWhWithoutBertCoumn).setCellFormula("(1/1000) *365 * 24 *" + cellNameInRow(numberOfDevicesColumn) + "*" + cellNameInRow(wattageDrawColumn));
                        categoryRow.createCell(kWhWithBertColumn).setCellFormula("(1/1000) *365 * " + cellNameInRow(dailyTimeOnColumn) + "*" + cellNameInRow(numberOfDevicesColumn) + "*" + cellNameInRow(wattageDrawColumn));
                        categoryRow.createCell(costPerYearWithoutBertColumn).setCellFormula(cellNameInRow(kWhWithoutBertCoumn) + "*" + electrictiyCostCell);
                        categoryRow.createCell(costPerYearWithBertColumn).setCellFormula(cellNameInRow(kWhWithBertColumn) + "*" + electrictiyCostCell);
                        categoryRow.createCell(savingsPerYearCoumn).setCellFormula(cellNameInRow(costPerYearWithoutBertColumn) + "-" + cellNameInRow(costPerYearWithBertColumn));
                        categoryRow.createCell(payBackTimeColumn).setCellFormula(cellNameInRow(costForBertsColumn) + "/" + cellNameInRow(savingsPerYearCoumn));

                        for (Cell cell : categoryRow){
                            cell.setCellStyle(categoryInfoStyle);
                        }
                        categoryRow.getCell(categoryIDColumn).setCellStyle(categoryInfoStyleLeft);
                        categoryRow.getCell(payBackTimeColumn).setCellStyle(categoryInfoStyleRight);
                    }
                }

                //Building Totals
                Log.d("ROI", "startPosition main: " + firstCategoryRowIndex);
                int lastCategoryRowIndex = sheet.getLastRowNum()+1;
                Row buildingTotalRow = addRowToSheet(sheet);
                //buildingTotalRow.setRowStyle(buildingTotalStyle);
                buildingTotalRow.createCell(categoryIDColumn).setCellValue("Totals:");

                List<Integer> columnsToSum = Arrays.asList(numberOfDevicesColumn, costForBertsColumn, kWhWithoutBertCoumn, kWhWithBertColumn, costPerYearWithBertColumn, costPerYearWithoutBertColumn, savingsPerYearCoumn);
                for (Integer i : columnsToSum) {
                    buildingTotalRow.createCell(i).setCellFormula(sumRowRangeInColum(columnNameForIndex(i), firstCategoryRowIndex, lastCategoryRowIndex));
                }
                buildingTotalRow.createCell(dailyTimeOnColumn).setCellValue("");
                buildingTotalRow.createCell(wattageDrawColumn).setCellValue(""); // makes these cell exist so thier style can be set
                buildingTotalRow.createCell(payBackTimeColumn).setCellFormula(cellNameInRow(costForBertsColumn) + "/" + cellNameInRow(savingsPerYearCoumn));
                for (Cell cell : buildingTotalRow) {
                    cell.setCellStyle(buildingTotalStyle);
                }
                buildingTotalRow.getCell(categoryIDColumn).setCellStyle(buildingTotalStyleLeft);
                buildingTotalRow.getCell(payBackTimeColumn).setCellStyle(buildingTotalStyleRight);
                buildingTotalRows.add(sheet.getLastRowNum()+1);
                addRowToSheet(sheet);
            }
        }

        addRowToSheet(sheet);

        Row projectTotalHeadersRow = addRowToSheet(sheet);
        projectTotalHeadersRow.createCell(0).setCellValue("");
        projectTotalHeadersRow.createCell(numberOfDevicesColumn).setCellValue("Number Of Devices");
        projectTotalHeadersRow.createCell(costForBertsColumn).setCellValue("Cost of Berts");
        projectTotalHeadersRow.createCell(dailyTimeOnColumn).setCellValue("");
        projectTotalHeadersRow.createCell(wattageDrawColumn).setCellValue("");
        projectTotalHeadersRow.createCell(kWhWithoutBertCoumn).setCellValue("Yearly kWh \n w/Out Bert");
        projectTotalHeadersRow.createCell(kWhWithBertColumn).setCellValue("Yearly kWh \n with Bert");
        projectTotalHeadersRow.createCell(costPerYearWithoutBertColumn).setCellValue("Yearly Cost \n w/Out Bert ($)");
        projectTotalHeadersRow.createCell(costPerYearWithBertColumn).setCellValue("Yearly Cost \n with Bert");
        projectTotalHeadersRow.createCell(savingsPerYearCoumn).setCellValue("Savings Per Year ($)");
        projectTotalHeadersRow.createCell(payBackTimeColumn).setCellValue("Payback Time (Years)");
        for (Cell cell : projectTotalHeadersRow) {
            cell.setCellStyle(buildingColumnHeaderStyle);
        }
        projectTotalHeadersRow.getCell(0).setCellStyle(buildingColumnHeaderStyleLeft);
        projectTotalHeadersRow.getCell(payBackTimeColumn).setCellStyle(buildingColumnHeaderStyleRight);

        Row projectTotalsRow = addRowToSheet(sheet);
        projectTotalsRow.createCell(0).setCellValue("Project Totals:");
        projectTotalsRow.createCell(numberOfDevicesColumn).setCellFormula(sumRowsInColumn(buildingTotalRows, columnNameForIndex(numberOfDevicesColumn)));
        projectTotalsRow.createCell(costForBertsColumn).setCellFormula(sumRowsInColumn(buildingTotalRows, columnNameForIndex(costForBertsColumn)));
        projectTotalsRow.createCell(dailyTimeOnColumn).setCellValue("");
        projectTotalsRow.createCell(wattageDrawColumn).setCellValue("");
        projectTotalsRow.createCell(kWhWithoutBertCoumn).setCellFormula(sumRowsInColumn(buildingTotalRows, columnNameForIndex(kWhWithoutBertCoumn)));
        projectTotalsRow.createCell(kWhWithBertColumn).setCellFormula(sumRowsInColumn(buildingTotalRows, columnNameForIndex(kWhWithBertColumn)));
        projectTotalsRow.createCell(costPerYearWithoutBertColumn).setCellFormula(sumRowsInColumn(buildingTotalRows, columnNameForIndex(costPerYearWithoutBertColumn)));
        projectTotalsRow.createCell(costPerYearWithBertColumn).setCellFormula(sumRowsInColumn(buildingTotalRows, columnNameForIndex(costPerYearWithBertColumn)));
        projectTotalsRow.createCell(savingsPerYearCoumn).setCellFormula(sumRowsInColumn(buildingTotalRows, columnNameForIndex(savingsPerYearCoumn)));
        projectTotalsRow.createCell(payBackTimeColumn).setCellFormula(cellNameInRow(costForBertsColumn) + "/" + cellNameInRow(savingsPerYearCoumn));
        for (Cell cell : projectTotalsRow) {
            cell.setCellStyle(buildingTotalStyle);
        }
        projectTotalsRow.getCell(0).setCellStyle(buildingTotalStyleLeft);
        projectTotalsRow.getCell(payBackTimeColumn).setCellStyle(buildingTotalStyleRight);


        //sheet.autoSizeColumn(0);
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 6500);
        sheet.setColumnWidth(costForBertsColumn, 3000);

        sheet.setColumnWidth(savingsPerYearCoumn, 4250);
        sheet.setColumnWidth(payBackTimeColumn, 4250);

        FileOutputStream fileOut = new FileOutputStream(exportROI);
        workbook.write(fileOut);
        fileOut.close();
        return exportROI;
    }

    static private String columnNameForIndex(int i) {
        List<String> columnNames = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N");
        String columnName;
        try {
            columnName = columnNames.get(i);
        } catch (IndexOutOfBoundsException e) {
            columnName = "Column ID out of bounds! (Column ID was : " + i + ")";
        }
        return columnName;
    }

    static private String cellNameInRow(int cell){
        return cellName(cell, sheet.getLastRowNum()+1);
    }

    static private String cellName(int column, int row){
        String columnName = columnNameForIndex(column);
        columnName += String.valueOf(row);
        return columnName;
    }

    static private Row addRowToSheet(Sheet sheet) {
        return sheet.createRow(sheet.getLastRowNum() + 1);
    }

    private static String sumRowRangeInColum(String columnNumber, int startPosition, int endPosition){
        String equasion = "SUM(";
        equasion += columnNumber;
        Log.d("ROI", "startPosition: " + startPosition);
        equasion += String.valueOf(startPosition);
        equasion += ":";
        equasion += columnNumber;
        equasion += String.valueOf(endPosition);
        equasion += ")";
        return equasion;
    }

    private static String sumRowsInColumn(List<Integer> rows, String column){
        String totalBerts = "SUM(";
        for (Integer i : rows) {
            totalBerts += column;
            totalBerts += i;
            totalBerts += ", ";
        }
        totalBerts += ")";
        return totalBerts;
    }
}
