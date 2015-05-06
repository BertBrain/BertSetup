package bert.ui.roomList;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import bert.database.BertUnit;
import bert.database.Category;
import bert.database.Test;

import bert.database.Project;
import bert.database.ProjectProvider;
import bert.ui.projectList.ProjectListActivity;
import bert.ui.R;

import java.util.ArrayList;
import java.util.List;

public class RoomListActivity extends ActionBarActivity implements DeviceEditorView.OnFragmentInteractionListener, AuditWizardView.OnFragmentInteractionListener {

    String currentBuilding;

    Project selectedProject;
    List<String> buildings;
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
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) return;//if restoring, don't replace
            Bundle extras = getIntent().getExtras();
            try {
                selectedProject = ProjectProvider.getInstance().getProjectList().get(extras.getInt("projectIndex"));
            } catch (Exception e) {
                System.out.println("no project was sent to room list activity, using test project");
                selectedProject = Test.testProject;
            }

            DeviceEditorView auditWizard = new DeviceEditorView();
            auditWizard.setArguments(getIntent().getExtras());

            getFragmentManager().beginTransaction().add(R.id.fragment_container, auditWizard).commit();
        } else {
            System.out.println("error: fragment container not found");
        }
        createBuildingDropdown();
    }


    private void createBuildingDropdown(){
        if (buildings == null || buildings.size() == 0) {
                buildings = selectedProject.getBuildingNames();
        }
        Spinner buildingDropdown = (Spinner) findViewById(R.id.buildingDropdown);
        ArrayAdapter<String> buildingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, buildings);

        buildingDropdown.setAdapter(buildingAdapter);
        buildingDropdown.setSelection(buildings.size() - 1);

        buildingDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setDropdownPosition(position);
                createLocationlistView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setDropdownPosition(0);
                System.out.println("error: nothing selected");
            }
        });
    }

    private void setDropdownPosition(int position){
        currentBuilding = buildings.get(position);
    }

    private void createLocationlistView(){
        ArrayAdapter<String> locationTableAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getLocationsInBuilding(currentBuilding));

        ListView locationListView = (ListView) findViewById(R.id.locationListView);
        locationListView.setAdapter(locationTableAdapter);

        locationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDeviceEditorView(getLocationsInBuilding(currentBuilding).get(position));
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


        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, auditWizardView);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void openDeviceEditorView(String locationName){
        System.out.println("opening device editor view");
        DeviceEditorView deviceEditorView = new DeviceEditorView();

        Bundle args = new Bundle();
        args.putString("location", locationName);
        deviceEditorView.setArguments(args);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, deviceEditorView);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void openProjectActivity(View view) {
        Intent intent = new Intent(this, ProjectListActivity.class);
        startActivity(intent);
    }

    public void launchBuildingNameTextField(View view){
        newBuildingName = (EditText) findViewById(R.id.newBuildingName);
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
        createLocationlistView();
    }

    @Override
    public List<Category> getDeviceTypes() {
        return selectedProject.getCategories();
    }

    @Override
    public List<BertUnit> getBertListForLocation(String location) {
        //TODO: allow different buildings
        return selectedProject.getBertsByLocation(currentBuilding, location);
    }

    public List<String> getLocationsInBuilding(String building) {
        return selectedProject.getLocationNamesInBuilding(building);
    }
}
