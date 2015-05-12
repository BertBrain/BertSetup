package bert.ui.roomList;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bert.data.proj.Category;
import bert.ui.R;

public class AddCategory extends ActionBarActivity {

    EditText categoryNameTextField;
    EditText wattageTextField;
    Spinner bertTypeSelectionSpinner;
    Button finishedAddCategory;
    Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        categoryNameTextField = (EditText) findViewById(R.id.categoryNameTextField);
        wattageTextField = (EditText) findViewById(R.id.standyPowerTextField);
        bertTypeSelectionSpinner = (Spinner) findViewById(R.id.bertTypeSpinner);
        finishedAddCategory = (Button) findViewById(R.id.finshCreateCategoryButton);
        cancelButton = (Button) findViewById(R.id.cancelAddCategoryButton);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Category.bertTypes);
        bertTypeSelectionSpinner.setAdapter(adapter);

        finishedAddCategory.setEnabled(false);
        finishedAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int estimatedLoad;
                if (wattageTextField.getText().toString().length() == 0){
                    estimatedLoad = Category.UNSET;
                } else {
                    try {
                        estimatedLoad  = Integer.parseInt(wattageTextField.getText().toString());
                        if (estimatedLoad < 0){
                            AlertDialog.Builder invalidNumberAlert = new AlertDialog.Builder(getBaseContext());
                            invalidNumberAlert.setTitle("Invalid Number: Standby Wattage cannot be less than 0");
                            invalidNumberAlert.setPositiveButton("OK", null);
                            invalidNumberAlert.create().show();
                            return;
                        }

                    } catch (NumberFormatException e){
                        e.printStackTrace();
                        System.out.println();
                        AlertDialog.Builder invalidNumberAlert = new AlertDialog.Builder(getBaseContext());
                        invalidNumberAlert.setTitle("Invalid Number");
                        invalidNumberAlert.setPositiveButton("OK", null);
                        estimatedLoad = 0;
                        invalidNumberAlert.create().show();
                        return;
                    }
                }

                System.out.println("estimated load: " + estimatedLoad);
                Category category = new Category(categoryNameTextField.getText().toString(),
                        bertTypeSelectionSpinner.getSelectedItemPosition(),
                                                 estimatedLoad);
                System.out.println("new category: " + category);
                Intent returnValue = new Intent();
                returnValue.putExtra("category", category);
                setResult(RESULT_OK, returnValue);
                finish();
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
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
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
