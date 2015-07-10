package bert.ui.roomList.deviceList.auditWizard;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import bert.data.ProjectProvider;

import bert.data.proj.Building;
import bert.data.proj.RoomAudit;
import bert.data.proj.exceptions.DuplicateAuditException;
import bert.ui.R;
import bert.ui.common.BertAlert;
import bert.ui.common.BertEditTextAlert;
import bert.ui.common.ProjectChildEditorFragment;
import bert.ui.roomList.roomListActivity.AuditRoomListActivity;
import bert.utility.Cleaner;

import java.util.HashMap;

public class AuditWizardFragment extends ProjectChildEditorFragment {

    public static final String ARG_BUILDING_ID = "BUILDING_ID";
    public static final String ARG_PROJECT_ID = "PROJECT_ID";
    public static final String ARG_ROOM_ID = "ROOM_ID";

    private String projectID;
    private String buildingID;
    private String roomID;

    private Building building;

    private AuditRoomListActivity activity;

    private AuditTallyBoxGVA categoryGridAdapter;
    private RoomAudit roomAudit;

    private GridView gridView;
    private TextView totalBertsTextView;
    private EditText roomEditText;
    private Button cancelButton;
    private Button finishedButton;

    private String oldName;

    public static AuditWizardFragment newInstance(String projectID, String buildingID) {
        AuditWizardFragment fragment = new AuditWizardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PROJECT_ID, projectID);
        args.putString(ARG_BUILDING_ID, buildingID);
        fragment.setArguments(args);
        return fragment;
    }

    public static AuditWizardFragment newInstance(String projectID, String buildingID, String roomID) {
        AuditWizardFragment fragment = new AuditWizardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PROJECT_ID, projectID);
        args.putString(ARG_BUILDING_ID, buildingID);
        args.putString(ARG_ROOM_ID, roomID);

        fragment.setArguments(args);
        return fragment;
    }

    public AuditWizardFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectID = getArguments().getString(ARG_PROJECT_ID);
            buildingID = getArguments().getString(ARG_BUILDING_ID);
            roomID = getArguments().getString(ARG_ROOM_ID);
        }

        activity = (AuditRoomListActivity) getActivity();
        project = ProjectProvider.getInstance().getProject(projectID);
        building = project.getBuilding(buildingID);

        if (roomID != null) {
            roomAudit = project.getAuditForRoomAndBuilding(roomID, buildingID);
        } else {
            roomID = "";
            Log.d("AUDIT", "about to try creating cat");
            HashMap<String, Integer> catCount = new HashMap<>();
            Log.d("AUDIT", "project ID:" + projectID);
            Log.d("AUDIT", project == null ? "project null" : "project good");

            for (String s : project.getBuilding(buildingID).getCategoryNames()) {
                catCount.put(s, 0);
            }
            roomAudit = new RoomAudit(buildingID, "", catCount);

        }

        //TODO name needs to be updated shouldn't set it here
    }

    @Override public void onResume() {
        super.onResume();

        categoryGridAdapter = new AuditTallyBoxGVA(this, roomAudit, projectID, buildingID);
        gridView = (GridView) getView().findViewById(R.id.auditWizardGridView);
        gridView.setAdapter(categoryGridAdapter);

        totalBertsTextView = (TextView) getView().findViewById(R.id.totalCounterTextField);

        finishedButton = (Button) getView().findViewById(R.id.finishAuditWizardButton);
        finishedButton.setOnClickListener(new FinishButtonListener());

        cancelButton = (Button) getView().findViewById(R.id.canelAuditButton);
        cancelButton.setOnClickListener(new CancelButtonListener());

        roomEditText = (EditText) getView().findViewById(R.id.locationNameTextField);
        if (oldName != null){
            roomEditText.setText(oldName);
        } else {
            roomEditText.setText(roomAudit.getRoomID());
        }
        roomEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            //TODO make this interface a class in activity and use it throughout for hiding keyboard
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                activity.inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });

        setBertTotalCounter(roomAudit.totalBerts());
    }

    public void setBertTotalCounter(int count) {
        totalBertsTextView.setText("Total: " + count);
    }


    private void finishAuditWizard() {

        if (attemptToSetName(roomEditText.getText().toString())) {

            roomAudit.setRoomID(roomEditText.getText().toString());
            try {
                project.addAudit(roomAudit);
                project.save();
                activity.openNoSelection();
                activity.loadListView();
                //TODO need to close and reload view
            } catch (DuplicateAuditException e) {
                BertAlert.show(activity, "Cant Create Duplicate Audit");
            }
        } else {
            BertAlert.show(getActivity(), "Invalid Project Name", "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    roomEditText.requestFocus();
                    activity.inputManager.showSoftInput(null, InputMethodManager.SHOW_IMPLICIT);
                }
            });

        }
    }

    public boolean attemptToSetName(String name){
        if (!Cleaner.clean(name).equals("")) {
            if (roomAudit.getRoomID().equals(name) || !project.containtsAuditInBuilding(buildingID, name)) {
                roomAudit.setRoomID(name);

                return true;
            }

        }
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        oldName = roomEditText.getText().toString();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.audit_wizard_fragment, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    class FinishButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            activity.inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            finishAuditWizard();
        }
    }

    class CancelButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            activity.openNoSelection();
        }
    }
}