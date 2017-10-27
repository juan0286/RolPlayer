package rolplayer.rolmanager.com.rolplayer.beans;

import com.google.gson.GsonBuilder;

/**
 * Created by TiranoJuan on 24/10/2017.
 */

public class NuevoUsuarioJugador {

    private String id_firebase;
    private String email;
    private String name;
    private String urlImage;

    public NuevoUsuarioJugador() {
    }

    public String getId_firebase() {
        return id_firebase;
    }

    public void setId_firebase(String id_firebase) {
        this.id_firebase = id_firebase;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    @Override
    public String toString() {
        return "NuevoUsuarioJugador{" + "email=" + email + ", name=" + name + ", urlImage=" + urlImage + '}';
    }


    public String toJson() {
        return new GsonBuilder().create().toJson(this, NuevoUsuarioJugador.class);
    }


}
