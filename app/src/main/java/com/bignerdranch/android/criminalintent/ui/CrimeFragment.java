package com.bignerdranch.android.criminalintent.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.criminalintent.data.Crime;
import com.bignerdranch.android.criminalintent.databinding.FragmentCrimeBinding;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class CrimeFragment extends Fragment {

    private static final String TAG = "CrimeFragment";
    private static final String ARG_CRIME_ID = "CrimeId";

    private CrimeViewModel mViewModel;
    private FragmentCrimeBinding b;
    private Crime mCrime;
    private DateFormat mDateFormattter;

    @NonNull
    public static CrimeFragment newInstance(UUID crime_id) {
        CrimeFragment f = new CrimeFragment();
        f.setArguments(prepareArgs(crime_id));
        return f;
    }

    @NonNull
    public static Bundle prepareArgs(UUID crime_id) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_CRIME_ID, crime_id);

        return bundle;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDateFormattter =  DateFormat.getDateInstance(java.text.DateFormat.LONG);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(CrimeViewModel.class);

        b = FragmentCrimeBinding.inflate(inflater, container, false);

//        Intent intent = requireActivity().getIntent();
//        UUID id = (UUID)intent.getSerializableExtra(CrimeActivity.EXTRA_CRIME_ID);

        UUID id = (UUID)requireArguments().getSerializable(ARG_CRIME_ID);
        mCrime = mViewModel.getCrime(id);

        b.setCrime(mCrime);
        b.setDateFormattter(mDateFormattter);

        //b.setLifecycleOwner(this);

        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configureDatePickerDialog();

        mCrime.isModified().observe(getViewLifecycleOwner(), modified -> {
            if (modified) {
                HashSet<UUID> modifiedItems = mViewModel.getModifiedItems().getValue();
                if (modifiedItems !=null) {
                    modifiedItems.add(mCrime.getId());
                    mViewModel.setModifiedItems(modifiedItems);
                }
            }
        });
    }

    private void configureDatePickerDialog() {
        //b.buttonDateTime.setEnabled(false);

            b.buttonDateTime.setOnClickListener(v -> {

            DatePickerDlgFragment f = DatePickerDlgFragment.newInstance(mCrime.getDate());
            f.show(getChildFragmentManager(), DatePickerDlgFragment.TAG);

        });

        getChildFragmentManager().setFragmentResultListener(DatePickerDlgFragment.REQUEST_DATE, this, (requestKey, result) -> {
            if (DatePickerDlgFragment.REQUEST_DATE.equals(requestKey)) {
                Date d = (Date) result.getSerializable(DatePickerDlgFragment.RESULT_DATE);

                b.buttonDateTime.setText(mDateFormattter.format(d));
                mCrime.setDate(d);
            }
        });

    }

}