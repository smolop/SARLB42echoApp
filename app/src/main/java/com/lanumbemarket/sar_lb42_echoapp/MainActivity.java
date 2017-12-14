package com.lanumbemarket.sar_lb42_echoapp;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    EditText textOut;
    TextView textIn;
    Button buttonSwitch;
    Button buttonSend;
    EditText serverEditText;
    EditText portEditText;
    Socket socket = null;
    DataOutputStream dataOutputStream = null;
    DataInputStream dataInputStream = null;
    private String IP = "158.227.132.15";
    private String PORT = "50004";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        textOut = findViewById(R.id.textOut);
        buttonSend = findViewById(R.id.send);
        buttonSwitch = findViewById(R.id.switchButton);
        textIn = findViewById(R.id.textIn);
        serverEditText = findViewById(R.id.serverEditText);
        portEditText = findViewById(R.id.portEditText);

        textIn.setMovementMethod(new ScrollingMovementMethod());
        buttonSwitch.setOnClickListener(buttonSwitchOnClickListener);
        buttonSend.setOnClickListener(buttonSendOnClickListener);
    }

    private void connectTo(View view) {
        if (!serverEditText.getText().toString().isEmpty())
            IP = serverEditText.getText().toString();
        if (!portEditText.getText().toString().isEmpty())
            PORT = portEditText.getText().toString();
        textIn.setText(IP + " : " + PORT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            buttonSwitch.setBackgroundColor(getColor(R.color.colorPrimaryDark));
        }
        buttonSend.setEnabled(true);
        serverEditText.setEnabled(false);
        portEditText.setEnabled(false);

        try {
            for (InetAddress addr : InetAddress.getAllByName(IP))
                textIn.setText("IP: " + addr.getHostAddress() + " PORT: " + PORT + "\nMessage : \n");

            socket = new Socket(IP, Integer.parseInt(PORT));
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void disconnectFrom() {
        textIn.setText(getString(R.string.Result));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            buttonSwitch.setBackgroundColor(getColor(R.color.colorAlertDark));
        }
        serverEditText.setEnabled(true);
        portEditText.setEnabled(true);
        buttonSend.setEnabled(false);


        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (dataOutputStream != null) {
            try {
                dataOutputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (dataInputStream != null) {
            try {
                dataInputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    Button.OnClickListener buttonSwitchOnClickListener = new Button.OnClickListener() {
        @Override
        public void onClick(View view) {
            String buttonVal = buttonSwitch.getText().toString();
            String ON = getString(R.string.on);
            String OFF = getString(R.string.off);
            if (buttonVal.equals(ON)) {
                buttonSwitch.setText(OFF);
                disconnectFrom();
            } else if (buttonVal.equals(OFF)) {
                buttonSwitch.setText(ON);
                connectTo(view);
            }
        }
    };

    Button.OnClickListener buttonSendOnClickListener
            = new Button.OnClickListener() {

        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub

            try {
                dataOutputStream.writeUTF(textOut.getText().toString());
                textIn.append("\n" + dataInputStream.readUTF() + "\n");
                textIn.setScrollY(textIn.getScrollY()+64);
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch blockss
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    };

}
