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
 */
public class SelectableListGVA extends ArrayAdapter<String> {

    private int selectedPosition = -1;

    public List<String> titles;
    private Activity activity;

    private View lastSelectionView;
    private ArrayList<View> listCells = new ArrayList<>();

    AdapterView.OnItemClickListener listener;

    public SelectableListGVA(Activity activity, List<String> titles) {
        super(activity, android.R.layout.simple_list_item_1, titles);
        this.activity = activity;
        this.titles = titles;
    }

    public void selectView(int position) {
        if (listCells.size() != 0) {
            View selectedView = listCells.get(position);
            clearSelection();
            selectedView.setBackgroundColor(activity.getResources().getColor(R.color.ListSelection));
            lastSelectionView = selectedView;
        } else {
            selectedPosition = position;
        }
    }

    public void setOnClickListener() {}

    public void setOnClickListener(  AdapterView.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.d("SELECTABLE_LIST_GVA", "getting view at position: " + position);

        final View listCell = activity.getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);

        if (position == selectedPosition) {
            lastSelectionView = listCell;
            listCell.setBackgroundColor(activity.getResources().getColor(R.color.ListSelection));
        }
        Log.d("SELECTABLE_LIST_GVA", "listCell: " + listCell);
        listCells.add(position, listCell);
        Log.d("SELECTABLE_LIST_GVA", "list cells: " + listCells);
        TextView textView = (TextView) listCell.findViewById(android.R.id.text1);
        textView.setText(titles.get(position));

        listCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SELECTABLE_LIST_GVA", "item selected");
                if (lastSelectionView != null) {
                    lastSelectionView.setBackgroundColor(activity.getResources().getColor(R.color.LightGreen));
                }
                view.setBackgroundColor(activity.getResources().getColor(R.color.ListSelection));
                lastSelectionView = view;
                listener.onItemClick(null, view, position, 0);
            }
        });

        return listCell;
    }

    public void clearSelection() {
        if (lastSelectionView != null) {
            lastSelectionView.setBackgroundColor(activity.getResources().getColor(R.color.LightGreen));
        }
    }
}
