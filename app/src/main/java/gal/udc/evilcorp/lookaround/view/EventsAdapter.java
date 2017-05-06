package gal.udc.evilcorp.lookaround.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import gal.udc.evilcorp.lookaround.R;
import gal.udc.evilcorp.lookaround.model.Event;

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

    /**
     * Constructor del Adapter.
     * @param activity          Actividad padre del ListView.
     * @param data              ArrayList con los datos a mostrar.
     */
    public EventsAdapter(Activity activity, List<Event> data) {
        this.data = data;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * Obtiene el número de elementos del ArrayList.
     * @return      Devuelve el número de elementos.
     */
    @Override
    public int getCount() {
        return data.size();
    }

    /**
     * Devuelve el objeto dado una posición del ArrayList.
     * @param position          Posición del objeto.
     * @return                  Devuelve el objeto de esa posición.
     */
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    /**
     * Obtiene el identificador de un objeto en una posición concreta.
     * @param position          Posición del objeto.
     * @return                  Identificador del objeto.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Este método se encarga cargar la vista, procesar los datos y mostrarlos.
     * @param position          Posición del objeto.
     * @param view              Vista afectada.
     * @param viewGroup         Conjunto de vistas.
     * @return                  Devuelve el inflater con la vista y los datos.
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
        return vi;
    }

}
