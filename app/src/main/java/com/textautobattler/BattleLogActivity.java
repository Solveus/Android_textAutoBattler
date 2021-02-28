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
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.textautobattler.data.Game;

public class BattleLogActivity extends AppCompatActivity {

    private static final String nameVariableKey = "NAME_VARIABLE";
    private static final String textViewTexKey = "TEXT_EDIT_TEXT";

    private SharedPreferences settings;
    private Bundle arguments;
    private Game game;

    private boolean btnSkip = false;
    private boolean settingsSkip = false;

    private EditText logWindow;
    private String saveLog = "";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battle_log);

        // arrow back
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        arguments = getIntent().getExtras();

        if (arguments != null)
            game = (Game) arguments.getSerializable(Game.class.getSimpleName());

        // preferences
        settings = PreferenceManager.getDefaultSharedPreferences(this);

        // set skip log from settings
        settingsSkip = settings.getBoolean("skipLog", false);

        initUI();
        showBattleLog();
    }

    @Override
    protected void onResume() {

        // set text size from settings
        String sizeVal = settings.getString("font", null);
        if (sizeVal != null)
            logWindow.setTextSize(Integer.parseInt(sizeVal));

        // set skip log from settings
        settingsSkip = settings.getBoolean("skipLog", false);

        super.onResume();
    }

    // arrow back listener
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // save state
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString(nameVariableKey, saveLog);
        logWindow = (EditText) findViewById(R.id.textAreaLog);
        outState.putString(textViewTexKey, logWindow.getText().toString());

        super.onSaveInstanceState(outState);
    }

    // get older save state
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        saveLog = savedInstanceState.getString(nameVariableKey);
        String textViewText = savedInstanceState.getString(textViewTexKey);
        logWindow = (EditText) findViewById(R.id.textAreaLog);
        logWindow.append(saveLog);
    }

    private void initUI() {

        logWindow = (EditText) findViewById(R.id.textAreaLog);
        logWindow.setText(" ");
        logWindow.setFocusable(false);
        logWindow.setFocusableInTouchMode(false);
        logWindow.setEnabled(false);
        logWindow.setBackgroundColor(Color.TRANSPARENT);
    }

    private void showBattleLog() {
        // set characters 1, 2 and editText for print battle log

        // random first attacker
        int randomValue = (int) (Math.random() * 2); // 0 or 1

        // swap first and second characters
        if (randomValue == 1)
            game.battle(game.getGameCharacterOne(), game.getGameCharacterTwo(), logWindow, settingsSkip);
        else
            game.battle(game.getGameCharacterTwo(), game.getGameCharacterOne(), logWindow, settingsSkip);
    }

    public void buttonClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btn_clear_log:
                logWindow.setText(" ");
                break;
            case R.id.btn_skip_log:
                if (!game.getBtnSkipLog())
                    game.setBtnSkipLog(true);
        }

    }
}


