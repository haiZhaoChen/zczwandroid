package org.bigdata.zczw.SendGroupMsg;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.GroupInfo;
import org.bigdata.zczw.entity.Groups;
import org.bigdata.zczw.ui.WinToast;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.ServerUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.rong.imlib.model.Group;

public class GroupSelectActivity extends AppCompatActivity {

    private ListView listView;
    private SearchView searchView;
    private List<Group> groupList;
    private Map<String, Boolean> groupselect;
    private MyHolder myHolder;
    private MyAdapter myAdapter;
    private List<GroupInfo> myGroupInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_select);

        initView();
        initDate();

    }

    private void initView() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);
        getSupportActionBar().setTitle("选择群组");

        listView = (ListView) findViewById(R.id.de_group_list);
        searchView = (SearchView) findViewById(R.id.seacher_groups);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                myHolder = (MyHolder) view.getTag();
                if (groupselect.get(groupList.get(position).getId())) {
                    groupselect.put(groupList.get(position).getId(), false);
                } else {
                    groupselect.put(groupList.get(position).getId(), true);
                }
                myAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initDate() {
        // 获取群组列表
        ServerUtils.getMyCircle(0, group);
    }

    RequestCallBack<String> group = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            Groups groups = JsonUtils.jsonToPojo(json, Groups.class);
            switch (groups.getStatus()) {
                case 200://获取成功
                    groupList = new ArrayList<>();
                    myGroupInfo = groups.getData();
                    for (int i = 0; i < myGroupInfo.size(); i++) {
                        groupList.add(new Group(myGroupInfo.get(i).getId() + "", myGroupInfo.get(i).getName(), null));
                    }
                    groupselect = new HashMap<>();
                    for (Group group : groupList) {
                        groupselect.put(group.getId(), false);
                    }

                    myAdapter = new MyAdapter();
                    listView.setAdapter(myAdapter);
                    break;
                case 400://客户端错误
                    WinToast.toast(getApplicationContext(), "客户端错误");
                    break;
                case 444://登陆过期
                    WinToast.toast(getApplicationContext(), "登录过期");
                    break;
                case 500://服务器错误
                    WinToast.toast(getApplicationContext(), "服务器错误");
                    break;
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.de_group_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        Bundle bundle = new Bundle();
        MyMap myMap = new MyMap();
        myMap.setBooleanMap(groupselect);
        bundle.putSerializable("group", myMap);
        Intent intent = new Intent(GroupSelectActivity.this, SendGroupActivity.class);
        intent.putExtras(bundle);
        setResult(1, intent);
        finish();
        return super.onOptionsItemSelected(item);
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return myGroupInfo.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.group_select_item, null);
                myHolder = new MyHolder();
                myHolder.checkBox = (CheckBox) convertView.findViewById(R.id.de_checkbox);
                myHolder.imageView = (ImageView) convertView.findViewById(R.id.de_imageView);
                myHolder.textView = (TextView) convertView.findViewById(R.id.group_name);
                myHolder.count = (TextView) convertView.findViewById(R.id.group_count);
                convertView.setTag(myHolder);
            } else {
                myHolder = (MyHolder) convertView.getTag();
            }
            myHolder.textView.setText(myGroupInfo.get(position).getName());
            myHolder.count.setText(myGroupInfo.get(position).getTotle()+"名群成员");
            myHolder.checkBox.setChecked(groupselect.get(myGroupInfo.get(position).getId()));
            return convertView;
        }
    }

    public final class MyHolder {
        public CheckBox checkBox;
        public TextView textView;
        public TextView count;
        public ImageView imageView;
    }
}
