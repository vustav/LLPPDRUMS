package com.kiefer.files.keepers.soundSources;

import com.kiefer.files.keepers.Keeper;

import java.util.ArrayList;

public class SoundSourceManagerKeeper implements Keeper {
    public int activeSoundSourceIndex;
    public ArrayList<Keeper> ssKeepers;
}
