package com.mb.bank.service;

import java.util.ArrayList;
import java.util.Collections;

public class UniqueRandomNumbers {

    public String generate(){
        StringBuilder sb = new StringBuilder();
        ArrayList<Integer> list = new ArrayList<>();
        for (int i=1; i<22; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        for (int i=0; i<6; i++) {
            sb.append(list.get(i));
        }
        return sb.toString();
    }
}
