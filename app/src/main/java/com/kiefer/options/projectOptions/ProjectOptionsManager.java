package com.kiefer.options.projectOptions;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.engine.EngineFacade;
import com.kiefer.files.keepers.ProjectOptionKeeper;
import com.kiefer.popups.WarningPopup;

public class ProjectOptionsManager extends BroadcastReceiver {

    private LLPPDRUMS llppdrums;
    private EngineFacade engineFacade;

    private boolean vibrateOnTouch;

    public ProjectOptionsManager(LLPPDRUMS llppdrums, EngineFacade engineFacade) {
        this.llppdrums = llppdrums;
        this.engineFacade = engineFacade;

        vibrateOnTouch = true;
    }

    public void setDriver(String driver) {
        engineFacade.setDriver(driver);
    }

/*
    private BluetoothProfile.ServiceListener mProfileListener = new BluetoothProfile.ServiceListener() {

        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            if (profile == BluetoothProfile.HEADSET) {
                BluetoothHeadset mBluetoothHeadset = (BluetoothHeadset) proxy;
                if (ActivityCompat.checkSelfPermission(llppdrums, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (mBluetoothHeadset.getConnectedDevices().size() > 0) {
                    //IS_BLUETOOTH_CONNECTED = true;
                    Log.e("asd", "Bluetooth device is connected");
                }

            }
        }

        @Override
        public void onServiceDisconnected(int profile)
        {
            if (profile == BluetoothProfile.HEADSET)
            {
                mBluetoothHeadset = null;
                IS_BLUETOOTH_CONNECTED = false;
                Logs.d(TAG,"Bluetooth device is disconnected");
            }
        }
    };

 */

    /** BLUETOOTH **/
    public void BTCheck() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //verkar inte gå att neka den här permissionen men kanske i nyare android-versioner? Bäst att ha ordentlig check
        if (llppdrums.checkSelfPermission(Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED ) {
            if(mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()
                    && mBluetoothAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET) == BluetoothHeadset.STATE_CONNECTED){
                showBTWarning();
            }
        }
        else {
            llppdrums.requestPermissions(new String[] {Manifest.permission.BLUETOOTH}, LLPPDRUMS.BLUETOOTH_CONNECT_PERMISSION_CODE);
        }

/*
        if (llppdrums.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED ) {
            //init();
        }
        else {
            llppdrums.requestPermissions(new String[] {Manifest.permission.BLUETOOTH_CONNECT}, BLUETOOTH_CONNECT_PERMISSION_CODE);
        }

 */
/*
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();


        //do we have permission
        if (ActivityCompat.checkSelfPermission(llppdrums, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return false;

            //if a bt-headset is connected, show a warning
            if(mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()
                    && mBluetoothAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET) == BluetoothHeadset.STATE_CONNECTED){
                showBTWarning();
            }
        }
        else{
            //om man inte har fått perm
            //kanske nån varning om BT ändå?
            //llppdrums.requestPermissions(PERMISSIONS, PERMISSIONS_CODE);
        }

 */







       //if (isBluetoothHeadsetConnected()) {
            //showBTWarning();
        //}
    }

    private boolean isBluetoothHeadsetConnected() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (ActivityCompat.checkSelfPermission(llppdrums, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return false;
        }
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()
                && mBluetoothAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET) == BluetoothHeadset.STATE_CONNECTED;

    }

    public void showBTWarning(){
        //Log.e("ProjectOptionsManager", "BT connected");
        String label = llppdrums.getResources().getString(R.string.btWarningLabel);
        String txt = llppdrums.getResources().getString(R.string.btWarningTxt);
        new WarningPopup(llppdrums, label, txt);
    }
//The BroadcastReceiver that listens for bluetooth broadcasts
    //private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
           //... //Device found
            }
            else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
           //... //Device is now connected
                //Log.e("ProjectOptionsManager", "BT connected");
                showBTWarning();
            }
            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
           //... //Done searching
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
           //... //Device is about to disconnect
            }
            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
           //... //Device has disconnected
            }
        }
    //};

    /** GET **/
    public boolean vibrateOnTouch() {
        return vibrateOnTouch;
    }

    /** RESTORE **/
    public ProjectOptionKeeper getKeeper(){
        ProjectOptionKeeper keeper = new ProjectOptionKeeper();
        keeper.driver = engineFacade.getDriver();

        return keeper;
    }
}
