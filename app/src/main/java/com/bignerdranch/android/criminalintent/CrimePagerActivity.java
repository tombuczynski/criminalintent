package com.bignerdranch.android.criminalintent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.bignerdranch.android.criminalintent.ui.CrimeFragmentAdapter;
import com.bignerdranch.android.criminalintent.ui.CrimeViewModel;

import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    public static final String EXTRA_CRIME_ID = "com.bignerdranch.android.criminalintent.crimeid";
    public static final String EXTRA_MODIFIED_CRIMES_SET = "com.bignerdranch.android.criminalintent.crimeset";
    public ImageButton mButtonArrowStart;
    public ImageButton mButtonArrowEnd;

    private CrimeViewModel mViewModel;
    private ViewPager2 mViewPagerCrimes;

    private class OnPageChangeCallback extends ViewPager2.OnPageChangeCallback {

        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);

            enableButtons(position);
        }
    }

    private final OnPageChangeCallback pagerCallback = new OnPageChangeCallback();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        mViewPagerCrimes = findViewById(R.id.viewPagerCrimes);

        mViewModel = new ViewModelProvider(this).get(CrimeViewModel.class);

        configureButtons();
        configureViewPager();


        mViewModel.getModifiedItems().observe(this, modifiedItems -> {
            if (modifiedItems.size() > 0) {
                Intent data = new Intent();
                data.putExtra(EXTRA_MODIFIED_CRIMES_SET, modifiedItems);
                setResult(RESULT_OK, data);
            }
        });

    }

    private void configureViewPager() {
        mViewPagerCrimes.setAdapter(new CrimeFragmentAdapter(this, mViewModel));

        Intent intent = getIntent();
        UUID id = (UUID) intent.getSerializableExtra(EXTRA_CRIME_ID);

        int itemPos = mViewModel.getCrimeItemPos(id);
        mViewPagerCrimes.setCurrentItem(itemPos, false);

        enableButtons(itemPos);
    }

    private void configureButtons() {
        mButtonArrowStart = findViewById(R.id.imageButtonStart);
        mButtonArrowEnd = findViewById(R.id.imageButtonEnd);

        mButtonArrowStart.setOnClickListener(v -> mViewPagerCrimes.setCurrentItem(0, false));
        mButtonArrowEnd.setOnClickListener(v -> mViewPagerCrimes.setCurrentItem(mViewModel.getCrimeList().size() - 1,false));
    }

    private void enableButtons(int itemPos) {
        mButtonArrowStart.setEnabled(itemPos > 0);
        mButtonArrowEnd.setEnabled(itemPos < mViewModel.getCrimeList().size() - 1);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mViewPagerCrimes.registerOnPageChangeCallback(pagerCallback);
    }

    @Override
    protected void onStop() {
        super.onStop();
        
        mViewPagerCrimes.unregisterOnPageChangeCallback(pagerCallback);
    }
}