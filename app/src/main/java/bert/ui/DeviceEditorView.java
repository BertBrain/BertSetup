package bert.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.EditText;

import bert.ui.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeviceEditorView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeviceEditorView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceEditorView extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private String location;
    private TextView tempLocationDisplay;

    private EditText deviceNameTextField;
    private EditText macAdressTextField;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeviceEditorView.
     */
    // TODO: Rename and change types and number of parameters
    public static DeviceEditorView newInstance(String param1, String param2) {
        DeviceEditorView fragment = new DeviceEditorView();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public void onResume(){
        super.onResume();
        System.out.println(getView());
        macAdressTextField = (EditText) getView().findViewById(R.id.macAdress);
        deviceNameTextField = (EditText) getView().findViewById(R.id.deviceName);
        tempLocationDisplay = (TextView) getView().findViewById(R.id.tempLocationDisplay);
        System.out.println(getArguments());
        if (getArguments() != null){
            tempLocationDisplay.setText(getArguments().getString("location"));
        } else {
            System.out.println("null arguments to device editor view");
        }

        String[] names = {"Printer 1", "Printer 2", "Projector"}; //TODO: pull from database
        ArrayAdapter<String> locationTableAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, names);

        ListView locationListView = (ListView) getView().findViewById(R.id.nameList);
        locationListView.setAdapter(locationTableAdapter);

        locationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                loadDeviceNamed(String.valueOf(position)); //TODO: make this pull from database

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
    }

    private void loadDeviceNamed(String deviceName){
        System.out.println("loading device named" + deviceName);
        //TODO: implement this, pull from database
        //TODO: make this update text fields
        deviceNameTextField.setText(deviceName);
    }
}
