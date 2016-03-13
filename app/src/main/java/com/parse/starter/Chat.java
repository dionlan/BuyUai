package com.parse.starter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class Chat extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_configuracao_perfil_usuario);

        ImageView imagemVoltarView = (ImageView) findViewById(R.id.imagemVoltar);
        imagemVoltarView.setOnClickListener(new View.OnClickListener() {

            @Override
            @SuppressWarnings("deprecation")
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}