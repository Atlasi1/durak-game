package com.example.durak_game;

public class Helper {
    public static String getImageString(Card cd) {
        String filename = "images/";
        if(cd.getValue().getRank() >= 6 && cd.getValue().getRank() <= 10) {
            filename += cd.getValue().getRank();
        }
        else {
            filename += cd.getValue().name().toLowerCase();
        }
        return filename.concat("_of_" + cd.getSuit().name().toLowerCase() + ".png");
    }

}
