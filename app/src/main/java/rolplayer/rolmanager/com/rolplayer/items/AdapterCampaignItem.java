package rolplayer.rolmanager.com.rolplayer.items;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import rolplayer.rolmanager.com.rolplayer.R;

/**
 * Created by TiranoJuan on 31/10/2017.
 */

public class AdapterCampaignItem extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<CampaignItem> items = new ArrayList<>();

    public AdapterCampaignItem(Activity activity, ArrayList<CampaignItem> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void addAll(ArrayList<CampaignItem> category) {
        for (int i = 0; i < category.size(); i++) {
            items.add(category.get(i));
        }
    }

    public void clear() {
        items.clear();
    }

    @Override
    public Object getItem(int arg0) {
        return items.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (convertView == null) {
            LayoutInflater inf = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.item_campa, null);
        }

        CampaignItem dir = items.get(position);

        TextView nombre_camp = (TextView) v.findViewById(R.id.nombre);
        nombre_camp.setText(dir.getNombre());

        TextView nombre_mas= (TextView) v.findViewById(R.id.nombreMaster);
        nombre_mas.setText(dir.getNombreMaster());

        if (dir.getImagen() != null) {
            ImageView imagen = (ImageView) v.findViewById(R.id.menuItem_icono);
            imagen.setImageDrawable(dir.getImagen());
        }
        return v;
    }

    public void setItems(ArrayList<CampaignItem> items) {
        this.items = items;
    }
}
