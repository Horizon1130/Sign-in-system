package cn.xiaoph.apps.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

import cn.xiaoph.apps.R;
import cn.xiaoph.library.adapter.BasicsAdapter;


public class CourseAdapter extends BasicsAdapter<JSONObject> {

    public CourseAdapter(Context context, List<JSONObject> data) {
        super(context, data);
    }

    @Override
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        ObjectItem itemView = null;
        if (paramView == null) {
            paramView = layoutInflater.inflate(R.layout.adapter_item_course, null);
            TextView title = (TextView) paramView.findViewById(R.id.input_item_title);
            TextView time = (TextView) paramView.findViewById(R.id.input_item_time);
            TextView address = (TextView) paramView.findViewById(R.id.input_item_address);
            TextView status = (TextView) paramView.findViewById(R.id.input_item_status);

            itemView = new ObjectItem(title, address, time, status);
            paramView.setTag(itemView);
        } else {
            itemView = (ObjectItem) paramView.getTag();
        }
        JSONObject obj = data.get(paramInt);

        String title = obj.getString("title");
        String address = obj.getString("address");
        String time = obj.getString("time");
        String status = obj.getString("statusVal");

        itemView.getTitle().setText(title);
        itemView.getTime().setText(time);
        itemView.getStatus().setText(status);
        itemView.getAddress().setText(address);

        return paramView;
    }

    class ObjectItem {

        private TextView title, address, time, status;

        public ObjectItem(TextView title, TextView address, TextView time, TextView status) {
            this.title = title;
            this.address = address;
            this.time = time;
            this.status = status;
        }

        public TextView getTitle() {
            return title;
        }

        public void setTitle(TextView title) {
            this.title = title;
        }

        public TextView getTime() {
            return time;
        }

        public void setTime(TextView time) {
            this.time = time;
        }

        public TextView getStatus() {
            return status;
        }

        public void setStatus(TextView status) {
            this.status = status;
        }

        public TextView getAddress() {
            return address;
        }

        public void setAddress(TextView address) {
            this.address = address;
        }
    }
}
