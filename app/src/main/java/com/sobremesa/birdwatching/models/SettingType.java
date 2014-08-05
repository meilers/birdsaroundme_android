package com.sobremesa.birdwatching.models;

/**
 * Created by omegatai on 2014-07-09.
 */
public enum SettingType {
    DISTANCE(0), DATE(1), SORT_BY(2);
    private final int value;

    private SettingType(int value) {
        this.value = value;
    }
}