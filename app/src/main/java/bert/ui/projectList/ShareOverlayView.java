package bert.ui.projectList;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import java.io.File;

/**
 * Created by liamcook on 5/12/15.
 */
public class ShareOverlayView {

    File fileToShare;

    Button emailButton;
    Button saveToSDButton;
    FrameLayout layout;

    ProjectListActivity activity;

    ShareOverlayView(Button emailButton, Button saveToSDButton, FrameLayout layout, ProjectListActivity activity) {
        this.emailButton = emailButton;
        this.saveToSDButton = saveToSDButton;
        this.layout = layout;
        this.activity = activity;

        emailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareByEmail();
            }
        });
        saveToSDButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareBySD();
            }
        });
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        hide();
    }

    public void hide(){
        layout.setVisibility(View.INVISIBLE);
    }

    private void show(){
        layout.setVisibility(View.VISIBLE);
    }

    public void shareFile(File file) {
        show();
        this.fileToShare = file;
    }

    private void shareByEmail() {
        System.out.println("sharing by email");
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_STREAM, fileToShare.toURI());
        intent.putExtra(Intent.EXTRA_TEXT, "EXPORTED CSV FROM BERT SETUP APPLICATION");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Exported CSV");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            System.out.println("no email client installed");
            //TODO: make popup telling user to install email client
        }

        hide();
    }

    private void shareBySD() {
        System.out.println("sharing by SD");
        hide();
    }

    private void cancel() {
        fileToShare = null;
        hide();
    }
}
