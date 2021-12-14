package com.kiefer.utils;

import java.util.Random;

public class NmbrUtils {

    // .9 - 1.1
    public static float getMiniRandomMultiplier(){
        Random random = new Random();
        return random.nextFloat() * .2f + .9f;
    }

    // .75 - 1.25
    public static float getMaxiRandomMultiplier(){
        Random random = new Random();
        return random.nextFloat() * .5f + .75f;
    }

    public static float removeImpossibleNumbers(float n){
        if(n < 0){
            return 0;
        }
        else if(n > 1){
            return 1;
        }
        return n;
    }

    //returns a float between min and max
    public static float getRndmizer(float min, float max){
        float span = max - min;
        Random r = new Random();
        return r.nextFloat() * span + min;
    }
}
