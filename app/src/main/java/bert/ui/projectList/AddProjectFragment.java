package bert.ui.projectList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import bert.data.utility.Cleaner;
import bert.data.proj.Project;
import bert.data.ProjectProvider;
import bert.ui.BertAlert;
import bert.ui.R;
import bert.ui.buildingList.BuildingListActivity;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddProjectFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddProjectFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProjectFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private Button createButton;
    private TextView nameTextField;
    private TextView contactTextField;
    private TextView contactNumberTextField;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment AddProjectFragment.
     */
    public static AddProjectFragment newInstance() {
        return new AddProjectFragment();
    }

    public AddProjectFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_project, container, false);
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
        nameTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    nameTextField.setText(Cleaner.cleanProjectName(nameTextField.getText().toString()));
                    createButton.setEnabled(!nameTextField.getText().toString().trim().isEmpty());
                }
                return false;
            }
        });
        contactTextField = (TextView) getView().findViewById(R.id.contactTextField);
        contactNumberTextField = (TextView) getView().findViewById(R.id.contactNumberTextField);
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
        public void openAddProjectView();
        public void closeAddProjectView();
    }

    private void createProjectAndFinish() {
        String newProjectName = nameTextField.getText().toString();

        if (ProjectProvider.getInstance().projectNameCheck(newProjectName)) {
            Project newProject = new Project(newProjectName);
            newProject.setContactName(contactTextField.getText().toString());
            newProject.setContactNumber(contactNumberTextField.getText().toString());

            nameTextField.setText("");
            contactTextField.setText("");
            contactNumberTextField.setText("");

            ProjectProvider.getInstance().addProject(newProject);

            ProjectListActivity activity = (ProjectListActivity) getActivity();
            Intent intent = new Intent(activity, BuildingListActivity.class);
            intent.putExtra(BuildingListActivity.ARG_PROJECT_ID, ProjectProvider.getInstance().getProjectList().size() - 1);
            activity.loadProjectList();
            activity.closeAddProjectView();
            startActivity(intent);

            try {
                FileOutputStream fileOutputStream = new FileOutputStream(newProjectName);
                ObjectOutputStream outputStream = new ObjectOutputStream(fileOutputStream);
                outputStream.writeObject(newProject);
            } catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            BertAlert.show(getActivity(), "A project with the same name already exists");
        }
    }
}