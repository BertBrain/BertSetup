package bert.ui.roomList;

import android.app.Activity;
import android.content.Context;
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

import bert.data.FileProvider;
import bert.data.ProjectProvider;
import bert.data.proj.BertUnit;

import bert.data.proj.Category;

import bert.data.proj.Project;
import bert.data.utility.Cleaner;
import bert.ui.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AuditWizardFragment extends Fragment {

    public static final String ARG_BUILDING_ID = "BUILDING_ID";
    public static final String ARG_PROJECT_ID = "PROJECT_ID";

    private int projectID;
    private int buildingID;

    private Project project;

    private Button cancelButton;
    private Button finishedButton;
    private GridView gridView;
    private TextView totalBertsCounter;

    private AuditTallyBoxGVA tallyGridAdapter;
    private EditText locationEditText;

    private RoomListActivity activity;

    public static AuditWizardFragment newInstance(int projectID, int buildingID) {
        AuditWizardFragment fragment = new AuditWizardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PROJECT_ID, projectID);
        args.putInt(ARG_BUILDING_ID, buildingID);
        fragment.setArguments(args);
        return fragment;
    }

    public AuditWizardFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectID = getArguments().getInt(ARG_PROJECT_ID);
            buildingID = getArguments().getInt(ARG_BUILDING_ID);
        }
        activity = (RoomListActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audit_wizard, container, false);
    }

    @Override public void onResume() {
        super.onResume();
        project = ProjectProvider.getInstance().getProjectList().get(projectID);

        tallyGridAdapter = new AuditTallyBoxGVA(this, activity, android.R.layout.simple_gallery_item, project.getBuildings().get(buildingID).getCategories(), projectID, buildingID);
        gridView = (GridView) getView().findViewById(R.id.auditWizardGridView);
        gridView.setAdapter(tallyGridAdapter);

        totalBertsCounter = (TextView) getView().findViewById(R.id.totalCounterTextField);

        finishedButton = (Button) getView().findViewById(R.id.finishAuditWizardButton);
        finishedButton.setEnabled(tallyGridAdapter.updateBertTotal() != 0);
        finishedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAuditWizard();
            }
        });

        cancelButton = (Button) getView().findViewById(R.id.canelAuditButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {
               activity.openNoSelectionView("Select or Create A Room");
           }
        });

        locationEditText = (EditText) getView().findViewById(R.id.locationNameTextField);
        locationEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });
    }

    public void setCanFinish(boolean canFinish) {
        finishedButton.setEnabled(canFinish);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setBertTotalCounter(int count) {
        totalBertsCounter.setText("Total: " + count);
    }

    private void openNoRoomNamePopup() {
        AlertDialog.Builder noRoomNameSetAlert = new AlertDialog.Builder(getView().getContext());
        noRoomNameSetAlert.setTitle("Room Name Not Specified");
        noRoomNameSetAlert.setPositiveButton("Set Room Name", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                locationEditText.setFocusable(true);
                locationEditText.requestFocus();
                locationEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            manager.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
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

    private void finishAuditWizard() {
        String location = locationEditText.getText().toString();
        if (Cleaner.isValid(location)) {
            HashMap<Category, Integer> categoryCounts = tallyGridAdapter.getCounts();
            List<BertUnit> bertList = new ArrayList<>();
            int categoryCount = 0;
            for (Category category : project.getBuildings().get(buildingID).getCategories()) {
                if (categoryCounts.get(category) != null) {
                    for (int i = 0; i < categoryCounts.get(category); i++) {
                        String countString = i == 0 ? "" : String.valueOf(i+1);
                        String name = location + " - " + category.getName() + " " + countString;
                        BertUnit bert = new BertUnit(name, location, "", buildingID, categoryCount, false);
                        bertList.add(bert);
                    }
                }
                categoryCount++;
            }
            project.addBerts(bertList);
            activity.onResume(); //Refresh view
            activity.openDeviceListFragment(bertList.get(0).getLocation());
            FileProvider.saveProject(project);
        } else {
            openNoRoomNamePopup();
        }

    }
}