package com.kiefer.machine.sequence.sequenceModules.autoRandom.modules;

import com.kiefer.LLPPDRUMS;
import com.kiefer.machine.sequence.track.Step;
import com.kiefer.machine.sequence.sequenceModules.SequenceModule;
import com.kiefer.machine.sequence.track.DrumTrack;

import java.util.ArrayList;
import java.util.Random;

public abstract class AutoRandomModuleBool extends AutoRandomModule {
    private final ArrayList<String> autoStepValues;
    protected int autoStepInterval = -1;
    private final Random random;

    public abstract void setAutoRndOn(Step step, boolean on, int sub);
    public abstract boolean getAutoRndOn(Step step, int sub);

    public AutoRandomModuleBool(LLPPDRUMS llppdrums, SequenceModule sequenceModule) {
        super(llppdrums, sequenceModule);

        //set up the randomAutoValues
        autoStepValues = new ArrayList<>();
        autoStepValues.add(SequenceModule.NONE);
        autoStepValues.add(SequenceModule.ALL);
        autoStepValues.add(SequenceModule.EVERY_SECOND);
        autoStepValues.add(SequenceModule.EVERY_THIRD);
        autoStepValues.add(SequenceModule.EVERY_FOURTH);
        autoStepValues.add(SequenceModule.EVERY_FIFTH);
        autoStepValues.add(SequenceModule.EVERY_SIXTH);
        autoStepValues.add(SequenceModule.EVERY_SEVENTH);

        random = new Random();
    }

    /** AUTO STEP **/
    @Override
    public void setAutoValue(final DrumTrack drumTrack, String s, int sub) {
        autoStepInterval = Integer.parseInt(s.substring(s.length() - 1));

        for (int step = 0; step < drumTrack.getSteps().size(); step++) {

            Step drum = drumTrack.getSteps().get(step);

            //if the interval isn't 0
            if (autoStepInterval != 0) {
                //if mod0 it should be on
                boolean on = step % autoStepInterval == 0;
                setAutoRndOn(drum, on, sub);
            }

            //if the interval is 0 just turn them all off
            else {
                setAutoRndOn(drum, false, sub);
            }
        }
    }

    public void pushLeft(DrumTrack drumTrack){
        /*
        ArrayList<Step> steps = drumTrack.getDrums();

        //make an array of the autoOn-values (skip the first since its the one that gets pushed off). This is what we'll work on.
        ArrayList<Boolean> onValues = new ArrayList<>();
        for (int step = 1; step < steps.size(); step++) {
            onValues.add(getAutoRndOn(steps.get(step)));
        }

        //add a new value at the end
        onValues.add(false);

        //set the last antry in onValues to the correct boolean
        switch (autoStepInterval) {
            case 1:
                onValues.set(onValues.size() - 1, true);
                break;

            case 2:
                //if the second to last is off, turn on the last
                if (!(onValues.get(onValues.size() - 2))) {
                    onValues.set(onValues.size() - 1, true);
                }
                break;

            case 3:
                //if the second and third to last is off, turn on the last
                if (!(onValues.get(onValues.size() - 2)) &&
                        !(onValues.get(onValues.size() - 3))) {
                    onValues.set(onValues.size() - 1, true);
                }
                break;

            case 4:
                //etc...
                if (!(onValues.get(onValues.size() - 2)) &&
                        !(onValues.get(onValues.size() - 3)) &&
                        !(onValues.get(onValues.size() - 4))) {
                    onValues.set(onValues.size() - 1, true);
                }
                break;

            case 5:
                if (!(onValues.get(onValues.size() - 2)) &&
                        !(onValues.get(onValues.size() - 3)) &&
                        !(onValues.get(onValues.size() - 4)) &&
                        !(onValues.get(onValues.size() - 5))) {
                    onValues.set(onValues.size() - 1, true);
                }
                break;

            case 6:
                if (!(onValues.get(onValues.size() - 2)) &&
                        !(onValues.get(onValues.size() - 3)) &&
                        !(onValues.get(onValues.size() - 4)) &&
                        !(onValues.get(onValues.size() - 5)) &&
                        !(onValues.get(onValues.size() - 6))) {
                    onValues.set(onValues.size() - 1, true);
                }
                break;

            case 7:
                if (!(onValues.get(onValues.size() - 2)) &&
                        !(onValues.get(onValues.size() - 3)) &&
                        !(onValues.get(onValues.size() - 4)) &&
                        !(onValues.get(onValues.size() - 5)) &&
                        !(onValues.get(onValues.size() - 6)) &&
                        !(onValues.get(onValues.size() - 7))) {
                    onValues.set(onValues.size() - 1, true);
                }
                break;

            //randomize of no pattern is selected
            default:
                onValues.set(onValues.size() - 1, random.nextInt(2) == 1);
                break;
        }

        //update the drums with the new autoRnd-values
        for (int step = 0; step < steps.size(); step++) {
            setAutoRndOn(steps.get(step), onValues.get(step));
        }

         */
    }

    public void pushRight(DrumTrack drumTrack){
        /*
        ArrayList<Step> steps = drumTrack.getDrums();

        //make an array of the autoOn-values (skip the last one since its the one getting pushed off). This is what we'll work on.
        ArrayList<Boolean> onValues = new ArrayList<>();
        for (int step = 0; step < steps.size() - 1; step++) {
            onValues.add(getAutoRndOn(steps.get(step)));
        }
        //add a new value to the start of the array
        onValues.add(0, false);

        //set the last antry in onValues to the correct boolean
        switch (autoStepInterval) {
            case 1:
                onValues.set(0, true);
                break;

            case 2:
                //if the second to last is off, turn on the last
                if (!(onValues.get(1))) {
                    onValues.set(0, true);
                }
                break;

            case 3:
                //if the second and third to last is off, turn on the last
                if (!(onValues.get(1)) &&
                        !(onValues.get(2))) {
                    onValues.set(0, true);
                }
                break;

            case 4:
                //etc...
                if (!(onValues.get(1)) &&
                        !(onValues.get(2)) &&
                        !(onValues.get(3))) {
                    onValues.set(0, true);
                }
                break;

            case 5:
                if (!(onValues.get(1)) &&
                        !(onValues.get(2)) &&
                        !(onValues.get(3)) &&
                        !(onValues.get(4))) {
                    onValues.set(0, true);
                }
                break;

            case 6:
                if (!(onValues.get(1)) &&
                        !(onValues.get(2)) &&
                        !(onValues.get(3)) &&
                        !(onValues.get(4)) &&
                        !(onValues.get(5))) {
                    onValues.set(0, true);
                }
                break;

            case 7:
                if (!(onValues.get(1)) &&
                        !(onValues.get(2)) &&
                        !(onValues.get(3)) &&
                        !(onValues.get(4)) &&
                        !(onValues.get(5)) &&
                        !(onValues.get(6))) {
                    onValues.set(0, true);
                }
                break;

            //randomize of no pattern is selected
            default:
                onValues.set(0, random.nextInt(2) == 1);
                break;
        }

        //update the drums with the new autoRnd-values
        for (int step = 0; step < steps.size(); step++) {
            setAutoRndOn(steps.get(step), onValues.get(step));
        }

         */
    }

    /** GET **/
    @Override
    public ArrayList<String> getAutoStepValues(){
        return autoStepValues;
    }
}
