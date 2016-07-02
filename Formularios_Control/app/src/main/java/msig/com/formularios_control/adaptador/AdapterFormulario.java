package msig.com.formularios_control.adaptador;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import msig.com.formularios_control.R;
import msig.com.formularios_control.objetos.Formulario;

/**
 * Created by wmazariegos on 08/06/2016.
 */
public class AdapterFormulario extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<Formulario> items;

    public AdapterFormulario(Activity activity, ArrayList<Formulario> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    public void clear() {
        items.clear();
    }

    public void addAll(ArrayList<Formulario> category) {
        for (int i = 0; i < category.size(); i++) {
            items.add(category.get(i));
        }
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
            v = inf.inflate(R.layout.item_adapter, null);
        }

        Formulario dir = items.get(position);
        TextView title = (TextView) v.findViewById(R.id.category);
        title.setText(dir.getTitle());

        TextView description = (TextView) v.findViewById(R.id.texto);
        description.setText(dir.getDescription());

        CircleImageView imagen = (CircleImageView) v.findViewById(R.id.imageView4);
        //ImageView imagen = (ImageView) v.findViewById(R.id.imageView4);
        //imagen.setImageDrawable(dir.getImage());
        imagen.setImageResource(dir.getImage());

        return v;
    }
}
