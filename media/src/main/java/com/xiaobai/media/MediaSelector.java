package com.xiaobai.media;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.xiaobai.media.activity.MediaActivity;
import com.xiaobai.media.bean.MediaFile;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 作者: 胡庆岭
 * 创建时间: 2020/6/20 22:37
 * 更新时间: 2020/6/20 22:37
 * 描述:
 */
public class MediaSelector {
    private MediaOption mOption = MediaSelector.getDefaultOption();
    private ArrayList<MediaFile> mSelectorMediaFileData;
    private WeakReference<Fragment> mSoftFragment;
    private WeakReference<Activity> mSoftActivity;

    public static final String KEY_MEDIA_OPTION = "key_media_option";
    public static final String KEY_MEDIA_DATA = "key_media_data";
    public static final int REQUEST_CODE_MEDIA = 2080;

    private MediaSelector(@NonNull Fragment fragment) {
        mSoftFragment = new WeakReference<>(fragment);
    }

    private MediaSelector(@NonNull Activity activity) {
        mSoftActivity = new WeakReference<>(activity);
    }

    public static MediaSelector with(@NonNull Activity activity) {
        return new MediaSelector(activity);
    }

    public static MediaSelector with(@NonNull Fragment fragment) {
        return new MediaSelector(fragment);
    }

    public MediaSelector buildMediaOption(@NonNull MediaOption mediaOption) {
        this.mOption = mediaOption;
        return this;
    }

    public MediaSelector buildMediaSelectorData(ArrayList<MediaFile> selectorMediaFileData) {
        this.mSelectorMediaFileData = selectorMediaFileData;
        if (mSelectorMediaFileData == null) {
            mSelectorMediaFileData = new ArrayList<>();
        }
        return this;
    }

    public void openActivity() {
        if (mSoftActivity.get() != null) {
            Activity activity = mSoftActivity.get();
            Intent intent = new Intent(activity, MediaActivity.class);
            intent.putExtra(MediaSelector.KEY_MEDIA_OPTION, mOption);
            intent.putExtra(MediaSelector.KEY_MEDIA_DATA, mSelectorMediaFileData);
            activity.startActivityForResult(intent, MediaSelector.REQUEST_CODE_MEDIA);
        } else if (mSoftFragment.get() != null) {
            Fragment fragment = mSoftFragment.get();
            Intent intent = new Intent(fragment.getContext(), MediaActivity.class);
            intent.putExtra(MediaSelector.KEY_MEDIA_OPTION, mOption);
            intent.putExtra(MediaSelector.KEY_MEDIA_DATA, mSelectorMediaFileData);
            fragment.startActivityForResult(intent, MediaSelector.REQUEST_CODE_MEDIA);
        }
    }

    public static class MediaOption implements Parcelable {
        public static final int MEDIA_ALL = 0;
        public static final int MEDIA_IMAGE = 1;
        public static final int MEDIA_VIDEO = 2;

        public MediaOption() {
        }

        public int maxSelectorMedia = R.integer.max_choose_media;
        public boolean isCompress;
        public boolean isShowCamera;
        //是不是选择单一类型
        public boolean isSelectorMultiple;
        public boolean isCrop;
        public int cropScaleX = 1;
        public int cropScaleY = 1;
        public int cropWidth = 720;
        public int cropSHeight = 720;
        public int mediaType = MediaOption.MEDIA_IMAGE;
        public ArrayList<MediaFile> selectorFileData;


        protected MediaOption(Parcel in) {
            maxSelectorMedia = in.readInt();
            isCompress = in.readByte() != 0;
            isShowCamera = in.readByte() != 0;
            isSelectorMultiple = in.readByte() != 0;
            isCrop = in.readByte() != 0;
            cropScaleX = in.readInt();
            cropScaleY = in.readInt();
            cropWidth = in.readInt();
            cropSHeight = in.readInt();
            mediaType = in.readInt();
            selectorFileData = in.createTypedArrayList(MediaFile.CREATOR);
        }

        public static final Creator<MediaOption> CREATOR = new Creator<MediaOption>() {
            @Override
            public MediaOption createFromParcel(Parcel in) {
                return new MediaOption(in);
            }

            @Override
            public MediaOption[] newArray(int size) {
                return new MediaOption[size];
            }
        };


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(maxSelectorMedia);
            dest.writeByte((byte) (isCompress ? 1 : 0));
            dest.writeByte((byte) (isShowCamera ? 1 : 0));
            dest.writeByte((byte) (isSelectorMultiple ? 1 : 0));
            dest.writeByte((byte) (isCrop ? 1 : 0));
            dest.writeInt(cropScaleX);
            dest.writeInt(cropScaleY);
            dest.writeInt(cropWidth);
            dest.writeInt(cropSHeight);
            dest.writeInt(mediaType);
            dest.writeTypedList(selectorFileData);
        }
    }

    public static MediaOption getDefaultOption() {
        return new MediaOption();
    }

}
