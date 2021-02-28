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

import android.content.Context;
import android.text.Html;
import android.widget.EditText;

import java.io.Serializable;
import java.util.ArrayList;

import static com.textautobattler.data.HtmlUtils.BR;
import static com.textautobattler.data.HtmlUtils.COLOR_BLUE;
import static com.textautobattler.data.HtmlUtils.COLOR_GREEN;
import static com.textautobattler.data.HtmlUtils.COLOR_ORANGE;
import static com.textautobattler.data.HtmlUtils.COLOR_RED;
import static com.textautobattler.data.HtmlUtils.COLOR_VIOLET;
import static com.textautobattler.data.HtmlUtils.TAG_CLOSE;

public class Game implements Serializable {

    // settings
    private final int DEFAULT_SLEEP = 1;
    private int sleepSec;
    private boolean btnSkipLog;

    // game variables
    private ArrayList<GameCharacter> gameCharacterList;
    private ArrayList<Weapon> weaponList;

    private ArrayList<String> gameCharacterNamesList;
    private ArrayList<String> weaponNamesList;

    private GameCharacter gameCharacterOne;
    private GameCharacter gameCharacterTwo;

    public Game(Context context) {
        init(context);
    }

    private void init(Context context) {
        gameCharacterList = new ArrayList<>();
        weaponList = new ArrayList<>();
        gameCharacterNamesList = new ArrayList<>();
        weaponNamesList = new ArrayList<>();

        sleepSec = DEFAULT_SLEEP;

        // init start weapons
        initWeapons();
        initCharacters();
        // init uses for set file create path
        XmlAssistant.initAssistant(context);
        parseXml();
    }


    public void setBtnSkipLog(boolean btnSkipLog) {
        this.btnSkipLog = btnSkipLog;
    }

    public boolean getBtnSkipLog() {
        return btnSkipLog;
    }

    public GameCharacter getGameCharacterOne() {
        return gameCharacterOne;
    }

    public GameCharacter getGameCharacterTwo() {
        return gameCharacterTwo;
    }

    public void setGameCharacterOne(GameCharacter gameCharacterOne) {
        this.gameCharacterOne = gameCharacterOne;
    }

    public void setGameCharacterTwo(GameCharacter gameCharacterTwo) {
        this.gameCharacterTwo = gameCharacterTwo;
    }

    public ArrayList<GameCharacter> getGameCharacterList() {
        return gameCharacterList;
    }

    public ArrayList<Weapon> getWeaponList() {
        return weaponList;
    }

    public ArrayList<String> getGameCharacterNamesList() {
        return gameCharacterNamesList;
    }

    public ArrayList<String> getWeaponNamesList() {
        return weaponNamesList;
    }

    public void parseXml() {
        if (XmlAssistant.fileCharactersExists())
            addCharactersFromXml(XmlAssistant.parseCharacters());

        if (XmlAssistant.fileWeaponExists())
            addWeaponFromXml(XmlAssistant.parseWeapons());
    }

    public void addCharactersFromXml(ArrayList<GameCharacter> list) {
        // if XML file empty or not exists
        if (list == null)
            throw new NullPointerException("gameCharacters.xml not exists!");

        for (int i = 0; i < list.size(); i++)
            addCharacter(list.get(i));
    }

    // add game character in list
    public void addWeaponFromXml(ArrayList<Weapon> list) {
        if (list == null)
            throw new NullPointerException("weapon.xml not exists!");

        for (int i = 0; i < list.size(); i++)
            addWeapon(list.get(i));
    }

    // add game character in list
    public boolean addCharacter(GameCharacter character) {

        if (character == null)
            throw new NullPointerException();

        // check duplicates by names
        for (String name : gameCharacterNamesList)
            if (name.equals(character.getName()))
                return false;

        // add character
        gameCharacterList.add(character);
        // add name
        gameCharacterNamesList.add(character.getName());

        return true;
    }

    // add weapon in list
    public boolean addWeapon(Weapon weapon) {

        if (weapon == null)
            throw new NullPointerException();

        // check duplicates by names
        for (String name : weaponNamesList)
            if (name.equals(weapon.getName()))
                return false;

        // add weapon
        weaponList.add(weapon);
        // add weapon name
        weaponNamesList.add(weapon.getName());

        return true;
    }

    // remove character
    public boolean removeCharacterByIndex(int index) {
        if (index < 0 || index > gameCharacterList.size())
            throw new NullPointerException();

        // list must be have minimum 1 character
        if (gameCharacterList.size() == 1)
            return false;

        // delete character from xml file by name
        XmlAssistant.removeGameCharacter(getGameCharacterNamesList().get(index));
        gameCharacterList.remove(index);
        gameCharacterNamesList.remove(index);

        return true;
    }

    // remove weapon
    public boolean removeWeaponByIndex(int index) {
        if (index < 0 || index > weaponList.size())
            throw new NullPointerException();

        // list must be have minimum 1 weapon
        if (weaponList.size() == 1) {
            return false;
        }

        XmlAssistant.removeWeapon(getWeaponNamesList().get(index));
        weaponList.remove(index);
        weaponNamesList.remove(index);

        return true;
    }

    // init default weapons
    private void initWeapons() {
        addWeapon(new Weapon());
    }

    // init default characters
    private void initCharacters() {
        addCharacter(new GameCharacter("Developer", 200, 0, 50, new Weapon()));
    }

    // battle log
    public void battle(GameCharacter oneGameCharacter, GameCharacter twoGameCharacter, EditText logWindow, Boolean skipSetting) {

        String characterInfo = BR + COLOR_VIOLET + oneGameCharacter.getName() + " VS " + twoGameCharacter.getName() + BR;
        logWindow.append(Html.fromHtml(characterInfo));

        Thread battle = new Thread(() -> {
            String winner;

            for (int i = 1; oneGameCharacter.getAlive() && twoGameCharacter.getAlive(); i++) {
                try {
                    if (!btnSkipLog && !skipSetting)
                        Thread.sleep(sleepSec * 1000); // in seconds

                    // print in editText round message in thread
                    String round = BR + COLOR_ORANGE + TextMessages.getRoundText(i) + TAG_CLOSE + BR;
                    logWindow.post(() -> logWindow.append(Html.fromHtml(round)));

                    // print in editText health message in thread
                    String health = BR + COLOR_GREEN + TextMessages.getHealthText(oneGameCharacter, twoGameCharacter) + TAG_CLOSE + BR;
                    logWindow.post(() -> logWindow.append(Html.fromHtml(health)));

                    // print in editText attackOne message in thread
                    String attackOne = BR + COLOR_RED + GameCharacter.attack(oneGameCharacter, twoGameCharacter) + TAG_CLOSE + BR;
                    logWindow.post(() -> logWindow.append(Html.fromHtml(attackOne)));

                    // print in editText attackTwo message in thread
                    String attackTwo = BR + COLOR_RED + GameCharacter.attack(twoGameCharacter, oneGameCharacter) + TAG_CLOSE + BR;
                    logWindow.post(() -> logWindow.append(Html.fromHtml(attackTwo)));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (oneGameCharacter.getAlive())
                winner = COLOR_BLUE + TextMessages.getWinnerText(oneGameCharacter) + TAG_CLOSE + BR;
            else
                winner = COLOR_BLUE + TextMessages.getWinnerText(twoGameCharacter) + TAG_CLOSE + BR;

            // print in editText winner message in thread
            logWindow.post(() -> logWindow.append(Html.fromHtml(winner)));
        });
        battle.start();

        if (btnSkipLog && !skipSetting)
            btnSkipLog = false;
    }
}