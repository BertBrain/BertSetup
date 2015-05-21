package bert.ui.categoryList;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import bert.data.ProjectProvider;
import bert.data.proj.Project;
import bert.ui.R;

public class CategoryListActivity extends ActionBarActivity implements CategoryDetailFragment.OnFragmentInteractionListener, AddCategoryFragment.OnFragmentInteractionListener {

    public static final String ARG_PROJECT_ID = "PROJECT_ID";
    public static final String ARG_BUILDING_ID = "Building_ID";

    private int projectID;
    private int buildingID;
    private Project project;

    private Button addCategoryButton;
    private ListView categoryListView;
    private ArrayAdapter<String> categoryListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail);

        projectID = getIntent().getExtras().getInt(ARG_PROJECT_ID);
        project = ProjectProvider.getInstance().getProjectList().get(projectID);
        buildingID = getIntent().getExtras().getInt(ARG_BUILDING_ID);

        addCategoryButton = (Button) findViewById(R.id.create_list_item_button);
        addCategoryButton.setText("Add Category");
        addCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryAddFragment();
            }
        });

        createCategoryListView();
    }

    public void createCategoryListView() {
        categoryListViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, project.getBuildings().get(buildingID).getCategoryNames());
        categoryListView = (ListView) findViewById(R.id.item_list_view);
        categoryListView.setAdapter(categoryListViewAdapter);
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openCategoryDetailFragment(position);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        this.setTitle(project.getBuildings().get(buildingID).getCategories().size() + " Categories");
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

    private void openCategoryDetailFragment(int index) {
        loadFragment(new CategoryDetailFragment().newInstance(projectID, buildingID, index));
    }

    private void openCategoryAddFragment() {
        loadFragment(AddCategoryFragment.newInstance(projectID, buildingID));
    }

    private void loadFragment(Fragment frag) {
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.fragment_frame_layout, frag);
        t.addToBackStack(null);
        t.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}

    @Override
    public void categoryCreationCanceled() {
        if (project.getBuildings().get(buildingID).getCategories().size() > 0) {
            openCategoryDetailFragment(0);
        }
    }

    @Override
    public void categoryCreationSuccessful() {
        createCategoryListView();
        openCategoryDetailFragment(project.getBuildings().get(buildingID).getCategories().size() - 1);
    }
}