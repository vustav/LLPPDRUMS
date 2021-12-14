package com.kiefer.randomization.presets;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.randomization.rndSeqManager.RndSeqManager;

public class RandomizeSeqPresetWaltz extends RandomizeSeqPreset {

    public RandomizeSeqPresetWaltz(LLPPDRUMS llppdrums, RndSeqManager rndSeqManager){
        super(llppdrums, rndSeqManager, llppdrums.getResources().getString(R.string.randomizerWaltzName));
    }

    @Override
    public void createPreset() {
        /*
        int tempo = 120;
        int steps = 6;
        int beats = 3;

        ArrayList<RndSeqPresetTrack> tracks = new ArrayList<>();

        RndSeqPresetTrack track  = createBasicBaseTrack(steps, beats);
        track.setRandomizeFx(true);
        track.setRandomizePan(true);
        track.setRandomizeVol(false);
        tracks.add(track);

        track = createBasicSnareTrack(steps, beats);
        for(int sub = 0; sub < track.getnOfSubs(); sub++) {
            track.setSubPerc(1, sub, 1);
            track.setStepVol(1, sub, NmbrUtils.getRndmizer(.3f, .7f));
            track.setStepPitch(1, sub, NmbrUtils.getRndmizer(.4f, .5f));
            track.setSubPerc(4, sub, 1);
            track.setStepVol(4, sub, NmbrUtils.getRndmizer(.3f, .7f));
            track.setStepPitch(4, sub, NmbrUtils.getRndmizer(.4f, .5f));
        }
        tracks.add(track);

        track  = createBasicHHTrack(steps);
        track.setRandomizeFx(true);
        track.setRandomizePan(true);
        track.setRandomizeVol(false);
        tracks.add(track);

        track  = createRandomTrack(steps);
        track.setRandomizeFx(true);
        track.setRandomizePan(true);
        track.setRandomizeVol(false);
        tracks.add(track);

        rndSeqManager.setTracks(tracks);
        rndSeqManager.setTempo(tempo);

         */
    }
}
