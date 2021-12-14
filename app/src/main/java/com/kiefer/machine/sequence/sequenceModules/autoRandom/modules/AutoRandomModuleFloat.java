package com.kiefer.machine.sequence.sequenceModules.autoRandom.modules;

import com.kiefer.LLPPDRUMS;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.machine.sequence.track.DrumTrack;

import java.util.ArrayList;

public abstract class AutoRandomModuleFloat extends AutoRandomModule {
    private final ArrayList<String> autoStepValues;

    public abstract void setAutoRndValue(Step step, float value, int sub);

    public AutoRandomModuleFloat(LLPPDRUMS llppdrums, SequenceModule sequenceModule) {
        super(llppdrums, sequenceModule);

        autoStepValues = new ArrayList<>();
        autoStepValues.add("1");
        autoStepValues.add("0.8");
        autoStepValues.add("0.6");
        autoStepValues.add("0.4");
        autoStepValues.add("0.2");
        autoStepValues.add("0");
    }

    /** AUTO STEP **/
    @Override
    public void setAutoValue(final DrumTrack drumTrack, String s, int sub) {
        float value = Float.parseFloat(s);
        for (int step = 0; step < drumTrack.getSteps().size(); step++) {
            Step drum = drumTrack.getSteps().get(step);
            setAutoRndValue(drum, value, sub);
        }
    }

    /** GET **/
    @Override
    public ArrayList<String> getAutoStepValues(){
        return autoStepValues;
    }
}
