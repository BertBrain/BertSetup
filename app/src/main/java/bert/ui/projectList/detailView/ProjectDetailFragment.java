package bert.ui.projectList.detailView;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import bert.data.ProjectProvider;
import bert.data.proj.Project;
import bert.data.proj.exceptions.InvalidProjectNameException;
import bert.ui.common.ProjectChildEditorFragment;
import bert.utility.Cleaner;
import bert.ui.common.BertAlert;
import bert.ui.R;
import bert.ui.projectList.activity.ProjectListActivity;

/**
 * A simple {@link Fragment} subclass.
 */
abstract public class ProjectDetailFragment extends ProjectChildEditorFragment {

    public static final String ARG_PROJECT_ID = "PROJECT_ID";

    private ProjectListActivity activity;

    protected String projectID;

    private EditText projectNameEditText;
    private EditText contactNameEditText;
    private EditText contactNumberEditText;
    private TextView dateCreatedTextView;
    private TextView dateModifiedTextView;

    private Button openProjectButton;
    private Button deleteButton;

    View listCell;
    int listCellIndex;

    public ProjectDetailFragment() {}

    public ProjectDetailFragment newInstance(String projectID) {
        Bundle args = new Bundle();
        args.putString(ARG_PROJECT_ID, projectID);
        this.setArguments(args);
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (ProjectListActivity) this.getActivity();
        if (getArguments() != null) {
            this.projectID = getArguments().getString(ARG_PROJECT_ID);
            Log.d("PROJ_DETAIL" , "name: + " + projectID);
            Log.d("PROJ_DETAIL", "projects: " + ProjectProvider.getInstance().getAuditProjectNameList());
            project = ProjectProvider.getInstance().getProject(projectID);
        }
    }

    abstract public void openBuildingList();

    @Override
    public void onResume() {
        Log.d("ProjectName", "OnResume");
        super.onResume();
        project = ProjectProvider.getInstance().getProject(projectID);
        listCell = activity.projectListAdapter.selectedView;

        listCellIndex = activity.projectListAdapter.getCurrentSelectionIndex();
        projectNameEditText = (EditText) getView().findViewById(R.id.nameEditText);
        projectNameEditText.setText(project.getProjectName());
        projectNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //activity.projectListAdapter.setTitleAtIndex(listIndex, charSequence.toString());
                if(listCell != null) {
                    TextView view = (TextView) listCell.findViewById(android.R.id.text1);
                    view.setText(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        projectNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    updateNameTextField();
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
                updateNameTextField();
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
                        if ( ProjectProvider.getInstance().deleteProject(projectID)){
                            project = null;
                            activity.onResume();
                            activity.openNoSelectionView();
                            activity.loadListView();
                        } else {
                            BertAlert.show(getActivity(), "Unable to delete project");
                        }


                    }
                }, "Cancel", null);
            }
        });
    }

    public void updateNameTextField() {
        if (project != null ) {
            try {
                Log.d("ProjectName", "setting project name: " + projectNameEditText.getText().toString());
                project.setProjectName(projectNameEditText.getText().toString());

            } catch (InvalidProjectNameException e) {
                //BertAlert.show(getActivity(), "Invalid Project Name");
            }
            projectID = project.getProjectName();
            projectNameEditText.setText(project.getProjectName());
            activity.projectListAdapter.titles.set(listCellIndex, project.getProjectName());
        }
    }

    public void onButtonPressed(Uri uri) {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onPause(){
        updateNameTextField();
        super.onPause();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
