package gal.udc.evilcorp.lookaround.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import gal.udc.evilcorp.lookaround.R;
import gal.udc.evilcorp.lookaround.model.Event;

/**
 * Created by eloy on 06/05/2017.
 */

public class ListItemActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item_activity);

        Intent intent = getIntent();
        Event event = (Event) intent.getSerializableExtra("event_serializable");

        TextView title = (TextView) findViewById(R.id.rl_title);
        TextView place = (TextView) findViewById(R.id.rl_place);
        TextView description = (TextView) findViewById(R.id.rl_description);

        title.setText(event.getName());
        place.setText(event.getPlace());
        description.setText(event.getDescription());
    }

}
