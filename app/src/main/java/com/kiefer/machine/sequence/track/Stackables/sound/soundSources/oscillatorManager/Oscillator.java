package com.kiefer.machine.sequence.track.Stackables.sound.soundSources.oscillatorManager;

import android.util.Log;

import com.kiefer.Deleter;
import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.files.keepers.soundSources.OscillatorKeeper;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.popups.trackMenu.TrackVolPopup;
import com.kiefer.utils.ImgUtils;
import com.kiefer.utils.NmbrUtils;

import java.util.Random;

import nl.igorski.mwengine.core.BaseInstrument;
import nl.igorski.mwengine.core.SynthEvent;
import nl.igorski.mwengine.core.SynthInstrument;

public class Oscillator {
    private final LLPPDRUMS llppdrums;
    private final DrumTrack drumTrack;
    private final OscillatorManager oscillatorManager;
    private SynthInstrument synthInstrument, liveInstrument; //liveInstrument is only used to play the sample manually (get conflicts otherwise)
    private SynthEvent liveEvent;

    private int wavePopupImgId;


    //oscillatorVolume is set in the synthManager and stepVolume in the sequencer. The sum of them is whats played. Pitches work the same way.
    private float oscillatorVolume;
    int oscillatorPitchLin, oscillatorPitchLog;

    private final Random random;

    private boolean on;

    public Oscillator(LLPPDRUMS llppdrums, DrumTrack drumTrack, OscillatorManager oscillatorManager, boolean on) {
        this.llppdrums = llppdrums;
        this.drumTrack = drumTrack;
        this.oscillatorManager = oscillatorManager;

        synthInstrument  = new SynthInstrument();

        wavePopupImgId = ImgUtils.getRandomImageId();

        liveInstrument  = new SynthInstrument();
        liveEvent = new SynthEvent(0, liveInstrument); //pitch will be randomized below
        liveEvent.setDuration(1);
        //liveEvent.calculateBuffers();

        //these aren't part of the UI and will never be set again
        setInstrumentDefaults();

        this.on = on;
        random = new Random();
        randomizeWaveForm();
        randomizeVolume();
        randomizePitch();
        randomizeAtk();
        randomizeRelease();

        deactivate();
    }

    private void setInstrumentDefaults(){
        float n = 0;
        liveInstrument.getAdsr().setSustainLevel(n);
        synthInstrument.getAdsr().setSustainLevel(n);
        liveInstrument.getAdsr().setReleaseTime(n);
        synthInstrument.getAdsr().setReleaseTime(n);
    }

    /** ACTIVATION **/
    public void activate(){
        //Log.e("OSC", "activate(), on: "+on);
        //if(on) {
            //if(synthInstrument.getAudioChannel().getMuted()) {
                synthInstrument.getAudioChannel().setMuted(false);
            //}
        //}

    }
    public void deactivate(){
        //Log.e("OSC", "deactivate()");
        //if(on) {
        //if(!synthInstrument.getAudioChannel().getMuted()) {
            synthInstrument.getAudioChannel().setMuted(true);
        //}
        //}
    }

    /** SUBS **/
    public void updateSubs(int subs){
        setAttackTime(getAtkTime());
        setReleaseTime(getReleaseTime());
    }

    /** RANDOMIZE **/
    public void randomizeAll(){
        randomizeOn();
        randomizeWaveForm();
        randomizeVolume();
        randomizePitch();
        randomizeAtk();
        randomizeRelease();
    }

    public void randomizeOn(){
        if(oscillatorManager.getOscillators()[0] == this){
            on = true;
        }
        else {
            on = getRandomOn();
        }
        setOn(on);
    }

    private boolean getRandomOn(){
        return random.nextInt(2) == 1;
    }

    public void randomizeWaveForm(){
        setWaveForm(getRandomWave());
    }

    private int getRandomWave(){
        return random.nextInt(oscillatorManager.getWaves().length);
    }

    public void randomizeVolume(){
        setVolume(random.nextFloat());
    }

    public void randomizePitch(){
        setOscillatorPitch(random.nextInt(llppdrums.getResources().getInteger(R.integer.maxPitch)));
    }

    public void randomizeAtk(){
        setAttackTime(getRandomAtk());
    }

    private float getRandomAtk(){
        return random.nextFloat() * llppdrums.getResources().getInteger(R.integer.maxOscAtkTime) / 100;
    }

    public void randomizeRelease(){
        setReleaseTime(getRandomDecay());
    }

    private float getRandomDecay(){
        return random.nextFloat() * llppdrums.getResources().getInteger(R.integer.maxOscDecayTime) / 100;
    }

    /** SET **/
    public void setWaveForm(int wave){
        synthInstrument.getOscillatorProperties(0).setWaveform(wave);
        liveInstrument.getOscillatorProperties(0).setWaveform(wave);
    }
    public void setAttackTime(float f){
        float atkTime = f * drumTrack.getNOfSubs();
        //Log.e("asdasd", "atk: "+atkTime);
        synthInstrument.getAdsr().setAttackTime(NmbrUtils.removeImpossibleNumbers(atkTime));
        liveInstrument.getAdsr().setAttackTime(NmbrUtils.removeImpossibleNumbers(atkTime));

        synthInstrument.getAdsr().setDecayTime(0.1f);
        synthInstrument.getAdsr().setSustainLevel(getVolume() * .1f);
    }
    public void setReleaseTime(float f){
        /*
        float dcyTime = f * drumTrack.getNOfSubs();
        synthInstrument.getAdsr().setDecayTime(NmbrUtils.removeImpossibleNumbers(dcyTime));
        liveInstrument.getAdsr().setDecayTime(NmbrUtils.removeImpossibleNumbers(dcyTime));

         */
        synthInstrument.getAdsr().setReleaseTime(f);
    }

    //se the top for an explanation of the different volumes
    public void setVolume(float volume){
        oscillatorVolume = volume;
        liveEvent.setVolume(NmbrUtils.removeImpossibleNumbers(volume) * TrackVolPopup.VOL_MULTIPLIER * .5f);
    }

    public void setSolo(){
        //
    }

    public void setMute(){
        //
    }

    public void setOn(boolean on){
        this.on = on;
        if(llppdrums.getDrumMachine() != null) { //if called on restoration drumMachine is null: just deactivate since seq0 always starts active
            if (llppdrums.getDrumMachine().getSelectedSequence() == llppdrums.getDrumMachine().getPlayingSequence() && on) {
                activate();
            } else {
                deactivate();
            }
        }
        else{
            deactivate();
        }
    }

    public void setOscillatorPitch(int pitch){

        //keep the linear value to be able to return it to the synthPopup
        oscillatorPitchLin = pitch;

        //convert the value to the corresponding on a logarithmic scale, and make sure it's at least minPitch
        oscillatorPitchLog = (int)lin2log((float) pitch) + llppdrums.getResources().getInteger(R.integer.minPitch);

        //synthInstrument.setFrequency(oscillatorPitchLog);
        liveEvent.setFrequency(oscillatorPitchLog);
    }

    private float lin2log(float z) {
        float x = 1;
        float y = llppdrums.getResources().getInteger(R.integer.maxPitch);
        double b = Math.log(y/x)/(y-x);
        double a = y / Math.exp(b*y);
        double rawLog = a * Math.exp(b*z);
        return Math.max(Math.round(rawLog) - 1, 0); //round it a lil
    }

    /** GET **/
    public int getWavePopupImageId(){
        return wavePopupImgId;
    }

    public BaseInstrument getSynthInstrument(){
        return (BaseInstrument)synthInstrument;
    }

    public int getOscillatorWaveForm() {
        return synthInstrument.getOscillatorProperties(0).getWaveform();
    }

    public float getVolume() {
        return oscillatorVolume;
    }

    public int getOscillatorPitchLin() {
        return oscillatorPitchLin;
    }

    public float getOscillatorPitchLog() {
        return oscillatorPitchLog;
    }

    public float getAtkTime() {
        return synthInstrument.getAdsr().getAttackTime();
    }

    public float getReleaseTime() {
        return synthInstrument.getAdsr().getReleaseTime();
    }

    public boolean isOn(){
        return on;
    }

    /** CONTROL **/
    protected void play(){
        liveEvent.play();

        /*
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {
                //
            }

            public void onFinish() {
                stop();
            }
        }.start();

         */
    }
    protected void stop(){
        Random r = new Random();
        Log.e("Oscillator", "stop(), "+r.nextInt());
        liveEvent.stop();
    }

    /** RESTORATION **/
    public void restore(OscillatorKeeper k){
        setOn(k.on);
        setVolume(Float.parseFloat(k.oscillatorVolume));
        setOscillatorPitch(k.oscillatorPitchLin);
        setWaveForm(k.waveForm);
        setAttackTime(Float.parseFloat(k.atk));
        setReleaseTime(Float.parseFloat(k.release));
    }

    public OscillatorKeeper getKeeper(){
        OscillatorKeeper keeper = new OscillatorKeeper();
        keeper.oscillatorVolume = Float.toString(getVolume());
        keeper.oscillatorPitchLin = getOscillatorPitchLin();
        keeper.on = isOn();
        keeper.waveForm = getOscillatorWaveForm();
        keeper.atk = Float.toString(getAtkTime());
        keeper.release = Float.toString(getReleaseTime());
        return keeper;
    }

    /** DESTROY **/
    protected void destroy(){

        //Log.e("Osc", "destroy(), 0");
        Deleter deleter = llppdrums.getDeleter();

        //remove fxs
        synthInstrument.getAudioChannel().getProcessingChain().reset();
        synthInstrument.getAudioChannel().getProcessingChain().delete();
        //synthInstrument.getAudioChannel().getProcessingChain().setDeletable(true);
        liveInstrument.getAudioChannel().getProcessingChain().reset();
        liveInstrument.getAudioChannel().getProcessingChain().delete();
        //liveInstrument.getAudioChannel().getProcessingChain().setDeletable(true);

        //liveEvent.delete();
        deleter.addEvent(liveEvent);
        //liveEvent.setDeletable(true);
        liveEvent = null;

        // calling 'delete()' on all instruments invokes the native layer destructor
        // (and frees memory allocated to their resources, e.g. AudioChannels, Processors)
        if(synthInstrument != null) {
            //synthInstrument.delete();
            deleter.addInstrument(synthInstrument);
            synthInstrument = null;
        }

        if(liveInstrument != null) {
            //liveInstrument.delete();
            deleter.addInstrument(liveInstrument);
            liveInstrument = null;
        }
    }
}
