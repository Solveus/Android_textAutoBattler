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

public class DeleteCharacterFragment extends DialogFragment implements View.OnClickListener {

    private ArrayAdapter playerAdapter;
    private View view;
    private Game game;
    private int selectedCharacterIndex = 0;
    private ArrayList<String> playerNames;
    private Spinner players;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getDialog().setTitle("Delete character");
        view = inflater.inflate(R.layout.fragment_delete_character, null);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);
        view.findViewById(R.id.btn_save).setOnClickListener(this);

        game = (Game) getArguments().getSerializable("Game");

        initUI();

        return view;
    }

    private void initUI() {

        playerNames = game.getGameCharacterNamesList();
        players = (Spinner) view.findViewById(R.id.spinner_delete_character);

        playerAdapter = new ArrayAdapter<String>(view.getContext(), R.layout.spinner_item, playerNames);
        playerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // set adapter for each player spinner
        players.setAdapter(playerAdapter);
        // set listener for each player spinner
        players.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getId() == R.id.spinner_delete_character)
                    selectedCharacterIndex = position;
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
                if (selectedCharacterIndex == 0)
                    Toast.makeText(view.getContext(), "Can't delete this character!", Toast.LENGTH_SHORT).show();
                else {
                    if (game.removeCharacterByIndex(selectedCharacterIndex)) {
                        Toast.makeText(view.getContext(), "Succesfull", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }
                }
        }
    }
}




