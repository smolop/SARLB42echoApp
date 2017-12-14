package com.lanumbemarket.sar_lb42_echoapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    EditText textOut;
    TextView textIn;
    Button buttonSwitch;
    final String host = null;
    final String port = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        textOut = findViewById(R.id.textOut);
        Button buttonSend = findViewById(R.id.send);
        buttonSwitch = findViewById(R.id.switchButton);
        textIn = findViewById(R.id.textIn);
        buttonSend.setOnClickListener(buttonSwitchOnClickListener);
        buttonSend.setOnClickListener(buttonSendOnClickListener);
    }

    Button.OnClickListener buttonSwitchOnClickListener = new Button.OnClickListener(){
        @Override
        public void onClick(View view) {
            if(buttonSwitch.getText().toString() == "ON"){
                buttonSwitch.setText("OFF");
            }else if(buttonSwitch.getText().toString() == "OFF"){
                buttonSwitch.setText("ON");
            }
        }
    };

    Button.OnClickListener buttonSendOnClickListener
            = new Button.OnClickListener(){

        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub
            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;

            try {
                socket = new Socket("158.227.132.15", 50004);
                //socket = new Socket("g000008.gi.ehu.es", 50004);
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream.writeUTF(textOut.getText().toString());
                textIn.setText(dataInputStream.readUTF());
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            finally{
                if (socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataOutputStream != null){
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataInputStream != null){
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }};

}
