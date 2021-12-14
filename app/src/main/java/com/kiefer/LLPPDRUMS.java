package com.kiefer;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.kiefer.controller.Controller;
import com.kiefer.files.keepers.LLPPDRUMSKeeper;
import com.kiefer.info.InfoManager;
import com.kiefer.options.projectOptions.ProjectOptionsManager;
import com.kiefer.machine.sequencerUI.SequencerUI;
import com.kiefer.files.KeeperFileHandler;
import com.kiefer.ui.tabs.Tab;
import com.kiefer.ui.tabs.TabManager;
import com.kiefer.fragments.ControllerFragment;
import com.kiefer.fragments.DrumMachineFragment;
import com.kiefer.fragments.TopFragment;
import com.kiefer.ui.tabs.interfaces.TabHoldable;
import com.kiefer.ui.tabs.interfaces.Tabable;
import com.kiefer.machine.DrumMachine;
import com.kiefer.engine.EngineFacade;
import com.kiefer.utils.ImgUtils;

import androidx.fragment.app.FragmentActivity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.File;
import java.util.ArrayList;

public class LLPPDRUMS extends FragmentActivity implements TabManager.OnTabClickedListener, TabHoldable {

    private static String LOG_TAG = "MWEngineFacade"; // logcat identifier
    private static int PERMISSIONS_CODE = 8081981;

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
    private String appFolderPath, templatesFolderPath;

    //tabs
    private ArrayList<Tabable> tabables;

    //fragments
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private Fragment activeFragment;
    private TopFragment topFragment;
    private SequencerUI sequencerUI; //used when creating drumMachineFragment. We need a reference since the modules use it to draw their Drawables in it
    private DrumMachineFragment drumMachineFragment;
    private ControllerFragment controllerFragment;

    //UI
    private FrameLayout background;

    @Override
    public void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.layout_main);

        // these may not necessarily all be required for your use case (e.g. if you're not recording
        // from device audio inputs or reading/writing files) but are here for self-documentation

        /*
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            String[] PERMISSIONS = {
                    Manifest.permission.RECORD_AUDIO, // RECORD_AUDIO must be granted prior to engine.start()
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            // Check if we have all the necessary permissions, if not: prompt user
            int permission = checkSelfPermission(Manifest.permission.RECORD_AUDIO);
            if (permission == PackageManager.PERMISSION_GRANTED)
                init();
            else
                requestPermissions(PERMISSIONS, PERMISSIONS_CODE);
        }

         */
        init();
    }

    /*
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if(requestCode != PERMISSIONS_CODE) {
            return;
        }
        for ( int i = 0; i < permissions.length; i++ ) {
            String permission = permissions[ i ];
            int grantResult   = grantResults[ i ];
            if ( permission.equals( Manifest.permission.RECORD_AUDIO ) && grantResult == PackageManager.PERMISSION_GRANTED ) {
                init();
            }
            else{
                requestPermissions( new String[]{ Manifest.permission.RECORD_AUDIO }, PERMISSIONS_CODE );
            }
        }
    }

     */

    /**
     * Called when the activity is destroyed. This also fires
     * on screen orientation changes, hence the override as we need
     * to watch the engines memory allocation outside of the Java environment
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
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
    protected void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        createKeeper();
    }


    public void createKeeper(){
        LLPPDRUMSKeeper keeper = getKeeper();
        if(keeper != null) {
            keeperFileHandler.write(keeper, appFolderPath, getString(R.string.lastDataFileName), false);
        }
    }

    /** INIT **/
    private void init() {

        keeperFileHandler = new KeeperFileHandler(this);

        //create the project folder if it doesn't exist
        //String externalStorageDirectory = Environment.getExternalStorageDirectory().getPath();
        File mediaStorageDir = getExternalFilesDir(null);

        //create the app folder in the root directory if it doesn't exists
        File appFolder = new File(mediaStorageDir, getResources().getString(R.string.app_name));
        if (!appFolder.exists()) {
            appFolder.mkdirs();
        }
        appFolderPath = appFolder.getAbsolutePath();

        //templates folder
        File templatesFolder = new File(appFolder, getResources().getString(R.string.templatesFolder));
        if (!templatesFolder.exists()) {
            templatesFolder.mkdirs();
        }
        templatesFolderPath = templatesFolder.getAbsolutePath();

        //tempFile path
        String lastDataPath = appFolderPath + "/" + getString(R.string.lastDataFileName) + getString(R.string.templateExtension);

        //set up the layout
        background = findViewById(R.id.mainBg);

        LLPPDRUMSKeeper keeper;
        try{
            keeper = (LLPPDRUMSKeeper) keeperFileHandler.readKeepers(lastDataPath);
        }
        catch (Exception e){
            keeper = null;
        }

        int tempo;
        try {
            tempo = keeper.drumMachineKeeper.initTempo;
        }
        catch (Exception e){
            tempo = getResources().getInteger(R.integer.defaultTempo);
        }

        int steps;
        try {
            steps = keeper.drumMachineKeeper.initSteps;
        }
        catch (Exception e){
            steps = getResources().getInteger(R.integer.defaultNOfSteps);
        }
/*
        int subs;
        try {
            subs = keeper.drumMachineKeeper.initSubs;
        }
        catch (Exception e){
            subs = getResources().getInteger(R.integer.defaultNOfSubs);
        }

 */

        String driver;
        try {
            driver = keeper.projectOptionKeeper.driver;
        }
        catch (Exception e){
            driver = null; //will be set in the engine
        }

        //create the main objects used by the app
        engineFacade = new EngineFacade(this, tempo, steps, driver);

        infoManager = new InfoManager(this);

        //create the necessary Bitmaps before creating the tabables
        int imgId = ImgUtils.getRandomImageId();
        Bitmap tabBitmap = ImgUtils.getTabImg(this, imgId, 0, 2, TabManager.HORIZONTAL);
        Bitmap bgBitmap = ImgUtils.getBgImg(this, imgId, TabManager.HORIZONTAL);
        try {
            drumMachine = new DrumMachine(this, engineFacade, tabBitmap, bgBitmap, keeper.drumMachineKeeper);
        }
        catch (Exception e){
            drumMachine = new DrumMachine(this, engineFacade, tabBitmap, bgBitmap, null);
        }

        imgId = ImgUtils.getRandomImageId();
        tabBitmap = ImgUtils.getTabImg(this, imgId, 1, 2, TabManager.HORIZONTAL);
        bgBitmap = ImgUtils.getBgImg(this, imgId, TabManager.HORIZONTAL);
        try {
            controller = new Controller(this, engineFacade, tabBitmap, bgBitmap, drumMachine.getSequenceManager(), keeper.controllerKeeper);
        }
        catch (Exception e){
            controller = new Controller(this, engineFacade, tabBitmap, bgBitmap, drumMachine.getSequenceManager(), null);
        }

        //add the tabables to the Array
        tabables = new ArrayList<>();
        tabables.add(drumMachine);
        tabables.add(controller);

        sequencerUI = new SequencerUI(this);
        sequencerUI.setSequencerClickedListener(drumMachine);

        //create the optionManager
        projectOptionsManager = new ProjectOptionsManager(this, engineFacade);

        //register the BT-handler
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        this.registerReceiver(projectOptionsManager, filter);

        //this will run after the UI is ready
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                projectOptionsManager.BTCheck();
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
    }

    @Override
    public void onTabClicked(Tab tab) {
        //The user selected a tab in the TopFragment
        switchFragment(tab.getN());
        background.setBackground(new BitmapDrawable(getResources(), tab.getBitmap()));

        topFragment.setTabAppearances(0, tabables, tab.getN(), null, null); //no need to provide bitmaps here since nothing will change
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
    public EngineFacade getEngineFacade() {
        return engineFacade;
    }

    public InfoManager getInfoManager(){
        return infoManager;
    }

    @Override
    public ArrayList<Tabable> getTabables(int tierNo) {
        return tabables;
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

    public SequencerUI getSequencer(){
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

    public String getTemplatesFolderPath() {
        return templatesFolderPath;
    }

    //FILE HANDLER
    public KeeperFileHandler getKeeperFileHandler() {
        return keeperFileHandler;
    }

    public ProjectOptionsManager getProjectOptionsManager() {
        return projectOptionsManager;
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

    //gets a keeper as argument and uses it during creation and sends it further to tracks
    public void load(final LLPPDRUMSKeeper k){
        drumMachine.load(k.drumMachineKeeper);
        controller.load(k.controllerKeeper);
    }
}
