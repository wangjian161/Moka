package com.moka.call.net;

import android.support.annotation.NonNull;

import java.io.File;

/**
 * Created by susu on 2017/6/10.
 */

public class MultipartInfo {
    private MediaType mediaType;
    private String name; //key
    private String filename;
    private File file;

    public MultipartInfo(MediaType mediaType, String name, String filename, @NonNull File file) {
        this.mediaType = mediaType;
        this.name = name;
        this.filename = filename;
        this.file = file;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "MultipartInfo{" +
                "mediaType=" + mediaType +
                ", name='" + name + '\'' +
                ", filename='" + filename + '\'' +
                ", file=" + file +
                '}';
    }

    public enum MediaType {
        MIXED,
        ALTERNATIVE,
        DIGEST,
        PARALLEL,
        FORM
    }
}
