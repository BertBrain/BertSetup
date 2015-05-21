package bert.ui.buildingList;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import bert.data.ProjectProvider;
import bert.data.proj.Project;
import bert.ui.NoSelectionFragment;
import bert.ui.R;

public class BuildingListActivity extends ActionBarActivity {

    public static String ARG_PROJECT_ID = "PROJECT_ID";

    private Button addBuildingButton;
    private ArrayAdapter<String> buildingListViewAdapter;
    private ListView buildingListView;
    private int projectID;
    private Project project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail);

        Bundle extras = getIntent().getExtras();
        projectID = extras.getInt(ARG_PROJECT_ID);
        project = ProjectProvider.getInstance().getProjectList().get(projectID);

        addBuildingButton = (Button) findViewById(R.id.create_list_item_button);
        addBuildingButton.setText("Add Building");
        addBuildingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddBuildingView();
            }
        });

        buildingListView = (ListView) findViewById(R.id.item_list_view);
        buildingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openBuildingDetailView(position);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        setTitle(project.getProjectName() + " Buildings (" + project.getBuildings().size() + ")");
        loadListView();
        clearFragmentContainer();
    }

    public void clearFragmentContainer() {
        int numberOfBuildings = project.getBuildings().size();
        String message = numberOfBuildings > 0 ? "Select or Create A Building" : "Create a Building";
        loadFragment(NoSelectionFragment.newInstance(message));
    }

    private void openAddBuildingView() {
        loadFragment(AddBuildingFragment.newInstance(projectID));
    }

    protected void openBuildingDetailView(int buildingID) {
        loadFragment(BuildingDetailFragment.newInstance(projectID, buildingID));
    }

    public void loadListView() {
        buildingListViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, project.getBuildingNames());
        buildingListView.setAdapter(buildingListViewAdapter);
        Log.d("BuildingListActivity", "Loaded: " + project.getBuildingNames().size() + " buildings");
    }

    private void loadFragment(Fragment frag) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame_layout, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
