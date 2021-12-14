package com.kiefer.info;

import com.kiefer.LLPPDRUMS;
import com.kiefer.info.controller.ControllerInfo;
import com.kiefer.info.main.MainInfo;
import com.kiefer.info.sequence.SequenceInfo;
import com.kiefer.info.sequenceManager.SequenceManagerInfo;
import com.kiefer.popups.info.InfoPopup;

public class InfoManager extends InfoHolder{

    public InfoManager(LLPPDRUMS llppdrums){
        super(llppdrums);
    }
    private InfoPopup infoPopup;

    @Override
    public void setupInfos(){
        infos.add(new MainInfo(llppdrums));
        infos.add(new ControllerInfo(llppdrums));
        infos.add(new SequenceManagerInfo(llppdrums));
        infos.add(new SequenceInfo(llppdrums));
    }

    //Infos need a reference to infoPopup if they use links. Created infoPopups will add themselves when created and remove themselves when destroyed.
    public void setInfoPopup(InfoPopup infoPopup){
        this.infoPopup = infoPopup;
    }

    public void destroyInfoPopup(){
        infoPopup = null;
    }

    public InfoPopup getInfoPopup() {
        return infoPopup;
    }
}
