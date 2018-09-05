package com.unsa.etf.rma.amina_salcin.drugaspirala;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Amina on 5/15/2018.
 */

public class NetworkUtils {
  //  private static final String LOG_TAG=NetworkUtils.class.getSimpleName();
    private static final String BOOK_BASE_URL="https://www.googleapis.com/books/v1/volumes?q=intitle:";
    private static final String QUERY_Param="q";
    private static final String MAX_RESULTS="maxResults";
    private static final String PRINT_TYPE="printType";
    static String getBookInfo(String queryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJSONString = null;
        try {
            String query = null;
            try {
                query = URLEncoder.encode(queryString, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            URL requestURL= new URL("https://www.googleapis.com/books/v1/volumes?q=intitle:"+query+"&maxResults=5&orderBy=relevance");
      //      Log.d(LOG_TAG, requestURL.toString());
            urlConnection=(HttpURLConnection)requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream;
            try{
                inputStream=urlConnection.getInputStream();
                StringBuffer buffer=new StringBuffer();
                if(inputStream==null){
                    return null;
                }
                reader=new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while((line=reader.readLine())!=null){
                    buffer.append(line+"\n");
                }
                if(buffer.length()==0){
                    return null;
                }
                bookJSONString=buffer.toString();
            }
            catch (FileNotFoundException e){
            //    Log.d(LOG_TAG, "poludio");
                return  null;
            }
            catch(Exception e){
                e.printStackTrace();
                return null;
            }

        }
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
        finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(reader!=null){
                try{
                    reader.close();
                }
                catch (IOException e){
                    e.printStackTrace();
                }
            }
            return  bookJSONString;
        }

    }


}
