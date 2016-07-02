package msig.com.formularios_control.formularios;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import msig.com.formularios_control.R;
import msig.com.formularios_control.adaptador.AdapterDetalleChincheSalivosa;
import msig.com.formularios_control.adaptador.AdapterFormulario;
import msig.com.formularios_control.adaptador.AdapterMaestroChincheSalivosa;
import msig.com.formularios_control.localhost.AdminDataBase;
import msig.com.formularios_control.objetos.DetalleFormulario;
import msig.com.formularios_control.objetos.Formulario;
import msig.com.formularios_control.objetos.MaestroFormulario;

public class VerMaestrosChincheSalivosa extends AppCompatActivity implements AdapterView.OnItemClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_maestros_chinche_salivosa);

        ArrayList<MaestroFormulario> category = consulta();
        ListView lv = (ListView) findViewById(R.id.list_detalle_chinche_salivosa);
        AdapterMaestroChincheSalivosa adapter = new AdapterMaestroChincheSalivosa(this, category);

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView textView = (TextView) view.findViewById(R.id.txt_maestro_chinchesalivosa_idform);
        String text = textView.getText().toString();
        //Toast.makeText(this,"Seleccionado "+text,Toast.LENGTH_SHORT).show();

        int id_enviado = Integer.parseInt(text);
        Intent verForm;
        verForm = new Intent(this, FormularioChincheSalivosa.class);
        verForm.putExtra("id_form",id_enviado);
        startActivity(verForm);
    }
    public ArrayList<MaestroFormulario> consulta(){
        ArrayList<MaestroFormulario> category = new ArrayList<>();
        AdminDataBase admin = new AdminDataBase(this,"registro_muestreo", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor fila = bd.rawQuery("select * from form_chinche_salivosa order by 1",null);
        Integer numero_datos = fila.getCount();
        if (numero_datos > 0) {
            if (fila.moveToFirst()) {
                do {
                    MaestroFormulario obj;
                    obj = new MaestroFormulario(fila.getString(0), fila.getString(1), fila.getString(2), fila.getString(3),fila.getString(4),fila.getString(5),fila.getString(6),fila.getString(7),fila.getString(8));
                    category.add(obj);
                } while (fila.moveToNext());
            }

        }
        return category;
    }
}
