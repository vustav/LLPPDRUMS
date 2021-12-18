package com.kiefer.machine.sequence;

import android.graphics.Bitmap;
import android.graphics.drawable.GradientDrawable;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.engine.EngineFacade;
import com.kiefer.files.keepers.DrumSequenceKeeper;
import com.kiefer.files.keepers.DrumTrackKeeper;
import com.kiefer.interfaces.Tempoizer;
import com.kiefer.randomization.rndSeqManager.RndSeqManager;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.randomization.rndTrackManager.RndTrackManager;
import com.kiefer.ui.tabs.TabManager;
import com.kiefer.ui.tabs.interfaces.TabHoldable;
import com.kiefer.ui.tabs.interfaces.Tabable;
import com.kiefer.machine.sequence.sequenceModules.OnOff;
import com.kiefer.machine.sequence.sequenceModules.Pan;
import com.kiefer.machine.sequence.sequenceModules.Pitch;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.machine.sequence.sequenceModules.Volume;
import com.kiefer.utils.ColorUtils;
import com.kiefer.utils.ImgUtils;

import java.util.ArrayList;

/**
 * Tracks have an OscillatorManager with two Oscillators holding a SynthInstrument each. Drums
 * add/remove themselves by creating AudioEvents on those instruments. When an oscillator is turned
 * off or another Sequence is played the SynthInstruments are only muted, not deleted. AudioEvents
 * are only deleted when removed. Muted Instruments shouldn't stress the cpu.
 *
 * A Sequence holds all the tracks plus the sequencerModule. A Sequencer module decides what's shown in the sequencer and the Tracks holds all the info about themselves.
 * **/

public class DrumSequence implements TabHoldable, Tabable, Tempoizer {

    private final LLPPDRUMS llppdrums;
    private final EngineFacade engineFacade;

    private final String sequenceName;
    private ArrayList<DrumTrack> tracks;

    //rnd
    private final RndSeqManager rndSeqManager; //handles sequence-wide randomization. Tracks have their own.

    //engine
    private int tempo;

    //bitmaps
    //private final int tabImgId;
    private final Bitmap tabBitmap, bgBitmap;

    //gradients
    private final GradientDrawable stepsGradientDrawable, tempoGradientDrawable, randomGradientDrawable, copyGradientDrawable;

    //gfx
    //private final LinearLayout fxBtnGraphics, mixerBtnGraphics;

    //bgs
    private final int copyFromBgId, tempoBgId, subsBgId, trackRndManagerBg;

    //sequencer modules
    private SequenceModule selectedSequenceModule;
    private ArrayList<SequenceModule> sequenceModules;

    public DrumSequence(LLPPDRUMS llppdrums, EngineFacade engineFacade, int seqNo, Bitmap tabBitmap, Bitmap bgBitmap){
        this(llppdrums, engineFacade, seqNo, tabBitmap, bgBitmap, null);
    }

    public DrumSequence(LLPPDRUMS llppdrums, EngineFacade engineFacade, int seqNo, Bitmap tabBitmap, Bitmap bgBitmap, DrumSequenceKeeper keeper){
        this.llppdrums = llppdrums;
        this.engineFacade = engineFacade;
        sequenceName = Integer.toString(seqNo);

        stepsGradientDrawable = ColorUtils.getRandomGradientDrawable(ColorUtils.getRandomColor(), ColorUtils.getRandomColor());
        tempoGradientDrawable = ColorUtils.getRandomGradientDrawable(ColorUtils.getRandomColor(), ColorUtils.getRandomColor());
        //subsGradientDrawable = ColorUtils.getRandomGradientDrawable(ColorUtils.getRandomColor(), ColorUtils.getRandomColor());
        randomGradientDrawable = ColorUtils.getRandomGradientDrawable(ColorUtils.getRandomColor(), ColorUtils.getRandomColor());
        copyGradientDrawable = ColorUtils.getRandomGradientDrawable(ColorUtils.getRandomColor(), ColorUtils.getRandomColor());

        copyFromBgId = ImgUtils.getRandomImageId();
        tempoBgId = ImgUtils.getRandomImageId();
        subsBgId = ImgUtils.getRandomImageId();
        trackRndManagerBg = ImgUtils.getRandomImageId();

        //fxBtnGraphics = new DrumTrackFxBtnGraphics(llppdrums).getLayout();
        //mixerBtnGraphics = new DrumTrackMixBtnGraphics(llppdrums).getLayout();

        this.tabBitmap = tabBitmap;
        this.bgBitmap = bgBitmap;

        //use default tempo if no keeper
        int t;
        if(keeper != null){
            try {
                t = Integer.parseInt(keeper.tempo);
            }
            catch (Exception e){
                //Log.e("DrumSequence", e.getMessage());
                t = llppdrums.getResources().getInteger(R.integer.defaultTempo);
            }
        }
        else{
            t = llppdrums.getResources().getInteger(R.integer.defaultTempo);
        }
        tempo = t;

        /*
        //same with subs
        int s;
        if(keeper != null){
            try {
                s = Integer.parseInt(keeper.subs);
            }
            catch (Exception e){
                //Log.e("DrumSequence", e.getMessage());
                s = llppdrums.getResources().getInteger(R.integer.defaultNOfSubs);
            }
        }
        else{
            s = llppdrums.getResources().getInteger(R.integer.defaultNOfSubs);
        }
        subs = s;

         */

        setupSequenceModules();

        //use default nOfTracks if no keeper
        int nOfTracks;
        if (keeper != null) {
            nOfTracks = keeper.nOfTracks;
        }
        else {
            nOfTracks = llppdrums.getResources().getInteger(R.integer.defaultNOfTracks);
        }
        setupTracks(nOfTracks, keeper);

        rndSeqManager = new RndSeqManager(llppdrums, this, tracks.get(0).getSoundManager().getPresetCategories()); //track doesn't amtter, just get any presets
    }

    /** SETUP **/
    private void setupSequenceModules(){
        sequenceModules = new ArrayList<>();

        //create the necessary Bitmaps before creating the modules and adding them to the array
        int imgId = ImgUtils.getRandomImageId();
        Bitmap tabBitmap = ImgUtils.getTabImg(llppdrums, imgId, 0, 4, TabManager.VERTICAL);
        Bitmap bgBitmap = ImgUtils.getBgImg(llppdrums, imgId, TabManager.VERTICAL);
        sequenceModules.add(new OnOff(llppdrums, this, tabBitmap, bgBitmap));

        imgId = ImgUtils.getRandomImageId();
        tabBitmap = ImgUtils.getTabImg(llppdrums, imgId, 1, 4, TabManager.VERTICAL);
        bgBitmap = ImgUtils.getBgImg(llppdrums, imgId, TabManager.VERTICAL);
        sequenceModules.add(new Volume(llppdrums, this, tabBitmap, bgBitmap));

        imgId = ImgUtils.getRandomImageId();
        tabBitmap = ImgUtils.getTabImg(llppdrums, imgId, 2, 4, TabManager.VERTICAL);
        bgBitmap = ImgUtils.getBgImg(llppdrums, imgId, TabManager.VERTICAL);
        sequenceModules.add(new Pitch(llppdrums, this, tabBitmap, bgBitmap));

        imgId = ImgUtils.getRandomImageId();
        tabBitmap = ImgUtils.getTabImg(llppdrums, imgId, 3, 4, TabManager.VERTICAL);
        bgBitmap = ImgUtils.getBgImg(llppdrums, imgId, TabManager.VERTICAL);
        sequenceModules.add(new Pan(llppdrums, this, tabBitmap, bgBitmap));

        selectedSequenceModule = sequenceModules.get(0);
    }

    private void setupTracks(int nOfTracks, DrumSequenceKeeper keeper){
        tracks = new ArrayList<>();
        for(int i = 0; i<nOfTracks; i++) {
            addTrack(keeper);
        }
    }

    /** ACTIVATION **/
    //activation means when a sequence is playing, has nothing to do with witch is shown in the UI
    public void activate(){
        if(llppdrums.getDrumMachine() != null)
            //Log.e("DrumSequence", "++++activate()++++, seq: " + llppdrums.getDrumMachine().getSequences().indexOf(this));

            //turn on the little play-icon in the tab
            if(engineFacade.isPlaying()) {
                llppdrums.getDrumMachineFragment().getTabManager().showIcon(llppdrums.getDrumMachine().getSequences().indexOf(this), true);
            }

        //activate each track
        for(final DrumTrack track : tracks){
            track.activate();
        }
    }

    public void deactivate(){
        if(llppdrums.getDrumMachine() != null)
            //Log.e("DrumSequence", "----deactivate()----, seq: " + llppdrums.getDrumMachine().getSequences().indexOf(this));

            //turn off the little play-icon in the tab
            if(engineFacade.isPlaying()) {
                llppdrums.getDrumMachineFragment().getTabManager().showIcon(llppdrums.getDrumMachine().getSequences().indexOf(this), false);

                //unlock the UI if THIS seq is selected on deactivation
                //if(llppdrums.getDrumMachine().getSelectedSequence() == this){
                //llppdrums.getSequencer().unlockUI();
                //}
            }

        for(final DrumTrack track : tracks){
            track.deactivate();
        }
    }

    /** SELECTION **/
    //selection means when a tab is clicked, has nothing to du with witch is played

    //on selection, "select" the selected module to make it draw its Drawable in the sequencer
    public void select(){
        setSelectedSequenceModule(sequenceModules.indexOf(selectedSequenceModule));

        //update the spinner in the fragment
        if(llppdrums.getDrumMachineFragment() != null) {
            llppdrums.getDrumMachineFragment().setTempo(tempo);
            //llppdrums.getDrumMachineFragment().setSubs(subs);

            if(getNOfSteps() > 1){
                llppdrums.getSequencer().setRemoveStepBtnEnabled(true);
            }
            else{
                llppdrums.getSequencer().setRemoveStepBtnEnabled(false);
            }
        }

        for(DrumTrack dt : tracks){
            dt.select();
        }

        llppdrums.getDrumMachineFragment().setTempoGradient(tempoGradientDrawable);
        llppdrums.getDrumMachineFragment().setRndGradient(randomGradientDrawable);
        llppdrums.getDrumMachineFragment().setCopyGradient(copyGradientDrawable);

    }

    public void deselect(){
        /** TA BORT GAMLA DRAWABLES FRÅN MINNET?? **/
        selectedSequenceModule.deselect();

        /*
        //remove the gfx from the btns
        if (fxBtnGraphics.getParent() != null) {
            ((ViewGroup) fxBtnGraphics.getParent()).removeView(fxBtnGraphics);
        }

        if (mixerBtnGraphics.getParent() != null) {
            ((ViewGroup) mixerBtnGraphics.getParent()).removeView(mixerBtnGraphics);
        }

         */
    }

    public void setSelectedSequenceModule(int moduleNo){
        selectedSequenceModule.deselect();
        SequenceModule sm = sequenceModules.get(moduleNo);
        selectedSequenceModule = sm;
        sm.select();
    }

    /** SEQ-EVENTS **/
    //TRACKS
    public void moveTrack(int from, int to){
        DrumTrack dt = tracks.remove(from);
        tracks.add(to, dt);
    }

    public void addTrack(){
        addTrack(null);
    }
    public void addTrack(DrumSequenceKeeper keeper){
        //create a drumTrack
        DrumTrack drumTrack;

        if (keeper != null) {
            DrumTrackKeeper dtk = keeper.drumTrackKeepers.get(tracks.size());
            drumTrack = new DrumTrack(llppdrums, this, keeper.nOfSteps, dtk);
        }
        //if keeper is null it's either no keeper (use default), or a track is added by a btn (no keeper provided, but use getNOfSteps())
        else{
            if(tracks.size() != 0) {
                drumTrack = new DrumTrack(llppdrums, this, getNOfSteps(), null);
            }
            else{
                drumTrack = new DrumTrack(llppdrums, this, llppdrums.getResources().getInteger(R.integer.defaultNOfSteps), null);
            }
        }
        tracks.add(drumTrack);

        //drumTrack.deactivate();

        //activate the track to add it and its drums to the engine if this sequence is playing
        if(llppdrums.getDrumMachine() != null) {
            if(llppdrums.getDrumMachine().getSelectedSequence() == this) {
                llppdrums.getSequencer().addTrack();
            }
            if (llppdrums.getDrumMachine().getPlayingSequence() == this) {
                drumTrack.activate();
            }
        }
    }

    public void removeTrack(final int trackNo){
        if(tracks.size() > 1) {
            //destroyTrack(trackNo);
            //Log.e("DrumSequence", "removeTrack()");
            DrumTrack dt = tracks.remove(trackNo);
            dt.destroy();

            if(this == llppdrums.getDrumMachine().getSelectedSequence()) {
                llppdrums.getSequencer().removeTrack(trackNo);
            }

            //dt.destroy();
        }
    }

    public void removeLastTrack(){
        if(tracks.size() > 1) {
            int trackNo = tracks.size() - 1;
            removeTrack(trackNo);
        }
    }

    public void setNOfTracks(int n){
        while (n < getNOfTracks()){
            removeLastTrack();
        }
        while (n > getNOfTracks()){
            addTrack();
        }
    }

    //STEPS
    public void setNOfSteps(int n){
        while (n < getNOfSteps()){
            removeStep();
        }
        while (n > getNOfSteps()){
            addStep();
        }
    }

    public void addStep(){
        //add a step to the engine if this sequence is playing
        if (this == llppdrums.getDrumMachine().getPlayingSequence()) {
            engineFacade.addStep();
        }

        //create the drums for the step (the drum adds itself to the sequencer if this sequence is selected)
        for (DrumTrack drumTrack : tracks) {
            drumTrack.addStep(getNOfSteps());
            drumTrack.positionDrums();
        }

        //update the sequencerUI
        if(this == llppdrums.getDrumMachine().getSelectedSequence()) {
            llppdrums.getSequencer().addStep();
        }

        //seems AudioEvents aren't added to the sequencer without this...
        setTempo(tempo);
        setNOfSteps(getNOfSteps());
    }

    public void removeStep(){
        if (getNOfSteps() > 1) {
            if (this == llppdrums.getDrumMachine().getPlayingSequence()) {
                engineFacade.removeStep();
            }

            for(DrumTrack drumTrack : tracks){
                drumTrack.removeDrum();
                drumTrack.positionDrums();
            }

            if(this == llppdrums.getDrumMachine().getSelectedSequence()) {
                llppdrums.getSequencer().removeStep();
            }

            //not sure if needed here but seems to be needed when adding
            setTempo(tempo);
            setNOfSteps(getNOfSteps());
        }
    }

    @Override
    public void setTempo(int tempo){
        this.tempo = tempo;
        if(llppdrums.getDrumMachine() != null) {
            if (llppdrums.getDrumMachine().getPlayingSequence() == this) {
                engineFacade.setTempo(tempo);
            }
            if(llppdrums.getDrumMachine().getSelectedSequence() == this){
                if(llppdrums.getDrumMachineFragment() != null) {
                    llppdrums.getDrumMachineFragment().setTempo(getTempo());
                }
            }
        }
    }

    public void lowerTempo(){
        if(getTempo() > llppdrums.getResources().getInteger(R.integer.minTempo)) {
            setTempo(getTempo() - 1);
        }
    }

    public void raiseTempo(){
        if(getTempo() < llppdrums.getResources().getInteger(R.integer.maxTempo)) {
            setTempo(getTempo() + 1);
        }
    }

    /** RANDOMIZE **/
    //called when rndSeq-btn is pressed
    public void randomize(){
        rndSeqManager.randomizeSequence();
    }

    /** GET **/
    //TABS
    @Override
    public String getLabel(){
        return sequenceName;
    }
    @Override
    public Bitmap getTabBitmap(){
        return tabBitmap;
    }

    @Override
    public Bitmap getBgBitmap(){
        return bgBitmap;
    }
/*
    public int getTabImgId() {
        return tabImgId;
    }

 */

    public ArrayList<Tabable> getTabables(int tierNo){
        ArrayList<Tabable> tabs = new ArrayList<>();
        tabs.addAll(sequenceModules);
        return tabs;
    }

    //INFO
    public String getFullName(){
        return llppdrums.getResources().getString(R.string.seqName)+": "+ getLabel() + llppdrums.getResources().getString(R.string.stringDivider) + selectedSequenceModule.getFullName();
    }

    public int getNOfTracks(){
        return tracks.size();
    }

    public int getNOfSteps(){
        if(tracks.size() != 0) {
            return tracks.get(0).getNOfSteps();
        }
        return -1;
    }

    //OBJECTS
    public ArrayList<SequenceModule> getSequenceModules() {
        return sequenceModules;
    }

    public SequenceModule getSelectedSequenceModule() {
        return selectedSequenceModule;
    }

    public int getModuleIndex(SequenceModule module){
        return sequenceModules.indexOf(module);
    }

    public ArrayList<DrumTrack> getTracks() {
        //Log.e("DrumSequence", "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
        //Log.e("DrumSequence", "getTracks(), size: "+tracks.size());
        //Log.e("DrumSequence", "vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv");
        return tracks;
    }

    public RndTrackManager[] getDrumRandomizers(){
        RndTrackManager[] randomizers = new RndTrackManager[tracks.size()];

        for(int track = 0; track < tracks.size(); track++){
            randomizers[track] = tracks.get(track).getRndTrackManager();
        }

        return randomizers;
    }

    public RndSeqManager getRandomizeManager(){
        return rndSeqManager;
    }

    public int getTempo(){
        return tempo;
    }

    public int getTempoBgId() {
        return tempoBgId;
    }

    public int getSubsBgId(){
        return subsBgId;
    }

    public int getTrackRndManagerBg() {
        return trackRndManagerBg;
    }

    public int getCopyFromBgId() {
        return copyFromBgId;
    }

    public GradientDrawable getCopyGradientDrawable() {
        return copyGradientDrawable;
    }

    public GradientDrawable getRandomGradientDrawable() {
        return randomGradientDrawable;
    }

    public GradientDrawable getStepsGradientDrawable() {
        return stepsGradientDrawable;
    }

    public GradientDrawable getTempoGradientDrawable() {
        return tempoGradientDrawable;
    }

    /*
    public LinearLayout getMixerBtnGraphics() {
        return mixerBtnGraphics;
    }

    public LinearLayout getFxBtnGraphics() {
        return fxBtnGraphics;
    }

     */

    /** SET **/

    /** STATS **/
    /*
    public int getNOfEvents(){
        int nOfEvents = 0;
        for(DrumTrack dt : tracks){
            nOfEvents += dt.getNOfEvents();
        }
        return nOfEvents;
    }
    public int getNOfSequencedEvents(){
        int nOfEvents = 0;
        for(DrumTrack dt : tracks){
            nOfEvents += dt.getNOfSequencedEvents();
        }
        return nOfEvents;
    }

     */

    /** SEQUENCER UPDATES **/
    public void handleSequencerPositionChange(int sequencerPosition){
        if(sequencerPosition == 0){
            rndSeqManager.autoRandomize();
        }

        for (DrumTrack dt : tracks){
            dt.handleSequencerPositionChange(sequencerPosition);
        }
    }

    /** TRANSPORT **/
    public void play(){

    }

    public void stop(){
        for(DrumTrack dt : tracks){
            dt.stop();
        }
    }

    /** RESET **/
    public void reset(boolean updateDrawables){
        for(DrumTrack dt : tracks){
            dt.reset(updateDrawables);
        }
    }

    /** RESTORATION **/
    //when restoring we don't need to handle trackNo, since sequences are created with a keeper. On load do that first, then restore them.
    public void load(DrumSequenceKeeper keeper){
        setNOfSteps(keeper.nOfSteps);
        setNOfTracks(keeper.nOfTracks);

        restore(keeper);
    }

    public void restore(DrumSequenceKeeper keeper){
        setTempo(Integer.parseInt(keeper.tempo));
        for(int trackNo = 0; trackNo < tracks.size(); trackNo++){
            tracks.get(trackNo).restore(keeper.drumTrackKeepers.get(trackNo));
        }
    }

    public DrumSequenceKeeper getKeeper(){
        DrumSequenceKeeper keeper = new DrumSequenceKeeper();

        keeper.nOfTracks = getNOfTracks();
        keeper.nOfSteps = getNOfSteps();
        keeper.tempo = Integer.toString(getTempo());
        keeper.drumTrackKeepers = new ArrayList<>();
        for(DrumTrack dt : tracks){
            keeper.drumTrackKeepers.add(dt.getKeeper());
        }
        return keeper;
    }

    /** DESTRUCTION **/

    public void destroyTrack(int trackNo){
        tracks.get(trackNo).destroy();
    }

    public void destroy(){
        for(DrumTrack dt : tracks){
            dt.destroy();
        }
    }
}
