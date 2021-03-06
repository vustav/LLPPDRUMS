package com.kiefer.machine.sequence.sequenceModules.autoRandom;

import android.graphics.Bitmap;

import com.kiefer.LLPPDRUMS;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.modules.AutoRandomPerc;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.modules.AutoRandomReturn;
import com.kiefer.ui.tabs.TabManager;
import com.kiefer.utils.ImgUtils;

import java.util.ArrayList;

/** Used by OnOff. The only thing it does is overriding setup with less modules, since OnOff only has perc and return. **/

public class AutoRandomOnOff extends AutoRandom{

    public AutoRandomOnOff(LLPPDRUMS llppdrums, DrumSequence drumSequence, SequenceModule sequenceModule, int tabIndex){
        super(llppdrums, drumSequence, sequenceModule, tabIndex);
    }

    @Override
    protected void setup(){
        modules = new ArrayList<>();
        modules.add(new AutoRandomPerc(llppdrums, sequenceModule));
        modules.add(new AutoRandomReturn(llppdrums, sequenceModule));
        selectedModule = modules.get(0);
    }
}
