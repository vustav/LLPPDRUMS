package com.kiefer.machine.sequence.track.Stackables.fx;

import androidx.core.content.ContextCompat;
import android.util.Log;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.files.keepers.fx.FxKeeper;
import com.kiefer.files.keepers.fx.FxManagerKeeper;
import com.kiefer.machine.sequence.track.Stackables.StackableManager;
import com.kiefer.machine.sequence.track.Stackables.fx.fxs.Fx;
import com.kiefer.machine.sequence.track.Stackables.fx.fxs.FxBitCrusher;
import com.kiefer.machine.sequence.track.Stackables.fx.fxs.FxDecimator;
import com.kiefer.machine.sequence.track.Stackables.fx.fxs.FxDelay;
import com.kiefer.machine.sequence.track.Stackables.fx.fxs.FxFlanger;
import com.kiefer.machine.sequence.track.Stackables.fx.fxs.FxFormantFilter;
import com.kiefer.machine.sequence.track.Stackables.fx.fxs.FxHPFilter;
import com.kiefer.machine.sequence.track.Stackables.fx.fxs.FxLPFilter;
import com.kiefer.machine.sequence.track.Stackables.fx.fxs.FxLimiter;
import com.kiefer.machine.sequence.track.Stackables.fx.fxs.FxReverb;
import com.kiefer.machine.sequence.track.Stackables.fx.fxs.FxTremolo;
import com.kiefer.machine.sequence.track.Stackables.fx.fxs.FxWaveShaper;
import com.kiefer.machine.sequence.track.Stackables.Stackable;
import com.kiefer.utils.ImgUtils;

import java.util.ArrayList;
import java.util.Random;

import nl.igorski.mwengine.core.ProcessingChain;

public class FxManager extends StackableManager {
    protected LLPPDRUMS llppdrums;
    //private FxManagerUser fxManagerUser;
    protected Fxer fxer;
    protected ArrayList<Fx> fxs;

    //used to create random fx and to poopulate the list. Otherwise we'd have to create actual fxs to get access to the info which seems bad
    protected ArrayList<StackableInfo> stackableInfos;

    protected Fx selectedFx;

    //tabManager used by the popup
    //private TabManager tabManager;

    //IMGs
    private final int bgImgId, fxListPopupImgId;

    //rnd
    private final Random random;
    private final int MAX_N_RND_FXS = 2;

    //public FxManager(LLPPDRUMS llppdrums, FxManagerUser fxManagerUser){
    public FxManager(LLPPDRUMS llppdrums, Fxer fxer){
        super(fxer);
        this.llppdrums = llppdrums;
        this.fxer = fxer;

        //tabManager = new TabManager(llppdrums);

        random = new Random();

        fxs = new ArrayList<>();
        bgImgId = ImgUtils.getRandomImageId();
        fxListPopupImgId = ImgUtils.getRandomImageId();

        stackableInfos = new ArrayList<>();
        stackableInfos.add(new StackableInfo(llppdrums.getResources().getString(R.string.fxFlangerName), ContextCompat.getColor(llppdrums, R.color.fxFlangerColor)));
        stackableInfos.add(new StackableInfo(llppdrums.getResources().getString(R.string.fxDelayName), ContextCompat.getColor(llppdrums, R.color.fxDelayColor)));
        stackableInfos.add(new StackableInfo(llppdrums.getResources().getString(R.string.fxReverbName), ContextCompat.getColor(llppdrums, R.color.fxReverbColor)));
        stackableInfos.add(new StackableInfo(llppdrums.getResources().getString(R.string.fxBitCrusherName), ContextCompat.getColor(llppdrums, R.color.fxBitCrusherColor)));
        stackableInfos.add(new StackableInfo(llppdrums.getResources().getString(R.string.fxDecimatorName), ContextCompat.getColor(llppdrums, R.color.fxDecimatorColor)));
        stackableInfos.add(new StackableInfo(llppdrums.getResources().getString(R.string.fxTremoloName), ContextCompat.getColor(llppdrums, R.color.fxTremoloColor)));
        stackableInfos.add(new StackableInfo(llppdrums.getResources().getString(R.string.fxFFName), ContextCompat.getColor(llppdrums, R.color.fxFFColor)));
        stackableInfos.add(new StackableInfo(llppdrums.getResources().getString(R.string.fxWSName), ContextCompat.getColor(llppdrums, R.color.fxWSColor)));
        stackableInfos.add(new StackableInfo(llppdrums.getResources().getString(R.string.fxHPName), ContextCompat.getColor(llppdrums, R.color.fxHPColor)));
        stackableInfos.add(new StackableInfo(llppdrums.getResources().getString(R.string.fxLPName), ContextCompat.getColor(llppdrums, R.color.fxLPColor)));
        stackableInfos.add(new StackableInfo(llppdrums.getResources().getString(R.string.fxLimiterName), ContextCompat.getColor(llppdrums, R.color.fxLimiterColor)));
    }

    //has to be in the same order as fxInfos
    private Fx getNewFx(int fxNo, boolean randomizeAutomation){
        switch (fxNo){
            case 0:
                return new FxFlanger(llppdrums, this, fxer, fxNo, randomizeAutomation);
            case 1:
                return new FxDelay(llppdrums, this, fxer, fxNo, randomizeAutomation);
            case 2:
                return new FxReverb(llppdrums, this, fxer, fxNo, randomizeAutomation);
            case 3:
                return new FxBitCrusher(llppdrums, this, fxer, fxNo, randomizeAutomation);
            case 4:
                return new FxDecimator(llppdrums, this, fxer, fxNo, randomizeAutomation);
            case 5:
                return new FxTremolo(llppdrums, this, fxer, fxNo, randomizeAutomation);
            case 6:
                return new FxFormantFilter(llppdrums, this, fxer, fxNo, randomizeAutomation);
            case 7:
                return new FxWaveShaper(llppdrums, this, fxer, fxNo, randomizeAutomation);
            case 8:
                return new FxHPFilter(llppdrums, this, fxer, fxNo, randomizeAutomation);
            case 9:
                return new FxLPFilter(llppdrums, this, fxer, fxNo, randomizeAutomation);
            case 10:
                return new FxLimiter(llppdrums, this, fxer, fxNo, randomizeAutomation);
        }
        return null;
    }

    protected Fx createNewFx(int i, boolean randomizeAutomation){
        Fx fx = getNewFx(i, randomizeAutomation);
        selectedFx = fx; //created are always selected
        fxs.add(fx);
        return fx;
    }

    public void createRandomStackable(boolean automation){
        Random r = new Random();
        Fx fx = createNewFx(r.nextInt(stackableInfos.size()), automation);

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

    public void moveStackable(final int from, final int to){
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

    public void removeStackable(int n){
        Fx fx = fxs.remove(n);
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


    public void changeSelectedStackable(int newStackableIndex){

        //create the new fx
        Fx newFx = getNewFx(newStackableIndex, false);

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

    public void turnSelectedStackableOn(Boolean on){
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

    public void setSelectedStackable(int index, int sender){
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
            createRandomStackable(true);
        }

        if(fxs.size() > 0){
            selectedFx = fxs.get(0);
        }
        else{
            setIndicator(); //id < 0 this is calls in createRandomFx()
        }
    }public void randomizeAll(int n) {
        destroy();
        fxs = new ArrayList<>();

        for(int i = 0; i < n; i++){
            //if()
            createRandomStackable(true);
        }

        if(fxs.size() > 0){
            selectedFx = fxs.get(0);
        }
        else{
            setIndicator(); //id < 0 this is calls in createRandomFx()
        }
    }

    /** GET **/

    public String getName(){
        return "FX MANAGER";
    }

    public ArrayList<StackableInfo> getStackableInfos() {
        return stackableInfos;
    }

    public int getBgImgId() {
        return bgImgId;
    }

    public int getListPopupImgId() {
        return fxListPopupImgId;
    }

    public ArrayList<Stackable> getStackables(){
        ArrayList<Stackable> al = new ArrayList<>();
        for(Fx fx : fxs){
            al.add(fx);
        }
        return al;
    }

    public Stackable getSelectedStackable() {
        return selectedFx;
    }

    public int getSelectedStackableIndex(){
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
            setSelectedStackable(k.selectedFxIndex, 4);
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
}
