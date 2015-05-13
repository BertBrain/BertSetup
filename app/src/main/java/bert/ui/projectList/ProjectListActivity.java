package bert.ui.projectList;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.content.Intent;

import bert.data.ProjectProvider;
import bert.ui.R;
import bert.ui.NoSelectionView;
import bert.ui.roomList.RoomListActivity;

public class ProjectListActivity extends ActionBarActivity implements AddProjectView.OnFragmentInteractionListener, ProjectDetailFragment.OnFragmentInteractionListener {

    private ListView projectListView;
    private Button addProjectButton;
    private FrameLayout detailFrame;
    private ArrayAdapter<String> projectTableAdapter;

    @Override
    public void onFragmentInteraction(android.net.Uri uri) {}

    @Override
    public void openAddProjectView() {
        loadFragment(new AddProjectView());
    }

    public void openProjectDetailView(int projectIndex) {
        loadFragment(ProjectDetailFragment.newInstance(projectIndex));
    }

    @Override
    public void closeAddProjectView() {
        loadFragment(new NoSelectionView());
    }

    private void loadFragment(Fragment frag) {
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.fragment_frame_layout, frag);
        t.addToBackStack(null);
        t.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail);
        addProjectButton = (Button) findViewById(R.id.create_list_item_button);
        addProjectButton.setText("Add Project");
        addProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddProjectView();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    public void loadProjectList() {
        projectTableAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ProjectProvider.getInstance().getProjectNameList());
        projectListView = (ListView) findViewById(R.id.item_list_view);
        projectListView.setAdapter(projectTableAdapter);
        projectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openProjectDetailView(position);
            }
        });
    }
}
