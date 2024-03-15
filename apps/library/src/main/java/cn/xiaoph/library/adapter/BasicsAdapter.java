package cn.xiaoph.library.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class BasicsAdapter<T> extends BaseAdapter implements
		IBasicsAdapter<T> {
	
	public List<T> data;
	public LayoutInflater layoutInflater;
	public Context context;
	public OnClickListener listener;

	public OnClickListener getListener() {
		return listener;
	}

	public void setListener(OnClickListener listener) {
		this.listener = listener;
	}

	public abstract View getView(int paramInt, View paramView,
			ViewGroup paramViewGroup);

	public BasicsAdapter(Context context, List<T> data) {
		this.context = context;
		this.data = data;
		this.layoutInflater = LayoutInflater.from(context);
	}

	public BasicsAdapter(Context context, List<T> data,OnClickListener listener) {
		this.context = context;
		this.data = data;
		this.layoutInflater = LayoutInflater.from(context);
		this.listener = listener;
	}

	public void setData(List<T> data) {
		this.data.addAll(data);
	}

	public List<T> getData() {
		return this.data;
	}

	public int getCount() {
		return this.data.size();
	}

	public T getItem(int position) {
		return this.data.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

}