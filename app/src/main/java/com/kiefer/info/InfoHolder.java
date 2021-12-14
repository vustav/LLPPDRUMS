package com.kiefer.info;

import com.kiefer.LLPPDRUMS;

import java.util.ArrayList;

public abstract class InfoHolder {
    protected final LLPPDRUMS llppdrums;
    protected ArrayList<Info> infos;

    public abstract void setupInfos();

    public InfoHolder(LLPPDRUMS llppdrums){
        this.llppdrums = llppdrums;
        infos = new ArrayList<>();
        setupInfos();
    }

    public Info getInfo(String key){
        for(Info info : infos){
            if(info.getKey().equals(key)){
                return info;
            }
        }
        return null;
    }

    public ArrayList<Info> getInfos(){
        return infos;
    }
}
