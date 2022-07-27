package com.kiefer.files.keepers.soundSources;

import com.kiefer.files.keepers.Keeper;
import com.kiefer.files.keepers.fx.FxKeeper;

import java.util.ArrayList;

public class SoundManagerKeeper implements Keeper {
    public ArrayList<SoundSourceManagerKeeper> soundSourceManagerKeepers;
    public int selectedSSMIndex;
}
