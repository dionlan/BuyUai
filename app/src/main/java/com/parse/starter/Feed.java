package com.parse.starter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Feed extends AppCompatActivity {

    ListView listView;
    SimpleAdapter simpleAdapter;
    List<Map<String, String>> publicacaoData = new ArrayList<Map<String, String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_publicacoes);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.listaFeeds);
        publicacaoData = new ArrayList<Map<String, String>>();
        simpleAdapter = new SimpleAdapter(this, publicacaoData, android.R.layout.simple_list_item_2, new String[]{"username", "detalheProduto"}, new int[]{android.R.id.text1, android.R.id.text2});

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Publicacao");
        query.whereContainedIn("username", ParseUser.getCurrentUser().getList("seguindo"));
        query.orderByDescending("createdAt");
        query.setLimit(20);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> publicacaoObjects, ParseException e) {

                if (e == null) {
                    if (publicacaoObjects.size() > 0) {
                        for (ParseObject publicacaoObject : publicacaoObjects) {
                            Map<String, String> pub = new HashMap<String, String>(3);
                            pub.put("username", publicacaoObject.getString("username"));
                            pub.put("detalheProduto", publicacaoObject.getString("detalheProduto"));

                            publicacaoData.add(pub);
                        }
                        listView.setAdapter(simpleAdapter);
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.usuarios) {

            Intent i = new Intent(getApplicationContext(), ListaUsuario.class);
            startActivity(i);

        }else if (id == R.id.publicar) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Enviar uma oferta");
            final EditText descricao = new EditText(this);
            descricao.setHint("Descrição");

            final EditText preco = new EditText(this);
            preco.setHint("Preço");
            preco.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);

            LinearLayout lay = new LinearLayout(this);
            lay.setOrientation(LinearLayout.VERTICAL);


            Button buyButton = new Button(this);
            buyButton.setText("Tirar Foto");

            buyButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    //Aqui dentro do OnClick, você faz da mesma forma que você trabalha para abrir uma Activity
                    Intent intent = new Intent();
                    intent.setType("image*//*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Contact Image"), 1);

                }
            });

            lay.addView(descricao);
            lay.addView(preco);
            lay.addView(buyButton);

            builder.setView(lay);

            builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    ParseObject publica = new ParseObject("Publicacao");
                    publica.put("username", ParseUser.getCurrentUser().getUsername());
                    publica.put("descricao", String.valueOf(descricao.getText()));
                    publica.put("preco", String.valueOf(preco.getText()));

                    //detalhe = descrição e preço
                    String detalheProduto = String.valueOf(descricao.getText()) + " - " + String.valueOf(preco.getText());
                    publica.put("detalheProduto", detalheProduto);

                    publica.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {

                                Toast.makeText(getApplicationContext(), "Sua publicação foi enviada.", Toast.LENGTH_LONG).show();
                            } else {
                                Log.i("AppInfo", "ERRO: "+e);
                                Toast.makeText(getApplicationContext(), "Sua publicação não pode ser enviada - por favor tente novamente.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.cancel();
                }
            });
            builder.show();
            return true;
        }else if (id == R.id.logout) {
            ParseUser.getCurrentUser().logOut();
            startActivity(new Intent(Feed.this, DispatchActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean isEmpty(EditText etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }
}