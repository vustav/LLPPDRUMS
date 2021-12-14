package com.kiefer.machine.sequence.track.soundManager.oscillatorManager;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.files.keepers.Keeper;
import com.kiefer.files.keepers.soundSources.OscillatorManagerKeeper;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.machine.sequence.track.soundManager.events.OscEvents;
import com.kiefer.machine.sequence.track.soundManager.events.SoundEvents;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.presets.osc.OscPreset;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.presets.osc.OscPresetBass;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.presets.osc.OscPresetHH;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.presets.osc.OscPresetSnare;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.presets.osc.OscPresetTom;
import com.kiefer.machine.sequence.track.soundManager.oscillatorManager.presets.SoundSourcePreset;
import com.kiefer.utils.ImgUtils;

import java.util.ArrayList;
import java.util.Random;

import nl.igorski.mwengine.core.ProcessingChain;
import nl.igorski.mwengine.core.BaseInstrument;

public class OscillatorManager extends SoundSource {
    private final LLPPDRUMS llppdrums;
    private final DrumSequence drumSequence;
    private final DrumTrack drumTrack;

    private final Random random;

    private final Oscillator[] oscillators;
    private final String[] waves;

    private final int bgImageId, presetsListImageId;

    public OscillatorManager(LLPPDRUMS llppdrums, DrumSequence drumSequence, DrumTrack drumTrack){
        super();
        this.llppdrums = llppdrums;
        this.drumSequence = drumSequence;
        this.drumTrack = drumTrack;
        random = new Random();

        waves = llppdrums.getResources().getStringArray(R.array.oscWaves);

        bgImageId = ImgUtils.getRandomImageId();
        presetsListImageId = ImgUtils.getRandomImageId();

        //create the oscillators
        oscillators = new Oscillator[llppdrums.getResources().getInteger(R.integer.nOfOscillators)];
        for(int i = 0; i < oscillators.length; i++){
            createOscillator(i);
        }
        setupPresets();
    }

    private void createOscillator(int oscillatorNo){

        boolean on;
        //always set the first to on
        if(oscillatorNo == 0){
            on = true;
        }
        //randomize the rest
        else{
            on = random.nextInt(2) == 1;
        }

        oscillators[oscillatorNo] = new Oscillator(llppdrums, drumTrack, this, on);
        //oscillators[oscillatorNo].getSynthInstrument().getAudioChannel().setPan(drumTrack.getPan());
    }

    public void updateSubs(int subs){
        for(int i = 0; i < oscillators.length; i++){
            oscillators[i].updateSubs(subs);
        }
    }

    /** ACTIVATION **/
    @Override
    public void activate(){
        for(Oscillator o : oscillators){
            o.activate();
        }
        //setIndicator();
    }

    @Override
    public void deactivate(){
        for(Oscillator o : oscillators){
            o.deactivate();
        }
    }

    /** PLAY **/
    @Override
    public void playDrum(){
        for(Oscillator o : oscillators){
            if(o.isOn()) {
                o.play();
            }
        }
    }

    /** RANDOMIZE **/
    @Override
    public void randomizeAll(){
        for(Oscillator o : oscillators){
            o.randomizeAll();
        }
    }

    /** PRESETS **/
    private void setupPresets(){
        presets.add(new OscPresetBass(llppdrums));
        presets.add(new OscPresetHH(llppdrums));
        presets.add(new OscPresetSnare(llppdrums));
        presets.add(new OscPresetTom(llppdrums));
    }

    @Override
    public void setPreset(String preset){
        for (SoundSourcePreset op : presets) {
            if (preset.equals(op.getName())) {
                ((OscPreset)op).setupOscillators(this);
                //drumTrack.setName(op.getName());
            }
        }
    }

    @Override
    public void setRandomPreset(){
        OscPreset op = (OscPreset) presets.get(random.nextInt(presets.size()));

        op.setupOscillators(this);
        //drumTrack.setName(op.getName());
    }

    /** SELECTION **/
    public void select(){
        //setIndicator();
    }
    public void deselect(){

    }

    /** SET **/
    @Override
    public void setPan(float pan){
        //if(drumTrack.getTrackNo() == 0) {
            //Log.e("OscMan", "set pan: " + pan);
        //}
        for(Oscillator o : oscillators){
            o.getSynthInstrument().getAudioChannel().setPan(pan);
        }
    }

    public void setOscillatorVolume(final int oscillatorNo, final float volume){
        oscillators[oscillatorNo].setVolume(volume);
    }

    public void setOscillatorOn(final int oscillatorNo, boolean on){
        oscillators[oscillatorNo].setOn(on);
    }

    public void setWaveForm(final int oscillatorNo, final int waveForm){
        oscillators[oscillatorNo].setWaveForm(waveForm);
    }

    public void setAttackTime(final int oscillatorNo, final float attackTime) {
        oscillators[oscillatorNo].setAttackTime(attackTime);
    }

    public void setDecayTime(final int oscillatorNo, final float decayTime) {
        oscillators[oscillatorNo].setDecayTime(decayTime);
    }

    public void setOscillatorPitch(final int oscillatorNo, final int pitch){
        oscillators[oscillatorNo].setOscillatorPitch(pitch);
    }

    /** GET **/

    @Override
    public SoundEvents getSoundEvents(int nOfSteps, int subs, int step, boolean add){
        return new OscEvents(llppdrums, drumSequence, drumTrack, this, nOfSteps, subs, step, add);
    }

    @Override
    public ProcessingChain[] getProcessingChains(){
        ProcessingChain[] pcs = new ProcessingChain[oscillators.length];
        for(int i = 0; i<oscillators.length; i++){
            pcs[i] = oscillators[i].getSynthInstrument().getAudioChannel().getProcessingChain();
        }
        return pcs;
    }

    @Override
    public BaseInstrument getInstrument(int instrNo){
        return oscillators[instrNo].getSynthInstrument();
    }

    //if it's on, doesn't have to be playing
    public boolean isOscillatorOn(int oscillatorNo){
        return oscillators[oscillatorNo].isOn();
    }

    public float getOscillatorVolume(int instrNo){
        return oscillators[instrNo].getVolume();
    }

    public float getOscillatorWaveForm(int oscillatorNo){
        return oscillators[oscillatorNo].getOscillatorWaveForm();
    }

    public float getOscillatorPitchLin(int oscillatorNo){
        return oscillators[oscillatorNo].getOscillatorPitchLin();
    }

    public float getOscillatorPitchLog(int oscillatorNo){
        return oscillators[oscillatorNo].getOscillatorPitchLog();
    }

    public float getOscillatorAtkTime(int oscillatorNo){
        return oscillators[oscillatorNo].getAtkTime();
    }

    public float getOscillatorDecayTime(int oscillatorNo){
        return oscillators[oscillatorNo].getDecayTime();
    }

    public Oscillator getOscillator(int oscNo){
        return oscillators[oscNo];
    }

    public Oscillator[] getOscillators() {
        return oscillators;
    }

    public String[] getWaves() {
        return waves;
    }

    public String getRandomOscPreset(){
        Random r = new Random();
        return presets.get(r.nextInt(presets.size())).getName();
    }

    /** GFX **/
    @Override
    public int getBgImageId() {
        return bgImageId;
    }

    @Override
    public int getPresetListImageId() {
        return presetsListImageId;
    }

    public int getWavePopupImageId(int oscNo) {
        return oscillators[oscNo].getWavePopupImageId();
    }

    /** RESTORATION **/
    @Override
    public void restore(Keeper keeper){
        OscillatorManagerKeeper k = (OscillatorManagerKeeper)keeper;
        for(int i = 0; i < oscillators.length; i++){
            oscillators[i].restore(k.oscillatorKeepers.get(i));
        }
    }

    @Override
    public OscillatorManagerKeeper getKeeper(){
        OscillatorManagerKeeper keeper = new OscillatorManagerKeeper();

        keeper.oscillatorKeepers = new ArrayList<>();
        for(Oscillator o : oscillators){
            keeper.oscillatorKeepers.add(o.getKeeper());
        }
        return keeper;
    }

    /** DESTROY **/
    @Override
    public void destroy(){
        for(Oscillator o : oscillators){
            o.destroy();
        }
    }
}
