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

import bert.ui.R;

/**
 * Created by liamcook on 6/17/15.
 * @author afiolmahon
 * @author lcook
 */
public class SelectableListGVA extends ArrayAdapter<String> {

    private int selectedPosition = -1;

    public List<String> titles;
    private Activity activity;

    private View lastSelectionView;
    private ArrayList<View> listCells = new ArrayList<>();

    private int cellColor;
    private int selectedCellColor;

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
        } else {
            selectedPosition = position;
        }
    }

    public void selectView(View selectedView) {
        clearSelection();
        selectedView.setBackgroundColor(selectedCellColor);
        lastSelectionView = selectedView;
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

        listCells.add(position, listCell);
        TextView textView = (TextView) listCell.findViewById(android.R.id.text1);
        textView.setText(titles.get(position));

        listCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectView(view);
                listener.onItemClick(null, view, position, 0);
                selectedPosition = position;
            }
        });

        return listCell;
    }

    public void clearSelection() {
        for (View view : listCells){
            view.setBackgroundColor(activity.getResources().getColor(R.color.LightGreen));
        }
    }
}
