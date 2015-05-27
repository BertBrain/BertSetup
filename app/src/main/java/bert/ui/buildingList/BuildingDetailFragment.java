package bert.ui.buildingList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import bert.data.ProjectProvider;
import bert.data.proj.Building;
import bert.data.proj.Project;
import bert.data.proj.exceptions.InvalidBuildingNameException;
import bert.data.utility.Cleaner;
import bert.ui.NoSelectionFragment;
import bert.ui.R;
import bert.ui.categoryList.CategoryListActivity;
import bert.ui.roomList.RoomListActivity;

public class BuildingDetailFragment extends Fragment {

    private static final String ARG_PROJECT_ID = "PROJECT_ID";
    private static final String ARG_BUILDING_ID = "BUILDING_ID";

    private int projectID;
    private String buildingID;
    private Project project;
    private Building building;

    private EditText nameEditText;
    private Button buildingButton;
    private Button categoryButton;
    private Button deleteButton;

    private TextView bertCountTextView;

    private TextView startTimeDisplay;
    private TextView endTimeDisplay;
    private TimeRangeDisplay timeDisplay;

    public static BuildingDetailFragment newInstance(int projectID, String buildingID) {
        BuildingDetailFragment fragment = new BuildingDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PROJECT_ID, projectID);
        args.putString(ARG_BUILDING_ID, buildingID);
        fragment.setArguments(args);
        return fragment;
    }

    public BuildingDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectID = getArguments().getInt(ARG_PROJECT_ID);
            buildingID = getArguments().getString(ARG_BUILDING_ID);
            project = ProjectProvider.getInstance().getProject(projectID);
            building = project.getBuilding(buildingID);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startTimeDisplay = (TextView) getView().findViewById(R.id.openingTimeTextView);

        endTimeDisplay = (TextView) getView().findViewById(R.id.closingTimeTextView);
        timeDisplay = new TimeRangeDisplay(getActivity(), startTimeDisplay, building.getStartTime(), endTimeDisplay, building.getEndTime());

        bertCountTextView = (TextView) getView().findViewById(R.id.bertCountTextView);
        bertCountTextView.setText(Integer.toString(project.getBertsByBuilding(buildingID).size()));

        nameEditText = (EditText) getView().findViewById(R.id.buildingNameEditText);
        nameEditText.setText(buildingID);
        nameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                try {
                    String newBuildingID = nameEditText.getText().toString();
                    project.renameBuilding(buildingID, newBuildingID);
                    buildingID = newBuildingID;
                    project.save();
                    ((BuildingListActivity) getActivity()).loadListView();
                } catch (InvalidBuildingNameException e) {
                    nameEditText.setText(buildingID);
                }
                InputMethodManager mgr = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                mgr.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return false;
            }
        });

        categoryButton = (Button) getView().findViewById(R.id.categoryListButton);
        categoryButton.setText("View Categories (" + project.getBuilding(buildingID).getCategoryCount() + ")");
        categoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCategoryListActivity();
            }
        });

        buildingButton = (Button) getView().findViewById(R.id.roomListButton);
        buildingButton.setText("View Rooms (" + project.getLocationNamesInBuilding(buildingID).size() + ")");
        buildingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRoomListActivity();
            }
        });

        deleteButton = (Button) getView().findViewById(R.id.buildingDeleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Are you sure you want to delete this building?");
                alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        project.deleteBuilding(buildingID);
                        project.save();
                        ((BuildingListActivity)getActivity()).loadFragment(NoSelectionFragment.newInstance("Select or Create a Building"));
                        ((BuildingListActivity)getActivity()).loadListView();
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alert.create().show();
            }
        });
    }

    private void openRoomListActivity() {
        Intent intent = new Intent(getActivity(), RoomListActivity.class);
        intent.putExtra(RoomListActivity.ARG_PROJECT_ID, projectID);
        intent.putExtra(RoomListActivity.ARG_BUILDING_ID, buildingID);
        getActivity().startActivity(intent);
    }

    public void openCategoryListActivity() {
        Intent i = new Intent(this.getActivity(), CategoryListActivity.class);
        i.putExtra(CategoryListActivity.ARG_PROJECT_ID, projectID);
        i.putExtra(CategoryListActivity.ARG_BUILDING_ID, buildingID);
        startActivity(i);
    }

    @Override
    public void onPause() {
        super.onPause();
        building.setStartTime(timeDisplay.getStartTime());
        building.setEndTime(timeDisplay.getEndTime());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_building_detail, container, false);
    }

    public void onButtonPressed(Uri uri) {}

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
