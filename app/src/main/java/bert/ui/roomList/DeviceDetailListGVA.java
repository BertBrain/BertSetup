package bert.ui.roomList;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import bert.data.proj.BertUnit;
import bert.data.proj.Category;
import bert.ui.R;

/**
 * Created by liamcook on 5/20/15.
 */
public class DeviceDetailListGVA extends ArrayAdapter<BertUnit> {

    List <BertUnit> berts;
    Activity activity;
    DeviceListFragment owner;

    public DeviceDetailListGVA(Activity activity, DeviceListFragment owner, List<BertUnit> berts) {
        super(activity, android.R.layout.simple_list_item_1, berts);
        this.owner = owner;
        this.activity = activity;
        this.berts = berts;
    }

    @Override
    public int getCount(){
        return berts.size() + 1;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView != null) {
            return convertView;
        } else {
            View listCell = activity.getLayoutInflater().inflate(R.layout.fragment_device_list_cell, parent, false);
            TextView textView = (TextView) listCell.findViewById(R.id.deviceListTextField);
            Button indicator = (Button) listCell.findViewById(R.id.hasMACIndicator);

            boolean isAddBertButton = (position == getCount() - 1);

            if (isAddBertButton) {
                textView.setText(DeviceListFragment.ADD_BUILDING_STRING);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        owner.addDevice();
                    }
                });
                indicator.setVisibility(View.INVISIBLE);
            } else {
                textView.setText(berts.get(position).getName());
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        owner.loadDeviceAtPosition(position);
                    }
                });
                indicator.setVisibility(berts.get(position).getMAC().length() == 0 ? View.VISIBLE : View.INVISIBLE);
            }
            return listCell;
        }
    }
}
