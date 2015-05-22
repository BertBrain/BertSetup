package bert.ui.buildingList;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import bert.data.ProjectProvider;
import bert.data.proj.Project;
import bert.ui.NoSelectionFragment;
import bert.ui.R;

public class BuildingListActivity extends ActionBarActivity {

    public static final String ARG_PROJECT_ID = "PROJECT_ID";

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
        project = ProjectProvider.getInstance().getProject(projectID);

        addBuildingButton = (Button) findViewById(R.id.create_list_item_button);
        addBuildingButton.setText("Add Building");
        addBuildingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddBuildingView();
            }
        });

        clearFragmentContainer();
    }

    @Override
    public void onResume() {
        super.onResume();

        buildingListView = (ListView) findViewById(R.id.item_list_view);
        buildingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = project.getOrderedBuildingKeys().get(position);
                openBuildingDetailView(index);
            }
        });

        setTitle(project.getProjectName() + " Buildings (" + project.getBuildingCount() + ")");
        loadListView();
    }



    public void clearFragmentContainer() {
        int numberOfBuildings = project.getBuildingCount();
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
        buildingListViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, project.getOrderedBuildingNames());
        buildingListView.setAdapter(buildingListViewAdapter);
        Log.d("BuildingListActivity", "Loaded: " + project.getBuildingNames().size() + " buildings");
    }

    public void loadFragment(Fragment frag) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame_layout, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
