package rolplayer.rolmanager.com.rolplayer.services;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rolplayer.rolmanager.com.rolplayer.beans.NuevoUsuarioJugador;

/**
 * Created by TiranoJuan on 24/10/2017.
 */

public class NuevoJugadorService {

    private static final String TAG = NuevoJugadorService.class.getName();
    private static final String URL_BASE = "https://jsonplaceholder.typicode.com/";
    private static final String URL_COMPLEMENTO = "posts/1";


    public NuevoJugadorService() {
        NuevoUsuarioJugador nuj = new NuevoUsuarioJugador();
        nuj.setEmail("jg.com");
        nuj.setName("pepe nuevo jugador");
        nuj.setId_firebase("askadksvcjsdjdh");

    }

    public void nuevojugador() {

    }

    public void enviarJsonPut() {

        // Mapeo de los pares clave-valor
        HashMap<String, String> parametros = new HashMap();
        parametros.put("titulo", "Nuevo Post");
        parametros.put("descripcion", "Nuevo Titulo");
        parametros.put("imagen", "/img/nuevo_post.png");

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.PUT,
                URL_BASE + URL_COMPLEMENTO,
                new JSONObject(parametros),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RESPUESTA DE REST PUT", "Respuesta Volley:" + response.toString());
                        // Manejo de la respuesta
                        // notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de errores

                    }
                });

       // RolMediaSingleton.getInstance(this).getRequestQueue().add(a);
    }

    public JsonArrayRequest llamadaporGet() {

        JsonArrayRequest jsArrayRequest = new JsonArrayRequest(
                URL_BASE + URL_COMPLEMENTO,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("RESPUESTA POR GET: ", "Respuesta Volley:" + response.toString());

                        //notifyDataSetChanged();
                    }


                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error:" + error.getMessage());

                    }
                });

        return jsArrayRequest;
    }


    public List<NuevoUsuarioJugador> parseJson(JSONObject jsonObject) {
        // Variables locales
        List<NuevoUsuarioJugador> posts = new ArrayList();
        JSONArray jsonArray = null;

        try {
            // Obtener el array del objeto
            jsonArray = jsonObject.getJSONArray("items");

            for (int i = 0; i < jsonArray.length(); i++) {

                try {
                    JSONObject objeto = jsonArray.getJSONObject(i);

                    NuevoUsuarioJugador post = new NuevoUsuarioJugador(
                    );
                    post.setId_firebase("sasas");
                    post.setName("pepe");
                    post.setEmail("j@g.com");

                    posts.add(post);

                } catch (JSONException e) {
                    Log.e(TAG, "Error de parsing: " + e.getMessage());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return posts;
    }

}
