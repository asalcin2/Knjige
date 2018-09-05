package com.unsa.etf.rma.amina_salcin.drugaspirala;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Amina on 5/18/2018.
 */

public class DohvatiNajnovije extends AsyncTask<String, Void, ArrayList<Knjiga>> {
    //private static final String LOG_TAG = DohvatiKnjige.class.getSimpleName();
  //  private static final String TAG = DohvatiKnjige.class.getSimpleName();
   // private static final String TAG1 = DohvatiKnjige.class.getSimpleName();
    public interface IDohvatiNajnovijeDone {
        public void onNajnovijeDone(ArrayList<Knjiga> listeKnjiga);
    }
    ArrayList<Knjiga> listeKnjiga = new ArrayList<Knjiga>();
    public DohvatiNajnovije.IDohvatiNajnovijeDone getPozivatelj() {
        return pozivatelj;
    }
    public void setPozivatelj(DohvatiNajnovije.IDohvatiNajnovijeDone pozivatelj) {
        this.pozivatelj = pozivatelj;
    }
    private DohvatiNajnovije.IDohvatiNajnovijeDone pozivatelj = null;
    public DohvatiNajnovije(DohvatiNajnovije.IDohvatiNajnovijeDone i) {
        this.pozivatelj = i;
    }

    @Override
    protected void onPostExecute(ArrayList<Knjiga> aVoid) {
        pozivatelj.onNajnovijeDone(aVoid);
    }

    @Override
    protected ArrayList<Knjiga> doInBackground(String... strings) {
        String h=null;
        h = NetworkUtiliss.getBookInfo(strings[0]);
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
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return listeKnjiga;

    }
}
