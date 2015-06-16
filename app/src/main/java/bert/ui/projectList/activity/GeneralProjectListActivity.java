package bert.ui.projectList.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

import bert.data.ProjectProvider;
import bert.ui.R;
import bert.ui.common.NoSelectionFragment;

/**
 * Created by liamcook on 5/29/15.
 */
abstract public class GeneralProjectListActivity extends ActionBarActivity {

    private ListView projectListView;
    private Button addProjectButton;
    private ArrayAdapter<String> projectTableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail);
        addProjectButton = (Button) findViewById(R.id.create_list_item_button);
        addProjectButton.setText("Add Project");
        addProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddProjectView(); //is abstract
            }
        });
        openNoSelectionView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        projectTableAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getProjects()); //abstract

        projectListView = (ListView) findViewById(R.id.item_list_view);
        projectListView.setAdapter(projectTableAdapter);


        projectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openProjectDetailView(position);
            }
        });

        int projectListSize = ProjectProvider.getInstance().getTotalProjects();
        this.setTitle(getTitlePrefix() + ", " + ((projectListSize == 1) ? ("1 Project") : (projectListSize + " Projects")));
    }

    abstract public List<String> getProjects();

    abstract public void openProjectDetailView(int projectIndex);
    abstract public String getTitlePrefix();

    public void closeAddProjectView() {
        openNoSelectionView();
    }

    abstract public void openAddProjectView();

    protected void openNoSelectionView() {
        loadFragment(NoSelectionFragment.newInstance("Select or Create a Project"));
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
