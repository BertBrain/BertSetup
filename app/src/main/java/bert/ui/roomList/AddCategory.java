package bert.ui.roomList;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import bert.database.Category;
import bert.ui.R;

public class AddCategory extends ActionBarActivity {

    EditText categoryNameTextField;
    EditText wattageTextField;
    Spinner bertTypeSelectionSpinner;
    Button finishedAddCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        categoryNameTextField = (EditText) findViewById(R.id.categoryNameTextField);
        wattageTextField = (EditText) findViewById(R.id.standyPowerTextField);
        bertTypeSelectionSpinner = (Spinner) findViewById(R.id.bertTypeSpinner);
        finishedAddCategory = (Button) findViewById(R.id.finshCreateCategoryButton);

        finishedAddCategory.setEnabled(false);
        finishedAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int estimatedLoad = Integer.parseInt(wattageTextField.getText().toString());
                System.out.println("estimated load: " + estimatedLoad);
                Category category = new Category(categoryNameTextField.getText().toString(),
                                                 bertTypeSelectionSpinner.getSelectedItemPosition(),
                                                 estimatedLoad);
                System.out.println("new category: " + category);
            }
        });
        categoryNameTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    finishedAddCategory.setEnabled(categoryNameTextField.getText().toString().length() > 0);
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_category, menu);
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
