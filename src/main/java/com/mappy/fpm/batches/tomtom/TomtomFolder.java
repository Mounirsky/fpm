package com.mappy.fpm.batches.tomtom;

import javax.inject.Inject;
import javax.inject.Named;

public class TomtomFolder {
    private final String inputFolder;
    private final String zone;

    @Inject
    public TomtomFolder(
            @Named("com.mappy.fpm.tomtom.input") String inputFolder,
            @Named("com.mappy.fpm.tomtom.zone") String zone) {
        this.inputFolder = inputFolder;
        this.zone = zone;
    }

    public String getFile(String name) {
        if (name.startsWith("2d")) {
            return inputFolder + zone + "_" + name;
        }
        return inputFolder + zone + "___________" + name;
    }

    public String getTollsFile() {
        return inputFolder + "tolls.json";
    }
}