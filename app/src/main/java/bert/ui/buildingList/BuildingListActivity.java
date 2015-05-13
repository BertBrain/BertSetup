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
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.List;

import bert.data.ProjectProvider;
import bert.ui.NoSelectionView;
import bert.ui.R;

public class BuildingListActivity extends ActionBarActivity {

    public static String PROJECT_ID_KEY = "PROJECT_ID_KEY";

    Button addBuildingButton;
    ListView buildingListView;
    int projectID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail);

        Bundle extras = getIntent().getExtras();
        projectID = extras.getInt(PROJECT_ID_KEY);

        setTitle("Project: " + ProjectProvider.getInstance().getProjectList().get(projectID).getProjectName());

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
        reloadListView();
        clearFragmentContainer();
    }

    public void clearFragmentContainer(){
        int numberOfBuildings = ProjectProvider.getInstance().getProjectList().get(projectID).getBuildings().size();
        String message = numberOfBuildings > 0 ? "Select or Create A Building" : "Create a Building";
        loadFragment(NoSelectionView.newInstance(message));
    }

    private void openAddBuildingView(){
        loadFragment(AddBuildingView.newInstance(projectID));
    }

    private void openBuildingDetailView(int buildingID){
        loadFragment(BuildingDetailView.newInstance(projectID, buildingID));
    }

    public void reloadListView() {loadListView();}

    private void loadListView(){
        List<String> buildingNames = ProjectProvider.getInstance().getProjectList().get(projectID).getBuildingNames();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, buildingNames);
        buildingListView.setAdapter(adapter);
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
        getMenuInflater().inflate(R.menu.menu_building_list, menu);
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
