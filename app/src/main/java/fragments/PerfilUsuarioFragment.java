package fragments;

import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.parse.ParseUser;
import com.parse.starter.DispatchActivity;
import com.parse.starter.ListaUsuario;
import com.parse.starter.R;
import com.parse.starter.publicacoes.CustomAdapter;
import com.parse.starter.publicacoes.ItemObject;

import java.util.ArrayList;
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

        GridView gridview = (GridView)view.findViewById(R.id.gridview);
        gridview.setHorizontalSpacing(5);
        gridview.setVerticalSpacing(5);
        gridview.setAdapter(new MyAdapter());

        return view;
    }

    private class MyAdapter extends BaseAdapter
    {
        private List<Item> items = new ArrayList<Item>();
        private LayoutInflater inflater;

        public MyAdapter()
        {
            inflater = LayoutInflater.from(getContext().getApplicationContext());

            items.add(new Item("Distribuidora Recanto - Cerveja Antarctica, R$32,00, Válido até 04/03/2016", R.drawable.antarctica));
            items.add(new Item("Distribuidora Recanto - Cerveja Skol, R$30,00, Válido até 04/03/2016", R.drawable.skol));
            items.add(new Item("Distribuidora Recanto - Cerveja Bohemia, R$36,00, Válido até 04/03/2016", R.drawable.bohemia));
            items.add(new Item("Distribuidora Recanto - Cerveja Devassa, R$33,00, Válido até 04/03/2016", R.drawable.devassa));
            items.add(new Item("Distribuidora Recanto - Cerveja Brahma, R$29,00, Válido até 04/03/2016", R.drawable.brahma));
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i)
        {
            return items.get(i);
        }

        @Override
        public long getItemId(int i)
        {
            return items.get(i).drawableId;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup)
        {
            View v = view;
            ImageView picture;
            TextView name;

            if(v == null)
            {
                v = inflater.inflate(R.layout.gridview_item, viewGroup, false);
                v.setTag(R.id.picture, v.findViewById(R.id.picture));
                v.setTag(R.id.text, v.findViewById(R.id.text));
            }

            picture = (ImageView)v.getTag(R.id.picture);
            name = (TextView)v.getTag(R.id.text);

            Item item = (Item)getItem(i);

            picture.setImageResource(item.drawableId);
            name.setText(item.name);

            return v;
        }

        private class Item
        {
            final String name;
            final int drawableId;

            Item(String name, int drawableId)
            {
                this.name = name;
                this.drawableId = drawableId;
            }
        }
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
        if (id == R.id.usuarios) {
            Log.i("AppInfo", "ID USUARIOS: " );
            startActivity(new Intent(getActivity().getBaseContext(), ListaUsuario.class));

        } else if (id == R.id.logout) {
            ParseUser.getCurrentUser().logOut();
            startActivity(new Intent(getActivity(), DispatchActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
