package bert.ui.buildingList.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import bert.data.ProjectProvider;
import bert.data.proj.Project;
import bert.ui.R;
import bert.ui.buildingList.AddBuildingFragment;
import bert.ui.common.NoSelectionFragment;
import bert.ui.common.SelectableListGVA;

/**
 * Created by liamcook on 5/29/15.
 */
abstract public class GeneralBuildingListActivity extends ActionBarActivity {

    public static final String ARG_PROJECT_ID = "PROJECT_ID";

    private Button addBuildingButton;
    private SelectableListGVA buildingListViewAdapter;
    private ListView buildingListView;
    static protected String projectID;
    static protected Project project;
    public InputMethodManager inputManager;

    abstract protected void openBuildingDetailView(String buildingID);
    abstract public String getTitlePrefix();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            projectID = extras.getString(ARG_PROJECT_ID);
        }
        project = ProjectProvider.getInstance().getProject(projectID);
        addBuildingButton = (Button) findViewById(R.id.create_list_item_button);
        addBuildingButton.setText("Add Building");
        addBuildingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddBuildingView();
            }
        });

        inputManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        clearFragmentContainer();
    }

    @Override
    public void onResume() {
        super.onResume();

        setTitle(getTitlePrefix() + ": " + project.getProjectName() + " Buildings (" + project.getBuildingCount() + ")");
        loadListView();
    }

    public void clearFragmentContainer() {
        int numberOfBuildings = project.getBuildingCount();
        String message = numberOfBuildings > 0 ? "Select or Create A Building" : "Create a Building";
        loadFragment(NoSelectionFragment.newInstance(message));
    }

    private void openAddBuildingView() {
        buildingListViewAdapter.clear();
        loadFragment(AddBuildingFragment.newInstance(projectID));
    }

    public void loadListView() {
        buildingListViewAdapter = new SelectableListGVA(this, project.getBuildingNames());

        buildingListView = (ListView) findViewById(R.id.item_list_view);
        buildingListView.setAdapter(buildingListViewAdapter);
        buildingListViewAdapter.setOnClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                generalOpenBuildingDetailView(buildingListViewAdapter.getItem(position));
            }
        });

        Log.d("BuildingListActivity", "Loaded: " + project.getBuildingNames().size() + " buildings");
    }

    public void generalOpenBuildingDetailView(String buildingID){
        int position = buildingListViewAdapter.titles.indexOf(buildingID);
        buildingListViewAdapter.selectView(position);
        openBuildingDetailView(buildingID);
    }

    public void loadFragment(Fragment frag) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame_layout, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
