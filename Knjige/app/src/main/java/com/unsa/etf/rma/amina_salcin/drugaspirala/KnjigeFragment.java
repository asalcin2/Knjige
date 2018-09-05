package com.unsa.etf.rma.amina_salcin.drugaspirala;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.unsa.etf.rma.amina_salcin.drugaspirala.R.color.plava;


/**
 * A simple {@link Fragment} subclass.
 */
public class KnjigeFragment extends Fragment {
    public interface naPrvi{
        public void  naPrviFragment(ArrayList<Knjiga>k);
        public void  naPrviFragment1(Knjiga k);
    }
    String kategorija;
    String pisac;
     String varijabla="";
      ArrayList<Knjiga> knjigeZaIzabranuKategoriju=new ArrayList<Knjiga>();
    ArrayList<Knjiga> sveknjige=new ArrayList<Knjiga>();
    public static ArrayList<Pair<String, Integer>>listaPozicijaZaKategoriju=new ArrayList<Pair<String, Integer>>() ;
    public static ArrayList<Pair<String, Integer>>listaPozicijazaAutore=new ArrayList<Pair<String, Integer>>() ;

    private naPrvi n;
    public KnjigeFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        n=(naPrvi) getActivity();
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_knjige, container, false);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final Adapterr adapter;
        ListView listaKnjiga = (ListView)getView(). findViewById(R.id.listaKnjiga);
        Button ponisti = (Button)getView(). findViewById(R.id.dPovratak);
        Bundle b=getArguments();
        String q="";


        if (b != null) {
            if (b.containsKey("Item")) {
                varijabla = b.getString("Item");
            }
            if (b.containsKey("knjigeZaKategoriju")) {
                Log.d("adadadasd", "ne radi");
                sveknjige.clear();
                //ArrayList<Knjiga>j=
                //sveknjige.addAll(j);
               // sveknjige.addAll(KategorijeAkt.db.knjige());
               // knjigeZaIzabranuKategoriju=b.getParcelableArrayList("knjigeZaKategoriju");;
                boolean t=false;

                for(int h=0; h<varijabla.length()-1; h++){
                    if(varijabla.charAt(h)=='.' && varijabla.charAt(h+1)=='\n') {
                        pisac=varijabla.substring(0,h);t=true;
                    }
                }
                if(t) kategorija="";
                else {
                    kategorija=varijabla;
                    pisac="";
                }
                if(pisac.equals("")){
                    kategorija=varijabla;
                    /*for (Knjiga k:sveknjige){
                        if(k.getKategorija().equals(kategorija)==true)knjigeZaIzabranuKategoriju.add(k);
                    }*/
                    knjigeZaIzabranuKategoriju=KategorijeAkt.db.knjigeKategorija(KategorijeAkt.db.index(kategorija));
                }
                else {
//                    for (Knjiga k:sveknjige){
//                        for(Autor a: k.getAutori()) {
//                            if (a.getImeiPrezime().equals(pisac)) knjigeZaIzabranuKategoriju.add(k);
//                        }
//                    }
                    knjigeZaIzabranuKategoriju=KategorijeAkt.db.knjigeAutora(KategorijeAkt.db.daLiPostojiAutor(pisac));
                }


            }
        }
        adapter=new Adapterr(this.getContext(), R.layout.red, knjigeZaIzabranuKategoriju);
        listaKnjiga.setAdapter(adapter);
        ponisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n.naPrviFragment(sveknjige);
            }
        });
        listaKnjiga.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackgroundColor(getResources().getColor(R.color.plava));
                if(pisac.equals("")){
                   KategorijeAkt.db.update(knjigeZaIzabranuKategoriju.get(position));
                 adapter.notifyDataSetChanged();
                 //        listaPozicijaZaKategoriju.add(Pair.create(kategorija, position));
                }
                else {
                    KategorijeAkt.db.update(knjigeZaIzabranuKategoriju.get(position));
                    adapter.notifyDataSetChanged();
                 //   listaPozicijazaAutore.add(Pair.create(pisac, position));
                }
            }
        });
    }

    public class Adapterr extends ArrayAdapter<Knjiga> {

        public Adapterr(@NonNull Context context, int resource, @NonNull List<Knjiga> objects) {
            super(context, resource, objects);
        }

        @Override
        public int getCount() {
            return knjigeZaIzabranuKategoriju.size();
        }

        @Nullable
        @Override
        public Knjiga getItem(int position) {
            return super.getItem(position);
        }
        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view=getLayoutInflater().inflate(R.layout.red, null);
            final int g=i;
            ImageView slika=(ImageView)view.findViewById(R.id.eNaslovna);
            TextView autor=(TextView)view.findViewById(R.id.eAutor);
            TextView naslov=(TextView) view.findViewById(R.id.eNaziv);
            TextView opis=(TextView)view.findViewById(R.id.eOpis);
            TextView brojstranica=(TextView)view.findViewById(R.id.eBrojStranica);
            TextView datum=(TextView)view.findViewById(R.id.eDatumObjavljivanja);
            Button preporuka = (Button)view. findViewById(R.id.dPreporuci);
            preporuka.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    n.naPrviFragment1(knjigeZaIzabranuKategoriju.get(g));
                }
            });

            /*for (Knjiga h:knjigeZaIzabranuKategoriju){
                for(Knjiga j:sveknjige){
                    if(h.getNaziv().equals(j.getNaziv())&& h.getKategorija()==j.getKategorija()&& h.getOpis().equals(j.getOpis())){
                        if(h.isSelektovana()==1){
                            KategorijeAkt.db.update(j);
                        }
                    }
                }
            }*/
                Bitmap bmp = null;
                slika.setImageBitmap(knjigeZaIzabranuKategoriju.get(i).getIdSlike());
                String a1="";
                Picasso.get().load(knjigeZaIzabranuKategoriju.get(i).getSlika().toString()).into(slika);
                for(Autor a:knjigeZaIzabranuKategoriju.get(i).getAutori()){
                    a1+=a.getImeiPrezime()+" ";
                }
                autor.setText(a1);
                opis.setText("Opis:"+"\n"+knjigeZaIzabranuKategoriju.get(i).getOpis());
                datum.setText("Datum izdavanja je:" +"\n"+knjigeZaIzabranuKategoriju.get(i).getDatumObjavljivanja());
                brojstranica.setText("Broj stranica je:"+"\n"+String.valueOf(knjigeZaIzabranuKategoriju.get(i).getBrojStranica()));
                 naslov.setText(knjigeZaIzabranuKategoriju.get(i).getNaziv());
            //    knjigeZaIzabranuKategoriju=KategorijeAkt.db.
           /* */
            if(pisac.equals("")){
                knjigeZaIzabranuKategoriju=KategorijeAkt.db.knjigeKategorija(KategorijeAkt.db.index(kategorija));
                if(knjigeZaIzabranuKategoriju.get(i).isSelektovana()==1)view.setBackgroundColor(getResources().getColor(plava));


               /* for (Pair<String, Integer> k:listaPozicijaZaKategoriju){
                    if(k.second==i && k.first.equals(knjigeZaIzabranuKategoriju.get(i).getKategorija())) {

                        view.setBackgroundColor(getResources().getColor(plava));// view.setBackgroundColor(0xffaabbed);
                        //knjigeZaIzabranuKategoriju.get(i).setSelektovana(1);
                    }

                }*/
            }
            else {
                knjigeZaIzabranuKategoriju=KategorijeAkt.db.knjigeAutora(KategorijeAkt.db.daLiPostojiAutor(pisac));
                if(knjigeZaIzabranuKategoriju.get(i).isSelektovana()==1)view.setBackgroundColor(getResources().getColor(plava));
            }


            //}

            return view;
        }
    }
}
