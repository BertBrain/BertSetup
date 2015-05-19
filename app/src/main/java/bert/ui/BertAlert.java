package bert.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by liamcook on 5/15/15.
 */
public class BertAlert {

    static DialogInterface.OnClickListener emptyListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {}
    };

    public static void show(Activity activity, String title) {
        show(activity, title, "Ok");
    }

    public static void show(Activity activity, String title, String button1) {
        show(activity, title, button1, emptyListener, "", emptyListener);
    }

    public static void show(Activity activity, String title, String button1, DialogInterface.OnClickListener listener) {
        show(activity, title, button1, listener, "", emptyListener);
    }

    public static void show(Activity activity, String title, String button1, DialogInterface.OnClickListener listener1, String button2, DialogInterface.OnClickListener listener2){
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setTitle(title);
        alert.setPositiveButton(button1, listener1);
        alert.setNegativeButton(button2, listener2);
        alert.create().show();
    }


}
