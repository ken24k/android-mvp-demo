package com.ken24k.android.mvpdemo.common.utils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Environment;
import android.util.Base64;

import com.ken24k.android.mvpdemo.app.MyApplication;
import com.ken24k.android.mvpdemo.common.Constants;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * 文件相关工具
 * Created by wangming on 2020-05-28
 */

public class FileUtils {

    public class FileType {
        public static final String JPG = ".jpg";
        public static final String APK = ".apk";
    }

    public static class FilePath {
        private static final String ROOT_PATH = MyApplication.getInstance().getFilesDir().getAbsolutePath() + "/";
        public static final String JPG_PATH = ROOT_PATH + "IMAGES/";// 图片地址
    }

    public static class ExternalFilePath {
        private static final String ROOT_PATH = getRootPath();
        private static final String LOG_PATH = ROOT_PATH + "LOGS/";// 日志目录
        public static final String DOWNLOAD_PATH = ROOT_PATH + "DOWNLOADS/";// 下载目录
        public static final String APP_APK_PATH = DOWNLOAD_PATH + "app" + FileType.APK;// 本地apk包地址
    }

    /**
     * 根目录
     */
    public static String getRootPath() {
        // 有sd卡
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Constants.ROOT_NAME + "/";
        }
        // 没有sd卡
        else {
            return Environment.getDataDirectory().getAbsolutePath() + "/" + Constants.ROOT_NAME + "/";
        }
    }

    /**
     * 解压zip到指定的路径
     */
    public static void unZipFolder(String zipPath, String outPath) {
        if (!new File(zipPath).exists()) {
            return;
        }
        FileUtils.deleteFile(outPath);
        File checkFile = new File(outPath);
        if (!checkFile.exists()) {
            checkFile.getParentFile().mkdirs();
        }
        ZipInputStream inZip = null;
        FileOutputStream out = null;
        try {
            inZip = new ZipInputStream(new FileInputStream(zipPath));
            ZipEntry zipEntry;
            String szName;
            while ((zipEntry = inZip.getNextEntry()) != null) {
                szName = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    // 获取部件的文件夹名
                    szName = szName.substring(0, szName.length() - 1);
                    File folder = new File(outPath + File.separator + szName);
                    folder.mkdirs();
                } else {
                    File file = new File(outPath + File.separator + szName);
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();
                        file.createNewFile();
                    }
                    // 获取文件的输出流
                    out = new FileOutputStream(file);
                    int len;
                    byte[] buffer = new byte[1024];
                    // 读取（字节）字节到缓冲区
                    while ((len = inZip.read(buffer)) != -1) {
                        // 从缓冲区（0）位置写入（字节）字节
                        out.write(buffer, 0, len);
                        out.flush();
                    }
                    out.close();
                }
            }
            inZip.close();
        } catch (IOException e) {
            e.getMessage();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (inZip != null) {
                    inZip.close();
                }
            } catch (IOException e) {

            }
        }
    }

    /**
     * asset文件是否存在
     */
    public static boolean isAssetFileExist(String assetPath) {
        AssetManager assetManager = MyApplication.getInstance().getAssets();
        InputStream source = null;
        try {
            source = assetManager.open(new File(assetPath).getPath());
            if (source != null) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.getMessage();
            return false;
        } finally {
            if (source != null) {
                try {
                    source.close();
                } catch (IOException e) {

                }
            }
        }
    }

    /**
     * 复制asset文件到本地
     */
    public static void copyAssetFile2Native(String assetPath, String outPath) {
        AssetManager assetManager = MyApplication.getInstance().getAssets();
        OutputStream destination = null;
        InputStream source = null;
        try {
            source = assetManager.open(new File(assetPath).getPath());
            File destinationFile = new File(outPath);
            if (destinationFile.exists()) {
                deleteFile(destinationFile.getPath());
            }
            destinationFile.getParentFile().mkdirs();
            destination = new FileOutputStream(destinationFile);
            byte[] buffer = new byte[1024];
            int nread;
            while ((nread = source.read(buffer)) != -1) {
                if (nread == 0) {
                    nread = source.read();
                    if (nread < 0)
                        break;
                    destination.write(nread);
                    continue;
                }
                destination.write(buffer, 0, nread);
            }

        } catch (IOException e) {
            e.getMessage();
        } finally {
            if (destination != null) {
                try {
                    destination.close();
                } catch (IOException e) {

                }
            }
            if (source != null) {
                try {
                    source.close();
                } catch (IOException e) {

                }
            }
        }
    }

    /**
     * 文件转化为base64编码文件
     */
    public static String file2Base64(String path) {
        InputStream is = null;
        byte[] data;
        String result = null;
        try {
            is = new FileInputStream(path);
            // 创建一个字符流大小的数组。
            data = new byte[is.available()];
            // 写入数组
            is.read(data);
            // 用默认的编码格式进行编码
            result = Base64.encodeToString(data, Base64.DEFAULT);
        } catch (IOException e) {

        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {

                }
            }
        }
        return result;
    }

    /**
     * base64编码文件转化为文件
     */
    public static String base642Image(String base64Str, String filePath, String fileType) {
        String id = DateUtils.getCurrentDate(DateUtils.DateFormat.yyyyMMddHHmmssSSS);
        String path = filePath + id + fileType;
        base642File(base64Str, path);
        return path;
    }

    /**
     * base64编码文件转化为文件
     */
    private static void base642File(String base64Str, String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        byte[] data = Base64.decode(base64Str, Base64.DEFAULT);
        for (int i = 0; i < data.length; i++) {
            if (data[i] < 0) {
                // 调整异常数据
                data[i] += 256;
            }
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(path);
            os.write(data);
            os.flush();
        } catch (Exception e) {

        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {

                }
            }
        }
    }

    /**
     * 清空文件夹
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.isDirectory() && null != file.listFiles() && file.listFiles().length > 0) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                deleteFile(f.getPath());
            }
        } else if (file.isDirectory() && null != file.listFiles() && file.listFiles().length == 0) {
            file.delete();
        } else if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 读取本地文件(*.txt, *.json)
     */
    public static String loadFile(String path) {
        File f = new File(path);
        if (!f.exists()) {
            return null;
        }
        String result = null;
        FileInputStream fin = null;
        try {
            int length = (int) f.length();
            byte[] buff = new byte[length];
            fin = new FileInputStream(f);
            fin.read(buff);
            fin.close();
            result = new String(buff, "UTF-8");
        } catch (Exception e) {
            e.getMessage();
        } finally {
            if (fin != null) {
                try {
                    fin.close();
                } catch (IOException e) {

                }
            }
        }
        return result;
    }

    /**
     * 保存文件到本地
     */
    public static String saveFile(String filePath, InputStream inputStream) {
        OutputStream outputStream = null;
        File file = null;
        try {
            if (filePath == null) {
                return null;
            }
            file = new File(filePath);
            if (file == null || !file.exists()) {
                file.createNewFile();
            }
            byte[] fileReader = new byte[4096];
            outputStream = new FileOutputStream(file);
            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }
                outputStream.write(fileReader, 0, read);
            }
            outputStream.flush();
        } catch (Exception e) {

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {

                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {

                }
            }
        }
        return file.getPath();
    }

    /**
     * 保存文件到本地（断点续传）
     */
    public static String saveFile(String filePath, long start, InputStream inputStream) {
        RandomAccessFile raf = null;
        File file = null;
        try {
            file = new File(filePath);
            raf = new RandomAccessFile(filePath, "rw");
            byte[] fileReader = new byte[4096];
            raf.seek(start);
            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }
                raf.write(fileReader, 0, read);
            }
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {

                }
            }
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {

                }
            }
        }
        return file.getPath();
    }

    /**
     * 保存图片到本地
     */
    public static boolean saveBitmap(Bitmap bitmap, File file, Bitmap.CompressFormat format) {
        if (bitmap == null || bitmap.getWidth() == 0 || bitmap.getHeight() == 0) {
            return false;
        }
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        OutputStream os = null;
        boolean ret = false;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file));
            ret = bitmap.compress(format, 100, os);
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
        } catch (IOException e) {

        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {

                }
            }
        }
        return ret;
    }

    /**
     * 将byte[]转换成Bitmap
     *
     * @param bytes
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getBitmapFromByte(byte[] bytes, int width, int height) {
        final YuvImage image = new YuvImage(bytes, ImageFormat.NV21, width, height, null);
        ByteArrayOutputStream os = new ByteArrayOutputStream(bytes.length);
        if (!image.compressToJpeg(new Rect(0, 0, width, height), 80, os)) {
            return null;
        }
        byte[] tmp = os.toByteArray();
        Bitmap bmp = BitmapFactory.decodeByteArray(tmp, 0, tmp.length);
        return bmp;
    }

    /**
     * 写入本地日志
     */
    public static String string2File(String content, String fileName) {
        File file = new File(ExternalFilePath.LOG_PATH, fileName);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        boolean operateFlg;
        try {
            operateFlg = file.createNewFile();
            if (operateFlg) {
                fos = new FileOutputStream(file);
                osw = new OutputStreamWriter(fos, "UTF-8");
                osw.write(content);
                osw.flush();
                fos.flush();
                return file.getAbsolutePath();
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (osw != null) {
                    osw.close();
                }
            } catch (Exception e) {

            }
        }
    }

}