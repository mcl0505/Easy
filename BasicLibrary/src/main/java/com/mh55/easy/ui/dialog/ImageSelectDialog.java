package com.mh55.easy.ui.dialog;

import static com.mh55.easy.ext.PerminssionExtKt.getImageDialogPermission;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.blankj.utilcode.util.AppUtils;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.luck.picture.lib.language.LanguageConfig;
import com.luck.picture.lib.permissions.PermissionConfig;
import com.mh55.easy.R;
import com.mh55.easy.utils.LogUtil;
import com.mh55.easy.utils.ToastUtil;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.ExplainReasonCallbackWithBeforeParam;
import com.permissionx.guolindev.dialog.DefaultDialog;
import com.permissionx.guolindev.request.ExplainScope;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    //单选或者多选   ture单选   flase多选
    private boolean isDisplayCamera;
    //是否压缩
    private boolean isCompress;
    //是否裁剪
    private boolean enableCrop;
    private String mTitle1;
    private String mTitle2;
    //图片裁剪的宽高
    private int wight;
    private int height;
    private boolean isCircleCrop;
    //回调监听
    private OnSelectCallBackListener onSelectCallBackListener;

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
        PermissionX.init(this)
                .permissions(PermissionConfig.getReadPermissionArray(mContext,chooseType))
                .explainReasonBeforeRequest()
                .onExplainRequestReason((explainScope, list, b) -> {
                    if (b) {
                        String msg =
                                AppUtils.getAppName() + "需要多媒体权限进行图片获取,方便文件上传。";
                        DefaultDialog dialog = new DefaultDialog(
                                mContext,
                                Arrays.asList(PermissionConfig.getReadPermissionArray(mContext, chooseType)),
                                msg,
                                "授权",
                                "拒绝",
                                mContext.getResources().getColor(R.color.color_333333),
                                mContext.getResources().getColor(R.color.color_666666));
                        explainScope.showRequestReasonDialog(dialog);

                    }
                })
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted){
                        dismiss();

                        PictureSelector.create(this)
                                .openGallery(chooseType)
                                .isPreviewImage(true)
                                .isPreviewVideo(true)
                                .setImageEngine(GlideEngine.createGlideEngine())
                                .setMaxSelectNum(isSingle ? 1 : 3)
                                .isPreviewAudio(true)
                                .setLanguage(LanguageConfig.CHINESE)
                                .isDisplayCamera(isDisplayCamera)
                                .forResult(new OnResultCallbackListener<LocalMedia>() {
                                    @Override
                                    public void onResult(ArrayList<LocalMedia> result) {
                                        List<String> list = new ArrayList<>();
                                        for (LocalMedia localMedia : result) {
                                            list.add(localMedia.getRealPath());
                                        }
                                        onSelectCallBackListener.selectCallBack(list);
                                    }

                                    @Override
                                    public void onCancel() {

                                    }
                                });
                    }else {
                        ToastUtil.INSTANCE.toast("权限获取不足,请前往设置中打开后重试",null);
                    }
                });

    }

    /**
     * 打开相机
     */
    private void openCamera() {
        String[] p = PermissionConfig.getReadPermissionArray(mContext,SelectMimeType.ofVideo());
        List<String> pList = new ArrayList<>();
        pList.addAll(Arrays.asList(p));
        pList.add("android.permission.CAMERA");
        PermissionX.init(this)
                .permissions(pList)
                .explainReasonBeforeRequest()
                .onExplainRequestReason((explainScope, list, b) -> {
                    if (b) {
                        String msg =
                                AppUtils.getAppName() + "需要拍摄照片与录制视频权限,方便文件上传。";
                        DefaultDialog dialog = new DefaultDialog(
                                mContext,
                                pList,
                                msg,
                                "授权",
                                "拒绝",
                                mContext.getResources().getColor(R.color.color_333333),
                                mContext.getResources().getColor(R.color.color_666666));
                        explainScope.showRequestReasonDialog(dialog);

                    }
                })
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted){
                        dismiss();
                        if (chooseType == SelectMimeType.ofAudio()) {
                            Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                            startActivity(intent);
                        } else {
                            PictureSelector.create(this)
                                    .openCamera(chooseType)
                                    .setLanguage(LanguageConfig.CHINESE)
                                    .forResult(new OnResultCallbackListener<LocalMedia>() {
                                        @Override
                                        public void onResult(ArrayList<LocalMedia> result) {
                                            List<String> list = new ArrayList<>();
                                            for (LocalMedia localMedia : result) {
                                                list.add(localMedia.getRealPath());
                                            }
                                            onSelectCallBackListener.selectCallBack(list);
                                        }

                                        @Override
                                        public void onCancel() {

                                        }
                                    });
                        }
                    }else {
                        LogUtil.d(grantedList);
                        LogUtil.d(deniedList);
                        ToastUtil.INSTANCE.toast("权限获取不足,请前往设置中打开后重试",null);
                    }
                });


    }

    public void show(FragmentManager manager) {
        show(manager, TAG);
    }

    /**
     * 选择结果回调监听
     */
    public interface OnSelectCallBackListener {
        void selectCallBack(List<String> result);
    }

    /**
     * 点击相册监听
     */
    public interface OnSelectOpenPhotoListener {
        void clickOpenPhoto();
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
        this.isDisplayCamera = builder.isDisplayCamera;
        this.enableCrop = builder.enableCrop;
        this.isCompress = builder.isCompress;
        this.mTitle1 = builder.mTitle1;
        this.mTitle2 = builder.mTitle2;
        this.wight = builder.wight;
        this.height = builder.height;
        this.isCircleCrop = builder.isCircleCrop;
        this.onSelectCallBackListener = builder.onSelectCallBackListener;
        this.onSelectOpenPhotoListener = builder.onSelectOpenPhotoListener;
        this.onSelectOpenCameraListener = builder.onSelectOpenCameraListener;
    }

    public static class Builder {
        //选择类型    PictureMimeType.ofImage()图片
        private int chooseType = SelectMimeType.ofImage();
        //单选或者多选   ture单选   flase多选
        private boolean isSingle = true;
        private boolean isDisplayCamera = true;
        //是否压缩
        private boolean isCompress = true;
        //是否裁剪
        private boolean enableCrop = true;
        //图片裁剪的宽高   如16:9 3:2 3:4 1:1 可自定义
        private int wight = 1;
        private int height = 1;
        //是否圆形剪裁
        private boolean isCircleCrop = false;
        private OnSelectCallBackListener onSelectCallBackListener;
        private OnSelectOpenPhotoListener onSelectOpenPhotoListener;
        private OnSelectOpenCameraListener onSelectOpenCameraListener;
        private String mTitle1 = "从相册选择";
        private String mTitle2 = "打开相机";

        public Builder setChooseType(int chooseType) {  // PictureMimeType.ofImage()
            this.chooseType = chooseType;
            return this;
        }

        public Builder setSingle(boolean single) {
            isSingle = single;
            return this;
        }

        public Builder setDisplayCamera(boolean displayCamera) {
            isDisplayCamera = displayCamera;
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

        public Builder setOnSelectCallBackListener(OnSelectCallBackListener onSelectCallBackListener) {
            this.onSelectCallBackListener = onSelectCallBackListener;
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
