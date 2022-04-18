package com.kiefer.machine.fx;

import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.ViewGroup;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.files.keepers.fx.FxKeeper;
import com.kiefer.files.keepers.fx.FxManagerKeeper;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.popups.fxManager.FxManagerPopup;
import com.kiefer.utils.ImgUtils;

import java.util.ArrayList;
import java.util.Random;

import nl.igorski.mwengine.core.ProcessingChain;

public class FxManager {
    private LLPPDRUMS llppdrums;
    //private FxManagerUser fxManagerUser;
    private DrumTrack drumTrack;
    private ArrayList<Fx> fxs;
    private ArrayList<FxInfo> fxInfos;

    private Fx selectedFx;

    //IMGs
    private final int fxManagerImgId, fxPopupImgId;

    //rnd
    private final Random random;
    private final int MAX_N_RND_FXS = 2;

    //public FxManager(LLPPDRUMS llppdrums, FxManagerUser fxManagerUser){
    public FxManager(LLPPDRUMS llppdrums, DrumTrack drumTrack){
        this.llppdrums = llppdrums;
        this.drumTrack = drumTrack;
        //this.drumTrack = drumTrack;

        random = new Random();

        fxs = new ArrayList<>();
        fxManagerImgId = ImgUtils.getRandomImageId();
        fxPopupImgId = ImgUtils.getRandomImageId();

        fxInfos = new ArrayList<>();
        fxInfos.add(new FxInfo(llppdrums.getResources().getString(R.string.fxFlangerName), ContextCompat.getColor(llppdrums, R.color.fxFlangerColor)));
        fxInfos.add(new FxInfo(llppdrums.getResources().getString(R.string.fxDelayName), ContextCompat.getColor(llppdrums, R.color.fxDelayColor)));
        fxInfos.add(new FxInfo(llppdrums.getResources().getString(R.string.fxReverbName), ContextCompat.getColor(llppdrums, R.color.fxReverbColor)));
        fxInfos.add(new FxInfo(llppdrums.getResources().getString(R.string.fxBitCrusherName), ContextCompat.getColor(llppdrums, R.color.fxBitCrusherColor)));
        fxInfos.add(new FxInfo(llppdrums.getResources().getString(R.string.fxDecimatorName), ContextCompat.getColor(llppdrums, R.color.fxDecimatorColor)));

        //randomizeAll();
    }

    //has to be in the same order as fxInfos
    private Fx getNewFx(int index, boolean randomizeAutomation){
        switch (index){
            case 0:
                return new FxFlanger(llppdrums, drumTrack, index, randomizeAutomation);
            case 1:
                return new FxDelay(llppdrums, drumTrack, index, randomizeAutomation);
            case 2:
                return new FxReverb(llppdrums, drumTrack, index, randomizeAutomation);
            case 3:
                return new FxBitCrusher(llppdrums, drumTrack, index, randomizeAutomation);
            case 4:
                return new FxDecimator(llppdrums, drumTrack, index, randomizeAutomation);
        }
        return null;
    }

    private Fx createNewFx(int i, boolean randomizeAutomation){
        Fx fx = getNewFx(i, randomizeAutomation);
        selectedFx = fx; //created are always selected
        fxs.add(fx);
        return fx;
    }

    public void createRandomFx(boolean automation){
        Random r = new Random();
        Fx fx = createNewFx(r.nextInt(fxInfos.size()), automation);

        //if(llppdrums.getDrumMachine().getSelectedSequence() == llppdrums.getDrumMachine().getPlayingSequence()) {
        addFxToEngine(fx);
        //}
    }

    private void addFxsToEngine(){
        if(fxs != null) {
            for (Fx fx : fxs) {
                addFxToEngine(fx);
            }
        }
    }

    private void addFxToEngine(final Fx fx){
        for (ProcessingChain pc : drumTrack.getSoundManager().getProcessingChains()) {
            pc.addProcessor(fx.getBaseProcessor());
        }
        setIndicator();
    }

    public void moveFx(final int from, final int to){
        Fx fx = fxs.remove(from);
        fxs.add(to, fx);
        rearrangeFxs();
    }

    private void rearrangeFxs(){
        /** TA BORT TRÅD OM DET KRÅNGLAR **/
        //new Thread(new Runnable() {
        //public void run() {
        for(ProcessingChain pc : drumTrack.getSoundManager().getProcessingChains()) {
            if (pc != null) {
                //ProcessingChain processingChain = drumTrack.getSoundManager().getOscillators()[i].getSynthInstrument().getAudioChannel().getProcessingChain();
                pc.reset();

                for(Fx fx : fxs){
                    if(fx.isOn()) {
                        pc.addProcessor(fx.getBaseProcessor());
                    }
                }
            }
        }
        //}
        //}).start();
    }

    public void removeFx(int fxNo){
        Fx fx = fxs.remove(fxNo);
        destroyFx(fx);
    }

    public void destroyFx(Fx fx){
        removeFxFromEngine(fx);
        fx.destroy();
        if(fx == selectedFx){
            selectedFx = null;
        }
    }

    private void destroyFxs(){
        if(fxs != null) {
            for (Fx fx : fxs) {
                destroyFx(fx);
            }
        }

        //this one is added here since when randomizing from RndTrack Manager fxs doesn't always get removed properly, but it shouldn't be needed here
        //since destroyFx(fx) calls  removeFxFromEngine(fx);
        for(ProcessingChain pc : drumTrack.getSoundManager().getProcessingChains()){
            pc.reset();
        }
    }

    private void removeFxFromEngine(Fx fx){
        for(ProcessingChain pc : drumTrack.getSoundManager().getProcessingChains()){
            if(pc != null){
                pc.removeProcessor(fx.getBaseProcessor());
            }
            else{
                Log.e("FxManager","removeFxFromEngine(), pc == null");
            }
        }
        setIndicator();
    }

    private void removeFxsFromEngine(){
        if(fxs != null) {
            for (Fx fx : fxs) {
                removeFxFromEngine(fx);
            }
        }
    }

    public void changeSelectedFx(int newFxIndex){

        //create the new fx
        Fx newFx = getNewFx(newFxIndex, false);

        //set on to whatever the old fx had. No need to add the enw to the engine, it will be done in rearrangeFxs()
        newFx.setOn(selectedFx.isOn());

        //replace them in the array
        fxs.set(fxs.indexOf(selectedFx), newFx);

        //destroy the old one
        //selectedFx.destroy();
        destroyFx(selectedFx);

        //replace the variable
        selectedFx = newFx;

        //rearrange to add it in the right position in the engine
        rearrangeFxs();
    }

    public void turnSelectedFxOn(Boolean on){
        turnFxOn(selectedFx, on);
    }

    public void turnFxOn(Fx fx, Boolean on){
        //Log.e("FxManager", "fx: "+fxs.indexOf(selectedFx)+", "+on);
        fx.setOn(on);
        if(on){
            rearrangeFxs();
        }
        else {
            removeFxFromEngine(fx);
        }

        //turn on/off the indicator
        setIndicator();
    }

    private void setIndicator(){
        /*
        if (llppdrums.getSequencer() != null) {
            int i = 0;
            for (Fx fx : fxs) {
                if (fx.isOn()) {
                    i++;
                }
            }
            llppdrums.getSequencer().setTrackFxIndicator(drumTrack.getTrackNo(), i > 0);
        }

         */
    }

    public boolean isAnFxOn(){
        for (Fx fx : fxs) {
            if (fx.isOn()) {
                return true;
            }
        }
        return false;
    }

    public void releaseFxViews(){
        for(Fx fx : fxs){
            if(fx.getLayout().getParent() != null){
                ((ViewGroup)fx.getLayout().getParent()).removeView(fx.getLayout());
            }
        }
    }

    public void setSelectedFx(int index){
        selectedFx = fxs.get(index);
    }

    /** ACTIVATION **/
    // LAGGAR DET FÅR VI DESTROY OCH SKAPA NYA HÄR
    public void activate(){
        //addFxsToEngine();
        automate(llppdrums.getEngineFacade().getStep(), false);
    }

    private ArrayList<FxKeeper> keepers;
    public void deactivate(){
        //removeFxsFromEngine();
        resetAutomations();

    }

    /** SELECTION **/
    public void select(){
        //setIndicator();
    }

    public void deselect(){

    }

    /** STEPS **/
    public void addStep(){
        for(Fx fx : fxs){
            fx.addStep();
        }
    }

    public void removeStep(){
        for(Fx fx : fxs){
            fx.removeStep();
        }
    }

    /** RANDOMIZATION **/
    public void randomizeAll() {
        //Log.e("FxManager", "randomizeAll()");
        //try {
            destroy();
            fxs = new ArrayList<>();

            int nOfFxs = random.nextInt(MAX_N_RND_FXS + 1);

            for(int i = 0; i < nOfFxs; i++){
                createRandomFx(true);
            }

            if(fxs.size() > 0){
                selectedFx = fxs.get(0);
            }
            else{
                setIndicator(); //id < 0 this is calls in createRandomFx()
            }

        //}
        //catch (Exception e){
            //Log.e("FxManager.randomizeAll", e.getMessage());
        //}
    }

    /** GET **/

    public ArrayList<FxInfo> getFxInfos() {
        return fxInfos;
    }

    public int getFxManagerImgId() {
        return fxManagerImgId;
    }

    public int getFxPopupImgId() {
        return fxPopupImgId;
    }

    public ArrayList<Fx> getFxs(){
        return fxs;
    }

    public Fx getSelectedFx() {
        return selectedFx;
    }

    /** TRANSPORT **/
    public void play(){

    }

    public void stop(){
        for(Fx fx : fxs){
            fx.stop();
        }
    }

    /** AUTOMATE **/
    public void automate(int step, boolean isPopupShowing){
        for(Fx fx : fxs){
            fx.automate(step, isPopupShowing);
        }
    }

    public void resetAutomations(){
        for(Fx fx : fxs){
            fx.resetAutomation();
        }
    }

    /** RESET **/
    public void reset(){
        for(Fx fx : fxs){
            destroyFx(fx);
        }
        fxs = new ArrayList<>();
        setIndicator();
    }

    /** RESTORATION **/
    public void restore(FxManagerKeeper k){

        destroy();
        fxs = new ArrayList<>(); //fxs add themselves in createNewFx

        for(FxKeeper fxk : k.fxKeepers){
            Fx fx = createNewFx(fxk.fxIndex, false);
            fx.restore(fxk);
            //addFxToEngine(fx);

            if(fx.isOn()) {
                addFxToEngine(fx);
            }
        }

        //only set a selected if at least one exists
        if(k.selectedFxIndex > 0) {
            setSelectedFx(k.selectedFxIndex);
        }
    }

    public FxManagerKeeper getKeeper(){
        FxManagerKeeper keeper = new FxManagerKeeper();
        keeper.fxKeepers = new ArrayList<>();
        for(Fx fx : fxs){
            keeper.fxKeepers.add(fx.getKeeper());
        }
        keeper.selectedFxIndex = fxs.indexOf(selectedFx);
        return keeper;
    }

    /** DESTROY **/
    public void destroy(){
        destroyFxs();
        fxs = null;
    }

    /** INNER CLASS **/
    public static class FxInfo {
        private final String name;
        private final int color;

        private FxInfo(String name, int color){
            this.name = name;
            this.color = color;
        }

        public String getName(){
            return name;
        }

        public int getColor(){
            return color;
        }
    }

    /** INTERFACE **/
    public interface FxManagerUser {
        FxManager getFxManager();
        String getPopupLabel();
        int getColor();
        void setFxManagerPopup(FxManagerPopup fxManagerPopup);
        //AudioChannel getAudioChannels();
    }
}
