package com.winlator.shortcutapi;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.winlator.container.ContainerManager;
import com.winlator.container.Shortcut;

import java.util.ArrayList;

public class ExternalShortcutsProvider extends ContentProvider {
    private static final String[] COLUMNS = new String[]{
        "_id",
        "shortcut_id",
        "shortcut_name",
        "container_id",
        "container_name",
        "shortcut_path",
        "launch_action",
        "launch_uri"
    };

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        if (!ExternalShortcutContract.AUTHORITY.equals(uri.getAuthority())) return null;
        if (!"/shortcuts".equals(uri.getPath())) return null;

        MatrixCursor cursor = new MatrixCursor(projection == null ? COLUMNS : projection);
        ContainerManager manager = new ContainerManager(getContext());
        ArrayList<Shortcut> shortcuts = manager.loadShortcuts();

        int index = 0;
        for (Shortcut shortcut : shortcuts) {
            String shortcutId = ExternalShortcutUtils.getShortcutId(shortcut);
            Uri launchUri = ExternalShortcutUtils.buildLaunchUri(shortcut);
            MatrixCursor.RowBuilder row = cursor.newRow();
            row.add(index++);
            row.add(shortcutId);
            row.add(shortcut.name);
            row.add(shortcut.container.id);
            row.add(shortcut.container.getName());
            row.add(shortcut.file.getPath());
            row.add(ExternalShortcutContract.ACTION_RUN_SHORTCUT);
            row.add(launchUri.toString());
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        if (ExternalShortcutContract.AUTHORITY.equals(uri.getAuthority()) && "/shortcuts".equals(uri.getPath())) {
            return "vnd.android.cursor.dir/vnd.com.winlator.shortcut";
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        throw new UnsupportedOperationException("Read only");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Read only");
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Read only");
    }
}
