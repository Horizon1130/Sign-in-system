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

public class StudentSignAdapter extends BasicsAdapter<JSONObject> {

    public StudentSignAdapter(Context context, List<JSONObject> data) {
        super(context, data);
    }

    @Override
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        ObjectItem itemView = null;
        if (paramView == null) {
            itemView = new ObjectItem();

            paramView = layoutInflater.inflate(R.layout.adapter_item_student_sign, null);
            itemView.face = paramView.findViewById(R.id.img_item_face);
            itemView.time = paramView.findViewById(R.id.input_item_time);
            itemView.nice = paramView.findViewById(R.id.input_item_nice);
            itemView.status = paramView.findViewById(R.id.input_item_status);

            paramView.setTag(itemView);
        } else {
            itemView = (ObjectItem) paramView.getTag();
        }
        JSONObject obj = data.get(paramInt);

        String face = obj.getString("face");
        String nice = obj.getString("nice");
        if (obj.containsKey("status")) {
            if (obj.getInteger("status").equals(0)) {
                itemView.status.setTextColor(context.getResources().getColor(R.color.sign_late_color));
            } else {
                itemView.status.setTextColor(context.getResources().getColor(R.color.sign_normal_color));
            }
            itemView.status.setText(obj.getString("statusVal"));
        }
        itemView.nice.setText(nice);
        if (obj.containsKey("time")) {
            itemView.time.setText(obj.getString("time"));
            itemView.time.setVisibility(View.VISIBLE);
        }
        ImageLoader.getInstance().displayImage(face, itemView.face, BitmapType.circleImage);

        return paramView;
    }

    class ObjectItem {

        public ImageView face;
        public TextView nice, status, time;

    }
}