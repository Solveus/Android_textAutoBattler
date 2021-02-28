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

public class Weapon implements Serializable {
    public enum qualityWeapon {Common, Rare, Epic}

    public enum typeWeapon {Hands, Sword, Dagger, Staff}

    private String name;
    private qualityWeapon quality;
    private typeWeapon type;
    private int physicDamage;
    private int magicDamage;

    private static double[] qualityBuff;

    public Weapon() {
        setName("Strong Fists");
        setQuality(qualityWeapon.Common);
        setType(typeWeapon.Hands);
        setPhysicDamage(5);
        setMagicDamage(0);

        qualityBuff = new double[]{1.0, 1.2, 1.5};
    }

    public Weapon(String name, qualityWeapon quality, typeWeapon type, int physicDamage, int magicDamage) {
        setName(name);
        setQuality(quality);
        setType(type);
        setPhysicDamage(physicDamage);
        setMagicDamage(magicDamage);

        qualityBuff = new double[]{1.0, 1.2, 1.5};
    }

    public void setType(typeWeapon type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMagicDamage(int magicDamage) {
        this.magicDamage = magicDamage;
    }

    public void setPhysicDamage(int physicDamage) {
        this.physicDamage = physicDamage;
    }

    public void setQuality(qualityWeapon quality) {
        this.quality = quality;
    }

    public typeWeapon getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public int getMagicDamage() {
        return magicDamage;
    }

    public int getPhysicDamage() {
        return physicDamage;
    }

    public qualityWeapon getQuality() {
        return quality;
    }

    public static double[] getQualityBuff() {
        return qualityBuff;
    }
}

