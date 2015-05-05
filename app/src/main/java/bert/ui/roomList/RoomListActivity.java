package bert.ui.roomList;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import bert.database.BertUnit;
import bert.database.Project;
import bert.database.ProjectProvider;
import bert.ui.projectList.ProjectListActivity;
import bert.ui.R;

import java.util.List;

public class RoomListActivity extends ActionBarActivity implements DeviceEditorView.OnFragmentInteractionListener, AuditWizardView.OnFragmentInteractionListener {

    Project selectedProject;

    @Override
    public void onFragmentInteraction(android.net.Uri uri) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) return;//if restoring, don't replace
            Bundle extras = getIntent().getExtras();
            selectedProject = ProjectProvider.getInstance().getProjectList().get(extras.getInt("projectIndex"));
            DeviceEditorView auditWizard = new DeviceEditorView();
            auditWizard.setArguments(getIntent().getExtras());

            getFragmentManager().beginTransaction().add(R.id.fragment_container, auditWizard).commit();
        }
        else {
            System.out.println("error: fragment container not found");
        }


        ArrayAdapter<String> locationTableAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getLocations());

        ListView locationListView = (ListView) findViewById(R.id.locationListView);
        locationListView.setAdapter(locationTableAdapter);

        locationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDeviceEditorView(getLocations().get(position));
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

    @Override
    public List<String> getDeviceTypes() {
        return selectedProject.getCategoryNames();
    }

    @Override
    public List<BertUnit> getBertListForLocation(String location) {
        //TODO: allow different buildings
        return selectedProject.getBertsByLocation("Math", location);
    }

    public List<String> getLocations() {
        return selectedProject.getLocationNames();
    }
}
