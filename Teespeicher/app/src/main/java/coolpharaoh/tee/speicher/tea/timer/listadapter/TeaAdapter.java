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
import coolpharaoh.tee.speicher.tea.timer.datastructure.N2Tea;

public class TeaAdapter extends BaseAdapter
{

    private LayoutInflater inflater;
    private List<N2Tea> items;

    public TeaAdapter(Activity context, List<N2Tea> items) {
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

        N2Tea item = items.get(position);

        View vi=convertView;

        if(convertView==null)
            vi = inflater.inflate(R.layout.tealist_single_layout,parent,false);

        TextView txtName = vi.findViewById(R.id.textViewListTeaName);
        TextView txtSort = vi.findViewById(R.id.textViewListSortOfTea);

        txtName.setText(item.getName());
        txtSort.setText(item.getSortOfTea().getType());

        return vi;
    }
}
