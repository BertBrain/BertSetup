package bert.ui.common;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bert.data.ProjectProvider;
import bert.ui.R;

/**
 * Created by liamcook on 6/17/15.
 * @author afiolmahon
 * @author lcook
 */
public class SelectableListGVA extends ArrayAdapter<String> {

    private Activity activity;
    private ListView listView;

    public List<String> titles;
    private ArrayList<View> listCells = new ArrayList<>();

    private int cellColor;
    private int selectedCellColor;

    private int selectedPosition = -1;

    private OnListClickedListener listener;

    public SelectableListGVA(Activity activity, List<String> titles) {

        super(activity, android.R.layout.simple_list_item_1, titles);
        this.activity = activity;
        this.titles = titles;
        this.activity = activity;
        this.cellColor = activity.getResources().getColor(R.color.LightGreen);
        this.selectedCellColor = activity.getResources().getColor(R.color.ListSelection);
    }

    public void setListener(OnListClickedListener listener) {
        this.listener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (listView == null ) {
            listView = (ListView) parent;
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d("GVA", "position clicked: " + i);
                    indexPressed(i);
                    listener.onTitlePressed(titles.get(i));
                }
            });
        }

        View listCell;
        try {
            listCell = listCells.get(position);
            Log.d("GVA", "reusing cell: " + listCell);
        } catch (IndexOutOfBoundsException e) {
            listCell = activity.getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);

            TextView textView = (TextView) listCell.findViewById(android.R.id.text1);
            textView.setText(titles.get(position));

            Log.d("GVA", "adding cell: " + listCell);
            listCells.add(position, listCell);
        }

        Log.d("selectingview", "selectedposition: " + selectedPosition + "position: " + position);
        if (position == selectedPosition) {
            listCell.setBackgroundColor(selectedCellColor);
        }

        return listCell;
    }

    public void indexPressed(int position) {
        clearSelection();
        selectedPosition = position;
        try {
            View listCell = listCells.get(position);
            listCell.setBackgroundColor(selectedCellColor);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            //this can occur when an index is selected but the view has not loaded yet
        }
    }

    public void setTempTitleForPosition(String tempTitleForPosition, int position) {
        try {
            View listCell = listCells.get(position);

            Log.d("GVA", "setting temp title: " + tempTitleForPosition + " in cell: " + listCell);
            TextView textView = (TextView) listCell.findViewById(android.R.id.text1);
            textView.setText(tempTitleForPosition);
            forceRedraw();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

    }

    public void setRealTitleIDForPosition(String title, int position) {
        Log.d("GVA", "setting real title: " + title);
        setTempTitleForPosition(title, position);
        try {
            titles.set(position, title);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public void clearSelection() {

        Log.d("GVA", "clearing selection");
        for (View view : listCells){
            view.setBackgroundColor(cellColor);
        }
        forceRedraw();
    }

    public int getCurrentSelectionIndex(){
        Log.d("GVA", "current pos: " + selectedPosition);
        return selectedPosition;
    }

    private void forceRedraw() {
        if (listView != null)  {
            listView.invalidateViews();
        }
    }
}