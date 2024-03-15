package cn.xiaoph.apps.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jph.takephoto.model.TImage;

import java.util.ArrayList;

import cn.xiaoph.library.activity.BaseActivity;
import cn.xiaoph.library.http.WebHttpEngine;
import es.dmoral.toasty.Toasty;

public abstract class BasicsActivity extends BaseActivity {

	public WebHttpEngine http = new WebHttpEngine();

	public void getImages(ArrayList<TImage> images){

	}

	private boolean isPermissionRequested;

	/**
	 * Android6.0之后需要动态申请权限
	 */
	protected void requestPermission() {
		if (Build.VERSION.SDK_INT >= 23 && !isPermissionRequested) {
			isPermissionRequested = true;
			ArrayList<String> permissionsList = new ArrayList<>();
			String[] permissions = {
					Manifest.permission.ACCESS_NETWORK_STATE,
					Manifest.permission.INTERNET,
					Manifest.permission.WRITE_EXTERNAL_STORAGE,
					Manifest.permission.READ_EXTERNAL_STORAGE,
					Manifest.permission.ACCESS_COARSE_LOCATION,
					Manifest.permission.ACCESS_FINE_LOCATION,
					Manifest.permission.ACCESS_WIFI_STATE,
			};

			for (String perm : permissions) {
				if (PackageManager.PERMISSION_GRANTED != checkSelfPermission(perm)) {
					permissionsList.add(perm);
				}
			}

			if (!permissionsList.isEmpty()) {
				String[] strings = new String[permissionsList.size()];
				requestPermissions(permissionsList.toArray(strings), 0);
			}
		}
	}

	public void relogin(){
//		Intent intent = new Intent(BasicsActivity.this,
//				LoginActivity.class);
//		startActivity(intent);
//		Toasty.warning(this, "登录失效，请重新登录", Toast.LENGTH_SHORT, true).show();
//		finish();
	}
	
	public boolean isEmpty(Object obj) {
		String text = "";
		if (obj instanceof TextView) {
			text = ((TextView) obj).getText().toString();
		}
		if (obj instanceof EditText) {
			text = ((EditText) obj).getText().toString();
		}
		if ("".equals(text) || text == null) {
			return true;
		}
		return false;
	}
	
	public void isEmpty(Object...obj) {	
		boolean isEmptyParam = false;
		for (Object object : obj) {
			if (object instanceof TextView) {
				String text = ((TextView) object).getText().toString();
				String messageHint = ((TextView) object).getHint().toString();
				if ("".equals(text) || text == null) {
					isEmptyParam = true;
					Toasty.warning(this, messageHint, Toast.LENGTH_SHORT, true).show();
				}
			}
		}
		if (isEmptyParam) {
			return ;
		}
	}
	
}
