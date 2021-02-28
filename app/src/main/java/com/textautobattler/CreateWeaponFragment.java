/*
 * Copyright (c) 2021.
 *
 * Author: Finogenov Vasily
 * Nickname: Solveus, solveus_666
 *
 * Github: https://github.com/Solveus
 *
 *
 */

package com.textautobattler;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.textautobattler.data.Game;
import com.textautobattler.data.Weapon;
import com.textautobattler.data.XmlAssistant;

public class CreateWeaponFragment extends DialogFragment implements View.OnClickListener {

    private View view;

    private EditText fieldWeaponName;
    private EditText fieldWeaponPhysicDamage;
    private EditText fieldWeaponMagicDamage;
    private String weaponName = " ";
    private String weaponPhysicDamage = "0";
    private String weaponMagicDamage = "0";
    private Spinner weaponQuality;
    private Spinner weaponType;

    private int selectedIndexType;
    private int selectedIndexQuality;

    private Game game;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setTitle(R.string.toolbar_battlelog_title);
        view = inflater.inflate(R.layout.fragment_create_weapon, null);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        view.findViewById(R.id.btn_save).setOnClickListener(this);

        game = (Game) getArguments().getSerializable("Game");

        initUI();

        return view;
    }

    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;

            case R.id.btn_save:
                if (checkInput()) {
                    addWeapon();
                    Toast.makeText(view.getContext(), "Successful!", Toast.LENGTH_LONG).show();
                    dismiss();
                }
        }
    }


    private boolean checkInput() {

        weaponName = fieldWeaponName.getText().toString();
        weaponPhysicDamage = fieldWeaponPhysicDamage.getText().toString();
        weaponMagicDamage = fieldWeaponMagicDamage.getText().toString();

        if (weaponName == null ||
                fieldWeaponPhysicDamage == null ||
                fieldWeaponMagicDamage == null) {

            Toast.makeText(view.getContext(), "Please, fill all fields.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (weaponName.trim().equals("")) {
            Toast.makeText(view.getContext(), "Please, input correct name.", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void initUI() {
        fieldWeaponName = (EditText) view.findViewById(R.id.dialog_field_weapon_name);
        fieldWeaponPhysicDamage = (EditText) view.findViewById(R.id.dialog_field_weapon_physic_damage);
        fieldWeaponMagicDamage = (EditText) view.findViewById(R.id.dialog_field_weapon_magic_damage);

        weaponType = (Spinner) view.findViewById(R.id.fragment_spinner_type);
        weaponQuality = (Spinner) view.findViewById(R.id.fragment_spinner_quality);

        weaponType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getId() == R.id.fragment_spinner_type)
                    selectedIndexType = position;
                if (parent.getId() == R.id.fragment_spinner_quality)
                    selectedIndexQuality = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(view.getContext(), "Nothing Selected", Toast.LENGTH_SHORT).show();
            }
        });

        weaponQuality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getId() == R.id.fragment_spinner_quality)
                    selectedIndexType = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(view.getContext(), "Nothing Selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addWeapon() {
        Weapon newWeapon = new Weapon(
                weaponName,
                Weapon.qualityWeapon.valueOf(weaponQuality.getSelectedItem().toString()),
                Weapon.typeWeapon.valueOf(weaponType.getSelectedItem().toString()),
                Integer.parseInt(weaponPhysicDamage),
                Integer.parseInt(weaponMagicDamage)
        );

        if (game.addWeapon(newWeapon)) {
            XmlAssistant.serializeWeapon(newWeapon);
        }
    }
}