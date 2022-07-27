package com.bignerdranch.android.criminalintent.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.criminalintent.CrimeActivity;
import com.bignerdranch.android.criminalintent.databinding.FragmentCrimeBinding;

import org.jetbrains.annotations.Contract;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.Observable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class CrimeFragment extends Fragment {

    private static final String TAG = "CrimeFragment";

    public static final String ARG_CRIME_ID = "crime_id";

    private CrimeViewModel mViewModel;
    private FragmentCrimeBinding b;

    @NonNull
    @Contract(" -> new")
    public static CrimeFragment newInstance() {
        return new CrimeFragment();
    }

    public static Bundle prepareArgs(UUID crime_id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_CRIME_ID, crime_id);

        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(CrimeViewModel.class);

        b = FragmentCrimeBinding.inflate(inflater, container, false);

//        Intent intent = requireActivity().getIntent();
//        UUID id = (UUID)intent.getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);

        UUID id = (UUID)requireArguments().getSerializable(ARG_CRIME_ID);
        b.setCrime(mViewModel.getCrime(id));

        //b.setLifecycleOwner(this);

        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        b.buttonDateTime.setEnabled(false);

        handleViewEvents();
    }

    private void handleViewEvents() {
        b.buttonDateTime.setOnClickListener(v -> {
//            Log.d(TAG, "title = " + mCrime.getTitle());
//            Log.d(TAG, "solved = " + mCrime.isSolved());
//
//            mCrime = new Crime("Klipa", true);
//            mBinding.setCrime(mCrime);

        });
    }

}