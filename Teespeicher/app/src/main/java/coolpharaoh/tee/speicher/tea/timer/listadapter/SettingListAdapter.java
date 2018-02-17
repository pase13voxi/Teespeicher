package coolpharaoh.tee.speicher.tea.timer.listadapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import coolpharaoh.tee.speicher.tea.timer.R;

/**
 * Created by paseb on 14.02.2018.
 */

public class SettingListAdapter extends BaseAdapter {


    LayoutInflater inflater;
    List<ListRowItem> items;

    public SettingListAdapter(Activity context, List<ListRowItem> items) {
        super();

        this.items = items;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ListRowItem item = items.get(position);

        View vi=convertView;

        if(convertView==null)
            vi = inflater.inflate(R.layout.settinglist_single_layout, null);

        TextView txtName = (TextView) vi.findViewById(R.id.textViewListSettingHeading);
        TextView txtSort = (TextView) vi.findViewById(R.id.textViewListSettingDescription);

        txtName.setText(item.getHeading());
        txtSort.setText(item.getDescription());

        return vi;
    }
}