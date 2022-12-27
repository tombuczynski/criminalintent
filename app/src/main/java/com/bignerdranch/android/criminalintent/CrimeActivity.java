package com.bignerdranch.android.criminalintent;

import android.content.Intent;
import android.os.Bundle;

import com.bignerdranch.android.criminalintent.ui.CrimeFragment;
import com.bignerdranch.android.criminalintent.ui.CrimeViewModel;

import java.util.UUID;

import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

/**
 * Created by Tom Buczynski on 13.02.2022.
 */
public class CrimeActivity extends FragmentContainerActivity {
    public static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crimeid";
    public static final String EXTRA_MODIFIED_CRIMES_SET = "com.bignerdranch.android.criminalintent.crimeset";

    private CrimeViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(CrimeViewModel.class);

        mViewModel.getModifiedItems().observe(this, modifiedItems -> {
            if (modifiedItems.size() > 0) {
                Intent data = new Intent();
                data.putExtra(EXTRA_MODIFIED_CRIMES_SET, modifiedItems);
                setResult(RESULT_OK, data);
            }
        });
    }

    @Override
   protected Class<? extends Fragment> getFragmentClass() {
      return CrimeFragment.class;
   }

    @Override
    protected Bundle getArgs() {
        Intent intent = getIntent();
        UUID id = (UUID)intent.getSerializableExtra(EXTRA_CRIME_ID);

        return CrimeFragment.prepareArgs(id, true);
    }


//    @Override
//    public void onBackPressed() {
//        Intent data = new Intent();
//        data.putExtra(EXTRA_CRIME_SET, mViewModel.getModifiedItems().getValue());
//        setResult(RESULT_OK, data);
//
//        super.onBackPressed();
//    }
}
