package bert.ui.categoryList;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import bert.data.ProjectProvider;
import bert.data.proj.Building;
import bert.data.proj.Category;
import bert.data.proj.Project;
import bert.data.proj.exceptions.InvalidCategoryNameException;
import bert.utility.Cleaner;
import bert.ui.common.BertAlert;
import bert.ui.R;

public class AddCategoryFragment extends Fragment {

    private static final String ARG_PROJECT_ID = "PROJECT_ID";
    private static final String ARG_BUILDING_ID = "BUILDING_ID";

    private int projectID;
    private String buildingID;

    private Project project;
    private Building building;
    private OnFragmentInteractionListener activity;

    private ArrayAdapter<String> bertTypeSpinnerAdapter;

    private EditText categoryNameEditText;
    private EditText estimatedLoadEditText;
    private Spinner bertTypeSpinner;
    private Button finishButton;
    private Button cancelButton;

    private OnFragmentInteractionListener mListener;

    public static AddCategoryFragment newInstance(int projectID, String buildingID) {
        AddCategoryFragment fragment = new AddCategoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PROJECT_ID, projectID);
        args.putString(ARG_BUILDING_ID, buildingID);
        fragment.setArguments(args);
        return fragment;
    }

    public AddCategoryFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectID = getArguments().getInt(ARG_PROJECT_ID);
            buildingID = getArguments().getString(ARG_BUILDING_ID);
            project = ProjectProvider.getInstance().getProject(projectID);
            building = project.getBuilding(buildingID);
            activity = (OnFragmentInteractionListener) getActivity();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        categoryNameEditText = (EditText) getView().findViewById(R.id.categoryNameTextField);
        categoryNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean hasValidName = Cleaner.isValid(categoryNameEditText.getText().toString());
                finishButton.setEnabled(hasValidName);
            }
        });

        estimatedLoadEditText = (EditText) getView().findViewById(R.id.standyPowerTextField);

        bertTypeSpinnerAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, Category.bertTypes);
        bertTypeSpinner = (Spinner) getView().findViewById(R.id.bertTypeSpinner);
        bertTypeSpinner.setAdapter(bertTypeSpinnerAdapter);

        finishButton = (Button) getView().findViewById(R.id.finshCreateCategoryButton);
        finishButton.setEnabled(false);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCategoryAndFinish();
            }
        });

        cancelButton = (Button) getView().findViewById(R.id.cancelAddCategoryButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.categoryCreationCanceled();
            }
        });
    }

    private void createCategoryAndFinish() {
        String categoryID = categoryNameEditText.getText().toString();
        int bertType = bertTypeSpinner.getSelectedItemPosition();
        int estimatedLoad;
        try {
            if (estimatedLoadEditText.getText().toString().length() != 0) {
                estimatedLoad = Integer.valueOf(estimatedLoadEditText.getText().toString());
            } else {
                estimatedLoad = Category.UNSET_ESTIMATED_LOAD;
            }
        } catch (NumberFormatException e) {
            estimatedLoad = Category.UNSET_ESTIMATED_LOAD;
            BertAlert.show(getActivity(), "Invalid number entered.");
            System.out.println("invalid number passed");
            return;
        }
        Category newCategory = new Category (bertType, estimatedLoad);
        try {
            building.addCategory(categoryID, newCategory);
            project.save();
            activity.categoryCreationSuccessful(categoryID);
        } catch (InvalidCategoryNameException e) {
            e.printStackTrace();
            BertAlert.show(getActivity(), "Invalid Category Name.");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_category_add, container, false);
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
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
        public void categoryCreationCanceled();
        public void categoryCreationSuccessful(String categoryID);
    }
}
