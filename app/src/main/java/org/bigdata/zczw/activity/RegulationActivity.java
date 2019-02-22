package org.bigdata.zczw.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.bigdata.zczw.R;
import org.bigdata.zczw.adapter.RegulationAdapter;
import org.bigdata.zczw.entity.Regulation;
import org.bigdata.zczw.entity.Regulations;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.ServerUtils;

import java.util.ArrayList;

/*
* 规章制度 章节目录
* */

public class RegulationActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String name;
    private int id;

    private ListView listView;
    private RegulationAdapter adapter;
    private ArrayList<Regulation> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regulation);
        AppManager.getAppManager().addActivity(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);

        name = getIntent().getStringExtra("name");
        id = getIntent().getIntExtra("id",0);

        getSupportActionBar().setTitle(name);

        listView = (ListView) findViewById(R.id.list_regulation_act);

        ServerUtils.getRegulation(id+"",callback);

        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(RegulationActivity.this,PdfActivity.class);
        intent.putExtra("list",arrayList);
        intent.putExtra("position",position);
        startActivity(intent);
    }


    private RequestCallBack<String> callback = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            Regulations bean = JsonUtils.jsonToPojo(json,Regulations.class);
            if (bean != null && bean.getStatus() == 200) {
                if (bean.getData()!=null && bean.getData().size()>0) {
                    arrayList = (ArrayList<Regulation>) bean.getData();
                    adapter = new RegulationAdapter(RegulationActivity.this,arrayList);
                    listView.setAdapter(adapter);
                }
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
