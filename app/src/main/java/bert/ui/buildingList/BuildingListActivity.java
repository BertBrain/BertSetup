package bert.ui.buildingList;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
        setTitle(project.getProjectName() + " Buildings (" + project.getBuildings().size() + ")");

        addBuildingButton = (Button) findViewById(R.id.create_list_item_button);
        addBuildingButton.setText("Add Building");
        addBuildingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddBuildingView();
            }
        });

        buildingListView = (ListView) findViewById(R.id.item_list_view);
        loadListView();
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
        loadListView();
        clearFragmentContainer();
    }

    public void clearFragmentContainer() {
        int numberOfBuildings = project.getBuildings().size();
        String message = numberOfBuildings > 0 ? "Select or Create A Building" : "Create a Building";
        loadFragment(NoSelectionFragment.newInstance(message));
    }

    private void openAddBuildingView(){
        loadFragment(AddBuildingFragment.newInstance(projectID));
    }

    private void openBuildingDetailView(int buildingID){
        loadFragment(BuildingDetailFragment.newInstance(projectID, buildingID));
    }

    public void loadListView() {
        buildingListViewAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                project.getBuildingNames());
        buildingListView.setAdapter(buildingListViewAdapter);
    }

    private void loadFragment(Fragment frag) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame_layout, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
