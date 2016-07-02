package msig.com.formularios_control.formularios;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import msig.com.formularios_control.R;
import msig.com.formularios_control.adaptador.AdapterDetalleChincheSalivosa;
import msig.com.formularios_control.detalles.AddDetalleChincheSalivosa;
import msig.com.formularios_control.interfaces.FormularioListener;
import msig.com.formularios_control.localhost.AdminDataBase;
import msig.com.formularios_control.objetos.DetalleFormulario;
import msig.com.formularios_control.objetos.MaestroFormulario;

public class FormularioChincheSalivosa extends AppCompatActivity implements AdapterView.OnItemClickListener, FormularioListener {
    @Bind(R.id.txt_head_Cargo_resp) EditText txtCargo_resp;
    @Bind(R.id.txt_head_Fecha_muestreo) EditText txtFecha_muestreo;
    @Bind(R.id.txt_head_no_ficha) EditText txtNo_ficha;
    @Bind(R.id.txt_head_Nombre_resp) EditText txtNombre_resp;
    @Bind(R.id.txt_head_Numero_finca) EditText txtNumero_finca;
    @Bind(R.id.txt_head_Numero_muestra) EditText txtNumero_muestra;
    @Bind(R.id.txt_head_Lote) EditText txtLote;
    @Bind(R.id.txt_head_area) EditText txtArea;
    @Bind(R.id.btn_chinchesalivosa_adddetalle) Button btn_chinchesalivosa_adddetalle;
    @Bind(R.id.btn_form_chinchesalivosa_actualizar) Button btn_actualizar;
    @Bind(R.id.list_detalle_chinche_salivosa) ListView listDetalle;

    private DatePicker datePicker;                              //----------------------------------
    private Calendar calendar;
    private int id_form;
    private String modo;
    private int year, month, day;
    private boolean INSERTO_MAESTRO = false;                    //GUARDA EL ESTADO SI SE INSERTA

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_chinche_salivosa);
        ButterKnife.bind(this); //<---- Hace la inyeccion con la vista
        id_form = getIntent().getExtras().getInt("id_form");    //Recibe el ID del formulario (0 si se va a insertar)
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);                     //Asignacion de dato a variable año
        month = calendar.get(Calendar.MONTH);                   //Asignacion de dato a variable mes
        day = calendar.get(Calendar.DAY_OF_MONTH);              //Asignacion de dato a variable dia
        showDate(year, month+1, day);                           //Mostrar la fecha actual al crear pantalla
        actualizarLista();                                      //Actualiza la lista de detalles
        if(id_form == 0){
            modo="insert";                                      //Carga el modo insertar
            btn_actualizar.setVisibility(View.GONE);            //Oculta el boton actualizar             //Consulta que numero de formulario se va llenar
        }else{
            modo="update";                                      //Carga el modo update
            cargarDatos(id_form);                               //Carga datos y envia el ID del formulario recibido
        }
    }
    /**
     * Llama a la función que mostrara el dialogo y a su vez mostrará el selector de fechas
     * @param view
     */
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }
    /**
     * Crea el dialogo para mostrar el seleccionador de fechas
     * @param id
     * @return
     */
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }
    /**
     * Mostrar en el seleccionador de fechas la fecha actual
     */
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };
    /**
     * Mostrar la fecha seleccionada en el EditText
     * @param year
     * @param month
     * @param day
     */
    private void showDate(int year, int month, int day) {
        txtFecha_muestreo.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }
    /**
     * Funcion del boton para Agregar detalle del formulario
     */
    @OnClick(R.id.btn_chinchesalivosa_adddetalle)
    public void agregarDetalle(){
        if(modo.equals("insert")){
            actualizarLista();
            if(validar()){
                if(guardarLocal()) {
                    AddDetalleChincheSalivosa frag = new AddDetalleChincheSalivosa();
                    Bundle args = new Bundle();
                    id_form = maxId();
                    args.putInt("id_formulario",id_form);
                    args.putString("fecha",txtFecha_muestreo.getText().toString());
                    args.putString("no_ficha",txtNo_ficha.getText().toString());
                    args.putInt("estacion",0);
                    args.putInt("vivos",0);
                    args.putInt("ninfa1",0);
                    args.putInt("ninfa2",0);
                    args.putInt("tallos",0);
                    frag.setArguments(args);
                    frag.setTargetFragment(frag,1);
                    frag.show(getSupportFragmentManager(), "dialog");

                }else{
                    Toast.makeText(this,"Ha ocurrido un error al guardar los datos",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this,"Asegurese de completar todos los campos",Toast.LENGTH_SHORT).show();
            }
        }
        if(modo.equals("update")){ //SI EL FORMULARIO ESTA EN MODO UPDATE EL BOTON PASA A SER "GUARDAR CAMBIOS"
            INSERTO_MAESTRO = true;
            if(validar() == true){
                if(guardarLocal() == true) {
                    Toast.makeText(this,"ACTUALIZADO",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"Ha ocurrido un error al guardar los datos",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this,"Asegurese de completar todos los campos",Toast.LENGTH_SHORT).show();
            }

        }

    }

    @OnClick(R.id.btn_form_chinchesalivosa_actualizar)
    public void actualizar(){
        if(modo.equals("insert")){
            //BOTON ACTUALIZAR EN MODO INSERT
            actualizarLista();
        }
        if(modo.equals("update")){
            //BOTON ACTUALIZAR EN MODO UPDATE PASA A SER AGREGAR DETALLE
            actualizarLista();
            AddDetalleChincheSalivosa frag = new AddDetalleChincheSalivosa();
            Bundle args = new Bundle();
            args.putInt("id_formulario",id_form);
            args.putString("fecha",txtFecha_muestreo.getText().toString());
            args.putString("no_ficha",txtNo_ficha.getText().toString());
            args.putInt("estacion",0);
            args.putInt("vivos",0);
            args.putInt("ninfa1",0);
            args.putInt("ninfa2",0);
            args.putInt("tallos",0);
            frag.setArguments(args);

            frag.setArguments(args);
            frag.show(getSupportFragmentManager(), "");
        }
    }
    /**
     * Guarda en la base de datos local el maestro del formulario
     * @return
     */
    public boolean guardarLocal(){
            if(INSERTO_MAESTRO == false) {
                try{
                    AdminDataBase admin = new AdminDataBase(this, "registro_muestreo", null, 1);
                    SQLiteDatabase bd = admin.getWritableDatabase();
                    ContentValues registro = new ContentValues();
                    registro.put("no_finca", txtNumero_finca.getText().toString());
                    registro.put("no_ficha", txtNo_ficha.getText().toString());
                    registro.put("lote", txtLote.getText().toString());
                    registro.put("area", txtArea.getText().toString());
                    registro.put("f_muestreo", txtFecha_muestreo.getText().toString());
                    registro.put("no_muestreo", txtNumero_muestra.getText().toString());
                    registro.put("cargo_resp", txtCargo_resp.getText().toString());
                    registro.put("nombre_resp", txtNombre_resp.getText().toString());
                    registro.put("enviado","NO");
                    bd.insert("form_chinche_salivosa", null, registro);
                    bd.close();
                    INSERTO_MAESTRO = true;
                    return true;
                }catch (Exception e){
                    return false;
                }
            }else{
                try{
                    AdminDataBase admin = new AdminDataBase(this, "registro_muestreo", null, 1);
                    SQLiteDatabase bd = admin.getWritableDatabase();
                    ContentValues registro = new ContentValues();
                    registro.put("lote", txtLote.getText().toString());
                    registro.put("area", txtArea.getText().toString());
                    registro.put("f_muestreo", txtFecha_muestreo.getText().toString());
                    registro.put("no_muestreo", txtNumero_muestra.getText().toString());
                    registro.put("cargo_resp", txtCargo_resp.getText().toString());
                    registro.put("nombre_resp", txtNombre_resp.getText().toString());

                    String[] args2 = new String[]{String.valueOf(id_form)};
                    bd.update("form_chinche_salivosa",registro,"id_form = ?", args2);
                    bd.close();
                    return true;
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "pone un número!!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
    }
    /**
     * Selecciona el id_form maximo para hacer la nueva incersion
     * @return
     */
    int maxId(){
        int numero_datos=0;
        try{
            AdminDataBase admin = new AdminDataBase(this,"registro_muestreo", null, 1);
            SQLiteDatabase bd = admin.getWritableDatabase();
            Cursor fila = bd.rawQuery("select max(id_form) from form_chinche_salivosa",null);
            System.out.println("CONSULTA DEL MAXIMO ");
            System.out.println("Tamaño de la consulta  "+ fila.getCount());
            if (fila.moveToFirst()) {
                do {
                    numero_datos = fila.getInt(0);
                } while (fila.moveToNext());
                System.out.println("VALOR MAXIMO "+ numero_datos);
            }
        }catch (Exception e){
            System.out.println("ERROR AL CONSULTAR: "+ e);
        }

        return numero_datos;
    }
    /**
     * Valida que los campos no esten vacios
     * @return
     */
    public boolean validar(){
        if(!txtCargo_resp.getText().toString().equals("") &&
            !txtFecha_muestreo.getText().toString().equals("") &&
            !txtNo_ficha.getText().toString().equals("") &&
            !txtNombre_resp.getText().toString().equals("") &&
            !txtNumero_finca.getText().toString().equals("") &&
            !txtNumero_muestra.getText().toString().equals("") &&
            !txtLote.getText().toString().equals("") &&
            !txtArea.getText().toString().equals("") &&
            !txtLote.getText().toString().equals("")){
            /*
            txtCargo_resp.setEnabled(false);
            txtFecha_muestreo.setEnabled(false);
            txtNo_ficha.setEnabled(false);
            txtNombre_resp.setEnabled(false);
            txtNumero_finca.setEnabled(false);
            txtNumero_muestra.setEnabled(false);
            txtArea.setEnabled(false);
            txtLote.setEnabled(false);
            */
            return true;
        }else{
            return false;
        }
    }
    /**
     * Actualiza la lista del formulario activo
     */
    public void actualizarLista(){
        ArrayList<DetalleFormulario> category = consulta();
        ListView lv = (ListView) findViewById(R.id.list_detalle_chinche_salivosa);
        AdapterDetalleChincheSalivosa adapter = new AdapterDetalleChincheSalivosa(this, category);

        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
    }

    /**
     * Metodo que carga los datos del item seleccionado y los envia al Dialogo
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try{
            TextView id_form = (TextView) view.findViewById(R.id.txt_detalle_id_form);
            String form_select = id_form.getText().toString();

            TextView estacion =(TextView) view.findViewById(R.id.txt_detalle_estacion);
            String estacion_select = estacion.getText().toString();

            TextView vivos = (TextView) view.findViewById(R.id.txt_detalle_vivos);
            String vivos_select = vivos.getText().toString();

            TextView ninfa1 =(TextView) view.findViewById(R.id.txt_detalle_ninfa1);
            String ninfa1_select = ninfa1.getText().toString();

            TextView ninfa2 = (TextView) view.findViewById(R.id.txt_detalle_ninfa2);
            String ninfa2_select = ninfa2.getText().toString();

            TextView tallos = (TextView) view.findViewById(R.id.txt_detalle_tallos);
            String tallos_select = tallos.getText().toString();

            AddDetalleChincheSalivosa frag = new AddDetalleChincheSalivosa();
            Bundle args = new Bundle();
            args.putInt("id_formulario",Integer.parseInt(form_select));
            args.putString("fecha",txtFecha_muestreo.getText().toString());
            args.putString("no_ficha",txtNo_ficha.getText().toString());
            args.putInt("estacion",Integer.parseInt(estacion_select));
            args.putInt("vivos",Integer.parseInt(vivos_select));
            args.putInt("ninfa1",Integer.parseInt(ninfa1_select));
            args.putInt("ninfa2",Integer.parseInt(ninfa2_select));
            args.putInt("tallos",Integer.parseInt(tallos_select));
            frag.setArguments(args);
            frag.show(getSupportFragmentManager(), "");
        }catch (Exception e){
            System.out.println("ERROR: "+ e);
        }

    }

    /**
     * Recarga una lista con los datos del detalle
     * @return
     */
    public ArrayList<DetalleFormulario> consulta(){
        ArrayList<DetalleFormulario> category = new ArrayList<>();
        category.add(new DetalleFormulario("Est.","Id Form","Vivos","Ninfa1","Ninfa2","Tallos","Calculo"));
        AdminDataBase admin = new AdminDataBase(this,"registro_muestreo", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();

        Cursor fila = bd.rawQuery("select estacion, id_form, vivos, ninfa1, ninfa2, tallos from detalle_chinche_salivosa where id_form = "+id_form+" order by estacion",null);
        Integer numero_datos = fila.getCount();
        if (numero_datos > 0) {
            if (fila.moveToFirst()) {
                do {
                    DetalleFormulario obj;
                    DecimalFormat decimales = new DecimalFormat("0.00");
                    float calculo = (fila.getInt(2)+fila.getInt(3)+fila.getInt(4))/fila.getFloat(5);
                    System.out.println("CALCULADO: "+ calculo);
                    obj = new DetalleFormulario(fila.getString(0), fila.getString(1), fila.getString(2), fila.getString(3),fila.getString(4),fila.getString(5),decimales.format(calculo));
                    category.add(obj);
                } while (fila.moveToNext());
            }

        }
        return category;
    }

    /**
     * Metodo que carga los datos si el formulario esta en modo de UPDATE
     * @param id_formulario
     */
    public void cargarDatos(int id_formulario){
        actualizarLista();
        Button btnGuardarCambios = (Button) findViewById(R.id.btn_chinchesalivosa_adddetalle);
        btnGuardarCambios.setText("GUARDAR CAMBIOS");
        Button btnAgregarDetalle = (Button) findViewById(R.id.btn_form_chinchesalivosa_actualizar);
        btnAgregarDetalle.setText("AGREGAR DETALLE");
        MaestroFormulario obj = new MaestroFormulario();
        AdminDataBase admin = new AdminDataBase(this,"registro_muestreo", null, 1);
        SQLiteDatabase bd = admin.getWritableDatabase();
        Cursor fila = bd.rawQuery("select * from form_chinche_salivosa where id_form = "+id_form+"",null);
        Integer numero_datos = fila.getCount();
        if (numero_datos > 0) {
            if (fila.moveToFirst()) {
                do {
                    obj = new MaestroFormulario(fila.getString(0), fila.getString(1), fila.getString(2), fila.getString(3),fila.getString(4),fila.getString(5),fila.getString(6),fila.getString(7),fila.getString(8));
                } while (fila.moveToNext());
            }
        }
        txtNumero_finca.setText(obj.getNo_finca());
        txtNo_ficha.setText(obj.getNo_ficha());
        txtLote.setText(obj.getLote());
        txtArea.setText(obj.getArea());
        txtFecha_muestreo.setText(obj.getF_muestreo());
        txtNumero_muestra.setText(obj.getNo_muestreo());
        txtCargo_resp.setText(obj.getCargo_resp());
        txtNombre_resp.setText(obj.getNombre_resp());
    }
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        //outState.putString("OPERACION", tipoOperacion);
        //outState.putString("PROGRAMA", id_programaGeneral);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        //tipoOperacion = savedInstanceState.getString("OPERACION");
        //id_programaGeneral = savedInstanceState.getString("PROGRAMA");
        //TextView idprograma = (TextView) findViewById(R.id.txt_id_programa);
        //idprograma.setText(id_programaGeneral);
    }

    @Override
    public void pulsado() {
        actualizarLista();
    }

}
