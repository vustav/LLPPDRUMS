package com.kiefer.popups.soundManager.oscillatorManager;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.graphics.customViews.CSpinnerButton;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.machine.sequence.track.Stackables.sound.soundSources.SoundSourceManager;
import com.kiefer.popups.soundManager.SoundManagerPopupOLD;

import java.util.ArrayList;

public class OscillatorManagerView implements SoundManagerPopupOLD.SoundSourceView {
    private final ArrayList<OscillatorView> oscillatorViews;
    private CSpinnerButton presetSpinnerButton;
    private LinearLayout layout;

    public OscillatorManagerView(final LLPPDRUMS llppdrums, final DrumTrack drumTrack){

        //inflate the View
        layout = (LinearLayout) llppdrums.getLayoutInflater().inflate(R.layout.popup_oscillator_manager, null);

        //random
        Button rndBtn = layout.findViewById(R.id.oscManagerRndBtn);
        rndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //drumTrack.getSoundManager().randomizeAll(false);

                //drumTrack.reactivate();

                ((SoundSourceManager) drumTrack.getSoundManager().getSelectedStackable()).getOscillatorManager().randomizeAll();

                for(OscillatorView o: oscillatorViews) {
                    o.updateUI();
                }
            }
        });

        //..and the oscillators
        LinearLayout oscLayout = layout.findViewById(R.id.oscLayout);

        oscillatorViews = new ArrayList<>();
        for(int i = 0; i < llppdrums.getResources().getInteger(R.integer.nOfOscillators); i++){
            OscillatorView oscillatorViewPopup = new OscillatorView(llppdrums, ((SoundSourceManager)drumTrack.getSoundManager().getSelectedStackable()).getOscillatorManager(), drumTrack, i);
            oscillatorViews.add(oscillatorViewPopup);
            oscLayout.addView(oscillatorViewPopup.getLayout());
        }

        //..and the playBtn
        Button playBtn = layout.findViewById(R.id.oscManagerPlayBtn);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //drumTrack.getSoundManager().play();
                ((SoundSourceManager) drumTrack.getSoundManager().getSelectedStackable()).play();
            }
        });
    }

    @Override
    public LinearLayout getLayout(){
        return layout;
    }

    public void updateUI(){
        for(OscillatorView osc : oscillatorViews){
            osc.updateUI();
        }
    }
}
