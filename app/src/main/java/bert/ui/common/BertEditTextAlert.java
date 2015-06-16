package bert.ui.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;

/**
 * Created by liamcook on 6/16/15.
 */
public class BertEditTextAlert {

    public static void show(Activity activity, String title, String message, String okButton, String cancelButton, final AlertResponder responder){
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);

        alert.setTitle(title);
        alert.setMessage("Message");

        // Set an EditText view to get user input
        final EditText input = new EditText(activity);
        alert.setView(input);

        alert.setPositiveButton(okButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                responder.okPressed(input.getText().toString());
            }
        });

        alert.setNegativeButton(cancelButton, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                responder.cancelPressed(input.getText().toString());
            }
        });

        alert.show();
    }

    public interface AlertResponder {
        public void cancelPressed(String result);
        public void okPressed(String result);
    }
}


