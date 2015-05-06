package bert.ui.projectList;

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

import java.util.ArrayList;
import java.util.List;

import bert.database.Project;
import bert.database.ProjectProvider;
import bert.ui.R;
import bert.ui.roomList.NoSelectionView;
import bert.ui.roomList.RoomListActivity;

public class ProjectListActivity extends ActionBarActivity implements AddProjectView.OnFragmentInteractionListener{

    @Override
    public void onFragmentInteraction(android.net.Uri uri){

    }

    @Override
    public void closeCreateProjectView(){
        NoSelectionView emptyView = new NoSelectionView();
        Bundle args = new Bundle();
        args.putString("message", "");
        emptyView.setArguments(args);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_new_project, emptyView);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_menu);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        loadProjectList();
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
        AddProjectView newProjectFragment = new AddProjectView();

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container_new_project, newProjectFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //this opens a project after it has been selected from list - list name can be passed
    public void openMain(int projectListIndex){
        Intent i = new Intent(this, RoomListActivity.class);
        i.putExtra("projectIndex", projectListIndex);
        startActivity(i);
    }

    public List<String> getProjectNameList() {
        List<String> names = new ArrayList<>();
        for (Project p : ProjectProvider.getInstance().getProjectList()) {
            names.add(p.getProjectName());
        }
        return names;
    }

    public void loadProjectList() {
        ArrayAdapter<String> projectTableAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getProjectNameList());

        ListView projectListView = (ListView) findViewById(R.id.projectListView);
        projectListView.setAdapter(projectTableAdapter);

        projectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openMain(position);
            }
        });
    }
}
