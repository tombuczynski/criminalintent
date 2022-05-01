package com.bignerdranch.android.criminalintent;

import androidx.appcompat.app.AppCompatActivity;
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

        if (savedInstanceState == null) {
            addCrimeFragment(R.id.fragment_container, getFragmentClass());
        }
    }

    private void addCrimeFragment(int containerId, Class<? extends Fragment> fragmentClass) {
        FragmentManager fragMan = getSupportFragmentManager();

        fragMan.beginTransaction()
                .setReorderingAllowed(true)
                .add(containerId, fragmentClass, null)
                .commit();
    }

    protected abstract Class<? extends Fragment> getFragmentClass();
}