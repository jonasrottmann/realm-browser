package de.jonasrottmann.realmbrowser.files;

import java.util.ArrayList;
import java.util.List;

public class RealmFileValidator {

    private static List<String> ignoreExtensionList = new ArrayList<>();

    static {
        ignoreExtensionList.add(".log");
        ignoreExtensionList.add(".log_a");
        ignoreExtensionList.add(".log_b");
        ignoreExtensionList.add(".lock");
        ignoreExtensionList.add(".management");
        ignoreExtensionList.add(".temp");
    }

    private RealmFileValidator() {
    }

    public static boolean isValidFileName(String fileName) {
        return fileName.lastIndexOf(".") > 0 && !ignoreExtensionList.contains(fileName.substring(fileName.lastIndexOf(".")));
    }
}
