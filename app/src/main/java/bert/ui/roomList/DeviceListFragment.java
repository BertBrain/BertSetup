package bert.ui.roomList;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import bert.data.proj.BertUnit;
import bert.data.proj.Project;
import bert.ui.R;

public class DeviceListFragment extends Fragment {
    public static final String ARG_LOCATION = "LOCATION";
    public static final String ARG_BUILDING = "BUILDING_ID";
    public static final String ARG_PROJECT = "PROJECT_ID";

    public static final String ADD_BUILDING_STRING = "+ Add Bert";

    private RoomListActivity activity;
    private Project project;
    private int projectID;
    private int buildingID;
    private String location;
    private List<BertUnit> bertList;

    private ArrayAdapter<String> deviceTableAdapter;
    private ListView locationListView;
    private FrameLayout detailView;

    public static DeviceListFragment newInstance(int projectID, int buildingID, String location) {
        DeviceListFragment fragment = new DeviceListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PROJECT, projectID);
        args.putInt(ARG_BUILDING, buildingID);
        args.putString(ARG_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    public DeviceListFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        activity = (RoomListActivity) getActivity();
        project = activity.getProject();
        super.onCreate(savedInstanceState);

        loadFromArgs();
    }

    private void loadFromArgs() {
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
        List<String> bertNameList = new ArrayList<>();
        locationListView = (ListView) getView().findViewById(R.id.bertList);
        loadList();
    }

    private void loadList() {
        List<String> bertNameList = new ArrayList<String>();
        if (getArguments() != null) {
            for (BertUnit bert : bertList) {
                bertNameList.add(bert.getName());
            }
        }
        final ArrayList<String> listStrings = new ArrayList<>(bertNameList);
        if (listStrings.size() != 0){
            listStrings.add(ADD_BUILDING_STRING);
        }
        deviceTableAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, listStrings);
        locationListView = (ListView) getView().findViewById(R.id.bertList);
        locationListView.setAdapter(deviceTableAdapter);
        locationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == listStrings.size() - 1) {
                    addDevice();
                } else {
                    loadDeviceAtPosition(position);
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_editor_view, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void loadNewDevice(){
        loadFromArgs();
        loadList();
        loadDeviceAtPosition(deviceTableAdapter.getCount() - 2);
    }

    private void loadDeviceAtPosition(int position) {
        System.out.println("editing device");
        DeviceDetailEditFragment fragment = DeviceDetailEditFragment.newInstance(projectID, buildingID, location, position);
        loadFragment(fragment);
    }

    private void addDevice() {
        System.out.println("adding device");
        DeviceDetailAddFragment fragment = DeviceDetailAddFragment.newInstance(projectID, buildingID, location);
        loadFragment(fragment);
    }

    private void loadFragment(Fragment frag) {
        FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.bertDeviceDetailViewFrame, frag);
        t.addToBackStack(null);
        t.commit();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}