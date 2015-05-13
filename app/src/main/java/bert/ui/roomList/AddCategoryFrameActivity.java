package bert.ui.roomList;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import bert.data.proj.Category;
import bert.ui.R;
import bert.ui.categoryList.CategoryAddFragment;

public class AddCategoryFrameActivity extends ActionBarActivity implements CategoryAddFragment.OnFragmentInteractionListener {

    public static final String ARG_PROJECT_ID = "PROJECT_ID";

    private int projectID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_frame_layout);
        projectID = getIntent().getExtras().getInt(ARG_PROJECT_ID);

        CategoryAddFragment frag = CategoryAddFragment.newInstance(projectID);

        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.frame_layout, frag);
        t.addToBackStack(null);
        t.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_category, menu);
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

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void categoryCreationCanceled() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    public void categoryCreationSuccessful() {
        setResult(RESULT_OK);
        finish();
    }
}
