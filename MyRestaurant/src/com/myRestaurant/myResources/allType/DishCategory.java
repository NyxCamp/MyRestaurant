package com.myRestaurant.myResources.allType;

import java.io.Serializable;

public enum DishCategory implements Serializable {
    STAPLE(1),
    SPECIAL(2),
    DRINK(3),
    COLD(4);

    public final int i;

    DishCategory(int i) {
        this.i = i;
    }

    @Override
    public String toString() {
        switch (i){
            case 1 :{
                return "主食";
            }
            case 2 :{
                return "特色菜";
            }
            case 3 :{
                return "饮品";
            }
            case 4 :{
                return "凉菜";
            }
            default:{
                return "无类别";
            }
        }
    }
}
