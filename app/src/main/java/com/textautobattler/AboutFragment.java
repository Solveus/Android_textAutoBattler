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


public class AboutFragment extends DialogFragment {

    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_about, null);
        return view;
    }

}




