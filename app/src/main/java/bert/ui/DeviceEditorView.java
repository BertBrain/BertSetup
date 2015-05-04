package bert.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import bert.database.BertUnit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeviceEditorView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeviceEditorView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceEditorView extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_LOCATION = "location";

    private String location;
    private int position;

    private OnFragmentInteractionListener mListener;

    private EditText deviceNameTextField;
    private EditText macAddressTextField;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param location the room used to populate the bertlist.
     * @return A new instance of fragment DeviceEditorView.
     */
    // TODO: Rename and change types and number of parameters
    public static DeviceEditorView newInstance(String location) {
        DeviceEditorView fragment = new DeviceEditorView();
        Bundle args = new Bundle();
        args.putString(ARG_LOCATION, location);
        fragment.setArguments(args);
        return fragment;
    }

    public DeviceEditorView() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            location = getArguments().getString(ARG_LOCATION);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        System.out.println(getView());
        macAddressTextField = (EditText) getView().findViewById(R.id.macAdress);
        deviceNameTextField = (EditText) getView().findViewById(R.id.deviceName);
        System.out.println(getArguments());


        MainActivity activity = (MainActivity)getActivity();
        List<String> names = new ArrayList<String>();
        if (getArguments() != null) {
            for (BertUnit bert : getBertList()) {
                names.add(bert.getName());
            }
        }

        ArrayAdapter<String> locationTableAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, names);

        ListView locationListView = (ListView) getView().findViewById(R.id.nameList);
        locationListView.setAdapter(locationTableAdapter);

        locationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadDeviceAtPostion(position);
            }
        });

        macAddressTextField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    updateMAC();
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
        public List<BertUnit> getBertListForLocation(String location);
    }

    private void updateMAC() {
        getBertList().get(position).setMAC(macAddressTextField.getText().toString());
    }

    private void loadDeviceAtPostion(int position){
        this.position = position;
        BertUnit b = getBertList().get(position);
        deviceNameTextField.setText(b.getName());
        macAddressTextField.setText(b.getMAC());
    }

    private List<BertUnit> getBertList(){
        MainActivity activity = (MainActivity) getActivity();
        List<BertUnit> berts;
        if (getArguments() != null) {
            berts = activity.getBertListForLocation(location);
        } else {
            berts = new ArrayList<BertUnit>();
        }
        return  berts;
    }
}
