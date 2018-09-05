package com.unsa.etf.rma.amina_salcin.drugaspirala;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListeFragment extends Fragment {

    public interface NaKlik{
        public void  naKLik(ArrayList<String> unosi, ArrayList<Knjiga> knjige);
        public void KlikNaKategoriju(ArrayList<Knjiga>knjigeIzKat, String nazivkat);
        public void daNestaneMjestoF2();
        public void  online(ArrayList<String> unosi, ArrayList<Knjiga>svek);
    }
    public static final String EXTRA_MESSAGE = "lista kategorija";
    public static  ArrayList<String> unosi=new ArrayList<String>();;
     public static ArrayList<Knjiga>Zaautore=new ArrayList<Knjiga>();
    public static ArrayList<String>konacno=new ArrayList<String>();
    ArrayAdapter<String> adapter;
    EditText pretraga;
    Button pretragaKnjiga;
    Button dodajKategoriju;
    Button dodajKnjigu;
    ListView listaKategorija;
    Button autori;
    Button kategorije;
    Button online;
    private NaKlik naKliik;
    public ListeFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        naKliik=(NaKlik)getActivity();
    }

    ArrayList<Knjiga>l=new ArrayList<Knjiga>();
    ArrayList<Knjiga>l1=new ArrayList<Knjiga>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_liste, container, false);
        return v;
}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle b=getArguments();
        if (b != null ) {
            if (b.containsKey("listak")) {
                //Zaautore = b.getParcelableArrayList("listak");
            }
            if (b.containsKey("listak1")) {
           //   Zaautore=b.getParcelableArrayList("listak1");
            }
        }
        ArrayList<String> pomocnii=KategorijeAkt.db.unosi();
        unosi=pomocnii;
        ArrayList<Knjiga> pomocnii1=KategorijeAkt.db.knjige();
        Zaautore=pomocnii1;
        pretraga = (EditText)getView().findViewById(R.id.tekstPretraga);
        pretragaKnjiga = (Button)getView().findViewById(R.id.dPretraga);
        dodajKategoriju = (Button)getView().findViewById(R.id.dDodajKategoriju);
        dodajKnjigu = (Button)getView().findViewById(R.id.dDodajKnjigu);
        listaKategorija = (ListView)getView().findViewById(R.id.listaKategorija);
        autori=(Button)getView().findViewById(R.id.dAutor);
        kategorije=(Button)getView().findViewById(R.id.dKategorija);
        online=(Button)getView().findViewById(R.id.dDodajOnline);
        adapter= new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, unosi);
        dodajKategoriju.setEnabled(false);
        System.out.println("");
        listaKategorija.setAdapter(adapter);
        pretragaKnjiga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                naKliik.daNestaneMjestoF2();
                final String s=pretraga.getText().toString();
                if(pretraga.getText().toString().equals("")!=true) {
                    adapter.getFilter().filter(s);
                    listaKategorija.setAdapter(adapter);
                    boolean daLiJePrazan = true;
                    for (String a : unosi) {
                        if (a.equalsIgnoreCase(s)) daLiJePrazan = false;
                    }
                    if (daLiJePrazan) dodajKategoriju.setEnabled(true);
                    pretraga.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            naKliik.daNestaneMjestoF2();
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            naKliik.daNestaneMjestoF2();
                            dodajKategoriju.setEnabled(false);
                            ArrayAdapter<String> adapter12 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, unosi);
                            listaKategorija.setAdapter(adapter12);
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            naKliik.daNestaneMjestoF2();
                        }

                    });
                }
            }
        });
        dodajKategoriju.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                naKliik.daNestaneMjestoF2();
                if(pretraga.getText().toString().equals("")!=true){
                    KategorijeAkt.db.dodajKategoriju(pretraga.getText().toString());
                    unosi.add(pretraga.getText().toString());
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, unosi);
                    listaKategorija.setAdapter(adapter1);
                    adapter.clear();
                    adapter.addAll(unosi);
                    adapter.notifyDataSetChanged();
                    pretraga.setText("");
                    dodajKategoriju.setEnabled(false);
                }
            }
        });
        dodajKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                naKliik.daNestaneMjestoF2();
                pretragaKnjiga.setVisibility(View.VISIBLE);
                pretraga.setVisibility(View.VISIBLE);
                dodajKategoriju.setVisibility(View.VISIBLE);
                naKliik.naKLik(unosi, Zaautore);
            }
        });
        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                naKliik.online(unosi, Zaautore);
            }
        });
        listaKategorija.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long index=KategorijeAkt.db.index(listaKategorija.getItemAtPosition(position).toString());
                ArrayList<Knjiga>zaKategoriju=KategorijeAkt.db.knjigeKategorija(index);
                naKliik.KlikNaKategoriju(zaKategoriju, listaKategorija.getItemAtPosition(position).toString());

            }
        });
        kategorije.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                naKliik.daNestaneMjestoF2();
                pretragaKnjiga.setVisibility(View.VISIBLE);
                pretraga.setVisibility(View.VISIBLE);
                dodajKategoriju.setVisibility(View.VISIBLE);
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, unosi);
                listaKategorija.setAdapter(adapter1);
            }
        });

        autori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pretragaKnjiga.setVisibility(View.GONE);
                pretraga.setVisibility(View.GONE);
                dodajKategoriju.setVisibility(View.GONE);
                naKliik.daNestaneMjestoF2();
                pomocnaFunkcija(Zaautore);
            }
        });
    }


    public void pomocnaFunkcija(ArrayList<Knjiga> pom){
            final ArrayList<String>samoautori=new ArrayList<String>();
            samoautori.clear();
            int brojac = 1;
            for (int i = 0; i < Zaautore.size(); i++) {
                    for(int h=0; h<Zaautore.get(i).getAutori().size(); h++){
                        for(int g=0; g<Zaautore.size(); g++){
                            for(int f=0; f<Zaautore.get(g).getAutori().size(); f++)
                            if (i != g && Zaautore.get(i).getAutori().get(h).getImeiPrezime().equals(Zaautore.get(g).getAutori().get(f).getImeiPrezime())) {
                                brojac++;
                            }
                        }
                        if (brojac > 1)
                            samoautori.add(Zaautore.get(i).getAutori().get(h).getImeiPrezime() + ".\n" + "Broj napisanih knjiga je: " + brojac + ".");
                        else {
                            samoautori.add(Zaautore.get(i).getAutori().get(h).getImeiPrezime() + ".\n");
                        }
                        brojac = 1;
                    }
            }
            for (int i = 0; i < samoautori.size(); i++) {
                for (int j = 0; j < samoautori.size(); j++) {
                    if (i != j && samoautori.get(i).equals(samoautori.get(j)))
                        samoautori.set(j, "");
                }
            }
            konacno.clear();
            for (String g : samoautori) {
                if (g.equals("") != true) {
                    konacno.add(g);
                }
            }
            boolean ima = false;
            for (String q : samoautori) {
                for (String d : konacno) {
                    if (q.equals(d)) ima = true;
                }
                if (ima != true) konacno.add(q);
                ima = false;
            }
            ArrayAdapter<String> adapter11 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, konacno);
            listaKategorija.setAdapter(adapter11);
            pretragaKnjiga.setVisibility(View.GONE);
            pretraga.setVisibility(View.GONE);
            dodajKategoriju.setVisibility(View.GONE);
        }
}

