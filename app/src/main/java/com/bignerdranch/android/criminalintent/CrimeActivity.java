package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;

import com.bignerdranch.android.criminalintent.ui.CrimeFragment;

import java.util.UUID;

import androidx.fragment.app.Fragment;

/**
 * Created by Tom Buczynski on 13.02.2022.
 */
public class CrimeActivity extends FragmentContainerActivity {
    public static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crimeid";

    @Override
   protected Class<? extends Fragment> getFragmentClass() {
      return CrimeFragment.class;
   }

    @Override
    protected Bundle getArgs() {
        Intent intent = getIntent();
        UUID id = (UUID)intent.getSerializableExtra(EXTRA_CRIME_ID);

        return CrimeFragment.prepareArgs(id);
    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);

        super.onBackPressed();
    }
}
