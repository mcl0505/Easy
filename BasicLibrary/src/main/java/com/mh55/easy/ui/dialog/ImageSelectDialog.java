package com.mh55.easy.ui.dialog;

import static android.Manifest.permission.READ_MEDIA_AUDIO;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.blankj.utilcode.util.AppUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.basic.PictureSelectionCameraModel;
import com.luck.picture.lib.basic.PictureSelectionModel;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.engine.CompressFileEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.luck.picture.lib.language.LanguageConfig;
import com.luck.picture.lib.permissions.PermissionConfig;
import com.mh55.easy.R;
import com.permissionx.guolindev.PermissionX;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropImageEngine;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnNewCompressListener;

/**
 * 公司：坤创科技
 * 作者：Android 孟从伦
 * 文件名：ImageSelectDialog
 * 创建时间：2020/6/5
 * 功能描述：   图片选择弹框
 * 可选择相册打开与相机
 */
public class ImageSelectDialog extends BaseDialogFragment implements View.OnClickListener {
    private TextView tvPhoto;
    private TextView tvCamera;
    private TextView tvCancel;


    //选择类型    PictureMimeType.ofImage()图片
    private int chooseType;
    //单选或者多选   ture单选   flase多选
    private boolean isSingle;
    //是否压缩
    private boolean isCompress;
    //是否裁剪
    private boolean enableCrop;
    private String mTitle1;
    private String mTitle2;
    private String mOpenPhoneHint;
    private String mOpenCameraHint;
    //图片裁剪的宽高
    private int wight;
    private int height;
    private boolean isCircleCrop;
    private boolean isDisplayCamera;
    //回调监听
    private OnSelectCallBackListener onSelectCallBackListener;
    //回调监听
    private OnPermissionDeniedListListener onDeniedListListener;

    private OnSelectOpenPhotoListener onSelectOpenPhotoListener;
    private OnSelectOpenCameraListener onSelectOpenCameraListener;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_select_img;
    }

    @Override
    public int setWidth() {
        return LinearLayout.LayoutParams.MATCH_PARENT;
    }

    @Override
    protected void main(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        tvPhoto = mRootView.findViewById(R.id.select_picture);
        tvCamera = mRootView.findViewById(R.id.select_camera);
        tvCancel = mRootView.findViewById(R.id.select_cancel);

        tvPhoto.setOnClickListener(this);
        tvCamera.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvPhoto.setText(mTitle1);
        tvCamera.setText(mTitle2);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.select_picture) {
            if (onSelectOpenPhotoListener == null) {
                openPhoto();
            } else {
                onSelectOpenPhotoListener.clickOpenPhoto();
            }

        } else if (v.getId() == R.id.select_camera) {
            if (onSelectOpenCameraListener == null) {
                openCamera();
            } else {
                onSelectOpenCameraListener.clickOpenPhoto();
            }
        } else if (v.getId() == R.id.select_cancel) {
            dismiss();
        }
    }

    /**
     * 打开相册选择   可选图片  视频
     */
    private void openPhoto() {
        String[] p = PermissionConfig.getReadPermissionArray(mContext, chooseType);
        PermissionX.init(this)
                .permissions(p)
                .explainReasonBeforeRequest()
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> {
                    if (beforeRequest){
                        String msg = AppUtils.getAppName() + "\n"+mOpenPhoneHint;
                        scope.showRequestReasonDialog(Arrays.asList(p), msg, "授权", "拒绝");
                    }
                })
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {

                        PictureSelectionModel model = PictureSelector.create(this)
                                .openGallery(chooseType)
                                .isPreviewImage(true)
                                .isPreviewVideo(true)
                                .setImageEngine(GlideEngine.createGlideEngine())
                                .setMaxSelectNum(isSingle ? 1 : 3)
                                .isDisplayCamera(isDisplayCamera)
                                .isPreviewAudio(true)
                                .setLanguage(LanguageConfig.CHINESE)
                                ;

                        if (isCompress){
                            model.setCompressEngine((CompressFileEngine) (context, source, call) -> {
                                Luban.with(mContext).load(source).ignoreBy(100)
                                        .setCompressListener(new OnNewCompressListener() {
                                            @Override
                                            public void onStart() {

                                            }

                                            @Override
                                            public void onSuccess(String source, File compressFile) {
                                                if (call != null) {
                                                    call.onCallback(source, compressFile.getAbsolutePath());
                                                }
                                            }

                                            @Override
                                            public void onError(String source, Throwable e) {
                                                if (call != null) {
                                                    call.onCallback(source, null);
                                                }
                                            }
                                        }).launch();
                            });
                        }

                        if (enableCrop){
                            model.setCropEngine((fragment, inputUri, destinationUri, dataCropSource, requestCode) -> {
                                UCrop uCrop = UCrop.of(inputUri, destinationUri, dataCropSource);
                                uCrop.setImageEngine(new UCropImageEngine() {
                                    @Override
                                    public void loadImage(Context context, String url, ImageView imageView) {
                                        Glide.with(context).load(url).into(imageView);
                                    }

                                    @Override
                                    public void loadImage(Context context, Uri uri, int i, int i1, OnCallbackListener<Bitmap> onCallbackListener) {

                                    }

                                });
//                                        uCrop.withOptions(buildOptions());
                                //设置裁剪宽高比例
                                uCrop.withAspectRatio(wight,height);
                                uCrop.start(fragment.getActivity(), fragment, requestCode);
                            });
                        }

                        model.forResult(new OnResultCallbackListener<LocalMedia>() {
                            @Override
                            public void onResult(ArrayList<LocalMedia> result) {
                                List<LocalMedia> list = new ArrayList<>();
                                for (LocalMedia localMedia : result) {
                                    list.add(localMedia);
                                }
                                onSelectCallBackListener.selectCallBack(list);
                            }

                            @Override
                            public void onCancel() {

                            }
                        });

                        dismiss();
                    }else {
                        if (onDeniedListListener != null) {
                            onDeniedListListener.onDeniedListListener(deniedList);
                        }
                    }
                });


    }


    /**
     * 打开相机
     */
    private void openCamera() {

        String tip = (chooseType == SelectMimeType.ofAudio()) ? "需要以下权限进行音频录制" : mOpenCameraHint;
        String[] p = PermissionConfig.getReadPermissionArray(mContext,SelectMimeType.ofVideo());
        List<String> pList = new ArrayList<>();
        pList.addAll(Arrays.asList(p));
        pList.add("android.permission.CAMERA");
        if (chooseType == SelectMimeType.ofAudio()){
            pList.add(READ_MEDIA_AUDIO);
        }


        PermissionX.init(this)
                .permissions(pList)
                .explainReasonBeforeRequest()
                .onExplainRequestReason((scope, deniedList, beforeRequest) -> {
                    if (beforeRequest){
                        String msg = AppUtils.getAppName() + "\n" + tip;
                        scope.showRequestReasonDialog(pList, msg, "授权", "拒绝");
                    }

                })
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        if (chooseType == SelectMimeType.ofAudio()) {
                            Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                            startActivity(intent);
                        } else {
                            PictureSelectionCameraModel model = PictureSelector.create(this)
                                    .openCamera(chooseType)
                                    .setLanguage(LanguageConfig.CHINESE);
                            if (isCompress){
                                model.setCompressEngine((CompressFileEngine) (context, source, call) -> {
                                    Luban.with(mContext).load(source).ignoreBy(100)
                                            .setCompressListener(new OnNewCompressListener() {
                                                @Override
                                                public void onStart() {

                                                }

                                                @Override
                                                public void onSuccess(String source, File compressFile) {
                                                    if (call != null) {
                                                        call.onCallback(source, compressFile.getAbsolutePath());
                                                    }
                                                }

                                                @Override
                                                public void onError(String source, Throwable e) {
                                                    if (call != null) {
                                                        call.onCallback(source, null);
                                                    }
                                                }
                                            }).launch();
                                });
                            }

                            if (enableCrop){
                                model.setCropEngine((fragment, inputUri, destinationUri, dataCropSource, requestCode) -> {
                                    UCrop uCrop = UCrop.of(inputUri, destinationUri, dataCropSource);
                                    uCrop.setImageEngine(new UCropImageEngine() {
                                        @Override
                                        public void loadImage(Context context, String url, ImageView imageView) {
                                            Glide.with(context).load(url)
                                                    .into(imageView);
                                        }

                                        @Override
                                        public void loadImage(Context context, Uri uri, int i, int i1, OnCallbackListener<Bitmap> onCallbackListener) {

                                        }

                                    });
//                                        uCrop.withOptions(buildOptions());
                                    //设置裁剪宽高比例
                                    uCrop.withAspectRatio(wight,height);
                                    uCrop.start(fragment.getActivity(), fragment, requestCode);
                                });
                            }
                            model.forResult(new OnResultCallbackListener<LocalMedia>() {
                                        @Override
                                        public void onResult(ArrayList<LocalMedia> result) {
                                            List<LocalMedia> list = new ArrayList<>();
                                            for (LocalMedia localMedia : result) {
                                                list.add(localMedia);
                                            }
                                            onSelectCallBackListener.selectCallBack(list);
                                        }

                                        @Override
                                        public void onCancel() {

                                        }
                                    });
                            dismiss();
                        }
                    } else {
                        if (onDeniedListListener != null) {
                            onDeniedListListener.onDeniedListListener(deniedList);
                        }
                    }


                });

    }

    void savefile(URI sourceuri) {
        String sourceFilename = sourceuri.getPath();
        String destinationFilename = Environment.getExternalStorageDirectory().getPath() + File.separatorChar + "abc.mp3";

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void show(FragmentManager manager) {
        show(manager, TAG);
    }

    /**
     * 选择结果回调监听
     */
    public interface OnSelectCallBackListener {
        void selectCallBack(List<LocalMedia> result);
    }

    /**
     * 点击相册监听
     */
    public interface OnSelectOpenPhotoListener {
        void clickOpenPhoto();
    }

    /**
     * 点击相册监听
     */
    public interface OnPermissionDeniedListListener {
        void onDeniedListListener(List<String> deniedList);
    }

    /**
     * 点击相机监听
     */
    public interface OnSelectOpenCameraListener {
        void clickOpenPhoto();
    }

    public ImageSelectDialog(Builder builder) {
        this.chooseType = builder.chooseType;
        this.isSingle = builder.isSingle;
        this.enableCrop = builder.enableCrop;
        this.isCompress = builder.isCompress;
        this.mTitle1 = builder.mTitle1;
        this.mTitle2 = builder.mTitle2;
        this.wight = builder.wight;
        this.height = builder.height;
        this.isCircleCrop = builder.isCircleCrop;
        this.isDisplayCamera = builder.isDisplayCamera;
        this.onSelectCallBackListener = builder.onSelectCallBackListener;
        this.onDeniedListListener = builder.onDeniedListListener;
        this.onSelectOpenPhotoListener = builder.onSelectOpenPhotoListener;
        this.onSelectOpenCameraListener = builder.onSelectOpenCameraListener;
    }

    public static class Builder {
        //选择类型    PictureMimeType.ofImage()图片
        private int chooseType = SelectMimeType.ofImage();
        //单选或者多选   ture单选   flase多选
        private boolean isSingle = true;
        //是否压缩
        private boolean isCompress = false;
        //是否裁剪
        private boolean enableCrop = false;
        //图片裁剪的宽高   如16:9 3:2 3:4 1:1 可自定义
        private int wight = 0;
        private int height = 0;
        //是否圆形剪裁
        private boolean isCircleCrop = false;
        private boolean isDisplayCamera = false;
        private OnSelectCallBackListener onSelectCallBackListener;
        private OnPermissionDeniedListListener onDeniedListListener;
        private OnSelectOpenPhotoListener onSelectOpenPhotoListener;
        private OnSelectOpenCameraListener onSelectOpenCameraListener;
        private String mTitle1 = "从相册选择";
        private String mTitle2 = "打开相机";
        private String mOpenPhoneHint = "需要以下权限进行多媒体资源获取";
        private String mOpenCameraHint = "需要以下权限进行相机拍摄";

        public Builder setChooseType(int chooseType) {  // PictureMimeType.ofImage()
            this.chooseType = chooseType;
            return this;
        }

        public Builder setSingle(boolean single) {
            isSingle = single;
            return this;
        }

        public Builder setTitle1(String mTitle) {
            mTitle1 = mTitle;
            return this;
        }

        public Builder setTitle2(String mTitle) {
            mTitle2 = mTitle;
            return this;
        }

        public Builder setmOpenPhoneHint(String mOpenPhoneHint) {
            this.mOpenPhoneHint = mOpenPhoneHint;
            return this;
        }

        public Builder setmOpenCameraHint(String mOpenCameraHint) {
            this.mOpenCameraHint = mOpenCameraHint;
            return this;
        }

        public Builder setCompress(boolean compress) {
            isCompress = compress;
            return this;
        }

        public Builder setEnableCrop(boolean enableCrop) {
            this.enableCrop = enableCrop;
            return this;
        }

        //如16:9 3:2 3:4 1:1 可自定义
        public Builder setWightHeight(int wight, int height) {
            this.wight = wight;
            this.height = height;
            return this;
        }

        public Builder setCircleCrop(boolean circleCrop) {
            isCircleCrop = circleCrop;
            return this;
        }

        public Builder setDisplayCamera(boolean displayCamera) {
            isDisplayCamera = displayCamera;
            return this;
        }

        public Builder setOnSelectCallBackListener(OnSelectCallBackListener onSelectCallBackListener) {
            this.onSelectCallBackListener = onSelectCallBackListener;
            return this;
        }

        public Builder setOnDeniedListListener(OnPermissionDeniedListListener onDeniedListListener) {
            this.onDeniedListListener = onDeniedListListener;
            return this;
        }

        public Builder setOnSelectOpenPhotoListener(OnSelectOpenPhotoListener onSelectOpenPhotoListener) {
            this.onSelectOpenPhotoListener = onSelectOpenPhotoListener;
            return this;
        }

        public Builder setOnSelectOpenCameraListener(OnSelectOpenCameraListener onSelectOpenCameraListener) {
            this.onSelectOpenCameraListener = onSelectOpenCameraListener;
            return this;
        }

        public ImageSelectDialog build() {
            return new ImageSelectDialog(this);
        }
    }
}
