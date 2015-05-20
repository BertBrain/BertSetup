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
    public static File generateCSV(Project project) throws IOException {
        File exportCSV = new File(FileProvider.getExportsDirectory(), project.getProjectName() + "_Configurator.csv");
        exportCSV.setWritable(true);
        FileWriter writer = new FileWriter(exportCSV);
        for (BertUnit b : project.getBerts()) {
            writer.write(b.getMAC());
            writer.write(',');
            writer.write(b.getCSVName());
            writer.write(',');
            writer.write(b.getLocation());
            writer.write(';');
            writer.write(project.getBuildings().get(b.getBuildingID()).getName());
            writer.write('\n');
            Log.d("CSV EXPORT", "Exported Bert " + b.getName() + " to CSV File");
        }
        writer.close();
        return exportCSV;
    }
}
