package bert.data.utility;

import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
            rows.add(Arrays.asList("Bert Type", "Number Of Devices", "Daily Time On", "Wattage Draw", "Energy/Year Without Bert", "Energy/Year With Bert", "Cost/Year Without Bert", "Cost/Year With Bert", "Savings/Year", "Payback Time (months)"));

            for (int categoryID = 0; categoryID < building.getCategories().size(); categoryID++) {
                Log.d("ROI_EXPORT", "adding category");
                Category category = building.getCategories().get(categoryID);
                List<String> info = new ArrayList<>();

                info.add(category.getName());
                info.add(String.valueOf(project.getBertsByCategory(buildingID, categoryID).size()));
                info.add(String.valueOf(building.getTimeOccupied().hour24()));
                info.add(String.valueOf(category.getEstimatedLoad()));
                info.add("=365*24*INDIRECT(CONCATENATE(\"\"D\"\",ROW()))/1000");
                info.add("=365*INDIRECT(CONCATENATE(\"\"C\"\",ROW()))*INDIRECT(CONCATENATE(\"\"D\"\",ROW()))/1000");
                info.add("=INDIRECT(CONCATENATE(\"\"E\"\",ROW())) * INDIRECT(CONCATENATE(\"\"E\"\",ROW()" + String.valueOf(-categoryID - 2) + "))");
                info.add("=INDIRECT(CONCATENATE(\"\"F\"\",ROW())) * INDIRECT(CONCATENATE(\"\"E\"\",ROW()" + String.valueOf(-categoryID - 2) + "))");
                info.add("=INDIRECT(CONCATENATE(\"\"G\"\",ROW())) - INDIRECT(CONCATENATE(\"\"H\"\",ROW()))");
                info.add("=INDIRECT(CONCATENATE(\"\"B\"\",ROW())) * INDIRECT(CONCATENATE(\"\"C\"\",ROW()" + String.valueOf(-categoryID - 2) + ")) / INDIRECT(CONCATENATE(\"\"I\"\",ROW()))");
                rows.add(info);
            }
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

    private String getCellWithColumn(String column){
        return "INDIRECT(CONCATENATE(\"\"" + column + "\"\",ROW()))";
    }
}
