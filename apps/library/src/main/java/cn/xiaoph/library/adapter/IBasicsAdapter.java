package cn.xiaoph.library.adapter;

import android.widget.ListAdapter;
import java.util.List;

public abstract interface IBasicsAdapter<T> extends ListAdapter
{
  public abstract List<T> getData();

  public abstract void setData(List<T> paramList);

  public abstract void notifyDataSetChanged();
}