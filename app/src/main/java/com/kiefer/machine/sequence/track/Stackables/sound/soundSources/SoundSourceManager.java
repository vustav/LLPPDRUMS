package com.kiefer.machine.sequence.track.Stackables.sound.soundSources;

import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.automation.AutomationManager;
import com.kiefer.files.keepers.soundSources.SoundSourceManagerKeeper;
import com.kiefer.info.sequence.trackMenu.SoundManagerInfo;
import com.kiefer.info.sequence.trackMenu.fxManager.BitCrusherInfo;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.machine.sequence.track.Stackables.Stackable;
import com.kiefer.machine.sequence.track.Stackables.Stacker;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.eventManager.StepEventsManager;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.oscillatorManager.OscillatorManager;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.presets.SoundSourcePreset;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.sampleManager.SmplManager;
import com.kiefer.popups.info.InfoPopup;
import com.kiefer.popups.soundManager.oscillatorManager.OscillatorManagerView;
import com.kiefer.popups.soundManager.smplManager.SampleManagerView;
import com.kiefer.utils.ColorUtils;
import com.kiefer.utils.ImgUtils;

import java.util.ArrayList;
import java.util.Random;

import nl.igorski.mwengine.core.ProcessingChain;

/** Holds a SampleManager and an OscillatorManager.**/

public class SoundSourceManager implements Stackable {
    public static final String OSC = "osc", SAMPLE = "sample";

    private final LLPPDRUMS llppdrums;
    private final DrumSequence drumSequence;
    private final DrumTrack drumTrack;

    private SoundSource activeSoundSource;

    private ArrayList<SoundSource> soundSources;

    private final int bgImageId;
    private final int automationBgId;

    protected AutomationManager automationManager;

    //PARAMS
    protected ArrayList<String> paramNames;

    //UI
    private int gradColor1, gradColor2;
    private FrameLayout soundView;
    private OscillatorManagerView oscillatorManagerView;
    private SampleManagerView sampleManagerView;
    //private RadioButton oscRadio, sampleRadio;

    public SoundSourceManager(LLPPDRUMS llppdrums, DrumSequence drumSequence, DrumTrack drumTrack, int soundSource, boolean automation){
        this.llppdrums = llppdrums;
        this.drumSequence = drumSequence;
        this.drumTrack = drumTrack;

        randomizeColors();
        bgImageId = ImgUtils.getRandomImageId();
        automationBgId = ImgUtils.getRandomImageId();

        soundSources = new ArrayList<>();
        OscillatorManager oscillatorManager = new OscillatorManager(llppdrums, drumSequence, drumTrack);
        oscillatorManager.deactivate();
        soundSources.add(oscillatorManager);
        SmplManager smplManager = new SmplManager(llppdrums, drumSequence, drumTrack);
        smplManager.deactivate();
        soundSources.add(smplManager);

        activeSoundSource = soundSources.get(soundSource);
        activeSoundSource.activate();

        setupParamNames();

        automationManager = new AutomationManager(llppdrums, drumTrack, this);
        if(automation){
            randomizeAutomation();
        }


        //on creation steps gets the eventManagers themselves. After creation they're added automatically here. This is because SoundManager is created before the steps so we can't do this loop yet.
        if(drumTrack.getSteps() != null) {
            for (Step s : drumTrack.getSteps()) {
                s.addStepEventsManager(getStepEventManager(s, drumTrack.getNOfSubs()));
            }
        }
    }

    public void randomizeColors(){

        gradColor1 = ColorUtils.getRandomColor();
        gradColor2 = ColorUtils.getRandomColor();

    }

    public void setupParamNames(){
        paramNames = new ArrayList<>();
        paramNames.add("FIXA");
        paramNames.add("FIXA");
        paramNames.add("FIXA");
    }

    /** RND **/
    private void randomizeAutomation(){
        Random random = new Random();
        final int MAX_N_RND_FXS = 2;
        int nOfAutos = random.nextInt(MAX_N_RND_FXS + 1);

        for(int i = 0; i < nOfAutos; i++){
            automationManager.addAutomation();
        }
    }

    @Override
    public View getLayout(){//create the members
        oscillatorManagerView = new OscillatorManagerView(llppdrums, drumTrack);
        sampleManagerView = new SampleManagerView(llppdrums, drumTrack);

        //inflate the View
        final View layout = llppdrums.getLayoutInflater().inflate(R.layout.popup_sound_manager, null);
        layout.setBackground(ContextCompat.getDrawable(llppdrums, getBgImageId()));

        //create the popupWindow
        //int width = RelativeLayout.LayoutParams.WRAP_CONTENT;@dimen/defaultSeekBarWidth
        int width = (int) llppdrums.getResources().getDimension(R.dimen.defaultSeekBarWidth) * 2 + (int) llppdrums.getResources().getDimension(R.dimen.marginLarge) * 9;
        int height = RelativeLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(layout, width, height, focusable);

        //add a nice animation
        popupWindow.setAnimationStyle(R.style.popup_animation);

        //setup the TV
        /*
        String name = llppdrums.getResources().getString(R.string.soundManagerLabel) + drumTrack.getName();
        TextView label = layout.findViewById(R.id.soundManagerLabel);
        label.setText(name);
        int bgColor = drumTrack.getColor();
        label.setBackgroundColor(bgColor);
        label.setTextColor(ColorUtils.getContrastColor(bgColor));

         */

        //set up the infoBtn
        ImageView infoBtn = layout.findViewById(R.id.soundManagerInfoBtn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InfoPopup(llppdrums, SoundManagerInfo.key);
            }
        });

        //set up the sound view
        soundView = layout.findViewById(R.id.soundManagerSoundView);

        //set up the radio btns
        /*
        oscRadio = layout.findViewById(R.id.radioOscButton);
        oscRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveSoundSource(SoundSourceManager.OSC);
                setSoundView();
            }
        });
        sampleRadio = layout.findViewById(R.id.radioSampleButton);
        sampleRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setActiveSoundSource(SoundSourceManager.SAMPLE);
                setSoundView();
            }
        });

         */
        setSoundView();

        return layout;
    }

    private void setSoundView(){
        soundView.removeAllViews();
        if(getActiveSoundSource() instanceof OscillatorManager){
            //oscRadio.setChecked(true);
            soundView.addView(oscillatorManagerView.getLayout());
        }
        if(getActiveSoundSource() instanceof SmplManager){
            //sampleRadio.setChecked(true);
            soundView.addView(sampleManagerView.getLayout());
        }
    }

    @Override
    /** FIIIXXXA **/
    public String getInfoKey(){
        return BitCrusherInfo.key;
    }

    /** ACTIVATION **/
    public void activate(){
        activeSoundSource.activate();
    }

    public void deactivate(){
        //activeSoundSource.deactivate();

        for(SoundSource ss : soundSources){
            ss.deactivate();
        }
    }

    /** SELECTION **/
    public void select(){
        //oscillatorManager.select();
    }

    public void deselect(){
        //oscillatorManager.deselect();
    }

    /** RND **/
    /*
    public void randomizeSoundSource(){
        Random r = new Random();
        if(r.nextBoolean()){
            setActiveSoundSource(OSC);
        }
        else{
            setActiveSoundSource(SAMPLE);
        }
    }

     */
    public void randomizeSoundSource(float samplePerc){
        Random r = new Random();
        if(r.nextFloat() > samplePerc){
            setActiveSoundSource(OSC);
        }
        else{
            setActiveSoundSource(SAMPLE);
        }
    }

    /** SET **/
    public void setActiveSoundSource(String tag) {
        if(activeSoundSource != null) {
            activeSoundSource.deactivate();
        }

        if(tag.equals(OSC)){
            activeSoundSource = soundSources.get(0);

            //shouldn't be needed but sometimes both are on so do this just in case
            soundSources.get(1).deactivate();
        }
        else{
            activeSoundSource = soundSources.get(1);
            soundSources.get(0).deactivate();
        }

        if(llppdrums.getDrumMachine().getPlayingSequence() == drumSequence) {
            activeSoundSource.activate();
        }
    }
    public void setActiveSoundSource(int n) {
        if(activeSoundSource != null) {
            activeSoundSource.deactivate();
        }

        if(n == 0){
            activeSoundSource = soundSources.get(0);

            //shouldn't be needed but sometimes both are on so do this just in case
            soundSources.get(1).deactivate();
        }
        else{
            activeSoundSource = soundSources.get(1);
            soundSources.get(0).deactivate();
        }

        if(llppdrums.getDrumMachine().getPlayingSequence() == drumSequence) {
            activeSoundSource.activate();
        }
    }

    //atk
    public void setOscillatorAttackTime(int oscNo, float time){
        ((OscillatorManager)soundSources.get(0)).setAttackTime(oscNo, time);
    }

    //dec
    public void setOscillatorReleaseTime(int oscNo, float time){
        ((OscillatorManager)soundSources.get(0)).setReleaseTime(oscNo, time);
    }

    public void setOscillatorPitch(int oscNo, int pitch){
        ((OscillatorManager)soundSources.get(0)).setOscillatorPitch(oscNo, pitch);
    }

    //pan
    public void setPan(float pan){
        //activeSoundSource.setPan(pan);

        for(SoundSource ss : soundSources){
            ss.setPan(pan);
        }
    }

    public void setOscillatorVolume(final int oscillatorNo, final float volume){
        ((OscillatorManager)soundSources.get(0)).setOscillatorVolume(oscillatorNo, volume);
    }

    private boolean on = true;
    public void setOn(boolean on){
        for(SoundSource ss : soundSources){
            if(on) {
                ss.deactivate();
                on = false;
            }
            else{
                ss.activate();
                on = true;
            }
        }
    }

    @Override
    public boolean isOn() {
        return on;
    }

    /** GET **/


    @Override
    public GradientDrawable getBgGradient(){
        return ColorUtils.getGradientDrawable(gradColor1, gradColor2, ColorUtils.HORIZONTAL);
    }

    @Override
    public GradientDrawable getTabGradient(){
        return ColorUtils.getGradientDrawable(gradColor2, gradColor1, ColorUtils.HORIZONTAL);
    }

    @Override
    public AutomationManager getAutomationManager() {
        return automationManager;
    }

    public OscillatorManager getOscillatorManager(){
        return (OscillatorManager) soundSources.get(0);
    }

    public SmplManager getSmplManager(){
        return (SmplManager) soundSources.get(1);
    }

    public SoundSource getActiveSoundSource() {
        return activeSoundSource;
    }

    public ArrayList<ProcessingChain> getProcessingChains() {
        ArrayList<ProcessingChain> chains = new ArrayList<>();
        for(SoundSource ss : soundSources){
            for(ProcessingChain pc : ss.getProcessingChains()) {
                chains.add(pc);
            }
        }
        return chains;
    }

    public int getAutomationBgId() {
        return automationBgId;
    }

    public void setRandomPresets(){
        for(SoundSource ss : soundSources){
            ss.setRandomPreset();
        }
    }

    public String getName(){
        return activeSoundSource.getName();
    }

    /** RndSeqManager calls this with one of the static strings in SoundSourcePreset, so make sure to cover them and add anything extra class-specific **/
    public void setPresets(String s){
        setOscPreset(s);
        setSmplPreset(s);
    }

    public void setOscPreset(String s){
        getOscillatorManager().setPreset(s);
    }

    public void setSmplPreset(String s){
        getSmplManager().setPreset(s);
    }

    public StepEventsManager getStepEventManager(Step step, int subs){
        return new StepEventsManager(llppdrums, drumSequence, drumTrack, this, step, subs);
    }

    /** EVENTS **/
    /*
    public void positionEvents(int nOfSteps, int step){
        for(SoundSource ss : soundSources){
            ss.positionEvents(nOfSteps, step);
        }
    }

    public void randomizePitch(boolean autoRnd, int sub){
        for(SoundSource ss : soundSources){
            ss.randomizePitch(autoRnd, sub);
        }
    }

     */

    /** SUBS **/
    public void updateSubs(int subs){
        getOscillatorManager().updateSubs(subs);
    }

    /** TRANSPORT **/
    public void play(){
        activeSoundSource.playDrum();
    }

    public void stop(){
        automationManager.stop();
    }

    /** RND **/
    public void randomizeAll(){
        activeSoundSource.randomizeAll();
    }

    /** GFX **/

    public int getBgImageId() {
        return bgImageId;
    }

    /** RESTORATION **/
    public void restore(SoundSourceManagerKeeper k){
        activeSoundSource = soundSources.get(k.activeSoundSourceIndex);
        for(int i = 0; i<k.ssKeepers.size(); i++){
            soundSources.get(i).restore(k.ssKeepers.get(i));
        }
        drumTrack.updateEventSamples();
    }

    public SoundSourceManagerKeeper getKeeper(){
        SoundSourceManagerKeeper smk = new SoundSourceManagerKeeper();

        smk.activeSoundSourceIndex = soundSources.indexOf(activeSoundSource);

        smk.ssKeepers = new ArrayList<>();
        for(SoundSource ss : soundSources){
            smk.ssKeepers.add(ss.getKeeper());
        }

        return smk;
    }

    /** AUTO **/
    public void automate(int step, boolean popupShowing){
        automationManager.automate(step, popupShowing);
    }

    public void resetAutomation(){
        automationManager.resetAutomation();
    }

    //FIXXXXXXXXXXXA
    @Override
    public ArrayList<String> getParams(){
        return paramNames;
    }

    @Override
    public float turnOnAutoValue(String param, float autoValue, boolean popupShowing) {
        return autoValue;
    }

    @Override
    public void turnOffAutoValue(String param, float oldValue, boolean popupShowing) {

    }

    /** STEPS **/
    public void addStep(){
        automationManager.addStep();
    }

    public void removeStep(){
        automationManager.removeStep();
    }

    /** DESTRUCTION **/
    public void destroy(){

        for(Step step : drumTrack.getSteps()){
            step.destroyStepEventsManager(this);
        }

        for(SoundSource ss : soundSources){
            ss.destroy();
        }
    }
}
