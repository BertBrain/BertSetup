package bert.ui.projectList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import java.util.ArrayList;
import java.util.List;
import java.lang.System.*;
/**
 * Created by liamcook on 5/14/15.
 */
public class ExportChooser {

    String[] exportOptions = {"Email", "Save to SD"};

    private Activity activity;

    public ExportChooser(Activity activity){
        this.activity = activity;
    }

    public void exportFile(String name) { //TODO: pass a file
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Exporting " + name);
        builder.setItems(exportOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        email();
                        break;
                    case 1:
                        saveToSD();
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }

            }
        });
        builder.create().show();
    }

    private void email(){
        System.out.println("emailing file");
    }

    private void saveToSD(){
        System.out.println("saving file to SD");
    }
}
