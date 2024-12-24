package com.example.demo.Enum;

import lombok.Getter;

@Getter
public enum Mois {
    JANUARY(1),
    FEBRUARY(2),
    MARCH(3),
    APRIL(4),
    MAY(5),
    JUNE(6),
    JULY(7),
    AUGUST(8),
    SEPTEMBER(9),
    OCTOBER(10),
    NOVEMBER(11),
    DECEMBER(12);

    private final int value;


    Mois(int value) {
        this.value = value;
    }

}
