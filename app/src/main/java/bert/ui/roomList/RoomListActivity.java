package bert.ui.roomList;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import bert.data.FileProvider;
import bert.data.proj.Building;
import bert.data.proj.Category;
import bert.data.proj.Project;
import bert.data.ProjectProvider;
import bert.ui.NoSelectionView;
import bert.ui.R;

import java.util.List;

public class RoomListActivity extends ActionBarActivity implements DeviceEditorView.OnFragmentInteractionListener, AuditWizardView.OnFragmentInteractionListener {

    public static final String ARG_PROJECT_INDEX = "projectIndex";
    public static final String ARG_BUILDING_ID = "buildingID";

    private Project project;
    private int buildingID;

    private Button startAuditButton;

    private ArrayAdapter<String> locationTableAdapter;
    private ListView locationListView;

    @Override
    public void onFragmentInteraction(android.net.Uri uri) {}

    public Project getProject() {
        return project;
    }

    @Override
    public void onResume(){
        super.onResume();
        createLocationlistView();
        clearFragmentContainer();
    }

    private void clearFragmentContainer(){
        int numberOfRooms = project.getLocationNamesInBuilding(buildingID).size();
        String message = numberOfRooms > 0 ? "Select or Create A Room" : "Create a Room";
        loadFragment(NoSelectionView.newInstance(message));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail);
        if (savedInstanceState != null) return;//if restoring, don't replace

        Bundle extras = getIntent().getExtras();
        buildingID = extras.getInt(ARG_BUILDING_ID);
        project = ProjectProvider.getInstance().getProjectList().get(extras.getInt(ARG_PROJECT_INDEX));

        startAuditButton = (Button) findViewById(R.id.create_list_item_button);
        startAuditButton.setText("Audit Wizard");
        startAuditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAuditWizardView();
            }
        });

        createLocationlistView();
        this.setTitle("Project: [" + project.getProjectName() + "] Building: [" + project.getBuildings().get(buildingID).getName() + "]");
    }

    //TODO should be private
    public void createLocationlistView() {
        locationTableAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, project.getLocationNamesInBuilding(buildingID));
        locationListView = (ListView) findViewById(R.id.item_list_view);
        locationListView.setAdapter(locationTableAdapter);
        locationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDeviceEditorView(project.getLocationNamesInBuilding(buildingID).get(position));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK){
            System.out.println("activty result achieved in activity: " + data.getExtras().get("category"));
            Category newCategory = (Category) data.getExtras().get("category");
            System.out.println("cateogry name: " + newCategory.getName());
            project.addCategory(newCategory);

        } if (resultCode == RESULT_CANCELED) {
            System.out.println("add category canceled");
        }
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

    public void openAuditWizardView() {
        AuditWizardView auditWizardView = AuditWizardView.newInstance(buildingID);
        AuditTallyBoxGVA.resetCounts();
        loadFragment(auditWizardView);
    }

    public void openDeviceEditorView(String locationName) {
        loadFragment(DeviceEditorView.newInstance(buildingID, locationName));
    }

    public void openNoSelectionView(String message) {
        loadFragment(NoSelectionView.newInstance(message));
    }

    private void loadFragment(Fragment frag) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_frame_layout, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
