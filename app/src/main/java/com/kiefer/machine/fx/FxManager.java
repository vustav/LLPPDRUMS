package com.kiefer.machine.fx;

import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.ViewGroup;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.files.keepers.fx.FxKeeper;
import com.kiefer.files.keepers.fx.FxManagerKeeper;
import com.kiefer.popups.fxManager.FxManagerPopup;
import com.kiefer.utils.ImgUtils;

import java.util.ArrayList;
import java.util.Random;

import nl.igorski.mwengine.core.ProcessingChain;

public class FxManager {
    protected LLPPDRUMS llppdrums;
    //private FxManagerUser fxManagerUser;
    protected FXer fxer;
    protected ArrayList<Fx> fxs;

    //used to create random fx and to poopulate the list. Otherwise we'd have to create actual fxs to get access to the info which seems bad
    protected ArrayList<FxInfo> fxInfos;

    protected Fx selectedFx;

    //tabManager used by the popup
    //private TabManager tabManager;

    //IMGs
    private final int fxManagerImgId, fxPopupImgId;

    //rnd
    private final Random random;
    private final int MAX_N_RND_FXS = 2;

    //public FxManager(LLPPDRUMS llppdrums, FxManagerUser fxManagerUser){
    public FxManager(LLPPDRUMS llppdrums, FXer fxer){
        this.llppdrums = llppdrums;
        this.fxer = fxer;

        //tabManager = new TabManager(llppdrums);

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
        fxInfos.add(new FxInfo(llppdrums.getResources().getString(R.string.fxTremoloName), ContextCompat.getColor(llppdrums, R.color.fxTremoloColor)));
        fxInfos.add(new FxInfo(llppdrums.getResources().getString(R.string.fxFFName), ContextCompat.getColor(llppdrums, R.color.fxFFColor)));
        fxInfos.add(new FxInfo(llppdrums.getResources().getString(R.string.fxWSName), ContextCompat.getColor(llppdrums, R.color.fxWSColor)));
        fxInfos.add(new FxInfo(llppdrums.getResources().getString(R.string.fxHPName), ContextCompat.getColor(llppdrums, R.color.fxHPColor)));
        fxInfos.add(new FxInfo(llppdrums.getResources().getString(R.string.fxLPName), ContextCompat.getColor(llppdrums, R.color.fxLPColor)));
        fxInfos.add(new FxInfo(llppdrums.getResources().getString(R.string.fxLimiterName), ContextCompat.getColor(llppdrums, R.color.fxLimiterColor)));
    }

    //has to be in the same order as fxInfos
    private Fx getNewFx(int fxNo, boolean randomizeAutomation){
        switch (fxNo){
            case 0:
                return new FxFlanger(llppdrums, fxer, fxNo, randomizeAutomation);
            case 1:
                return new FxDelay(llppdrums, fxer, fxNo, randomizeAutomation);
            case 2:
                return new FxReverb(llppdrums, fxer, fxNo, randomizeAutomation);
            case 3:
                return new FxBitCrusher(llppdrums, fxer, fxNo, randomizeAutomation);
            case 4:
                return new FxDecimator(llppdrums, fxer, fxNo, randomizeAutomation);
            case 5:
                return new FxTremolo(llppdrums, fxer, fxNo, randomizeAutomation);
            case 6:
                return new FxFormantFilter(llppdrums, fxer, fxNo, randomizeAutomation);
            case 7:
                return new FxWaveShaper(llppdrums, fxer, fxNo, randomizeAutomation);
            case 8:
                return new FxHPFilter(llppdrums, fxer, fxNo, randomizeAutomation);
            case 9:
                return new FxLPFilter(llppdrums, fxer, fxNo, randomizeAutomation);
            case 10:
                return new FxLimiter(llppdrums, fxer, fxNo, randomizeAutomation);
        }
        return null;
    }

    protected Fx createNewFx(int i, boolean randomizeAutomation){
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

    protected void addFxToEngine(final Fx fx){
        for (ProcessingChain pc : fxer.getProcessingChains()) {
            pc.addProcessor(fx.getBaseProcessor());
        }
        setIndicator();
    }

    public void moveFx(final int from, final int to){
        Fx fx = fxs.remove(from);
        fxs.add(to, fx);
        rearrangeFxs();
    }

    protected void rearrangeFxs(){
        for(ProcessingChain pc : fxer.getProcessingChains()) {
            if (pc != null) {
                pc.reset();
                for(Fx fx : fxs){
                    if(fx.isOn()) {
                        //don't use addFxToEngine() here since we already loop the pcs to reset them
                        pc.addProcessor(fx.getBaseProcessor());
                    }
                }
            }
        }
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
    }

    private void removeFxFromEngine(Fx fx){
        for(ProcessingChain pc : fxer.getProcessingChains()){
            if(pc != null){
                pc.removeProcessor(fx.getBaseProcessor());
            }
            else{
                Log.e("FxManager","removeFxFromEngine(), pc == null");
            }
        }
        setIndicator();
    }

    public void removeFxsFromEngine(){
        if(fxs != null) {
            for (Fx fx : fxs) {
                removeFxFromEngine(fx);
            }
        }

        /** SHOULDN'T BE NEEDED **/
        for(ProcessingChain pc : fxer.getProcessingChains()) {
            pc.reset();
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

    protected void setIndicator(){
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
    public void activate(){
        automate(llppdrums.getEngineFacade().getStep(), false);
    }

    public void deactivate(){
        resetAutomations();
    }

    /** ADD/REMOVE **/
    public void addFxsToEngine(){
        rearrangeFxs();
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
    public void randomizeAll(boolean allowZero) {
        destroy();
        fxs = new ArrayList<>();

        int nOfFxs = random.nextInt(MAX_N_RND_FXS + 1);

        if(!allowZero && nOfFxs == 0){
            nOfFxs += 1;
        }

        for(int i = 0; i < nOfFxs; i++){
            createRandomFx(true);
        }

        if(fxs.size() > 0){
            selectedFx = fxs.get(0);
        }
        else{
            setIndicator(); //id < 0 this is calls in createRandomFx()
        }
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

    public int getSelectedFxIndex(){
        return fxs.indexOf(selectedFx);
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
