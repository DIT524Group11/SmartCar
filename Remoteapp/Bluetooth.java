package com.example.anita.acceleration;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


public class Bluetooth extends Activity  {

    public static OutputStream mmOutStream;
    //Button forward, send;
    //TextView myLabel;
    // EditText myTextbox;
    Button b1;
    EditText ed1;
    private WebView wv1;

   public static BluetoothAdapter mBluetoothAdapter;
    public static BluetoothSocket mmSocket;
    public static BluetoothDevice mmDevice;
    public static OutputStream mmOutputStream;
    public static InputStream mmInputStream;
    public static  Thread workerThread;

    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;



    void findBT() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {


        }

       else if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals("Group 11")) {
                    mmDevice = device;

                    break;
                }
            }
        }

    }
    static void instruction (String message) {
        byte[] toSend = message.getBytes();
        try {
            if (mBluetoothAdapter == null) { Log.d("Null", "null"); } else {
                mmOutStream.write(toSend); }
        }
        catch (IOException e){
            Log.d("Instruction", "Instruction");
        }
    }
    void openBT() throws IOException {
        {

            try {
                UUID applicationUUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
                if (mmDevice == null)
                {
                    Log.d("NULL", "mmdevice is NULL");
                }
                BluetoothSocket socket = mmDevice.createRfcommSocketToServiceRecord(applicationUUID);
     Log.d("Wth", " lol");
                socket.connect();
                Log.d("Wth", " lol");
                mmOutStream = socket.getOutputStream();



            } catch (IOException e) {
                Log.e("ERROR", "Exception during write", e);
            }
        }

        beginListenForData();


    }

    void beginListenForData() {
        final Handler handler = new Handler();
        final byte delimiter = 10;

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                    try {
                        int bytesAvailable = 0;
                        if (bytesAvailable > 0) {
                            byte[] packetBytes = new byte[bytesAvailable];
                            mmInputStream.read(packetBytes);
                            for (int i = 0; i < bytesAvailable; i++) {
                                byte b = packetBytes[i];
                                if (b == delimiter) {
                                    byte[] encodedBytes = new byte[readBufferPosition];
                                    System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
                                    final String data = new String(encodedBytes, "US-ASCII");
                                    readBufferPosition = 0;

                                    handler.post(new Runnable() {
                                        public void run() {
                                        }
                                    });
                                } else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    }    catch (IOException ex) {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start()   ;
    }


    }
