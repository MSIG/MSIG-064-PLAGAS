package msig.com.formularios_control;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import msig.com.formularios_control.localhost.AdminDataBase;
import msig.com.formularios_control.objetos.DetalleFormulario;
import msig.com.formularios_control.objetos.MaestroFormulario;
import msig.com.formularios_control.services.HttpHandlerFunction;

public class MainActivity extends AppCompatActivity{
    private boolean errorEnUnDato = false;
    private boolean errorEnAlgunEnvio = false;
    private String URL = "http://www.megabyteguatemala.com/Plagas/services/execute.php";
    private String function = "fn_inserta_m_chinche";
    private String functionD = "fn_inserta_d_chinche";

    @Bind(R.id.progressBar) ProgressBar progressBar;
    @Bind(R.id.statusEnviando) TextView statusEnviando;
    @Bind(R.id.btn_indice_eliminarlocal) Button btnEliminarLocal;
    @Bind(R.id.btn_indice_guardarservidor) Button btnGuardarServidor;
    @Bind(R.id.btn_indice_nuevoformulario) Button btnNuevoFormulario;
    @Bind(R.id.btn_indice_verdatos) Button btnVerDatos;
    @Bind(R.id.container) RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

    /**
     * Metodo para guardar en el servidor
     */
    @OnClick(R.id.btn_indice_guardarservidor)
    public void guardarServidor(){
        confirmarEnviar();
    }

    /**
     * Confirma el envio de datos al servidor
     */
    public void confirmarEnviar(){
        //VERIFICAR LA CONEXION A INTERNET
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setTitle("Importante");
        dlg.setMessage("Esta seguro de almacenar esta informacion? Los datos enviados se eliminan del dispositivo");
        dlg.setCancelable(true);
        dlg.setNegativeButton("Cancelar",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dlg, int id) {
                dlg.cancel();
            }
        });

        dlg.setPositiveButton("Confirmar",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dlg, int id) {
                dlg.cancel();
                ejecucion();//Arranca el hilo de ejecución
            }
        });
        dlg.show();
    }

    /**
     * Metodo para llenar formulario
     */
    @OnClick(R.id.btn_indice_nuevoformulario)
    public void verFormularios(){
        Intent verForm;
        verForm = new Intent(this, ListadoFormularios.class);
        verForm.putExtra("operacion","llenar");
        startActivity(verForm);
    }

    /**
     * Metodo para ver formularios
     */
    @OnClick(R.id.btn_indice_verdatos)
    public void verDatos(){
        Intent verForm;
        verForm = new Intent(this, ListadoFormularios.class);
        verForm.putExtra("operacion","ver");
        startActivity(verForm);
    }

    /**
     * Metodo para eliminar datos locales
     */
    @OnClick(R.id.btn_indice_eliminarlocal)
    public void eliminarLocal(){
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setTitle("Importante");
        dlg.setMessage("Eliminar la base de datos local? ");
        dlg.setCancelable(true);
        dlg.setNegativeButton("Cancelar",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dlg, int id) {
                dlg.cancel();
            }
        });

        dlg.setPositiveButton("Confirmar",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dlg, int id) {
                if(eliminarDatosLocal()){
                    Snackbar.make(container, "Datos eliminados con exito", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        dlg.show();
    }

    /**
     * Elimina todos los datos de la base de datos
     * @return
     */
    public boolean eliminarDatosLocal(){
        try {
            AdminDataBase admin = new AdminDataBase(this, "registro_muestreo", null, 1);
            SQLiteDatabase bd=admin.getWritableDatabase();
            int cantidad = bd.delete("detalle_chinche_salivosa", null,null);
            int cantidad2 = bd.delete("form_chinche_salivosa",null,null);
            bd.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    /**
     * Bloque o desbloqueo de botones
     * @param estado
     */
    public void bloqueoBotones(boolean estado){
        btnEliminarLocal.setEnabled(estado);
        btnGuardarServidor.setEnabled(estado);
        btnNuevoFormulario.setEnabled(estado);
        btnVerDatos.setEnabled(estado);
    }

    /**
     * <------------------------------------------------------------------------------------------->
     *     HILO DE ENVIO DE DATOS
     * </------------------------------------------------------------------------------------------->
     * Metodo que envia los datos al servidor
     *
     */
    public void ejecucion(){
        new Thread(new Runnable() {
            public void run() {
                progressBar.post(new Runnable() {
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                        statusEnviando.setVisibility(View.VISIBLE);
                        bloqueoBotones(false);
                    }
                });
                enviarServidorMaestro();
                progressBar.post(new Runnable() {
                    public void run() {
                        progressBar.setVisibility(View.VISIBLE);
                        statusEnviando.setVisibility(View.VISIBLE);
                        bloqueoBotones(false);
                    }
                });

                runOnUiThread(new Runnable() {
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        statusEnviando.setVisibility(View.GONE);
                        bloqueoBotones(true);

                    }
                });
            }
        }).start();
    }

    public void eliminarEnviado(int id_formulario) {
        try {
            AdminDataBase admin = new AdminDataBase(this, "registro_muestreo", null, 1);
            SQLiteDatabase bd=admin.getWritableDatabase();
            bd.execSQL("delete from detalle_chinche_salivosa where id_form = "+id_formulario+"");
            bd.execSQL("delete from form_chinche_salivosa where id_form = "+id_formulario+"");
            bd.close();
            System.out.println("FIN DE TODO EL PROCESO");
        } catch (Exception e) {
            System.out.println("ERROR AL ELIMINAR ENVIADO: "+ e);
        }
    }
    /**
     * Consulta los datos almacenados en el móvil ---------------------------------MAESTRO
     */
    public void enviarServidorMaestro(){
        try {
            Thread.sleep(300);
            System.out.println("ENTRANDO AL PROCESO MAESTRO....");
            AdminDataBase admin = new AdminDataBase(this,"registro_muestreo", null, 1);
            SQLiteDatabase bd = admin.getWritableDatabase();
            Cursor fila = bd.rawQuery("select * from form_chinche_salivosa",null);
            Integer numero_datos = fila.getCount();
            if (numero_datos > 0) {
                if (fila.moveToFirst()) {
                    do {
                        errorEnUnDato = false;
                        MaestroFormulario o;
                        o = new MaestroFormulario(fila.getString(0), fila.getString(1), fila.getString(2), fila.getString(3),fila.getString(4),fila.getString(5),fila.getString(6),fila.getString(7),fila.getString(8));
                        System.out.println("DATOS RECOGIDOS");
                        System.out.println("ID_FORM: " +o.getId_form());
                        System.out.println("NO_FINCA: " + o.getNo_finca());
                        System.out.println("NO_FICHA: " + o.getNo_ficha());
                        System.out.println("LOTE: " + o.getLote());
                        System.out.println("AREA: " + o.getArea());
                        System.out.println("F_MUESTREO: " + o.getF_muestreo());
                        System.out.println("NO_MUESTREO: " + o.getF_muestreo());
                        System.out.println("CARGO_RESP: " + o.getCargo_resp());
                        System.out.println("NOMBRE_RESP: " + o.getNombre_resp());
                        System.out.println("ENVIADO: " + fila.getString(9));
                        enviar(o);
                        if(errorEnUnDato){
                            errorEnAlgunEnvio = true;
                            System.out.println("HA OCURRIDO UN ERROR EN ALGUN DATO DEL DETALLE");
                        }else{
                            eliminarEnviado(Integer.parseInt(o.getId_form()));
                            System.out.println("NO HA EXISTIDO NINGUN ERROR PARA : "+ o.getId_form()+" SE HA ENVIDADO MAESTRO Y DETALLE");
                        }
                    } while (fila.moveToNext());
                    if(errorEnAlgunEnvio){
                        Snackbar.make(container, "Algunos datos no se enviaron, itentente nuevamente", Snackbar.LENGTH_SHORT).show();
                    }else{
                        Snackbar.make(container, "Todos los datos se enviaron exitosamente", Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
            bd.close();
        } catch(InterruptedException e) {}
    }

    /**
     * Envia los datos al servidor -----------------------------------------MAESTRO
     * @param objEnviar
     */
    public void enviar(MaestroFormulario objEnviar) {
        try {
            Thread.sleep(300);
            System.out.println("COMENZANDO ENVIO DE MAESTRO ....");
            String values = "0," + objEnviar.getId_form() + ",'" + objEnviar.getF_muestreo() + "'," + objEnviar.getNo_ficha() + "," + objEnviar.getNo_finca() + "," + objEnviar.getLote() + "," + objEnviar.getArea() + "," + objEnviar.getNo_muestreo() + ",0,'" + objEnviar.getCargo_resp() + "','" + objEnviar.getNombre_resp() + "'";
            try {
                HttpHandlerFunction service = new HttpHandlerFunction();
                String response = service.post(URL, function, values);
                String respuesta_servidor = response.trim();
                System.out.println("RESPUESTA SERVIDOR "+respuesta_servidor);
                if (respuesta_servidor.equals("OK")) {
                    System.out.println(" MAESTRO ENVIADO ");
                    enviarServidorDetalle(objEnviar.getId_form());
                } else {
                    errorEnUnDato = true;
                    System.out.println(" ERROR AL ENVIAR MAESTRO "+ respuesta_servidor);
                }
            } catch (Exception error) {
                System.out.println("ERROR MAESTRO :"+error);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Consulta los datos almacenados en el móvil ---------------------------------DETALLE
     */
    public void enviarServidorDetalle(String id_formulario){
        try {
            Thread.sleep(300);
            AdminDataBase admin = new AdminDataBase(this,"registro_muestreo", null, 1);
            SQLiteDatabase bd = admin.getWritableDatabase();
            Cursor fila = bd.rawQuery("select * from detalle_chinche_salivosa where id_form = "+id_formulario+"",null);
            Integer numero_datos = fila.getCount();
            if (numero_datos > 0) {
                if (fila.moveToFirst()) {
                    do {
                        DetalleFormulario o;
                        o = new DetalleFormulario(fila.getString(0), fila.getString(1), fila.getString(2), fila.getString(3),fila.getString(4),fila.getString(5),fila.getString(6),fila.getString(7),fila.getString(8));
                        enviarDetalle(o);
                    } while (fila.moveToNext());
                }
            }
            bd.close();
        } catch(InterruptedException e) {}
    }

    /**
     * Envia los datos al servidor -----------------------------------------DETALLE
     * @param objEnviar
     */
    public void enviarDetalle(DetalleFormulario objEnviar) {
        try {
            System.out.println("--------------------------> COMENZANDO ENVIO DE DETALLE");
            Thread.sleep(300);
            String values = "0,"+objEnviar.getId_form()+",'"+objEnviar.getFecha()+"',"+objEnviar.getNo_ficha()+","+objEnviar.getEstacion()+","+objEnviar.getVivos()+","+objEnviar.getNinfa1()+","+objEnviar.getNinfa2()+",0,"+objEnviar.getTallos()+",0";
            System.out.println("VALUES: "+ values);
            try {
                HttpHandlerFunction service = new HttpHandlerFunction();
                String response = service.post(URL, functionD, values);
                String respuesta_servidor = response.trim();
                if (respuesta_servidor.equals("OK")) {
                    System.out.println("FIN DE UN CICLO ----- > DETALLE ENVIADO "+ respuesta_servidor);
                } else {
                    errorEnUnDato = true;
                    System.out.println("FIN DE UN CICLO ----- >ERROR AL ENVIAR DETALLE "+ respuesta_servidor);
                }
            } catch (Exception error) {
                System.out.println("-----------------------------> ERROR DETALLE: "+error);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * ----------------------------------------------------------------------MAESTRO
     * Si el dato se envia correctamente se cambia de estatus ENVIADO = 'SI'
     * @param id_form
     */
    public void cambiarStatus(String id_form){
        try {
            AdminDataBase admin = new AdminDataBase(this, "registro_muestreo", null, 1);
            SQLiteDatabase bd = admin.getWritableDatabase();
            ContentValues registro = new ContentValues();
            registro.put("enviado", "SI");
            String[] args2 = new String[]{String.valueOf(id_form)};
            bd.update("form_chinche_salivosa",registro,"id_form = ?", args2);
            bd.close();
        }catch (Exception e){
            System.out.println("ERROR AL CAMBIAR ESTADO MAESTRO DE "+id_form);
        }
    }
    /**
     * ----------------------------------------------------------------------DETALLE
     * Si el dato se envia correctamente se cambia de estatus ENVIADO = 'SI'
     * @param id_form
     */
    public void cambiarStatusDetalle(String id_form){
        try {
            AdminDataBase admin = new AdminDataBase(this, "registro_muestreo", null, 1);
            SQLiteDatabase bd = admin.getWritableDatabase();
            ContentValues registro = new ContentValues();
            registro.put("enviado", "SI");
            String[] args2 = new String[]{String.valueOf(id_form)};
            bd.update("detalle_chinche_salivosa",registro,"id_form = ?", args2);
            bd.close();
        }catch (Exception e){
            System.out.println("ERROR AL CAMBIAR ESTADO DETALLE DE "+id_form);
        }
    }
}