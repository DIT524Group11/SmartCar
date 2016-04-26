package com.g11.rover.roverapp;

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
        import android.widget.Button;

        import com.google.android.gms.appindexing.AppIndex;
        import com.google.android.gms.common.api.GoogleApiClient;

        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;
        import java.util.Set;
        import java.util.UUID;

public class RemoteC extends Activity {
    Button openButton;
    Button Foward;
    Button Reverse;
    Button Stop;
    Button Right;
    Button Left;
    private OutputStream mmOutStream;
    //Button forward, send;
    //TextView myLabel;
    // EditText myTextbox;

    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    private Camera cam;
    byte[] readBuffer;
    int readBufferPosition;
    int counter;
    volatile boolean stopWorker;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        WebView view = new WebView(this);
        setContentView(view);
        view.loadUrl("http://hackerman.nu");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_c);

       // cam = new Camera();

        openButton = (Button) findViewById(R.id.btBluetooth);
        Foward = (Button) findViewById(R.id.btForward);
        Reverse = (Button) findViewById(R.id.btReverse);
        Right = (Button) findViewById(R.id.btRight);
        Left = (Button) findViewById(R.id.btLeft);
        Stop = (Button) findViewById(R.id.btStop);
        //Button closeButton = (Button)findViewById(R.id.close);
        //TextView myLabel = (TextView)findViewById(R.id.label);


        //Open Button
        openButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Log.d("WORK", "work");
                    findBT();
                    openBT();
                } catch (IOException ex) {
                }
            }
        });

        //Send Button
        Foward.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                    instruction("m");



            }
        });

        Reverse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                instruction("b");
            }
        });

        Right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                instruction("r");
            }
        });

        Left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                instruction("l");
            }
        });


        Stop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                instruction("s");
            }
        });
        //Close button
        //  closeButton.setOnClickListener(new View.OnClickListener() {
        //  public void onClick(View v) {
        //   try {
        //     closeBT();
        //   } catch (IOException ex) {
        //    }
        //  }
        //  });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    void findBT() {
        Log.d("work1", "work1");
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {

            Log.d("Bnull", "Bnull");

        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
            Log.d("Enabled", "Bluetooth was enabled");
        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            Log.d("DeviceB", "There is a paired device");
            for (BluetoothDevice device : pairedDevices) {
                Log.d("Looptest", "loop");
                if (device.getName().equals("Group 11")) {
                    Log.d("Hey", "Hey");
                    mmDevice = device;
                    break;
                }
            }
        }
        // myLabel.setText("Bluetooth Device Found");
    }
    void instruction (String message) {
        byte[] toSend = message.getBytes();
        try {
            mmOutStream.write(toSend);
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

               socket.connect();
                 mmOutStream = socket.getOutputStream();


                // Your Data is sent to  BT connected paired device ENJOY.
            } catch (IOException e) {
                Log.e("ERROR", "Exception during write", e);
            }
        }

        beginListenForData();

        //myLabel.setText("Bluetooth Opened");
    }

    void beginListenForData() {
        final Handler handler = new Handler();
        final byte delimiter = 10; //This is the ASCII code for a newline character

        stopWorker = false;
        readBufferPosition = 0;
        readBuffer = new byte[1024];
        workerThread = new Thread(new Runnable() {
            public void run() {
                while (!Thread.currentThread().isInterrupted() && !stopWorker) {
                    try {
                       // int bytesAvailable = mmInputStream.available();
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
                                            //myLabel.setText(data);
                                        }
                                    });
                                } else {
                                    readBuffer[readBufferPosition++] = b;
                                }
                            }
                        }
                    } catch (IOException ex) {
                        stopWorker = true;
                    }
                }
            }
        });

        workerThread.start();
    }

    void sendForward(View view) throws IOException {
        //String msg = myTextbox.getText().toString();
        switch (view.getId()) {
            case R.id.btForward:

                String msg = setForward();
                msg += "\n";
                mmOutputStream.write(msg.getBytes());
                //myLabel.setText("Data Sent");
                break;
        }
    }

    void sendReverse(View view) throws IOException {
        switch (view.getId()) {
            case R.id.btReverse:
                String msg = setReverse();
                msg += "\n";
                mmOutputStream.write(msg.getBytes());
                //  myLabel.setText("Data Sent");

                break;
        }
    }

    void sendLeft(View view) throws IOException {
        switch (view.getId()) {
            case R.id.btLeft:
                String msg = setLeft();
                msg += "\n";
                mmOutputStream.write(msg.getBytes());
                // myLabel.setText("left turn");

                break;
        }

    }

    void sendRight(View view) throws IOException {

        switch (view.getId()) {
            case R.id.btLeft:
                String msg = setRight();
                msg += "\n";
                mmOutputStream.write(msg.getBytes());
                //  myLabel.setText("Data Sent");

                break;

        }
    }

    void sendStop(View view) throws IOException {

        switch (view.getId()) {
            case R.id.btLeft:
                String msg = setStop();
                msg += "\n";
                mmOutputStream.write(msg.getBytes());
                //  myLabel.setText("Data Sent");

                break;

        }
    }


    String setForward() {
        return "f";
    }

    String setRight() {
        return "r";
    }

    String setLeft() {
        return "l";
    }

    String setReverse() {
        return "b";
    }

    String setStop() {
        return "g";
    }
}