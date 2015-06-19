package bert.ui.common;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bert.ui.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * Use the {@link NoSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoSelectionFragment extends Fragment {
    private static final String ARG_MESSAGE = "message";
    private String mMessage;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param message message to display in fragment.
     * @return A new instance of fragment NoSelectionFragment.
     */
    public static NoSelectionFragment newInstance(String message) {
        NoSelectionFragment fragment = new NoSelectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    public NoSelectionFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMessage = getArguments().getString(ARG_MESSAGE);
        } else {
            mMessage = "No Message";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_common_no_selection, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView messageView = (TextView) getView().findViewById(R.id.noSelectionTextView);
        messageView.setText(mMessage);
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
