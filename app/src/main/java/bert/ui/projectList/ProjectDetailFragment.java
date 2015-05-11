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
import android.widget.EditText;
import android.widget.TextView;

import bert.data.proj.Project;
import bert.data.ProjectProvider;
import bert.ui.R;
import bert.ui.roomList.RoomListActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProjectDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProjectDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectDetailFragment extends Fragment {

    public static final String ARG_PROJECT_INDEX = "PROJECT_INDEX";

    private int projectIndex;
    
    private EditText projectNameEditText;
    private EditText contactNameEditText;
    private EditText contactNumberEditText;
    private TextView dateCreatedTextField;
    private TextView dateModifiedTextField;
    private Button exportToBertConfigButton;
    private Button exportToROIButton;
    private Button openProjectButton;
    
    private OnFragmentInteractionListener mListener;
    private Project currentProject;
    
    public static ProjectDetailFragment newInstance(String param1, String param2) {
        return new ProjectDetailFragment();
    }

    public ProjectDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.projectIndex = getArguments().getInt(ARG_PROJECT_INDEX);
            currentProject = ProjectProvider.getInstance().getProjectList().get(projectIndex);
        }
    }
    
    @Override
    public void onResume() {
        super.onResume();
        projectNameEditText = (EditText) getView().findViewById(R.id.projectNameEditText);
        contactNameEditText = (EditText) getView().findViewById(R.id.contactNameEditText);
        contactNumberEditText = (EditText) getView().findViewById(R.id.contactNumberEditText);
        dateCreatedTextField = (TextView) getView().findViewById(R.id.dateCreatedTextField);
        dateModifiedTextField = (TextView) getView().findViewById(R.id.dateAcessedTextField);
        exportToBertConfigButton = (Button) getView().findViewById(R.id.exportToBertConfiguratorButton);
        exportToROIButton = (Button) getView().findViewById(R.id.exportToROIButton);
        openProjectButton = (Button) getView().findViewById(R.id.openProjectButton);
        openProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRoomList();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_project_detail_view, container, false);
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

    public void openRoomList() {
        Intent i = new Intent(this.getActivity(), RoomListActivity.class);
        i.putExtra(RoomListActivity.ARG_PROJECT_INDEX, projectIndex);
        startActivity(i);
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
