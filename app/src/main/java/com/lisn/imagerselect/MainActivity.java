package com.lisn.imagerselect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.util.ArrayList;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import me.nereo.multi_image_selector.bean.Image;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_IMAGE = 189;
    private Button bt_selectImager;
    private int mRequestCode = 188;
    private TextView tv;
    private ImageGridview IMageGV;
    private ArrayList<String> mSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(MainActivity.this));
        initView();


    }

    private void initView() {
        bt_selectImager = (Button) findViewById(R.id.bt_selectImager);

        bt_selectImager.setOnClickListener(this);
        tv = (TextView) findViewById(R.id.tv);
        IMageGV= (ImageGridview) findViewById(R.id.ImageGv);

        IMageGV.setNumColumns(5);
        IMageGV.setOnItemClickCallback(new ImageGridview.OnItemClickCallback() {
            @Override
            public void onItemClick(int postion, String path) {
                delFile(path);
                Log.e("delFile", "onItemClick: "+path );
            }
        });
        IMageGV.setAddImagesCallback(new ImageGridview.AddImagesCallback() {
            @Override
            public void addCallback() {
                startSelectImage(mSelect,null);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_selectImager:
//                Intent intent=new Intent(this, MultiImageSelectorActivity.class);
//                startActivityForResult(intent, mRequestCode);

//                startSelectImage(mSelect,null);
                ArrayList<String> imageByPath = IMageGV.getImageByPath();
                Log.e("----", "onClick: "+imageByPath.toString() );

//                Intent uploadService = new Intent(MainActivity.this, UploadService.class);
//                startService(uploadService);

                new upLoadThread().start();
                break;
        }
    }

    /**
     * 死循环发送消息
     */
    private class upLoadThread extends Thread {
        @Override
        public void run() {
            super.run();
            Looper.prepare();
            Handler handler=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    Log.e("---", "handleMessage: -----" );
                    Toast.makeText(getApplicationContext(), "handler msg", Toast.LENGTH_LONG).show();
                    sendEmptyMessageDelayed(1,50000);

//                    SystemClock.sleep(30000);
                }
            };

            handler.sendEmptyMessage(1);
            Looper.loop();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mRequestCode) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> result = data.getStringArrayListExtra("select_result");
                Log.e("result", "onActivityResult: " + result.toString());

                tv.setText(result.toString());
            }
        }else if (requestCode == REQUEST_IMAGE){
            if (resultCode == RESULT_OK) {
                ArrayList<String> result = data.getStringArrayListExtra("select_result");
                Log.e("result", "onActivityResult: " + result.toString());
                mSelect=result;
                tv.setText(result.toString());

                if (result.size()>0 &&result!=null){
                    IMageGV.setData(result);

                }
            }
        }
    }

    private void delFile(final String path) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("是否删除");
        builder.setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (IMageGV != null) {
                    IMageGV.delectImage(path);
                }

            }
        });
        builder.show();
    }

    /**开始选择图片*/
    public void startSelectImage(ArrayList<String> mSelectPath, String dir) {
        Intent intent = new Intent(MainActivity.this, MultiImageSelectorActivity.class);
        // 是否显示调用相机拍照
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大图片选择数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 6);
        //文件输出路径
//        intent.putExtra(MultiImageSelectorActivity.OUTPUT_FILE_PATH, dir);

        // 设置模式 (支持 单选/.MODE_SINGLE 或者 多选/.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        // 默认选择图片,回填选项(支持String ArrayList)
        if (mSelectPath != null && mSelectPath.size()>0) {
            intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, mSelectPath);
        }
        this.startActivityForResult(intent, REQUEST_IMAGE);
    }

    /**
     * 存储文件目录
     */
    public static String getFileMediaPath(Context context, String vehicleSn) {
        String folderPath = null;
        String mediaFile = "/" + vehicleSn + "/";
        folderPath = getAppRootPath(context) + mediaFile;
        File file = new File(folderPath);
        if (!file.exists())
            file.mkdirs();
        Log.v("folderPath", folderPath);
        return folderPath;
    }

    /**
     * 获取包的目录地址
     */
    public static String getAppRootPath(Context context) {
        File sdcard = Environment.getExternalStorageDirectory();
//        String app = "com.tengdi.motorvehicle";// context.getPackageName();
        String app = context.getPackageName();
        String apkPath = sdcard.getAbsolutePath() + "/" + app;
        File file = new File(apkPath);
        if (!file.exists())
            file.mkdirs();
        return apkPath;
    }
}
