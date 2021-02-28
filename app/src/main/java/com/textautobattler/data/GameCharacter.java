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

import java.io.Serializable;

public class GameCharacter implements Serializable {

    private String name;
    private int health;
    private int maxHealth;
    private int attackPower;
    private int magicPower;
    private Weapon weapon;
    private boolean alive;

    public GameCharacter(String name, int health, int attackPower, int magicPower, Weapon weapon) {
        setName(name);
        setHealth(health);
        setAttackPower(attackPower);
        setMagicPower(magicPower);
        setAlive(true);
        setWeapon(weapon);
        setMaxHealth(health);
    }

    public void setName(String name) {
        if (name.length() == 0) {
            throw new IllegalArgumentException("Uncorrect name!");
        }
        this.name = name;
    }

    public void setAttackPower(int attackPower) {
        if (attackPower < 0) {
            this.attackPower = 0;
        }
        this.attackPower = attackPower;
    }

    public void setHealth(int health) {
        if (health < 0)
            health = 0;

        this.health = health;
    }

    public void setMaxHealth(int maxHealth) {
        if (maxHealth < 0)
            maxHealth = 0;
        this.maxHealth = maxHealth;
    }

    public void setMagicPower(int magicPower) {
        if (magicPower < 0) {
            this.magicPower = 0;
        }
        this.magicPower = magicPower;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getMagicPower() {
        return magicPower;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public String getName() {
        return name;
    }

    public Boolean getAlive() {
        return alive;
    }


    // random only players attack power
    private static int damageRand(int damage) {
        //damage dispersion is +- 20% of attack power
        int dispersion = (damage / 5);
        int min = damage - dispersion;
        int max = (damage + dispersion) - min;
        return (int) ((Math.random() * ++max) + min); // number in range [min; max]
    }

    public void heal(double percent) {
        // Input double as percent, 0.1 = 10%
        if (percent > 1) percent = 1;
        if (percent < 0) percent = 0;

        setHealth((int) (health + maxHealth * percent));
    }

    public void resurrection() {
        alive = true;
        setHealth(maxHealth);
    }

    public static String attack(GameCharacter characterOne, GameCharacter characterTwo) {

        String log = "";

        if (characterTwo.getHealth() <= 0) {
            characterTwo.setAlive(false);
            return log;
        }

        if (characterOne.getHealth() <= 0) {
            characterOne.setAlive(false);
            return log;
        }


        int damage;
        String typeOfDamage;

        double weaponQualityBuff = Weapon.getQualityBuff()[characterOne.weapon.getQuality().ordinal()];

        // if mage damage > physic damage
        if ((characterOne.magicPower + characterOne.weapon.getMagicDamage())
                > characterOne.attackPower + characterOne.weapon.getPhysicDamage()) {

            damage = (int) (damageRand(characterOne.magicPower) + characterOne.weapon.getMagicDamage() * weaponQualityBuff);
            typeOfDamage = "magic";
        } else {
            damage = (int) (damageRand(characterOne.attackPower) + characterOne.weapon.getPhysicDamage() * weaponQualityBuff);
            typeOfDamage = "physic";
        }
        // if attackPower == magicPower can add armor with physic and magic resists and choose higher damage

        characterTwo.setHealth(characterTwo.getHealth() - damage);
        log = ("\n" + characterOne.getName() + " make " + damage + " damages " + characterTwo.getName() + ". Type of damage " + typeOfDamage);

        return log;
    }


}