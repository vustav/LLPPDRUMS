package com.kiefer.popups.info.commandManager;

import com.kiefer.popups.info.InfoPopup;

public class SelectInfoCommand implements Command{
    private final InfoPopup.InfoNode nodeFrom, nodeTo;
    private final InfoPopup infoPopup;
    private float posTo, posFrom;

    public SelectInfoCommand(InfoPopup.InfoNode nodeFrom, InfoPopup.InfoNode nodeTo, InfoPopup infoPopup, float posTo, float posFrom){
        this.nodeFrom = nodeFrom;
        this.nodeTo = nodeTo;
        this.infoPopup = infoPopup;
        this.posTo = posTo;
        this.posFrom = posFrom;
    }

    @Override
    public boolean execute(){
        infoPopup.setSelectedNode(nodeTo, posTo);
        return true;
    }

    @Override
    public boolean unExecute(){
        infoPopup.setSelectedNode(nodeFrom, posFrom);
        return true;
    }

    @Override
    public boolean isUndoable(){
        return true;
    }

    public void setPosTo(float pos){
        posTo = pos;
    }

    public void setPosFrom(float pos){
        posFrom = pos;
    }

    public InfoPopup.InfoNode getNodeFrom() {
        return nodeFrom;
    }

    public InfoPopup.InfoNode getNodeTo() {
        return nodeTo;
    }
}
