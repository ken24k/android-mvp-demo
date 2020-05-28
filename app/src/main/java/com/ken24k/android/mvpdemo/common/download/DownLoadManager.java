package com.ken24k.android.mvpdemo.common.download;


import com.ken24k.android.mvpdemo.common.retrofit.RetrofitService;
import com.ken24k.android.mvpdemo.common.utils.FileUtils;
import com.ken24k.android.mvpdemo.common.utils.SettingsUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by wangming on 2020-05-28
 */

public class DownLoadManager {

    private static DownLoadManager loadManager;

    private HashMap<String, FileObserver> hashMap;
    private OkHttpClient client;
    private Retrofit retrofit;
    private RetrofitService apiServer;
    private DownFileCallback fileCallback;
    private String currentPath;

    private long downSize;
    private long totalSize;

    private DownLoadManager() {
        hashMap = new HashMap<>();
        client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        if (downSize != 0 && totalSize != 0) {
                            request = request.newBuilder().addHeader("RANGE", "bytes=" + downSize + "-" + totalSize).build();
                        }
                        Response response = chain.proceed(request);
                        return response.newBuilder().body(new ProgressResponseBody(response.body(),
                                new ProgressResponseBody.ProgressListener() {
                                    @Override
                                    public void onProgress(long totalSize, long downSize) {
                                        if (fileCallback != null) {
                                            fileCallback.onProgress(totalSize, downSize);
                                        }
                                    }
                                })).build();
                    }
                }).build();
        retrofit = new Retrofit.Builder().client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(SettingsUtils.getApiUrl()).build();
        apiServer = retrofit.create(RetrofitService.class);
    }

    public static DownLoadManager getInstance() {
        synchronized (Object.class) {
            if (loadManager == null) {
                loadManager = new DownLoadManager();
            }
        }
        return loadManager;
    }

    /**
     * 下载单个文件
     *
     * @param downModel
     * @param
     */
    public void downFile(final DownModel downModel, final DownFileCallback fileCallback) {
        if (downModel == null) {
            return;
        }
        // 如果正在下载，则暂停
        final String url = downModel.getUrl();
        if (isDownLoad(url)) {
            pause(url);
            return;
        }
        // 是否是断点下载
        if (downModel.getDownSize() != 0 && downModel.getTotalSize() != 0) {
            totalSize = downModel.getTotalSize();
            downSize = downModel.getDownSize();
        } else {
            totalSize = 0;
            downSize = 0;
        }
        // 获取保存地址
        currentPath = downModel.getPath();
        File checkFile = new File(currentPath);
        if (!checkFile.exists()) {
            checkFile.getParentFile().mkdirs();
        }

        this.fileCallback = fileCallback;

        FileObserver observer = apiServer.downloadFile(url).map(new Function<ResponseBody, String>() {
            @Override
            public String apply(ResponseBody body) {
                if (downModel.getDownSize() != 0 && downModel.getTotalSize() != 0) {
                    return FileUtils.saveFile(currentPath, downModel.getDownSize(), body.byteStream());
                }
                return FileUtils.saveFile(currentPath, body.byteStream());
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new FileObserver<String>() {
                    @Override
                    public void onSuccess(String path) {
                        downModel.setFinish(true);
                        downModel.setPath(path);
                        downModel.setExists(true);

                        if (fileCallback != null) {
                            fileCallback.onSuccess(path);
                        }
                        hashMap.remove(url);
                    }

                    @Override
                    public void onError(String msg) {
                        if (fileCallback != null) {
                            fileCallback.onFail(msg);
                        }
                        hashMap.remove(url);
                    }
                });
        // 保存
        hashMap.put(url, observer);
    }

    /**
     * 暂停/取消任务
     *
     * @param url 完整url
     */
    private void pause(String url) {
        if (hashMap.containsKey(url)) {
            FileObserver observer = hashMap.get(url);
            if (observer != null) {
                observer.dispose();
                hashMap.remove(url);
            }
        }
    }

    /**
     * 是否在下载
     *
     * @param url
     * @return
     */
    private boolean isDownLoad(String url) {
        return hashMap.containsKey(url);
    }

    private abstract class FileObserver<T> extends DisposableObserver<T> {

        @Override
        public void onNext(T t) {
            onSuccess(t);
        }

        @Override
        public void onError(Throwable e) {
            onError(e.getMessage());
        }

        @Override
        public void onComplete() {

        }

        public abstract void onSuccess(T o);

        public abstract void onError(String msg);

    }

}
