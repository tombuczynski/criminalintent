package com.bignerdranch.android.criminalintent;

import com.bignerdranch.android.criminalintent.ui.CrimeFragment;

import androidx.fragment.app.Fragment;

/**
 * Created by Tom Buczynski on 13.02.2022.
 */
public class CrimeActivity extends FragmentContainerActivity {
   @Override
   protected Class<? extends Fragment> getFragmentClass() {
      return CrimeFragment.class;
   }
}
