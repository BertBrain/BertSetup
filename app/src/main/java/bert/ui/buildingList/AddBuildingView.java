package bert.ui.buildingList;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import bert.data.ProjectProvider;
import bert.data.proj.Building;
import bert.data.proj.Project;
import bert.ui.R;
import bert.ui.roomList.RoomListActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link AddBuildingView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddBuildingView extends Fragment {

    private static final String PROJECT_ID_KEY = "PROJECT_ID_KEY";

    private int projectID;

    EditText buildingNameTextField;
    Button addBuildingButton;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment AddBuildingView.
     */
    // TODO: Rename and change types and number of parameters
    public static AddBuildingView newInstance(int param1) {
        AddBuildingView fragment = new AddBuildingView();
        Bundle args = new Bundle();
        args.putInt(PROJECT_ID_KEY, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public AddBuildingView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectID = getArguments().getInt(PROJECT_ID_KEY);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        buildingNameTextField = (EditText) getView().findViewById(R.id.add_building_name_textfield);
        buildingNameTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (buildingNameTextField.getText().toString().length() > 0){
                    addBuildingButton.setEnabled(true);
                }
                return false;
            }
        });

        addBuildingButton = (Button) getView().findViewById(R.id.add_building_button);
        addBuildingButton.setEnabled(false);
        addBuildingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBuilding();
            }
        });
    }

    private void addBuilding(){
        final String newName = buildingNameTextField.getText().toString();
        if (!ProjectProvider.getInstance().getProjectList().get(projectID).getBuildingNames().contains(newName)){
            Building b = new Building(newName);
            Project project = ProjectProvider.getInstance().getProjectList().get(projectID);
            project.addBuilding(b);

            Intent intent = new Intent(getActivity(), RoomListActivity.class);
            intent.putExtra(RoomListActivity.ARG_PROJECT_INDEX, projectID);
            intent.putExtra(RoomListActivity.ARG_BUILDING_ID, project.getBuildings().size() - 1);
            getActivity().startActivity(intent);
        } else {
            AlertDialog.Builder noRoomNameSetAlert = new AlertDialog.Builder(getView().getContext());
            noRoomNameSetAlert.setTitle("This Building Already Exists");
            noRoomNameSetAlert.setPositiveButton("Open Building", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int buildingID  = ProjectProvider.getInstance().getProjectList().get(projectID).getBuildingNames().indexOf(newName);
                    Intent intent = new Intent(getActivity(), RoomListActivity.class);
                    intent.putExtra(RoomListActivity.ARG_PROJECT_INDEX, projectID);
                    intent.putExtra(RoomListActivity.ARG_BUILDING_ID, buildingID);
                    getActivity().startActivity(intent);
                }
            });
            noRoomNameSetAlert.setNegativeButton("Change Name", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //possible TODO: directly open fullscreen text editing
                }
            });
            noRoomNameSetAlert.create().show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_building_view, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
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
