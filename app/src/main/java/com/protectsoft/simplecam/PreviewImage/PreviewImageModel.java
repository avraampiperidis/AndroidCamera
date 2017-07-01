package com.protectsoft.simplecam.PreviewImage;

import java.io.File;
import java.util.ArrayList;

/**
 */
public class PreviewImageModel {

    private File file;

    public PreviewImageModel(File file) {
        this.file = file;
    }

    public static ArrayList<PreviewImageModel> fromFiles(ArrayList<File> files) {
        ArrayList<PreviewImageModel> models = new ArrayList<PreviewImageModel>();

        for(int i =0; i < files.size(); i++) {
            if(files.get(i).exists()) {
                models.add(new PreviewImageModel(files.get(i)));
            }
        }

        return models;
    }

    public File getFile() {
        return file;
    }


}
