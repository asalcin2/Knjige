package com.unsa.etf.rma.amina_salcin.drugaspirala;


import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPreporuci extends Fragment {
    ArrayList<String> emailovi = new ArrayList<String>();
    ArrayList<Pair<String, String>>parovi=new ArrayList<Pair<String, String>>();
    public FragmentPreporuci() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v=inflater.inflate(R.layout.fragment_fragment_preporuci, container, false);

       return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView naslov=(TextView)getView().findViewById(R.id.nnaslov);
        TextView dugme=(Button)getView().findViewById(R.id.dPosalji);

        TextView opis=(TextView)getView().findViewById(R.id.podaci);
        Bundle b=getArguments();
        final Knjiga k=new Knjiga();
        if (b != null) {
            if (b.containsKey("knjiga")) {
                Knjiga kk = b.getParcelable("knjiga");
                k.setOpis(kk.getOpis());
                k.setNaziv(kk.getNaziv());
                k.setAutori(kk.getAutori());
                naslov.setText(k.getNaziv());
                opis.setText("Opis:\n"+k.getOpis());
            }
        }
        String ime="";
        ContentResolver cr=getContext().getContentResolver();
        final Spinner kontakti=(Spinner)getView().findViewById(R.id.sKontakti);
        Cursor c=getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, null, null,  null);
        Cursor c1=getContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,  null);
        while (c.moveToNext()&& c1.moveToNext()) {
            parovi.add(new Pair(c1.getString(c1.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)), c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.DATA)).toString()));
            emailovi.add(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)));
        }
        c.close();
        c1.close();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, emailovi);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kontakti.setAdapter(adapter);
        dugme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ime="";
                for(Pair<String, String>p:parovi){
                    if(p.second.equals(kontakti.getSelectedItem().toString())){
                        ime=p.first;
                        break;
                    }
                }
                String autori="";
                for(Autor a:k.getAutori()){
                    autori+=a.getImeiPrezime();
                }
                String s="Zdravo, "+ime+"\n"+"Procitaj knjigu "+k.getNaziv()+" od "+autori+"!";
                Intent emailItent=new Intent(Intent.ACTION_SEND);
                emailItent.setData(Uri.parse("mailto:"));
                emailItent.setType("message/rfc822");
                emailItent.putExtra(Intent.EXTRA_EMAIL, new String[]{kontakti.getSelectedItem().toString()});
                emailItent.putExtra(Intent.EXTRA_CC, "");
                emailItent.putExtra(Intent.EXTRA_SUBJECT, "Preporuka");
                emailItent.putExtra(Intent.EXTRA_TEXT, s);
                try{
                        startActivity(Intent.createChooser(emailItent, "Send mail..."));
                        getActivity().finish();

                }
                catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(getContext(), "There is no email client installed.", Toast.LENGTH_LONG).show();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }


            }
        });
    }



}
