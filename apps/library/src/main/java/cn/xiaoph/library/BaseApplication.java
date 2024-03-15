package cn.xiaoph.library;

import android.app.Application;

import com.alibaba.fastjson.parser.ParserConfig;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

import cn.xiaoph.library.task.Initialization;
import es.dmoral.toasty.Toasty;

public class BaseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		ParserConfig.getGlobalInstance().setAutoTypeSupport(true); 
		new Initialization(this).getHttpRequestPath();

		Toasty.Config.getInstance()
				.setErrorColor(getResources().getColor(R.color.toast_error))
				.setInfoColor(getResources().getColor(R.color.toast_info))
				.setSuccessColor(getResources().getColor(R.color.toast_success))
				.setWarningColor(getResources().getColor(R.color.toast_warning))
				.apply();

		File cacheDir = StorageUtils.getCacheDirectory(this);  //缓存文件夹路径
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
				.memoryCacheExtraOptions(480, 800) // default = device screen dimensions 内存缓存文件的最大长宽
				.diskCacheExtraOptions(480, 800, null)  // 本地缓存的详细信息(缓存的最大长宽)，最好不要设置这个
				.threadPriority(Thread.NORM_PRIORITY - 2) // default 设置当前线程的优先级
				.tasksProcessingOrder(QueueProcessingType.FIFO) // default
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(2 * 1024 * 1024)) //可以通过自己的内存缓存实现
				.memoryCacheSize(2 * 1024 * 1024)  // 内存缓存的最大值
				.memoryCacheSizePercentage(13) // default
				.diskCache(new UnlimitedDiscCache(cacheDir)) // default 可以自定义缓存路径
				.diskCacheSize(50 * 1024 * 1024) // 50 Mb sd卡(本地)缓存的最大值
				.diskCacheFileCount(100)  // 可以缓存的文件数量
				// default为使用HASHCODE对UIL进行加密命名， 还可以用MD5(new Md5FileNameGenerator())加密
				.diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
				.imageDownloader(new BaseImageDownloader(this)) // default
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
				.writeDebugLogs() // 打印debug log
				.build(); //开始构建
		ImageLoader.getInstance().init(config);
	}
}
