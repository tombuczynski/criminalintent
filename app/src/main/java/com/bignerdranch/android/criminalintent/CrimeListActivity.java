package com.bignerdranch.android.criminalintent;

import com.bignerdranch.android.criminalintent.ui.CrimeListFragment;

import androidx.fragment.app.Fragment;

/**
 * Created by Tom Buczynski on 20.02.2022.
 */
public class CrimeListActivity extends FragmentContainerActivity {

   @Override
   protected Class<? extends Fragment> getFragmentClass() {
      return CrimeListFragment.class;
   }
}
