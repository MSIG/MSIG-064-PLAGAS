package msig.com.formularios_control.adaptador;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import msig.com.formularios_control.R;
import msig.com.formularios_control.objetos.MaestroFormulario;

/**
 * Created by wmazariegos on 10/06/2016.
 */
public class AdapterMaestroChincheSalivosa extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<MaestroFormulario> items;

    public AdapterMaestroChincheSalivosa(Activity activity, ArrayList<MaestroFormulario> items) {
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

    public void addAll(ArrayList<MaestroFormulario> category) {
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
            v = inf.inflate(R.layout.item_adapter_maestro_chinchesalivosa, null);
        }

        MaestroFormulario dir = items.get(position);

        TextView id_form = (TextView) v.findViewById(R.id.txt_maestro_chinchesalivosa_idform);
        id_form.setText(dir.getId_form());

        TextView no_finca = (TextView) v.findViewById(R.id.txt_maestro_chinchesalivosa_nofinca);
        no_finca.setText("Finca: "+dir.getNo_finca());

        TextView no_ficha = (TextView) v.findViewById(R.id.txt_maestro_chinchesalivosa_noficha);
        no_ficha.setText("Ficha: " +dir.getNo_ficha());

        TextView lote = (TextView) v.findViewById(R.id.txt_maestro_chinchesalivosa_lote);
        lote.setText("Lote: "+dir.getLote());

        TextView area = (TextView) v.findViewById(R.id.txt_maestro_chinchesalivosa_area);
        area.setText("Area: "+dir.getArea());

        TextView fecha_muestreo = (TextView) v.findViewById(R.id.txt_maestro_chinchesalivosa_fechamuestreo);
        fecha_muestreo.setText("Fecha muestreo: "+dir.getF_muestreo());

        TextView no_muestreo = (TextView) v.findViewById(R.id.txt_maestro_chinchesalivosa_nomuestreo);
        no_muestreo.setText("Muestreo: " +dir.getNo_muestreo());

        TextView cargoresp = (TextView) v.findViewById(R.id.txt_maestro_chinchesalivosa_cargoresp);
        cargoresp.setText("Cargo: "+dir.getCargo_resp());

        TextView nombreresp = (TextView) v.findViewById(R.id.txt_maestro_chinchesalivosa_nombreresp);
        nombreresp.setText("Responsable: "+dir.getNombre_resp());

        return v;
    }
}
