package com.unsa.etf.rma.amina_salcin.drugaspirala;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ArrayAdapter;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;


public class Knjiga implements Parcelable {
    public Knjiga(){}

    public static final Creator<Knjiga> CREATOR = new Creator<Knjiga>() {
        @Override
        public Knjiga createFromParcel(Parcel in) {
            return new Knjiga(in);
        }

        @Override
        public Knjiga[] newArray(int size) {
            return new Knjiga[size];
        }
    };

    public Knjiga(String id, String naziv, ArrayList<Autor> autori, String opis, String datumObjavljivanja, URL slika, int brojStranica) {
        this.id = id;
        this.naziv = naziv;
        this.autori = autori;
        this.opis = opis;
        this.datumObjavljivanja = datumObjavljivanja;
        this.slika = slika;
        this.brojStranica = brojStranica;
        this.selektovana=0;
    }

    public boolean isStaraKnjiga() {
        return staraKnjiga;
    }

    public void setStaraKnjiga(boolean staraKnjiga) {
        this.staraKnjiga = staraKnjiga;
    }

    public long getKategorija1() {
        return kategorija1;
    }

    public void setKategorija1(long kategorija1) {
        this.kategorija1 = kategorija1;
    }

    private long kategorija1;

    public int isSelektovana() {
        return selektovana;
    }

    public void setSelektovana(int selektovana) {
        this.selektovana = selektovana;
    }

    private int selektovana;
    private boolean staraKnjiga;

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String nazivKnjige) {
        this.naziv = nazivKnjige;
    }

    public String getAutorKnjige() {
        return autorKnjige;
    }

    public void setAutorKnjige(String autorKnjige) {
        this.autorKnjige = autorKnjige;
    }

    public Bitmap getIdSlike() {
        return idSlike;
    }
    public String getKategorija() {
        return kategorija;
    }

    public void setKategorija(String kategorija) {
        this.kategorija = kategorija;
    }

    public void setIdSlike(Bitmap idSlike) {
        this.idSlike = idSlike;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private String naziv;
    private ArrayList<Autor>autori;
    private String opis;
    private String datumObjavljivanja;
    private URL slika;

    public ArrayList<Autor> getAutori() {
        return autori;
    }

    public void setAutori(ArrayList<Autor> autori) {
        this.autori = autori;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getDatumObjavljivanja() {
        return datumObjavljivanja;
    }

    public void setDatumObjavljivanja(String datumObjavljivanja) {
        this.datumObjavljivanja = datumObjavljivanja;
    }

    public URL getSlika() {
        return slika;
    }

    public void setSlika(URL slika) {
        this.slika = slika;
    }

    public int getBrojStranica() {
        return brojStranica;
    }

    public void setBrojStranica(int brojStranica) {
        this.brojStranica = brojStranica;
    }

    private int brojStranica;
    private String autorKnjige;
    private String kategorija;
    private Bitmap idSlike;

    public Knjiga(String nazivKnjige, String autorKnjige, String kategorija) {
        this.naziv = nazivKnjige;
        this.autorKnjige = autorKnjige;
        this.kategorija=kategorija;
        this.selektovana=0;
    }

    protected Knjiga(Parcel in) {
        naziv = in.readString();
        autorKnjige = in.readString();
        kategorija = in.readString();
        idSlike = (Bitmap)in.readParcelable(getClass().getClassLoader());
    }
 ;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(naziv);
        dest.writeString(autorKnjige);
        dest.writeString(kategorija);
        dest.writeParcelable(idSlike, flags);
    }
}
