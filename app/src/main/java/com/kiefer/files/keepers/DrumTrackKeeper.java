package com.kiefer.files.keepers;

import com.kiefer.files.keepers.fx.FxManagerKeeper;
import com.kiefer.files.keepers.soundSources.SoundManagerKeeper;

import java.util.ArrayList;

public class DrumTrackKeeper implements Keeper {
    public float volume;
    public int color, nOfSubs;
    public String name;
    public SoundManagerKeeper soundManagerKeeper;
    public FxManagerKeeper fxManagerKeeper;
    public ArrayList<StepKeeper> stepKeepers;
}
