/*
 * Copyright (c) 2021.
 *
 * Author: Finogenov Vasily
 * A.K.A : Solveus, solveus_666
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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.textautobattler.data.Game;

import java.util.ArrayList;

public class DeleteWeaponFragment extends DialogFragment implements View.OnClickListener {

    private View view;
    private Game game;
    private int selectedIndex = 0;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().setTitle("Delete character");
        view = inflater.inflate(R.layout.fragment_delete_weapon, null);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        view.findViewById(R.id.btn_save).setOnClickListener(this);

        game = (Game) getArguments().getSerializable("Game");

        initUI();

        return view;
    }

    private void initUI() {


        ArrayList<String> weaponNames = game.getWeaponNamesList();
        Spinner weapons = (Spinner) view.findViewById(R.id.spinner_delete_weapon);

        ArrayAdapter<String> weaponAdapter = new ArrayAdapter<>(view.getContext(), R.layout.spinner_item, weaponNames);
        weaponAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // set adapter for each player spinner
        weapons.setAdapter(weaponAdapter);
        // set listener for each player spinner
        weapons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getId() == R.id.spinner_delete_weapon)
                    selectedIndex = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(view.getContext(), "Nothing Selected", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_cancel:
                dismiss();
                break;

            case R.id.btn_save:
                if (selectedIndex == 0)
                    Toast.makeText(view.getContext(), "Can't delete this weapon!", Toast.LENGTH_SHORT).show();
                else {
                    if (game.removeWeaponByIndex(selectedIndex)) {
                        Toast.makeText(view.getContext(), "Succesfull", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }
        }
    }
}




