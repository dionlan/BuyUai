package com.parse.starter;

import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import fragments.ContatosFragment;
import fragments.FeedFragment;
import fragments.PerfilUsuarioFragment;

public class PrincipalMainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    public ViewPager viewPager;
    public int chamaFragmentUsuarios = 0;
    ViewPagerAdapter adapter = null;
    int[] tabIcons;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon_tabs);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Log.i("AppInfo", "CREATE CHAMADO");
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.

    }

    public void setupTabIcons() {
            tabIcons = new int[]{
                    R.drawable.ic_tab_favourite,
                    R.drawable.ic_tab_contacts
                    //R.drawable.ic_tab_call
            };
            tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            tabLayout.getTabAt(1).setIcon(tabIcons[1]);
            //tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    public void setupTabIcons(int chamaFragmentUsuarios) {

            tabIcons = new int[]{
                    R.drawable.ic_tab_favourite,
                    R.drawable.ic_tab_contacts
            };
            tabLayout.getTabAt(0).setIcon(tabIcons[0]);
            tabLayout.getTabAt(1).setIcon(tabIcons[1]);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFrag(new FeedFragment(), "FEED");

        adapter.addFrag(new PerfilUsuarioFragment(), "PERFIL DO USUÁRIO");

        viewPager.setAdapter(adapter);
    }

    public void setupViewPager(ViewPager viewPager, int usuarioSelecionado) {

        Log.i("AppInfo", "ABA USUARIO SELECIONADA!");
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Log.i("AppInfo", "MONTA FEED!");
        adapter.addFrag(new FeedFragment(), "FEED");



        Log.i("AppInfo", "MONTA CONTATOS!");
        adapter.addFrag(new ContatosFragment(), "CONTATOS");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (ParseUser.getCurrentUser().get("isComercio").equals(false)) {
            getMenuInflater().inflate(R.menu.menu_lista_feed_pessoa_fisica, menu);

        }else{
            getMenuInflater().inflate(R.menu.menu_lista_feed_pessoa_juridica, menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.publicar) {

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
    }
}
