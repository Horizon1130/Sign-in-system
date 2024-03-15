package cn.xiaoph.apps.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.xiaoph.apps.R;
import cn.xiaoph.library.adapter.BasicsAdapter;
import cn.xiaoph.library.util.BitmapType;


public class NewsAdapter extends BasicsAdapter<JSONObject> {

    public NewsAdapter(Context context, List<JSONObject> data) {
        super(context, data);
    }

    @Override
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        ObjectItem itemView = null;

        paramView = layoutInflater.inflate(R.layout.adapter_item_news, null);
        ImageView cover = (ImageView) paramView.findViewById(R.id.img_item_cover);
        TextView title = (TextView) paramView.findViewById(R.id.input_item_title);
        TextView classify = (TextView) paramView.findViewById(R.id.input_item_classify);
        TextView time = (TextView) paramView.findViewById(R.id.input_item_time);

        itemView = new ObjectItem(cover, title, classify, time);

        JSONObject obj = data.get(paramInt);

        String coverVal = obj.getString("cover");
        String titleVal = obj.getString("title");
        String classifyVal = obj.getString("classify");
        String timeVal = obj.getString("time");

        if (coverVal != null) {
            ImageLoader.getInstance().displayImage(coverVal, itemView.cover, BitmapType.commodityImage);
            itemView.cover.setVisibility(View.VISIBLE);
        } else {
            itemView.cover.setVisibility(View.GONE);
        }
        itemView.title.setText(titleVal);
        itemView.time.setText(timeVal);
        itemView.classify.setText(classifyVal);

        return paramView;
    }

    class ObjectItem {

        public ImageView cover;
        public TextView title, classify, time;

        public ObjectItem(ImageView cover, TextView title, TextView classify, TextView time) {
            this.cover = cover;
            this.title = title;
            this.classify = classify;
            this.time = time;
        }
    }
}
