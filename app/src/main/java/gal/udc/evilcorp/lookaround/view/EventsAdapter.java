package gal.udc.evilcorp.lookaround.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.HashMap;
import java.util.List;

import gal.udc.evilcorp.lookaround.R;
import gal.udc.evilcorp.lookaround.model.Event;
import gal.udc.evilcorp.lookaround.util.CustomVolleyRequest;
import gal.udc.evilcorp.lookaround.util.Utils;

/**
 * Created by avelino on 01/05/2017.
 */

public class EventsAdapter extends BaseAdapter {

    /** Constantes para identificar los campos */
    public static final String NAME = "ScoresAdapter.NAME";
    public static final String LOCAL = "ScoresAdapter.LOCAL";

    /* Variables privadas */
    private List<Event> data;
    private LayoutInflater inflater;
    private Activity activity;
    /**
     * Constructor
     * @param activity          listView's parent
     * @param data              data we want to display (arraylist)
     */
    public EventsAdapter(Activity activity, List<Event> data) {
        this.data = data;
        this.activity = activity;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Number of elements in the arrar
     * @return      number of elements
     */
    @Override
    public int getCount() {
        return data.size();
    }

    /**
     * Returns the object given a position
     * @param position          position
     * @return                  object given the position
     */
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    /**
     * Gets the object's ID from its position
     * @param position          object's position
     * @return                  returns ID
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * This method displays the view
     * @param position          object's position
     * @param view              view we want to display
     * @param viewGroup         set of views
     * @return                  returns the view
     */
    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View vi = view;
        if(view == null)
            vi = inflater.inflate(R.layout.layout_records_list, null);

        TextView name = (TextView)vi.findViewById(R.id.rl_name);
        TextView local = (TextView)vi.findViewById(R.id.rl_local);

        Event datos;
        datos = data.get(position);

        name.setText(datos.getName());
        local.setText(datos.getDescription());

        ImageLoader imageLoader = CustomVolleyRequest.getInstance(activity.getApplicationContext())
                .getImageLoader();

        NetworkImageView avatar = (NetworkImageView) vi.findViewById(R.id.result_icon);
        avatar.setImageUrl(Utils.URL_FB + datos.getId() + "/picture", imageLoader);

        return vi;
    }

    /**
     * This method updates the list
     * @param data               data we want to update
     */
    public void update(List<Event> data) {
        this.data = data;
        notifyDataSetChanged();
    }

}
