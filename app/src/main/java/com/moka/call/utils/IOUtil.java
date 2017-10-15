package com.moka.call.utils;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by su on 17-06-10.
 */
public final class IOUtil {

    private static final String TAG = IOUtil.class.getSimpleName();

    private IOUtil() {
    }

    /**
     * Close closable object and wrap {@link IOException} with {@link RuntimeException}
     * @param closeable closeable object
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                Log.w(TAG, e);
            }
        }
    }

    /**
     * Close closable and hide possible {@link IOException}
     * @param closeable closeable object
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                // Ignored
            }
        }
    }

    public static void writeObject(Serializable s, String filePath) {
        writeObject(s, new File(filePath));
    }

    public static void writeObject(Serializable s, File file) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(s);
            oos.close();
        } catch (IOException e) {
            Log.e(TAG, "writeObject", e);
        } finally {
            close(oos);
            close(fos);
        }
    }

    public static Object readObject(File file) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (IOException e) {
            Log.e(TAG, "file: " + file, e);
        } catch (ClassNotFoundException e) {
            Log.e(TAG, "file: " + file, e);
        } finally {
            close(ois);
            close(fis);
        }
        return null;
    }

    public static Object readObject(String filepath) {
        File file = new File(filepath);
        if (file.exists() && file.isFile()) {
            return readObject(file);
        } else {
            Log.e(TAG, "wrong filepath: " + filepath);
        }
        return null;
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean makeDirIfNotExist(String path) {
        File rootDirFile = new File(path);
        if (!rootDirFile.exists()) {
            return rootDirFile.mkdir();
        } else {
            if (!rootDirFile.isDirectory()) {
                if (rootDirFile.delete()) {
                    return rootDirFile.mkdir();
                }
            } else {
                return true;
            }
        }
        return false;
    }

    public static File createFileIfNotExist(String filepath) {
        File file = new File(filepath);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            return file;
        } catch (IOException e) {
            Log.e(TAG, "filepath: " + filepath, e);
        }
        return null;
    }

    public static void deleteFiles(File file) {
        Log.d(TAG, "file: " + file.getAbsoluteFile());
        if (file.isFile()) {
            file.delete();
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    deleteFiles(f);
                }
            }
            file.delete();
        }
    }

    public static void save(String dir, String filename, String result) {
        File file = new File(dir);
        if (!file.exists() || !file.isDirectory()) {
            Log.e(TAG, "file: " + file);
            return;
        }
        byte[] bt = result.getBytes();
        FileOutputStream in = null;
        try {
            in = new FileOutputStream(new File(file, filename));
            try {
                in.write(bt, 0, bt.length);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @NonNull
    public static String streamToString(@NonNull InputStream input) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(input), 8192);
        try {
            String line;
            final List<String> buffer = new LinkedList<String>();
            while ((line = reader.readLine()) != null) {
                buffer.add(line);
            }
            return TextUtils.join("\n", buffer);
        } finally {
            closeQuietly(reader);
        }
    }
}
