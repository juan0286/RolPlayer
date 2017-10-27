package rolplayer.rolmanager.com.rolplayer.services;

/**
 * Created by TiranoJuan on 25/10/2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;


public final class RolMediaSingleton {
    // Atributos
    private static RolMediaSingleton singleton;
    private RequestQueue requestQueue;
    private static Context context;

    private RolMediaSingleton(Context context) {
        RolMediaSingleton.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized RolMediaSingleton getInstance(Context context) {
        if (singleton == null) {
            singleton = new RolMediaSingleton(context);
        }
        return singleton;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public void addToRequestQueue(Request req) {
        getRequestQueue().add(req);
    }

}