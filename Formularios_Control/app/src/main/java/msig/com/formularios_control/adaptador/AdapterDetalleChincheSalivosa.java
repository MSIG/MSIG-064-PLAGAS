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
import msig.com.formularios_control.objetos.DetalleFormulario;
import msig.com.formularios_control.objetos.Formulario;

/**
 * Created by wmazariegos on 08/06/2016.
 */
public class AdapterDetalleChincheSalivosa extends BaseAdapter {

    protected Activity activity;
    protected ArrayList<DetalleFormulario> items;

    public AdapterDetalleChincheSalivosa(Activity activity, ArrayList<DetalleFormulario> items) {
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

    public void addAll(ArrayList<DetalleFormulario> category) {
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
            v = inf.inflate(R.layout.item_adapter_detalle_chinchesalivosa, null);
        }

        DetalleFormulario dir = items.get(position);

        TextView id_form = (TextView) v.findViewById(R.id.txt_detalle_id_form);
        id_form.setText(dir.getId_form());

        TextView estacion = (TextView) v.findViewById(R.id.txt_detalle_estacion);
        estacion.setText(dir.getEstacion());

        TextView vivos = (TextView) v.findViewById(R.id.txt_detalle_vivos);
        vivos.setText(dir.getVivos());

        TextView ninfa1 = (TextView) v.findViewById(R.id.txt_detalle_ninfa1);
        ninfa1.setText(dir.getNinfa1());

        TextView ninfa2 = (TextView) v.findViewById(R.id.txt_detalle_ninfa2);
        ninfa2.setText(dir.getNinfa2());

        TextView tallos = (TextView) v.findViewById(R.id.txt_detalle_tallos);
        tallos.setText(dir.getTallos());

        TextView calculo = (TextView) v.findViewById(R.id.txt_detalle_calculo);
        calculo.setText(dir.getCalculo());

        return v;
    }
}
