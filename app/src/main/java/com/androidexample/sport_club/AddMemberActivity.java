package com.androidexample.sport_club;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidexample.sport_club.*;

import data.ClubOlympusContract;

public class AddMemberActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EDIT_MEMBER_LOADER = 111;
    Uri currentMemberUri;

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText sportEditText;
    private Spinner genderSpinner;
    private Spinner countMonthSpinner;
    private int gender = 0;
    private int month = 0;
    private ArrayAdapter spinnerAdapter;
    private ArrayAdapter spinnerAdapter1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);

        Intent intent = getIntent();

        currentMemberUri = intent.getData();

        if (currentMemberUri == null) {
            setTitle("Add a Member");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit the Member");
            getSupportLoaderManager().initLoader(EDIT_MEMBER_LOADER,
                    null, this);
        }

        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        sportEditText = findViewById(R.id.sportEditText);
        genderSpinner = findViewById(R.id.genderSpinner);
        countMonthSpinner = findViewById(R.id.countMonthSpinner);


        spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(spinnerAdapter);
// спиннер для кличества месяцев
        spinnerAdapter1 = ArrayAdapter.createFromResource(this,
                R.array.count_month, android.R.layout.simple_spinner_item);
        spinnerAdapter1.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        countMonthSpinner.setAdapter(spinnerAdapter);



        genderSpinner.setOnItemSelectedListener
                (new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        String selectedGender =
                                (String) parent.getItemAtPosition(position);
                        if (!TextUtils.isEmpty(selectedGender)) {
                            if (selectedGender.equals("Male")) {
                                gender = ClubOlympusContract.MemberEntry.GENDER_MALE;
                            } else if (selectedGender.equals("Female")) {
                                gender = ClubOlympusContract.MemberEntry.GENDER_FEMALE;
                            } else {
                                gender = ClubOlympusContract.MemberEntry.GENDER_UNKNOWN;
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        gender = 0;
                    }
                });
        countMonthSpinner.setOnItemSelectedListener
                (new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view,
                                               int position, long id) {
                        String selectedMonth =
                                (String) parent.getItemAtPosition(position);
                        if (!TextUtils.isEmpty(selectedMonth)) {
                            if (selectedMonth.equals("1 month")) {
                                month = ClubOlympusContract.SportCost.MONTH_ONE;
                            } else if (selectedMonth.equals("6 month")) {
                                month  = ClubOlympusContract.SportCost.MONTH_SIX;
                            } else if (selectedMonth.equals("1 year")) {
                                month  = ClubOlympusContract.SportCost.MONTH_YEAR;
                            } else {
                                month = ClubOlympusContract.SportCost.MONTH_UNKNOWN;
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        month = 0;
                    }
                });


    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        super.onPrepareOptionsMenu(menu);

        if (currentMemberUri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete_member);
            menuItem.setVisible(false);
        }

        return true;
    }


// для меню сохранения членов клуба
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_member_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_member:
                saveMember();
                return true;
            case R.id.delete_member:
                showDeleteMemberDialog();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveMember() {

        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String sport = sportEditText.getText().toString().trim();

        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(this,
                    "Input the first name",
                    Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(this,
                    "Input the last name",
                    Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(sport)) {
            Toast.makeText(this,
                    "Input the sport",
                    Toast.LENGTH_LONG).show();
            return;
        } else if (gender == ClubOlympusContract.MemberEntry.GENDER_UNKNOWN) {
            Toast.makeText(this,
                    "Choose the gender",
                    Toast.LENGTH_LONG).show();
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(ClubOlympusContract.MemberEntry.COLUMN_FIRST_NAME, firstName);
        contentValues.put(ClubOlympusContract.MemberEntry.COLUMN_LAST_NAME, lastName);
        contentValues.put(ClubOlympusContract.MemberEntry.COLUMN_SPORT, sport);
        contentValues.put(ClubOlympusContract.MemberEntry.COLUMN_GENDER, gender);

        if (currentMemberUri == null) {
            ContentResolver contentResolver = getContentResolver();
            Uri uri = contentResolver.insert(ClubOlympusContract.MemberEntry.CONTENT_URI,
                    contentValues);

            if (uri == null) {
                Toast.makeText(this,
                        "Insertion of data in the table failed",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Data saved", Toast.LENGTH_LONG).show();
            }
        } else {
            int rowsChanged = getContentResolver().update(currentMemberUri,
                    contentValues, null, null);

            if (rowsChanged == 0) {
                Toast.makeText(this,
                        "Saving of data in the table failed",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Member updated", Toast.LENGTH_LONG).show();
            }
        }


    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {

        String[] projection = {
                ClubOlympusContract.MemberEntry._ID,
                ClubOlympusContract.MemberEntry.COLUMN_FIRST_NAME,
                ClubOlympusContract.MemberEntry.COLUMN_LAST_NAME,
                ClubOlympusContract.MemberEntry.COLUMN_GENDER,
                ClubOlympusContract.MemberEntry.COLUMN_SPORT
        };

        return new CursorLoader(this,
                currentMemberUri,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int firstNameColumnIndex = cursor.getColumnIndex(
                    ClubOlympusContract.MemberEntry.COLUMN_FIRST_NAME
            );
            int lastNameColumnIndex = cursor.getColumnIndex(
                    ClubOlympusContract.MemberEntry.COLUMN_LAST_NAME
            );
            int genderColumnIndex = cursor.getColumnIndex(
                    ClubOlympusContract.MemberEntry.COLUMN_GENDER
            );
            int sportColumnIndex = cursor.getColumnIndex(
                    ClubOlympusContract.MemberEntry.COLUMN_SPORT
            );

            String firstName = cursor.getString(firstNameColumnIndex);
            String lastName = cursor.getString(lastNameColumnIndex);
            int gender = cursor.getInt(genderColumnIndex);
            String sport = cursor.getString(sportColumnIndex);

            firstNameEditText.setText(firstName);
            lastNameEditText.setText(lastName);
            sportEditText.setText(sport);

            switch (gender) {
                case ClubOlympusContract.MemberEntry.GENDER_MALE:
                    genderSpinner.setSelection(1);
                    break;
                case ClubOlympusContract.MemberEntry.GENDER_FEMALE:
                    genderSpinner.setSelection(2);
                    break;
                case ClubOlympusContract.MemberEntry.GENDER_UNKNOWN:
                    genderSpinner.setSelection(0);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    private void showDeleteMemberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want delete the member?");
        builder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMember();
                    }
                });
        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog != null) {
                            dialog.dismiss();
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteMember() {
        if (currentMemberUri != null) {
            int rowsDeleted = getContentResolver().delete(currentMemberUri,
                    null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this,
                        "Deleting of data from the table failed",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this,
                        "Member is deleted",
                        Toast.LENGTH_LONG).show();
            }

            finish();

        }
    }
}