package com.ct.cacatrackerproject.utils;

import java.util.HashSet;
import java.util.Set;

public class CheckCodigoPostal {
    public static final Set<String> codigoPostal = new HashSet<>();

    static {
        codigoPostal.add("03559");
        codigoPostal.add("03540");
        codigoPostal.add("03114");
        codigoPostal.add("03016");
        codigoPostal.add("03015");
        codigoPostal.add("03014");
        codigoPostal.add("03013");
        codigoPostal.add("03012");
        codigoPostal.add("03011");
        codigoPostal.add("03010");
        codigoPostal.add("03009");
        codigoPostal.add("03008");
        codigoPostal.add("03007");
        codigoPostal.add("03006");
        codigoPostal.add("03005");
        codigoPostal.add("03004");
        codigoPostal.add("03003");
        codigoPostal.add("03002");
        codigoPostal.add("03001");
    }

    public boolean cpValido(String cp){
        return codigoPostal.contains(cp);
    }
}
