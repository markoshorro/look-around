package gal.udc.evilcorp.lookaround.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import gal.udc.evilcorp.lookaround.R;
import gal.udc.evilcorp.lookaround.model.Event;
import gal.udc.evilcorp.lookaround.util.CustomVolleyRequest;
import gal.udc.evilcorp.lookaround.util.Utils;

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

        final TextView title = (TextView) findViewById(R.id.rl_title);
        TextView place = (TextView) findViewById(R.id.rl_place);
        TextView description = (TextView) findViewById(R.id.rl_description);

        title.setText(event.getPlace());
        place.setText(event.getName());
        description.setText(event.getDescription());

        ImageLoader imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext())
                .getImageLoader();

        NetworkImageView avatar = (NetworkImageView) findViewById(R.id.avatar);
        avatar.setImageUrl(Utils.URL_FB+event.getId()+"/picture", imageLoader);
    }

}
