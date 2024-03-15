package cn.xiaoph.library.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import cn.xiaoph.library.R;

public class BitmapType {

    public static final DisplayImageOptions aaaa = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.bg_unloaded) //设置图片在下载期间显示的图片
            .showImageForEmptyUri(R.mipmap.bg_broken)//设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.mipmap.bg_broken)  //设置图片加载/解码过程中错误时候显示的图片
            .cacheInMemory(true)//设置下载的图片是否缓存在内存中
            .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
            .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
            .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
            .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
            //.decodingOptions(BitmapFactory.Options decodingOptions)//设置图片的解码配置
            .delayBeforeLoading(0)//int delayInMillis为你设置的下载前的延迟时间
            //设置图片加入缓存前，对bitmap进行设置
            //.preProcessor(BitmapProcessor preProcessor)
            .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
            .displayer(new RoundedBitmapDisplayer(360))//不推荐用！！！！是否设置为圆角，弧度为多少
//			.displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间，可能会出现闪动
            .build();//构建完成

    public static final DisplayImageOptions commodityImage = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.bg_unloaded) //设置图片在下载期间显示的图片
            .showImageForEmptyUri(R.mipmap.bg_broken)//设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.mipmap.bg_commodity_fragment)  //设置图片加载/解码过程中错误时候显示的图片
            .cacheInMemory(true) // default
            .cacheOnDisk(true) // default
            .cacheInMemory(true).bitmapConfig(Bitmap.Config.ARGB_8888) // default
            .displayer(new SimpleBitmapDisplayer()) // default
            .build();

    public static final DisplayImageOptions logo = new DisplayImageOptions.Builder()
            .showImageOnLoading(R.mipmap.icon_commodity_loading) //设置图片在下载期间显示的图片
            .showImageForEmptyUri(R.mipmap.icon_commodity_loading)//设置图片Uri为空或是错误的时候显示的图片
            .showImageOnFail(R.mipmap.icon_commodity_loading)  //设置图片加载/解码过程中错误时候显示的图片
            .cacheInMemory(true) // default
            .cacheOnDisk(true) // default
            .cacheInMemory(true).bitmapConfig(Bitmap.Config.ARGB_8888) // default
            .displayer(new SimpleBitmapDisplayer()) // default
            .build();

    public static final DisplayImageOptions image = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.ic_user_face)
            // resource or
            .showImageOnFail(R.mipmap.ic_user_face)
            // resource or drawable
            .showImageOnLoading(R.mipmap.ic_user_face).cacheInMemory(true) // default
            .cacheOnDisk(true) // default
            .cacheInMemory(true).bitmapConfig(Bitmap.Config.ARGB_8888) // default
            .displayer(new SimpleBitmapDisplayer()) // default
            .build();

    public static final DisplayImageOptions tran = new DisplayImageOptions.Builder()
            .cacheInMemory(true) // default
            .cacheOnDisk(true) // default
            .bitmapConfig(Bitmap.Config.ARGB_8888) // default
            .displayer(new SimpleBitmapDisplayer()) // default
            .build();

    public static final DisplayImageOptions circleImage = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.ic_user_face)
            // resource or
            .showImageOnFail(R.mipmap.ic_user_face)
            // resource or drawable
            .showImageOnLoading(R.mipmap.ic_user_face).cacheInMemory(true)
            .cacheOnDisk(true).displayer(new CircleBitmapDisplayer()).build();

    public static DisplayImageOptions get18(Context context) {
        DisplayMetrics displayMetrics = context.getResources()
                .getDisplayMetrics();
        int r = (int) (18.0f * displayMetrics.density);
        DisplayImageOptions y_icon = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_user_face)
                .showImageOnFail(R.mipmap.ic_user_face)
                .showImageOnLoading(R.mipmap.ic_user_face)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .displayer(new RoundedBitmapDisplayer(r)) // default
                .build();
        return y_icon;

    }

    public static DisplayImageOptions get24(Context context) {
        DisplayMetrics displayMetrics = context.getResources()
                .getDisplayMetrics();
        int r = (int) (24.0f * displayMetrics.density);
        DisplayImageOptions y_icon = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_user_face)
                // resource or
                .showImageOnFail(R.mipmap.ic_user_face)
                // resource or drawable
                .showImageOnLoading(R.mipmap.ic_user_face)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .displayer(new RoundedBitmapDisplayer(r)) // default
                .build();
        return y_icon;
    }

    public static DisplayImageOptions get32(Context context) {
        DisplayMetrics displayMetrics = context.getResources()
                .getDisplayMetrics();
        int r = (int) (32.0f * displayMetrics.density);
        DisplayImageOptions y_icon = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_user_face)
                // resource oric_user_face
                .showImageOnFail(R.mipmap.ic_user_face)
                // resource or drawable
                .showImageOnLoading(R.mipmap.ic_user_face)
                .cacheInMemory(true) // default
                .cacheOnDisk(true) // default
                .bitmapConfig(Bitmap.Config.ARGB_8888) // default
                .displayer(new RoundedBitmapDisplayer(r)) // default
                .build();
        return y_icon;
    }

    public static final DisplayImageOptions options = new DisplayImageOptions.Builder()
            // .showImageOnLoading(R.drawable.ic_stub) // resource or drawable
            // .showImageForEmptyUri(R.drawable.ic_empty) // resource or drawable
            // .showImageOnFail(R.drawable.ic_error) // resource or drawable
            // .preProcessor(...)
            .resetViewBeforeLoading(true) // default
            .delayBeforeLoading(0).cacheInMemory(true) // default
            .cacheOnDisk(true) // default
            // .postProcessor(...)
            // .extraForDownloader(...)
            .considerExifParams(false) // default
            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED) // default
            .bitmapConfig(Bitmap.Config.ARGB_8888) // default
            // .decodingOptions(...)
            .displayer(new SimpleBitmapDisplayer()) // default
            .handler(new Handler()) // default
            .build();
}
