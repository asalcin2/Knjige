package com.unsa.etf.rma.amina_salcin.drugaspirala;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Amina on 5/14/2018.
 */

public class DohvatiKnjige extends AsyncTask<String, Void, ArrayList<Knjiga>> {
    private static final String LOG_TAG = DohvatiKnjige.class.getSimpleName();
    private static final String TAG = DohvatiKnjige.class.getSimpleName();
    private static final String TAG1 = DohvatiKnjige.class.getSimpleName();
    public interface IDohvatiKnjigeDone {
        public void onDohvatiDone(ArrayList<Knjiga> listeKnjiga);
    }
    ArrayList<Knjiga> listeKnjiga = new ArrayList<Knjiga>();
    public IDohvatiKnjigeDone getPozivatelj() {
        return pozivatelj;
    }
    public void setPozivatelj(IDohvatiKnjigeDone pozivatelj) {
        this.pozivatelj = pozivatelj;
    }
    private IDohvatiKnjigeDone pozivatelj = null;
    public DohvatiKnjige(IDohvatiKnjigeDone i) {
        this.pozivatelj = i;
    }

    @Override
    protected void onPostExecute(ArrayList<Knjiga> aVoid) {
        pozivatelj.onDohvatiDone(aVoid);
    }

    @Override
    protected ArrayList<Knjiga> doInBackground(String... strings) {
        String odvojeno[] = strings[0].split(";");
        ArrayList<String> sve = new ArrayList<String>();
        for (String s : odvojeno) {
            sve.add(s);
        }
        System.out.println("greskssa " + String.valueOf(odvojeno.length));
        if(odvojeno.length==0) sve.add(strings[0]);
        for (int w = 0; w < sve.size(); w++) {
            String h =null;
            h=NetworkUtils.getBookInfo(sve.get(w));
            if(h!=null){
                try {
                    JSONObject jo = new JSONObject(h);
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
                                        opis = volumeinfo.optString("description");
                                        datumObjavljivanja = volumeinfo.optString("publishedDate");
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
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return listeKnjiga;
    }
}
