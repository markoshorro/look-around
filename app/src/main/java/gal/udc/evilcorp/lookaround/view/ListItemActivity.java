package gal.udc.evilcorp.lookaround.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import gal.udc.evilcorp.lookaround.R;
import gal.udc.evilcorp.lookaround.model.Event;
import gal.udc.evilcorp.lookaround.model.CustomVolleyRequest;
import gal.udc.evilcorp.lookaround.util.Utils;

/**
 * Created by eloy on 06/05/2017.
 */

public class ListItemActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_item);

        Intent intent = getIntent();
        Event event = (Event) intent.getSerializableExtra("event_serializable");

        // toolbar
        setupActionBar();

        final TextView title = (TextView) findViewById(R.id.rl_title);
        TextView place = (TextView) findViewById(R.id.rl_place);
        TextView description = (TextView) findViewById(R.id.rl_description);

        title.setText(event.getPlace());
        place.setText(event.getName());
        description.setText(event.getDescription());

        ImageLoader imageLoader = CustomVolleyRequest.getInstance(
                this.getApplicationContext()).getImageLoader();

        NetworkImageView avatar = (NetworkImageView) findViewById(R.id.avatar);
        avatar.setImageUrl(Utils.URL_FB + event.getId() + "/picture", imageLoader);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.title_event));
        }
    }

}
