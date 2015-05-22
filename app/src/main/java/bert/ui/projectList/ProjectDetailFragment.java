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
import bert.data.proj.exceptions.InvalidProjectNameException;
import bert.data.proj.Project;
import bert.data.ProjectProvider;
import bert.data.utility.CSVExporter;
import bert.data.utility.Cleaner;
import bert.data.utility.ROIExporter;
import bert.ui.BertAlert;
import bert.ui.R;

import bert.ui.buildingList.BuildingListActivity;

public class ProjectDetailFragment extends Fragment {

    public static final String ARG_PROJECT_ID = "PROJECT_ID";

    private ProjectListActivity activity;

    private int projectID;
    private Project currentProject;

    private EditText projectNameEditText;
    private EditText contactNameEditText;
    private EditText contactNumberEditText;
    private TextView dateCreatedTextView;
    private TextView dateModifiedTextView;
    private TextView roomCountTextView;
    private TextView bertCountTextView;
    private Button exportToBertConfigButton;
    private Button exportToROIButton;
    private Button openProjectButton;

    private OnFragmentInteractionListener mListener;

    public static ProjectDetailFragment newInstance(int projectIndex) {
        ProjectDetailFragment fragment = new ProjectDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PROJECT_ID, projectIndex);
        fragment.setArguments(args);
        return fragment;
    }

    public ProjectDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (ProjectListActivity)this.getActivity();
        if (getArguments() != null) {
            this.projectID = getArguments().getInt(ARG_PROJECT_ID);
            currentProject = ProjectProvider.getInstance().getProject(projectID);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        projectNameEditText = (EditText) getView().findViewById(R.id.nameEditText);
        projectNameEditText.setText(currentProject.getProjectName());
        projectNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        currentProject.setProjectName(v.getText().toString());
                        currentProject.save();
                    } catch (InvalidProjectNameException e) {
                        BertAlert.show(getActivity(), "Invalid Project Name");
                        v.setText(currentProject.getProjectName());
                    }
                    v.setText(currentProject.getProjectName());
                }
                return false;
            }
        });

        contactNameEditText = (EditText) getView().findViewById(R.id.contactEditText);
        contactNameEditText.setText(currentProject.getContactName());
        contactNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String text = Cleaner.clean(v.getText().toString());
                    currentProject.setContactName(text);
                    v.setText(text);
                    currentProject.save();
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
                    currentProject.save();
                }
                return false;
            }
        });

        dateCreatedTextView = (TextView) getView().findViewById(R.id.dateCreatedTextView);
        dateCreatedTextView.setText(currentProject.getCreationDate());

        dateModifiedTextView = (TextView) getView().findViewById(R.id.dateAccessedTextView);
        dateModifiedTextView.setText(currentProject.getModifiedDate());

        roomCountTextView = (TextView) getView().findViewById(R.id.roomCountTextView);
        roomCountTextView.setText(Integer.toString(currentProject.getLocationCount()));

        bertCountTextView = (TextView) getView().findViewById(R.id.bertCountTextView);
        bertCountTextView.setText(Integer.toString(currentProject.getBerts().size()));

        exportToBertConfigButton = (Button) getView().findViewById(R.id.exportToBertConfiguratorButton);
        exportToBertConfigButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File fileToShare;
                try {
                    fileToShare = CSVExporter.generateCSV(currentProject);
                    new ExportChooser(activity).exportFile("CSV for bert configurator", fileToShare);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("Project Detail Fragment", "Unable to generate Configurator CSV File");
                    fileToShare = new File("test file");
                }
            }
        });

        exportToROIButton = (Button) getView().findViewById(R.id.exportToROIButton);
        exportToROIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    File fileToShare = ROIExporter.generateROI(currentProject);
                    new ExportChooser(activity).exportFile("ROI spreadsheet", fileToShare);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        openProjectButton = (Button) getView().findViewById(R.id.openProjectButton);
        openProjectButton.setText("View Buildings (" + currentProject.getBuildingCount() + ")");
        openProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBuildingList();
            }
        });
    }

    public void openBuildingList() {
        Intent i = new Intent(this.getActivity(), BuildingListActivity.class);
        i.putExtra(BuildingListActivity.ARG_PROJECT_ID, projectID);
        startActivity(i);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_project_detail, container, false);
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
    }
}
