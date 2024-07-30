package com.sewjo.sewjo.Interfaces;

import java.util.ArrayList;
import java.util.List;

public interface FabricInterface {
    static List<String> getFabricTypes() {
        List<String> fabricTypes = new ArrayList<>();
        fabricTypes.add("Cotton");
        fabricTypes.add("Linen");
        fabricTypes.add("Silk");
        fabricTypes.add("Velvet");
        fabricTypes.add("Wool");
        fabricTypes.add("Leather");
        fabricTypes.add("Other");
        return fabricTypes;
    }
}