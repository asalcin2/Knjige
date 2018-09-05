package com.unsa.etf.rma.amina_salcin.drugaspirala;

import java.util.ArrayList;

/**
 * Created by Amina on 5/14/2018.
 */

public class Autor {
    public Autor(String imeiPrezime, String id) {
        this.imeiPrezime = imeiPrezime;
        dodajKnjigu(id);
    }

    public String getImeiPrezime() {
        return imeiPrezime;
    }

    public void setImeiPrezime(String imeiPrezime) {
        this.imeiPrezime = imeiPrezime;
    }

    public ArrayList<String> getKnjige() {
        return knjige;
    }

    public void setKnjige(ArrayList<String> knjige) {
        this.knjige = knjige;
    }

    private String imeiPrezime;
    private ArrayList<String>knjige=new ArrayList<String>();
    public void dodajKnjigu(String id){
        boolean a=false;
        for (String s: knjige) {
            if(s==id) a=true;
        }
        if(!a){
            knjige.add(id);
        }
    }
}
