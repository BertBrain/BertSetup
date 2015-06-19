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

    static String[] exportOptions = {"Wireless", "Save to SD"};

    static private Activity activity;

    static public void exportFile(Activity activity, final String name, final File dataToSend) {
        exportFile(activity, "", name, dataToSend);
    }

    static public void exportFile(Activity replacmentActivity, final String desination, final String name, final File dataToSend) {
        activity = replacmentActivity;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Exporting " + name);
        builder.setItems(exportOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0:
                        email(desination, name + " export", dataToSend);
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

    static private void email(String destination, String title, File file){
        System.out.println("emailing file");
        file.setReadable(true, false);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_MIME_TYPES, "text/plain");
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_CC, "destination");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        Log.d("EMAIL_SENDER", Uri.fromFile(file).toString());
        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        activity.startActivity(Intent.createChooser(intent, "Choose Export Method"));
    }

    static private void saveToSD(File file)
    {
        //posible TODO: let user choose file path
        //currently just pressing this generates csv that is automatically stored on device
        System.out.println("saving file to SD");
    }
}
