package msig.com.formularios_control.detalles;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import msig.com.formularios_control.R;
import msig.com.formularios_control.interfaces.FormularioListener;
import msig.com.formularios_control.localhost.AdminDataBase;

public class AddDetalleChincheSalivosa extends DialogFragment implements DialogInterface.OnShowListener{
    @Bind(R.id.txtChincheSalivosa_estacion) EditText txtEstacion;
    @Bind(R.id.txtChincheSalivosa_ninfa1) EditText txtNinfa1;
    @Bind(R.id.txtChincheSalivosa_ninfa2) EditText txtNinfa2;
    @Bind(R.id.txtChincheSalivosa_tallos) EditText txtTallos;
    @Bind(R.id.txtChincheSalivosa_vivos) EditText txtVivos;
    @Bind(R.id.idFormularioNuevo) TextView txtvIdFormulario;
    private int id_formulario;
    private String fecha;
    private String no_ficha;
    private int estacion;
    private int vivos;
    private int ninfa1;
    private int ninfa2;
    private int tallos;
    String modo;

    private FormularioListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        id_formulario = getArguments().getInt("id_formulario");
        fecha = getArguments().getString("fecha");
        no_ficha = getArguments().getString("no_ficha");
        estacion = getArguments().getInt("estacion");
        vivos = getArguments().getInt("vivos");
        ninfa1 = getArguments().getInt("ninfa1");
        ninfa2 = getArguments().getInt("ninfa2");
        tallos = getArguments().getInt("tallos");
        System.out.println("FECHA RECIBIDA "+ fecha);
        System.out.println("NO_FICHA RECIBIADA "+ no_ficha);
        System.out.println("ID_FORMULARIO RECIBIDO "+ id_formulario);
        if(estacion == 0){
            modo="insert";
            AlertDialog.Builder builder =  new  AlertDialog.Builder(getActivity())
                    .setTitle(R.string.abc_tituloDetalle)
                    .setPositiveButton(R.string.abc_aceptar,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {}
                            })
                    .setNeutralButton(R.string.abc_cancelar,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {}
                            });

            LayoutInflater i = getActivity().getLayoutInflater();
            View view = i.inflate(R.layout.activity_add_detalle_fragment,null);
            ButterKnife.bind(this,view);
            builder.setView(view);
            AlertDialog dialog = builder.create();
            dialog.setOnShowListener(this);
            txtvIdFormulario.setText("Formulario "+ id_formulario);
            return dialog;
        }else{
            modo="update";
            AlertDialog.Builder builder =  new  AlertDialog.Builder(getActivity())
                    .setTitle(R.string.abc_eliminar)
                    .setPositiveButton(R.string.abc_modificar,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                    .setNeutralButton(R.string.abc_cancelar,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                    .setNegativeButton(R.string.abc_eliminar,
                            new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                    });

            LayoutInflater i = getActivity().getLayoutInflater();
            View view = i.inflate(R.layout.activity_add_detalle_fragment,null);
            ButterKnife.bind(this, view);
            builder.setView(view);
            AlertDialog dialog = builder.create();
            dialog.setOnShowListener(this);
            cargarDatos();
            txtvIdFormulario.setText("Formulario: "+ id_formulario);
            return dialog;
        }
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        final AlertDialog dialog = (AlertDialog) getDialog();
        //idEstiva.setText("Estacion: "+ estiva);
        if (dialog != null) {
            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(Dialog.BUTTON_NEGATIVE);
            Button neutralButton = dialog.getButton(Dialog.BUTTON_NEUTRAL);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(modo.equals("insert")){
                        if(guardar()) {
                            Toast.makeText(getActivity(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                            listener.pulsado();
                            dialog.cancel();
                        }
                    }
                    if(modo.equals("update")){
                        if(modificar()) {
                            Toast.makeText(getActivity(), "Modificado con exito", Toast.LENGTH_SHORT).show();
                            listener.pulsado();
                            dialog.cancel();

                        }
                    }
                }
            });

            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    confirmarEliminar(dialog);
                }
            });

            neutralButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.pulsado();
                    dialog.cancel();

                }
            });
        }

    }

    /**
     * Guarda el detalle en base de datos local
     * @return
     */
    public boolean guardar(){
        if(validar()){
            if(validarEstacion()){
                if(validarTallo()){
                    try {
                        System.out.println("FECHA A INSERTAR "+ fecha);
                        System.out.println("FORMULARIO A INSERTAR "+ id_formulario);
                        System.out.println("NO_FICHA A INSERTAR "+ no_ficha);
                        AdminDataBase admin = new AdminDataBase(getContext(), "registro_muestreo", null, 1);
                        SQLiteDatabase bd = admin.getWritableDatabase();
                        ContentValues registro = new ContentValues();
                        registro.put("estacion", Integer.parseInt(txtEstacion.getText().toString()));
                        registro.put("id_form", id_formulario);
                        registro.put("fecha", fecha);
                        registro.put("no_ficha",no_ficha);
                        registro.put("vivos", txtVivos.getText().toString());
                        registro.put("ninfa1", txtNinfa1.getText().toString());
                        registro.put("ninfa2", txtNinfa2.getText().toString());
                        registro.put("tallos", txtTallos.getText().toString());
                        registro.put("enviado", "NO");
                        bd.insert("detalle_chinche_salivosa", null, registro);
                        bd.close();
                        return true;
                    }catch (Exception e){
                        System.out.println("-------------------> ERROR "+ e);
                        return false;
                    }
                }else{
                    Toast.makeText(getActivity(), "EL VALOR 'TALLO' NO PUEDE SER MENOR A '0'", Toast.LENGTH_SHORT).show();
                    return false;
                }

            }else{
                Toast.makeText(getActivity(), "EL VALOR 'ESTACION' NO PUEDE SER '0'", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(getActivity(), "INGRESE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * Modifica el detalle
     * @return
     */
    public boolean modificar(){
        if(validar()){
            if(validarEstacion()){
                if(validarTallo()){
                    try {
                        AdminDataBase admin = new AdminDataBase(getContext(), "registro_muestreo", null, 1);
                        SQLiteDatabase bd = admin.getWritableDatabase();
                        ContentValues registro = new ContentValues();
                        registro.put("estacion", Integer.parseInt(txtEstacion.getText().toString()));
                        registro.put("fecha",fecha);
                        registro.put("no_ficha",no_ficha);
                        registro.put("vivos", txtVivos.getText().toString());
                        registro.put("ninfa1", txtNinfa1.getText().toString());
                        registro.put("ninfa2", txtNinfa2.getText().toString());
                        registro.put("tallos", txtTallos.getText().toString());

                        String[] args2 = new String[]{String.valueOf(estacion),String.valueOf(id_formulario)};
                        bd.update("detalle_chinche_salivosa",registro,"estacion = ? and id_form = ?", args2);
                        bd.close();
                        return true;
                    }catch (Exception e){
                        System.out.println("--------------> ERROR "+ e);
                        return false;
                    }
                }else{
                    Toast.makeText(getActivity(), "EL VALOR 'TALLO' NO PUEDE SER MENOR A '0'", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }else{
                Toast.makeText(getActivity(), "EL VALOR ESTACION NO PUEDE SER '0'", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(getActivity(), "INGRESE TODOS LOS CAMPOS", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * Muestra los datos en su respectivo campo
     */
    public void cargarDatos(){
        txtEstacion.setText(String.valueOf(estacion));
        txtNinfa1.setText(String.valueOf(ninfa1));
        txtTallos.setText(String.valueOf(tallos));
        txtVivos.setText(String.valueOf(vivos));
        txtNinfa2.setText(String.valueOf(ninfa2));
    }

    /**
     * Valida que los campos no esten vacios
     * @return
     */
    public boolean validar(){
        if(txtEstacion.getText().toString().length() != 0 &&
                txtNinfa2.getText().toString().length() !=0 &&
                txtVivos.getText().toString().length() != 0 &&
                txtTallos.getText().toString().length() !=0 &&
                txtNinfa1.getText().toString().length() != 0){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Valida que la estacion no sea '0'
     * @return
     */
    public boolean validarEstacion(){
        if(!txtEstacion.getText().toString().equals("0")){
            return true;
        }else{
            return false;
        }
    }
    public boolean validarTallo(){
        if(!txtTallos.getText().toString().equals("0")){
            return true;
        }else{
            return false;
        }
    }
    /**
     * Elimina el dato actual en el dialogo
     * @return
     */
    public boolean eliminar(){
        try {
            AdminDataBase admin = new AdminDataBase(getContext(), "registro_muestreo", null, 1);
            SQLiteDatabase bd=admin.getWritableDatabase();
            bd.execSQL("delete from detalle_chinche_salivosa where estacion = "+estacion+" and id_form = "+id_formulario+"");
            bd.close();
            Toast.makeText(getContext(), "ELIMINADO CON EXITO", Toast.LENGTH_SHORT).show();
            return true;
        } catch (Exception e) {
            System.out.println("---------------------------> ERROR "+ e);
            Toast.makeText(getContext(), "ERROR AL ELIMINAR, INTENTE NUEVAMENTE", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * Confirmacion de eliminacion de datos
     */
    public void confirmarEliminar(final AlertDialog dialog){
        android.app.AlertDialog.Builder dlg = new android.app.AlertDialog.Builder(getContext());
        dlg.setTitle("Importante");
        dlg.setMessage("Eliminar este dato?");
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
                if(eliminar()){
                    listener.pulsado();
                    dialog.cancel();
                }
            }
        });
        dlg.show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof FormularioListener) {
            listener = (FormularioListener) activity;
        }
    }

}
