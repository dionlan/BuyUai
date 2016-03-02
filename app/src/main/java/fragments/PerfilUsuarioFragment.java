package fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.starter.DispatchActivity;
import com.parse.starter.ListaUsuario;
import com.parse.starter.R;

import java.util.List;

public class PerfilUsuarioFragment extends Fragment {

    ViewPager viewPager = null;
    View view = null;

    public PerfilUsuarioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_perfil_usuario, container, false);
        viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
        setHasOptionsMenu(true);

        ParseUser currentUser = ParseUser.getCurrentUser();

        ParseFile imagemContato = (ParseFile)currentUser.get("foto");
        final ImageView imageComercioView = (ImageView) view.findViewById(R.id.foto_perfil);

        if (imagemContato == null){

            imageComercioView.setImageResource(R.drawable.ic_user);
        }else {

            imagemContato.getDataInBackground(new GetDataCallback() {

                @Override
                public void done(byte[] data, ParseException e) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    imageComercioView.setImageBitmap(bitmap);
                }
            });
        }

        String nomeUsuario = currentUser.getString("nomePessoaFisica");
        TextView razaoUsuarioView = (TextView) view.findViewById(R.id.nomeUsuario);
        razaoUsuarioView.setText(nomeUsuario);

        List<ParseObject> qtdSeguindo = (List<ParseObject>) currentUser.get("seguindo");
        TextView qtdSeguindoView = (TextView) view.findViewById(R.id.qtd_seguindo);
        qtdSeguindoView.setText(String.valueOf(qtdSeguindo.size()));

        view.findViewById(R.id.seguindo).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getBaseContext(), ListaUsuario.class));

            }
        });

        view.findViewById(R.id.qtd_seguindo).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startActivity(new Intent(getActivity().getBaseContext(), ListaUsuario.class));
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_usuarios, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
       /* if (id == R.id.usuarios) {
            Log.i("AppInfo", "ID USUARIOS: " );
            startActivity(new Intent(getActivity().getBaseContext(), ListaUsuario.class));

        } else */if (id == R.id.logout) {
            ParseUser.getCurrentUser().logOut();
            startActivity(new Intent(getActivity(), DispatchActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
