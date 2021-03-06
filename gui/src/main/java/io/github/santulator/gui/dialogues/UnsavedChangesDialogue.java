/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

package io.github.santulator.gui.dialogues;

import io.github.santulator.gui.common.UnsavedResponse;
import io.github.santulator.gui.i18n.I18nKey;
import io.github.santulator.gui.i18n.I18nManager;
import io.github.santulator.session.FileNameTool;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

import static io.github.santulator.gui.i18n.I18nKey.*;
import static io.github.santulator.gui.view.CssTool.applyCss;

public class UnsavedChangesDialogue {
    private static final String ALERT_ID = "unsavedChanges";

    private final Path file;

    private final I18nManager i18nManager;

    private UnsavedResponse result;

    public UnsavedChangesDialogue(final Path file, final I18nManager i18nManager) {
        this.file = file;
        this.i18nManager = i18nManager;
    }

    public void showDialogue() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        String message = i18nManager.text(FILE_MODIFIED, filename());
        Map<ButtonType, UnsavedResponse> map = unsavedResponseMap();

        alert.setTitle(i18nManager.text(FILE_UNSAVED));
        alert.setHeaderText(message);
        alert.getButtonTypes().setAll(map.keySet());
        alert.getDialogPane().setId(ALERT_ID);
        applyCss(alert);

        result = alert.showAndWait()
            .map(map::get)
            .orElse(UnsavedResponse.CANCEL);
    }

    private String filename() {
        if (file == null) {
            return i18nManager.text(MAIN_WINDOW_UNTITLED);
        } else {
            return FileNameTool.filename(file);
        }
    }

    public UnsavedResponse getUserResponse() {
        return result;
    }

    private Map<ButtonType, UnsavedResponse> unsavedResponseMap() {
        Map<ButtonType, UnsavedResponse> map = new LinkedHashMap<>();

        addButton(map, FILE_SAVE, UnsavedResponse.SAVE);
        addButton(map, FILE_DISCARD, UnsavedResponse.DISCARD);
        addButton(map, FILE_CANCEL, UnsavedResponse.CANCEL);

        return map;
    }

    private void addButton(final Map<ButtonType, UnsavedResponse> map, final I18nKey key, final UnsavedResponse response) {
        map.put(new ButtonType(i18nManager.text(key)), response);
    }
}
