package com.kiefer.randomization.rndSeqManager;

import com.kiefer.LLPPDRUMS;
import com.kiefer.files.keepers.rndSeqManager.RndSeqManagerKeeper;
import com.kiefer.interfaces.Tempoizer;
import com.kiefer.machine.sequence.track.soundManager.presets.SoundSourcePreset;
import com.kiefer.randomization.presets.RandomizeSeqPresetCustom;
import com.kiefer.randomization.presets.RandomizeSeqPresetDesert;
import com.kiefer.randomization.presets.RandomizeSeqPresetJazz;
import com.kiefer.randomization.presets.RandomizeSeqPresetRockPlus;
import com.kiefer.randomization.presets.RandomizeSeqPreset;
import com.kiefer.randomization.presets.RandomizeSeqPresetDisco;
import com.kiefer.randomization.presets.RandomizeSeqPresetHiBeat;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrack;
import com.kiefer.randomization.presets.tracks.RndSeqPresetTrackRandom;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.utils.ImgUtils;

import java.util.ArrayList;
import java.util.Random;

/** Handles sequence-wide randomization. **/

public class RndSeqManager implements Tempoizer {
    private final LLPPDRUMS llppdrums;
    private final DrumSequence drumSequence;

    private ArrayList<RandomizeSeqPreset> rndSeqPresets;
    private RandomizeSeqPreset selectedRandomizeSeqPreset;

    private ArrayList<String> soundPresetCategories;

    private final int bgImgId, presetListImgId;

    private ArrayList<RndSeqPresetTrack> tracks;

    private int tempo;

    public RndSeqManager(LLPPDRUMS llppdrums, DrumSequence drumSequence, ArrayList<String> soundPresetCategories, RndSeqManagerKeeper keeper) {
        this.llppdrums = llppdrums;
        this.drumSequence = drumSequence;
        this.soundPresetCategories = soundPresetCategories;

        bgImgId = ImgUtils.getRandomImageId();
        presetListImgId = ImgUtils.getRandomImageId();

        setupPresets();

        if(keeper != null){
            restore(keeper);
        }
        else {
            Random r = new Random();
            selectedRandomizeSeqPreset = rndSeqPresets.get(r.nextInt(rndSeqPresets.size()));
            selectedRandomizeSeqPreset.createPreset();
        }
    }

    private void setupPresets() {
        rndSeqPresets = new ArrayList<>();
        rndSeqPresets.add(new RandomizeSeqPresetRockPlus(llppdrums, this));
        rndSeqPresets.add(new RandomizeSeqPresetDisco(llppdrums, this));
        rndSeqPresets.add(new RandomizeSeqPresetHiBeat(llppdrums, this));
        rndSeqPresets.add(new RandomizeSeqPresetJazz(llppdrums, this));
        //rndSeqPresets.add(new RandomizeSeqPresetJazz(llppdrums, this));
    }

    /** RND SEQ **/
    //called when rndSeq-btn is pressed (and on start up if no keepers)
    public void randomizeSequence() {
        drumSequence.reset(false); //no need to update drawables here since they get upadated in the new randomization below

        int steps = tracks.get(0).getSteps().size();

        drumSequence.setNOfSteps(steps);
        drumSequence.setTempo(tempo);
        drumSequence.setNOfTracks(tracks.size());

        for(int trackNo = 0; trackNo < tracks.size(); trackNo++){
            randomizeTrack(tracks.get(trackNo), drumSequence.getTracks().get(trackNo));
        }
    }

    public void randomizeTrack(RndSeqPresetTrack rndTrack, DrumTrack drumTrack){
        drumTrack.getRndTrackManager().randomize(rndTrack);
    }

    /** TRACKS **/
    public void moveTrack(final int from, final int to){
        RndSeqPresetTrack track = tracks.remove(from);
        tracks.add(to, track);
    }

    public void removeTrack(int fxNo){
        tracks.remove(fxNo);
    }

    public void addTrack(){
        tracks.add(new RndSeqPresetTrackRandom(llppdrums, tracks.get(0).getSteps().size(), tracks.get(0).getnOfSubs()));
    }

    public void setTracks(ArrayList<RndSeqPresetTrack> tracks){
        this.tracks = tracks;
    }

    /** STEPS **/
    public void addStep(){
        selectedRandomizeSeqPreset.addStep();
        addModifiedMarker();
    }
    public void removeStep(){
        selectedRandomizeSeqPreset.removeStep();
        addModifiedMarker();
    }

    /** AUTORANDOMIZATION **/

    //called on step 0 for autoRandomization
    public void autoRandomize() {
        for(DrumTrack dt : drumSequence.getTracks()){
            if(dt.automationsActive()){
                dt.getRndTrackManager().autoRandomize();
            }
        }
    }

    public void returnAutoRandomizations(int stepNo){
        for(DrumTrack dt : drumSequence.getTracks()){
            if(dt.returnActive() && dt.getSteps().get(stepNo).returnActive()){
                dt.getRndTrackManager().returnAutoRandomization(stepNo);
            }
        }
    }

    public void returnAutoRandomizations(){
        for(DrumTrack dt : drumSequence.getTracks()){
            for(int stepNo = 0; stepNo < drumSequence.getNOfSteps(); stepNo++)
            if(dt.returnActive() && dt.getSteps().get(stepNo).returnActive()){
                dt.getRndTrackManager().returnAutoRandomization(stepNo);
            }
        }
    }

    /** GET **/
    public int getBgImgId() {
        return bgImgId;
    }

    public int getPresetListImgId() {
        return presetListImgId;
    }

    public ArrayList<RandomizeSeqPreset> getRndSeqPresets() {
        return rndSeqPresets;
    }

    public RandomizeSeqPreset getSelectedRandomizePreset() {
        return selectedRandomizeSeqPreset;
    }

    public void addModifiedMarker(){
        selectedRandomizeSeqPreset.addModifiedMarker();
    }

    public void removeModifiedMarker(){
        selectedRandomizeSeqPreset.removeModifiedMarker();
    }

    public ArrayList<RndSeqPresetTrack> getTracks(){
        return tracks;
    }

    @Override
    public int getTempo(){
        return tempo;
    }

    public ArrayList<String> getSoundPresetCategories() {
        return soundPresetCategories;
    }

    /** SET **/
    @Override
    public void setTempo(int tempo){
        this.tempo = tempo;
    }

    public void setSelectedRandomizePreset(String s) {
        for(RandomizeSeqPreset r : rndSeqPresets){
            if(r.getName().equals(s)){
                selectedRandomizeSeqPreset = r;
                selectedRandomizeSeqPreset.createPreset();
                return;
            }
        }
    }

    public void setTrackOsc(int trackNo, String osc){
        tracks.get(trackNo).setPresetCategories(osc);
    }

    /** RESTORE **/
    public RndSeqManagerKeeper getKeeper(){
        RndSeqManagerKeeper keeper = new RndSeqManagerKeeper();
        keeper.randomizeSeqPresetKeeper = selectedRandomizeSeqPreset.getKeeper();
        keeper.rndSeqPresetTrackKeepers = new ArrayList<>();
        for(RndSeqPresetTrack t : tracks){
            keeper.rndSeqPresetTrackKeepers.add(t.getKeeper());
        }
        return keeper;
    }

    public void restore(RndSeqManagerKeeper keeper){
        RandomizeSeqPresetCustom randomizeSeqPresetCustom = new RandomizeSeqPresetCustom(llppdrums, this, keeper);
        randomizeSeqPresetCustom.createPreset();
        selectedRandomizeSeqPreset = randomizeSeqPresetCustom;
    }
}
