package bert.ui.projectList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.File;
/**
 * Created by liamcook on 5/14/15.
 */
public class ExportChooser {

    String[] exportOptions = {"Wireless", "Save to SD"};


    private ProjectListActivity activity;

    public ExportChooser(ProjectListActivity activity){
        this.activity = activity;
    }

    public void exportFile(String name, final File dataToSend) { //TODO: pass a file
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Exporting " + name);
        builder.setItems(exportOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        email(dataToSend);
                        break;
                    case 1:
                        saveToSD(dataToSend);
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }

            }
        });
        builder.create().show();
    }

    private void email(File file){
        System.out.println("emailing file");
        file.setReadable(true, false);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Bert Setup Export");
        Log.d("EMAIL_SENDER", Uri.fromFile(file).toString());
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        activity.startActivity(Intent.createChooser(intent, "Choose Export Method"));
    }

    private void saveToSD(File file)
    {
        System.out.println("saving file to SD");
    }
}
