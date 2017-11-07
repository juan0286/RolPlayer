package rolplayer.rolmanager.com.rolplayer.items;

import android.graphics.drawable.Drawable;

import rolplayer.rolmanager.com.rolplayer.beans.CampaignDTO;

/**
 * Created by TiranoJuan on 31/10/2017.
 */

public class CampaignItem {

    private String nombre;
    private String nombreMaster;
    private Drawable imagen;
    private long id_campaign;

    public CampaignItem(String nombre, String nombreMaster, Drawable imagen) {
        this.nombre = nombre;
        this.nombreMaster = nombreMaster;
        this.imagen = imagen;
    }
    public CampaignItem(CampaignDTO cm){
        this.nombre = cm.getNombre();
        this.nombreMaster = cm.getNombreMaster();
        //this.imagen = (cm.isAceptada())? "" : "";
        this.id_campaign = cm.getId_campaign();
    }

    public CampaignItem() {
        super();
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

    public Drawable getImagen() {
        return imagen;
    }

    public void setImagen(Drawable imagen) {
        this.imagen = imagen;
    }

    public long getId_campaign() {
        return id_campaign;
    }

    public void setId_campaign(long id_campaign) {
        this.id_campaign = id_campaign;
    }
}
