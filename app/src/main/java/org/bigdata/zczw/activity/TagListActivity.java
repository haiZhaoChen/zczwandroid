package org.bigdata.zczw.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.R;
import org.bigdata.zczw.adapter.TagListAdapter;
import org.bigdata.zczw.entity.Record;
import org.bigdata.zczw.entity.TagDataEntity;
import org.bigdata.zczw.entity.TagLabel;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.ServerUtils;

import java.io.Serializable;
import java.util.List;

public class TagListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private TagListAdapter adapter;
    private List<TagDataEntity> data;

    @Override
    protected void onStart() {
        super.onStart();
        ServerUtils.getTagLabel(tag);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_list);
        AppManager.getAppManager().addActivity(this);
        getSupportActionBar().setTitle("所有标签");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);

        initView();
        initData();
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.list_tag_act);
    }

    private void initData() {
        listView.setOnItemClickListener(this);
    }

    private RequestCallBack<String> tag = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            TagLabel tagLabel = JsonUtils.jsonToPojo(json, TagLabel.class);
            if (tagLabel.getMsg().equals("OK")) {
                data = tagLabel.getData();
                adapter = new TagListAdapter(TagListActivity.this, data);
                listView.setAdapter(adapter);
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(TagListActivity.this,TagCreateActivity.class);
        intent.putExtra("change",0);
        intent.putExtra("name",data.get(position).getLabelName());
        intent.putExtra("tagDate", (Serializable) data.get(position).getUserIds());
        intent.putExtra("labelId", data.get(position).getLabelId());
        intent.putExtra("isMy", data.get(position).isIsMy());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.de_add_tag, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            //新建标签
            case R.id.tag_new:
                Intent intent = new Intent(TagListActivity.this,UserSelectActivity.class);
                intent.putExtra("type","new");
                startActivityForResult(intent, 99);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
