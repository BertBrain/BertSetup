package bert.ui.projectList;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import bert.data.FileProvider;
import bert.data.ProjectProvider;
import bert.data.proj.Category;
import bert.data.proj.Project;
import bert.ui.R;

public class CategoryEditorActivity extends ActionBarActivity {

    public static final String ARG_PROJECT_ID = "PROJECT_ID";

    private int projectID;
    private Project project;
    private Category focusedCategory;

    private GridLayout categoryEditorLayout;

    private ListView categoryListView;
    private ArrayAdapter<String> categoryListViewAdapter;
    private EditText categoryNameEditText;
    private EditText estimatedLoadEditText;
    private Spinner bertTypeSpinner;
    private ArrayAdapter<String> bertTypeSpinnerAdapter;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_editor);
        this.projectID = getIntent().getExtras().getInt(ARG_PROJECT_ID);
        this.project = ProjectProvider.getInstance().getProjectList().get(projectID);

        focusedCategory = project.getCategories().get(0);

        categoryEditorLayout = (GridLayout) findViewById(R.id.category_editor_layout);

        categoryListViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, project.getCategoryNames());
        categoryListView = (ListView) findViewById(R.id.category_list_view);
        categoryListView.setAdapter(categoryListViewAdapter);
        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadCategory(position);
            }
        });

        categoryNameEditText = (EditText) findViewById(R.id.category_name_edit_text);
        estimatedLoadEditText = (EditText) findViewById(R.id.estimated_load_edit_text);

        bertTypeSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Category.bertTypes);
        bertTypeSpinner = (Spinner) findViewById(R.id.bert_type_spinner);
        bertTypeSpinner.setAdapter(bertTypeSpinnerAdapter);

        saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
    }

    private void loadCategory(int position) {
        focusedCategory = project.getCategories().get(position);
        categoryNameEditText.setText(focusedCategory.getName());
        estimatedLoadEditText.setText(String.valueOf(focusedCategory.getEstimatedLoad()));
        bertTypeSpinner.setSelection(focusedCategory.getBertTypeID());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_category_editor, menu);
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

    private void saveChanges() {
        focusedCategory.setName(categoryNameEditText.getText().toString());
        focusedCategory.setEstimatedLoad(Integer.valueOf(estimatedLoadEditText.getText().toString()));
        focusedCategory.setBertTypeID(bertTypeSpinner.getSelectedItemPosition());
        FileProvider.saveProject(project);
    }
}
