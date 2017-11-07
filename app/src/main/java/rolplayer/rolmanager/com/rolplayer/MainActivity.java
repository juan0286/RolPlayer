package rolplayer.rolmanager.com.rolplayer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import rolplayer.rolmanager.com.rolplayer.fragments.EstadoPjFragment;
import rolplayer.rolmanager.com.rolplayer.fragments.ExperienciaPjFragment;
import rolplayer.rolmanager.com.rolplayer.fragments.GlosarioFragment;
import rolplayer.rolmanager.com.rolplayer.fragments.HistoriaFragment;
import rolplayer.rolmanager.com.rolplayer.fragments.HojaPjFragment;
import rolplayer.rolmanager.com.rolplayer.fragments.ObjetosPjFragment;
import rolplayer.rolmanager.com.rolplayer.fragments.PersonajesFragment;
import rolplayer.rolmanager.com.rolplayer.fragments.SortilegiosPjFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // hacer llamado para traer info de campaign
        Bundle datos = this.getIntent().getExtras();
        long id_c = datos.getLong("id_campaign");
        callCampaignInfo(id_c);


    }


    private void callCampaignInfo(long id_c){

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fm = getSupportFragmentManager();


        if (id == R.id.glosario) {
            fm.beginTransaction().replace(R.id.content_frame, new GlosarioFragment()).commit();
        } else if (id == R.id.historia) {
            fm.beginTransaction().replace(R.id.content_frame, new HistoriaFragment()).commit();
        } else if (id == R.id.personajes) {
            fm.beginTransaction().replace(R.id.content_frame, new PersonajesFragment()).commit();

        } else if (id == R.id.estado) {
            fm.beginTransaction().replace(R.id.content_frame, new EstadoPjFragment()).commit();
        } else if (id == R.id.sortilegios) {
            fm.beginTransaction().replace(R.id.content_frame, new SortilegiosPjFragment()).commit();
        } else if (id == R.id.cajaExp) {
            fm.beginTransaction().replace(R.id.content_frame, new ExperienciaPjFragment()).commit();
        } else if (id == R.id.mochila) {
            fm.beginTransaction().replace(R.id.content_frame, new ObjetosPjFragment()).commit();
        } else if (id == R.id.hoja) {
            fm.beginTransaction().replace(R.id.content_frame, new HojaPjFragment()).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
