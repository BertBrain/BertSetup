package bert.ui.categoryList;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import bert.data.FileProvider;
import bert.data.ProjectProvider;
import bert.data.proj.Category;
import bert.data.proj.Project;
import bert.ui.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryDetailFragment extends Fragment {
    private static final String ARG_PROJECT_ID = "PROJECT_ID";
    private static final String ARG_CATEGORY_ID = "CATEGORY_ID";

    private int projectID;
    private int categoryID;

    private Project project;
    private Category category;

    private EditText categoryNameEditText;
    private EditText estimatedLoadEditText;
    private Spinner bertTypeSpinner;
    private ArrayAdapter<String> bertTypeSpinnerAdapter;
    private Button saveButton;

    private OnFragmentInteractionListener mListener;

    public static CategoryDetailFragment newInstance(int projectID, int categoryID) {
        CategoryDetailFragment fragment = new CategoryDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PROJECT_ID, projectID);
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
            project = ProjectProvider.getInstance().getProjectList().get(projectID);
            category = project.getCategories().get(categoryID);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        categoryNameEditText = (EditText) getView().findViewById(R.id.category_name_edit_text);
        categoryNameEditText.setText(category.getName());

        estimatedLoadEditText = (EditText) getView().findViewById(R.id.estimated_load_edit_text);
        estimatedLoadEditText.setText(String.valueOf(category.getEstimatedLoad()));

        bertTypeSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Category.bertTypes);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        category.setEstimatedLoad(Integer.valueOf(estimatedLoadEditText.getText().toString()));
        category.setBertTypeID(bertTypeSpinner.getSelectedItemPosition());
        FileProvider.saveProject(project);
        ((CategoryListActivity) getActivity()).createCategoryListView();
    }
}
