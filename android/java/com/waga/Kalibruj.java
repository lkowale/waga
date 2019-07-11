package com.waga;

import android.bluetooth.BluetoothSocket;
import android.widget.TextView;

import com.waga.ArduinoEvent;

import java.io.IOException;

/**
 * Created by pandejo on 05.01.2018.
 */

public class Kalibruj extends ArduinoEvent {



    void init(TextView t, BluetoothSocket btS){
        super.init(t,btS);

        indeks=0;
       // printLine("Skalowanie do "+ String.valueOf(waga_kalibracji)+" kg");

//        try
//        {
////            btSocket.getOutputStream().write("k1#".toString().getBytes());
//
//        }
//        catch (IOException e)
//        {
////                msg("Error");
//        }
    }

    void parse(String kod,String s){
        if(kod.equals("k2")){

            indeks=1;
//            printLine("Postaw na wagę: "+s+"[g] i  wcisnij Skala");
            printLine("Wsyp: "+String.valueOf(waga_kalibracji)+"[kg] i wcisnij Skala");

        }
        if(kod.equals("k3"))
        {
            try
            {
                btSocket.getOutputStream().write(("k1"+String.valueOf(waga_kalibracji)+"#").toString().getBytes());
            }
            catch (IOException e)
            {
//                msg("Error");
            }
        }

        if(kod.equals("k5")) {
            printLine("Przeskalowano, wsp.wagi: " + s );
            indeks=2;
        }
        if(kod.equals("k6"))
            try
            {
                btSocket.getOutputStream().write("k6#".toString().getBytes());
            }
            catch (IOException e)
            {
//                msg("Error");
            }
        if(kod.equals("k7"))
            printLine("Wytarowano wagę, offset: "+s);
    }
}
