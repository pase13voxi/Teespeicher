package coolpharaoh.tee.speicher.tea.timer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by paseb on 03.11.2016.
 */

public class SoftwareListAdapter extends BaseAdapter
{

    LayoutInflater inflater;
    List<ListRowItem> items;

    public SoftwareListAdapter(Activity context, List<ListRowItem> items) {
        super();

        this.items = items;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ListRowItem item = items.get(position);

        View vi=convertView;

        if(convertView==null)
            vi = inflater.inflate(R.layout.softwarelist_single_layout, null);

        TextView txtName = (TextView) vi.findViewById(R.id.textViewListSoftwareHeading);
        TextView txtSort = (TextView) vi.findViewById(R.id.textViewListSoftwareDescription);

        txtName.setText(item.getHeading());
        txtSort.setText(item.getDescription());

        return vi;
    }
}
