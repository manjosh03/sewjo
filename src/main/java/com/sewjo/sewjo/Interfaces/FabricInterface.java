package com.sewjo.sewjo.Interfaces;

import java.util.ArrayList;
import java.util.List;

public interface FabricInterface {
    static List<String> getFabricTypes(){
        List <String> patternTypes = new ArrayList<>();
        patternTypes.add("Top");
        patternTypes.add("Bottom");
        patternTypes.add("Dress");
        patternTypes.add("Jacket");
        patternTypes.add("Coat");
        patternTypes.add("Accessories");
        patternTypes.add("Other");
        return patternTypes;
    }
}