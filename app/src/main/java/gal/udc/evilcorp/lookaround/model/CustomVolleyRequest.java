package gal.udc.evilcorp.lookaround.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by eloy on 06/05/2017.
 */

public class CustomVolleyRequest {

    private static CustomVolleyRequest instance;
    private RequestQueue requestQueue;
    private static ImageLoader imageLoader;

    private CustomVolleyRequest(Context context) {
        requestQueue = Volley.newRequestQueue(context);

        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(
                            20);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static CustomVolleyRequest getInstance(Context context) {
        if (instance == null) {
            instance = new CustomVolleyRequest(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public static ImageLoader getImageLoader() {
        return imageLoader;
    }

}
