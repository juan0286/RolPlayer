package rolplayer.rolmanager.com.rolplayer.beans;

/**
 * Created by TiranoJuan on 26/10/2017.
 */

public class Idioma {


    private long id_idioma;


    private String nombre;


    private String descripcion;

    public Idioma() {
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Idioma other = (Idioma) obj;
        if (this.id_idioma != other.id_idioma) {
            return false;
        }
        return true;
    }

    public long getId_idioma() {
        return id_idioma;
    }

    public void setId_idioma(long id_idioma) {
        this.id_idioma = id_idioma;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


    @Override
    public String toString() {
        return "\nnombre=" + nombre + "\n descripcion=" + descripcion;
    }
}