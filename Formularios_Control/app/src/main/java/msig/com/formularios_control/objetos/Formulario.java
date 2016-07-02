package msig.com.formularios_control.objetos;

/**
 * Created by wmazariegos on 08/06/2016.
 */
public class Formulario {
    private String title;
    private String categoryId;
    private String description;
    private int imagen;

    public Formulario() {
        super();
    }

    public Formulario(String categoryId, String title, String description, int imagen) {
        super();
        this.title = title;
        this.description = description;
        this.imagen = imagen;
        this.categoryId = categoryId;
    }


    public String getTitle() {
        return title;
    }

    public void setTittle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getImage() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getCategoryId(){return categoryId;}

    public void setCategoryId(String categoryId){this.categoryId = categoryId;}
}
