package com.parse.starter;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;

/**
 * Created by dionlan on 07/03/2016.
 */
public class DetailsActivity extends AppCompatActivity {

    boolean like = false;
    public DetailsActivity(){
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        ImageView imagemVoltarView = (ImageView) findViewById(R.id.imagemVoltar);
        imagemVoltarView.setOnClickListener(new View.OnClickListener() {

            @Override
            @SuppressWarnings("deprecation")
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final ImageView imagemLikeView = (ImageView) findViewById(R.id.imagemLike);
        imagemLikeView.setOnClickListener(new View.OnClickListener() {

            @Override
            @SuppressWarnings("deprecation")
            public void onClick(View v) {
                imagemLikeView.setImageResource(R.drawable.ic_yes_like);
                like = true;
                if(like){
                    imagemLikeView.setImageResource(R.drawable.ic_no_like);
                    like = false;
                }
            }
        });

      /*  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_detalhe_oferta);


        if (toolbar != null) {
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.ic_action_back_teste);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }*/
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getParentActivityIntent();
        Log.i("AppInfo", "Activity com os detalhes da Imag! ");

        String title = getIntent().getStringExtra("title");
        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(title);

        Bundle extras = getIntent().getExtras();
        byte[] b = extras.getByteArray("image");
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        ImageView imageView = (ImageView) findViewById(R.id.image);
        imageView.setImageBitmap(bmp);

        //Bitmap bitmap = getIntent().getParcelableExtra("image");
        Log.i("AppInfo", "BITMAP " + bmp);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main_back, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.back:
                super.onBackPressed();
                return true;
            case R.id.logout:

                return true;*/
        }
        return super.onOptionsItemSelected(item);
    }
}