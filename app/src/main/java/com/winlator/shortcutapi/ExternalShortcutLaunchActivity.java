package com.winlator.shortcutapi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.winlator.XrActivity;
import com.winlator.XServerDisplayActivity;
import com.winlator.container.Container;
import com.winlator.container.ContainerManager;
import com.winlator.container.Shortcut;
import com.winlator.core.FileUtils;

import java.io.File;

public class ExternalShortcutLaunchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String path = intent.getStringExtra(ExternalShortcutContract.EXTRA_SHORTCUT_PATH);
        if (path == null && intent.getData() != null) path = FileUtils.getFilePathFromUri(intent.getData());

        if (path != null) {
            File file = new File(path);
            if (file.exists()) {
                ContainerManager manager = new ContainerManager(this);
                int containerId = 0;
                for (String line : FileUtils.readLines(file)) {
                    if (line.trim().startsWith("container_id=")) {
                        try {
                            containerId = Integer.parseInt(line.split("=")[1].trim());
                            break;
                        } catch (Exception e) {}
                    }
                }

                if (containerId > 0) {
                    com.winlator.container.Container container = manager.getContainerById(containerId);
                    if (container != null) {
                        Shortcut shortcut = new Shortcut(container, file);
                        launch(shortcut);
                        return;
                    }
                }
            }
        }

        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    private void launch(Shortcut shortcut) {
        if (!XrActivity.isSupported()) {
            Intent intent = new Intent(this, XServerDisplayActivity.class);
            intent.putExtra("container_id", shortcut.container.id);
            intent.putExtra("shortcut_path", shortcut.file.getPath());
            intent.putExtra("external_shortcut", true);
            startActivity(intent);
        } else {
            XrActivity.openIntent(this, shortcut.container.id, shortcut.file.getPath());
        }
        setResult(Activity.RESULT_OK);
        finish();
    }
}
