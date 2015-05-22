package bert.ui.categoryList;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import bert.data.FileProvider;
import bert.data.ProjectProvider;
import bert.data.proj.Category;
import bert.data.proj.Project;
import bert.ui.R;

public class CategoryDetailFragment extends Fragment {
    private static final String ARG_PROJECT_ID = "PROJECT_ID";
    private static final String ARG_BUILDING_ID = "BUILDING_ID";
    private static final String ARG_CATEGORY_ID = "CATEGORY_ID";

    private static final String UNDEFINED_LOAD_STRING = "Undefined";

    private int projectID;
    private int buildingID;
    private int categoryID;

    private CategoryListActivity activity;

    private Project project;
    private Category category;

    private EditText categoryNameEditText;
    private EditText estimatedLoadEditText;
    private Spinner bertTypeSpinner;
    private ArrayAdapter<String> bertTypeSpinnerAdapter;
    private Button saveButton;

    private OnFragmentInteractionListener mListener;

    public static CategoryDetailFragment newInstance(int projectID, int buildingID, int categoryID) {
        CategoryDetailFragment fragment = new CategoryDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PROJECT_ID, projectID);
        args.putInt(ARG_BUILDING_ID, buildingID);
        args.putInt(ARG_CATEGORY_ID, categoryID);
        fragment.setArguments(args);
        return fragment;
    }

    public CategoryDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectID = getArguments().getInt(ARG_PROJECT_ID);
            categoryID = getArguments().getInt(ARG_CATEGORY_ID);
            buildingID = getArguments().getInt(ARG_BUILDING_ID);
            project = ProjectProvider.getInstance().getProject(projectID);
            category = project.getBuilding(buildingID).getCategory(categoryID);
        }
        activity = (CategoryListActivity)getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        categoryNameEditText = (EditText) getView().findViewById(R.id.category_name_edit_text);
        categoryNameEditText.setText(category.getName());
        categoryNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(textView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });

        estimatedLoadEditText = (EditText) getView().findViewById(R.id.estimated_load_edit_text);
        estimatedLoadEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(textView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });
        estimatedLoadEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && estimatedLoadEditText.getText().toString().contentEquals(UNDEFINED_LOAD_STRING) ) {
                    estimatedLoadEditText.setText("");
                }
            }
        });
        if (category.getEstimatedLoad() != Category.UNSET) {
            estimatedLoadEditText.setText(String.valueOf(category.getEstimatedLoad()));
        } else {
            estimatedLoadEditText.setText(UNDEFINED_LOAD_STRING);
        }


        bertTypeSpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, Category.bertTypes);
        bertTypeSpinner = (Spinner) getView().findViewById(R.id.bert_type_spinner);
        bertTypeSpinner.setAdapter(bertTypeSpinnerAdapter);
        bertTypeSpinner.setSelection(category.getBertTypeID());

        saveButton = (Button) getView().findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveChanges();
            }
        });
        activity.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_detail, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    private void saveChanges() {
        category.setName(categoryNameEditText.getText().toString());

        try {
          category.setEstimatedLoad(Integer.valueOf(estimatedLoadEditText.getText().toString()));
        } catch (NumberFormatException e) {
            category.setEstimatedLoad(Category.UNSET);
            if (!estimatedLoadEditText.hasFocus()){
                estimatedLoadEditText.setText(UNDEFINED_LOAD_STRING);
            }
        }

        category.setBertTypeID(bertTypeSpinner.getSelectedItemPosition());
        project.save();
        ((CategoryListActivity) getActivity()).createCategoryListView();
    }
}
