package com.example.bluetoothexample;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;


    TextView mStatusBluetooth, mPaired;
    ImageView mBlueTv;
    Button mOnBtn, mOffBtn, mDescobrirBtn, mPairedBtn;

    BluetoothAdapter mBlueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStatusBluetooth = findViewById(R.id.statusBluetoothTv);
        mPaired = findViewById(R.id.pairedTv);
        mBlueTv = findViewById(R.id.bluetoothIv);
        mOnBtn = findViewById(R.id.onBtn);
        mOffBtn = findViewById(R.id.offBtn);
        mDescobrirBtn = findViewById(R.id.discoverableBluetooth);
        mPairedBtn = findViewById(R.id.pairedBtn);

        // Adapter
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();

        // Check if bluetooth is available or not
        if (mBlueAdapter == null){
            mStatusBluetooth.setText("Bluetooth is not available!");
        } else{
            mStatusBluetooth.setText("Bluetooth is available!");
        }

        // set image according bluetooth status on/off
        if (mBlueAdapter.isEnabled()){
            mBlueTv.setImageResource(R.drawable.ic_action_on);
        }else {
            mBlueTv.setImageResource(R.drawable.ic_action_off);
        }

        // on btn click
        mOnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mBlueAdapter.isEnabled()){
                    showToast("Turning on Bluetooth ...");
                    // intent to on bluetooth
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE_BT);
                }else {
                    showToast("Bluetooth is already on");
                }
            }
        });

        // discover bluetooth btn
        mDescobrirBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mBlueAdapter.isDiscovering()){
                    showToast("Making your device discoverable");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent, REQUEST_DISCOVER_BT);
                }
            }
        });

        // off btn
        mOffBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBlueAdapter.isEnabled()){
                    mBlueAdapter.disable();
                    showToast("Turning Bluetooth off");
                    mBlueTv.setImageResource(R.drawable.ic_action_off);
                }else {
                    showToast("Bluetooth is already off");
                }
            }
        });

        // get paired devices
        mPairedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBlueAdapter.isEnabled()){
                     mPaired.setText("Paired Devices");
                    Set<BluetoothDevice> devices = mBlueAdapter.getBondedDevices();
                    for (BluetoothDevice device: devices){
                        mPaired.append("\nDevice: " + devices.getClass()+ "," + device);
                    }
                } else {
                    // bluetooth is off, can't get paired devices
                    showToast("Turn on bluetooth to get paired devices ");
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if(resultCode == RESULT_OK){
                    // bluetooth is on
                    mBlueTv.setImageResource(R.drawable.ic_action_on);
                    showToast("Bluetooth is on");
                }else {
                    // user denied to turn bluetooth on
                    showToast("Could't on bluetooth  ");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // toast message function
    private void showToast (String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}