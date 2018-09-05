package com.unsa.etf.rma.amina_salcin.drugaspirala;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static android.content.Intent.EXTRA_TEXT;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOnline extends Fragment implements DohvatiKnjige.IDohvatiKnjigeDone, DohvatiNajnovije.IDohvatiNajnovijeDone, MojResultReceiver.Receiver {

    Spinner rezultat ;
    @Override
    public void onNajnovijeDone(ArrayList<Knjiga> listeKnjiga) {
        Log.i(TAG, String.valueOf("vdxdfd"));
    }

    @Override
    public void onReceiverResult(int resultCode, Bundle resultData) {
        switch (resultCode){
            case KnjigePoznanika.STATUS_START:
                break;
            case KnjigePoznanika.STATUS_FINISH:
                ArrayList<Knjiga> results=resultData.getParcelableArrayList("result");
                webKnjige=results;
                ArrayList<String>rez=new ArrayList<String>();
                for(Knjiga k:webKnjige)rez.add(k.getNaziv());
                final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, rez);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                rezultat.setAdapter(adapter1);
                break;
            case KnjigePoznanika.STATUS_ERROR:
                String error=resultData.getString(Intent.EXTRA_TEXT);
                Toast.makeText(getContext(), error, Toast.LENGTH_LONG).show();
                break;

        }
    }

    public interface naPrvi1{
        public void  naPrviFragment1(ArrayList<Knjiga>dodaneKnjige);
    }
    naPrvi1 n;
    ArrayList<Knjiga>dodaneKnjige=new ArrayList<Knjiga>();
    private static final String TAG=FragmentOnline.class.getSimpleName();
    ArrayList<String> kategorije=new ArrayList<String>();
    ArrayList<Knjiga>webKnjige=new ArrayList<Knjiga>();
    MojResultReceiver  mReceiver;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        n=(FragmentOnline.naPrvi1) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_fragment_online, container, false);
        return v;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle b=getArguments();
        if (b != null) {
            if (b.containsKey("listakat")) {
                kategorije=b.getStringArrayList("listakat");
            }
            if (b.containsKey("sveknjige")) {
               // dodaneKnjige=b.getParcelableArrayList("sveknjige");
            }
        }
        dodaneKnjige=KategorijeAkt.db.knjige();
        final EditText upit=(EditText)getView().findViewById(R.id.textUpit);
        final Spinner spiner = (Spinner) getView().findViewById(R.id.sKategorije);
        rezultat= (Spinner) getView().findViewById(R.id.sRezultat);
        final Button dodajKnjigu=(Button)getView().findViewById(R.id.dAdd);
        final Button odustani=(Button)getView().findViewById(R.id.dPovratak);
        final Button run=(Button)getView().findViewById(R.id.dRun);
        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u=upit.getText().toString();
                String pom="autor:";
                String pom1="korisnik:";

                if(u.contains(pom)){
                    Log.i(TAG, String.valueOf(u.substring(0,6)));
                    DohvatiNajnovije asyncTask =new DohvatiNajnovije(new DohvatiNajnovije.IDohvatiNajnovijeDone() {
                        @Override
                        public void onNajnovijeDone(ArrayList<Knjiga> j) {
                            Log.i(TAG, String.valueOf(j.size()));
                            webKnjige=j;
                            ArrayList<String>rez=new ArrayList<String>();
                            for(Knjiga k:webKnjige)rez.add(k.getNaziv());
                            final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, rez);
                            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            rezultat.setAdapter(adapter1);
                            Log.i(TAG, String.valueOf("shgsh"+rez.size()));
                        }


                    });

                    asyncTask.execute(u.substring(6));

                } else  if(u.contains(pom1)){
                    Log.i(TAG, String.valueOf("shgsh111"));
                    Intent intent=new Intent(Intent.ACTION_SYNC, null, getActivity(), KnjigePoznanika.class);
                    mReceiver=new MojResultReceiver(new Handler());
                    mReceiver.setmReceiver(FragmentOnline.this);
                    intent.putExtra("receiver", mReceiver);
                    intent.putExtra("idKorisnika", u.substring(9));
                    getActivity().startService(intent);
                }
                else  {
                    DohvatiKnjige asyncTask =new DohvatiKnjige(new DohvatiKnjige.IDohvatiKnjigeDone() {
                        @Override
                        public void onDohvatiDone(ArrayList<Knjiga> j) {
                            webKnjige=j;
                            ArrayList<String>rez=new ArrayList<String>();
                            for(Knjiga k:webKnjige)rez.add(k.getNaziv());
                            final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, rez);
                            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            rezultat.setAdapter(adapter1);
                        }

                    });
                    asyncTask.execute(u);
                }
            }
        });

        odustani.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n.naPrviFragment1(dodaneKnjige);
            }
        });
        dodajKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rezultat.getCount()==0 || spiner.getCount()==0) Toast.makeText(getContext(), R.string.zaToast, Toast.LENGTH_LONG).show();
                else {

                    int i=rezultat.getSelectedItemPosition();
                    if(i<webKnjige.size()){
                        webKnjige.get(i).setKategorija1(KategorijeAkt.db.index(spiner.getSelectedItem().toString()));
                        webKnjige.get(i).setKategorija(spiner.getSelectedItem().toString());
                        webKnjige.get(i).setStaraKnjiga(false);
                        KategorijeAkt.db.dodajKnjigu(webKnjige.get(i));
                        dodaneKnjige.add(webKnjige.get(i));
                    }
                    }

            }
        });
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, kategorije);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner.setAdapter(adapter);
    }
    public FragmentOnline() {
        // Required empty public constructor
    }
    @Override
    public void onDohvatiDone(ArrayList<Knjiga> j) {
        webKnjige=j;
        ArrayList<String>rez=new ArrayList<String>();
        for(Knjiga k:webKnjige)rez.add(k.getNaziv());
        final ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, rez);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
}
