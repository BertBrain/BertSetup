package bert.ui.common;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

    public int selectedPosition = -1;

    public List<String> titles;
    private Activity activity;

    private ArrayList<View> listCells = new ArrayList<>();

    private int cellColor;
    private int selectedCellColor;

    public View selectedView;

    AdapterView.OnItemClickListener listener;

    public SelectableListGVA(Activity activity, List<String> titles) {

        super(activity, android.R.layout.simple_list_item_1, titles);
        this.activity = activity;
        this.titles = titles;
        this.activity = activity;
        this.cellColor = activity.getResources().getColor(R.color.LightGreen);
        this.selectedCellColor = activity.getResources().getColor(R.color.ListSelection);
    }

    public void selectView(int position) {
        Log.d("selectingview", "position: " + position);
        Log.d("selectingview", "number of list cells: " + listCells.size());
        if (listCells.size() != 0) {
            View v = listCells.get(position);
            selectView(v);
        }
        selectedPosition = position;
    }

    public void selectView(View selectedView) {
        clearSelection();
        selectedView.setBackgroundColor(selectedCellColor);
        this.selectedView = selectedView;
    }

    public void setOnClickListener() {}

    public void setOnClickListener(  AdapterView.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final View listCell = activity.getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);

        Log.d("selectingview", "selectedposition: " + selectedPosition + "position: " + position);
        if (position == selectedPosition) {
            listCell.setBackgroundColor(selectedCellColor);
        }

        TextView textView = (TextView) listCell.findViewById(android.R.id.text1);
        textView.setText(titles.get(position));

        listCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("GVA", "click index: " + position);
                selectedPosition = position;
                selectView(view);
                listener.onItemClick(null, view, position, 0);
            }
        });

        try {
            listCells.set(position, listCell);
        } catch (IndexOutOfBoundsException e) {
            listCells.add(position, listCell);
        }

        return listCell;
    }

    public void clearSelection() {
        if (selectedView != null) {
            selectedView.setBackgroundColor(cellColor);
        }
        for (View view : listCells){
            view.setBackgroundColor(cellColor);
        }
    }

    public int getCurrentSelectionIndex(){
        Log.d("GVA", "current pos: " + selectedPosition);
        return selectedPosition;
    }
}
