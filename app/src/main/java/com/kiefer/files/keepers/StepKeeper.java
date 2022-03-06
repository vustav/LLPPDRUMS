package com.kiefer.files.keepers;

import com.kiefer.files.keepers.soundSources.SoundEventsKeeper;
import com.kiefer.files.keepers.soundSources.StepEventsManagerKeeper;

public class StepKeeper implements Keeper {
    public boolean on;
    public StepEventsManagerKeeper stepEventsManagerKeeper;
}
