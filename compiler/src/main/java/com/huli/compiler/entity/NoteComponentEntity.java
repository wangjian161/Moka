package com.huli.compiler.entity;

/**
 * Created by su on 17-5-27.
 */

public class NoteComponentEntity {
    private String description;
    private String className;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "NoteComponentEntity{" +
                "description='" + description + '\'' +
                ", className='" + className + '\'' +
                '}';
    }
}
