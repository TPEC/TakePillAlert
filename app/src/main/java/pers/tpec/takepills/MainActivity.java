package pers.tpec.takepills;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class MainActivity extends AppCompatActivity implements XRecyclerView.LoadingListener {
    private XRecyclerView xRecyclerView;
    private MultiTypeAdapter multiTypeAdapter;
    private final Items items = new Items();
    private int nowPage = 0;
    private static final int PAGE_SIZE = 200;

    private final List<TPInfo> infoList = new ArrayList<>();

    private static final Comparator<TPInfo> comparator = new Comparator<TPInfo>() {
        @Override
        public int compare(TPInfo tpInfo, TPInfo t1) {
            return Long.compare(t1.getTime(), tpInfo.getTime());
        }
    };

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        private Context context;

        @Override
        public void set(String key, Object value) {
            if (key.equals("context")) {
                this.context = (Context) value;
            }
        }

        @Override
        public void click(View view, Object data) {

        }

        @Override
        public void longClick(View view, Object data) {
            if (data instanceof TPInfo) {
                final TPInfo tpInfo = (TPInfo) data;
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("删除").setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteList(tpInfo);
                    }
                }).setNegativeButton("取消", null).show();
            }
        }
    };

    private void deleteList(TPInfo tpInfo) {
        for (int i = 0; i < infoList.size(); i++) {
            if (tpInfo.getTime() == infoList.get(i).getTime()) {
                infoList.remove(i);
                showToast("删除成功！");
                break;
            }
        }
        saveDataToLocal();
        refreshItems();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("吃药记录");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        onItemClickListener.set("context", this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                addList(null);
            }
        });

        xRecyclerView = findViewById(R.id.xRecyclerView);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        xRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xRecyclerView.setLoadingListener(this);
        xRecyclerView.setPullRefreshEnabled(true);
        xRecyclerView.setLoadingMoreEnabled(false);
        multiTypeAdapter = new MultiTypeAdapter();
        multiTypeAdapter.register(TPInfo.class, new TPInfoProvider(onItemClickListener));
        multiTypeAdapter.register(String.class, new TPTopProvider(onItemClickListener));
        multiTypeAdapter.setItems(items);
        xRecyclerView.setAdapter(multiTypeAdapter);

        loadDataFromLocal();
        refreshItems();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    private void addList(TPInfo tpInfo) {
        if (tpInfo == null) {
            infoList.add(new TPInfo());
        } else {
            infoList.add(tpInfo);
        }
        Collections.sort(infoList, comparator);
        saveDataToLocal();
        refreshItems();
        showToast("添加成功！");
    }

    private Toast toast;

    private void showToast(String msg) {
        if (TextUtils.isEmpty(msg)) return;
        if (toast == null) {
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
            toast.show();
        } else {
            toast.setText(msg);
            toast.show();
        }
    }

    @Override
    public void onRefresh() {
        loadDataFromLocal();
        refreshItems();
        xRecyclerView.refreshComplete();
    }

    @Override
    public void onLoadMore() {
        xRecyclerView.loadMoreComplete();
    }

    private void loadDataFromLocal() {
        SharedPreferences sp = SharedPreferencesUtil.getSP(this);
        String data = sp.getString("list", "");
        try {
            JSONArray array = new JSONArray(data);
            infoList.clear();
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                infoList.add(TPInfo.from(object));
            }
            Collections.sort(infoList, comparator);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void saveDataToLocal() {
        JSONArray array = new JSONArray();
        try {
            for (TPInfo tpInfo : infoList) {
                array.put(tpInfo.toJSONObject());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Map<String, String> m = new HashMap<>();
        m.put("list", array.toString());
        SharedPreferencesUtil.save(this, m);
    }

    private void refreshItems() {
        nowPage = 0;
        items.clear();
        if (infoList.isEmpty()) {
            items.add("--");
        } else {
            long t = (new Date().getTime() - infoList.get(0).getTime()) / 60000;
            long m = t % 60;
            long h = t / 60 % 24;
            long d = t / 60 / 24;
            StringBuilder sb = new StringBuilder("距离上次吃药：");
            if (d > 0) {
                sb.append(d).append("天")
                        .append(h).append("时")
                        .append(m).append("分");
            } else if (h > 0) {
                sb.append(h).append("时")
                        .append(m).append("分");
            } else {
                sb.append(m).append("分");
            }

            items.add(sb.toString());
        }
        for (int i = 0; i < PAGE_SIZE && i < infoList.size(); i++) {
            items.add(infoList.get(i));
        }
        multiTypeAdapter.notifyDataSetChanged();
    }
}
