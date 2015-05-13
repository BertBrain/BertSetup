package bert.ui.projectList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import bert.data.FileProvider;
import bert.data.proj.Project;
import bert.data.ProjectProvider;
import bert.data.utility.CSVExporter;
import bert.data.utility.Cleaner;
import bert.ui.R;
import bert.ui.categoryList.CategoryEditorActivity;
import bert.ui.roomList.RoomListActivity;

//TODO use 1 save method for all writing to project done
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
    private Project currentProject;

    private EditText projectNameEditText;
    private EditText contactNameEditText;
    private EditText contactNumberEditText;
    private TextView dateCreatedTextField;
    private TextView dateModifiedTextField;
    private Button exportToBertConfigButton;
    private Button exportToROIButton;
    private Button openProjectButton;
    private Button categoryEditorButton;
    private Button buildingEditorButton;
    
    private OnFragmentInteractionListener mListener;

    public static ProjectDetailFragment newInstance(int projectIndex) {
        ProjectDetailFragment fragment = new ProjectDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PROJECT_INDEX, projectIndex);
        fragment.setArguments(args);
        return fragment;
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
        projectNameEditText.setText(currentProject.getProjectName());
        projectNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String text = Cleaner.cleanProjectName(v.getText().toString());
                    if (ProjectProvider.getInstance().projectNameCheck(text)) {
                        v.setText(text);
                        currentProject.setProjectName(text);
                        FileProvider.saveProject(currentProject);
                    } else {
                        v.setText(currentProject.getProjectName());
                    }
                }
                return false;
            }
        });

        contactNameEditText = (EditText) getView().findViewById(R.id.contactNameEditText);
        contactNameEditText.setText(currentProject.getContactName());
        contactNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String text = Cleaner.clean(v.getText().toString());
                    currentProject.setContactName(text);
                    v.setText(text);
                    FileProvider.saveProject(currentProject);
                }
                return false;
            }
        });

        contactNumberEditText = (EditText) getView().findViewById(R.id.contactNumberEditText);
        contactNumberEditText.setText(currentProject.getContactNumber());
        contactNumberEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String text = Cleaner.clean(v.getText().toString());
                    currentProject.setContactNumber(text);
                    v.setText(text);
                    FileProvider.saveProject(currentProject);
                }
                return false;
            }
        });

        dateCreatedTextField = (TextView) getView().findViewById(R.id.dateCreatedTextField);
        dateCreatedTextField.setText(currentProject.getCreationDate());

        dateModifiedTextField = (TextView) getView().findViewById(R.id.dateAcessedTextField);
        dateModifiedTextField.setText(currentProject.getModifiedDate());

        exportToBertConfigButton = (Button) getView().findViewById(R.id.exportToBertConfiguratorButton);
        exportToBertConfigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//TODO add alert
                File fileToShare;
                try {
                    fileToShare = CSVExporter.generateCSV(currentProject);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("Project Detail Fragment", "Unable to generate Configurator CSV File");
                    fileToShare = new File("test file");
                }
            }
        });

        exportToROIButton = (Button) getView().findViewById(R.id.exportToROIButton);//TODO implement

        openProjectButton = (Button) getView().findViewById(R.id.openProjectButton);
        openProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRoomList();
            }
        });

        categoryEditorButton = (Button) getView().findViewById(R.id.categoryEditorButton);
        categoryEditorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryEditor();
            }
        });

        buildingEditorButton = (Button) getView().findViewById(R.id.buildingEditorButton);
        buildingEditorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBuildingEditor();
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
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
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
        i.putExtra(RoomListActivity.ARG_BUILDING_ID, 0);
        startActivity(i);
    }

    public void openBuildingEditor() {
        //TODO write
    }

    public void openCategoryEditor() {
        Intent i = new Intent(this.getActivity(), CategoryEditorActivity.class);
        i.putExtra(CategoryEditorActivity.ARG_PROJECT_ID, projectIndex);
        startActivity(i);
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

}
