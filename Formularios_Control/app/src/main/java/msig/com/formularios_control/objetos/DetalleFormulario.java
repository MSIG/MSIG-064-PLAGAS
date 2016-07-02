package msig.com.formularios_control.objetos;

/**
 * Created by wmazariegos on 09/06/2016.
 */
public class DetalleFormulario {
    private String estacion;
    private String id_form;
    private String fecha;
    private String no_ficha;
    private String vivos;
    private String ninfa1;
    private String ninfa2;
    private String tallos;
    private String calculo;

    public DetalleFormulario() {
    }

    public DetalleFormulario(String estacion, String id_form, String fecha, String no_ficha, String vivos, String ninfa1, String ninfa2, String tallos, String calculo) {
        this.estacion = estacion;
        this.id_form = id_form;
        this.fecha = fecha;
        this.no_ficha = no_ficha;
        this.vivos = vivos;
        this.ninfa1 = ninfa1;
        this.ninfa2 = ninfa2;
        this.tallos = tallos;
        this.calculo = calculo;
    }

    public DetalleFormulario(String estacion, String id_form, String fecha, String no_ficha, String vivos, String ninfa1, String ninfa2, String tallos) {
        this.estacion = estacion;
        this.id_form = id_form;
        this.fecha = fecha;
        this.no_ficha = no_ficha;
        this.vivos = vivos;
        this.ninfa1 = ninfa1;
        this.ninfa2 = ninfa2;
        this.tallos = tallos;
    }

    public DetalleFormulario(String estacion, String id_form, String vivos, String ninfa1, String ninfa2, String tallos, String calculo) {
        this.estacion = estacion;
        this.id_form = id_form;
        this.vivos = vivos;
        this.ninfa1 = ninfa1;
        this.ninfa2 = ninfa2;
        this.tallos = tallos;
        this.calculo = calculo;
    }

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public String getId_form() {
        return id_form;
    }

    public void setId_form(String id_form) {
        this.id_form = id_form;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNo_ficha() {
        return no_ficha;
    }

    public void setNo_ficha(String no_ficha) {
        this.no_ficha = no_ficha;
    }

    public String getVivos() {
        return vivos;
    }

    public void setVivos(String vivos) {
        this.vivos = vivos;
    }

    public String getNinfa1() {
        return ninfa1;
    }

    public void setNinfa1(String ninfa1) {
        this.ninfa1 = ninfa1;
    }

    public String getNinfa2() {
        return ninfa2;
    }

    public void setNinfa2(String ninfa2) {
        this.ninfa2 = ninfa2;
    }

    public String getTallos() {
        return tallos;
    }

    public void setTallos(String tallos) {
        this.tallos = tallos;
    }

    public String getCalculo() {
        return calculo;
    }

    public void setCalculo(String calculo) {
        this.calculo = calculo;
    }
}
