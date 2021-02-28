/*
 * Copyright (c) 2021.
 *
 * Author: Finogenov Vasily
 * A.K.A : Solveus, solveus_666
 *
 * Github: https://github.com/Solveus
 *
 */

package com.textautobattler;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.textautobattler.data.Game;

public class EditGameObjects extends AppCompatActivity {
    private CreateCharacterFragment createCharacter;
    private DeleteCharacterFragment deleteCharacter;

    private CreateWeaponFragment createWeapon;
    private DeleteWeaponFragment deleteWeapon;

    Game game;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_game_objects);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        createCharacter = new CreateCharacterFragment();
        createWeapon = new CreateWeaponFragment();
        deleteCharacter = new DeleteCharacterFragment();
        deleteWeapon = new DeleteWeaponFragment();

        Bundle arguments = getIntent().getExtras();

        if (arguments != null) {
            game = (Game) arguments.getSerializable(Game.class.getSimpleName());
        }

        createCharacter.setArguments(arguments);
        deleteCharacter.setArguments(arguments);
        createWeapon.setArguments(arguments);
        deleteWeapon.setArguments(arguments);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // arrow back listener
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                this.finish();

                Intent back = new Intent(this, MainActivity.class);
                back.putExtra(Game.class.getSimpleName(), game);
                startActivity(back);

                break;

            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;

            case R.id.action_about:
                new AboutFragment().show(getFragmentManager(), "aboutFragment");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void buttonClick(View view) {

        switch (view.getId()) {
            case R.id.btn_create_weapon:
                createWeapon.show(getFragmentManager(), "createWeapon");
                break;

            case R.id.btn_create_character:
                createCharacter.show(getFragmentManager(), "createCharacter");
                break;

            case R.id.btn_delete_character:
                deleteCharacter.show(getFragmentManager(), "deleteCharacter");
                break;
            case R.id.btn_delete_weapon:
                deleteWeapon.show(getFragmentManager(), "deleteWeapon");
        }
    }
}