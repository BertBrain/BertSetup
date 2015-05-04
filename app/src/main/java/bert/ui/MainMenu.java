package bert.ui;

import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.content.Intent;

import bert.ui.R;

import java.util.ArrayList;

public class MainMenu extends ActionBarActivity implements AddProject.OnFragmentInteractionListener{

    @Override
    public void onFragmentInteraction(android.net.Uri uri){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);

        ArrayAdapter<String> projectTableAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getProjects());

        ListView projectListView = (ListView) findViewById(R.id.projectListView);
        projectListView.setAdapter(projectTableAdapter);

        projectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openMain(String.valueOf(position)); //TODO: make this get actual project name
            }
        });

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

    public void openNewProjectView(View view){
        System.out.println("opening new project fragment");
        AddProject newProjectFragment = new AddProject();

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_new_project, newProjectFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //this opens a project after it has been selected from list - list name can be passed
    public void openMain(String project){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //TODO: make this pass a project name
    }

    //opens a project when new project is pressed - project must be found through view
    public void openMain(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        //TODO: make this pass a project name
    }

    //TODO: get from database
    public ArrayList<String> getProjects(){
        ArrayList<String> projects = new ArrayList<String>();
        projects.add("SCH");
        projects.add("Haverford");
        projects.add("Other School");
        return projects;
    }
}