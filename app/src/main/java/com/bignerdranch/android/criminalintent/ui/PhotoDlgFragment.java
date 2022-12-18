package com.bignerdranch.android.criminalintent.ui;

import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.bignerdranch.android.criminalintent.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.signature.ObjectKey;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by Tom Buczynski on 17.12.2022.
 */
public class PhotoDlgFragment extends DialogFragment {
    public static final String TAG = "PhotoDlgFragment";

    private static final String ARG_FILE = "file";
    private static final String ARG_FULLSCREEN = "fullscreen";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_photo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        File photoFile = (File)requireArguments().getSerializable(ARG_FILE);
        boolean fullscreen = requireArguments().getBoolean(ARG_FULLSCREEN);

        Long key = photoFile.lastModified();

        ImageView imageView = view.findViewById(R.id.imageViewPhoto);
        imageView.setOnClickListener(v -> {
            FragmentManager fragMan = requireActivity().getSupportFragmentManager();
            fragMan.popBackStack(TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            dismiss();
        });

        DisplayMetrics dm = getResources().getDisplayMetrics();

        if (fullscreen) {
            Glide.with(this)
                    .load(photoFile)
                    .signature(new ObjectKey(key))
                    .into(imageView);
        } else {
            Glide.with(this)
                    .load(photoFile)
                    .signature(new ObjectKey(key))
                    .override(Math.min(dm.widthPixels, dm.heightPixels))
                    .into(imageView);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dlg = super.onCreateDialog(savedInstanceState);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dlg;
    }


    @NonNull
    public static PhotoDlgFragment newInstance(File photoFile) {
        PhotoDlgFragment f = new PhotoDlgFragment();
        f.setArguments(prepareArgs(photoFile, false));

        return f;
    }

    public static void show(@NonNull FragmentActivity activity, File photoFile, boolean fullscreen) {
        FragmentManager fragMan = activity.getSupportFragmentManager();

        if (fullscreen) {
            fragMan.beginTransaction()
                    .setReorderingAllowed(true)
                    .add(android.R.id.content, PhotoDlgFragment.class, prepareArgs(photoFile, true))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(TAG)
                    .commit();
        } else {
            PhotoDlgFragment f = PhotoDlgFragment.newInstance(photoFile);
            f.show(fragMan, PhotoDlgFragment.TAG);
        }
    }

    @NonNull
    public static Bundle prepareArgs(File photoFile, boolean fullscreen) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_FILE, photoFile);
        bundle.putBoolean(ARG_FULLSCREEN, fullscreen);

        return bundle;
    }
}
