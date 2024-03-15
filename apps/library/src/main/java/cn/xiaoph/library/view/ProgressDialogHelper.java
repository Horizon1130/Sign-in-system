package cn.xiaoph.library.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.xiaoph.library.R;

public class ProgressDialogHelper {

	private Activity mActivity;
	private Dialog mAlertDialog;

	public ProgressDialogHelper(Activity activity) {
		this.mActivity = activity;
	}

	public void showProgressDialog(String msg) {
		showProgressDialog(msg, false, null);
	}


	public void showProgressDialog(final String msg, final boolean cancelable,
								   final DialogInterface.OnCancelListener cancelListener) {
		dismissProgressDialog();
		this.mActivity.runOnUiThread(new Runnable() {
			public void run() {
				if ((ProgressDialogHelper.this.mActivity == null)
						|| (ProgressDialogHelper.this.mActivity.isFinishing())) {
					return;
				}
				ProgressDialogHelper.this.mAlertDialog = new Dialog(ProgressDialogHelper.this.mActivity,  R.style.time_dialog);
				ProgressDialogHelper.this.mAlertDialog.setCancelable(cancelable);
				ProgressDialogHelper.this.mAlertDialog.setOnCancelListener(cancelListener);
				View v = LayoutInflater.from(ProgressDialogHelper.this.mActivity).inflate(R.layout.view_progress, null);
				TextView inputMessage = ( TextView) v.findViewById(R.id.input_view_message);
				inputMessage.setText(msg);
				ProgressDialogHelper.this.mAlertDialog.show();
				ProgressDialogHelper.this.mAlertDialog.setContentView(v);
				ProgressDialogHelper.this.mAlertDialog
						.setCanceledOnTouchOutside(false);
			}
		});
	}

	public void dismissProgressDialog() {
		this.mActivity.runOnUiThread(new Runnable() {
			public void run() {
				if ((ProgressDialogHelper.this.mAlertDialog != null)
						&& (ProgressDialogHelper.this.mAlertDialog.isShowing())
						&& (!ProgressDialogHelper.this.mActivity.isFinishing())) {
					ProgressDialogHelper.this.mAlertDialog.dismiss();
					ProgressDialogHelper.this.mAlertDialog = null;
				}
			}
		});
	}
}
