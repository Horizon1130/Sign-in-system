package cn.xiaoph.library.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import cn.xiaoph.library.R;


public class ConfirmGrade extends Dialog implements
		View.OnClickListener {

	private Context context;
	private String title;
	private String content;
	private String confirmButtonText;
	private String cancelButtonText;
	private ClickListenerInterface clickListenerInterface;

	public interface ClickListenerInterface {

		public void doConfirm(String score);

		public void doCancel();
	}

	public ConfirmGrade(String title, String content, Context context) {
		super(context, R.style.time_dialog);
		this.content = content;
		this.title = title;
		this.context = context;
	}

	public ConfirmGrade(String title, String content,
                        String confirmButtonText, String cancelButtonText, Context context) {
		super(context, R.style.time_dialog);
		this.confirmButtonText = confirmButtonText;
		this.cancelButtonText = cancelButtonText;
		this.content = content;
		this.title = title;
		this.context = context;
	}

	public ConfirmGrade(Context context) {
		super(context);
	}

	EditText tvContent;

	public void init() {
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.view_dialog_grade, null);
		setContentView(view);

		TextView tvTitle = (TextView) view.findViewById(R.id.view_dialog_title);
		tvContent = (EditText) view.findViewById(R.id.view_dialog_content);
		TextView tvConfirm = (TextView) view.findViewById(R.id.btn_confirm);
		TextView tvCancel = (TextView) view.findViewById(R.id.btn_cancel);

		tvTitle.setText(title);
		tvContent.setHint(content);
		if (confirmButtonText != null){
			tvConfirm.setText(confirmButtonText);
		}
		if (cancelButtonText != null){
			tvCancel.setText(cancelButtonText);
		}

		tvConfirm.setOnClickListener(this);
		tvCancel.setOnClickListener(this);

		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽�?�高�?
		lp.width = (int) (d.widthPixels * 0.8); // 高度设置为屏幕的0.6
		dialogWindow.setAttributes(lp);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		findViewById(R.id.btn_confirm);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.btn_confirm) {
			clickListenerInterface.doConfirm(tvContent.getText().toString());
		}else if(view.getId() == R.id.btn_cancel){
			clickListenerInterface.doCancel();
		}
	}

	public void setClickListener(ClickListenerInterface clickListenerInterface) {
		this.clickListenerInterface = clickListenerInterface;
	}
}