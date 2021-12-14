package com.kiefer.files.keepers.soundSources;

import com.kiefer.files.keepers.Keeper;

import java.util.ArrayList;

public class SoundManagerKeeper implements Keeper {
    public int activeSoundSourceIndex;
    public ArrayList<Keeper> ssKeepers;
}
