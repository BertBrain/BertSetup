package bert.ui.projectList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import bert.data.proj.BertUnit;
import bert.data.proj.Building;
import bert.data.proj.exceptions.InvalidProjectNameException;
import bert.data.utility.Cleaner;
import bert.data.proj.Project;
import bert.data.ProjectProvider;
import bert.ui.BertAlert;
import bert.ui.R;
import bert.ui.buildingList.BuildingListActivity;

public class AddProjectFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private Button createButton;
    private TextView nameTextField;
    private TextView contactTextField;
    private TextView contactNumberTextField;

    public static AddProjectFragment newInstance() {
        return new AddProjectFragment();
    }

    public AddProjectFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        createButton = (Button) getView().findViewById(R.id.createProjectButton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createProjectAndFinish();
            }
        });
        createButton.setEnabled(false);
        nameTextField = (TextView) getView().findViewById(R.id.projectNameTextField);
        nameTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String cleanedName = Cleaner.cleanProjectName(nameTextField.getText().toString());
                createButton.setEnabled(!cleanedName.isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        contactTextField = (TextView) getView().findViewById(R.id.contactTextField);
        contactNumberTextField = (TextView) getView().findViewById(R.id.contactNumberTextField);
    }

    private void createProjectAndFinish() {
        String newProjectName = nameTextField.getText().toString();

        try {
            Project newProject = new Project(newProjectName,
                    new ArrayList<BertUnit>(),
                    new HashMap<String, Building>()
            );
            newProject.setContactName(contactTextField.getText().toString());
            newProject.setContactNumber(contactNumberTextField.getText().toString());

            nameTextField.setText("");
            contactTextField.setText("");
            contactNumberTextField.setText("");

            ProjectProvider.getInstance().addProject(newProject);

            ProjectListActivity activity = (ProjectListActivity) getActivity();
            Intent intent = new Intent(activity, BuildingListActivity.class);
            intent.putExtra(BuildingListActivity.ARG_PROJECT_ID, ProjectProvider.getInstance().getTotalProjects() - 1);

            activity.closeAddProjectView();
            startActivity(intent);
        } catch (InvalidProjectNameException e) {
            BertAlert.show(getActivity(), "Invalid project name");
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
        public void openAddProjectView();
        public void closeAddProjectView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_project_add, container, false);
    }

}