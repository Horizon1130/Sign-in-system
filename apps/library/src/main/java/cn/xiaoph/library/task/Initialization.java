package cn.xiaoph.library.task;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;
import cn.xiaoph.library.http.ApiResources;
import es.dmoral.toasty.Toasty;

public class Initialization {
	
	private Context context;
	
	public Initialization(){}
	
	public Initialization(Context context){
		this.context = context;
	}
	
	public void getHttpRequestPath(){
		try {
			ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
			String domain_name = appInfo.metaData.getString("domain_name");
			int timeout = appInfo.metaData.getInt("timeout");
			ApiResources.domain_name = domain_name;
			ApiResources.timeout = timeout;
		} catch (Exception e) {
			Toasty.warning(context, "网络通信初始化错误", Toast.LENGTH_SHORT, true).show();
		}
	}

}
