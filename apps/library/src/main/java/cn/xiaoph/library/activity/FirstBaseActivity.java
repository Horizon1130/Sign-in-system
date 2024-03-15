package cn.xiaoph.library.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.xiaoph.library.R;
import cn.xiaoph.library.view.CustomDatePicker;

@SuppressLint("NewApi")
public abstract class FirstBaseActivity extends TakePhotoActivity {

	private static final Integer SELECT_PHOTO_COUNT = 5;	//选择图片数量
	private static final PhotoForm SELECT_PHOTO_FORM = PhotoForm.Capture;	//图片选择源
	private static final Boolean COMPRESS = true;		//是否压缩
	private static final Integer MAXSIZE = 102400;		//图片大小 byte
	private static final Integer MAXPIXEL = 800;		//图片长宽限制 px

	enum PhotoForm {
		File,			//文件
		Capture		//相册
	}

	protected void showWindowPhoto(final Integer selectPhotoCount) {
		final Dialog pickerDialog = new Dialog(this, R.style.time_dialog);
		pickerDialog.setCancelable(false);
		pickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		pickerDialog.setContentView(R.layout.item_popupwindows);

		Window window = pickerDialog.getWindow();
		window.setGravity(Gravity.BOTTOM);
		WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(dm);
		WindowManager.LayoutParams lp = window.getAttributes();
		lp.width = dm.widthPixels;
		window.setAttributes(lp);

		RelativeLayout parent = (RelativeLayout) pickerDialog.findViewById(R.id.parent);
		TextView btnCamera = (TextView) pickerDialog.findViewById(R.id.item_popupwindows_camera);
		TextView btnPhoto = (TextView) pickerDialog.findViewById(R.id.item_popupwindows_photo);
		TextView btnCancel = (TextView) pickerDialog.findViewById(R.id.item_popupwindows_cancel);
		parent.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				pickerDialog.dismiss();
			}
		});
		btnCamera.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showCapture();
				pickerDialog.dismiss();
			}
		});
		btnPhoto.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				showAlbum(selectPhotoCount);
				pickerDialog.dismiss();
			}
		});
		btnCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				pickerDialog.dismiss();
			}
		});
		pickerDialog.show();
	}

	/**
	 * 打开相册
	 * @param selectPhotoCount
	 */
	public void showAlbum(Integer selectPhotoCount){
		TakePhoto takePhoto = getTakePhoto();
		Uri imageUri = getImageUri();
		configCompress(takePhoto);
		int photoCount = SELECT_PHOTO_COUNT;
		if (selectPhotoCount != null){
			photoCount = selectPhotoCount;
		}
		if(photoCount >= 0){
			takePhoto.onPickMultiple(photoCount);
			return ;
		}
		if(SELECT_PHOTO_FORM == PhotoForm.Capture){
			takePhoto.onPickFromGallery();
		}else if(SELECT_PHOTO_FORM == PhotoForm.File){
			takePhoto.onPickFromDocuments();
		}
	}

	public void showCapture(){
		TakePhoto takePhoto = getTakePhoto();
		Uri imageUri = getImageUri();
		configCompress(takePhoto);
		takePhoto.onPickFromCapture(imageUri);
	}

	public Uri getImageUri(){
		File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
		if (!file.getParentFile().exists())file.getParentFile().mkdirs();
		Uri imageUri = Uri.fromFile(file);
		return imageUri;
	}

	private void configCompress(TakePhoto takePhoto){
		if(!COMPRESS){           //图片不进行压缩
			takePhoto.onEnableCompress(null,false);
			return ;
		}
		int maxSize= MAXSIZE;                //图片大小
		int maxPixel= MAXPIXEL;                  //图片长宽限制
		CompressConfig config= new CompressConfig.Builder().setMaxPixel(MAXSIZE).setMaxPixel(MAXPIXEL).create();
		takePhoto.onEnableCompress(config, false);                         //压缩进度条显示
	}

	public abstract void getImages(ArrayList<TImage> images);

	@Override
	public void takeCancel() {
		super.takeCancel();
	}

	@Override
	public void takeFail(TResult result, String msg) {
		super.takeFail(result, msg);
	}

	@Override
	public void takeSuccess(TResult result) {
		super.takeSuccess(result);
		getImages(result.getImages());
	}

	public void showDatePicker(final TextView dateView, boolean showSpecificTime, final String pattern, int beforeYear,
			int afterYear) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm",
				Locale.CHINA);
		String now = sdf.format(new Date());
		if (dateView.getText().toString() != null
				&& !"".equals(dateView.getText().toString())) {
			String value = dateView.getText().toString();
			if (pattern != null) {
				SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
				Date parse = dateFormat.parse(value);
				now = dateFormat.format(parse);
			} else {
				Date parse = sdf.parse(value);
				now = sdf.format(parse);
			}
		}
		Calendar startCal = Calendar.getInstance();
		startCal.add(Calendar.YEAR, beforeYear);
		Date minDate = startCal.getTime();

		Calendar endCal = Calendar.getInstance();
		endCal.add(Calendar.YEAR, afterYear);
		Date maxDate = endCal.getTime();

		String min = sdf.format(minDate);
		String max = sdf.format(maxDate);

		CustomDatePicker customDatePicker = new CustomDatePicker(this,
				new CustomDatePicker.ResultHandler() {
					@Override
					public void handle(String time) {
						if (pattern != null) {
							SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
							try {
								Date parse = dateFormat.parse(time);
								dateView.setText(dateFormat.format(parse));
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}else {
							dateView.setText(time);
						}
					}
				}, min, max); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
		customDatePicker.showSpecificTime(showSpecificTime); // 不显示时和分
		customDatePicker.setIsLoop(false); // 不允许循环滚动
		customDatePicker.setMonthClear(true);
		customDatePicker.setDayClear(true);
		customDatePicker.setHourClear(true);
		customDatePicker.setMinClear(true);
		customDatePicker.show(now);
	}
	
	public void showListPicker(final TextView textView, List<String> list,
			String title, String confirmText, String cancelText) {
//		CustomListPicker customDatePicker = new CustomListPicker(this, list,
//				title, confirmText, cancelText,
//				new CustomListPicker.ResultHandler() {
//					@Override
//					public void handle(String text) {
//						textView.setText(text);
//					}
//				});
//		customDatePicker.setIsLoop(false);
//		customDatePicker.show(textView.getText().toString());
	}

}