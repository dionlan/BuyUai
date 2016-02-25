package fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;
import com.parse.starter.DispatchActivity;
import com.parse.starter.ListaUsuario;
import com.parse.starter.PerfilUsuario;
import com.parse.starter.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedFragment extends Fragment {

    ListView listView;
    SimpleAdapter simpleAdapter;
    List<Map<String, String>> publicacaoData = new ArrayList<Map<String, String>>();
    Toolbar toolbarInferior = null;
    View view = null;
    public FeedFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        view = inflater.inflate(R.layout.fragment_feed, container, false);

        listView = (ListView) view.findViewById(R.id.listaFeeds);
        publicacaoData = new ArrayList<Map<String, String>>();
        simpleAdapter = new SimpleAdapter(getActivity(), publicacaoData, android.R.layout.simple_list_item_2, new String[]{"username", "detalheProduto"}, new int[]{android.R.id.text1, android.R.id.text2});

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

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.publicar) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder.setTitle("Enviar uma oferta");
            final EditText descricao = new EditText(getActivity());
            descricao.setHint("Descrição");

            final EditText preco = new EditText(getActivity());
            preco.setHint("Preço");
            preco.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);

            LinearLayout lay = new LinearLayout(getActivity());
            lay.setOrientation(LinearLayout.VERTICAL);


            Button buyButton = new Button(getActivity());
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

                                Toast.makeText(getActivity(), "Sua publicação foi enviada.", Toast.LENGTH_LONG).show();
                            } else {
                                Log.i("AppInfo", "ERRO: "+e);
                                Toast.makeText(getActivity(), "Sua publicação não pode ser enviada - por favor tente novamente.", Toast.LENGTH_LONG).show();
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
            startActivity(new Intent(getActivity(), DispatchActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
