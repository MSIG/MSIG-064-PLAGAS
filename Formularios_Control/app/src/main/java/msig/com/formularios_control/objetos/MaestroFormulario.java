package msig.com.formularios_control.objetos;

/**
 * Created by wmazariegos on 10/06/2016.
 */
public class MaestroFormulario {
    private String id_form;
    private String no_finca;
    private String no_ficha;
    private String lote;
    private String area;
    private String f_muestreo;
    private String no_muestreo;
    private String cargo_resp;
    private String nombre_resp;

    public MaestroFormulario() {
    }

    public MaestroFormulario(String id_form, String no_finca, String no_ficha, String lote, String area, String f_muestreo, String no_muestreo, String cargo_resp, String nombre_resp) {
        this.id_form = id_form;
        this.no_finca = no_finca;
        this.no_ficha = no_ficha;
        this.lote = lote;
        this.area = area;
        this.f_muestreo = f_muestreo;
        this.no_muestreo = no_muestreo;
        this.cargo_resp = cargo_resp;
        this.nombre_resp = nombre_resp;
    }

    public String getId_form() {
        return id_form;
    }

    public void setId_form(String id_form) {
        this.id_form = id_form;
    }

    public String getNo_finca() {
        return no_finca;
    }

    public void setNo_finca(String no_finca) {
        this.no_finca = no_finca;
    }

    public String getNo_ficha() {
        return no_ficha;
    }

    public void setNo_ficha(String no_ficha) {
        this.no_ficha = no_ficha;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getF_muestreo() {
        return f_muestreo;
    }

    public void setF_muestreo(String f_muestreo) {
        this.f_muestreo = f_muestreo;
    }

    public String getNo_muestreo() {
        return no_muestreo;
    }

    public void setNo_muestreo(String no_muestreo) {
        this.no_muestreo = no_muestreo;
    }

    public String getCargo_resp() {
        return cargo_resp;
    }

    public void setCargo_resp(String cargo_resp) {
        this.cargo_resp = cargo_resp;
    }

    public String getNombre_resp() {
        return nombre_resp;
    }

    public void setNombre_resp(String nombre_resp) {
        this.nombre_resp = nombre_resp;
    }
}
