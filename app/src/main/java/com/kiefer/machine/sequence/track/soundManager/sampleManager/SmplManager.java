package com.kiefer.machine.sequence.track.soundManager.sampleManager;

import android.util.Log;

import com.kiefer.Deleter;
import com.kiefer.LLPPDRUMS;
import com.kiefer.files.keepers.Keeper;
import com.kiefer.files.keepers.soundSources.SampleManagerKeeper;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.machine.sequence.track.soundManager.SoundSource;
import com.kiefer.machine.sequence.track.soundManager.presets.SoundSourcePreset;
import com.kiefer.machine.sequence.track.soundManager.presets.smpl.SampleCategory;
import com.kiefer.machine.sequence.track.soundManager.presets.smpl.SampleCategoryBass;
import com.kiefer.machine.sequence.track.soundManager.presets.smpl.SampleCategoryClap;
import com.kiefer.machine.sequence.track.soundManager.presets.smpl.SampleCategoryCrash;
import com.kiefer.machine.sequence.track.soundManager.presets.smpl.SampleCategoryHHClosed;
import com.kiefer.machine.sequence.track.soundManager.presets.smpl.SampleCategoryHHOpen;
import com.kiefer.machine.sequence.track.soundManager.presets.smpl.SampleCategoryHandDrums;
import com.kiefer.machine.sequence.track.soundManager.presets.smpl.SampleCategoryMisc;
import com.kiefer.machine.sequence.track.soundManager.presets.smpl.SampleCategoryRide;
import com.kiefer.machine.sequence.track.soundManager.presets.smpl.SampleCategorySnare;
import com.kiefer.machine.sequence.track.soundManager.presets.smpl.SampleCategoryTom;
import com.kiefer.utils.ImgUtils;

import java.util.Random;

import nl.igorski.mwengine.core.SampleEvent;
import nl.igorski.mwengine.core.BaseInstrument;
import nl.igorski.mwengine.core.SampledInstrument;
import nl.igorski.mwengine.core.ProcessingChain;
import nl.igorski.mwengine.core.SampleManager;

public class SmplManager extends SoundSource {
    private final LLPPDRUMS llppdrums;
    private final DrumSequence drumSequence;
    private final DrumTrack drumTrack;
    private final Random random;

    private SampledInstrument sampledInstrument, liveInstrument;
    private SampleEvent liveEvent;

    private final int categoryListImageId, sampleListImageId;

    private SampleCategory selectedCategory;

    public SmplManager(LLPPDRUMS llppdrums, DrumSequence drumSequence, DrumTrack drumTrack){
        super();
        this.llppdrums = llppdrums;
        this.drumSequence = drumSequence;
        this.drumTrack = drumTrack;
        random = new Random();

        categoryListImageId = ImgUtils.getRandomImageId();
        sampleListImageId = ImgUtils.getRandomImageId();

        sampledInstrument = new SampledInstrument();
        liveInstrument = new SampledInstrument();
        liveEvent = new SampleEvent(liveInstrument);

        deactivate();
        setupPresets();
        setRandomPreset();
    }

    /** ACTIVATION **/
    @Override
    public void activate(){
        sampledInstrument.getAudioChannel().setMuted(false);
    }

    @Override
    public void deactivate(){
        sampledInstrument.getAudioChannel().setMuted(true);
    }

    /** PLAY **/
    @Override
    public void playDrum(){
        liveEvent.play();
    }

    /** RANDOMIZE **/
    @Override
    public void randomizeAll(){
        setRandomPreset();
    }

    /** PRESETS **/
    /** Make sure to have names of the presets that covers the static names in SoundSourcePreset. RndSeqManager will call
     * setPreset() with those **/
    private void setupPresets(){
        presets.add(new SampleCategoryBass());
        presets.add(new SampleCategorySnare());
        presets.add(new SampleCategoryClap());
        presets.add(new SampleCategoryHHClosed());
        presets.add(new SampleCategoryHHOpen());
        presets.add(new SampleCategoryTom());
        presets.add(new SampleCategoryCrash());
        presets.add(new SampleCategoryRide());
        //presets.add(new SampleCategoryHandDrums());
        //presets.add(new SampleCategoryMisc());
    }

    /** RndSeqManager calls this with one of the static strings in SoundSourcePreset, so make sure to cover them and add anything extra class-specific **/
    @Override
    public void setPreset(String preset){
        for (SoundSourcePreset ssp : presets) {
            if (preset.equals(ssp.getName())) {
                selectedCategory = (SampleCategory) ssp;
                selectedCategory.randomizeSample();

                //drumTrack.setName(selectedCategory.getSelectedSample().getName());
                drumTrack.updateEventSamples();
                updateLiveEvent();
            }
        }
    }

    public void setSample(int i){
        selectedCategory.setSelectedSample(i);
        drumTrack.updateEventSamples();
        updateLiveEvent();
    }

    private void updateLiveEvent(){
        liveEvent.setSample(SampleManager.getSample(selectedCategory.getSelectedSample().getName()));
    }

    @Override
    public void setRandomPreset(){
        selectedCategory = (SampleCategory) presets.get(random.nextInt(presets.size()));
        selectedCategory.randomizeSample();
        updateLiveEvent();

        //this will not be done on creation since SmplManager is created before the steps, but it doesn't
        //matter since the steps are created with SmplManager and will create events with the right samples
        //ie only done when randomizing after creation
        drumTrack.updateEventSamples();
    }

    /** SET **/
    @Override
    public void setPan(float pan){
        //Log.e("asd", "pan: "+pan);
        sampledInstrument.getAudioChannel().setPan(pan);
    }

    /** GET **/
    public SampleCategory getSelectedCategory(){
        return selectedCategory;
    }

    /*
    @Override
    public SoundEvents getSoundEvents(int nOfSteps, int subs, int step, boolean add){
        return new SmplEvents(llppdrums, drumSequence, drumTrack, this, nOfSteps, subs, step, add);
    }

     */

    @Override
    public ProcessingChain[] getProcessingChains(){
        ProcessingChain[] pcs = new ProcessingChain[1];
        pcs[0] = sampledInstrument.getAudioChannel().getProcessingChain();
        return pcs;
    }

    @Override
    public BaseInstrument getInstrument(int instrNo){
        return sampledInstrument;
    }

    /** GFX **/
    public int getPresetListImageId() {
        return categoryListImageId;
    }

    public int getSampleListImageId() {
        return sampleListImageId;
    }

    /** RESTORATION **/
    @Override
    public void restore(Keeper k){
        SampleManagerKeeper keeper = (SampleManagerKeeper) k;
        //Log.e("SampleManagerKeeper", "restore(), selectedCategory: "+keeper.selectedCategory);
        selectedCategory = (SampleCategory) presets.get(keeper.selectedCategory);
        setSample(keeper.selectedSample);

        //selectedCategory.setSelectedSample(keeper.selectedSample);
        //drumTrack.updateEventSamples();
        //updateLiveEvent();
    }

    @Override
    public SampleManagerKeeper getKeeper(){
        //Log.e("SampleManagerKeeper", "getKeeper(), presets.indexOf(selectedCategory): "+presets.indexOf(selectedCategory));
        SampleManagerKeeper keeper = new SampleManagerKeeper();
        keeper.selectedCategory = presets.indexOf(selectedCategory);
        keeper.selectedSample = selectedCategory.getSelectedSampleIndex();
        return keeper;
    }

    /** DESTROY **/
    @Override
    public void destroy(){
        //Log.e("SmplManager", "delete");

        Deleter deleter = llppdrums.getDeleter();

        //remove fxs
        sampledInstrument.getAudioChannel().getProcessingChain().reset();
        sampledInstrument.getAudioChannel().getProcessingChain().delete();
        liveInstrument.getAudioChannel().getProcessingChain().reset();
        liveInstrument.getAudioChannel().getProcessingChain().delete();

        //liveEvent.delete();
        deleter.addEvent(liveEvent);
        //liveEvent.setDeletable(true);
        liveEvent = null;

        // calling 'delete()' on all instruments invokes the native layer destructor
        // (and frees memory allocated to their resources, e.g. AudioChannels, Processors)
        if(sampledInstrument != null) {
            //sampledInstrument.delete();
            deleter.addInstrument(sampledInstrument);
            sampledInstrument = null;
        }

        if(liveInstrument != null) {
            //liveInstrument.delete();
            deleter.addInstrument(liveInstrument);
            liveInstrument = null;
        }
    }
}
