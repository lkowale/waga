package com.waga;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


public class ledControl extends ActionBarActivity {

    Button btnTara,btnSkala,btnRozlacz,btnPomiar;

    TextView tvWaga,tvPredkosc,tvInfo;
    TextView tvPetli,tvPamiec, tvWagaR;
    TextView tvLong,tvLati,tvDist;
    TextView tvOdlegl,tvDawka,tvRoznWagi;
    GPSLocator lgps;
    ArduinoEvent activeEvent=null;
//    TextFragment activeFragment;
    String address = null;

    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ConnectedThread mConnectedThread;
    private int mState;
    private static final String TAG = "MainActivity";

    // Constants that indicate the current connection state
    public static final int STATE_NONE = 0;       // we're doing nothing
    public static final int STATE_LISTEN = 1;     // now listening for incoming connections
    public static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    public static final int STATE_CONNECTED = 3;  // now connected to a remote device
    boolean connect=false;
    private ProgressDialog progress;

    Handler timerHandler = new Handler();
    Runnable   timerRunnable = new Runnable() {

        @Override
        public void run() {
            tvPredkosc.setText(String.valueOf(lgps.getSpeed()));
            timerHandler.postDelayed(this, 1100);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(com.waga.DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device

        //view of the ledControl
        setContentView(R.layout.activity_led_control);

        //call the widgtes
        btnTara = (Button)findViewById(R.id.button2);
        btnSkala = (Button)findViewById(R.id.button3);
        btnRozlacz= (Button)findViewById(R.id.button4);
        btnPomiar = (Button)findViewById(R.id.button5);

        tvWaga = (TextView)findViewById(R.id.textView2);
        tvPredkosc = (TextView)findViewById(R.id.textView4);
        tvInfo = (TextView)findViewById(R.id.textView6);
        tvPetli= (TextView)findViewById(R.id.textView7);
        tvPamiec= (TextView)findViewById(R.id.textView8);
        tvLong= (TextView)findViewById(R.id.textView10);
        tvLati= (TextView)findViewById(R.id.textView12);
        tvWagaR= (TextView)findViewById(R.id.textView11);
        tvDist= (TextView)findViewById(R.id.textView9);
        tvDawka= (TextView)findViewById(R.id.textView13);
        tvOdlegl = (TextView)findViewById(R.id.textView15);
        tvRoznWagi= (TextView)findViewById(R.id.textView16);

        new ConnectBT().execute(); //Call the class to connect

        //config
        SharedPreferences sharedPref = this.getSharedPreferences("PREFER",Context.MODE_PRIVATE);
//        int defaultValue = getResources().getInteger(R.integer.saved_high_score_default_key);
        sharedPref.getInt("szerokosc",15);

        lgps= new GPSLocator(this,tvLong,tvLati,tvDist,sharedPref.getInt("szerokosc",15));

//        btnOk.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                enterCommand();      //method to turn on
//            }
//        });

        btnRozlacz.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {

                try
                {
                    btSocket.getOutputStream().write("go#".toString().getBytes());
                }
                catch (IOException e)
                {
                    msg("Error");
                }
                timerHandler.postDelayed(timerRunnable, 1000);
                btnRozlacz.setText("Rozłącz");
                btnRozlacz.setVisibility(View.GONE);
                connect=true;
//            Timer timer = new Timer();
//            TimerTask t = new TimerTask() {
//                @Override
//                public void run() {
//
//                    tvPredkosc.setText(String.valueOf(lgps.getSpeed()));
//                }
//            };
//            timer.scheduleAtFixedRate(t,1000,1000);
        }
    });

        btnTara.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                activeEvent = new TarujWage();
                activeEvent.init(tvInfo,btSocket);
            }
        });

        btnSkala.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
//                SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("PREFER",Context.MODE_PRIVATE);
//                int waga=sharedPref.getInt("waga",500);
//
//                if (btSocket!=null) //If the btSocket is busy
//                {
//                    try
//                    {
//                        btSocket.getOutputStream().write(("k1"+String.valueOf(waga)+"#").toString().getBytes());
//
//                    }
//                    catch (IOException e)
//                    { msg("Error");}
//                }

                    SharedPreferences sharedPref = getApplicationContext().getSharedPreferences("PREFER",Context.MODE_PRIVATE);
//        int defaultValue = getResources().getInteger(R.integer.saved_high_score_default_key);

                    int waga=sharedPref.getInt("waga",500);
                    activeEvent = new Kalibruj();
                    activeEvent.init(tvInfo, btSocket);
                    activeEvent.waga_kalibracji=waga;
                    activeEvent.parse("k3","k3");
                }
//                if(activeEvent instanceof Kalibruj)
//                {
//                    if(activeEvent.indeks==1)
//                        activeEvent.parse("k3","k3");
//                    if(activeEvent.indeks==2)
//                        activeEvent.parse("k6","k6");
//                }

        });

        btnPomiar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (lgps.mierzy)
                {
                    //zakoncz pomiar
                    btnPomiar.setText("Start");
                    tvDawka.setText(String.valueOf(lgps.zakonczPomiar(Integer.valueOf(tvWaga.getText().toString()))));
                    tvOdlegl.setText("Droga: "+String.valueOf(Math.round(lgps.odleglosc)));
                    tvRoznWagi.setText("Waga: "+String.valueOf(lgps.roznica_wagi));
                }
                else
                {
                    //rozpocznij pomiar
                    btnPomiar.setText("Stop");
                    lgps.rozpocznijPomiar(Integer.valueOf(tvWaga.getText().toString()));
                    tvOdlegl.setText("Droga: 0");
                    tvRoznWagi.setText("Waga: 0");
                }
            }
        });
//        contactList = new ArrayList<User>();
//
//        adapter = new UsersAdapter(this, contactList);
//        lv = (ListView) findViewById(R.id.listview1);
//        lv.setAdapter(adapter);


        }
    @Override
    protected void onPause(){
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        Disconnect();
    }
    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.getOutputStream().write("stop#".toString().getBytes());
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout

    }

//    private void turnOffLed()
//    {
//        if (btSocket!=null)
//        {
//            try
//            {
//                btSocket.getOutputStream().write("tf".toString().getBytes());
//            }
//            catch (IOException e)
//            {
//                msg("Error");
//            }
//        }
//    }
//
//    private void enterCommand()
//    {
//
//        String input = editT.getText().toString();
//        editT.setText("");
//
//        if(input.startsWith("taruj")) {
//            activeEvent = new com.waga.TarujWage();
//            activeEvent.init(textV,btSocket);
//        }
//
//        if(input.startsWith("kalibruj")) {
//            activeEvent = new com.waga.Kalibruj();
//            activeEvent.init(textV,btSocket);
//        }
//        if(input.startsWith("ok")){
//            activeEvent.parse("k3","k3");
//        }
//        if(input.startsWith("kk")){
//            activeEvent.parse("k6","k6");
//        }
//        if(input.startsWith("kalwal")) {
//            activeEvent = new com.waga.KalibrujWalek();
//            activeEvent.init(textV,btSocket);
//        }
//

//        if (btSocket!=null)
//        {
//            try
//            {
////          przetraw komende
//
//                btSocket.getOutputStream().write("{\"dawka\":\"8000\"}".toString().getBytes());
//            }
//            catch (IOException e)
//            {
//                msg("Error");
//            }
//        }
//    }

    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_led_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void otworzOpcje(View view) {
        Intent intent=new Intent(this,OptionsActivity.class);
        startActivity(intent);
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(ledControl.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                 myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                 BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                 btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                 BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                 btSocket.connect();//start connection
                    // Start the thread to manage the connection and perform transmissions
                    mConnectedThread = new ConnectedThread(btSocket);
                    mConnectedThread.start();
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }


private class ConnectedThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;

    public ConnectedThread(BluetoothSocket socket) {

        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the BluetoothSocket input and output streams
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {

        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
        mState = STATE_CONNECTED;
    }

    public void run() {

        byte[] buffer = new byte[1024];
        byte[] toSend = new byte [1024];
        int bytes=0,begin=0;

        // Keep listening to the InputStream while connected
        while (mState == STATE_CONNECTED) {
            try {
                bytes += mmInStream.read(buffer, bytes, buffer.length - bytes);
                for(int i = begin; i < bytes; i++) {
                    if (buffer[i] == "#".getBytes()[0]) {
                        System.arraycopy(buffer, begin, toSend, 0,i-begin);
                        // mHandler.obtainMessage(1, begin, i, buffer).sendToTarget();
//                        String s = new String(buffer,begin,i-begin);
                       mHandler.obtainMessage(com.waga.Constants.MESSAGE_READ, i-begin, -1, toSend)
                                .sendToTarget();

//                        response.setText(buffer, begin, i-begin);

                        begin = i + 1;
                        if (i == bytes - 1) {
                            bytes = 0;
                            begin = 0;
                        }
                    }
                }
//                    // Read from the InputStream
//                    bytes = mmInStream.read(buffer);
//
//                    // Send the obtained bytes to the UI Activity
//                    mHandler.obtainMessage(Constants.MESSAGE_READ, bytes, -1, buffer)
//                            .sendToTarget();
            } catch (IOException e) {

//                connectionLost();
                break;
            }
        }
    }

    /**
     * Write to the connected OutStream.
     *
     * @param buffer The bytes to write
     */
    public void write(byte[] buffer) {
        try {
            mmOutStream.write(buffer);

        } catch (IOException e) {

        }
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {

        }
    }
}
    /**
     * The Handler that gets information back from the BluetoothChatService
     */
    private final Handler mHandler = new Handler() {
        JSONObject json;
        @Override
        public void handleMessage(Message msg) {
//            FragmentActivity activity = getActivity();
            switch (msg.what) {
//                case Constants.MESSAGE_STATE_CHANGE:
//                    switch (msg.arg1) {
//                        case BluetoothChatService.STATE_CONNECTED:
//                            setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
//                            mConversationArrayAdapter.clear();
//                            break;
//                        case BluetoothChatService.STATE_CONNECTING:
//                            setStatus(R.string.title_connecting);
//                            break;
//                        case BluetoothChatService.STATE_LISTEN:
//                        case BluetoothChatService.STATE_NONE:
//                            setStatus(R.string.title_not_connected);
//                            break;
//                    }
//                    break;
//                case Constants.MESSAGE_WRITE:
//                    byte[] writeBuf = (byte[]) msg.obj;
//                    // construct a string from the buffer
//                    String writeMessage = new String(writeBuf);
//                    mConversationArrayAdapter.add("Me:  " + writeMessage);
//
//                    break;
                case com.waga.Constants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
//                    response.setText(readMessage);
//                        contactList.clear();
                    try{
//                        JSONObject json =new JSONObject("{\"waga\":\"3222\",\"dystans\":\"3223\",\"predkosc\":\"15.7\"}#");
                        JSONObject json =new JSONObject(readMessage);
                        //jesli jest oboekt comm jest to polecnie  do wykonania przez aktywny obiekt 
//                        if(json.has("c")) {
//                            if (json.getString("c").equals("kw3"))
//                                activeEvent.parse(json.getString("c"), json.getString("val"), json.getString("val1"));
//                            else
//                                activeEvent.parse(json.getString("c"), json.getString("val"));
//                        }
//                        else
//                            {
//                            for (Iterator<String> iter = json.keys(); iter.hasNext(); ) {
//                                String key = iter.next();
//                                String value = json.getString(key);
//                                User contact = new User(key, value);
//                                // adding contact to contact list
//                                contactList.add(contact);
//                            }
//                            adapter.notifyDataSetChanged();
//                        }


                        if(json.getString("c").equals("i")){
                            tvWaga.setText(json.getString("1"));
                            tvPamiec.setText("Pamięć:"+json.getString("2"));
                            tvPetli.setText("Pętli:"+json.getString("3"));
                            tvWagaR.setText("WagaR:"+json.getString("4"));

                        }
                        else activeEvent.parse(json.getString("c"),json.getString("1"));

//                        if(json.getString("c").equals("t2")) activeEvent.parse();


                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
//                        textV.append("Error parsing:"+readMessage);
//                        textV.append(System.getProperty("line.separator"));

//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(getApplicationContext(),
//                                        "Json parsing error: " + e.getMessage(),
//                                        Toast.LENGTH_LONG).show();
//
//                            }
//                        });

                    }

//                    try{
//                        json =new JSONObject(readMessage);
//                        for(Iterator<String> iter = json.keys(); iter.hasNext();) {
//                            String key = iter.next();
//                            String value= json.getString(key);
//
//                            HashMap<String, String> contact = new HashMap<>();
//
//                            // adding each child node to HashMap key => value
//                            contact.put("name", key);
//                            contact.put("value", value);
//
//
//                            // adding contact to contact list
//                            contactList.add(contact);
//                        }
////                        adapter.notifyData
//                        } catch (final JSONException e) {
//                            Log.e(TAG, "Json parsing error: " + e.getMessage());
//                            runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(getApplicationContext(),
//                                            "Json parsing error: " + e.getMessage(),
//                                            Toast.LENGTH_LONG).show();
//                                }
//                            });
//
//                        }


//                    mConversationArrayAdapter.add(mConnectedDeviceName + ":  " + readMessage);
//                    writeToBuffer(readMessage);
//                    writeToFile(readMessage);
//                    writeToFile1(readMessage,getActivity());
                    break;
//                case Constants.MESSAGE_DEVICE_NAME:
//                    // save the connected device's name
//                    mConnectedDeviceName = msg.getData().getString(Constants.DEVICE_NAME);
//                    if (null != activity) {
//                        Toast.makeText(activity, "Connected to "
//                                + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
//                    }
//                    break;
//                case Constants.MESSAGE_TOAST:
//                    if (null != activity) {
//                        Toast.makeText(activity, msg.getData().getString(Constants.TOAST),
//                                Toast.LENGTH_SHORT).show();
//                    }
//                    break;
            }
        }
    };

}