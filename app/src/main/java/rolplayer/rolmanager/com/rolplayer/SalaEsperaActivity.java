package rolplayer.rolmanager.com.rolplayer;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rolplayer.rolmanager.com.rolplayer.beans.CampaignAccessRequestDTO;
import rolplayer.rolmanager.com.rolplayer.beans.CampaignDTO;
import rolplayer.rolmanager.com.rolplayer.beans.JugadorLogueado;
import rolplayer.rolmanager.com.rolplayer.beans.NuevoUsuarioJugador;
import rolplayer.rolmanager.com.rolplayer.beans.PersonajeDTO;
import rolplayer.rolmanager.com.rolplayer.items.AdapterCampaignItem;
import rolplayer.rolmanager.com.rolplayer.items.CampaignItem;
import rolplayer.rolmanager.com.rolplayer.services.RolMediaSingleton;

public class SalaEsperaActivity extends AppCompatActivity {


    private static final String ARG_PARAM1 = "name";
    private static final String ARG_PARAM2 = "user";
    private static final String ARG_PARAM3 = "mail";
    private static final String ARG_PARAM4 = "id";
    private static final String ARG_PARAM5 = "url";

    NuevoUsuarioJugador nuj = null;
    JugadorLogueado jugadorLogueado = null;

    SharedPreferences prefs = null;
    
    private String nombre;
    private String usuario;
    private String email;
    private String idFirebase;
    private String imageUrl;

    private Button btn_SumarCampa;
    private Button btn_act;

    View mProgressView,searchFormView;

    AdapterCampaignItem adapter = null;

    private int itemSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala_espera);

        adapter = new AdapterCampaignItem(this, null);

        prefs = getSharedPreferences("usuario", Context.MODE_PRIVATE);

        searchFormView = findViewById(R.id.layoutSalaEspera);
        mProgressView = findViewById(R.id.loginCore_progress);
        btn_SumarCampa = (Button) findViewById(R.id.btn_SumarCampa);
        btn_SumarCampa.setEnabled(false);

        nombre = prefs.getString(ARG_PARAM1,null);
        usuario = prefs.getString(ARG_PARAM2,null);
        email = prefs.getString(ARG_PARAM3,null);
        idFirebase = prefs.getString(ARG_PARAM4,null);
        imageUrl = prefs.getString(ARG_PARAM5,null);

        long logueadoCore= prefs.getLong("id_j",-1L);

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

        if (logueadoCore == -1L){
                call_loginJugadorEnCore(null);
            } else {

                // EVENTUALMENTE DEBE HABER UNA LLAMADA MÁS BARATA PARA ACTUALIZAR LA SALA DE ESPERA

                call_loginJugadorEnCore(null);
            }
        } else {
            returnLogin();
        }

        ListView lv = (ListView) findViewById(R.id.list_campaigns_espera);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                CampaignItem ci = (CampaignItem) adapter.getItem(pos);
                long id_c = ci.getId_campaign();
                iniciarMain(id_c);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(
                AdapterView<?> arg0, View arg1,int pos, long id) {
                    itemSeleccionado = pos;
                    confirmarBorrado();
                    return true;
            }
        });
    }

    private void confirmarBorrado(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogConfirm dialogo = new DialogConfirm();
        dialogo.show(fragmentManager,"tagConfirmacionBorrarCAR");

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sala_espera_menu, menu);
        ((SalaEsperaActivity) this).getSupportActionBar().setTitle(R.string.waitingRoom);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.se_actualizar:
                call_loginJugadorEnCore(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void buscarCampaigns(View v){

        Intent i = new Intent(SalaEsperaActivity.this,BuscarCampaignsActivity.class);
        Bundle b = new Bundle();
        b.putLong("id_jugador", jugadorLogueado.getIdJugador());
        i.putExtras(b);
        startActivity(i);
    }

    private void iniciarMain(long id_c) {

        Intent intent = new Intent(SalaEsperaActivity.this, MainActivity.class);
        Bundle b = new Bundle();
        b.putLong("id_campaign", id_c);
        startActivity(intent);
    }


    private void cargarDatos(){
        Resources res = getResources();
        ListView lv = (ListView) findViewById(R.id.list_campaigns_espera);
        ArrayList<CampaignItem> icampas = new ArrayList<>();

         // Cargo la lista con las campañas recibidas
        for(CampaignAccessRequestDTO c : jugadorLogueado.getCars()){
            CampaignItem e = new CampaignItem();
            e.setNombre(c.getCampaign().getNombre());
            e.setNombreMaster(c.getCampaign().getNombreMaster());
            e.setId_campaign(c.getCampaign().getId_campaign());

            Drawable drawable = res.getDrawable(R.drawable.reloj_de_arena);
            if (c.getStatus() == CampaignAccessRequestDTO.Status.ACEPTADA.ordinal()){
                drawable = res.getDrawable(R.drawable.checkok);
            } else if (c.getStatus() == CampaignAccessRequestDTO.Status.RECHAZADA.ordinal()){
                drawable = res.getDrawable(R.drawable.checkdenied);
            }
            e.setImagen(drawable);
            icampas.add(e);
        }
        adapter.setItems(icampas);
        lv.setAdapter(adapter);
        TextView nom = (TextView) findViewById(R.id.textView3);
        nom.setText(getString(R.string.welcome) + " " + nombre);

        // guardo el id_j  ESTO ES MUCHO MUY IMPORTANTE, YA QUE ESO EVALUARA SALSA DE ESPERA PARA SABER SI YA ESTA LOGUEADO EL PERSONAJE
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("id_j", jugadorLogueado.getIdJugador());

        editor.commit();        
        btn_SumarCampa.setEnabled(true);
    }

    private void returnLogin() {
        SharedPreferences prefs =
                getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("logueado", false);
        editor.commit();


    }

    private void evaluarResponse() {
        if (jugadorLogueado != null) {

            SharedPreferences prefs = getSharedPreferences("usuario", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("id_jugador", String.valueOf(jugadorLogueado.getIdJugador()));
            editor.putString("nombre_jugador", nombre);
            editor.putString("mail_jugador", email);
            if (jugadorLogueado.getPersonajes().size() == 1) {
                editor.putString("id_pj", String.valueOf(jugadorLogueado.getPersonajes().get(0).getId_pj()));
            }

            editor.commit();
            cargarDatos();
        }
        showProgress(false);
    }

    public void recargar(View v){
        call_loginJugadorEnCore(v);
    }


    private void call_borrarCar() {
        showProgress(true);
        btn_act.setEnabled(false);
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
                        if (error.networkResponse != null) {
                            if (error.getClass().equals(TimeoutError.class)) {
                                // Show timeout error message
                                Toast.makeText(SalaEsperaActivity.this,
                                        "Timeout Error",
                                        Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(SalaEsperaActivity.this, "Ocurrio Fallo Logueando en Core, intenta loguear de nuevo", Toast.LENGTH_LONG).show();
                            Log.d(tag, "Respuesta FALLO: " + error.toString());
                            returnLogin();
                        }
                        showProgress(false);
                        btn_act.setEnabled(true);
                    }
                }) {
            @Override
            public byte[] getBody() {

                String httpPostBody = nuj.toJson();

                return httpPostBody.getBytes();
            }
        };

        jsArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RolMediaSingleton.getInstance(this).getRequestQueue().add(jsArrayRequest);
    }

    public void call_loginJugadorEnCore(View v) {
        showProgress(true);
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
                        if (error.networkResponse != null) {
                            if (error.getClass().equals(TimeoutError.class)) {
                                // Show timeout error message
                                Toast.makeText(SalaEsperaActivity.this,
                                        "Timeout Error",
                                        Toast.LENGTH_LONG).show();
                            }
                        }else {
                            Toast.makeText(SalaEsperaActivity.this, "Ocurrio Fallo Logueando en Core, intenta loguear de nuevo", Toast.LENGTH_LONG).show();
                            Log.d(tag, "Respuesta FALLO: " + error.toString());
                            returnLogin();
                        }
                        showProgress(false);
                        btn_act.setEnabled(true);
                    }
                }) {
            @Override
            public byte[] getBody() {

                String httpPostBody = nuj.toJson();

                return httpPostBody.getBytes();
            }
        };

        jsArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                3000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RolMediaSingleton.getInstance(this).getRequestQueue().add(jsArrayRequest);

    }


    public JugadorLogueado parseJson(JSONObject jsonObject) {

        JSONArray jsonArrayCamps = null;
        JSONArray jsonArrayPerso = null;

        JugadorLogueado jl = null;

        try {

            jl = new JugadorLogueado();
            jl.setIdJugador(jsonObject.getLong("idJugador"));

            jsonArrayCamps = jsonObject.getJSONArray("cars");
            jsonArrayPerso = jsonObject.getJSONArray("personajes");

            List<CampaignAccessRequestDTO> cards = new ArrayList();
            List<PersonajeDTO> pjs = new ArrayList();


            for (int i = 0; i < jsonArrayCamps.length(); i++) {

                CampaignAccessRequestDTO card = new CampaignAccessRequestDTO();
                //jsonArrayCamps = jsonObject.getJSONArray("cars");
                JSONObject objeto = jsonArrayCamps.getJSONObject(i);

                card.setId_car(objeto.getLong("id_car"));
                card.setStatus(objeto.getInt("status"));
                card.setId_jugador(objeto.getLong("id_jugador"));
                Date d = stringToDate(objeto.getString("fecha"), "MMM d yyyy hh:mm:ss aa");
                //card.setFecha(objeto,objeto.get("fecha"));
                cards.add(card);

                CampaignDTO cd = new CampaignDTO();
                JSONObject objetocd = objeto.getJSONObject("cdto");

                cd.setId_campaign(objetocd.getLong("id_campaign"));
                cd.setNombre(objetocd.getString("nombre"));
                cd.setNombreMaster(objetocd.getString("nombreMaster"));
                card.setCampaign(cd);
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

            jl.setCars(cards);
            jl.setPersonajes(pjs);

        } catch (JSONException e) {
            Log.e("arreando el array", "Error de parsing: " + e.getMessage());
        }

        return jl;
    }

    private Date stringToDate(String aDate,String aFormat) {

        if(aDate==null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        Date stringDate = simpledateformat.parse(aDate, pos);
        return stringDate;

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


    public static class DialogConfirm extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getActivity());

            builder.setMessage("¿Confirma la acción seleccionada?")
                    .setTitle("Confirmacion")
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()  {
                        public void onClick(DialogInterface dialog, int id) {
                            //call_borrarCar();
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            return builder.create();
        }

    }

}
