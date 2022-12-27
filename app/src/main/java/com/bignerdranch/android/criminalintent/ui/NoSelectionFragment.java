package com.bignerdranch.android.criminalintent.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.criminalintent.R;

import org.jetbrains.annotations.Contract;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoSelectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoSelectionFragment extends Fragment {

    public NoSelectionFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Contract(" -> new")
    public static NoSelectionFragment newInstance() {
        return new NoSelectionFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_no_selection, container, false);
    }
}