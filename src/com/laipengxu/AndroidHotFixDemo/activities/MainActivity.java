package com.laipengxu.AndroidHotFixDemo.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.laipengxu.AndroidHotFixDemo.R;
import com.laipengxu.AndroidHotFixDemo.application.MyApplication;
import com.laipengxu.AndroidHotFixDemo.retrofit.ApiManager;
import com.laipengxu.AndroidHotFixDemo.utils.FileUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import java.io.*;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private Button mBtn_main;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initView();
        initListener();
        initData();
    }

    private void initView() {
        mBtn_main = (Button) findViewById(R.id.btn_main);
    }

    private void initListener() {
        mBtn_main.setOnClickListener(this);
    }

    private void initData() {
        // 加载补丁
        loadPatch();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_main:
                Toast.makeText(this, "Version1.0 fixed", Toast.LENGTH_LONG).show();
                //                startActivity(new Intent(MainActivity.this,ErrorActivity.class));
                //                startActivity(new Intent(MainActivity.this, SuccessActivity.class));
                break;
        }
    }

    /**
     * 加载补丁
     */
    private void loadPatch() {

        final File localFile = FileUtils.creatFile(Environment.getExternalStorageDirectory() + "/hotfix/hotfix.apatch");

        String downloadUrl = "hotfix.apatch";

        ApiManager
                .getInstance()
                .downloadVersion(downloadUrl)
                .concatMap(new Function<byte[], ObservableSource<File>>() {
                    @Override
                    public ObservableSource<File> apply(final byte[] bytes) throws Exception {
                        return Observable.create(new ObservableOnSubscribe<File>() {
                            @Override
                            public void subscribe(ObservableEmitter<File> emitter) throws Exception {
                                InputStream inputStream = new ByteArrayInputStream(bytes);
                                int len;
                                byte buffer[] = new byte[1024];
                                FileOutputStream outputStream = new FileOutputStream(localFile);
                                while ((len = inputStream.read(buffer)) != -1) {
                                    outputStream.write(buffer, 0, len);
                                    outputStream.flush();
                                }
                                inputStream.close();
                                outputStream.close();
                                emitter.onNext(localFile);
                            }
                        });
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<File>() {
                                   @Override
                                   public void onNext(File f) {
                                       try {
                                           MyApplication.getInstance().getPatchManager().addPatch(f.getAbsolutePath());
                                           Toast.makeText(MainActivity.this, "加载补丁成功", Toast.LENGTH_LONG).show();
                                       } catch (IOException e) {
                                           e.printStackTrace();
                                       }
                                   }

                                   @Override
                                   public void onError(Throwable throwable) {
                                       Toast.makeText(MainActivity.this, "加载补丁失败 => " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                                   }

                                   @Override
                                   public void onComplete() {
                                   }
                               }
                );
    }
}
