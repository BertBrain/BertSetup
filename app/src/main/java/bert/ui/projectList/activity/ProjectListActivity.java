package bert.ui.projectList.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import bert.data.ProjectProvider;
import bert.data.proj.Project;
import bert.data.proj.exceptions.InvalidProjectNameException;
import bert.ui.R;
import bert.ui.common.BertAlert;
import bert.ui.common.NoSelectionFragment;
import bert.ui.common.OnListClickedListener;
import bert.ui.common.SelectableListGVA;

/**
 * Created by liamcook on 5/29/15.
 * @author lcook
 * @author afiolmahon
 */
abstract public class ProjectListActivity extends ActionBarActivity {

    public static final String ARG_PROJECT_ID = "Project ID"; //Used to open the activity into a project detail view if desired

    private ListView projectListView;
    private Button addProjectButton;
    public SelectableListGVA projectListAdapter;

    abstract public List<String> getProjects();

    abstract public void openProjectDetailView(String projectID);

    abstract public String getTitlePrefix();

    abstract public String getNewProjectButtonName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getType() != null && getIntent().getType().equals("text/xml")) {
            Log.d("RECIEVING", "recieved XML");
            Uri uri = getIntent().getData();
            File file = new File(uri.getPath());
            Log.d("RECIEVING", "text: " + uri.toString());

            try {
                InputStream stream = getContentResolver().openInputStream(uri);
                Project newProject = Project.loadProject(stream);
                if (newProject != null) {
                    ProjectProvider.getInstance().addProject(newProject);
                    getIntent().putExtra(ARG_PROJECT_ID, newProject.getProjectName());
                    if (newProject.isAudit()){
                        getIntent().setClass(this, AuditProjectListActivity.class);
                    } else {
                        getIntent().setClass(this, InstallProjectListActivity.class);
                    }

                } else {
                    BertAlert.show(this, "There was an error importing the project");
                }
            } catch (InvalidProjectNameException e) {
                BertAlert.show(this, "The project name is invalid or is a duplicate so it cannot be imported.");
            } catch (FileNotFoundException e){
                BertAlert.show(this, "Error: file not fount");
            }
        }

        setContentView(R.layout.common_activity_master_detail);
        addProjectButton = (Button) findViewById(R.id.create_list_item_button);
        addProjectButton.setText(getNewProjectButtonName());
        addProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                projectListAdapter.clearSelection();
                openAddProjectView();
                projectListAdapter.clearSelection();
            }
        });

        loadListView();

        //TODO verify
        if (getIntent().hasExtra(ARG_PROJECT_ID)) {
            String projectID = getIntent().getExtras().getString(ARG_PROJECT_ID);
            Log.d("ProjectListActivity", "ProjectID (" + projectID + ") in intent, loading project detail view.");
            loadProject(projectID);
        } else {
            Log.d("ProjectListActivity", "No projectID passed in intent, loading no selection view");
            openNoSelectionView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        this.setTitle(getTitlePrefix() + ": " + ((getProjects().size() == 1) ? ("1 Project") : (getProjects().size() + " Projects")));
    }

    public void loadListView() {
        projectListAdapter = new SelectableListGVA(this, getProjects());
        projectListAdapter.setListener(new OnListClickedListener(){
            @Override
            public void onTitlePressed(String project) {
                loadProject(project);
            }
        });

        projectListView = (ListView) findViewById(R.id.item_list_view);
        projectListView.setAdapter(projectListAdapter);

    }

    abstract public void openAddProjectView();

    public void loadProject(String projectID) {

        int position = projectListAdapter.titles.indexOf(projectID);
        openProjectDetailView(projectListAdapter.getItem(position));
        projectListAdapter.indexPressed(position);

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
}
