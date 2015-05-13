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
import android.widget.FrameLayout;
import android.widget.ListView;

import bert.data.ProjectProvider;
import bert.data.proj.Project;
import bert.ui.R;

public class CategoryListActivity extends ActionBarActivity implements CategoryDetailFragment.OnFragmentInteractionListener {

    public static final String ARG_PROJECT_ID = "PROJECT_ID";

    private int projectID;
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

        addCategoryButton = (Button) findViewById(R.id.create_list_item_button);
        addCategoryButton.setText("Add Category");
        createCategoryListView();
    }

    public void createCategoryListView() {
        categoryListViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, project.getCategoryNames());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category_list, menu);
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
        loadFragment(new CategoryDetailFragment().newInstance(projectID, index));
    }

    private void loadFragment(Fragment frag) {
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.fragment_frame_layout, frag);
        t.addToBackStack(null);
        t.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}
}
