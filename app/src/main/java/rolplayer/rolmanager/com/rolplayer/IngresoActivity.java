package rolplayer.rolmanager.com.rolplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rolplayer.rolmanager.com.rolplayer.beans.CampaignDTO;
import rolplayer.rolmanager.com.rolplayer.beans.JugadorLogueado;
import rolplayer.rolmanager.com.rolplayer.beans.NuevoUsuarioJugador;
import rolplayer.rolmanager.com.rolplayer.beans.PersonajeDTO;
import rolplayer.rolmanager.com.rolplayer.services.RolMediaSingleton;

/**
 * Created by TiranoJuan on 31/10/2017.
 */

public class IngresoActivity extends Activity {

    private static final String ARG_PARAM1 = "name";
    private static final String ARG_PARAM2 = "user";
    private static final String ARG_PARAM3 = "mail";
    private static final String ARG_PARAM4 = "id";
    private static final String ARG_PARAM5 = "url";

    NuevoUsuarioJugador nuj = null;
    JugadorLogueado jugadorLogueado = null;

    private String nombre;
    private String usuario;
    private String email;
    private String idFirebase;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle datos = this.getIntent().getExtras();
        nombre = datos.getString(ARG_PARAM1);
        usuario = datos.getString(ARG_PARAM2);
        email = datos.getString(ARG_PARAM3);
        idFirebase = datos.getString(ARG_PARAM4);
        imageUrl = datos.getString(ARG_PARAM5);

        if (nombre != null
                || usuario != null
                || email != null
                || idFirebase != null
                ) {
            nuj = new NuevoUsuarioJugador();
            nuj.setEmail(email);
            nuj.setName(nombre);
            nuj.setId_firebase(idFirebase);
            nuj.setUrlImage(imageUrl);

            loginJugadorEnCore();
        } else {
            returnLogin();
        }


    }


    private void returnLogin() {
        SharedPreferences prefs =
                getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("logueado", false);
        editor.commit();
        Intent i = new Intent(IngresoActivity.this, LoginActivity.class);
        startActivity(i);
    }

    private void evaluarResponse() {
        if (jugadorLogueado != null) {

            SharedPreferences prefs =
                    getSharedPreferences("usuario", Context.MODE_PRIVATE);


            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("id_jugador", String.valueOf(jugadorLogueado.getIdJugador()));
            editor.putString("nombre_jugador", nombre);
            editor.putString("mail_jugador", email);


            editor.commit();


        }
    }

    private void loginJugadorEnCore() {
        String URL_COMPLEMENTO = "loginjugador";
        // debe recibir json Jugador
        final String tag = "Registro en Core";


        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                getString(R.string.URL_BASE) + URL_COMPLEMENTO,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d(tag, "Respuesta Volley:" + response.toString());
                        jugadorLogueado = parseJson(response);
                        evaluarResponse();
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(IngresoActivity.this, "Guardado Fallo", Toast.LENGTH_SHORT).show();
                        Log.d(tag, "Respuesta FALLO: " + error.toString());
                        returnLogin();
                    }
                }) {
            @Override
            public byte[] getBody() {

                String httpPostBody = nuj.toJson();

                return httpPostBody.getBytes();
            }
        };

        RolMediaSingleton.getInstance(this).getRequestQueue().add(jsArrayRequest);

    }


    public JugadorLogueado parseJson(JSONObject jsonObject) {


        JSONArray jsonArrayCamps = null;
        JSONArray jsonArrayPerso = null;

        JugadorLogueado jl = null;

        try {

            jl = new JugadorLogueado();
            jl.setIdJugador(jsonObject.getLong("idJugador"));

            jsonArrayCamps = jsonObject.getJSONArray("campaigns");
            jsonArrayPerso = jsonObject.getJSONArray("personajes");

            List<CampaignDTO> campaigns = new ArrayList();
            List<PersonajeDTO> pjs = new ArrayList();


            for (int i = 0; i < jsonArrayCamps.length(); i++) {

                CampaignDTO cm = new CampaignDTO();
                JSONObject objeto = jsonArrayCamps.getJSONObject(i);

                cm.setNombre(objeto.getString("nombre"));
                //cm.setAceptada(objeto.getBoolean("aceptada"));
                cm.setNombreMaster(objeto.getString("nombreMaster"));
                campaigns.add(cm);
            }
            for (int i = 0; i < jsonArrayPerso.length(); i++) {

                PersonajeDTO pm = new PersonajeDTO();
                JSONObject objeto = jsonArrayPerso.getJSONObject(i);

                pm.setNombre(objeto.getString("nombre"));
                pm.setId_pj(objeto.getLong("id_campaign"));
                pm.setNivel(objeto.getInt("nivel"));
                pm.setRaza(objeto.getString("raza"));
                pjs.add(pm);
            }

            //jl.setCampaigns(campaigns);
            jl.setPersonajes(pjs);

        } catch (JSONException e) {
            Log.e("arreando el array", "Error de parsing: " + e.getMessage());
        }

        return jl;
    }
}
