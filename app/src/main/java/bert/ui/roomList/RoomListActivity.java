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

    private Project project;
    private int currentBuildingID;

    private Spinner buildingDropdown;
    private Button addBuildingButton;
    private Button startAuditButton;
    private EditText newBuildingName;
    private ArrayAdapter<String> buildingAdapter;

    private ArrayAdapter<String> locationTableAdapter;
    private ListView locationListView;

    private AuditWizardView auditWizardView;

    @Override
    public void onFragmentInteraction(android.net.Uri uri) {}

    public Project getProject() {
        return project;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);
        if (savedInstanceState != null) return;//if restoring, don't replace

        Bundle extras = getIntent().getExtras();
        project = ProjectProvider.getInstance().getProjectList().get(extras.getInt(ARG_PROJECT_INDEX));
        addBuildingButton = (Button) findViewById(R.id.addBuildingButton);
        newBuildingName = (EditText) findViewById(R.id.newBuildingName);
        startAuditButton = (Button) findViewById(R.id.button_audit);
        createLocationlistView();
        createBuildingDropdown();
        boolean isBuildingListEmpty = (project.getBuildingNames().size() == 0);
        openNoSelectionView((isBuildingListEmpty) ? "Create a building" : "Select a Room or create a new one");
        setBuildingDropdownVisibility(!isBuildingListEmpty);
        startAuditButton.setEnabled(!isBuildingListEmpty);
        if (!isBuildingListEmpty) {
            currentBuildingID = 0; //TODO: make this get last viewed building
        }
        this.setTitle("Project: " + project.getProjectName());
    }

    private void createBuildingDropdown() {
        buildingDropdown = (Spinner) findViewById(R.id.buildingDropdown);
        buildingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, project.getBuildingNames());
        buildingDropdown.setAdapter(buildingAdapter);
        buildingDropdown.setSelection(project.getBuildingNames().size() - 1);
        buildingDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentBuildingID = position;
                createLocationlistView();
                setBuildingDropdownVisibility(true);
                openNoSelectionView("Select or Create a Room");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                openNoSelectionView("No Building Selected");
            }
        });
    }

    //TODO should be private
    public void createLocationlistView() {
        locationTableAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, project.getLocationNamesInBuilding(currentBuildingID));
        locationListView = (ListView) findViewById(R.id.locationListView);
        locationListView.setAdapter(locationTableAdapter);
        locationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDeviceEditorView(project.getLocationNamesInBuilding(currentBuildingID).get(position));
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

    public void openAuditWizardView(View view) {
        auditWizardView = AuditWizardView.newInstance(currentBuildingID);
        AuditTallyBoxGVA.resetCounts();
        loadFragment(auditWizardView);
    }

    public void openDeviceEditorView(String locationName) {
        loadFragment(DeviceEditorView.newInstance(currentBuildingID, locationName));
    }

    public void openNoSelectionView(String message) {
        loadFragment(NoSelectionView.newInstance(message));
    }

    private void loadFragment(Fragment frag) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void launchBuildingNameTextField(View view) {
        newBuildingName.setFocusable(true);
        newBuildingName.requestFocus();
        newBuildingName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addBuilding();
                }
                return false;
            }
        });
        InputMethodManager manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void addBuilding() {
        String input = newBuildingName.getText().toString().trim();
        if (!input.isEmpty()) {
            project.getBuildings().add(new Building(input));
            currentBuildingID = project.getBuildingNames().size() - 1;
            newBuildingName.setText("");
            newBuildingName.clearFocus();
            InputMethodManager manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);

            createBuildingDropdown();
            setBuildingDropdownVisibility(true);
            Button startAddWizardButton = (Button) findViewById(R.id.button_audit);
            startAddWizardButton.setEnabled(true);
            createLocationlistView();
            FileProvider.saveProject(project);
        }
    }

    public void setBuildingDropdownVisibility(boolean isVisible) {
        buildingDropdown.setMinimumWidth(0);
        addBuildingButton.setText((isVisible) ? "+" : "+ Add Building");
        //addBuildingButton.setGravity((isVisible) ? Gravity.RIGHT : Gravity.CENTER);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(((isVisible) ? 75 : LinearLayout.LayoutParams.MATCH_PARENT), LinearLayout.LayoutParams.MATCH_PARENT, 1);
        LinearLayout.LayoutParams dropDownParams = new LinearLayout.LayoutParams((isVisible) ? locationListView.getWidth()-75 : 0, LinearLayout.LayoutParams.MATCH_PARENT);
        addBuildingButton.setLayoutParams(buttonParams);
        buildingDropdown.setLayoutParams(dropDownParams);
    }
}
