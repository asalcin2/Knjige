package com.unsa.etf.rma.amina_salcin.drugaspirala;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Amina on 5/19/2018.
 */

public class KnjigePoznanika extends IntentService {
   // private static final String LOG_TAG = KnjigePoznanika.class.getSimpleName();

    public static final int STATUS_START=0;
public static final int STATUS_FINISH=1;
public static final int STATUS_ERROR=2;
    public KnjigePoznanika(){
        super("KnjigePoznanika");
    }
    public KnjigePoznanika(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final ResultReceiver receiver=intent.getParcelableExtra("receiver");
        Bundle bundle=new Bundle();
        receiver.send(STATUS_START, Bundle.EMPTY);
        String idkorinsika=intent.getStringExtra("idKorisnika");
        ArrayList<Knjiga>listeKnjiga=new ArrayList<Knjiga>();
        String zaRez=null;
        zaRez=NetworkUtils11.getBookInfo(idkorinsika);
        ArrayList<String>pozicije=new ArrayList<String>();
        if(zaRez!=null){
            try {
                JSONObject pom=new JSONObject(zaRez);
                JSONArray itemsi = pom.optJSONArray("items");
                if(itemsi!=null){
                    for(int g=0; g<itemsi.length(); g++){
                        JSONObject item=itemsi.optJSONObject(g);
                        if(item!=null){
                            String poz=item.optString("id");
                            if(poz!=null)pozicije.add(poz);
                        }
                    }
                    for(String b: pozicije){
                        String rez=null;
                        rez=NetworkUtils1.getBookInfo(idkorinsika, String.valueOf(b));
                        if(rez!=null){
                            try {
                                JSONObject jo = new JSONObject(rez);
                                JSONArray knjige = jo.optJSONArray("items");
                                if(knjige!=null){
                                    for (int i = 0; i < knjige.length(); i++) {
                                        JSONObject knjiga = knjige.optJSONObject(i);
                                        String id = null;
                                        String naziv = null;
                                        String autori = null;
                                        String opis = null;
                                        String datumObjavljivanja = null;
                                        URL urlSlika = null;
                                        int brojStranica = 0;
                                        if(knjiga!=null){
                                            JSONObject volumeinfo = knjiga.optJSONObject("volumeInfo");
                                            JSONObject imageLinks=null;
                                            ArrayList<Autor> autoriLista = new ArrayList<Autor>();
                                            if(volumeinfo!=null){
                                                imageLinks = volumeinfo.optJSONObject("imageLinks");
                                                try {
                                                    id = knjiga.optString("id");
                                                    naziv = volumeinfo.optString("title");
                                                    JSONArray vv = volumeinfo.optJSONArray("authors");
                                                    if(vv!=null){
                                                        for (int d = 0; d < vv.length(); d++) {
                                                            String ime = vv.getString(d);
                                                            autoriLista.add(new Autor(ime, id));
                                                        }
                                                    }
                                                    opis = volumeinfo.optString("description");
                                                    datumObjavljivanja = volumeinfo.optString("publishedDate");
                                                    if(imageLinks!=null){
                                                        String cc=imageLinks.optString("thumbnail");
                                                        if(cc!=null)
                                                            urlSlika = new URL(cc);
                                                        else {
                                                            urlSlika=new URL("https://d30y9cdsu7xlg0.cloudfront.net/png/111370-200.png");
                                                        }
                                                    }
                                                    else {
                                                        urlSlika=new URL("https://d30y9cdsu7xlg0.cloudfront.net/png/111370-200.png");
                                                    }
                                                    brojStranica = volumeinfo.optInt("pageCount");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            if (id == null) id = "Nepoznat id";
                                            if (autoriLista.size() == 0) autoriLista.add(new Autor("Nepoznat autor", id));
                                            if (opis == null) opis = "Nedostupna informacija";
                                            if (datumObjavljivanja == null) datumObjavljivanja = "Nepoznata informacija";
                                            if (urlSlika == null) {
                                                urlSlika = new URL("https://d30y9cdsu7xlg0.cloudfront.net/png/111370-200.png");
                                            }
                                            Knjiga nova = new Knjiga(id, naziv, autoriLista, opis, datumObjavljivanja, urlSlika, brojStranica);
                                            try {
                                                InputStream ig;
                                                if(imageLinks!=null){
                                                    String r=imageLinks.optString("thumbnail");
                                                    if(r!=null){
                                                        ig= new java.net.URL(imageLinks.optString("thumbnail")).openStream();
                                                    }
                                                    else {
                                                        ig=new java.net.URL("https://d30y9cdsu7xlg0.cloudfront.net/png/111370-200.png").openStream();
                                                    }
                                                }
                                                else {
                                                    ig=new java.net.URL("https://d30y9cdsu7xlg0.cloudfront.net/png/111370-200.png").openStream();
                                                }
                                                Bitmap image = BitmapFactory.decodeStream(ig);
                                                nova.setIdSlike(image);
                                            } catch (IOException e) {
                                                System.out.println("greska " + e);
                                            }
                                            nova.setStaraKnjiga(false);
                                            listeKnjiga.add(nova);
                                        }
                                    }
                                }
                            } catch(MalformedURLException w){
                                bundle.putString(Intent.EXTRA_TEXT, w.toString());
                                receiver.send(STATUS_ERROR, bundle);
                                w.printStackTrace();
                            }
                            catch (JSONException e) {
                                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                                receiver.send(STATUS_ERROR, bundle);
                                e.printStackTrace();
                            } catch (IOException e) {
                                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                                receiver.send(STATUS_ERROR, bundle);
                                e.printStackTrace();
                            }

                        }
                        if(rez==null ){
                            bundle.putString(Intent.EXTRA_TEXT, "Pokusajte ponovo!");
                            receiver.send(STATUS_ERROR, bundle);
                        }

                    }
                }
            } catch (JSONException e) {
                bundle.putString(Intent.EXTRA_TEXT, e.toString());
                receiver.send(STATUS_ERROR, bundle);
                e.printStackTrace();
            }
        }
        if(zaRez==null ){
            bundle.putString(Intent.EXTRA_TEXT, "Pokusajte ponovo!");
            receiver.send(STATUS_ERROR, bundle);
        }
        bundle.putParcelableArrayList("result", listeKnjiga);
        receiver.send(STATUS_FINISH, bundle);
    }
}
