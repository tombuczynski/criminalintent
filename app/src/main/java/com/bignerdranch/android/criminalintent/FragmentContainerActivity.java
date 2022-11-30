package com.bignerdranch.android.criminalintent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.bignerdranch.android.criminalintent.R;
import com.bignerdranch.android.criminalintent.ui.CrimeFragment;

public abstract class FragmentContainerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_container);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            addCrimeFragment(R.id.fragment_container, getFragmentClass());
        }
    }

    private void addCrimeFragment(int containerId, Class<? extends Fragment> fragmentClass) {
        FragmentManager fragMan = getSupportFragmentManager();

        fragMan.beginTransaction()
                .setReorderingAllowed(true)
                .add(containerId, fragmentClass, getArgs())
                .commit();
    }

    protected abstract Class<? extends Fragment> getFragmentClass();

    protected Bundle getArgs() {return null;}

    protected Fragment getFragment() {
        FragmentManager fragMan = getSupportFragmentManager();

        return fragMan.findFragmentById(R.id.fragment_container);
    }
}