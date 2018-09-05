package com.unsa.etf.rma.amina_salcin.drugaspirala;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Amina on 6/1/2018.
 */

public class BazaOpenHelper1 extends SQLiteOpenHelper {
    public static final String DATEBASE_NAME="mojaBaza.db";
    public static final int DATEBASE_VERSION=1;

    public static final String DATEBASE_TABLE1="Knjiga";
    public static final String ID="_id";
    public static final String NAZIV_KOLONE="naziv";
    public static final String NAZIV_KOLONE1="Opis";
    public static final String NAZIV_KOLONE2="datumObjavljivanja";
    public static final String NAZIV_KOLONE3="brojStranica";
    public static final String NAZIV_KOLONE4="idWebServisa";
    public static final String NAZIV_KOLONE5="idKategorije";
    public static final String NAZIV_KOLONE6="slika";
    public static final String NAZIV_KOLONE7="pregedana";

    private static final String DATEBASE_CREATE1="CREATE TABLE "+ DATEBASE_TABLE1+" ("
            +ID+" INTEGER PRIMARY KEY," +NAZIV_KOLONE+" TEXT "+ NAZIV_KOLONE1+" TEXT "+ NAZIV_KOLONE2+" TEXT "+ NAZIV_KOLONE3+
            " INTEGER "+NAZIV_KOLONE4+" TEXT "+NAZIV_KOLONE5+" INTEGER "+NAZIV_KOLONE6+" TEXT "+NAZIV_KOLONE7+" INTEGER);";


    public BazaOpenHelper1(Context context) {
        super(context, DATEBASE_NAME, null, DATEBASE_VERSION);
    }
    SQLiteDatabase mSqliteDatebase;

    public BazaOpenHelper1(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, SQLiteDatabase mSqliteDatebase) {
        super(context, name, factory, version);
        this.mSqliteDatebase = mSqliteDatebase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATEBASE_CREATE1);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS" + DATEBASE_TABLE1));
        onCreate(db);
    }

    public long dodajKnjiguPom(Knjiga knjiga){
        mSqliteDatebase=this.getReadableDatabase();
        //  String select_all="Select * from "+DATEBASE_TABLE1;
        try  {
            Cursor cur = mSqliteDatebase.rawQuery("Select * from " + DATEBASE_TABLE1, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        if (cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE)).equals(knjiga.getNaziv()) &&
                                cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE1)).equals(knjiga.getOpis())
                                && cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE2)).equals(knjiga.getDatumObjavljivanja())
                                && cur.getInt(cur.getColumnIndexOrThrow(NAZIV_KOLONE3)) == knjiga.getBrojStranica()
                                && cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE4)).equals(knjiga.getId())
                                && cur.getInt(cur.getColumnIndexOrThrow(NAZIV_KOLONE5)) == knjiga.getKategorija1()
                                && cur.getString(cur.getColumnIndexOrThrow(NAZIV_KOLONE6)).equals(knjiga.getSlika().toString())
                                && cur.getInt(cur.getColumnIndexOrThrow(NAZIV_KOLONE7)) == knjiga.isSelektovana()) {

                        }
                        return cur.getInt(cur.getColumnIndexOrThrow(ID));
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
    public long dodajKnjigu(Knjiga knjiga){
        long a=dodajKnjiguPom(knjiga);
        if(a==-1){
            try{
                mSqliteDatebase=this.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put(NAZIV_KOLONE, knjiga.getNaziv());
                values.put(NAZIV_KOLONE1, knjiga.getOpis());
                values.put(NAZIV_KOLONE2, knjiga.getDatumObjavljivanja());
                values.put(NAZIV_KOLONE3, knjiga.getBrojStranica());
                values.put(NAZIV_KOLONE4, knjiga.getId());
                values.put(NAZIV_KOLONE5, knjiga.getKategorija1());
                values.put(NAZIV_KOLONE6, knjiga.getSlika().toString());
                values.put(NAZIV_KOLONE7, knjiga.isSelektovana());
                mSqliteDatebase.insert(DATEBASE_TABLE1, null, values);
               /* for(Autor aa:knjiga.getAutori()){
                    dodajAutora(aa.getImeiPrezime());
                    dodajAutorstvo(daLiPostojiAutor(aa.getImeiPrezime()),a);
                }*/
                mSqliteDatebase.close();
            }
            catch (SQLException e)
            {

            }
        }
        return a;
    }
}
