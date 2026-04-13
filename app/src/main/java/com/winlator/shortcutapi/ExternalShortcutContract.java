package com.winlator.shortcutapi;

import android.net.Uri;

import com.winlator.BuildConfig;

public final class ExternalShortcutContract {
    public static final String ACTION_RUN_SHORTCUT = "com.winlator.action.RUN_SHORTCUT";
    public static final String EXTRA_SHORTCUT_ID = "shortcut_id";
    public static final String EXTRA_CONTAINER_ID = "container_id";
    public static final String EXTRA_SHORTCUT_PATH = "shortcut_path";

    public static final String AUTHORITY = BuildConfig.APPLICATION_ID + ".shortcuts";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/shortcuts");

    public static final String URI_SCHEME = "winlator";
    public static final String URI_HOST = "shortcut";

    private ExternalShortcutContract() {}
}
