package com.sensor.test.linker.common;

import java.util.ArrayList;

public class ListOfAccData extends ArrayList<AccelerometerData> {


    public int BinarySearch(AccelerometerData keyElement) {
        var endIndex = this.size();
        var startIndex = 0;
        var midIndex = (endIndex+startIndex) / 2;
        while(this.get(midIndex).submitDateDouble !=  keyElement.submitDateDouble) {
            if (this.get(midIndex).submitDateDouble < keyElement.submitDateDouble) {
                startIndex = midIndex;
            } else {
                endIndex = midIndex;
            }
            midIndex = (endIndex+startIndex) / 2;
        }
        get(midIndex).setVisited(true);
        return midIndex;
    }


}
