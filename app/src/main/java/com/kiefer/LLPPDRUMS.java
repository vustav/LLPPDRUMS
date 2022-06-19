package com.kiefer;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kiefer.controller.Controller;
import com.kiefer.files.keepers.Keeper;
import com.kiefer.files.keepers.LLPPDRUMSKeeper;
import com.kiefer.info.InfoManager;
import com.kiefer.interfaces.Loadable;
import com.kiefer.options.projectOptions.ProjectOptionsManager;
import com.kiefer.machine.sequencerUI.SequencerUI;
import com.kiefer.files.KeeperFileHandler;
import com.kiefer.ui.tabs.TabManager;
import com.kiefer.fragments.ControllerFragment;
import com.kiefer.fragments.drumMachine.DrumMachineFragment;
import com.kiefer.fragments.TopFragment;
import com.kiefer.ui.tabs.interfaces.TabHolder;
import com.kiefer.ui.tabs.interfaces.Tab;
import com.kiefer.machine.DrumMachine;
import com.kiefer.engine.EngineFacade;

import androidx.fragment.app.FragmentActivity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class LLPPDRUMS extends FragmentActivity implements TabManager.OnTabClickedListener, TabHolder, Loadable {
    /** TRÅDAR **/
    //DrumMachineFragment.showPlayIcon();
    //SequenceManager.handleSequencerPositionChange();
    //SequenceCounter.activateStep()
    //SequenceCounter.queueStep()
    /* pilarna ändras i UI-tråden */

    /** TILLFÄLLIGA FIXAR **/
    //kör pc.reset() i FxManager.removeFxsFromEngine() efter att de tagits bort i loop redan

    public static boolean disableLoad = false;
    public static final boolean hideUIonPlay = false;

    public static boolean checkBlueTooth = false;
    //måste avkommentera registerReceiver() och unregisterReceiver(), samt göra ProjectOptionsManager till BroadcastReceiver (avkommentera även där)

    private static String LOG_TAG = "MWEngineFacade"; // logcat identifier
    private static final int PERMISSIONS_CODE = 8081981;
    public static int RECORD_AUDIO_PERMISSION_CODE = 34564576;

    public static final int BLUETOOTH_CONNECT_PERMISSION_CODE = 956;

    //main classes
    private EngineFacade engineFacade;
    private DrumMachine drumMachine;
    private Controller controller;

    //options
    private ProjectOptionsManager projectOptionsManager;

    //info
    private InfoManager infoManager;

    //file-handling
    private KeeperFileHandler keeperFileHandler;
    //private String appFolderPath;
    private String folderPath;
    private String savedProjectsFolderPath, savedRndTemplateFolderPath;

    //tabs
    private ArrayList<Tab> tabs;

    //fragments
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private Fragment activeFragment;
    private TopFragment topFragment;
    private SequencerUI sequencerUI; //used when creating drumMachineFragment. We need a reference since the modules use it to draw their Drawables in it
    private DrumMachineFragment drumMachineFragment;
    private ControllerFragment controllerFragment;

    //deleter
    private Deleter deleter;

    //UI
    private FrameLayout background;

    /** FUTURE GLOBAL BOOLS **/
    //timer för rnd
    //Deleter
    //rensning av alla ProcessingChains i destroyFxs() i FxManager (borde inte behövas för görs ändå i destroyFx(Fx fx) som också anropas)

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.layout_main);
        //checkPermission();
        init();

        /** TEST CRASH **/
/*
        Button crashButton = new Button(this);
        crashButton.setText("Test Crash");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });

        addContentView(crashButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

 */


    }

    /** PERMISSIONS **/
    /** BEHÖVS INTE NU MEN BEHÅLL FÖR REFERENS **/
    private void checkPermission(){
        if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO)) {

                // Explain to the user why we need to read the contacts
                new AlertDialog.Builder(this)
                        .setTitle("PERMISSION TO RECORD AUDIO")
                        .setMessage("NEED THIS TO WORK")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_PERMISSION_CODE);
                            }
                        })
/*
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finishAffinity();
                            }
                        })
 */
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

            //requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_PERMISSION_CODE);
        }
        else{
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /*
        if(requestCode != RECORD_AUDIO_PERMISSION_CODE) {
            return;
        }

         */
        for (int i=0; i < permissions.length; i++) {
            String permission = permissions[i];
            int grantResult = grantResults[i];
            if (permission.equals(Manifest.permission.RECORD_AUDIO ) && grantResult == PackageManager.PERMISSION_GRANTED) {
                init();
            } else {
                //close the app if permission is denied
                finishAffinity();
            }
        }

        //BLUETOOTH
        //verkar inte gå att neka den här permissionen men kanske i nyare android-versioner? Bäst att ha ordentlig check
        if(checkBlueTooth) {
                /*
                if (permission.equals(Manifest.permission.BLUETOOTH) && grantResult == PackageManager.PERMISSION_GRANTED) {
                    projectOptionsManager.BTCheck();
                } else {
                    //om perm nekas, visa nån varning. Nu visas samma, borde fixa en egen för nekad perm i framtiden.
                    projectOptionsManager.showBTWarning();
                    //requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_PERMISSION_CODE);
                }

                 */
        }
    }

    /**
     * Called when the activity is destroyed. This also fires
     * on screen orientation changes, hence the override as we need
     * to watch the engines memory allocation outside of the Java environment
     */
    @Override
    public void onDestroy() {
        super.onDestroy();

        //createKeeper();

        //for blueTooth-listening
        //unregisterReceiver(projectOptionsManager);

        engineFacade.destroy();
        drumMachine.destroy();
    }

    /** TopFragment.OnTabSelectedListener **/
    //LLPPDRUMS sets itself as implementer of TopFragment.OnTabSelectedListener. This means onTabSelected() will be called when a tab is pressed
    //
    @Override
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof TopFragment) {
            TopFragment top = (TopFragment) fragment;
            top.setTabSelectedListener(this);
        }
        if (fragment instanceof DrumMachineFragment) {
            DrumMachineFragment drumMachineFragment = (DrumMachineFragment) fragment;
            drumMachineFragment.setTabSelectedListener(drumMachine);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle bundle){
        super.onSaveInstanceState(bundle);
        createKeeper();
    }

    public void createKeeper(){
        //Log.e("LLPPDRUMS", "createKeeper()");
        LLPPDRUMSKeeper keeper = getKeeper();
        if(keeper != null) {
            keeperFileHandler.write(keeper, folderPath, getString(R.string.lastDataFileName), false);
        }
    }

    /** INIT **/
    private void init() {


        keeperFileHandler = new KeeperFileHandler(this);

        //create the project folder if it doesn't exist
        File mediaStorageDir = getExternalFilesDir(null);

        folderPath = mediaStorageDir.getAbsolutePath();

        //projects folder
        File savedProjectsFolder = new File(folderPath, getResources().getString(R.string.savedProjectsFolder));
        if (!savedProjectsFolder.exists()) {
            savedProjectsFolder.mkdirs();
        }
        savedProjectsFolderPath = savedProjectsFolder.getAbsolutePath();

        //rndTemplate folder
        File savedRndTemplateFolder = new File(folderPath, getResources().getString(R.string.savedRndTemplatesFolder));
        if (!savedRndTemplateFolder.exists()) {
            savedRndTemplateFolder.mkdirs();
        }
        savedRndTemplateFolderPath = savedRndTemplateFolder.getAbsolutePath();

        //tempFile path
        String lastDataPath = folderPath + "/" + getString(R.string.lastDataFileName) + getString(R.string.templateExtension);

        //set up the layout
        background = findViewById(R.id.mainBg);

        LLPPDRUMSKeeper keeper;
        try{
            //Log.e("LLPPDRUMS", "keeper loaded");
            keeper = (LLPPDRUMSKeeper) keeperFileHandler.readKeepers(lastDataPath);
        }
        catch (Exception e){
            //Log.e("LLPPDRUMS", "keeper null");
            keeper = null;
        }

        if(disableLoad) {
            keeper = null;
        }

        int tempo;
        try {
            //Log.e("LLPPDRUMS", "init(), tempo from keeper");
            tempo = keeper.drumMachineKeeper.initTempo;
        }
        catch (Exception e){
            //Log.e("LLPPDRUMS", "init(), tempo default");
            tempo = getResources().getInteger(R.integer.defaultTempo);
        }

        int steps;
        try {
            steps = keeper.drumMachineKeeper.initSteps;
        }
        catch (Exception e){
            steps = getResources().getInteger(R.integer.defaultNOfSteps);
        }

        String driver;
        try {
            driver = keeper.projectOptionKeeper.driver;
        }
        catch (Exception e){
            driver = null; //will be set in the engine
        }

        //create the main objects used by the app
        engineFacade = new EngineFacade(this, tempo, steps, driver);

        deleter = new Deleter(engineFacade);

        infoManager = new InfoManager(this);

        try {
            if(keeper != null) {
                drumMachine = new DrumMachine(this, engineFacade, 0, keeper.drumMachineKeeper);
            }
            else{
                drumMachine = new DrumMachine(this, engineFacade, 0, null);
            }
        }
        catch (Exception e){
            drumMachine = new DrumMachine(this, engineFacade, 0, null);
            String message = "COULDN'T RESTORE";
            Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            toast.show();
        }

        //try {
        if(keeper != null){
            controller = new Controller(this, engineFacade, drumMachine.getSequenceManager(), 1, keeper.controllerKeeper);
        }
        //catch (Exception e){
        else{
            controller = new Controller(this, engineFacade, drumMachine.getSequenceManager(), 1, null);
        }

        //add the tabables to the Array
        tabs = new ArrayList<>();
        tabs.add(drumMachine);
        tabs.add(controller);

        sequencerUI = new SequencerUI(this);
        sequencerUI.setSequencerClickedListener(drumMachine);

        //create the optionManager
        projectOptionsManager = new ProjectOptionsManager(this, engineFacade);

        //register the BT-handler
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);

        //this will run after the UI is ready

        //this.registerReceiver(projectOptionsManager, filter);

        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                if(checkBlueTooth) {
                    //projectOptionsManager.BTCheck();
                }
                else{
                    projectOptionsManager.showBTWarning();
                }
            }
        });

        //create the TopFragment
        topFragment = TopFragment.newInstance();
        topFragment.setProjectOptionsManager(projectOptionsManager);

        //Get the FragmentManager and start a transaction.
        fragmentManager = getSupportFragmentManager();

        //Add the TopFragment
        addFragment(R.id.mainTopFragLayout, topFragment);

        //randomize the start-sequences
        if(keeper == null) {
            drumMachine.randomizeSequences();
        }

        /** for some reason it's full here, should probably find out why **/
        deleter.delete();
    }

    @Override
    public void onTabClicked(Tab tab) {
        switchFragment(tab.getTabIndex());
        topFragment.setTabAppearances(0, tabs, tab.getTabIndex());
    }

    /** FRAGMENT HANDLING **/
    public void switchFragment(int tab){
        int layout = R.id.mainBottomFragLayout;

        switch (tab){
            case 0:
                //machine
                if(!(activeFragment instanceof DrumMachineFragment)){

                    drumMachineFragment = DrumMachineFragment.newInstance();
                    replaceFragment(layout, drumMachineFragment);

                    //set the new active to be able to do this check on the next tab-click
                    activeFragment = drumMachineFragment;
                }
                break;
            case 1:
                //controller
                if(!(activeFragment instanceof ControllerFragment)){

                    //remove the sequencers layout from its parent when deleting the machine-fragment
                    if(sequencerUI.getLayout().getParent() != null) {
                        ((ViewGroup) sequencerUI.getLayout().getParent()).removeAllViews();
                    }

                    controllerFragment = ControllerFragment.newInstance();
                    replaceFragment(layout, controllerFragment);
                    controllerFragment.updateUI(controller);
                    activeFragment = controllerFragment;
                }
                break;
        }
    }

    public void addFragment(int layout, Fragment fragment){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(layout, fragment).commit();
    }

    public void replaceFragment(int layout, Fragment fragment){
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(layout, fragment).commit();
    }

    public void removeFragment(Fragment fragment) {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(fragment).commit();
    }

    /** GET **/
    public Deleter getDeleter() {
        return deleter;
    }

    public EngineFacade getEngineFacade() {
        return engineFacade;
    }

    public InfoManager getInfoManager(){
        return infoManager;
    }

    @Override
    public ArrayList<Tab> getTabs(int tierNo) {
        return tabs;
    }

    public FrameLayout getLayout(){
        return background;
    }

    public Fragment getActiveFragment() {
        return activeFragment;
    }

    //CLASSES
    public DrumMachine getDrumMachine(){
        return drumMachine;
    }

    public Controller getController() {
        return controller;
    }

    public SequencerUI getSequencerUI(){
        return sequencerUI;
    }

    //FRAGMENTS
    public FragmentManager getFragManager(){
        return fragmentManager;
    }
    public DrumMachineFragment getDrumMachineFragment(){
        return drumMachineFragment;
    }
    public TopFragment getTopFragment(){
        return topFragment;
    }

    public String getSavedProjectsFolderPath() {
        return savedProjectsFolderPath;
    }

    public String getSavedRndTemplateFolderPath() {
        return savedRndTemplateFolderPath;
    }

    //FILE HANDLER
    public KeeperFileHandler getKeeperFileHandler() {
        return keeperFileHandler;
    }

    public ProjectOptionsManager getProjectOptionsManager() {
        return projectOptionsManager;
    }

    public FrameLayout getBackground() {
        return background;
    }

    /** SET **/
    public void setSeqText(String text){
        if(sequencerUI != null) {
            sequencerUI.setLabelText(text);
        }
    }

    /** SEQUENCER UPDATES **/
    public void handleSequencerPositionChange(int sequencerPosition){
        //gets called when the notificationThread is created in EngineFacade, before any of these exist
        if(drumMachine != null) {
            drumMachine.handleSequencerPositionChange(sequencerPosition);
        }
        if(sequencerUI != null) {
            sequencerUI.handleSequencerPositionChange(sequencerPosition);
        }
    }

    /** FILE HANDLING **/
    public LLPPDRUMSKeeper getKeeper(){
        if(drumMachine != null) {
            LLPPDRUMSKeeper keeper = new LLPPDRUMSKeeper();
            keeper.drumMachineKeeper = drumMachine.getKeeper();
            keeper.controllerKeeper = controller.getKeeper();
            keeper.projectOptionKeeper = projectOptionsManager.getKeeper();
            return keeper;
        }
        return null;
    }

    public void load(final Keeper keeper){
        LLPPDRUMSKeeper k = (LLPPDRUMSKeeper) keeper;
        drumMachine.load(k.drumMachineKeeper);
        controller.load(k.controllerKeeper);

        getDrumMachineFragment().update();
        sequencerUI.notifyDataSetChange();
    }
}
