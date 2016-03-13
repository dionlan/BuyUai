package com.parse.starter;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import fragments.FeedFragment;
import fragments.PerfilComercianteFragment;
import fragments.PerfilUsuarioFragment;

public class PrincipalMainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    public TabLayout tabLayout;
    public ViewPager viewPager;
    ViewPagerAdapter adapter = null;
    int[] tabIcons;

    private Calendar calendar = null;
    private int year, month, day;
    TextView dataValidadeTextView;
    StringBuilder dataValidade = null;
    ImageView imagemPublicarView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_tabs);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        dataValidadeTextView = new TextView(this);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        //showDate(year, month + 1, day);

        setupTabIcons();
        /*final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        imagemPublicarView = (ImageView) findViewById(R.id.imagemPublicar);
        imagemPublicarView.setOnClickListener(new View.OnClickListener() {

            //Start new list activity
            public void onClick(View v) {


                builder.setTitle("Enviar uma oferta");
                final EditText descricao = new EditText(getApplication());
                descricao.setHint("Descrição");

                final EditText preco = new EditText(getApplication());
                preco.setHint("Preço");
                preco.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);

                dataValidadeTextView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    @SuppressWarnings("deprecation")
                    public void onClick(View v) {
                        showDialog(999);
                    }
                });

                final LinearLayout lay = new LinearLayout(getApplication());
                lay.setOrientation(LinearLayout.VERTICAL);

                Button buyButton = new Button(getApplication());
                buyButton.setText("Tirar Foto");

                buyButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        //Aqui dentro do OnClick, você faz da mesma forma que você trabalha para abrir uma Activity
                        Intent intent = new Intent();
                        intent.setType("image*//**//*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Seleciona uma imagem..."), 1);
                    }
                });

                lay.addView(descricao);
                lay.addView(preco);
                if(dataValidadeTextView.getParent()!=null) {
                    ((ViewGroup) dataValidadeTextView.getParent()).removeView(dataValidadeTextView);
                }else {
                    lay.addView(dataValidadeTextView);
                }

                lay.addView(buyButton);
                builder.setView(lay);

                builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        ParseObject publica = new ParseObject("Publicacao");
                        publica.put("username", ParseUser.getCurrentUser().getUsername());
                        publica.put("descricao", String.valueOf(descricao.getText()));
                        publica.put("preco", String.valueOf(preco.getText()));
                        publica.put("validadeOferta", String.valueOf(dataValidade));

                        //detalhe = descrição e preço
                        String detalheProduto = String.valueOf(descricao.getText()) + " - " + String.valueOf(preco.getText());
                        publica.put("detalheProduto", detalheProduto);
                        publica.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {


                                if (e == null) {

                                    Toast.makeText(getApplication(), "Sua publicação foi enviada.", Toast.LENGTH_LONG).show();

                                } else {
                                    Log.i("AppInfo", "ERRO: " + e);
                                    Toast.makeText(getApplication(), "Sua publicação não pode ser enviada - por favor tente novamente.", Toast.LENGTH_LONG).show();
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

            }
        });*/


    }

    public void setupTabIcons() {
            tabIcons = new int[]{
                    R.drawable.ic_feed_store,
                    R.drawable.ic_user1
                    //R.drawable.ic_tab_call
            };

            tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            tabLayout.getTabAt(1).setIcon(tabIcons[1]);
            //tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FeedFragment(), "FEED");
        if (ParseUser.getCurrentUser().get("isComercio").equals(false)) {
            adapter.addFrag(new PerfilUsuarioFragment(), "PERFIL DO COMERCIANTE");
        }else{
            adapter.addFrag(new PerfilComercianteFragment(), "PERFIL DO COMERCIANTE");
        }
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {

            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            // return null to display only the icon
            return null;
        }
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (ParseUser.getCurrentUser().get("isComercio").equals(false)) {
            getMenuInflater().inflate(R.menu.menu_lista_feed_pessoa_fisica, menu);

        }else{
            getMenuInflater().inflate(R.menu.menu_lista_feed_pessoa_juridica, menu);
        }

        return true;
    }*/

 /*   @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.publicar) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Enviar uma oferta");
            final EditText descricao = new EditText(this);
            descricao.setHint("Descrição");

            final EditText preco = new EditText(this);
            preco.setHint("Preço");
            preco.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);

            dataValidadeTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                @SuppressWarnings("deprecation")
                public void onClick(View v) {
                    showDialog(999);
                }
            });

            final LinearLayout lay = new LinearLayout(this);
            lay.setOrientation(LinearLayout.VERTICAL);

            Button buyButton = new Button(this);
            buyButton.setText("Tirar Foto");

            buyButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    //Aqui dentro do OnClick, você faz da mesma forma que você trabalha para abrir uma Activity
                    Intent intent = new Intent();
                    intent.setType("image*//**//*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Seleciona uma imagem..."), 1);
                }
            });

            lay.addView(descricao);
            lay.addView(preco);
            lay.addView(dataValidadeTextView);
            lay.addView(buyButton);
            builder.setView(lay);

            builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int which) {

                    ParseObject publica = new ParseObject("Publicacao");
                    publica.put("username", ParseUser.getCurrentUser().getUsername());
                    publica.put("descricao", String.valueOf(descricao.getText()));
                    publica.put("preco", String.valueOf(preco.getText()));
                    publica.put("validadeOferta", String.valueOf(dataValidade));

                    //detalhe = descrição e preço
                    String detalheProduto = String.valueOf(descricao.getText()) + " - " + String.valueOf(preco.getText());
                    publica.put("detalheProduto", detalheProduto);
                    publica.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {

                                Toast.makeText(getApplicationContext(), "Sua publicação foi enviada.", Toast.LENGTH_LONG).show();
                                lay.removeView(dataValidadeTextView);
                            } else {
                                Log.i("AppInfo", "ERRO: " + e);
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
        }
        return super.onOptionsItemSelected(item);
    }*/

/*    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };*/

 /*   private void showDate(int year, int month, int day) {
        dataValidadeTextView.setTextSize(16);
        dataValidade = new StringBuilder().append(day).append("/").append(month+1).append("/").append(year);
        dataValidadeTextView.setText("Data de validade da oferta: " + dataValidade);
        Log.i("AppInfo", "Data Validade: " +dataValidade);

    }*/
}
