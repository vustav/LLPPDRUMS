package com.kiefer.machine;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.engine.EngineFacade;
import com.kiefer.randomization.rndSeqManager.rndSeqManagerPopup.RndSeqManagerPopup;
import com.kiefer.machine.sequencerUI.SequencerUI;
import com.kiefer.files.keepers.DrumMachineKeeper;
import com.kiefer.files.keepers.DrumSequenceKeeper;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.popups.trackMenu.AutomateStepsPopup;
import com.kiefer.popups.trackMenu.TrackMenuPopup;
import com.kiefer.popups.nameColor.NamePopup;
import com.kiefer.randomization.rndTrackManager.RndTrackManagerPopup;
import com.kiefer.popups.soundManager.SoundManagerPopup;
import com.kiefer.ui.tabs.TabManager;
import com.kiefer.fragments.drumMachine.DrumMachineFragment;
import com.kiefer.ui.tabs.interfaces.TabHolder;
import com.kiefer.ui.tabs.interfaces.Tab;
import com.kiefer.popups.trackMenu.MixerPopup;
import com.kiefer.utils.ImgUtils;
import com.kiefer.popups.fxManager.FxManagerPopup;

import java.util.ArrayList;
import java.util.Random;

/** DrumMachine holds the 4 sequences and works as a base for all communication between objects without direct references to each other.
 *
 * **/

public class DrumMachine implements TabManager.OnTabClickedListener, TabHolder, Tab, SequencerUI.OnSequencerClickedListener {
    private final LLPPDRUMS llppdrums;
    private final EngineFacade engineFacade;

    private int tabIndex;

    private SequenceManager sequenceManager;

    //bitmaps
    //private final Bitmap tabBitmap, bgBitmap;
    private int bitmapId;
    private final int optionsBgId, savePopupBgId, loadPopupBgId;

    //sequences
    private DrumSequence playingSequence, selectedSequence; //playing = currently playing, selected = shown in UI
    private ArrayList<DrumSequence> sequences;

    public DrumMachine(LLPPDRUMS llppdrums, EngineFacade engineFacade, int tabIndex, DrumMachineKeeper keeper){
        this.llppdrums = llppdrums;
        this.engineFacade = engineFacade;

        this.tabIndex = tabIndex;

        bitmapId = ImgUtils.getRandomImageId();

        optionsBgId = ImgUtils.getRandomImageId();
        savePopupBgId = ImgUtils.getRandomImageId();
        loadPopupBgId = ImgUtils.getRandomImageId();

        setupSequences(llppdrums.getResources().getInteger(R.integer.nOfSequences), keeper);

        try {
            sequenceManager = new SequenceManager(llppdrums, sequences, keeper.sequenceManagerKeeper);
        }
        catch (Exception e){
            sequenceManager = new SequenceManager(llppdrums, sequences, null);
        }
    }

    private void setupSequences(int nOfSequences, DrumMachineKeeper keeper){
        sequences = new ArrayList<>();
        for(int i = 0; i<nOfSequences; i++){

            if(keeper != null) {
                sequences.add(new DrumSequence(llppdrums, engineFacade, i, keeper.sequenceKeepers.get(i)));
                sequences.get(i).restore(keeper.sequenceKeepers.get(i));
            }
            else{
                sequences.add(new DrumSequence(llppdrums, engineFacade, i));
            }
            //sequences.get(i).deactivate();
        }

        selectedSequence = sequences.get(0);
        setPlayingSequence(selectedSequence);
    }

    /** SELECTION **/
    //TopFragment.OnTabSelectedListener
    //if a tab in the first tier is clicked we need to recolor all tabs, if its the second tier, only the second and third tier need recoloring etc.
    @Override
    public void onTabClicked(Tab tab) {

        if(llppdrums.getActiveFragment() instanceof DrumMachineFragment){
            DrumMachineFragment drumMachineFragment = (DrumMachineFragment) llppdrums.getActiveFragment();

            int selectedTab;
            switch (tab.getTier()){
                //SEQUENCE
                case 0:
                    selectedSequence.deselect();
                    selectedSequence = sequences.get(tab.getTabIndex());

                    //update the dataSet if new tracks are added
                    llppdrums.getSequencer().notifyDataSetChange();

                    //update the number of steps shown in the sequencer
                    llppdrums.getSequencer().updateNOfSteps(selectedSequence.getNOfSteps());

                    //reset the counter if the selected sequence isn't playing and lock the UI if the selected sequence is playing
                    if (playingSequence != selectedSequence) {
                        llppdrums.getSequencer().resetCounter();
                        unlockUI();
                    }
                    else if(engineFacade.isPlaying()){
                        lockUI();
                    }

                    //tell the sequence to select itself
                    selectedSequence.select();

                    //set the tab appearances (here all tiers has to be set. If the next tier is clicked only that and those after has to be set etc.)
                    selectedTab = sequences.indexOf(selectedSequence);
                    drumMachineFragment.setTabAppearances(0, getTabables(), selectedTab);

                    selectedTab = selectedSequence.getSequenceModules().indexOf(selectedSequence.getSelectedSequenceModule());
                    drumMachineFragment.setTabAppearances(1, selectedSequence.getTabs(0), selectedTab);

                    selectedTab = selectedSequence.getSelectedSequenceModule().getModuleModes().indexOf(selectedSequence.getSelectedSequenceModule().getSelectedMode());
                    drumMachineFragment.setTabAppearances(2, selectedSequence.getSelectedSequenceModule().getTabs(0), selectedTab);
                    break;

                //SEQUENCE MODULE
                case 1:
                    selectedSequence.setSelectedSequenceModule(tab.getTabIndex());

                    selectedTab = selectedSequence.getSequenceModules().indexOf(selectedSequence.getSelectedSequenceModule());
                    drumMachineFragment.setTabAppearances(1, selectedSequence.getTabs(0), selectedTab);

                    selectedTab = selectedSequence.getSelectedSequenceModule().getModuleModes().indexOf(selectedSequence.getSelectedSequenceModule().getSelectedMode());
                    drumMachineFragment.setTabAppearances(2, selectedSequence.getSelectedSequenceModule().getTabs(0), selectedTab);
                    break;

                //MODULE MODE
                case 2:
                    selectedSequence.getSelectedSequenceModule().setSelectedMode(tab.getTabIndex());

                    selectedTab = selectedSequence.getSelectedSequenceModule().getModuleModes().indexOf(selectedSequence.getSelectedSequenceModule().getSelectedMode());
                    drumMachineFragment.setTabAppearances(2, selectedSequence.getSelectedSequenceModule().getTabs(0), selectedTab);
                    break;
            }
            updateSeqLabel();
        }
    }

    public void updateSeqLabel(){
        llppdrums.setSeqText(selectedSequence.getFullName());
    }

    /** ACTIVATION **/
    public void changePlayingSequence(DrumSequence drumSequence) {
        if (playingSequence != drumSequence) {
            if (playingSequence != null) {
                playingSequence.stop(); //turns off automations
                playingSequence.deactivate();
            }
            setPlayingSequence(drumSequence);
        }
    }


    private void setPlayingSequence(DrumSequence drumSequence){

        playingSequence = drumSequence;

        //reset the counter if the selected sequence isn't playing and lock the UI if the selected sequence is playing
        if (playingSequence != selectedSequence) {
            llppdrums.getSequencer().resetCounter();
            unlockUI();
        }
        else if(engineFacade.isPlaying()){
            lockUI();
        }

        //engineFacade.setSubs(playingSequence.getSubs()); //subs first since the others use it
        engineFacade.setSteps(playingSequence.getNOfSteps());
        engineFacade.setTempo(playingSequence.getTempo());

        playingSequence.activate();
    }

    /** TRANSPORT **/
    public void play(){
        //llppdrums.getProjectOptionsManager().BTWarning();

        engineFacade.playSequencer();

        //turn on the little play-icon in the tab
        //llppdrums.getDrumMachineFragment().getTabManager().showIcon(sequences.indexOf(playingSequence), true);
        llppdrums.getDrumMachineFragment().showPlayIcon(sequences.indexOf(playingSequence), true);

        //lock the UI to prevent laggy actions during playback
        if (selectedSequence == playingSequence) {
            lockUI();
        }

        playingSequence.play();
    }

    public void pause(){
        engineFacade.pauseSequencer();
        //llppdrums.getDrumMachineFragment().getTabManager().showIcon(sequences.indexOf(playingSequence), false);
        llppdrums.getDrumMachineFragment().showPlayIcon(sequences.indexOf(playingSequence), false);
        unlockUI();
        llppdrums.getDeleter().delete();
    }
    public void stop(){
        sequenceManager.reset();
        engineFacade.stopSequencer();
        //llppdrums.getDrumMachineFragment().getTabManager().showIcon(sequences.indexOf(playingSequence), false);
        llppdrums.getDrumMachineFragment().showPlayIcon(sequences.indexOf(playingSequence), false);
        llppdrums.getSequencer().resetCounter();
        unlockUI();

        playingSequence.stop();
        llppdrums.getDeleter().delete();
    }

    private void lockUI(){
        if(LLPPDRUMS.hideUIonPlay) {
            llppdrums.getSequencer().lockUI();
            llppdrums.getDrumMachineFragment().lockUI();
        }
    }

    private void unlockUI(){
        llppdrums.getSequencer().unlockUI();
        llppdrums.getDrumMachineFragment().unlockUI();
    }

    /** RANDOMIZATION **/
    public void randomizeSelectedSequence(){
        //try {
            //a 10% chance the bgColor of topFragment is changed
            Random r = new Random();
            if(r.nextInt(10) == 0){
                llppdrums.getTopFragment().randomizeBgGradient();
            }

            selectedSequence.randomize();
        //}
        //catch (Exception e){
            //llppdrums.getTopFragment().randomizeBgGradient();
            //Log.e("Exception in: ", "DrumMachine.randomizeSelectedSequence(), msg: "+e.getMessage());
        //}
    }

    public void randomizeSequences() {
        for (DrumSequence ds : sequences){
            ds.randomize();
            ds.deactivate();
        }
        playingSequence.activate();
    }

    /** SEQ-EVENTS **/
    @Override
    public void onStepTouch(int track, int step, ImageView stepIV, float startX, float startY, float endX, float endY, int action){
        Step drum = selectedSequence.getTracks().get(track).getSteps().get(step);
        selectedSequence.getSelectedSequenceModule().onStepTouch(engineFacade, stepIV, drum, startX, startY, endX, endY, action);
    }

    public void openNamePopup(int trackNo){
        new NamePopup(llppdrums, selectedSequence.getTracks().get(trackNo));
    }

    public void openSoundManagerPopup(int trackNo){
        new SoundManagerPopup(llppdrums, selectedSequence.getTracks().get(trackNo));
    }

    public void openAutoStepPopup(int trackNo, View parent){
        new AutomateStepsPopup(llppdrums, parent, selectedSequence.getSelectedSequenceModule(), selectedSequence.getTracks().get(trackNo));
    }

    public void openRndPopup(int trackNo, View parent){
        new RndTrackManagerPopup(llppdrums, selectedSequence.getTracks().get(trackNo));
    }

    public void openFxManagerPopup(int trackNo){
        new FxManagerPopup(llppdrums, selectedSequence.getTracks().get(trackNo));
    }

    public void openMixerPopup(int trackNo){
        new MixerPopup(llppdrums, selectedSequence.getTracks().get(trackNo));
    }

    public void openRandomOptionsPopup(){
        new RndSeqManagerPopup(llppdrums, selectedSequence.getRandomizeManager());
    }

    public void expandTrackBtns(int trackNo, View parent){
        new TrackMenuPopup(llppdrums, this, trackNo, parent);
    }

    //TRACKS

    /** GET **/

    //TABS
    @Override
    public ArrayList<Tab> getTabs(int tierNo) {
        ArrayList<Tab> tabs = new ArrayList<>();
        switch (tierNo) {
            case 0:
                return getTabables();
            case 1:
                return selectedSequence.getTabs(0);
            case 2:
                return selectedSequence.getSelectedSequenceModule().getTabs(0);
        }
        return tabs;
    }

    public ArrayList<Tab> getTabables() {
        ArrayList<Tab> tabs = new ArrayList<>();
        tabs.addAll(sequences);
        return tabs;
    }

    @Override
    public int getBitmapId(){
        return bitmapId;
    }

    public int getNOfSequences(){
        return sequences.size();
    }

    @Override
    public String getName(){
        return  llppdrums.getResources().getString(R.string.machineName);
    }

    public Integer[] getSelectedTabIndexes(){
        Integer[] indexes = new Integer[3];
        indexes[0] = sequences.indexOf(selectedSequence);
        indexes[1] = selectedSequence.getSequenceModules().indexOf(selectedSequence.getSelectedSequenceModule());
        indexes[2] = selectedSequence.getSelectedSequenceModule().getModuleModes().indexOf(selectedSequence.getSelectedSequenceModule().getSelectedMode());
        return indexes;
    }

    public int getOptionsBgId() {
        return optionsBgId;
    }

    //SEQUENCES
    public DrumSequence getSelectedSequence() {
        return selectedSequence;
    }
    public int getSelectedSequenceIndex() {
        return sequences.indexOf(selectedSequence);
    }
    public DrumSequence getPlayingSequence() {
        return playingSequence;
    }

    //MACHINE DATA
    public int getPlayingSequenceNOfTracks(){
        return playingSequence.getNOfTracks();
    }
    public int getPlayingSequenceNOfSteps(){
        return playingSequence.getNOfSteps();
    }
    public int getSelectedSequenceNOfTracks(){
        return selectedSequence.getNOfTracks();
    }
    public int getSelectedSequenceNOfSteps(){
        return selectedSequence.getNOfSteps();
    }

    //TRACKS
    public int getTrackColor(int trackNo){
        return selectedSequence.getTracks().get(trackNo).getColor();
    }

    //DRUMS
    public Drawable getDrumDrawable(Step step){
        return selectedSequence.getSelectedSequenceModule().getDrawable(step.getTrackNo(), step.getStepNo());
    }

    public Drawable getSelectedSequenceDrumDrawable(int trackNo, int step){
        return selectedSequence.getSelectedSequenceModule().getDrawable(trackNo, step);
    }

    public ArrayList<DrumSequence> getSequences() {
        return sequences;
    }

    public int getSavePopupBgId() {
        return savePopupBgId;
    }

    public int getLoadPopupBgId() {
        return loadPopupBgId;
    }

    public SequenceManager getSequenceManager() {
        return sequenceManager;
    }

    //TABS
    @Override
    public int getOrientation(){
        return Tab.VERTICAL;
    }

    @Override
    public int getTabIndex() {
        return tabIndex;
    }

    @Override
    public int getTier(){
        return 0;
    }

    /** SET **/

    /** STATS **/
    /*
    private void postStats() {
        for (DrumSequence ds : sequences) {
            Log.e("DrumMachine", "Seq: " + sequences.indexOf(ds) + ", events: " + ds.getNOfSequencedEvents());
        }
        Log.e("DrumMachine", "----------------------------------------------------------");
    }

     */

    /** SEQUENCER UPDATES **/
    public void handleSequencerPositionChange(int sequencerPosition){
        playingSequence.handleSequencerPositionChange(sequencerPosition);
        sequenceManager.handleSequencerPositionChange(sequencerPosition);
    }

    /** RESTORATION **/
    public void loadSelectedSequence(final DrumSequenceKeeper k){
        selectedSequence.load(k);
        llppdrums.getDrumMachineFragment().update();
    }

    //gets a keeper as argument and uses it during creation and sends it further to tracks
    public void load(final DrumMachineKeeper k){
        for(int seqNo = 0; seqNo < sequences.size(); seqNo++){
            sequences.get(seqNo).load(k.sequenceKeepers.get(seqNo));
        }
        sequenceManager.load(k.sequenceManagerKeeper);
    }

    public DrumMachineKeeper getKeeper(){
        //Log.e("DrumMachine", "getkEEPER()");
        DrumMachineKeeper keeper = new DrumMachineKeeper();
        keeper.initTempo = sequences.get(0).getTempo(); //since sequence 0 always is active on startup we do this
        keeper.initSteps = sequences.get(0).getNOfSteps(); //since sequence 0 always is active on startup we do this
        keeper.sequenceManagerKeeper = sequenceManager.getKeeper();

        keeper.sequenceKeepers = new ArrayList<>();
        for(DrumSequence ds : sequences){
            keeper.sequenceKeepers.add(ds.getKeeper());
        }
        return keeper;
    }

    /** DESTRUCTION **/
    public void destroy(){
        for(DrumSequence ds : sequences){
            ds.destroy();
        }
    }
}
