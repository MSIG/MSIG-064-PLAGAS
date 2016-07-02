package msig.com.formularios_control;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import msig.com.formularios_control.adaptador.AdapterFormulario;
import msig.com.formularios_control.formularios.FormularioChincheSalivosa;
import msig.com.formularios_control.formularios.VerMaestrosChincheSalivosa;
import msig.com.formularios_control.objetos.Formulario;

public class ListadoFormularios extends AppCompatActivity implements AdapterView.OnItemClickListener {
    String operacion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_formularios);
        ArrayList<Formulario> category = new ArrayList<>();
        ListView lv = (ListView) findViewById(R.id.lista_activity_main);
        category.add(new Formulario("id","Chinche salivosa","Formulario para el muestreo de chinche salivosa",R.drawable.chinche_salivosa));
        AdapterFormulario adapter = new AdapterFormulario(this, category);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        operacion = getIntent().getExtras().getString("operacion");
        if(position == 0){
            if(operacion.equals("llenar")){
                Intent verForm;
                verForm = new Intent(this, FormularioChincheSalivosa.class);
                verForm.putExtra("id_form",0);
                startActivity(verForm);
            }
            if(operacion.equals("ver")){
                Intent verForm;
                verForm = new Intent(this, VerMaestrosChincheSalivosa.class);
                startActivity(verForm);
            }
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("OPERACION", operacion);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        operacion = savedInstanceState.getString("OPERACION");
    }

}
