package rolplayer.rolmanager.com.rolplayer;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import rolplayer.rolmanager.com.rolplayer.beans.NuevoUsuarioJugador;
import rolplayer.rolmanager.com.rolplayer.services.RolMediaSingleton;

public class SignupActivity extends AppCompatActivity  implements
        GoogleApiClient.OnConnectionFailedListener,
        LoaderManager.LoaderCallbacks<Cursor>{

    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = SignupActivity.class.getName();

    private static final String ARG_PARAM1 = "name";
    private static final String ARG_PARAM2 = "user";
    private static final String ARG_PARAM3 = "mail";
    private static final String ARG_PARAM4 = "id";
    private static final String ARG_PARAM5 = "url";


    private static final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();


        if (firebaseUser != null) {
            updateUI();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);



        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, ResetPasswordActivity.class));
            }
        });

        btnSignIn.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.



                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    updateUI();
                                    finish();
                                }
                            }
                        });

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleRegisterInResult(result);
        }
    }

    private void handleRegisterInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            //Usuario logueado --> Mostramos sus datos
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d(TAG, "Obtuvo credencial Google :success Id: " + acct.getId());

            // Authenticar en firebase con la credencial de google
            firebaseAuthWithGoogle(acct);
            // linkear la cuenta para entrar con cualquier proveedor
            if (authGooFire) {
                AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
                linkearCuenta(credential);
            }

            //updateUI();
        } else {
            //Usuario no logueado --> Lo mostramos como "Desconectado"
            Log.d(TAG, "signInWithCredential Google :fail Error Code" + result.getStatus().toString());
            Toast.makeText(getApplicationContext(), result.getStatus().getStatus().toString(), Toast.LENGTH_LONG).show();

            //updateUI(user);
        }
    }


    private boolean linkAccounts = false;
    private boolean authGooFire = false;
    private boolean saveInCore = false;

    private void succLinkAccounts(){
        linkAccounts = true;
    }
    private void successAuthGoogleFirebase(){
        authGooFire = true;
    }
    private void succSaveCore(){
        authGooFire = true;
    }


    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        final boolean succ = false;
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        final AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            firebaseUser = firebaseAuth.getCurrentUser();
                            successAuthGoogleFirebase();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }

    private void linkearCuenta(final AuthCredential credential) {
        firebaseAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "linkWithCredential:success");
                            firebaseUser = task.getResult().getUser();
                            succLinkAccounts();
                        } else {
                            Log.w(TAG, "linkWithCredential:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }




    private void updateUI(){

        if (firebaseUser != null){
        // Guardar el nuevo usuario en la base de datos del rol

            NuevoUsuarioJugador nuj = new NuevoUsuarioJugador();
            nuj.setEmail(firebaseUser.getEmail());
            nuj.setName(firebaseUser.getDisplayName());
            nuj.setId_firebase(firebaseUser.getProviderId());
            nuj.setUrlImage(firebaseUser.getPhotoUrl().toString());

            // enviar a la base

            Intent intent = new Intent(SignupActivity.this, SalaEsperaActivity.class);
            intent.putExtra(ARG_PARAM1, firebaseUser.getDisplayName());
            intent.putExtra(ARG_PARAM2, firebaseUser.getEmail());
            intent.putExtra(ARG_PARAM3, firebaseUser.getEmail());
            intent.putExtra(ARG_PARAM4, firebaseUser.getUid());
            intent.putExtra(ARG_PARAM5, firebaseUser.getPhotoUrl().toString());

            startActivity(intent);
            finish();
        }

    }

    public void saveJugadorInCore() {

        String URL_COMPLEMENTO = "newuserjugador";
        // debe recibir json Jugador
        final String tag = "Registro en Core";

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest(
                Request.Method.PUT,
                getString(R.string.URL_BASE) + URL_COMPLEMENTO,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(SignupActivity.this, tag, Toast.LENGTH_SHORT).show();
                        Log.d(tag, "Respuesta Volley:" + response.toString());
                        // Manejo de la respuesta
                        // notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignupActivity.this, tag, Toast.LENGTH_SHORT).show();
                        Log.d(tag, "Respuesta FALLO: " + error.toString());

                    }
                }) {
            @Override
            public byte[] getBody() {
                String httpPostBody = "grant_type=password&username=Alice&password=password123";
                // usually you'd have a field with some values you'd want to escape, you need to do it yourself if overriding getBody. here's how you do it
                try {
                    httpPostBody = httpPostBody + "&randomFieldFilledWithAwkwardCharacters=" + URLEncoder.encode("{{%stuffToBe Escaped/", "UTF-8");
                } catch (UnsupportedEncodingException exception) {
                    Log.e("ERROR", "exception", exception);
                    // return null and don't pass any POST string if you encounter encoding error
                    return null;
                }
                return httpPostBody.getBytes();
            }
        };

        RolMediaSingleton.getInstance(this).getRequestQueue().add(jsArrayRequest);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
