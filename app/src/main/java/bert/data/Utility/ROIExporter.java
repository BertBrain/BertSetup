package bert.data.utility;

import android.util.Log;

import java.io.File;
import java.io.FileWriter;
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
    public static File generateROI(Project project) throws IOException {

        ArrayList<List<String>> rows = new ArrayList<>();

        rows.add(Arrays.asList("Bert ROI Sheet For: ", project.getProjectName()));
        rows.add(Arrays.asList(""));

        for (int buildingID = 0; buildingID < project.getBuildings().size(); buildingID++) {
            Log.d("ROI_EXPORT", "adding building");
            Building building = project.getBuildings().get(buildingID);
            List<String> firstLine = new ArrayList<>();
            firstLine.add(building.getName());
            rows.add(firstLine);

            rows.add(Arrays.asList("", "Bert Cost ($):", "80", "Average Electricity Cost ($/kwh): ", "0.1"));
            rows.add(Arrays.asList("Equipment", "Number Of Devices", "Daily Time On", "Wattage Draw", "Yearly kWh w/Out Bert", "Yearly kWh With Bert", "Cost/Year Without Bert", "Cost/Year With Bert", "$ Savings/Year", "Payback Time (Years)"));

            int numCategories = 0;
            for (int categoryID = 0; categoryID < building.getCategories().size(); categoryID++) {

                Log.d("ROI_EXPORT", "adding category");
                Category category = building.getCategories().get(categoryID);
                if (project.getBertsByCategory(buildingID, categoryID).size() != 0){
                    numCategories++;
                    List<String> info = new ArrayList<>();

                    info.add(category.getName());
                    info.add(String.valueOf(project.getBertsByCategory(buildingID, categoryID).size()));
                    info.add(String.valueOf(building.getTimeOccupied().hour24()));
                    int load = category.getEstimatedLoad();
                    info.add(load > 0 ? String.valueOf(load) : "Undefined Load");
                    info.add("=365*24*"+ getCell("B") +"*" + getCell("D")+ "/1000");
                    info.add("=365*"+ getCell("C") + "*" + getCell("B") + " * " + getCell("D") + "/1000");
                    info.add("=" + getCell("E") + "*" + getCell("E", -2 -categoryID));
                    info.add("=" + getCell("F") + "*" + getCell("E", -2 -categoryID));
                    info.add("=" + getCell("G") + "-" + getCell("H"));
                    info.add("=" + getCell("B") + "*" + getCell("C", -categoryID - 2) + "/ " + getCell("I"));
                    rows.add(info);
                }
            }

            List<String> totals = new ArrayList<>();
            totals.add("Totals: ");
            totals.add(sumColumn(numCategories));
            totals.add("");
            totals.add("");
            totals.add(sumColumn(numCategories));
            totals.add(sumColumn(numCategories));
            totals.add(sumColumn(numCategories));
            totals.add(sumColumn(numCategories));
            totals.add(sumColumn(numCategories));
            totals.add("=" + getCell("B") + "*" + getCell("C", -numCategories - 2) + "/ " + getCell("I"));
            rows.add(totals);
            rows.add(Arrays.asList(""));
        }

        String ROIString = "";
        for (List<String> row : rows){
            for (String str: row){
                ROIString += "\"";
                ROIString += str;
                ROIString += "\"";
                ROIString += ",";
            }
            ROIString += '\n';
        }
        Log.d("ROI String", ROIString);

        File exportROI = new File(FileProvider.getExportsDirectory(), project.getProjectName() + "_ROI.csv");
        exportROI.setWritable(true);
        FileWriter writer = new FileWriter(exportROI);


        writer.write(ROIString);
        writer.close();
        Log.d("ROI_EXPORT", exportROI.toString());
        return exportROI;
    }

    private static String getCell(String column, int offset){
        return "INDIRECT(CONCATENATE(\"\"" + column + "\"\",ROW() +" + String.valueOf(offset)+ " ))";
    }

    private static String getCell(String column){
        return "INDIRECT(CONCATENATE(\"\"" + column + "\"\",ROW()))";
    }

    private static String sumColumn(int columnHieght){
        String equasion = "=SUM(INDIRECT(CONCATENATE(CHAR(COLUMN() + 64), ROW() - 1 - ";
        equasion += String.valueOf(columnHieght);
        equasion += ", \"\":\"\", CHAR(COLUMN() + 64), ROW()-1)))";

        return equasion;
    }
}
