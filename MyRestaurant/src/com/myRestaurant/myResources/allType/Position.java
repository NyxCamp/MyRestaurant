package com.myRestaurant.myResources.allType;

public enum Position {
    MANAGER(1),
    COOK(2),
    RECEPTION(3),
    SERVER(4),
    CLIENT(5);

    public final int i;

    Position(int i) {
        this.i = i;
    }
}
