package rolplayer.rolmanager.com.rolplayer;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import rolplayer.rolmanager.com.rolplayer.beans.Idioma;
import rolplayer.rolmanager.com.rolplayer.beans.NuevoUsuarioJugador;
import rolplayer.rolmanager.com.rolplayer.services.RolMediaSingleton;

public class PruebasRestFull extends AppCompatActivity {


    String URL_BASE = "http://192.168.0.11:8086/RoleMasterManager/";

    Button put;
    Button get;
    Button post;
    Button delete;
    Button getLista;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pruebas_rest_full);

        get = findViewById(R.id.getprueba);
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                probarGEt();
            }
        });

        put = findViewById(R.id.putprueba);
        put.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                probarPUT();
            }
        });
        post = findViewById(R.id.postprueba);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                probarSaveJugadorCore();
            }
        });

        delete = findViewById(R.id.deleteprueba);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                probarDELETE();
            }
        });

        getLista = findViewById(R.id.deleteprueba);
        getLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                probarList();
            }
        });

    }


    void probarGEt() {


        String URL_COMPLEMENTO = "getjugador/2";


        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL_BASE + URL_COMPLEMENTO,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(PruebasRestFull.this, "RESPONDIO GET!", Toast.LENGTH_SHORT).show();
                        Log.d("RESPUESTA DE REST GET", "Respuesta Volley:" + response.toString());
                        // Manejo de la respuesta
                        // notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PruebasRestFull.this, "FALLO DE REST GET!", Toast.LENGTH_SHORT).show();
                        Log.d("RESPUESTA DE REST GET", "Respuesta FALLO: " + error.toString());

                    }
                });

        RolMediaSingleton.getInstance(this).getRequestQueue().add(jsArrayRequest);
    }

    void probarSaveJugadorCore() {
        String URL_COMPLEMENTO = "newuserjugador";
        // debe recibir json Jugador
        final String tag = "Registro en Core";

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.POST,
                getString(R.string.URL_BASE) + URL_COMPLEMENTO,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(PruebasRestFull.this, "Guardado Correcto", Toast.LENGTH_SHORT).show();
                        Log.d(tag, "Respuesta Volley:" + response.toString());
                        // Manejo de la respuesta
                        // notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PruebasRestFull.this, "Guardado Fallo", Toast.LENGTH_SHORT).show();
                        Log.d(tag, "Respuesta FALLO: " + error.toString());

                    }
                }) {
            @Override
            public byte[] getBody() {
                NuevoUsuarioJugador nuj = new NuevoUsuarioJugador();
                nuj.setEmail("asas");
                nuj.setName("sasafff");
                nuj.setId_firebase("adsdafasdfds");

                String httpPostBody = nuj.toJson();

                return httpPostBody.getBytes();
            }
        };

        RolMediaSingleton.getInstance(this).getRequestQueue().add(jsArrayRequest);

    }



    void probarPUT() {


        String URL_COMPLEMENTO = "putactualizarjugador/2";
        // debe recibir json Jugador

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.PUT,
                URL_BASE + URL_COMPLEMENTO,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(PruebasRestFull.this, "RESPUESTA DE REST PUT!", Toast.LENGTH_SHORT).show();
                        Log.d("RESPUESTA DE REST PUT", "Respuesta Volley:" + response.toString());
                        // Manejo de la respuesta
                        // notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PruebasRestFull.this, "FALLO DE REST PUT!", Toast.LENGTH_SHORT).show();
                        Log.d("RESPUESTA DE REST PUT", "Respuesta FALLO: " + error.toString());

                    }
                }) {
            @Override
            public byte[] getBody() {
                NuevoUsuarioJugador nuj = new NuevoUsuarioJugador();
                nuj.setEmail("asas");
                nuj.setName("sasafff");
                nuj.setId_firebase("adsdafasdfds");

                String httpPostBody = nuj.toJson();

                // usually you'd have a field with some values you'd want to escape, you need to do it yourself if overriding getBody. here's how you do it
//                try {
//                    httpPostBody = httpPostBody + "&randomFieldFilledWithAwkwardCharacters=" + URLEncoder.encode("{{%stuffToBe Escaped/", "UTF-8");
//                } catch (UnsupportedEncodingException exception) {
//                    Log.e("ERROR", "exception", exception);
//                    // return null and don't pass any POST string if you encounter encoding error
//                    return null;
//                }
                return httpPostBody.getBytes();
            }
        };

        RolMediaSingleton.getInstance(this).getRequestQueue().add(jsArrayRequest);
    }

    void probarDELETE() {


        String URL_COMPLEMENTO = "borrarjugador/2";
        // debe recibir String y OK status

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_BASE + URL_COMPLEMENTO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(PruebasRestFull.this, "RESPUESTA DE REST DELETE!", Toast.LENGTH_SHORT).show();
                        Log.d("RESPUESTA REST DELETE", "Respuesta Volley:" + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PruebasRestFull.this, "FALLO DE REST DELETE!", Toast.LENGTH_SHORT).show();
                        Log.d("RESPUESTA REST DELETE", "Respuesta FALLO: " + error.toString());
                    }
                });

        RolMediaSingleton.getInstance(this).getRequestQueue().add(stringRequest);
    }

    void probarList() {


        String URL_COMPLEMENTO = "idiomas";
        // debe recibir String y OK status

        final String TAG = "";


        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL_BASE + URL_COMPLEMENTO,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(PruebasRestFull.this, "RESPUESTA DE REST LISTAS!", Toast.LENGTH_SHORT).show();
                        //Log.d("RESPUESTA REST DELETE", "Respuesta Volley:" + response.toString());

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PruebasRestFull.this, "FALLO DE REST LIST!", Toast.LENGTH_SHORT).show();
                        Log.d("RESPUESTA REST LIST", "Respuesta FALLO: " + error.toString());
                    }
                });

        RolMediaSingleton.getInstance(this).getRequestQueue().add(jsArrayRequest);
    }


    public List<Idioma> parseJson(JSONObject jsonObject) {
        // Variables locales
        List<Idioma> idiomas = new ArrayList();
        JSONArray jsonArray = null;

        try {
            // Obtener el array del objeto
            jsonArray = jsonObject.getJSONArray("items");

            for (int i = 0; i < jsonArray.length(); i++) {

                try {
                    JSONObject objeto = jsonArray.getJSONObject(i);

                    Idioma post = new Idioma();
                    post.setId_idioma(Long.parseLong(objeto.getString("id_idioma")));
                    post.setDescripcion(objeto.getString("descripcion"));
                    post.setNombre(objeto.getString("nombre"));

                    idiomas.add(post);

                } catch (JSONException e) {
                    Log.e("arreando el array", "Error de parsing: " + e.getMessage());
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return idiomas;
    }

}
