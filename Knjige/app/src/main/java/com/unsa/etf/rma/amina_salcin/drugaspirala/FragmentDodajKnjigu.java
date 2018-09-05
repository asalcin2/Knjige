package com.unsa.etf.rma.amina_salcin.drugaspirala;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class     FragmentDodajKnjigu extends Fragment {
    BazaOpenHelper1 b;
    URL u;
    public interface ZaPonisti {
        public void ppon(ArrayList<Knjiga> kk);
        public void ppon1(ArrayList<Knjiga> kk);
    }
    public static final int PICK_IMAGE = 1;
    public String naslovzasliku="f";
    EditText naslovKnjige;
    ArrayList<Knjiga> knjige=new ArrayList<Knjiga>();
    Bitmap zaSliku;
    ImageView slika;
        private static final String A="AKSDKMDKMDKDK";
   private  ArrayList<String> kategorije=new ArrayList<String>();
    boolean daLijeImageviewPrazan;
    public   Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =getContext().getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
    private ZaPonisti inter;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        inter=( ZaPonisti) getActivity();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode ==PICK_IMAGE  && resultCode == Activity.RESULT_OK) {
            if (data == null)
            {
                daLijeImageviewPrazan=false;
                return;
            }
            ContentResolver contentResolver = getContext().getContentResolver();
            try {
                InputStream inputStream = contentResolver.openInputStream(data.getData());
                FileOutputStream outputStream;
                if(!naslovKnjige.getText().toString().equals(""))naslovzasliku=naslovKnjige.getText().toString();
                outputStream = getContext().openFileOutput(naslovzasliku, Context.MODE_PRIVATE);
                getBitmapFromUri(data.getData()).compress(Bitmap.CompressFormat.JPEG,90,outputStream);
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                zaSliku=getBitmapFromUri(data.getData());
                slika.setImageBitmap(BitmapFactory.decodeStream(getContext().openFileInput(naslovzasliku)));
                daLijeImageviewPrazan=true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=new BazaOpenHelper1(getContext());
    }

    public FragmentDodajKnjigu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_dodaj_knjigu, container, false);

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
        if (b != null) {
            if (b.containsKey("listakat")) {
                kategorije=b.getStringArrayList("listakat");
            }
            if(b.containsKey("listakat1")){
               // knjige=b.getParcelableArrayList("listakat1");
            }
        }

        knjige=KategorijeAkt.db.knjige();
        slika=(ImageView)getView().findViewById(R.id.naslovnaStr);
        daLijeImageviewPrazan=false;
        final Button nadjiSliku=(Button)getView().findViewById(R.id.dNadjiSliku);
        naslovKnjige=(EditText)getView().findViewById(R.id.nazivKnjige);
        final EditText imeAutora=(EditText)getView().findViewById(R.id.imeAutora);
        final Spinner spiner = (Spinner)getView().findViewById(R.id.sKategorijaKnjige);
        final Button dodajKnjigu=(Button)getView().findViewById(R.id.dUpisiKnjigu);
        final Button ponisti=(Button)getView().findViewById(R.id.ponistiKnjigu);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, kategorije);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner.setAdapter(adapter);

        slika.setImageResource(R.drawable.slikapocetna);
        ponisti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inter.ppon(knjige);
            }
        });
        dodajKnjigu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(  naslovKnjige.getText().toString().equals("") || imeAutora.getText().toString().equals("")|| spiner.getCount()==0  || daLijeImageviewPrazan==false){
                    Toast.makeText(getContext(), R.string.zaToast, Toast.LENGTH_SHORT).show();
                }else {
                    String autor=imeAutora.getText().toString();
                    Autor a=new Autor(autor, "Nepoznata informacija");
                    ArrayList<Autor>aa=new ArrayList<Autor>();
                    aa.add(a);
                    Knjiga kk= null;
                    try {
                        kk = new Knjiga("Nepoznata informacija", naslovKnjige.getText().toString(), aa, "Nepoznata informacija", "Nepoznata informacija",
                                new URL("http://2.bp.blogspot.com/-d9XGJtm2Nqg/VlSlvaE3iNI/AAAAAAAAAes/us4XZUA4_wg/s1600/mystery-book-300x225.jpg"), 0);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    kk.setKategorija1(KategorijeAkt.db.index(spiner.getSelectedItem().toString()));
                    kk.setKategorija(spiner.getSelectedItem().toString());
                    kk.setStaraKnjiga(true);
                    kk.setIdSlike(zaSliku);
                    KategorijeAkt.db.dodajKnjigu(kk);
                    knjige.add(kk);
                    naslovKnjige.setText("");
                    imeAutora.setText("");
                    slika.setImageResource(R.drawable.slikapocetna);
                    daLijeImageviewPrazan=false;
                }

            }
        });
        nadjiSliku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if (intent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivityForResult(Intent.createChooser(intent, "Izaberi izvor"), PICK_IMAGE);
                }
            }
        });
    }




}
