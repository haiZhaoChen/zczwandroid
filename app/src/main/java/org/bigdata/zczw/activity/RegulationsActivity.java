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
import org.bigdata.zczw.entity.Catalogs;
import org.bigdata.zczw.entity.Catalog;
import org.bigdata.zczw.adapter.CatalogAdapter;
import org.bigdata.zczw.utils.AppManager;
import org.bigdata.zczw.utils.JsonUtils;
import org.bigdata.zczw.utils.ServerUtils;

import java.util.ArrayList;
import java.util.HashMap;
/*
* 三标制度 制度列表
* */
public class RegulationsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private CatalogAdapter adapter;

    private ArrayList<Catalog> arrayList;
    private ArrayList<Catalog> showList;
    private ArrayList<Catalog> baseList;
    private HashMap<Integer,ArrayList<Catalog>> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regulations);
        AppManager.getAppManager().addActivity(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("三标制度");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.de_actionbar_back);

        listView = (ListView) findViewById(R.id.list_regulation);

        arrayList = new ArrayList<>();
        showList = new ArrayList<>();
        baseList = new ArrayList<>();
        map = new HashMap<>();


        ServerUtils.getAllCatalog(callback);

        listView.setOnItemClickListener(this);
    }

    private RequestCallBack<String> callback = new RequestCallBack<String>() {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            String json = responseInfo.result;
            Catalogs bean = JsonUtils.jsonToPojo(json,Catalogs.class);
            if (bean != null && bean.getStatus() == 200) {
                if (bean.getData()!=null && bean.getData().size()>0) {
                    arrayList = (ArrayList<Catalog>) bean.getData();
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (arrayList.get(i).getParentId() == 0) {
                            baseList.add(arrayList.get(i));
                        }
                    }
                    showList.addAll(baseList);
                    for (int i = 0; i < showList.size(); i++) {
                        ArrayList<Catalog> list = new ArrayList<>();
                        for (int j = 0; j < arrayList.size(); j++) {
                            if (showList.get(i).getId() == arrayList.get(j).getParentId()) {
                                list.add(arrayList.get(j));
                            }
                        }
                        map.put(showList.get(i).getId(),list);
                    }

                    adapter = new CatalogAdapter(RegulationsActivity.this,showList);
                    listView.setAdapter(adapter);
                }
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {

        }
    };


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (showList.get(position).getParentId() == 0) {
            Catalog catalog = showList.get(position);
            if (catalog.isShow()) {
                catalog.setShow(false);
                updateList(catalog);
            }else {
                catalog.setShow(true);
                updateList(catalog);
            }
        }else {
            Intent intent = new Intent(RegulationsActivity.this,RegulationActivity.class);
            intent.putExtra("name",showList.get(position).getName());
            intent.putExtra("id",showList.get(position).getId());
            startActivity(intent);
        }
    }

    private void updateList(Catalog catalog) {
        for (int i = 0; i < baseList.size(); i++) {
            if (baseList.get(i).getId() == catalog.getId()) {
                baseList.remove(i);
                baseList.add(i, catalog);
            }
        }
        showList.clear();
        for (int i = 0; i < baseList.size(); i++) {
            showList.add(baseList.get(i));
            if (baseList.get(i).isShow()) {
                showList.addAll(map.get(baseList.get(i).getId()));
            }
        }
        adapter.notifyDataSetChanged();
    }

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
