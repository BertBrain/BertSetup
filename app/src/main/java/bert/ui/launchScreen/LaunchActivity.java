package bert.ui.launchScreen;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import bert.ui.R;
import bert.ui.projectList.ProjectListActivity;

public class LaunchActivity extends Activity {

    ImageButton startImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        startImage = (ImageButton) findViewById(R.id.startScreenImage);
        startImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchProjectListActivity();
            }
        });
    }

    private void launchProjectListActivity(){
        Intent intent = new Intent(this, ProjectListActivity.class);
        startActivity(intent);
    }
}
