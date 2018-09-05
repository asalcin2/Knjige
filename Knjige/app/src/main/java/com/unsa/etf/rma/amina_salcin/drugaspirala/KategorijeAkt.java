package com.unsa.etf.rma.amina_salcin.drugaspirala;

//import android.app.Fragment;
//import android.app.FragmentManager;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.app.ListFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;

public class KategorijeAkt extends AppCompatActivity implements ListeFragment.NaKlik, FragmentDodajKnjigu.ZaPonisti, KnjigeFragment.naPrvi, FragmentOnline.naPrvi1 {
    public static BazaOpenHelper db;
    private ListeFragment fragment11;
    private FragmentDodajKnjigu fragment2;
    private  KnjigeFragment knjigeFragment;
    private FragmentOnline fragmentOnline;
    private FragmentPreporuci fragmentPreporuci;
    private boolean siriL=false;

    @Override
    public void onBackPressed() {
    if(siriL==true){
        if(findViewById(R.id.dodatni).getVisibility()==View.VISIBLE && (FragmentDodajKnjigu)getSupportFragmentManager().findFragmentById(R.id.dodatni)!=null)
        {findViewById(R.id.mjestoF1).setVisibility(View.VISIBLE); findViewById(R.id.dodatni).setVisibility(View.INVISIBLE);}
        else if((ListeFragment)getSupportFragmentManager().findFragmentById(R.id.mjestoF1)!=null && findViewById(R.id.dodatni).getVisibility()==View.INVISIBLE
                && findViewById(R.id.mjestoF2).getVisibility()==View.INVISIBLE){
            for(int i=0; i<getSupportFragmentManager().getBackStackEntryCount(); i++) getSupportFragmentManager().popBackStack();
        }
    }
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=new BazaOpenHelper(getApplicationContext());
        //db.deleteDatebase(getBaseContext());
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_kategorije_akt);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FrameLayout ldetalji = (FrameLayout)findViewById(R.id.mjestoF2);
        if (ldetalji != null) {
            siriL = true;
            findViewById(R.id.dodatni).setVisibility(View.INVISIBLE);
            fragment11 = (ListeFragment) fragmentManager.findFragmentByTag("ListeFragment1");
            if (fragment11 == null) {
                fragment11 = new ListeFragment();
                fragmentManager.beginTransaction().replace(R.id.mjestoF1, fragment11, "ListeFragment1").commit();
            } else {
                fragmentManager.popBackStack("ListeFragment1", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        } else {
            fragment11 = (ListeFragment) fragmentManager.findFragmentByTag("ListeFragment");
            if (fragment11 == null) {
                fragment11 = new ListeFragment();
                fragmentManager.beginTransaction().replace(R.id.fragment1, fragment11, "ListeFragment").commit();
            } else {
             //   fragmentManager.beginTransaction().replace(R.id.fragment1, fragment11, "ListeFragment1").commit();
             fragmentManager.popBackStack("ListeFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
    }

    @Override
    public void online(ArrayList<String>listakat1, ArrayList<Knjiga>sve) {
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        if(siriL){
            fragmentOnline=(FragmentOnline) getSupportFragmentManager().findFragmentByTag("FragmentOnline");
            if(fragmentOnline==null){
                findViewById(R.id.dodatni).setVisibility(View.VISIBLE);
                findViewById(R.id.mjestoF1).setVisibility(View.INVISIBLE);
                findViewById(R.id.mjestoF1).setVisibility(View.INVISIBLE);
                fragmentOnline=new FragmentOnline();
                Bundle arguments = new Bundle();
                arguments.putStringArrayList("listakat", listakat1);
                arguments.putParcelableArrayList("sveknjige", sve);
                fragmentOnline.setArguments(arguments);
                ft.replace(R.id.dodatni, fragmentOnline, "FragmentOnline").addToBackStack("FragmentOnline");
                //fragmentOnline.postaviKategorije1();
                ft.commit();
            }
            else {
                findViewById(R.id.dodatni).setVisibility(View.VISIBLE);
                findViewById(R.id.mjestoF1).setVisibility(View.INVISIBLE);
                findViewById(R.id.mjestoF2).setVisibility(View.INVISIBLE);
                Bundle arguments = new Bundle();
                arguments.putStringArrayList("listakat", listakat1);
                arguments.putParcelableArrayList("sveknjige", sve);
                fragmentOnline.setArguments(arguments);
                getSupportFragmentManager().beginTransaction().replace(R.id.dodatni, fragmentOnline, "FragmentOnline").commit();
                ft.replace(R.id.dodatni, fragmentOnline, "FragmentOnline").addToBackStack("FragmentOnline");
             //   fragmentOnline.postaviKategorije(listakat1);
                ft.commit();

            }

        }
        else {
            fragmentOnline=(FragmentOnline) getSupportFragmentManager().findFragmentByTag("FragmentOnline1");
            if(fragmentOnline==null){
                fragmentOnline=new FragmentOnline();
                Bundle arguments = new Bundle();
                arguments.putStringArrayList("listakat", listakat1);
                arguments.putParcelableArrayList("sveknjige", sve);
                fragmentOnline.setArguments(arguments);
                ft.replace(R.id.fragment1, fragmentOnline, "FragmentOnline1").addToBackStack("FragmentOnline1");
             //   fragmentOnline.postaviKategorije1();
                ft.commit();
            }
            else {
                Bundle arguments = new Bundle();
                arguments.putStringArrayList("listakat", listakat1);
                arguments.putParcelableArrayList("sveknjige", sve);
                fragmentOnline.setArguments(arguments);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, fragmentOnline, "FragmentOnline1").commit();
                //   getSupportFragmentManager().popBackStack("DodajKnjigu1", FragmentManager.POP_BACK_STACK_INCLUSIVE);
              //  fragmentOnline.postaviKategorije(listakat1);

            }
        }
    }

    @Override
    public void naKLik(ArrayList<String> listakat, ArrayList<Knjiga> knjige) {
        FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
        if(siriL){
            fragment2=(FragmentDodajKnjigu) getSupportFragmentManager().findFragmentByTag("DodajKnjigu");
            if(fragment2==null){
                findViewById(R.id.dodatni).setVisibility(View.VISIBLE);
                findViewById(R.id.mjestoF1).setVisibility(View.INVISIBLE);
                findViewById(R.id.mjestoF1).setVisibility(View.INVISIBLE);
                fragment2=new FragmentDodajKnjigu();
                Bundle arguments = new Bundle();
                arguments.putStringArrayList("listakat", listakat);
                arguments.putParcelableArrayList("listakat1", knjige);
                fragment2.setArguments(arguments);
                ft.replace(R.id.dodatni, fragment2, "DodajKnjigu").commit();
            }
            else {
                findViewById(R.id.dodatni).setVisibility(View.VISIBLE);
                findViewById(R.id.mjestoF1).setVisibility(View.INVISIBLE);
                findViewById(R.id.mjestoF2).setVisibility(View.INVISIBLE);
                Bundle arguments = new Bundle();
                arguments.putStringArrayList("listakat", listakat);
                arguments.putParcelableArrayList("listakat1", knjige);
                fragment2.setArguments(arguments);
                getSupportFragmentManager().popBackStack("DodajKnjigu", FragmentManager.POP_BACK_STACK_INCLUSIVE);

            }

        }
        else {
            fragment2=(FragmentDodajKnjigu) getSupportFragmentManager().findFragmentByTag("DodajKnjigu1");
            if(fragment2==null){
                fragment2=new FragmentDodajKnjigu();
                Bundle arguments = new Bundle();
                arguments.putStringArrayList("listakat", listakat);
                arguments.putParcelableArrayList("listakat1", knjige);
                fragment2.setArguments(arguments);
                ft.replace(R.id.fragment1, fragment2, "DodajKnjigu1").commit();
            }
            else {
                Bundle arguments = new Bundle();
                arguments.putStringArrayList("listakat", listakat);
                arguments.putParcelableArrayList("listakat1", knjige);
                fragment2.setArguments(arguments);
               getSupportFragmentManager().popBackStack("DodajKnjigu1", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }

    }


    @Override
    public void KlikNaKategoriju(ArrayList<Knjiga> knjigeIzKat, String nazivkat) {
        knjigeFragment=(KnjigeFragment) getSupportFragmentManager().findFragmentByTag("KnjigeFragment");
        if(siriL){
            findViewById(R.id.mjestoF2).setVisibility(View.VISIBLE);
               if(knjigeFragment!=null){
                   Bundle bund=new Bundle();
                   bund.putString("Item", nazivkat);
                   bund.putParcelableArrayList("knjigeZaKategoriju", knjigeIzKat);
                   knjigeFragment.setArguments(bund);
                   getSupportFragmentManager().popBackStack( "KnjigeFragment", FragmentManager.POP_BACK_STACK_INCLUSIVE);

            }
            else {
                Bundle bund=new Bundle();
                bund.putString("Item", nazivkat);
                bund.putParcelableArrayList("knjigeZaKategoriju", knjigeIzKat);
                knjigeFragment=new KnjigeFragment();
                knjigeFragment.setArguments(bund);
                FragmentTransaction fr= getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.mjestoF2, knjigeFragment, "KnjigeFragment");
              //  fr.addToBackStack("KnjigeFragment");
                fr.commit();
              //  knjigeFragment.postaviKnjige1();
            }

        }
        else{
            if(getSupportFragmentManager().findFragmentByTag("KnjigeFragment1")!=null){
                Bundle bund=new Bundle();
                bund.putString("Item", nazivkat);
                bund.putParcelableArrayList("knjigeZaKategoriju", knjigeIzKat);
                knjigeFragment.setArguments(bund);
                getSupportFragmentManager().popBackStack("KnjigeFragment1", FragmentManager.POP_BACK_STACK_INCLUSIVE);

               // getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, knjigeFragment, "KnjigeFragment1").commit();
              //  knjigeFragment.postavii(nazivkat, knjigeIzKat);
            }
            else {
                knjigeFragment=new KnjigeFragment();
                Bundle bund=new Bundle();
                bund.putString("Item", nazivkat);
                bund.putParcelableArrayList("knjigeZaKategoriju", knjigeIzKat);
                knjigeFragment.setArguments(bund);
                FragmentTransaction fr= getSupportFragmentManager().beginTransaction();
                fr.replace(R.id.fragment1, knjigeFragment, "KnjigeFragment1");
              // fr.addToBackStack("KnjigeFragment1");
                fr.commit();
              //  knjigeFragment.postaviKnjige1();
            }

        }
    }

    @Override
    public void daNestaneMjestoF2() {
        if(siriL)
        findViewById(R.id.mjestoF2).setVisibility(View.INVISIBLE);
    }



    @Override
    public void ppon(ArrayList<Knjiga> kk) {
        if(siriL){
            findViewById(R.id.dodatni).setVisibility(View.INVISIBLE);
            findViewById(R.id.mjestoF1).setVisibility(View.VISIBLE);
            findViewById(R.id.mjestoF2).setVisibility(View.INVISIBLE);
            fragment11=(ListeFragment)getSupportFragmentManager().findFragmentByTag("ListeFragment1");
            if(fragment11!=null){
                Bundle arguments = new Bundle();
                arguments.putParcelableArrayList("listak", kk);


               //
                fragment11.setArguments(arguments);
               // getSupportFragmentManager().beginTransaction().replace(R.id.mjestoF1, fragment11, "ListeFragment1").commit();
                getSupportFragmentManager().popBackStack("DodajKnjigu",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                // fragment11.postaviListu(kk);
            }
            else {
                Bundle arguments = new Bundle();
                arguments.putParcelableArrayList("listak", kk);
                ListeFragment fragment11=new ListeFragment();
                fragment11.setArguments(arguments);
                getSupportFragmentManager().beginTransaction().replace(R.id.mjestoF1, fragment11, "ListeFragment1").commit();
              //  fragment11.postaviListu1();
            }
            // z getSupportFragmentManager().popBackStack("dodaj",FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        else {
            fragment11=(ListeFragment)getSupportFragmentManager().findFragmentByTag("ListeFragment");
            if(fragment11!=null){
                Bundle arguments = new Bundle();
                arguments.putParcelableArrayList("listak", kk);
                fragment11.setArguments(arguments);
               // getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, fragment11, "ListeFragment1").commit();
                 getSupportFragmentManager().popBackStack("DodajKnjigu1",FragmentManager.POP_BACK_STACK_INCLUSIVE);

              //  fragment11.postaviListu1();
            }
            else {
                Bundle arguments = new Bundle();
                arguments.putParcelableArrayList("listak", kk);
                ListeFragment fragment11=new ListeFragment();
                fragment11.setArguments(arguments);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, fragment11, "ListeFragment").commit();

             //   fragment11.postaviListu1();
            }
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }



    @Override
    public void ppon1(ArrayList<Knjiga> kk) {
        if(siriL){
            if(fragment11!=null){
                Bundle arguments = new Bundle();
                arguments.putParcelableArrayList("listak", kk);
                fragment11.setArguments(arguments);
            }
            else {
                Bundle arguments = new Bundle();
                arguments.putParcelableArrayList("listak", kk);
                ListeFragment fragment11=new ListeFragment();
                fragment11.setArguments(arguments);
               // fragment11.postaviListu1();
            }
        }
        else {
            if(fragment11!=null){
                Bundle arguments = new Bundle();
                arguments.putParcelableArrayList("listak", kk);
                fragment11.setArguments(arguments);
            }
            else {
                Bundle arguments = new Bundle();
                arguments.putParcelableArrayList("listak", kk);
                ListeFragment fragment11=new ListeFragment();
                fragment11.setArguments(arguments);
               // fragment11.postaviListu1();
            }
        }
    }


    @Override
    public void naPrviFragment(ArrayList<Knjiga>kk) {
        if(siriL){

            fragment11=(ListeFragment)getSupportFragmentManager().findFragmentByTag("ListeFragment1");
            if(fragment11!=null){
                Bundle arguments = new Bundle();
                arguments.putParcelableArrayList("listak", kk);
                fragment11.setArguments(arguments);
                getSupportFragmentManager().popBackStack("KnjigeFragment",FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            else {
                Bundle arguments = new Bundle();
                arguments.putParcelableArrayList("listak", kk);
                ListeFragment fragment11=new ListeFragment();
                fragment11.setArguments(arguments);
                getSupportFragmentManager().popBackStack("KnjigeFragment",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.mjestoF1, fragment11, "ListeFragment1").commit();
               // fragment11.postaviListu1();

            }
        }
        else {
            fragment11=(ListeFragment)getSupportFragmentManager().findFragmentByTag("ListeFragment");
            if(fragment11!=null){

                Bundle arguments = new Bundle();
                arguments.putParcelableArrayList("listak", kk);
                fragment11.setArguments(arguments);
                getSupportFragmentManager().popBackStack("KnjigeFragment1",FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            else {
                Bundle arguments = new Bundle();
                arguments.putParcelableArrayList("listak", kk);
                ListeFragment fragment11=new ListeFragment();
                fragment11.setArguments(arguments);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, fragment11, "ListeFragment").commit();
                //fragment11.postaviListu1();

            }
        }

    }

    @Override
    public void naPrviFragment1(Knjiga k) {
        fragmentPreporuci=(FragmentPreporuci) getSupportFragmentManager().findFragmentByTag("FragmentPreporuci");
        if(fragmentPreporuci==null){
            fragmentPreporuci=new FragmentPreporuci();
            Bundle arguments = new Bundle();
            arguments.putParcelable("knjiga", k);
            arguments.putParcelable("knjiga", k);
            fragmentPreporuci.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, fragmentPreporuci, "FragmentPreporuci").addToBackStack("FragmentPreporuci").commit();
            //   fragmentOnline.postaviKategorije1();
          //  ft.commit();
        }
        else {
            Bundle arguments = new Bundle();
            arguments.putParcelable("knjiga", k);
            arguments.putParcelable("knjiga", k);
            fragmentPreporuci.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, fragmentPreporuci, "FragmentPreporuci").commit();
            //   getSupportFragmentManager().popBackStack("DodajKnjigu1", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            //  fragmentOnline.postaviKategorije(listakat1);

        }
    }

    @Override
    public void naPrviFragment1(ArrayList<Knjiga> kk) {
        if(siriL){

            fragment11=(ListeFragment)getSupportFragmentManager().findFragmentByTag("ListeFragment1");
            if(fragment11!=null){
                Bundle arguments = new Bundle();
                arguments.putParcelableArrayList("listak", kk);
                fragment11.setArguments(arguments);
                getSupportFragmentManager().popBackStack("FragmentOnline1",FragmentManager.POP_BACK_STACK_INCLUSIVE);
               // getSupportFragmentManager().beginTransaction().replace(R.id.mjestoF1, fragment11, "ListeFragment1").commit();

            }
            else {
                Bundle arguments = new Bundle();
                arguments.putParcelableArrayList("listak1", kk);
                ListeFragment fragment11=new ListeFragment();
                fragment11.setArguments(arguments);
               // getSupportFragmentManager().popBackStack("KnjigeFragment",FragmentManager.POP_BACK_STACK_INCLUSIVE);
                getSupportFragmentManager().beginTransaction().replace(R.id.mjestoF1, fragment11, "ListeFragment1").commit();
              //  fragment11.postaviListu1();

            }
        }
        else {
            fragment11=(ListeFragment)getSupportFragmentManager().findFragmentByTag("ListeFragment");
            if(fragment11!=null){
                Bundle arguments = new Bundle();
                arguments.putParcelableArrayList("listak", kk);
                fragment11.setArguments(arguments);
                getSupportFragmentManager().popBackStack("FragmentOnline1",FragmentManager.POP_BACK_STACK_INCLUSIVE);

            }
            else {
                Bundle arguments = new Bundle();
                arguments.putParcelableArrayList("listak1", kk);
                ListeFragment fragment11=new ListeFragment();
                fragment11.setArguments(arguments);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment1, fragment11, "ListeFragment").commit();

            }
        }
    }
}
