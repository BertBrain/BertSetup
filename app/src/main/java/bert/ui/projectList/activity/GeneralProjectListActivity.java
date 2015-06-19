package bert.ui.projectList.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.util.List;

import bert.data.ProjectProvider;
import bert.data.proj.Project;
import bert.data.proj.exceptions.InvalidProjectNameException;
import bert.ui.R;
import bert.ui.common.BertAlert;
import bert.ui.common.NoSelectionFragment;
import bert.ui.common.SelectableListGVA;

/**
 * Created by liamcook on 5/29/15.
 */
abstract public class GeneralProjectListActivity extends ActionBarActivity {

    public static final String ARG_PROJECT_ID = "Project ID";

    private ListView projectListView;
    private Button addProjectButton;
    private SelectableListGVA projectTableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getType() != null && getIntent().getType().equals("text/xml")) {
            Log.d("RECIEVING", "recieved XML");
            Uri uri = getIntent().getData();
            File recievedFile = new File(uri.getPath());
            Log.d("RECIEVING", "text: " + uri.toString());
            Log.d("RECIEVING", "file: " + recievedFile.toString());


            try {
                Project newProject = Project.loadProject(recievedFile);
                if (newProject != null) {
                    ProjectProvider.getInstance().addProject(newProject);
                } else {
                    BertAlert.show(this, "There was an error importing the project");
                }


                //create, import modify
                //add/edit/view buildings
                //create new audit
                //send install sheets
                //tally berts by M, V, and C

            } catch (InvalidProjectNameException e) {
                BertAlert.show(this, "This project already exists so it could not be imported.");
            }
        }

        setContentView(R.layout.activity_master_detail);
        addProjectButton = (Button) findViewById(R.id.create_list_item_button);
        addProjectButton.setText("Add Project");
        addProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddProjectView();
                projectTableAdapter.clear();
            }
        });
        openNoSelectionView();
    }

    @Override
    public void onResume() {
        super.onResume();
        projectTableAdapter = new SelectableListGVA(this, getProjects());

        projectListView = (ListView) findViewById(R.id.item_list_view);
        projectListView.setAdapter(projectTableAdapter);
        projectTableAdapter.setOnClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                openProjectDetailView(position);
            }
        });

        int projectListSize = ProjectProvider.getInstance().getTotalProjects();
        this.setTitle(getTitlePrefix() + ", " + ((projectListSize == 1) ? ("1 Project") : (projectListSize + " Projects")));
        try {
            String selectedProjectID = getIntent().getExtras().getString(ARG_PROJECT_ID);
            loadProject(selectedProjectID);
        } catch (NullPointerException e){

        }
    }

    abstract public List<String> getProjects();

    abstract public void openProjectDetailView(int projectIndex);
    abstract public String getTitlePrefix();

    public void closeAddProjectView() {
        openNoSelectionView();
    }

    abstract public void openAddProjectView();

    public void loadProject(String projectID){
        int position = projectTableAdapter.titles.indexOf(projectID);
        if (position > -1){
            openProjectDetailView(position);
            projectTableAdapter.selectView(position);
        }
    }

    public void openNoSelectionView() {
        loadFragment(NoSelectionFragment.newInstance("Create a project or select a project from the list of projects on the left"));
    }

    protected void loadFragment(Fragment frag) {
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.fragment_frame_layout, frag);
        t.addToBackStack(null);
        t.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_audit_project_list, menu);
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
