package com.kiefer.popups.stackableManager;

import android.view.View;
import android.widget.Button;

import com.kiefer.LLPPDRUMS;
import com.kiefer.R;
import com.kiefer.machine.sequence.track.DrumTrack;
import com.kiefer.machine.sequence.track.Stackables.StackableManager;
import com.kiefer.machine.sequence.track.Stackables.Stacker;

public class SoundManagerPopup extends StackableManagerPopup{

    public SoundManagerPopup(final LLPPDRUMS llppdrums, final StackableManager stackableManager, Stacker stacker, int type){
        super(llppdrums, stackableManager, stacker, type);

        final View customView = llppdrums.getLayoutInflater().inflate(R.layout.layout_sound_manager_custom_area, null);

        Button playBtn = customView.findViewById(R.id.soundManagerPlayBtn);

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DrumTrack)stacker).getSoundManager().play();
            }
        });

        customArea.addView(customView);
    }
}
