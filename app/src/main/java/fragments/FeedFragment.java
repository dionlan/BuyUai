package fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.starter.DispatchActivity;
import com.parse.starter.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedFragment extends Fragment {

    ListView listView;
    SimpleAdapter simpleAdapter;
    List<Map<String, String>> publicacaoData = new ArrayList<Map<String, String>>();
    View view = null;
    StringBuilder dataValidade = null;
    private Calendar calendar = null;
    private int year, month, day;
    static TextView dataValidadeTextView;
    ImageView imagemPublicacaoView;
    private FragmentActivity myContext;
    public FeedFragment(){

    }

    @Override
    public void onAttach(Activity activity) {
        myContext=(FragmentActivity) activity;
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_feed, container, false);
        listView = (ListView) view.findViewById(R.id.listaFeeds);
        publicacaoData = new ArrayList<Map<String, String>>();
        simpleAdapter = new SimpleAdapter(getActivity().getApplicationContext(), publicacaoData, android.R.layout.simple_list_item_2, new String[]{"username", "detalheProduto"}, new int[]{android.R.id.text1, android.R.id.text2});
        imagemPublicacaoView = (ImageView) view.findViewById(R.id.imagemPublicar);

        final android.app.DialogFragment dFragment = new DatePickerFragment();
        final FragmentManager fragManager = myContext.getFragmentManager();

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

        dataValidadeTextView = new TextView(getActivity());
        dataValidadeTextView.setTextSize(16);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dataValidade = new StringBuilder().append(day).append("/").append(month+2).append("/").append(year);

        imagemPublicacaoView.setOnClickListener(new View.OnClickListener() {


            @Override
            @SuppressWarnings("deprecation")
            public void onClick(View v) {
                if(dataValidadeTextView.getParent()!=null) {
                    ((ViewGroup) dataValidadeTextView.getParent()).removeView(dataValidadeTextView);
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Enviar uma oferta");
                final EditText descricao = new EditText(getActivity());
                descricao.setHint("Descrição");

                final EditText preco = new EditText(getActivity());
                preco.setHint("Preço");
                preco.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);

                dataValidadeTextView.setText("Data de validade da oferta: " + dataValidade);

                dataValidadeTextView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    @SuppressWarnings("deprecation")
                    public void onClick(View v) {

                        dFragment.show(fragManager, "Date Picker");

                    }
                });

                Log.i("AppInfo", "Data após o CLICK atualizada: " +DatePickerFragment.dataAtualizada);
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

                lay.addView(dataValidadeTextView);

                lay.addView(buyButton);

                builder.setView(lay);
                builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ParseObject publica = new ParseObject("Publicacao");
                        publica.put("username", ParseUser.getCurrentUser().getUsername());
                        publica.put("descricao", String.valueOf(descricao.getText()));
                        publica.put("preco", String.valueOf(preco.getText()));
                        publica.put("validadeOferta", String.valueOf(DatePickerFragment.dataAtualizada));

                        //detalhe = descrição e preço
                        String detalheProduto = String.valueOf(descricao.getText()) + " - " + String.valueOf(preco.getText());
                        publica.put("detalheProduto", detalheProduto);

                        publica.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {

                                    Toast.makeText(getActivity(), "Sua publicação foi enviada.", Toast.LENGTH_LONG).show();

                                } else {
                                    Log.i("AppInfo", "ERRO: " + e);
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
                        //((ViewGroup) dataValidadeTextView.getParent()).removeView(dataValidadeTextView);
                    }
                });

                builder.show();
            }
        });

        return view;
    }

    public static class DatePickerFragment extends android.app.DialogFragment implements DatePickerDialog.OnDateSetListener{

        static StringBuilder dataAtualizada = new StringBuilder();
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dpd = new DatePickerDialog(getActivity(),this,year,month,day);
            return dpd;
        }

        public void onDateSet(DatePicker view, int year, int month, int day){
            // Do something with the chosen date
            // Create a Date variable/object with user chosen date
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(year, month, day, 0, 0, 0);
            Date chosenDate = cal.getTime();

              // Format the date using style and locale
            /*DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
            String formattedDate = df.format(chosenDate);*/

            dataAtualizada = new StringBuilder().append(day).append("/").append(month+1).append("/").append(year);

            // Display the chosen date to app interface
            FeedFragment.dataValidadeTextView.setText("Data de validade da oferta: " +dataAtualizada);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.publicar) {

            /*AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());

            builder.setTitle("Enviar uma oferta");
            final EditText descricao = new EditText(getActivity().getApplicationContext());
            descricao.setHint("Descrição");

            final EditText preco = new EditText(getActivity().getApplicationContext());
            preco.setHint("Preço");
            preco.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);

            LinearLayout lay = new LinearLayout(getActivity().getApplicationContext());
            lay.setOrientation(LinearLayout.VERTICAL);


            Button buyButton = new Button(getActivity().getApplicationContext());
            buyButton.setText("Tirar Foto");

            buyButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    //Aqui dentro do OnClick, você faz da mesma forma que você trabalha para abrir uma Activity
                    Intent intent = new Intent();
                    intent.setType("image*//**//*");
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

                                Toast.makeText(getActivity().getApplicationContext(), "Sua publicação foi enviada.", Toast.LENGTH_LONG).show();
                            } else {
                                Log.i("AppInfo", "ERRO: "+e);
                                Toast.makeText(getActivity().getApplicationContext(), "Sua publicação não pode ser enviada - por favor tente novamente.", Toast.LENGTH_LONG).show();
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
            builder.show();*/
            return true;
        }else if (id == R.id.logout) {
            ParseUser.getCurrentUser().logOut();
            startActivity(new Intent(getActivity().getApplicationContext(), DispatchActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
       /* inflater.inflate(R.menu.menu_usuarios, menu);
        super.onCreateOptionsMenu(menu, inflater);*/
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        selectMenu(menu);
    }

    private void selectMenu(Menu menu) {
        menu.clear();
    }

}
