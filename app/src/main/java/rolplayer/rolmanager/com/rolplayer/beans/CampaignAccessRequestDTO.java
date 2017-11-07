package rolplayer.rolmanager.com.rolplayer.beans;

import com.google.gson.GsonBuilder;

import java.util.Date;

/**
 * Created by TiranoJuan on 03/11/2017.
 */

public class CampaignAccessRequestDTO {
    public enum Status {ESPERA, ACEPTADA, RECHAZADA}


    private long id_car;

    private Date fecha;

    private CampaignDTO cdto;

    private long id_jugador;

    private int status;

    public CampaignAccessRequestDTO() {
    }

    public long getId_car() {
        return id_car;
    }

    public void setId_car(long id_car) {
        this.id_car = id_car;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public CampaignDTO getCampaign() {
        return cdto;
    }

    public void setCampaign(CampaignDTO campaign) {
        this.cdto = campaign;
    }

    public long getId_jugador() {
        return id_jugador;
    }

    public void setId_jugador(long id_jugador) {
        this.id_jugador = id_jugador;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public String toJson() {
        return new GsonBuilder().create().toJson(this, CampaignAccessRequestDTO.class);
    }
}
