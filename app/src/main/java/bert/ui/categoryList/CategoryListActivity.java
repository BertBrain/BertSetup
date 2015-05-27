package bert.ui.categoryList;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import bert.data.ProjectProvider;
import bert.data.proj.Building;
import bert.data.proj.Project;
import bert.ui.NoSelectionFragment;
import bert.ui.R;

public class CategoryListActivity extends ActionBarActivity implements CategoryDetailFragment.OnFragmentInteractionListener, AddCategoryFragment.OnFragmentInteractionListener {

    public static final String ARG_PROJECT_ID = "PROJECT_ID";
    public static final String ARG_BUILDING_ID = "Building_ID";

    private int projectID;
    private String buildingID;

    private Project project;
    private Building building;

    private Button addCategoryButton;
    private ListView categoryListView;
    private ArrayAdapter<String> categoryListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail);

        projectID = getIntent().getExtras().getInt(ARG_PROJECT_ID);
        buildingID = getIntent().getExtras().getString(ARG_BUILDING_ID);

        project = ProjectProvider.getInstance().getProject(projectID);
        building = project.getBuilding(buildingID);

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
        categoryListViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, project.getBuilding(buildingID).getCategoryNames());
        categoryListView = (ListView) findViewById(R.id.item_list_view);
        categoryListView.setAdapter(categoryListViewAdapter);
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openCategoryDetailFragment(categoryListViewAdapter.getItem(position));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        this.setTitle(project.getBuilding(buildingID).getCategoryCount() + " Categories");
    }

    public void openCategoryDetailFragment(String categoryID) {
        loadFragment(new CategoryDetailFragment().newInstance(projectID, buildingID, categoryID));
    }

    public void openNoSelectionFragment() {
        loadFragment(NoSelectionFragment.newInstance("Select or Create a Category"));
    }

    public void openCategoryAddFragment() {
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
        if (project.getBuilding(buildingID).getCategoryCount() > 0) {
            openCategoryDetailFragment(building.getCategoryNames().get(0));
        }
    }

    @Override
    public void categoryCreationSuccessful(String buildingID) {
        createCategoryListView();
        openCategoryDetailFragment(buildingID);
    }
}