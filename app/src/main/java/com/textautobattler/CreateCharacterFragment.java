/*
 * Copyright (c) 2021.
 *
 * Author: Finogenov Vasily
 * A.K.A :  Solveus, solveus_666
 *
 * Github: https://github.com/Solveus
 *
 */

package com.textautobattler;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.textautobattler.data.Game;
import com.textautobattler.data.GameCharacter;
import com.textautobattler.data.Weapon;
import com.textautobattler.data.XmlAssistant;

public class CreateCharacterFragment extends DialogFragment implements OnClickListener {

    private View view;

    private EditText fieldCharacterName;
    private EditText fieldCharacterHealth;
    private EditText fieldCharacterPhysicDamage;
    private EditText fieldCharacterMagicDamage;
    private String characterName = " ";
    private String characterHealth = "0";
    private String characterPhysicDamage = "0";
    private String characterMagicDamage = "0";

    private Game game;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setTitle(R.string.toolbar_battlelog_title);
        view = inflater.inflate(R.layout.fragment_create_character, null);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        view.findViewById(R.id.btn_save).setOnClickListener(this);

        game = (Game) getArguments().getSerializable("Game");

        initTextFields();

        return view;
    }


    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;

            case R.id.btn_save:
                if (checkInput()) {
                    addCharacter();
                    Toast.makeText(view.getContext(), "Successful!", Toast.LENGTH_LONG).show();
                    dismiss();
                }
        }
    }


    private boolean checkInput() {

        characterName = fieldCharacterName.getText().toString();
        characterHealth = fieldCharacterHealth.getText().toString();
        characterPhysicDamage = fieldCharacterPhysicDamage.getText().toString();
        characterMagicDamage = fieldCharacterMagicDamage.getText().toString();

        if (characterName == null ||
                fieldCharacterHealth == null ||
                fieldCharacterPhysicDamage == null ||
                fieldCharacterMagicDamage == null) {

            Toast.makeText(view.getContext(), "Please, fill all fields.", Toast.LENGTH_LONG).show();
            return false;
        }

//        if(characterName.matches("[\\p{L}| ]+")) {
//            Toast.makeText(view.getContext(), "Please, input name without symbols.", Toast.LENGTH_LONG).show();
//            return false;
//        }

        if (characterName.trim().equals("")) {
            Toast.makeText(view.getContext(), "Please, input correct name.", Toast.LENGTH_LONG).show();
            return false;
        }

        if (Integer.parseInt(characterHealth) == 0) {
            Toast.makeText(view.getContext(), "Health can't be 0!", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private void initTextFields() {
        fieldCharacterName = (EditText) view.findViewById(R.id.dialog_field_player_name);
        fieldCharacterHealth = (EditText) view.findViewById(R.id.dialog_field_player_health);
        fieldCharacterPhysicDamage = (EditText) view.findViewById(R.id.dialog_field_player_physic_damage);
        fieldCharacterMagicDamage = (EditText) view.findViewById(R.id.dialog_field_player_magic_damage);
    }

    private void addCharacter() {
        GameCharacter newCharacter = new GameCharacter(
                characterName,
                Integer.parseInt(characterHealth),
                Integer.parseInt(characterPhysicDamage),
                Integer.parseInt(characterMagicDamage),
                new Weapon());

        if (game.addCharacter(newCharacter)) {
            XmlAssistant.serializeCharacter(newCharacter);
        }
    }
}



