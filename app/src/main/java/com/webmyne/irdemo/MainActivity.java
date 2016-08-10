package com.webmyne.irdemo;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    TextView textResponse;
    EditText editTextAddress, editTextPort;
    Button buttonConnect, buttonClear;
    ProgressDialog pd;
    String str = "sendir,1:1,1,37878,1,1,21,22,21,64,21,22,21,21,21,22,21,22,21,21,21,22,21,64,22,21,21,64,22,63,22,64,21,64,22,63,22,64,21,21,22,21,22,21,21,21,22,64,21,21,22,21,22,21,21,64,22,63,22,64,21,64,22,21,21,64,22,63,22,64,21,1516,341,85,22,3700";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextAddress = (EditText)findViewById(R.id.address);
        editTextPort = (EditText)findViewById(R.id.port);
        buttonConnect = (Button)findViewById(R.id.connect);
        buttonClear = (Button)findViewById(R.id.clear);
        textResponse = (TextView)findViewById(R.id.response);

        buttonConnect.setOnClickListener(buttonConnectOnClickListener);

        buttonClear.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                textResponse.setText("");
            }});
    }

    OnClickListener buttonConnectOnClickListener =
            new OnClickListener(){

                @Override
                public void onClick(View arg0) {
                    MyClientTask myClientTask = new MyClientTask(
                            editTextAddress.getText().toString(),
                            Integer.parseInt(editTextPort.getText().toString()));
                    myClientTask.execute();
                }};

    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String response = "";

        MyClientTask(String addr, int port){
            dstAddress = addr;
            dstPort = port;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd= new ProgressDialog(MainActivity.this,0);
            pd.setMessage("Connecting...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            try {

//                Socket socket = new Socket(dstAddress, dstPort);
//                PrintWriter out = new PrintWriter(socket.getOutputStream());
//                 out.write(str);
//                out.flush();

                IRSocketClient socketClient = new IRSocketClient(dstAddress, dstPort);
                socketClient.sendDataWithString(str);
                String r = socketClient.receiveDataFromServer();
                Log.e("Socket status",""+r);



            }catch (Exception e){Log.e("### EXC",e.toString());}

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            textResponse.setText(response);
            super.onPostExecute(result);
            pd.dismiss();
        }

    }

    public void sendToPort() throws IOException {
        Socket socket = null;
        OutputStreamWriter osw;

        try {
            socket = new Socket("192.168.1.177", 4998);
            osw =new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
            osw.write(str, 0, str.length());
        } catch (IOException e) {
            System.err.print(e);
        } finally {
            socket.close();
        }

    }

}