package com.unsa.etf.rma.amina_salcin.drugaspirala;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import java.util.ArrayList;

/**
 * Created by Amina on 5/19/2018.
 */

public class MojResultReceiver extends ResultReceiver {
    public interface Receiver{
        public void onReceiverResult(int resultCode, Bundle resultData);
    }
    public Receiver getmReceiver() {
        return mReceiver;
    }

    public void setmReceiver(Receiver mReceiver) {
        this.mReceiver = mReceiver;
    }

    private Receiver mReceiver;
    public MojResultReceiver( Handler handler){
        super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if(mReceiver!=null){
            mReceiver.onReceiverResult(resultCode, resultData);
        }
    }


}
