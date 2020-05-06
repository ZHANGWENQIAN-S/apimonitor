package com.wzy.bp.util;

import org.apache.commons.lang.StringUtils;

import java.util.List;

public class MahjongUtils {
    public static void changeHandTiles(List<String> hand, int movement, String tile) {
        String tiles = hand.get(0);
        String kan = tile.toLowerCase() +tile.toLowerCase() +tile.toLowerCase()+tile.toLowerCase();
        String pon = tile.toLowerCase() +tile.toLowerCase() +tile.toLowerCase();
        String closeKan = tile.toUpperCase() +tile.toUpperCase() +tile.toUpperCase()+tile.toUpperCase();

        switch (movement) {
            case 1:
                tiles = tiles.concat(tile);
                hand.set(0,tiles);
                break;
            case 2:
                int index = tiles.indexOf(tile);
                tiles = StringUtils.replaceOnce(tiles,tile,"");
                hand.set(0,tiles);
                break;
            case 3:
                tiles = tiles.replaceAll(tile,"");
                hand.set(0,tiles);
                hand.add(kan);
                break;
            case 4:
                for(int i = 1;i<hand.size();i++){
                    String tileNow = hand.get(i);
                    if(StringUtils.equals(tileNow,pon)){
                        tileNow=kan;
                    }
                    hand.set(i,tileNow);
                }
                tiles = tiles.replaceAll(tile,"");
                hand.set(0,tiles);
                break;
            case 5:
                tiles = tiles.replaceAll(tile,"");
                hand.set(0,tiles);

                hand.add(closeKan);
                break;
            case 14:
                tiles = StringUtils.replaceOnce(tiles,tile,"");
                tiles = StringUtils.replaceOnce(tiles,tile,"");
                hand.set(0,tiles);
                hand.add(pon);
                break;
        }


    }
}
