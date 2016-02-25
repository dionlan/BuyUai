package com.parse.starter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.util.ArrayList;
import java.util.List;

public class ListaUsuario extends AppCompatActivity {

    ArrayList<String> usuarios;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuario);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_cadastro);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(ParseUser.getCurrentUser().get("seguindo") == null) {

            List<String> listaVazia = new ArrayList<String>();
            ParseUser.getCurrentUser().put("seguindo", listaVazia);

        }

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);


        usuarios = new ArrayList<String>();

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked, usuarios);

        final ListView listView = (ListView) findViewById(R.id.listaUsuarios);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(arrayAdapter);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {

                    usuarios.clear();

                    for (ParseUser user : objects) {
                        usuarios.add(user.getUsername());
                    }

                    arrayAdapter.notifyDataSetChanged();

                    for (String username : usuarios) {

                        if (ParseUser.getCurrentUser().getList("seguindo").contains(username)) {

                            listView.setItemChecked(usuarios.indexOf(username), true);
                        }
                    }
                } else {

                    e.printStackTrace();

                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CheckedTextView checkedTextView = (CheckedTextView) view;
                if (checkedTextView.isChecked()) {

                    Log.i("AppInfo", "Linha is checked");

                    ParseUser.getCurrentUser().getList("seguindo").add(usuarios.get(position));
                    ParseUser.getCurrentUser().saveInBackground();

                } else {

                    Log.i("AppInfo", "Linha is not checked");

                    ParseUser.getCurrentUser().getList("seguindo").remove(usuarios.get(position));
                    ParseUser.getCurrentUser().saveInBackground();

                }

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_publicacoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.publicacoes) {

            Intent i = new Intent(getApplicationContext(), Feed.class);
            startActivity(i);
        }
        return true;
    }
    public void clicaTirarFoto(View v){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,0);
    }
}