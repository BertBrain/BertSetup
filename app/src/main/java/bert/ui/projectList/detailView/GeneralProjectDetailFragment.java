package bert.ui.projectList.detailView;


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

import bert.data.ProjectProvider;
import bert.data.proj.Project;
import bert.data.proj.exceptions.InvalidProjectNameException;
import bert.data.utility.Cleaner;
import bert.data.utility.ROIExporter;
import bert.ui.R;
import bert.ui.common.BertAlert;
import bert.ui.projectList.ExportChooser;
import bert.ui.projectList.activity.GeneralProjectListActivity;

/**
 * A simple {@link Fragment} subclass.
 */
abstract public class GeneralProjectDetailFragment extends Fragment {

    public static final String ARG_PROJECT_ID = "PROJECT_ID";

    private GeneralProjectListActivity activity;

    protected int projectID;
    protected Project currentProject;

    private EditText projectNameEditText;
    private EditText contactNameEditText;
    private EditText contactNumberEditText;
    private TextView dateCreatedTextView;
    private TextView dateModifiedTextView;

    private Button openProjectButton;

    public GeneralProjectDetailFragment() {
        // Required empty public constructor
    }

    public GeneralProjectDetailFragment newInstance(int projectIndex) {
        Bundle args = new Bundle();
        args.putInt(ARG_PROJECT_ID, projectIndex);
        this.setArguments(args);
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (GeneralProjectListActivity)this.getActivity();
        if (getArguments() != null) {
            this.projectID = getArguments().getInt(ARG_PROJECT_ID);
            currentProject = getProjectForID(projectID);
        }
    }

    abstract public Project getProjectForID(int projectID);

    abstract public void openBuildingList();

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

        openProjectButton = (Button) getView().findViewById(R.id.openProjectButton);
        //openProjectButton.setText("View Buildings (" + currentProject.getBuildingCount() + ")");
        openProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBuildingList();
            }
        });
    }

    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
