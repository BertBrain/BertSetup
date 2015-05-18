package bert.ui.roomList;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import bert.data.FileProvider;
import bert.data.proj.BertUnit;
import bert.data.proj.Project;
import bert.ui.R;

//TODO edit detail fragment shouldnt be available if bert list for location is empty
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeviceEditorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeviceEditorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceEditorFragment extends Fragment {
    public static final String ARG_LOCATION = "location";
    public static final String ARG_BUILDING = "building";
    public static final String ARG_PROJECT = "projectkey";

    public static final String ADD_BUILDING_STRING = "+ Add Building";

    private RoomListActivity activity;
    private Project project;
    private int projectID;
    private int buildingID;
    private String location;
    private int position;
    private List<BertUnit> bertList;

    private OnFragmentInteractionListener mListener;

    private ArrayAdapter<String> deviceTableAdapter;
    private ListView locationListView;
    private FrameLayout detailView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param location the room used to populate the bertlist.
     * @return A new instance of fragment DeviceEditorFragment.
     */
    public static DeviceEditorFragment newInstance(int projectID, int buildingID, String location) {
        DeviceEditorFragment fragment = new DeviceEditorFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PROJECT, projectID);
        args.putInt(ARG_BUILDING, buildingID);
        args.putString(ARG_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    public DeviceEditorFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        activity = (RoomListActivity) getActivity();
        project = activity.getProject();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            projectID = getArguments().getInt(ARG_PROJECT);
            buildingID = getArguments().getInt(ARG_BUILDING);
            location = getArguments().getString(ARG_LOCATION);
            bertList = project.getBertsByLocation(buildingID, location);
        }
    }

    @Override
    public void onResume() {
        super.onResume();


        List<String> bertNameList = new ArrayList<String>();
        if (getArguments() != null) {
            for (BertUnit bert : bertList) {
                bertNameList.add(bert.getName());
            }
        }


        final ArrayList<String> listStrings = new ArrayList<String>(bertNameList);
        listStrings.add(ADD_BUILDING_STRING);
        deviceTableAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, listStrings);

        locationListView = (ListView) getView().findViewById(R.id.bertList);
        locationListView.setAdapter(deviceTableAdapter);

        locationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == listStrings.size() -1 ) {
                    addDevice();
                } else {
                    loadDeviceAtPosition(position);
                }
            }
        });


    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device_editor_view, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener. in device editor");
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



    private void loadDeviceAtPosition(int position) {
        System.out.println("editing device");
        DeviceDetailEditFragment fragment = DeviceDetailEditFragment.newInstance(projectID, buildingID, location, position);
        loadFragment(fragment);
        /*
        this.position = position;
        BertUnit b = bertList.get(position);
        deviceNameTextField.setText(b.getName());
        macAddressTextField.setText(b.getMAC());
        roomTextField.setText(b.getLocation());
        buildingTextField.setText(project.getBuildings().get(b.getBuildingID()).getName());
        categorySelector.setSelection(bertList.get(position).getCategoryID());
        setDetailViewVisibility(true);(/*/
    }

    private void addDevice(){
        System.out.println("adding device");
        DeviceDetailAddFragment fragment = new DeviceDetailAddFragment();
        loadFragment(fragment);
    }

    private void loadFragment(Fragment frag) {
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.bertDeviceDetailViewFrame, frag);
        t.addToBackStack(null);
        t.commit();
    }

    private void saveChanges() {

    }
}