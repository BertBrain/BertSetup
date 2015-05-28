package bert.data.utility;

import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bert.data.FileProvider;
import bert.data.proj.BertUnit;
import bert.data.proj.Project;
import bert.data.proj.exceptions.InvalidMACExeption;

/**
 * Created by afiol-mahon on 5/11/15.
 */
public class CSVExporter {
    public static File generateCSV(Project project) throws IOException {
        File exportCSV = new File(FileProvider.getExportsDirectory(), project.getProjectName() + "_Configurator.csv");
        exportCSV.setWritable(true);
        FileWriter writer = new FileWriter(exportCSV);
        for (BertUnit b : project.getBerts()) {
            try {
                writer.write(formatMac(b.getMAC()));
                writer.write(',');
                writer.write(b.getCSVName());
                writer.write(',');
                writer.write(b.getLocation());
                writer.write(';');
                writer.write(b.getBuildingID());
                writer.write(';');
                writer.write(b.getCategoryID());
                writer.write('\n');
                Log.d("CSV EXPORT", "Exported Bert " + b.getName() + " to CSV File");
            } catch (InvalidMACExeption e) {
                Log.e("CSV_EXPORTER", e.message + " in " + b.getName());
            }

        }
        writer.close();
        return exportCSV;
    }


    private static String formatMac(String MAC) throws InvalidMACExeption{


        return MAC;
    }
}
