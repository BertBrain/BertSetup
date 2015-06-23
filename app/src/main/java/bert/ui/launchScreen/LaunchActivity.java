package bert.ui.launchScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import bert.ui.R;
import bert.ui.projectList.activity.AuditProjectListActivity;
import bert.ui.projectList.activity.InstallProjectListActivity;

public class LaunchActivity extends Activity {

    private ImageButton startImage;

    private Button auditButton;
    private Button installButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        startImage = (ImageButton) findViewById(R.id.startScreenImage);
        auditButton = (Button) findViewById(R.id.startAuditButton);
        installButton = (Button) findViewById(R.id.startInstallButton);

        auditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAuditActivity();
            }
        });

        installButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchInstallActivity();
            }
        });
    }

    private void launchInstallActivity() {
        Intent intent = new Intent(this, InstallProjectListActivity.class);
        startActivity(intent);
    }

    private void launchAuditActivity() {
        Intent intent = new Intent(this, AuditProjectListActivity.class);
        startActivity(intent);
    }
}
