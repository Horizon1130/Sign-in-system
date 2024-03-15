package cn.xiaoph.library.view;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

import cn.xiaoph.library.R;

public class CustomListPicker {

	/**
	 * 定义结果回调接口
	 */
	public interface ResultHandler {
		void handle(JSONObject value);
	}

	private ResultHandler handler;
	private Context context;
	private Dialog listPickerDialog;
	private ListPickerView pv_title;
	private TextView tv_cancle, tv_confirm;
	private JSONObject value;
	private List<JSONObject> list;

	public CustomListPicker(Context context, List<JSONObject> list, String confirmText, String cancelText, ResultHandler resultHandler) {
		this.context = context;
		this.handler = resultHandler;
		this.list = list;
		initDialog();
		initView(confirmText, cancelText);
	}

	private void initDialog() {
		if (listPickerDialog == null) {
			listPickerDialog = new Dialog(context, R.style.time_dialog);
			listPickerDialog.setCancelable(false);
			listPickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			listPickerDialog.setContentView(R.layout.custom_list_picker);
			Window window = listPickerDialog.getWindow();
			window.setGravity(Gravity.BOTTOM);
			WindowManager manager = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			DisplayMetrics dm = new DisplayMetrics();
			manager.getDefaultDisplay().getMetrics(dm);
			WindowManager.LayoutParams lp = window.getAttributes();
			lp.width = dm.widthPixels;
			window.setAttributes(lp);
		}
	}

	private void initView(String confirmText, String cancelText) {
		pv_title = (ListPickerView) listPickerDialog.findViewById(R.id.pv_title);
		tv_cancle = (TextView) listPickerDialog.findViewById(R.id.input_bar_left_button);
		tv_cancle.setVisibility(View.VISIBLE);
		tv_cancle.setText(cancelText);
		tv_confirm = (TextView) listPickerDialog.findViewById(R.id.input_bar_right_button);
		tv_confirm.setVisibility(View.VISIBLE);
		tv_confirm.setText(confirmText);

		tv_cancle.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				listPickerDialog.dismiss();
			}
		});

		tv_confirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				handler.handle(value);
				listPickerDialog.dismiss();
			}
		});
	}

	private void loadComponent() {
		pv_title.setData(list);
		executeScroll();
	}

	private void addListener() {
		pv_title.setOnSelectListener(new ListPickerView.onSelectListener() {

			@Override
			public void onSelect(JSONObject text) {
				value = text;
			}
		});
	}

	private void executeScroll() {
		pv_title.setCanScroll(list.size() > 1);
	}

	public void show(String text) {
		loadComponent();
		addListener();
		setSelectedTime(text);
		if(value == null){
			if (list.size() > 0){
				value = list.get(0);;
			}
		}
		listPickerDialog.show();
	}

	/**
	 * 设置控件是否可以循环滚动
	 */
	public void setIsLoop(boolean isLoop) {
		this.pv_title.setIsLoop(isLoop);
	}

	/**
	 * 设置控件默认选中的时间
	 */
	public void setSelectedTime(String text) {
		if (text != null && !"".equals(text)) {
			pv_title.setSelected(text);
		}else if (list.size() > 0) {
			String title = list.get(0).getString("title");
			pv_title.setSelected(title);
			value = list.get(0);
		}
		executeScroll();
	}

}
