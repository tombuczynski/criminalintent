package com.bignerdranch.android.criminalintent.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bignerdranch.android.criminalintent.R;
import com.bignerdranch.android.criminalintent.data.Crime;
import com.bignerdranch.android.criminalintent.databinding.FragmentCrimeBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ShareCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import static androidx.activity.result.contract.ActivityResultContracts.*;

public class CrimeFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "CrimeFragment";
    private static final String ARG_CRIME_ID = "CrimeId";
    private static final String ARGS_CONTACT_QUERY = "ContactQuery";
    private static final int ID_QUERY_PICKED_CONTACT_DETAILS = 1;
    private static final int ID_QUERY_PHONE_NUMBER = 2;

    private CrimeViewModel mViewModel;
    private FragmentCrimeBinding b;
    private Crime mCrime;
    private DateFormat mDateFormattter;

    private Cursor mPickedContactCursor;
    private Cursor mPhoneCursor;

    private final ActivityResultLauncher<Void> mPickContact = registerForActivityResult(new PickContact(), result -> {
        if (result != null) {
            initContactQuery(mPickedContactCursor, ID_QUERY_PICKED_CONTACT_DETAILS, result.toString());
        }
    });

    private final ActivityResultLauncher<String> mRequestPermission = registerForActivityResult(new RequestPermission(), result -> {
        if (result) {
            retrieveSuspectPhoneNumber();
        } else {
            Snackbar.make(b.buttonCallSuspect, R.string.no_read_contact_permission, BaseTransientBottomBar.LENGTH_SHORT).show();
        }
    });

    private final ActivityResultLauncher<Uri> mTakePhoto = registerForActivityResult(new TakePicture(), result -> {
        if (result) {
            updatePhotoView();
        }
    });

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

        mDateFormattter = DateFormat.getDateInstance(java.text.DateFormat.LONG);

        setHasOptionsMenu(true);

        mPhoneCursor = null;
        mPickedContactCursor = null;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViewModel = new ViewModelProvider(requireActivity()).get(CrimeViewModel.class);

        b = FragmentCrimeBinding.inflate(inflater, container, false);

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
        configureButtons();
        configurePhotoDialog();

        updatePhotoView();

        mCrime.isModified().observe(getViewLifecycleOwner(), modified -> {
            if (modified) {
                mViewModel.addModifiedItem(mCrime.getId(), CrimeViewModel.ItemModifyKind.CHANGED);
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_crime, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int menuItemId = item.getItemId();

        if (menuItemId == R.id.menu_remove_crime) {
            mViewModel.addModifiedItem(mCrime.getId(), CrimeViewModel.ItemModifyKind.REMOVED);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] strArgs = Objects.requireNonNull(args).getStringArray(ARGS_CONTACT_QUERY);

        if (id == ID_QUERY_PHONE_NUMBER) {
            String name =  strArgs[0];

            String[] projection = {Phone.NUMBER, Phone.IS_SUPER_PRIMARY, Data.DISPLAY_NAME_PRIMARY, Data.HAS_PHONE_NUMBER};
            return new CursorLoader(requireContext(), Data.CONTENT_URI, projection,
                    Data.DISPLAY_NAME_PRIMARY + " = ? AND " + Data.MIMETYPE + " = '" + Phone.CONTENT_ITEM_TYPE + "'",
                    new String[] {name}, null);

        } else {
            String uriString =  strArgs[0];
            Uri uri = Uri.parse(uriString);

            String[] projection = {Contacts.LOOKUP_KEY, Contacts.DISPLAY_NAME_PRIMARY, Contacts.HAS_PHONE_NUMBER};
            return new CursorLoader(requireContext(), uri, projection,
                    null,
                    null, null);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == ID_QUERY_PICKED_CONTACT_DETAILS) {
            mPickedContactCursor = data;

            if (data.getCount() == 1) {
                data.moveToFirst();

                @SuppressLint("Range")
                String name = data.getString(data.getColumnIndex(Contacts.DISPLAY_NAME_PRIMARY));

                b.buttonChooseSuspect.setText(name);
                mCrime.setSuspect(name);

                updateButtonCallSuspect();
            }

        } else if (loader.getId() == ID_QUERY_PHONE_NUMBER) {
            mPhoneCursor = data;

            retrieveSuspectPhoneNumber();

            LoaderManager.getInstance(this).destroyLoader(ID_QUERY_PHONE_NUMBER);
            mPhoneCursor = null;

        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        if (loader.getId() == ID_QUERY_PHONE_NUMBER) {
            mPhoneCursor = null;
        } else if (loader.getId() == ID_QUERY_PICKED_CONTACT_DETAILS) {
            mPickedContactCursor = null;
        }
    }

    private void updatePhotoView() {
        File photoFile = mViewModel.getPhotoFile(requireContext(), mCrime);
        if (photoFile.exists()) {
            Long key = photoFile.lastModified();

            Glide.with(this)
                    .load(photoFile)
                    .signature(new ObjectKey(key))
                    .into(b.imageViewPhoto);
        } else {
            b.imageViewPhoto.setImageDrawable(null);
        }

    }

    private void initContactQuery(Cursor data, int queryId, String... args) {
        LoaderManager man = LoaderManager.getInstance(this);

        if (data != null) {
            man.destroyLoader(queryId);
        }

        Bundle bundle = new Bundle();
        bundle.putStringArray(ARGS_CONTACT_QUERY, args);
        man.initLoader(queryId, bundle, this);
    }

    private Uri getPhotoUri() {
        File photoFile =  mViewModel.getPhotoFile(requireContext(), mCrime);

        return FileProvider.getUriForFile(requireContext(), "com.bignerdranch.android.criminalintent.fileprovider", photoFile);
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

    private void configurePhotoDialog() {
        b.imageViewPhoto.setOnClickListener(v -> {

            File photoFile = mViewModel.getPhotoFile(requireContext(), mCrime);
            if (photoFile.exists()) {
                PhotoDlgFragment.show(requireActivity(), photoFile, true);
            }

        });
    }

    private void configureButtons() {
        b.buttonSendReport.setOnClickListener(v -> sendReport(createReport()));

        if (isContactAppPresent()) {
            b.buttonChooseSuspect.setOnClickListener(v -> mPickContact.launch(null));
        } else {
            b.buttonChooseSuspect.setEnabled(false);
        }

        if (isPhoneAppPresent()) {
            b.buttonCallSuspect.setOnClickListener(v -> retrieveSuspectPhoneNumber());
        } else {
            b.buttonCallSuspect.setEnabled(false);
        }

        if (isCameraAppPresent()) {
            b.buttonTakePhoto.setOnClickListener(v -> mTakePhoto.launch(getPhotoUri()));
        } else {
            b.buttonTakePhoto.setEnabled(false);
        }

        String suspect = mCrime.getSuspect();
        if (suspect != null) {
            b.buttonChooseSuspect.setText(suspect);
        }

        updateButtonCallSuspect();
    }

    private void updateButtonCallSuspect() {
        b.buttonCallSuspect.setVisibility(mCrime.getSuspect() != null ? View.VISIBLE : View.GONE);
    }


    @SuppressLint("Range")
    private void retrieveSuspectPhoneNumber() {
        String suspect = mCrime.getSuspect();

        if (suspect != null) {
            Cursor data = mPhoneCursor != null ? mPhoneCursor : mPickedContactCursor;

            if (data != null) {
                boolean hasPhoneNumber = false;

                if (data.getCount() > 0) {
                    data.moveToFirst();
                    hasPhoneNumber = data.getInt(data.getColumnIndex(Contacts.HAS_PHONE_NUMBER)) != 0;
                }

                if (! hasPhoneNumber) {
                    Toast.makeText(requireContext(), R.string.suspect_has_no_phone_number, Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (mPhoneCursor == null) {

                if (isPermissionGranted(Manifest.permission.READ_CONTACTS)) {
                    initContactQuery(mPhoneCursor, ID_QUERY_PHONE_NUMBER, suspect);
                } else {
                    requestPermission(Manifest.permission.READ_CONTACTS, R.string.read_contact_permission_explain);
                }
            } else {
                String defaultNumber = null;

                do {
                    String number = data.getString(data.getColumnIndex(Phone.NUMBER));
                    boolean isDefault = data.getInt(data.getColumnIndex(Phone.IS_SUPER_PRIMARY)) != 0;

                    if (defaultNumber == null || isDefault) {
                        defaultNumber = number;
                    }

                    Log.d(TAG, "Phone: " + number + ", default:" + isDefault);
                } while (data.moveToNext());

                Log.d(TAG, "Default Phone: " + defaultNumber);

                dialPhoneNumber(defaultNumber);
            }
        }
    }

    private boolean isPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(String permission, int explainTextResId) {
        if (shouldShowRequestPermissionRationale(permission)) {
            Snackbar.make(b.buttonCallSuspect, explainTextResId, BaseTransientBottomBar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, v -> mRequestPermission.launch(permission))
                    .show();
        } else {
            mRequestPermission.launch(permission);
        }
    }

    private void dialPhoneNumber(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    private void sendReport(String reportText) {
//        Intent intent = new Intent(Intent.ACTION_SEND);
//
//        intent.setType("text/plain");
//        intent.putExtra(Intent.EXTRA_TEXT, reportText);
//        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.report_title));
//
//        Intent chooser = Intent.createChooser(intent, getString(R.string.send_report_title));
//
//        startActivity(chooser);

        new ShareCompat.IntentBuilder(requireContext())
                .setType("text/plain")
                .setText(reportText)
                .setChooserTitle(R.string.send_report_title)
                .startChooser();
    }

    @NonNull
    private String createReport() {
        String solved = getString(mCrime.isSolved() ? R.string.solved : R.string.not_solved);
        String date = mDateFormattter.format(mCrime.getDate());
        String suspect = mCrime.getSuspect();
        suspect = suspect != null ? suspect : getString(R.string.lack);

        return getString(R.string.report_text, mCrime.getTitle(), date, solved, suspect);
    }

    private boolean isContactAppPresent() {
        PackageManager pm = requireContext().getPackageManager();

        ResolveInfo info = pm.resolveActivity(new Intent(Intent.ACTION_PICK).setType(Contacts.CONTENT_TYPE), PackageManager.MATCH_DEFAULT_ONLY);

        return info != null;
    }

    private boolean isPhoneAppPresent() {
        PackageManager pm = requireContext().getPackageManager();

        ResolveInfo info = pm.resolveActivity(new Intent(Intent.ACTION_DIAL), PackageManager.MATCH_DEFAULT_ONLY);

        return info != null;
    }

    private boolean isCameraAppPresent() {
        PackageManager pm = requireContext().getPackageManager();

        ResolveInfo info = pm.resolveActivity(new Intent(MediaStore.ACTION_IMAGE_CAPTURE), PackageManager.MATCH_DEFAULT_ONLY);

        return info != null;
    }
}