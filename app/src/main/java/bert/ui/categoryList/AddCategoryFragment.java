package bert.ui.categoryList;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import bert.data.ProjectProvider;
import bert.data.proj.Category;
import bert.data.proj.Project;
import bert.ui.BertAlert;
import bert.ui.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddCategoryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCategoryFragment extends Fragment {
    private static final String ARG_PROJECT_ID = "PROJECT_ID";
    private static final String ARG_BUILDING_ID = "BUILDING_ID";

    private int projectID;
    private int buildingID;
    private Project project;
    private OnFragmentInteractionListener activity;

    private EditText categoryNameEditText;
    private EditText estimatedLoadEditText;
    private Spinner bertTypeSpinner;
    private ArrayAdapter<String> bertTypeSpinnerAdapter;
    private Button finishButton;
    private Button cancelButton;

    private OnFragmentInteractionListener mListener;

    public static AddCategoryFragment newInstance(int projectID, int buildingID) {
        AddCategoryFragment fragment = new AddCategoryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PROJECT_ID, projectID);
        args.putInt(ARG_BUILDING_ID, buildingID);
        fragment.setArguments(args);
        return fragment;
    }

    public AddCategoryFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectID = getArguments().getInt(ARG_PROJECT_ID);
            project = ProjectProvider.getInstance().getProjectList().get(projectID);
            buildingID = getArguments().getInt(ARG_BUILDING_ID);
            activity = (OnFragmentInteractionListener) getActivity();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        estimatedLoadEditText = (EditText) getView().findViewById(R.id.standyPowerTextField);

        bertTypeSpinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, Category.bertTypes);
        bertTypeSpinner = (Spinner) getView().findViewById(R.id.bertTypeSpinner);
        bertTypeSpinner.setAdapter(bertTypeSpinnerAdapter);

        cancelButton = (Button) getView().findViewById(R.id.cancelAddCategoryButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.categoryCreationCanceled();
            }
        });

        finishButton = (Button) getView().findViewById(R.id.finshCreateCategoryButton);
        finishButton.setEnabled(false);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCategoryAndFinish();
            }
        });

        categoryNameEditText = (EditText) getView().findViewById(R.id.categoryNameTextField);
        categoryNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

               finishButton.setEnabled(categoryNameEditText.getText().toString().length() > 0);

                return false;
            }
        });
    }

    private void createCategoryAndFinish() {
        String name = categoryNameEditText.getText().toString();
        int bertType = bertTypeSpinner.getSelectedItemPosition();
        int estimatedLoad;
        if (estimatedLoadEditText.getText().toString().length() > 0){
            try {
                estimatedLoad = Integer.valueOf(estimatedLoadEditText.getText().toString());
            } catch (NumberFormatException e){
                estimatedLoad = Category.UNSET;
                BertAlert.show(getActivity(), "Invalid number entered");
                System.out.println("invalid number passed");
                return;
            }
        } else {
            estimatedLoad = Category.UNSET;
        }
        project.getBuildings().get(buildingID).addCategory(new Category(name, bertType, estimatedLoad));
        activity.categoryCreationSuccessful();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        public void categoryCreationCanceled();
        public void categoryCreationSuccessful();
    }

}
