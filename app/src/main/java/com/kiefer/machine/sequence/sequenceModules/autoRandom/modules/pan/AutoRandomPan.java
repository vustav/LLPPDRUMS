package com.kiefer.machine.sequence.sequenceModules.autoRandom.modules.pan;

import com.kiefer.LLPPDRUMS;
import com.kiefer.machine.sequence.DrumSequence;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.machine.sequence.sequenceModules.autoRandom.AutoRandom;

import java.util.ArrayList;

/** Same as parent but modified to work on a step instead of a sub **/

public class AutoRandomPan extends AutoRandom {

    public AutoRandomPan(LLPPDRUMS llppdrums, DrumSequence drumSequence, SequenceModule sequenceModule, int tabIndex){
        super(llppdrums, drumSequence, sequenceModule, tabIndex);
    }

    @Override
    protected void setup(){
        modules = new ArrayList<>();
        modules.add(new AutoRandomPercPan(llppdrums, sequenceModule));
        modules.add(new AutoRandomMinPan(llppdrums, sequenceModule));
        modules.add(new AutoRandomMaxPan(llppdrums, sequenceModule));
        modules.add(new AutoRandomReturnPan(llppdrums, sequenceModule));
        selectedModule = modules.get(0);
    }
}
