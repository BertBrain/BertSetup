package bert.ui.roomList;

import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;

import bert.ui.R;
import bert.ui.categoryList.AddCategoryFragment;

public class AddCategoryFrameActivity extends ActionBarActivity implements AddCategoryFragment.OnFragmentInteractionListener {

    public static final String ARG_PROJECT_ID = "PROJECT_ID";
    public static final String ARG_BUILDING_ID = "ARG_BUILDING_ID";

    private int projectID;
    private String buildingID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_frame_layout);
        projectID = getIntent().getExtras().getInt(ARG_PROJECT_ID);
        buildingID = getIntent().getExtras().getString(ARG_BUILDING_ID);

        AddCategoryFragment frag = AddCategoryFragment.newInstance(projectID, buildingID);

        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.frame_layout, frag);
        t.addToBackStack(null);
        t.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
    public void categoryCreationSuccessful(String buildingID) {
        setResult(RESULT_OK);
        finish();
    }
}
