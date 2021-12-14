package com.kiefer.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kiefer.LLPPDRUMS;
import com.kiefer.controller.Controller;
import com.kiefer.R;
import com.kiefer.info.controller.ControllerInfo;
import com.kiefer.popups.info.InfoPopup;

public class ControllerFragment extends Fragment {
    private LLPPDRUMS llppdrums;
    private Controller controller;

    public ControllerFragment() {
        // Required empty public constructor
    }

    public static ControllerFragment newInstance() {
        return new ControllerFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        llppdrums = (LLPPDRUMS) getActivity();
        controller = llppdrums.getController();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_controller, container, false);

        FrameLayout counterHolder = view.findViewById(R.id.controllerCounterHolder);
        counterHolder.addView(llppdrums.getController().getSequenceManager().getCounter().getControllerLayout());

        //PREV
        ImageView prevSeqBtn = view.findViewById(R.id.controllerPrevSeqBtn);
        prevSeqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.selectPrevSeq();
            }
        });

        //SEQ
        final Button editSeqButton = view.findViewById(R.id.controllerSeqEditBtn);
        editSeqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.openEditSeqPopup();
            }
        });

        final Button seqBtn = view.findViewById(R.id.controllerSeqBtn);
        seqBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        controller.getSeqBtnManager().buttonPressed(seqBtn);
                        break;

                    case MotionEvent.ACTION_UP:
                        controller.getSeqBtnManager().buttonReleased(seqBtn);
                        break;
                }
                return false;
            }
        });

        //FUN
        final Button editFunButton = view.findViewById(R.id.controllerFunEditBtn);
        editFunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.openEditFunPopup();
            }
        });

        final Button funBtn = view.findViewById(R.id.controllerFuncBtn);
        /*
        funBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.funBtnPressed();
            }
        });

         */

        //NEXT
        ImageView nextSeqBtn = view.findViewById(R.id.controllerNextSeqBtn);
        nextSeqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.selectNextSeq();
            }
        });

        //set up the infoBtn
        ImageView infoBtn = view.findViewById(R.id.controllerInfoBtn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InfoPopup(llppdrums, ControllerInfo.key);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ViewGroup)controller.getSequenceManager().getCounter().getControllerLayout().getParent()).removeAllViews();
    }

    /** TABS **/


    /** UI **/
    public void updateUI(Controller controller){

    }
}
