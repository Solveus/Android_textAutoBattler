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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import com.textautobattler.data.Game;
import com.textautobattler.data.GameCharacter;
import com.textautobattler.data.Weapon;

import java.util.ArrayList;

import static java.lang.String.valueOf;

public class MainActivity extends AppCompatActivity {
    // labels player one
    private TextView valPlayerNameOne;
    private TextView valPlayerHealthOne;
    private TextView valPlayerAttackOne;
    private TextView valPlayerMagicOne;
    private TextView valWeaponNameOne;
    private TextView valWeaponTypeOne;
    private TextView valWeaponQualityOne;
    private TextView valWeaponPhysicDamageOne;
    private TextView valWeaponMagicDamageOne;
    // labels player two
    private TextView valPlayerNameTwo;
    private TextView valPlayerHealthTwo;
    private TextView valPlayerAttackTwo;
    private TextView valPlayerMagicTwo;
    private TextView valWeaponNameTwo;
    private TextView valWeaponTypeTwo;
    private TextView valWeaponQualityTwo;
    private TextView valWeaponPhysicDamageTwo;
    private TextView valWeaponMagicDamageTwo;
    private ArrayAdapter playerAdapter;
    private ArrayAdapter weaponAdapter;
    // game variables
    private Game game;
    private int characterOneIndex = 0;
    private int characterTwoIndex = 0;
    private int weaponOneIndex = 0;
    private int weaponTwoIndex = 0;
    private ArrayList<String> playerNames;
    private ArrayList<String> weaponNames;
    private AboutFragment aboutFragment;

    private boolean switchTheme;
    private SharedPreferences settings;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getText(R.string.toolbar_main_title));

        changeTheme();

        // init game logics
        game = new Game(this);
        //  init ui
        initUI();
    }

    @Override
    protected void onResume() {
        Bundle arguments = getIntent().getExtras();

        if (arguments != null && arguments.containsKey(Game.class.getSimpleName()))
            game = (Game) arguments.getSerializable(Game.class.getSimpleName());

        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intentSettings = new Intent(this, SettingsActivity.class);
                startActivity(intentSettings);
                break;

            case R.id.action_about:
                new AboutFragment().show(getFragmentManager(), "aboutFragment");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeTheme() {
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        switchTheme = settings.getBoolean("theme", false);

        if (!switchTheme)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    public void buttonOnClick(View view) {
        Intent intent;

        switch (view.getId()) {

            case R.id.btn_start_battle:
                // init characters and push it to battle log activity
                setBattlers();

                intent = new Intent(this, BattleLogActivity.class);
                intent.putExtra(Game.class.getSimpleName(), game);
                startActivity(intent);
                break;

            case R.id.btn_edit_objects:
                intent = new Intent(this, EditGameObjects.class);
                intent.putExtra(Game.class.getSimpleName(), game);
                startActivity(intent);
        }
    }

    private void setBattlers() {
        // init character 1 and set weapon
        game.setGameCharacterOne((game.getGameCharacterList().get(characterOneIndex)));
        game.getGameCharacterOne().setWeapon(game.getWeaponList().get(weaponOneIndex));
        game.getGameCharacterOne().resurrection();

        // init character 2 and set weapon
        game.setGameCharacterTwo((game.getGameCharacterList().get(characterTwoIndex)));
        game.getGameCharacterTwo().setWeapon(game.getWeaponList().get(weaponTwoIndex));
        game.getGameCharacterTwo().resurrection();
    }


    private void initUI() {
        valPlayerNameOne = (TextView) findViewById(R.id.val_player_name_one);
        valPlayerHealthOne = (TextView) findViewById(R.id.val_player_health_one);
        valPlayerAttackOne = (TextView) findViewById(R.id.val_player_attack_one);
        valPlayerMagicOne = (TextView) findViewById(R.id.val_player_magic_one);

        valWeaponNameOne = (TextView) findViewById(R.id.val_weapon_name_one);
        valWeaponTypeOne = (TextView) findViewById(R.id.val_weapon_type_one);
        valWeaponQualityOne = (TextView) findViewById(R.id.val_weapon_quality_one);
        valWeaponPhysicDamageOne = (TextView) findViewById(R.id.val_weapon_physic_damage_one);
        valWeaponMagicDamageOne = (TextView) findViewById(R.id.val_weapon_magic_damage_one);

        valPlayerNameTwo = (TextView) findViewById(R.id.val_player_name_two);
        valPlayerHealthTwo = (TextView) findViewById(R.id.val_player_health_two);
        valPlayerAttackTwo = (TextView) findViewById(R.id.val_player_attack_two);
        valPlayerMagicTwo = (TextView) findViewById(R.id.val_player_magic_two);

        valWeaponNameTwo = (TextView) findViewById(R.id.val_weapon_name_two);
        valWeaponTypeTwo = (TextView) findViewById(R.id.val_weapon_type_two);
        valWeaponQualityTwo = (TextView) findViewById(R.id.val_weapon_quality_two);
        valWeaponPhysicDamageTwo = (TextView) findViewById(R.id.val_weapon_physic_damage_two);
        valWeaponMagicDamageTwo = (TextView) findViewById(R.id.val_weapon_magic_damage_two);

        // init names for spinner
        playerNames = game.getGameCharacterNamesList();
        weaponNames = game.getWeaponNamesList();

        Spinner[] players = {(Spinner) findViewById(R.id.choose_player_one), (Spinner) findViewById(R.id.choose_player_two)};
        Spinner[] weapons = {(Spinner) findViewById(R.id.choose_weapon_one), (Spinner) findViewById(R.id.choose_weapon_two)};

        playerAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, playerNames);
        playerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        weaponAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, weaponNames);
        weaponAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        for (Spinner player : players) {
            // set adapter for each player spinner
            player.setAdapter(playerAdapter);
            // set listener for each player spinner
            player.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (parent.getId()) {
                        case R.id.choose_player_one:
                            GameCharacter characterOne = game.getGameCharacterList().get(position);

                            valPlayerNameOne.setText(characterOne.getName());
                            valPlayerHealthOne.setText(valueOf(characterOne.getMaxHealth()));
                            valPlayerAttackOne.setText(valueOf(characterOne.getAttackPower()));
                            valPlayerMagicOne.setText(valueOf(characterOne.getMagicPower()));

                            characterOneIndex = position;
                            break;
                        case R.id.choose_player_two:
                            GameCharacter characterTwo = game.getGameCharacterList().get(position);

                            valPlayerNameTwo.setText(characterTwo.getName());
                            valPlayerHealthTwo.setText(valueOf(characterTwo.getMaxHealth()));
                            valPlayerAttackTwo.setText(valueOf(characterTwo.getAttackPower()));
                            valPlayerMagicTwo.setText(valueOf(characterTwo.getMagicPower()));

                            characterTwoIndex = position;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(MainActivity.this, "Nothing Selected", Toast.LENGTH_SHORT).show();
                }
            });
        }

        for (Spinner weapon : weapons) {
            // set adapter for each weapon spinner
            weapon.setAdapter(weaponAdapter);
            // set listener for each weapon spinner
            weapon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (parent.getId()) {
                        case R.id.choose_weapon_one:
                            Weapon weaponOne = game.getWeaponList().get(position);

                            valWeaponNameOne.setText(weaponOne.getName());
                            valWeaponQualityOne.setText(valueOf(weaponOne.getQuality()));
                            valWeaponTypeOne.setText(valueOf(weaponOne.getType()));
                            valWeaponPhysicDamageOne.setText(valueOf(weaponOne.getPhysicDamage()));
                            valWeaponMagicDamageOne.setText(valueOf(weaponOne.getMagicDamage()));
                            weaponOneIndex = position;
                            break;
                        case R.id.choose_weapon_two:
                            Weapon weaponTwo = game.getWeaponList().get(position);

                            valWeaponNameTwo.setText(weaponTwo.getName());
                            valWeaponQualityTwo.setText(valueOf(weaponTwo.getQuality()));
                            valWeaponTypeTwo.setText(valueOf(weaponTwo.getType()));
                            valWeaponPhysicDamageTwo.setText(valueOf(weaponTwo.getPhysicDamage()));
                            valWeaponMagicDamageTwo.setText(valueOf(weaponTwo.getMagicDamage()));
                            weaponTwoIndex = position;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    Toast.makeText(MainActivity.this, "Nothing Selected", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

