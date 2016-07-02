package msig.com.formularios_control.localhost;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wmazariegos on 09/06/2016.
 */
public class AdminDataBase extends SQLiteOpenHelper {

    public AdminDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table form_chinche_salivosa (" +
                " id_form integer primary key autoincrement," +
                " no_finca text," +
                " no_ficha text," +
                " lote text," +
                " area text," +
                " f_muestreo text," +
                " no_muestreo text," +
                " cargo_resp text," +
                " nombre_resp text," +
                " enviado text" +
                ")");
        db.execSQL("create table detalle_chinche_salivosa (" +
                " estacion integer,  " +
                " id_form integer, " +
                " fecha text," +
                " no_ficha text," +
                " vivos text, " +
                " ninfa1 text," +
                " ninfa2 text," +
                " tallos text," +
                " enviado text," +
                " FOREIGN KEY(id_form) REFERENCES form_chinche_salivosa(id_form)" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
