package bert.ui.roomList.deviceList.auditWizard;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
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
import bert.data.proj.BertUnit;

import bert.data.proj.Building;

import bert.data.proj.Project;
import bert.data.proj.RoomAudit;
import bert.ui.R;
import bert.ui.roomList.RoomListActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AuditWizardFragment extends Fragment {

    public static final String ARG_BUILDING_ID = "BUILDING_ID";
    public static final String ARG_PROJECT_ID = "PROJECT_ID";

    private int projectID;
    private String buildingID;

    private RoomListActivity activity;
    private Project project;

    private AuditTallyBoxGVA tallyGridAdapter;
    private RoomAudit roomAudit;

    private GridView gridView;
    private TextView totalBertsTextView;
    private EditText roomEditText;
    private Button cancelButton;
    private Button finishedButton;


    public static AuditWizardFragment newInstance(int projectID, String buildingID) {
        AuditWizardFragment fragment = new AuditWizardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PROJECT_ID, projectID);
        args.putString(ARG_BUILDING_ID, buildingID);
        fragment.setArguments(args);
        return fragment;
    }

    public AuditWizardFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectID = getArguments().getInt(ARG_PROJECT_ID);
            buildingID = getArguments().getString(ARG_BUILDING_ID);
        }
        activity = (RoomListActivity) getActivity();
        project = ProjectProvider.getInstance().getProject(projectID);

        //TODO name needs to be updated cant set it here
        HashMap<String, Integer> catCount = new HashMap<>();
        for (String s : project.getBuilding(buildingID).getCategoryNames()) {
            catCount.put(s, 0);
        }
        roomAudit = new RoomAudit(buildingID, "AUDIT", catCount);

    }

    @Override public void onResume() {
        super.onResume();

        tallyGridAdapter = new AuditTallyBoxGVA(activity, android.R.layout.simple_gallery_item, roomAudit, projectID);
        gridView = (GridView) getView().findViewById(R.id.auditWizardGridView);
        gridView.setAdapter(tallyGridAdapter);

        totalBertsTextView = (TextView) getView().findViewById(R.id.totalCounterTextField);

        finishedButton = (Button) getView().findViewById(R.id.finishAuditWizardButton);
        finishedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                finishAuditWizard();
            }
        });

        cancelButton = (Button) getView().findViewById(R.id.canelAuditButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
               activity.openNoSelection();
           }
        });

        roomEditText = (EditText) getView().findViewById(R.id.locationNameTextField);
        roomEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
                activity.inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });
    }

    public void setBertTotalCounter(int count) {
        totalBertsTextView.setText("Total: " + count);
    }

    private void openNoRoomNamePopup() {
        AlertDialog.Builder noRoomNameSetAlert = new AlertDialog.Builder(getView().getContext());
        noRoomNameSetAlert.setTitle("Room Name Not Specified");
        noRoomNameSetAlert.setPositiveButton("Set Room Name", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                activity.inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                roomEditText.setFocusable(true);
                roomEditText.requestFocus();
                roomEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            activity.inputManager.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
                            finishAuditWizard();
                        }
                        return false;
                    }
                });
            }
        });

        noRoomNameSetAlert.setNeutralButton("Back to Editing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {}
        });
        noRoomNameSetAlert.create().show();
    }

    //FIXME THIS IS COMPLETELY DIFFERENT NOW UPDATE ASAP
    private void finishAuditWizard() {
        //TODO save, reload view, go to new appropriate view
    }

    //TODO MOVE THIS TO A NEW CLASS
    //TODO MAKE SURE LOCATION NAME IS SAFE
    //TODO make sure it saves
    private static List<BertUnit> convertAuditToBert(RoomAudit audit) {
        List<BertUnit> bertList = new ArrayList<>();
        for (String categoryID : audit.getCategoryNames()) {
            for (int i = 0; i < audit.getCategoryCount(categoryID); i++) {
                String countString = (i == 0) ? ("") : (String.valueOf(i + 1));
                String name = audit.getRoomID() + " - " + categoryID + " " + countString;
                BertUnit bert = new BertUnit(name, audit.getRoomID(), "", audit.getBuildingID(), categoryID);
                bertList.add(bert);
            }
        }
        return bertList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audit_wizard, container, false);
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