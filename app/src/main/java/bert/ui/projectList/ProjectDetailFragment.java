package bert.ui.projectList;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import bert.database.Project;
import bert.database.ProjectProvider;
import bert.ui.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProjectDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProjectDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectDetailFragment extends Fragment {
    
    
    EditText projectNameTextField;
    EditText contactNameTextField;
    EditText contactInfoTextField;
    TextView dateCreatedTextField;
    TextView dateModifiedTextField;
    Button exportToBertConfigButton;
    Button exportToROIButton;
    Button openProjectButton;
    
    private OnFragmentInteractionListener mListener;

   private Project currentProject;
    
    public static ProjectDetailFragment newInstance(String param1, String param2) {
        return new ProjectDetailFragment();
    }

    public ProjectDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentProject = ProjectProvider.getInstance().getProjectList().get(getArguments().getInt("PROJECT_INDEX"));
        }
    }
    
    @Override
    public void onResume(){
        super.onResume();
        projectNameTextField = (EditText) getView().findViewById(R.id.projectNameTextField);
        contactNameTextField = (EditText) getView().findViewById(R.id.projectContactTextField);
        contactInfoTextField = (EditText) getView().findViewById(R.id.projectContactTextField);
        dateCreatedTextField = (TextView) getView().findViewById(R.id.dateCreatedTextField);
        dateModifiedTextField = (TextView) getView().findViewById(R.id.dateAcessedTextField);
        exportToBertConfigButton = (Button) getView().findViewById(R.id.exportToBertConfiguratorButton);
        exportToROIButton = (Button) getView().findViewById(R.id.exportToROIButton);
        openProjectButton = (Button) getView().findViewById(R.id.openProjectButton);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_project_detail_view, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
