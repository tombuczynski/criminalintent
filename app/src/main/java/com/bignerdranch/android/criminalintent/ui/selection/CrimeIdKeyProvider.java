package com.bignerdranch.android.criminalintent.ui.selection;

import com.bignerdranch.android.criminalintent.ui.CrimeViewModel;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemKeyProvider;

/**
 * Created by Tom Buczynski on 25.12.2022.
 */
public class CrimeIdKeyProvider extends ItemKeyProvider<String> {

    private final CrimeViewModel mViewModel;

    public CrimeIdKeyProvider(CrimeViewModel viewModel) {
        super(SCOPE_CACHED);
        mViewModel = viewModel;
    }

    @Nullable
    @Override
    public String getKey(int position) {
        return mViewModel.getCrime(position).getId().toString();
    }

    @Override
    public int getPosition(@NonNull String key) {
        UUID id = UUID.fromString(key);

        return mViewModel.getCrimeItemPos(id);
    }
}
