package rolplayer.rolmanager.com.rolplayer.beans;

/**
 * Created by TiranoJuan on 31/10/2017.
 */

public class CampaignDTO {

    private long id_campaign;

    private String nombre;

    private String nombreMaster;


    public CampaignDTO() {
    }

    public long getId_campaign() {
        return id_campaign;
    }

    public void setId_campaign(long id_campaign) {
        this.id_campaign = id_campaign;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNombreMaster() {
        return nombreMaster;
    }

    public void setNombreMaster(String nombreMaster) {
        this.nombreMaster = nombreMaster;
    }

}
