package com.unsa.etf.rma.amina_salcin.drugaspirala;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Amina on 5/31/2018.
 */

public class BazaOpenHelper extends SQLiteOpenHelper {
    public static final String DATEBASE_TABLE="Kategorija";
    public static final String DATEBASE_TABLE1="Knjiga";
    public static final String DATEBASE_TABLE2="Autor";
    public static final String DATEBASE_TABLE3="Autorstvo";
    public static final String DATEBASE_NAME="mojaBaza.db";
    public static final int DATEBASE_VERSION=1;
    public static final String ID="_id";
    public static final String NAZIV_KOLONE="naziv";
    public static final String ID1="_id";
    public static final String NAZIV_KOLONE11="naziv";
    public static final String NAZIV_KOLONE1="Opis";
    public static final String NAZIV_KOLONE2="datumObjavljivanja";
    public static final String NAZIV_KOLONE3="brojStranica";
    public static final String NAZIV_KOLONE4="idWebServisa";
    public static final String NAZIV_KOLONE5="idKategorije";
    public static final String NAZIV_KOLONE6="slika";
    public static final String NAZIV_KOLONE7="pregedana";
    public static final String NAZIV_KOLONE8="ime";
    public static final String NAZIV_KOLONE9="idautora";
    public static final String NAZIV_KOLONE10="idknjige";

    private static final String DATEBASE_CREATE="CREATE TABLE "+ DATEBASE_TABLE+" ("
            +ID+" INTEGER PRIMARY KEY," +NAZIV_KOLONE+" TEXT);";

    private static final String DATEBASE_CREATE1="CREATE TABLE "+ DATEBASE_TABLE1+" ("
            +ID1+" INTEGER PRIMARY KEY," +NAZIV_KOLONE11+" TEXT,"+ NAZIV_KOLONE1+" TEXT,"+ NAZIV_KOLONE2+" TEXT,"+ NAZIV_KOLONE3+
            " INTEGER,"+NAZIV_KOLONE4+" TEXT,"+NAZIV_KOLONE5+" INTEGER,"+NAZIV_KOLONE6+" TEXT,"+NAZIV_KOLONE7+" INTEGER);";

    private static final String DATEBASE_CREATE2="CREATE TABLE "+ DATEBASE_TABLE2+" ("
            +ID+" INTEGER PRIMARY KEY," +NAZIV_KOLONE8+" TEXT);";

    private static final String DATEBASE_CREATE3="CREATE TABLE "+ DATEBASE_TABLE3+" ("
            +ID+" INTEGER PRIMARY KEY," +NAZIV_KOLONE9+" INTEGER,"+NAZIV_KOLONE10+" INTEGER);";


    public BazaOpenHelper(Context context) {
        super(context, DATEBASE_NAME, null, DATEBASE_VERSION);
        this.mSqliteDatebase = mSqliteDatebase;
    }
    SQLiteDatabase mSqliteDatebase;

    public BazaOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, SQLiteDatabase mSqliteDatebase) {
        super(context, name, factory, version);
        this.mSqliteDatebase = mSqliteDatebase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATEBASE_CREATE);
        db.execSQL(DATEBASE_CREATE1);
        db.execSQL(DATEBASE_CREATE2);
        db.execSQL(DATEBASE_CREATE3);
    }
public void deleteDatebase(Context c){
        c.deleteDatabase(DATEBASE_NAME);
}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS" + DATEBASE_TABLE));
        db.execSQL(String.format("DROP TABLE IF EXISTS" + DATEBASE_TABLE1));
        db.execSQL(String.format("DROP TABLE IF EXISTS" + DATEBASE_TABLE2));
        db.execSQL(String.format("DROP TABLE IF EXISTS" + DATEBASE_TABLE3));
        onCreate(db);
    }

    public long dodajKategoriju(String kategorija){
        long a=index(kategorija);
        if(a==-1){
            mSqliteDatebase=this.getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put(NAZIV_KOLONE, kategorija);
            a=mSqliteDatebase.insert(DATEBASE_TABLE, null, values);
            mSqliteDatebase.close();
        }

        return a;
    }
    public long dodajKnjigu(Knjiga knjiga){

        long a=dodajKnjiguPom(knjiga);
        if(a==-1) {

            mSqliteDatebase = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(NAZIV_KOLONE11, knjiga.getNaziv());
            values.put(NAZIV_KOLONE1, knjiga.getOpis());
            values.put(NAZIV_KOLONE2, knjiga.getDatumObjavljivanja());
            values.put(NAZIV_KOLONE3, knjiga.getBrojStranica());
            values.put(NAZIV_KOLONE4, knjiga.getId());
            values.put(NAZIV_KOLONE5, knjiga.getKategorija1());
            values.put(NAZIV_KOLONE6, knjiga.getSlika().toString());
            values.put(NAZIV_KOLONE7, knjiga.isSelektovana());
            a=mSqliteDatebase.insert(DATEBASE_TABLE1, null, values);
                for(Autor aa:knjiga.getAutori()){
                    dodajAutora(aa.getImeiPrezime());
                    dodajAutorstvo(daLiPostojiAutor(aa.getImeiPrezime()),a);
                }
             mSqliteDatebase.close();
        }
        return a;
    }




    public long index(String kategorija){
        mSqliteDatebase=this.getReadableDatabase();
        String select_all="Select * from "+DATEBASE_TABLE;
        Cursor cur=mSqliteDatebase.rawQuery(select_all, null);
        if(cur!=null){
            if(cur.moveToFirst()){
                do{
                    int id=cur.getInt(cur.getColumnIndex(ID));
                    String s=cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE));

                    if(s.equals(kategorija)) return id;
                }
                while(cur.moveToNext());
            } cur.close();
            mSqliteDatebase.close();
            return -1;
        }
        return -1;

    }
    public ArrayList<String> unosi(){
        ArrayList<String> l=new ArrayList<String>();
        mSqliteDatebase=this.getReadableDatabase();
        String select_all="Select * from "+DATEBASE_TABLE;
        Cursor cur=mSqliteDatebase.rawQuery(select_all, null);
        if(cur.moveToFirst()){
            do{
                int id=cur.getInt(cur.getColumnIndex(ID));
                String s=cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE));
                l.add(s);
            }
            while(cur.moveToNext());
        } cur.close();
       mSqliteDatebase.close();
        return l;
    }
    public long daLiPostojiAutor(String ime){
        mSqliteDatebase=this.getReadableDatabase();
        String select_all="Select * from "+DATEBASE_TABLE2;
        try {
            Cursor cur=mSqliteDatebase.rawQuery(select_all, null);
            if(cur.moveToFirst()){
                do{
                    int id=cur.getInt(cur.getColumnIndex(ID));
                    String s=cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE8));
                    if(s.equals(ime))return cur.getInt(cur.getColumnIndexOrThrow(ID));
                }
                while(cur.moveToNext());
            } cur.close();
            mSqliteDatebase.close();
            return -1;
        }
        catch (Exception w){
            return -1;
        }
    }

    public long dodajAutora(String ime){
        long a=-1;
        if(daLiPostojiAutor(ime)==-1){
            mSqliteDatebase=this.getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put(NAZIV_KOLONE8, ime);
            a=mSqliteDatebase.insert(DATEBASE_TABLE2, null, values);
            mSqliteDatebase.close();
        }
        return a;
    }
    public void dodajAutorstvo(long idAutora, long idKnjige){
         mSqliteDatebase=this.getWritableDatabase();
            ContentValues values=new ContentValues();
            values.put(NAZIV_KOLONE9, idAutora);
            values.put(NAZIV_KOLONE10, idKnjige);
            mSqliteDatebase.insert(DATEBASE_TABLE3, null, values);
            mSqliteDatebase.close();
    }

    public long dodajKnjiguPom(Knjiga knjiga){
        mSqliteDatebase=this.getReadableDatabase();
        try  {
            Cursor cur = mSqliteDatebase.rawQuery("Select * from " + DATEBASE_TABLE1, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        if (cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE11)).equals(knjiga.getNaziv())) {
                            return cur.getInt(cur.getColumnIndexOrThrow(ID));
                        }
                    }
                    while (cur.moveToNext());
                }
                cur.close();
                mSqliteDatebase.close();
                return -1;
            }
        }
        catch (Exception e){
            return -1;
        }
        return-1;
    }
    public void update(Knjiga k){
        long i=dodajKnjiguPom(k);
        mSqliteDatebase=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(NAZIV_KOLONE7, 1);
        mSqliteDatebase.update(DATEBASE_TABLE1, cv, ID1+"="+i, null);
        mSqliteDatebase.close();
    }
    public Knjiga vratiKnjiguZ(long idKnjige){
        mSqliteDatebase=this.getReadableDatabase();
        ArrayList<Pair<Long, Long >> autorstva=new ArrayList<Pair<Long, Long >>();
        ArrayList<Pair<Long, String >> autorstva1=new ArrayList<Pair<Long, String >>();
        Cursor cur1 = mSqliteDatebase.rawQuery("Select * from " + DATEBASE_TABLE3, null);
        if(cur1!=null){
            if(cur1.moveToFirst()){
                do{
                    autorstva.add((new Pair<Long, Long>(cur1.getLong(cur1.getColumnIndexOrThrow(NAZIV_KOLONE9)), cur1.getLong(cur1.getColumnIndexOrThrow(NAZIV_KOLONE10)))));
                }while(cur1.moveToNext());
            }
        }
        cur1.close();
        mSqliteDatebase.close();
        mSqliteDatebase=this.getReadableDatabase();
        Cursor cur11 = mSqliteDatebase.rawQuery("Select * from " + DATEBASE_TABLE2, null);
        if(cur11!=null){
            if(cur11.moveToFirst()){
                do{
                    autorstva1.add((new Pair<Long, String>(cur11.getLong(cur11.getColumnIndexOrThrow(ID)), cur11.getString(cur11.getColumnIndexOrThrow(NAZIV_KOLONE8)))));
                }while(cur11.moveToNext());
            }
        }
        cur11.close();
        mSqliteDatebase.close();
        mSqliteDatebase=this.getReadableDatabase();
        ArrayList<Pair<Integer, String >>kategorije=new ArrayList<Pair<Integer, String>>();
        Cursor cur111 = mSqliteDatebase.rawQuery("Select * from " + DATEBASE_TABLE, null);
        if(cur111!=null){
            if(cur111.moveToFirst()){
                do{
                    kategorije.add((new Pair<Integer, String>(cur111.getInt(cur111.getColumnIndexOrThrow(ID)), cur111.getString(cur111.getColumnIndexOrThrow(NAZIV_KOLONE)))));
                }while(cur111.moveToNext());
            }
        }
        cur111.close();
        mSqliteDatebase.close();
        mSqliteDatebase=this.getReadableDatabase();
        Cursor cur = mSqliteDatebase.rawQuery("Select * from " + DATEBASE_TABLE1, null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    try {
                        int id=cur.getInt(cur.getColumnIndexOrThrow(ID));
                        if(idKnjige==id){
                            ArrayList<Long> idautora=new ArrayList<Long>();
                            ArrayList<Autor> autoriii=new ArrayList<Autor>();
                            for(Pair<Long, Long> kl:autorstva){
                                if(kl.second==id) idautora.add(kl.first);
                            }
                            for(Long kl:idautora){
                                for(Pair<Long, String>ll:autorstva1){
                                    if(kl==ll.first){
                                        autoriii.add(new Autor(ll.second, cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE4))));
                                    }
                                }
                            }
                            Knjiga k=new Knjiga(cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE4)),cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE11)),
                                    autoriii, cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE1)),cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE2)),
                                    new URL(cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE6))),cur.getInt(cur.getColumnIndexOrThrow(NAZIV_KOLONE3)));

                            for(Pair<Integer, String> kl:kategorije){
                                if(kl.first==cur.getInt(cur.getColumnIndexOrThrow(NAZIV_KOLONE5)))k.setKategorija(kl.second);
                            }
                            k.setSelektovana(cur.getInt(cur.getColumnIndexOrThrow(NAZIV_KOLONE7)));
                            return k;
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                while (cur.moveToNext());
            }
            cur.close();
            mSqliteDatebase.close();
        }
      return null;
    }
    public ArrayList<Knjiga>knjigeAutora(long idAutora){
        mSqliteDatebase=this.getReadableDatabase();
        ArrayList<Knjiga>knjigee=new ArrayList<Knjiga>();
        ArrayList<Pair<Long, Long >> autorstva=new ArrayList<Pair<Long, Long >>();
        ArrayList<Pair<Long, Long >> autorstvapom=new ArrayList<Pair<Long, Long >>();
        ArrayList<Pair<Long, String >> autorstva1=new ArrayList<Pair<Long, String >>();
        Cursor cur1 = mSqliteDatebase.rawQuery("Select * from " + DATEBASE_TABLE3, null);
        if(cur1!=null){
            if(cur1.moveToFirst()){
                do{
                    if(idAutora==cur1.getLong(cur1.getColumnIndexOrThrow(NAZIV_KOLONE9)))
                    autorstva.add((new Pair<Long, Long>(cur1.getLong(cur1.getColumnIndexOrThrow(NAZIV_KOLONE9)), cur1.getLong(cur1.getColumnIndexOrThrow(NAZIV_KOLONE10)))));
                }while(cur1.moveToNext());
            }
        }
        cur1.close();
        mSqliteDatebase.close();
        for(Pair<Long, Long>aa: autorstva){
            knjigee.add(vratiKnjiguZ(aa.second));
        }
        return knjigee;
    }
    public ArrayList<Knjiga>knjigeKategorija(long idKategorije){
        mSqliteDatebase=this.getReadableDatabase();
        ArrayList<Knjiga>knjigee=new ArrayList<Knjiga>();
        ArrayList<Pair<Long, Long >> autorstva=new ArrayList<Pair<Long, Long >>();
        ArrayList<Pair<Long, String >> autorstva1=new ArrayList<Pair<Long, String >>();
        Cursor cur1 = mSqliteDatebase.rawQuery("Select * from " + DATEBASE_TABLE3, null);
        if(cur1!=null){
            if(cur1.moveToFirst()){
                do{
                    autorstva.add((new Pair<Long, Long>(cur1.getLong(cur1.getColumnIndexOrThrow(NAZIV_KOLONE9)), cur1.getLong(cur1.getColumnIndexOrThrow(NAZIV_KOLONE10)))));
                }while(cur1.moveToNext());
            }
        }
        cur1.close();
        mSqliteDatebase.close();
        mSqliteDatebase=this.getReadableDatabase();
        Cursor cur11 = mSqliteDatebase.rawQuery("Select * from " + DATEBASE_TABLE2, null);
        if(cur11!=null){
            if(cur11.moveToFirst()){
                do{
                    autorstva1.add((new Pair<Long, String>(cur11.getLong(cur11.getColumnIndexOrThrow(ID)), cur11.getString(cur11.getColumnIndexOrThrow(NAZIV_KOLONE8)))));
                }while(cur11.moveToNext());
            }
        }
        cur11.close();
        mSqliteDatebase.close();
        mSqliteDatebase=this.getReadableDatabase();
        ArrayList<Pair<Integer, String >>kategorije=new ArrayList<Pair<Integer, String>>();
        Cursor cur111 = mSqliteDatebase.rawQuery("Select * from " + DATEBASE_TABLE, null);
        if(cur111!=null){
            if(cur111.moveToFirst()){
                do{
                    kategorije.add((new Pair<Integer, String>(cur111.getInt(cur111.getColumnIndexOrThrow(ID)), cur111.getString(cur111.getColumnIndexOrThrow(NAZIV_KOLONE)))));
                }while(cur111.moveToNext());
            }
        }
        cur111.close();
        mSqliteDatebase.close();
        mSqliteDatebase=this.getReadableDatabase();
        Cursor cur = mSqliteDatebase.rawQuery("Select * from " + DATEBASE_TABLE1, null);
        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    ArrayList<Long> idautora=new ArrayList<Long>();
                    ArrayList<Autor> autoriii=new ArrayList<Autor>();
                    try {
                        int id=cur.getInt(cur.getColumnIndexOrThrow(ID));
                        for(Pair<Long, Long> kl:autorstva){
                            if(kl.second==id) idautora.add(kl.first);
                        }
                        for(Long kl:idautora){
                            for(Pair<Long, String>ll:autorstva1){
                                if(kl==ll.first){
                                    autoriii.add(new Autor(ll.second, cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE4))));
                                }
                            }
                        }
                        if(idKategorije==cur.getInt(cur.getColumnIndexOrThrow(NAZIV_KOLONE5))){
                            Knjiga k=new Knjiga(cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE4)),cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE11)),
                                    autoriii, cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE1)),cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE2)),
                                    new URL(cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE6))),cur.getInt(cur.getColumnIndexOrThrow(NAZIV_KOLONE3)));

                            for(Pair<Integer, String> kl:kategorije){
                                if(kl.first==cur.getInt(cur.getColumnIndexOrThrow(NAZIV_KOLONE5)))k.setKategorija(kl.second);
                            }
                            k.setSelektovana(cur.getInt(cur.getColumnIndexOrThrow(NAZIV_KOLONE7)));
                            knjigee.add(k);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                while (cur.moveToNext());
            }
            cur.close();
            mSqliteDatebase.close();
        }
        return knjigee;
    }
    public ArrayList<Knjiga> knjige(){
        mSqliteDatebase=this.getReadableDatabase();
        ArrayList<Knjiga>knjigee=new ArrayList<Knjiga>();
        ArrayList<Pair<Long, Long >> autorstva=new ArrayList<Pair<Long, Long >>();
        ArrayList<Pair<Long, String >> autorstva1=new ArrayList<Pair<Long, String >>();
        Cursor cur1 = mSqliteDatebase.rawQuery("Select * from " + DATEBASE_TABLE3, null);
        if(cur1!=null){
           if(cur1.moveToFirst()){
               do{
                   autorstva.add((new Pair<Long, Long>(cur1.getLong(cur1.getColumnIndexOrThrow(NAZIV_KOLONE9)), cur1.getLong(cur1.getColumnIndexOrThrow(NAZIV_KOLONE10)))));
               }while(cur1.moveToNext());
           }
        }
        cur1.close();
        mSqliteDatebase.close();
        mSqliteDatebase=this.getReadableDatabase();
        Cursor cur11 = mSqliteDatebase.rawQuery("Select * from " + DATEBASE_TABLE2, null);
        if(cur11!=null){
            if(cur11.moveToFirst()){
                do{
                    autorstva1.add((new Pair<Long, String>(cur11.getLong(cur11.getColumnIndexOrThrow(ID)), cur11.getString(cur11.getColumnIndexOrThrow(NAZIV_KOLONE8)))));
                }while(cur11.moveToNext());
            }
        }
        cur11.close();
        mSqliteDatebase.close();
        mSqliteDatebase=this.getReadableDatabase();
        ArrayList<Pair<Integer, String >>kategorije=new ArrayList<Pair<Integer, String>>();
        Cursor cur111 = mSqliteDatebase.rawQuery("Select * from " + DATEBASE_TABLE, null);
        if(cur111!=null){
            if(cur111.moveToFirst()){
                do{
                    kategorije.add((new Pair<Integer, String>(cur111.getInt(cur111.getColumnIndexOrThrow(ID)), cur111.getString(cur111.getColumnIndexOrThrow(NAZIV_KOLONE)))));
                }while(cur111.moveToNext());
            }
        }
        cur111.close();
        mSqliteDatebase.close();
        mSqliteDatebase=this.getReadableDatabase();
            Cursor cur = mSqliteDatebase.rawQuery("Select * from " + DATEBASE_TABLE1, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        ArrayList<Long> idautora=new ArrayList<Long>();
                        ArrayList<Autor> autoriii=new ArrayList<Autor>();
                        try {
                            int id=cur.getInt(cur.getColumnIndexOrThrow(ID));
                            for(Pair<Long, Long> kl:autorstva){
                                if(kl.second==id) idautora.add(kl.first);
                            }
                            for(Long kl:idautora){
                                for(Pair<Long, String>ll:autorstva1){
                                    if(kl==ll.first){
                                        autoriii.add(new Autor(ll.second, cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE4))));
                                    }
                                }
                            }
                            Knjiga k=new Knjiga(cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE4)),cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE11)),
                                    autoriii, cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE1)),cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE2)),
                                    new URL(cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE6))),cur.getInt(cur.getColumnIndexOrThrow(NAZIV_KOLONE3)));
                            k.setSelektovana(cur.getInt(cur.getColumnIndexOrThrow(NAZIV_KOLONE7)));
                            for(Pair<Integer, String> kl:kategorije){
                                if(kl.first==cur.getInt(cur.getColumnIndexOrThrow(NAZIV_KOLONE5)))k.setKategorija(kl.second);
                            }
                            knjigee.add(k);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    while (cur.moveToNext());
                }
                cur.close();
                mSqliteDatebase.close();
            }
        return knjigee;

    }

}
