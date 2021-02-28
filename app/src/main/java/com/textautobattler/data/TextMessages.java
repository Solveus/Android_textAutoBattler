/*
 * Copyright (c) 2021.
 *
 * Author: Finogenov Vasily
 * A.K.A : Solveus, solveus_666
 *
 * Github: https://github.com/Solveus
 *
 */
package com.textautobattler.data;


public class TextMessages {

    public static String getWinnerText(GameCharacter winner) {
        return (winner.getName() + " has win in this battle!");
    }

    public static String getRoundText(int number) {
        return ("Round " + number + ":");
    }

    public static String getHealthText(GameCharacter player, GameCharacter enemy) {
        return (player.getName() + " health: " + player.getHealth() + "; " + enemy.getName() + " health: " + enemy.getHealth() + ";");
    }
}
