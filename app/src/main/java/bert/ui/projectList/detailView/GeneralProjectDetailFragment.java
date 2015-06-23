package bert.ui.projectList.detailView;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import bert.data.ProjectProvider;
import bert.data.proj.exceptions.InvalidProjectNameException;
import bert.ui.common.ProjectChildEditorFragment;
import bert.utility.Cleaner;
import bert.ui.common.BertAlert;
import bert.ui.R;
import bert.ui.projectList.activity.GeneralProjectListActivity;

/**
 * A simple {@link Fragment} subclass.
 */
abstract public class GeneralProjectDetailFragment extends ProjectChildEditorFragment {

    public static final String ARG_PROJECT_ID = "PROJECT_ID";

    private GeneralProjectListActivity activity;

    protected String projectID;

    private EditText projectNameEditText;
    private EditText contactNameEditText;
    private EditText contactNumberEditText;
    private TextView dateCreatedTextView;
    private TextView dateModifiedTextView;

    private Button openProjectButton;
    private Button deleteButton;

    public GeneralProjectDetailFragment() {}

    public GeneralProjectDetailFragment newInstance(String projectIndex) {
        Bundle args = new Bundle();
        args.putString(ARG_PROJECT_ID, projectIndex);
        this.setArguments(args);
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (GeneralProjectListActivity) this.getActivity();
        if (getArguments() != null) {
            this.projectID = getArguments().getString(ARG_PROJECT_ID);
            project = ProjectProvider.getInstance().getProject(projectID);
        }
    }

    abstract public void openBuildingList();

    @Override
    public void onResume() {
        super.onResume();
        projectNameEditText = (EditText) getView().findViewById(R.id.nameEditText);
        projectNameEditText.setText(project.getProjectName());
        projectNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    try {
                        project.setProjectName(v.getText().toString());
                        project.save();
                    } catch (InvalidProjectNameException e) {
                        BertAlert.show(getActivity(), "Invalid Project Name");
                        v.setText(project.getProjectName());
                    }
                    v.setText(project.getProjectName());
                }
                return false;
            }
        });

        contactNameEditText = (EditText) getView().findViewById(R.id.contactEditText);
        contactNameEditText.setText(project.getContactName());
        contactNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String text = Cleaner.clean(v.getText().toString());
                    project.setContactName(text);
                    v.setText(text);
                    project.save();
                }
                return false;
            }
        });

        contactNumberEditText = (EditText) getView().findViewById(R.id.contactNumberEditText);
        contactNumberEditText.setText(project.getContactNumber());
        contactNumberEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String text = Cleaner.clean(v.getText().toString());
                    project.setContactNumber(text);
                    v.setText(text);
                    project.save();
                }
                return false;
            }
        });

        dateCreatedTextView = (TextView) getView().findViewById(R.id.dateCreatedTextView);
        dateCreatedTextView.setText(project.getCreationDate());

        dateModifiedTextView = (TextView) getView().findViewById(R.id.dateAccessedTextView);
        dateModifiedTextView.setText(project.getModifiedDate());

        openProjectButton = (Button) getView().findViewById(R.id.openProjectButton);
        openProjectButton.setText("Add/Edit/View Buildings (" + project.getBuildingCount() + ")");
        openProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBuildingList();
            }
        });

        deleteButton = (Button) getView().findViewById(R.id.deleteProjectButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BertAlert.show(getActivity(), "Warning: This cannot be undone", "Delete anyway", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ProjectProvider.getInstance().deleteProject(projectID);
                        activity.onResume();
                        activity.openNoSelectionView();
                    }
                }, "Cancel", null);
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
