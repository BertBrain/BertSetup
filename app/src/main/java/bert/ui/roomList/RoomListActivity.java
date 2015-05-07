package bert.ui.roomList;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import bert.database.BertUnit;
import bert.database.Category;
import bert.database.Test;

import bert.database.Project;
import bert.database.ProjectProvider;
import bert.ui.NoSelectionView;
import bert.ui.projectList.ProjectListActivity;
import bert.ui.R;

import java.util.ArrayList;
import java.util.List;

public class RoomListActivity extends ActionBarActivity implements DeviceEditorView.OnFragmentInteractionListener, AuditWizardView.OnFragmentInteractionListener {

    Project selectedProject;
    String currentBuilding;
    List<String> buildings;
    ListView locationListView;

    Spinner buildingDropdown;
    Button addBuildingButton;
    EditText newBuildingName;

    @Override
    public void onFragmentInteraction(android.net.Uri uri) {

    }

    @Override
    public String getBuilding(){
        return currentBuilding;
    }

    @Override
    public void addBerts(ArrayList<BertUnit> berts ){
        selectedProject.addBerts(berts);
        createLocationlistView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        if (savedInstanceState != null) return;//if restoring, don't replace
        Bundle extras = getIntent().getExtras();
        try {
            selectedProject = ProjectProvider.getInstance().getProjectList().get(extras.getInt("projectIndex"));
        } catch (Exception e) {
            System.out.println("no project was sent to room list activity, using test project");
            selectedProject = Test.testProject;
        }

        buildingDropdown = (Spinner) findViewById(R.id.buildingDropdown);
        addBuildingButton = (Button) findViewById(R.id.addBuildingButton);
        newBuildingName = (EditText) findViewById(R.id.newBuildingName);
        locationListView = (ListView) findViewById(R.id.locationListView);
        buildings = selectedProject.getBuildingNames();
        if ( buildings.size() == 0) {
            openNoSelectionView("Create a building");
            setDropdownVisiblity(false);
            Button startAddWizardButton = (Button) findViewById(R.id.button_audit);
            startAddWizardButton.setEnabled(false);
        } else {
            openNoSelectionView("Select A Room or create a new one");
            currentBuilding = buildings.get(0); //TODO: make this get last viewed building
            setDropdownVisiblity(true);
        }
        createBuildingDropdown();
    }

    private void createBuildingDropdown(){

        ArrayAdapter<String> buildingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, buildings);

        buildingDropdown.setAdapter(buildingAdapter);
        buildingDropdown.setSelection(buildings.size() - 1);

        buildingDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setDropdownPosition(position);
                createLocationlistView();
                openNoSelectionView("Select or Create a Room");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                openNoSelectionView("No Building Selected");
                System.out.println("error: nothing selected");
            }
        });
    }

    private void setDropdownPosition(int position){
        currentBuilding = buildings.get(position);
    }

    private void createLocationlistView(){
        ArrayAdapter<String> locationTableAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, selectedProject.getLocationNamesInBuilding(currentBuilding));

        ListView locationListView = (ListView) findViewById(R.id.locationListView);
        locationListView.setAdapter(locationTableAdapter);

        locationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDeviceEditorView(selectedProject.getLocationNamesInBuilding(currentBuilding).get(position));
            }
        });
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

    public void openAuditWizardView(View view){
        System.out.println("opening audit wizard view");

        AuditWizardView auditWizardView = new AuditWizardView();
        loadFragment(auditWizardView);
    }

    public void openDeviceEditorView(String locationName){
        System.out.println("opening device editor view");

        DeviceEditorView deviceEditorView = new DeviceEditorView();

        Bundle args = new Bundle();
        args.putString("location", locationName);
        deviceEditorView.setArguments(args);

        loadFragment(deviceEditorView);
    }

    public void openNoSelectionView(String message){
        System.out.println("openening no location view");
        NoSelectionView noSelectionView = new NoSelectionView();

        Bundle args = new Bundle();
        args.putString("message", message);
        noSelectionView.setArguments(args);

        loadFragment(noSelectionView);
    }

    private void loadFragment(Fragment frag){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void openProjectActivity(View view) {
        startActivity(new Intent(this, ProjectListActivity.class));
    }

    public void launchBuildingNameTextField(View view){

        newBuildingName.setFocusable(true);
        newBuildingName.requestFocus();
        newBuildingName.setOnEditorActionListener(new TextView.OnEditorActionListener(){
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    addBuilding();
                }
                return  false;
            }
        });
        InputMethodManager manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void addBuilding(){
        currentBuilding = newBuildingName.getText().toString();
        buildings.add(currentBuilding);
        newBuildingName.setText("");
        newBuildingName.clearFocus();
        InputMethodManager manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);

        createBuildingDropdown();
        setDropdownVisiblity(true);
        Button startAddWizardButton = (Button) findViewById(R.id.button_audit);
        startAddWizardButton.setEnabled(true);
        createLocationlistView();
    }

    public void setDropdownVisiblity(boolean visible){
        addBuildingButton = (Button) findViewById(R.id.addBuildingButton);

        if (visible){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(75,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1);
            addBuildingButton.setLayoutParams(layoutParams);

            buildingDropdown.setMinimumWidth(0);
            LinearLayout.LayoutParams dropdownLayoutParams = new LinearLayout.LayoutParams(locationListView.getWidth()-75,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            buildingDropdown.setLayoutParams(dropdownLayoutParams);

            addBuildingButton.setGravity(Gravity.RIGHT);
            addBuildingButton.setText("+");
        } else {
            LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            addBuildingButton.setLayoutParams(buttonLayoutParams);

            buildingDropdown.setMinimumWidth(0);
            LinearLayout.LayoutParams dropdownLayoutParams = new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            buildingDropdown.setLayoutParams(dropdownLayoutParams);

            addBuildingButton.setGravity(Gravity.CENTER);
            addBuildingButton.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            addBuildingButton.setText("+ Add Building");
        }
    }

    @Override
    public List<Category> getDeviceTypes() {
        return selectedProject.getCategories();
    }

    @Override
    public List<BertUnit> getBertListForLocation(String location) {
        return selectedProject.getBertsByLocation(currentBuilding, location);
    }
}
