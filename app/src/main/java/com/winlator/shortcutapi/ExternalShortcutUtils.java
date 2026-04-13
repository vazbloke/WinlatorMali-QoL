package com.winlator.shortcutapi;

import android.net.Uri;

import com.winlator.container.Container;
import com.winlator.container.ContainerManager;
import com.winlator.container.Shortcut;

import java.io.File;
import java.io.IOException;

public final class ExternalShortcutUtils {
    private ExternalShortcutUtils() {}

    public static String getShortcutId(Shortcut shortcut) {
        return shortcut.container.id + ":" + shortcut.file.getName();
    }

    public static Uri buildLaunchUri(Shortcut shortcut) {
        return new Uri.Builder()
            .scheme(ExternalShortcutContract.URI_SCHEME)
            .authority(ExternalShortcutContract.URI_HOST)
            .appendPath(String.valueOf(shortcut.container.id))
            .appendPath(shortcut.file.getName())
            .build();
    }

}
