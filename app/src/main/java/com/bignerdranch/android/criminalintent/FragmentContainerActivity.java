package com.bignerdranch.android.criminalintent;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public abstract class FragmentContainerActivity extends AppCompatActivity {

    protected boolean mIsDualPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment_container);

        mIsDualPanel = findViewById(R.id.fragment_container_details) != null;

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            addFragment(R.id.fragment_container, getFragmentClass());
        }
    }

    private void addFragment(int containerId, Class<? extends Fragment> fragmentClass) {
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