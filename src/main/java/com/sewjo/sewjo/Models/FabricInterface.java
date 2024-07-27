package com.sewjo.sewjo.Models;

import java.util.ArrayList;
import java.util.List;

public interface FabricInterface {
    static List<String> getFabricTypes() {
        List<String> patternTypes = new ArrayList<>();
        patternTypes.add("Cotton");
        patternTypes.add("Linen");
        patternTypes.add("Silk");
        patternTypes.add("Velvet");
        patternTypes.add("Wool");
        patternTypes.add("Leather");
        patternTypes.add("Other");
        return patternTypes;
    }
}