package bert.ui.projectList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import bert.database.Project;
import bert.database.ProjectProvider;
import bert.ui.R;
import bert.ui.roomList.RoomListActivity;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddProjectView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddProjectView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddProjectView extends Fragment {

    private OnFragmentInteractionListener mListener;

    private TextView nameTextField;
    private TextView dateTextField;
    private TextView contactTextField;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment AddProjectView.
     */
    public static AddProjectView newInstance() {
        AddProjectView fragment = new AddProjectView();
        return fragment;
    }

    public AddProjectView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_project, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        contactTextField = (TextView) getView().findViewById(R.id.projectContactTextField);
        nameTextField = (TextView) getView().findViewById(R.id.projectNameTextField);
        dateTextField = (TextView) getView().findViewById(R.id.projectDateTextField);

        Button doneButton = (Button) getView().findViewById(R.id.createProjectButton);
        doneButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                createProjectAndFinish();
            }
        });
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
        public void closeAddProjectView();
    }

    private void createProjectAndFinish(){//TODO add contact number to this form
        String nameText = nameTextField.getText().toString();
        String contactText = contactTextField.getText().toString();
        String dateText = dateTextField.getText().toString();

        Project newProject = new Project(nameText, contactText, dateText);

        clearNewProjectForm();

        ProjectProvider.getInstance().addProject(newProject);

        ProjectListActivity activity = (ProjectListActivity) getActivity();
        Intent intent = new Intent(activity, RoomListActivity.class);
        intent.putExtra("projectIndex", ProjectProvider.getInstance().getProjectList().size() - 1);
        activity.loadProjectList();
        activity.closeAddProjectView();
        startActivity(intent);

    }

    private void clearNewProjectForm() {
        nameTextField.setText("");
        contactTextField.setText("");
        dateTextField.setText("");
    }
}