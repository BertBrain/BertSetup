package bert.ui.roomList;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
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
 * {@link DeviceEditorView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeviceEditorView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceEditorView extends Fragment {
    public static final String ARG_LOCATION = "location";
    public static final String ARG_BUILDING = "building";

    private RoomListActivity activity;
    private Project project;
    private int buildingID;
    private String location;
    private int position;
    private List<BertUnit> bertList;

    private OnFragmentInteractionListener mListener;
    private EditText deviceNameTextField;
    private EditText macAddressTextField;
    private EditText roomTextField;
    private EditText buildingTextField;
    private ArrayAdapter<String> deviceTableAdapter;
    private ListView locationListView;
    private FrameLayout detailView;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param location the room used to populate the bertlist.
     * @return A new instance of fragment DeviceEditorView.
     */
    public static DeviceEditorView newInstance(String building, String location) {
        DeviceEditorView fragment = new DeviceEditorView();
        Bundle args = new Bundle();
        args.putString(ARG_BUILDING, building);
        args.putString(ARG_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    public DeviceEditorView() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        activity = (RoomListActivity) getActivity();
        project = activity.getProject();
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            buildingID = getArguments().getInt(ARG_BUILDING);
            location = getArguments().getString(ARG_LOCATION);
            bertList = project.getBertsByLocation(buildingID, location);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        macAddressTextField = (EditText) getView().findViewById(R.id.macAddress);
        deviceNameTextField = (EditText) getView().findViewById(R.id.deviceName);
        roomTextField = (EditText) getView().findViewById(R.id.room);
        buildingTextField = (EditText) getView().findViewById(R.id.building);
        setDetailViewVisibility(false);

        List<String> bertNameList = new ArrayList<String>();
        if (getArguments() != null) {
            for (BertUnit bert : bertList) {
                bertNameList.add(bert.getName());
            }
        }

        deviceTableAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, bertNameList);

        locationListView = (ListView) getView().findViewById(R.id.bertList);
        locationListView.setAdapter(deviceTableAdapter);

        locationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadDeviceAtPosition(position);
            }
        });

        macAddressTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE && v == macAddressTextField) {
                    updateMAC();
                }
                return false;
            }
        });

        deviceNameTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
           @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
               if (actionId == EditorInfo.IME_ACTION_DONE && v == deviceNameTextField) {
                   updateName();
               }
               return false;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    private void updateMAC() {
        if (bertList.size() > 0) {
            bertList.get(position).setMAC(macAddressTextField.getText().toString());
            FileProvider.saveProject(project);
        }
    }

    private void updateName() {
        if (bertList.size() > 0) {
            bertList.get(position).setName(deviceNameTextField.getText().toString());
            FileProvider.saveProject(project);
        }
        onResume();
    }

    private void loadDeviceAtPosition(int position) {
        this.position = position;
        BertUnit b = bertList.get(position);
        deviceNameTextField.setText(b.getName());
        macAddressTextField.setText(b.getMAC());
        roomTextField.setText(b.getLocation());
        buildingTextField.setText(project.getBuildings().get(b.getBuildingID()).getName());
        setDetailViewVisibility(true);
    }

    private void setDetailViewVisibility(boolean isVisible) {
        detailView = (FrameLayout) getView().findViewById(R.id.bertDeviceDetailViewFrame);
        detailView.setVisibility((isVisible) ? View.VISIBLE : View.INVISIBLE);
    }
}