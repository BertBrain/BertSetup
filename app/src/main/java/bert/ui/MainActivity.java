package bert.ui;

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

import com.example.liamcook.bert.R;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity implements DeviceEditorView.OnFragmentInteractionListener, AuditWizardView.OnFragmentInteractionListener{

    @Override
    public void onFragmentInteraction(android.net.Uri uri) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.fragment_container) != null) {

            //if restoring, don't replace
            if (savedInstanceState != null) {
                return;
            }

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
            openDeviceEditorView(getLocations().get(position)); // TODO: get name of location from database

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

    public void openMenu(View view){
        Intent intent = new Intent(this, MainMenu.class);
        startActivity(intent);
    }

    //TODO: all below should be from database
    @Override public ArrayList<String> getDeviceTypes(){
        ArrayList<String> deviceTypes = new  ArrayList<String>();
        deviceTypes.add("Coffe Machine");
        deviceTypes.add("Printer");
        deviceTypes.add("Projector");
        return deviceTypes;
    }

    public ArrayList<String> getLocations(){
        ArrayList<String> locations = new ArrayList<String>();
        locations.add("Gym");
        locations.add("Cafeteria");
        locations.add("101");
        locations.add("102");
        return locations;
    }


}
