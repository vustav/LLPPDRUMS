package com.kiefer.machine.sequence;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.engine.EngineFacade;
import com.kiefer.files.keepers.DrumSequenceKeeper;
import com.kiefer.files.keepers.DrumTrackKeeper;
import com.kiefer.files.keepers.rndSeqManager.RndSeqManagerKeeper;
import com.kiefer.interfaces.Tempoizer;
import com.kiefer.popups.nameColor.NamerColorizer;
import com.kiefer.randomization.rndSeqManager.RndSeqManager;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.randomization.rndTrackManager.RndTrackManager;
import com.kiefer.ui.tabs.interfaces.TabHolder;
import com.kiefer.ui.tabs.interfaces.Tab;
import com.kiefer.machine.sequence.sequenceModules.OnOff;
import com.kiefer.machine.sequence.sequenceModules.Pan;
import com.kiefer.machine.sequence.sequenceModules.Pitch;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.machine.sequence.sequenceModules.Volume;
import com.kiefer.utils.ColorUtils;
import com.kiefer.utils.ImgUtils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Tracks have an OscillatorManager with two Oscillators holding a SynthInstrument each. Drums
 * add/remove themselves by creating AudioEvents on those instruments. When an oscillator is turned
 * off or another Sequence is played the SynthInstruments are only muted, not deleted. AudioEvents
 * are only deleted when removed. Muted Instruments shouldn't stress the cpu.
 *
 * A Sequence holds all the tracks plus the sequencerModule. A Sequencer module decides what's shown in the sequencer and the Tracks holds all the info about themselves.
 * **/

public class DrumSequence implements TabHolder, Tab, Tempoizer, NamerColorizer {

    private final LLPPDRUMS llppdrums;
    private final EngineFacade engineFacade;

    private String sequenceName;
    private ArrayList<DrumTrack> tracks;

    //rnd
    private final RndSeqManager rndSeqManager; //handles sequence-wide randomization. Tracks have their own.

    //engine
    private int tempo;

    //bitmaps
    //private final int tabImgId;
    //private final Bitmap tabBitmap, bgBitmap;
    private final int bitmapId;
    private int tabColor;
    private Drawable backgroundGradient;

    //gradients
    private final GradientDrawable stepsGradientDrawable, tempoGradientDrawable, randomGradientDrawable, copyGradientDrawable;

    //gfx
    //private final LinearLayout fxBtnGraphics, mixerBtnGraphics;

    //bgs
    private final int copyFromBgId, tempoBgId, subsBgId, trackRndManagerBg;

    //sequencer modules
    private SequenceModule selectedSequenceModule;
    private ArrayList<SequenceModule> sequenceModules;

    public DrumSequence(LLPPDRUMS llppdrums, EngineFacade engineFacade, int tabIndex){
        this(llppdrums, engineFacade, tabIndex, null);
    }

    public DrumSequence(LLPPDRUMS llppdrums, EngineFacade engineFacade, int tabIndex, DrumSequenceKeeper keeper){
        this.llppdrums = llppdrums;
        this.engineFacade = engineFacade;

        bitmapId = ImgUtils.getRandomImageId();
        //tabColor = ColorUtils.getRandomColor();
        //backgroundGradient = ColorUtils.getGradientDrawable(tabColor, ColorUtils.getRandomColor(), Tab.HORIZONTAL);

        stepsGradientDrawable = ColorUtils.getRandomGradientDrawable(ColorUtils.getRandomColor(), ColorUtils.getRandomColor());
        tempoGradientDrawable = ColorUtils.getRandomGradientDrawable(ColorUtils.getRandomColor(), ColorUtils.getRandomColor());
        randomGradientDrawable = ColorUtils.getRandomGradientDrawable(ColorUtils.getRandomColor(), ColorUtils.getRandomColor());
        copyGradientDrawable = ColorUtils.getRandomGradientDrawable(ColorUtils.getRandomColor(), ColorUtils.getRandomColor());

        copyFromBgId = ImgUtils.getRandomImageId();
        tempoBgId = ImgUtils.getRandomImageId();
        subsBgId = ImgUtils.getRandomImageId();
        trackRndManagerBg = ImgUtils.getRandomImageId();

        String name;
        Random random = new Random();
        if(keeper != null){
            try {
                name = keeper.name;
            }
            catch (Exception e){
                name = Integer.toString(random.nextInt(100));;
            }
        }
        else{
            name = Integer.toString(random.nextInt(100));
        }
        sequenceName = name;

        int tabClr;
        if(keeper != null){
            try {
                tabClr = keeper.color;
            }
            catch (Exception e){
                tabClr = ColorUtils.getRandomColor();
            }
        }
        else{
            tabClr = ColorUtils.getRandomColor();
        }
        //Log.e("DrumSequence", "keeper.tabColor, color: "+keeper.tabColor);
        //Log.e("DrumSequence", "constr, color: "+tabClr);
        tabColor = tabClr;
        //tabColor = ColorUtils.getRandomColor();
        backgroundGradient = ColorUtils.getGradientDrawable(tabColor, ColorUtils.getRandomColor(), Tab.HORIZONTAL);

        //use default tempo if no keeper
        int t;
        if(keeper != null){
            try {
                t = keeper.tempo;
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

        setupSequenceModules();

        //use default nOfTracks if no keeper
        int nOfTracks;
        if (keeper != null) {
            nOfTracks = keeper.nOfTracks;
            nOfSteps = keeper.nOfSteps;
        }
        else {
            nOfTracks = llppdrums.getResources().getInteger(R.integer.defaultNOfTracks);
            nOfSteps = llppdrums.getResources().getInteger(R.integer.defaultNOfSteps);
        }
        setupTracks(nOfTracks, keeper);

        RndSeqManagerKeeper rndSeqManagerKeeper;
        if(keeper != null) {
            rndSeqManagerKeeper = keeper.rndSeqManagerKeeper;
        }
        else{
            rndSeqManagerKeeper = null;
        }
        rndSeqManager = new RndSeqManager(llppdrums, this, tracks.get(0).getSoundManager().getPresetCategories(), rndSeqManagerKeeper);

        deactivate();
    }
    private int nOfSteps;

    /** SETUP **/
    /** ALLA BEHÖVER ITNE VARSINA. SKULLE KUNNA FLYTTA TILL DrumMachine. **/
    private void setupSequenceModules(){
        sequenceModules = new ArrayList<>();

        //create the necessary Bitmaps before creating the modules and adding them to the array
        //int imgId = ImgUtils.getRandomImageId();
        //Bitmap tabBitmap = ImgUtils.getTabBitmap(llppdrums, imgId, 0, 4, TabManager.VERTICAL);
        //Bitmap bgBitmap = ImgUtils.getBgBitmap(llppdrums, imgId, TabManager.VERTICAL);
        sequenceModules.add(new OnOff(llppdrums, this, 0));

        //imgId = ImgUtils.getRandomImageId();
        //tabBitmap = ImgUtils.getTabBitmap(llppdrums, imgId, 1, 4, TabManager.VERTICAL);
        //bgBitmap = ImgUtils.getBgBitmap(llppdrums, imgId, TabManager.VERTICAL);
        sequenceModules.add(new Volume(llppdrums, this, 1));

        //imgId = ImgUtils.getRandomImageId();
        //tabBitmap = ImgUtils.getTabBitmap(llppdrums, imgId, 2, 4, TabManager.VERTICAL);
        //bgBitmap = ImgUtils.getBgBitmap(llppdrums, imgId, TabManager.VERTICAL);
        sequenceModules.add(new Pitch(llppdrums, this, 2));

        //imgId = ImgUtils.getRandomImageId();
        //tabBitmap = ImgUtils.getTabBitmap(llppdrums, imgId, 3, 4, TabManager.VERTICAL);
        //bgBitmap = ImgUtils.getBgBitmap(llppdrums, imgId, TabManager.VERTICAL);
        sequenceModules.add(new Pan(llppdrums, this, 3));

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
                //llppdrums.getDrumMachineFragment().getTabManager().showIcon(llppdrums.getDrumMachine().getSequences().indexOf(this), true);
                llppdrums.getDrumMachineFragment().showPlayIcon(llppdrums.getDrumMachine().getSequences().indexOf(this), true);
            }

        //activate each track
        for(final DrumTrack track : tracks){
            track.activate();
        }
    }

    public void deactivate(){
        if(llppdrums.getDrumMachine() != null)
            //Log.e("DrumSequence", "----deactivate()----, seq: " + llppdrums.getDrumMachine().getSequences().indexOf(this));

            //turn off the little play-icon in the tab (will get called when new sequences are playing BEFORE their viewHolder is created so do the playingSeq check as well)
            if(engineFacade.isPlaying() && llppdrums.getDrumMachine().getPlayingSequence() == this) {
                llppdrums.getDrumMachineFragment().showPlayIcon(llppdrums.getDrumMachine().getSequences().indexOf(this), false);

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
            llppdrums.getDrumMachineFragment().update();

            if(getNOfSteps() > 1){
                llppdrums.getSequencerUI().setRemoveStepBtnEnabled(true);
            }
            else{
                llppdrums.getSequencerUI().setRemoveStepBtnEnabled(false);
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
        DrumTrack drumTrack;

        if (keeper != null) {
            DrumTrackKeeper dtk = keeper.drumTrackKeepers.get(tracks.size());
            drumTrack = new DrumTrack(llppdrums, this, getNOfSteps(), dtk);
        }
        //if keeper is null it's either no keeper (use default), or a track is added by a btn (no keeper provided, but use getNOfSteps())
        else{
            drumTrack = new DrumTrack(llppdrums, this, getNOfSteps(), null);

        }
        tracks.add(drumTrack);

        //drumTrack.deactivate();

        //activate the track to add it and its drums to the engine if this sequence is playing
        if(llppdrums.getDrumMachine() != null) {
            if(llppdrums.getDrumMachine().getSelectedSequence() == this) {
                llppdrums.getSequencerUI().addTrack();
            }
            if (llppdrums.getDrumMachine().getPlayingSequence() == this) {
                drumTrack.activate();
            }
        }
    }

    /*
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

     */

    public void removeTrack(final int trackNo){
        if(tracks.size() > 1) {
            //destroyTrack(trackNo);
            //Log.e("DrumSequence", "removeTrack()");
            DrumTrack dt = tracks.remove(trackNo);
            dt.destroy();

            if(this == llppdrums.getDrumMachine().getSelectedSequence()) {
                llppdrums.getSequencerUI().removeTrack(trackNo);
            }

            //dt.destroy();
        }

        if(!llppdrums.getEngineFacade().isPlaying()){
            llppdrums.getDeleter().delete();
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
        nOfSteps++;
        //add a step to the engine if this sequence is playing
        if (this == llppdrums.getDrumMachine().getPlayingSequence()) {
            engineFacade.updateNOfSteps();
        }

        //create the drums for the step (the drum adds itself to the sequencer if this sequence is selected)
        for (DrumTrack drumTrack : tracks) {
            drumTrack.addStep();
            drumTrack.positionEvents();
        }

        //update the sequencerUI
        if(this == llppdrums.getDrumMachine().getSelectedSequence()) {
            llppdrums.getSequencerUI().addStep();
        }

        //seems AudioEvents aren't added to the sequencer without this...
        setTempo(tempo);
        setNOfSteps(getNOfSteps());

    }

    public void removeStep(){
        nOfSteps--;
        //if (getNOfSteps() > 1) {
            if (this == llppdrums.getDrumMachine().getPlayingSequence()) {
                engineFacade.updateNOfSteps();
            }

            for(DrumTrack drumTrack : tracks){
                drumTrack.removeStep();
                drumTrack.positionEvents();
            }

            if(this == llppdrums.getDrumMachine().getSelectedSequence()) {
                llppdrums.getSequencerUI().removeStep();
            }

            //not sure if needed here but seems to be needed when adding
            setTempo(tempo);
            setNOfSteps(getNOfSteps());

        //}

        if(!llppdrums.getEngineFacade().isPlaying()){
            llppdrums.getDeleter().delete();
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
                    //llppdrums.getDrumMachineFragment().setTempo(getTempo());
                    llppdrums.getDrumMachineFragment().setTempo();
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

    /** SET **/
    @Override
    public void setColor(int color){
        tabColor = color;
        llppdrums.getDrumMachine().getSequenceManager().updateSequenceColor(this);

        backgroundGradient = ColorUtils.getGradientDrawable(tabColor, ColorUtils.getRandomColor(), Tab.HORIZONTAL);

        if(this == llppdrums.getDrumMachine().getSelectedSequence()) {
            llppdrums.getDrumMachineFragment().setColor(true);
        }
    }

    @Override
    public void setName(String name){
        sequenceName = name;
        llppdrums.getDrumMachine().getSequenceManager().updateSequenceName(this);

        //llppdrums.getDrumMachineFragment().setTabName(getTabIndex());

        if(this == llppdrums.getDrumMachine().getSelectedSequence()){
            llppdrums.getDrumMachineFragment().setName(true);
        }
    }

    /** GET **/
    //TABS
    @Override
    public String getName(){
        return sequenceName;
    }

    //@Override
    //public Bitmap getTabBitmap(){
    //return tabBitmap;
    //}

    @Override
    public int getBitmapId(){
        return bitmapId;
    }

    @Override
    public int getColor(){
        return tabColor;
    }

    public Drawable getBackgroundGradient(){
        return backgroundGradient;
    }

    public int getBitmapId(int tier){
        if(tier == 0){
            return selectedSequenceModule.getBitmapId();
        }
        if(tier == 1){
            return selectedSequenceModule.getSelectedMode().getBitmapId();
        }
        else{
            return getBitmapId();
        }
    }

    @Override
    public int getOrientation(){
        return Tab.VERTICAL;
    }
/*
    public int getTabImgId() {
        return tabImgId;
    }

 */

    public ArrayList<Tab> getTabs(int tierNo){
        ArrayList<Tab> tabs = new ArrayList<>();
        tabs.addAll(sequenceModules);
        return tabs;
    }

    //INFO
    public String getFullName(){
        return llppdrums.getResources().getString(R.string.seqName)+": "+ getName() + llppdrums.getResources().getString(R.string.stringDivider) + selectedSequenceModule.getFullName();
    }

    public int getNOfTracks(){
        return tracks.size();
    }

    public int getNOfSteps(){
        //if(tracks.size() != 0) {
        //return tracks.get(0).getNOfSteps();
        //}
        //return -1;
        return nOfSteps;
    }

    //OBJECTS
    public ArrayList<SequenceModule> getSequenceModules() {
        return sequenceModules;
    }

    public SequenceModule getSelectedSequenceModule() {
        return selectedSequenceModule;
    }

    public int getSelectedSequenceModuleIndex() {
        return sequenceModules.indexOf(selectedSequenceModule);
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

    public boolean isInBaseMode(){
        return selectedSequenceModule.isInBaseMode();
    }

    //TABS
    @Override
    public int getTabIndex() {

        //initTabIndex is set on restore on startup and used to update tabs before DumMachine is done. Never updated again after that.
        if(llppdrums.getDrumMachine() == null){
            return initTabIndex;
        }

        return llppdrums.getDrumMachine().getSequences().indexOf(this);
    }

    @Override
    public int getTier(){
        /** NOT USED AS TABS ANYMORE WITH THE RecyclerView. Consider removing this **/
        return -666;
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

    /** RETURN MEMORY **/
    //keep track if automations are going to be returned, otherwise don't do unnecessary loops

    int returns = 0;
    public void returnModified(boolean returnValue){
        if(returnValue){
            returns++;
        }
        else{
            returns--;
        }
    }

    public boolean returnActive() {
        return returns > 0;
    }

    /** AUTOMATION MEMORY **/
    int automations = 0;
    public void automationsModified(boolean automationValue){
        if(automationValue){
            automations++;
        }
        else{
            automations--;
        }
    }

    public boolean automationsActive() {
        return automations > 0;
    }

    /** SEQUENCER UPDATES **/
    public void handleSequencerPositionChange(int sequencerPosition){

        /** REWORK THIS IF TOO HEAVY **/
        if(returnActive()) {
            if(sequencerPosition == 0) {
                rndSeqManager.returnAutoRandomizations(getNOfSteps() - 1);
            }
            else{
                rndSeqManager.returnAutoRandomizations(sequencerPosition - 1);
            }
        }

        if(sequencerPosition == 0 && automationsActive()){
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

    private int initTabIndex; //set on restore on startup and used to update tabs before DumMachine is done. Never updated again after that.

    public void restore(DrumSequenceKeeper keeper){
        setTempo(keeper.tempo);
        for(int trackNo = 0; trackNo < tracks.size(); trackNo++){
            tracks.get(trackNo).restore(keeper.drumTrackKeepers.get(trackNo));
        }
        initTabIndex = keeper.tabIndex;
        selectedSequenceModule = sequenceModules.get(keeper.selectedModule);

        for(int i = 0; i<keeper.sequenceModuleKeepers.size(); i++){
            sequenceModules.get(i).restore(keeper.sequenceModuleKeepers.get(i));
        }

        rndSeqManager.restore(keeper.rndSeqManagerKeeper);
    }

    public DrumSequenceKeeper getKeeper(){
        DrumSequenceKeeper keeper = new DrumSequenceKeeper();

        //Log.e("DrumSequence", "getKeeper, color: "+getColor());
        keeper.color = getColor();

        keeper.sequenceModuleKeepers = new ArrayList<>();
        for(SequenceModule sm : sequenceModules){
            keeper.sequenceModuleKeepers.add(sm.getKeeper());
        }

        keeper.selectedModule = getSelectedSequenceModuleIndex();
        keeper.tabIndex = getTabIndex();
        keeper.name = sequenceName;
        keeper.nOfTracks = getNOfTracks();
        keeper.nOfSteps = getNOfSteps();
        keeper.tempo = getTempo();
        keeper.drumTrackKeepers = new ArrayList<>();
        for(DrumTrack dt : tracks){
            keeper.drumTrackKeepers.add(dt.getKeeper());
        }

        keeper.rndSeqManagerKeeper = rndSeqManager.getKeeper();

        return keeper;
    }

    /** DESTRUCTION **/
    public void destroy(){
        for(DrumTrack dt : tracks){
            dt.destroy();
        }
    }
}
