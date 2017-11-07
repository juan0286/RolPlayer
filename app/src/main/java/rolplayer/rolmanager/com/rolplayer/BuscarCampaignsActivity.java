package rolplayer.rolmanager.com.rolplayer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rolplayer.rolmanager.com.rolplayer.beans.CampaignAccessRequestDTO;
import rolplayer.rolmanager.com.rolplayer.beans.CampaignDTO;
import rolplayer.rolmanager.com.rolplayer.items.AdapterCampaignItem;
import rolplayer.rolmanager.com.rolplayer.items.CampaignItem;
import rolplayer.rolmanager.com.rolplayer.services.GsonRequest;
import rolplayer.rolmanager.com.rolplayer.services.RolMediaSingleton;

public class BuscarCampaignsActivity extends AppCompatActivity {

    private enum TipoBusqueda {MASTER,CAMPAIGN};

    private AdapterCampaignItem adapter = null;

    private List<CampaignDTO> listaCampaigns = null;
    private ListaCampas items = null;
    ListView lv;
    private long id_j;

    View mProgressView,searchFormView;

    TextView txm, txc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_campaigns);
        ((BuscarCampaignsActivity) this).getSupportActionBar().setTitle(R.string.tittle_searchCampaigns);

        adapter = new AdapterCampaignItem(this, new ArrayList<CampaignItem>());

        Bundle datos = this.getIntent().getExtras();
        id_j = datos.getLong("id_jugador");

        txm = (TextView) findViewById(R.id.bc_editText_masterName);
        txc = (TextView) findViewById(R.id.bc_editText_campaignName);
        searchFormView = findViewById(R.id.buscar_form);
        mProgressView = findViewById(R.id.search_progress);

        Button bxc = findViewById(R.id.bc_button_bxc);

        Button bxm = findViewById(R.id.bc_button_bxm);
            bxm.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   String mn = txm.getText().toString();
                    crearBusquedaYejecutar(TipoBusqueda.MASTER,mn);
               }
            }
        );



        bxc.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {
               String cn = txc.getText().toString();
               crearBusquedaYejecutar(TipoBusqueda.CAMPAIGN,cn);
               }
           }
        );

        txc.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    String cn = txc.getText().toString();
                    crearBusquedaYejecutar(TipoBusqueda.CAMPAIGN,cn);
                    return true;
                }
                return false;
            }
        });


        txm.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    String cm = txm.getText().toString();
                    crearBusquedaYejecutar(TipoBusqueda.MASTER,cm);
                    return true;
                }
                return false;
            }
        });

        //adapter = new AdapterCampaignItem(this, null);
        lv = (ListView) findViewById(R.id.bc_list_campaigns);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                CampaignItem ci = (CampaignItem) adapter.getItem(pos);
                long id_c = ci.getId_campaign();
                call_solicitar(id_c);
            }
        });

    }

    private void crearBusquedaYejecutar(TipoBusqueda t, String param){
        txc.setInputType(InputType.TYPE_NULL);
        txm.setInputType(InputType.TYPE_NULL);
        if (param.length() < 4){
            Toast.makeText(BuscarCampaignsActivity.this,
                    getString(R.string.val_minimum4letters),
                    Toast.LENGTH_LONG).show();
        } else {
            HashMap<String, String> parametros = new HashMap();
            String cm = txm.getText().toString();
            parametros.put("id_jugador", String.valueOf(id_j));
            parametros.put("masterName", "");
            parametros.put("campaignName", "");

            if (t == TipoBusqueda.MASTER) {
                parametros.put("masterName", param);
            } else
            if (t == TipoBusqueda.CAMPAIGN) {
                parametros.put("campaignName", param);
            }
            call_buscarCampaignsGson(parametros);
        }
    }


    private void solicitarGson(long id_c) {

        String URL_COMPLEMENTO = "putCar";
        final String tag = "Registro en Core";

        final CampaignAccessRequestDTO card = new CampaignAccessRequestDTO();
        CampaignDTO c = new CampaignDTO();
        c.setId_campaign(id_c);
        card.setCampaign(c);
        card.setId_jugador(id_j);

        String params = "";
GsonRequest request = new GsonRequest<ListaCampas>(
                getString(R.string.URL_BASE) + URL_COMPLEMENTO + params,
                Request.Method.PUT,
                ListaCampas.class,null,
                new Response.Listener<ListaCampas>() {
                    @Override
                    public void onResponse(ListaCampas response) {
                        items = response;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(tag, "Error Volley:" + error.getMessage());
                    }
                }
        ){
            @Override
            public byte[] getBody() {
                System.out.println(card.toJson());
                return card.toJson().getBytes();
            }
        };

request.setRetryPolicy(new DefaultRetryPolicy(
        3000,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RolMediaSingleton.getInstance(this).addToRequestQueue(request);
    }

    private void call_buscarCampaignsGson(HashMap<String, String> parametros) {
        adapter.clear();
        lv.setAdapter(adapter);
        showProgress(true);
//        String URL_COMPLEMENTO = "buscarCampaigns";
        String URL_COMPLEMENTO = "campaignsForPlayer";
        final String tag = "Registro en Core";
        String params = "";
        boolean flag = true;
        for (Map.Entry<String, String> entry : parametros.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (flag) {
                params += "?" + key + "=" + value;
                flag = false;
            } else {
                params += "&" + key + "=" + value;
            }

        }
        RolMediaSingleton.getInstance(this).addToRequestQueue(
                new GsonRequest<ListaCampas>(
                        getString(R.string.URL_BASE) + URL_COMPLEMENTO + params,
                        Request.Method.GET,
                        ListaCampas.class,
                        null,
                        new Response.Listener<ListaCampas>() {
                            @Override
                            public void onResponse(ListaCampas response) {
                                items = response;
                                actualizarLista(items);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if (error.networkResponse == null) {
                                    if (error.getClass().equals(TimeoutError.class)) {
                                        showProgress(false);
                                        // Show timeout error message
                                        Toast.makeText(BuscarCampaignsActivity.this,
                                                "Timeout Error",
                                                Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    if (error != null) {
                                        showProgress(false);
                                        Log.d(tag, "Error Volley:" + error.getMessage().toString());
                                    }
                                }
                            }
                        }
                )

        );
    }

//    private void buscar(HashMap<String, String> parametros) {
//
//        String URL_COMPLEMENTO = "buscarCampaigns";
//        // debe recibir json Jugador
//        final String tag = "Registro en Core";
//
//        String  params = "";
//        boolean flag = true;
//        for(Map.Entry<String, String> entry : parametros.entrySet()) {
//            String key = entry.getKey();
//            String value = entry.getValue();
//            if (flag){
//                params+= "?" + key + "=" + value;
//                flag = false;
//            } else {
//                params+= "&" + key + "=" + value;
//            }
//
//        }
//
//        JsonArrayRequest jsArrayRequest = new JsonArrayRequest(
//                Request.Method.GET,
//                getString(R.string.URL_BASE) + URL_COMPLEMENTO + params,
//                new Response.Listener<JSONArray>() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        listaCampaigns = parseJson(response);
//                        if (listaCampaigns != null) {
//                            actualizarLista();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(BuscarCampaignsActivity.this, "Fallo Trayendo Campañas", Toast.LENGTH_SHORT).show();
//                        Log.d(tag, "Respuesta FALLO: " + error.toString());
//
//                    }
//                });
//
//
//        RolMediaSingleton.getInstance(this).getRequestQueue().add(jsArrayRequest);
//    }

    private int mStatusCode = 0;
    private void call_solicitar(long id_c) {
        showProgress(true);
        String URL_COMPLEMENTO = "putCar";
        // debe recibir json Jugador
        final String tag = "Nueva CampaignAccessRequest";
        final CampaignAccessRequestDTO card = new CampaignAccessRequestDTO();
        CampaignDTO c = new CampaignDTO();
        c.setId_campaign(id_c);
        card.setCampaign(c);
        card.setId_jugador(id_j);

        JsonObjectRequest jsRequest = new JsonObjectRequest(
                Request.Method.PUT,
                getString(R.string.URL_BASE) + URL_COMPLEMENTO,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //showProgress(false);
                        Log.d(tag, "Resp. Add Campaign: " + response.toString());
                        try {
                            JSONObject r = response.getJSONObject("map");
                            if (r.getBoolean("resultado")){
                                Toast.makeText(BuscarCampaignsActivity.this,"Solicitud Enviada",Toast.LENGTH_LONG).show();
                                volverASalaDeEspera();
                            } else {
                                Toast.makeText(BuscarCampaignsActivity.this,"No se pudo enviar la solicitud",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            showProgress(false);
                            Toast.makeText(BuscarCampaignsActivity.this,"Error interpretando Response",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showProgress(false);
                        if (error.networkResponse == null) {
                            if (error.getClass().equals(TimeoutError.class)) {
                                // Show timeout error message
                                Toast.makeText(BuscarCampaignsActivity.this,
                                        "Timeout Error",
                                        Toast.LENGTH_LONG).show();
                            }
                        }else {
                            if (error.networkResponse.statusCode == HttpURLConnection.HTTP_NOT_FOUND){
                                Toast.makeText(BuscarCampaignsActivity.this, "Servidor se encuentra caido", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(BuscarCampaignsActivity.this, "Ocurrio Fallo Logueando en Core, intenta loguear de nuevo", Toast.LENGTH_LONG).show();
                            }
                            Log.d(tag, "Respuesta FALLO: " + error.toString());
                        }
                    }
                }) {
                    @Override
                    public byte[] getBody() {
                        String httpPostBody = card.toJson();
                        return httpPostBody.getBytes();
                    }
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                if (response != null) {
                    mStatusCode = response.statusCode;
                }
                return super.parseNetworkResponse(response);
            }
        };
        jsRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RolMediaSingleton.getInstance(this).getRequestQueue().add(jsRequest);
    }

    private void volverASalaDeEspera() {
        Intent i = new Intent(BuscarCampaignsActivity.this,SalaEsperaActivity.class);
        startActivity(i);

    }

    private void actualizarLista(ListaCampas items) {

        ListView lv = (ListView) findViewById(R.id.bc_list_campaigns);
        ArrayList<CampaignItem> icampas = new ArrayList<>();

        // Cargo la lista con las campañas recibidas
        for (CampaignDTO c : items.getItems()) {
            CampaignItem e = new CampaignItem();
            e.setNombre(c.getNombre());
            e.setNombreMaster(c.getNombreMaster());
            e.setId_campaign(c.getId_campaign());
            icampas.add(e);
        }
        adapter.setItems(icampas);
        lv.setAdapter(adapter);
        showProgress(false);
    }

    public List<CampaignDTO> parseJson(JSONArray jsonArray) {
        List<CampaignDTO> cdtos = new ArrayList();
        try {

            for (int i = 0; i < jsonArray.length(); i++) {

                CampaignDTO cd = new CampaignDTO();

                JSONObject objetocd = jsonArray.getJSONObject(i);

                cd.setId_campaign(objetocd.getLong("id_campaign"));
                cd.setNombre(objetocd.getString("nombre"));
                cd.setNombreMaster(objetocd.getString("nombreMaster"));


                cdtos.add(cd);
            }
            if (cdtos.size() == 0){
                Toast.makeText(BuscarCampaignsActivity.this,
                        getString(R.string.bc_notfound),
                        Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            Log.e("arreando el array", "Error de parsing: " + e.getMessage());
        }

        return cdtos;
    }



    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            searchFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            searchFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    searchFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            searchFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class ListaCampas {
        // Encapsulamiento de Posts
        private List<CampaignDTO> campaigns;

        public ListaCampas(List<CampaignDTO> items) {
            this.campaigns = items;
        }

        public void setItems(List<CampaignDTO> items) {
            this.campaigns = items;
        }

        public List<CampaignDTO> getItems() {
            return campaigns;
        }
    }
}
