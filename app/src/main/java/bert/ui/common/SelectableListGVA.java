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
//FIXME the first time the list is interacted with the selection will not update but will behave like normal after 1 click

    private int selectedPosition = -1;

    public List<String> titles;
    private Activity activity;

    private ArrayList<View> listCells = new ArrayList<>();

    private int cellColor;
    private int selectedCellColor;

    AdapterView.OnItemClickListener listener;

    public SelectableListGVA(Activity activity, List<String> titles) {
        super(activity, android.R.layout.simple_list_item_1, titles);
        this.activity = activity;
        this.titles = titles;
        this.cellColor = activity.getResources().getColor(R.color.LightGreen);
        this.selectedCellColor = activity.getResources().getColor(R.color.ListSelection);
    }

    public void selectView(int position) {
        log("Position " + position + " selected");
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
    }

    public void setOnClickListener() {}

    public void setOnClickListener(AdapterView.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final View listCell = activity.getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);

        if (position == selectedPosition) {
            selectView(listCell);
        }

        listCells.add(position, listCell);

        TextView cellText = (TextView) listCell.findViewById(android.R.id.text1);
        cellText.setText(titles.get(position));

        listCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectView(view);
                selectedPosition = position;
                listener.onItemClick(null, view, position, 0);
            }
        });
        return listCell;
    }

    public void clearSelection() {
        for (View view : listCells) {
            view.setBackgroundColor(cellColor);
        }
    }

    public void log(String message) {
        Log.d("SELECTABLE_LIST_GVA", message);
    }
}
