package bert.ui.launchScreen;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import bert.ui.R;
import bert.ui.projectList.activity.AuditProjectListActivity;
import bert.ui.projectList.activity.InstallProjectListActivity;

public class LaunchActivity extends ActionBarActivity {

    ImageButton startImage;

    Button auditButton;
    Button installButton;

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

    private void launchInstallActivity(){
        Intent intent = new Intent(this, InstallProjectListActivity.class);
        startActivity(intent);
    }

    private void launchAuditActivity(){
        Intent intent = new Intent(this, AuditProjectListActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_launch, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
