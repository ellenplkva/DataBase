package com.androidexample.sport_club;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.androidexample.sport_club.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import data.ClubOlympusContract;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int MEMBER_LOADER = 123;
    MemberCursorAdapter memberCursorAdapter;

    ListView dataListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataListView = findViewById(R.id.dataListView);
    // прописываем для кнопки плюсик переход
        FloatingActionButton floatingActionButton =
                findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,
                        AddMemberActivity.class);
                startActivity(intent);
            }
        });
         // что-то со страницей вывода бд
        memberCursorAdapter = new MemberCursorAdapter(this,
                null, false);
        dataListView.setAdapter(memberCursorAdapter);

        dataListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(MainActivity.this,
                        AddMemberActivity.class);
                Uri currentMemberUri = ContentUris
                        .withAppendedId(ClubOlympusContract.MemberEntry.CONTENT_URI, id);
                intent.setData(currentMemberUri);
                startActivity(intent);

            }
        });

        getSupportLoaderManager().initLoader(MEMBER_LOADER,
                null, this);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {

        String[] projection = {
                ClubOlympusContract.MemberEntry._ID,
                ClubOlympusContract.MemberEntry.COLUMN_FIRST_NAME,
                ClubOlympusContract.MemberEntry.COLUMN_LAST_NAME,
                ClubOlympusContract.MemberEntry.COLUMN_SPORT
        };

        CursorLoader cursorLoader = new CursorLoader(this,
                ClubOlympusContract.MemberEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );

        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

        memberCursorAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

        memberCursorAdapter.swapCursor(null);

    }

    // меню удалить всех
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delit_table_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                showDeleteMemberDialog();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}