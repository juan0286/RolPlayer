package rolplayer.rolmanager.com.rolplayer.beans;

import java.util.List;

/**
 * Created by TiranoJuan on 31/10/2017.
 */

public class JugadorLogueado {

    public JugadorLogueado() {
    }

    private long idJugador;
    List<CampaignAccessRequestDTO> cars;
    List<PersonajeDTO> personajes;


    public JugadorLogueado(long idJugador, List<CampaignAccessRequestDTO> cars, List<PersonajeDTO> personajes) {
        this.idJugador = idJugador;
        this.cars = cars;
        this.personajes = personajes;
    }

    public long getIdJugador() {
        return idJugador;
    }

    public void setIdJugador(long idJugador) {
        this.idJugador = idJugador;
    }

    public List<CampaignAccessRequestDTO> getCars() {
        return cars;
    }

    public void setCars(List<CampaignAccessRequestDTO> cars) {
        this.cars = cars;
    }

    public List<PersonajeDTO> getPersonajes() {
        return personajes;
    }

    public void setPersonajes(List<PersonajeDTO> personajes) {
        this.personajes = personajes;
    }

}
