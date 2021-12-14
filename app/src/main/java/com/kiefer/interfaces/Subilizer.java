package com.kiefer.interfaces;

public interface Subilizer {
    //this one is used in a sloppy way since half the users (1) doesn't need trackNo =|
    void setNOfSubs(int track, int nOfSubs);
    int getNOfSubs();
}
