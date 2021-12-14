package com.kiefer.options.projectOptions;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.engine.EngineFacade;
import com.kiefer.files.keepers.ProjectOptionKeeper;
import com.kiefer.popups.WarningPopup;

public class ProjectOptionsManager extends BroadcastReceiver  {
    private LLPPDRUMS llppdrums;
    private EngineFacade engineFacade;

    public ProjectOptionsManager(LLPPDRUMS llppdrums, EngineFacade engineFacade){
        this.llppdrums = llppdrums;
        this.engineFacade = engineFacade;
    }

    public void setDriver(String driver){
        engineFacade.setDriver(driver);
    }

    /** BLUETOOTH **/
    public void BTCheck(){
        if(isBluetoothHeadsetConnected()){
            showBTWarning();
        }
    }

    private static boolean isBluetoothHeadsetConnected() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()
                && mBluetoothAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET) == BluetoothHeadset.STATE_CONNECTED;
    }

    private void showBTWarning(){
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


    /** RESTORE **/
    public ProjectOptionKeeper getKeeper(){
        ProjectOptionKeeper keeper = new ProjectOptionKeeper();
        keeper.driver = engineFacade.getDriver();

        return keeper;
    }
}
