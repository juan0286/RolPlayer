package rolplayer.rolmanager.com.rolplayer.beans;

/**
 * Created by TiranoJuan on 31/10/2017.
 */

public class PersonajeDTO {

    private long id_pj;

    private String nombre;

    private int nivel;

    private String raza;


    public PersonajeDTO() {
    }

    public long getId_pj() {
        return id_pj;
    }

    public void setId_pj(long id_pj) {
        this.id_pj = id_pj;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }
}
