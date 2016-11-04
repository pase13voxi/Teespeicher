package coolpharaoh.tee.speicher.tea.timer;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TeaAdapter extends BaseAdapter
{

    LayoutInflater inflater;
    List<Tea> items;

    public TeaAdapter(Activity context, List<Tea> items) {
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

        Tea item = items.get(position);

        View vi=convertView;

        if(convertView==null)
            vi = inflater.inflate(R.layout.tealist_single_layout, null);

        TextView txtName = (TextView) vi.findViewById(R.id.textViewListTeaName);
        TextView txtSort = (TextView) vi.findViewById(R.id.textViewListSortOfTea);

        txtName.setText(item.getName());
        txtSort.setText(item.getSortOfTea());

        return vi;
    }
}