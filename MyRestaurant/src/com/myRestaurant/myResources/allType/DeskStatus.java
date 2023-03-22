package com.myRestaurant.myResources.allType;

public enum DeskStatus {
    SPARE(1),
    BOOKED(2),
    BUSY(3);

    public final int i;

    DeskStatus(int i) {
        this.i = i;
    }
}
