package bert.data.utility;

import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import bert.data.FileProvider;
import bert.data.proj.BertUnit;
import bert.data.proj.Project;

/**
 * Created by afiol-mahon on 5/11/15.
 */
public class CSVExporter {
    public void generateCSV(Project project) throws IOException{
        File exportCSV = new File(FileProvider.getExportsDirectory(), project.getProjectName() + "_Configurator.csv");
        FileWriter writer = new FileWriter(exportCSV);
        for (BertUnit b : project.getBerts()) {
            writer.append(b.getMAC());
            writer.append(',');
            writer.append(b.getCSVName());
            writer.append(',');
            writer.append(b.getLocation());
            writer.append(';');
            writer.append(project.getBuildings().get(b.getBuildingID()).getName());
            writer.append(';');
            writer.append(project.getCategories().get(b.getCategoryID()).getName());
            writer.append('\n');
            Log.d("CSV EXPORT", "Exported Bert " + b.getName() + " to CSV File");
        }
    }
}
