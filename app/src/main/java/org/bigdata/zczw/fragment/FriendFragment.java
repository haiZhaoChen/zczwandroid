package org.bigdata.zczw.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.LexiconListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.cloud.util.UserWords;

import org.bigdata.zczw.R;
import org.bigdata.zczw.SendGroupMsg.SendGroupActivity;
import org.bigdata.zczw.Singleton;
import org.bigdata.zczw.activity.GroupListActivity;
import org.bigdata.zczw.activity.PersonalActivity;
import org.bigdata.zczw.activity.TagListActivity;
import org.bigdata.zczw.adapter.DeAddressMultiChoiceAdapter;
import org.bigdata.zczw.adapter.DeFriendListAdapter;
import org.bigdata.zczw.entity.Friend;
import org.bigdata.zczw.entity.User;
import org.bigdata.zczw.ui.DePinnedHeaderListView;
import org.bigdata.zczw.ui.DeSwitchGroup;
import org.bigdata.zczw.ui.DeSwitchItemView;
import org.bigdata.zczw.utils.JsonUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendFragment extends Fragment implements DeSwitchGroup.ItemHander, View.OnClickListener, TextWatcher,
        DeFriendListAdapter.OnFilterFinished, AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {

    public static final String ACTION_DMEO_AGREE_REQUEST = "action_demo_agree_request";
    private static final String TAG = FriendFragment.class.getSimpleName();
    protected DeAddressMultiChoiceAdapter mAdapter;
    private DePinnedHeaderListView mListView;
    private DeSwitchGroup mSwitchGroup;
    private ImageView imgMsg;
    private ImageView voice;
    /**
     * 好友list
     */
    protected List<Friend> mFriendsList;
    private List<Friend> mSearchFriendsList;
    private TextView textViwe;
    private ReceiveMessageBroadcastReciver mBroadcastReciver;

    private static View view;
    //搜索框
    private SearchView searchView;

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private int count = 0;
    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private String title;

    public FriendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_friend, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        searchView = (SearchView) view.findViewById(R.id.seacher_friends);
        mListView = (DePinnedHeaderListView) view.findViewById(R.id.de_ui_friend_list);
        mSwitchGroup = (DeSwitchGroup) view.findViewById(R.id.de_ui_friend_message);
        mListView.setPinnedHeaderView(LayoutInflater.from(this.getActivity()).inflate(R.layout.de_item_friend_index, mListView, false));
        textViwe = (TextView) mListView.getPinnedHeaderView();
        imgMsg = (ImageView) view.findViewById(R.id.img_add_msg_friend_frg);
        voice = (ImageView) view.findViewById(R.id.img_voice_search_frg);
        mListView.setFastScrollEnabled(false);
        mListView.setOnItemClickListener(this);
        mSwitchGroup.setItemHander(this);
        mListView.setHeaderDividersEnabled(false);
        mListView.setFooterDividersEnabled(false);
        searchView.setIconifiedByDefault(false);//自动缩小为图标
        searchView.setOnQueryTextListener(this);//事件监听器
        searchView.setSubmitButtonEnabled(false);//不显示搜索按钮
        searchView.setQueryHint("姓名/职务/部门 文字或语音检索");//默认显示文本

        //获取到TextView的ID
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text",null,null);
        //获取到TextView的控件
        TextView textView = (TextView) searchView.findViewById(id);
        //设置字体大小为14sp
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);//14sp
        //设置字体颜色
        textView.setTextColor(getActivity().getResources().getColor(R.color.black));
        //设置提示文字颜色
        textView.setHintTextColor(getActivity().getResources().getColor(R.color.barcolor2));


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_DMEO_AGREE_REQUEST);
        if (mBroadcastReciver == null) {
            mBroadcastReciver = new ReceiveMessageBroadcastReciver();
        }
        getActivity().registerReceiver(mBroadcastReciver, intentFilter);

        return view;
    }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            //搜索用的list
            mSearchFriendsList = new ArrayList<>();
            mFriendsList = Singleton.getInstance().getmFriendsList();
            if (mFriendsList != null && mFriendsList.size()>0) {
                mFriendsList = Singleton.getInstance().sort(mFriendsList,getContext());
                mAdapter = new DeAddressMultiChoiceAdapter(getActivity(), mFriendsList);
                mListView.setAdapter(mAdapter);
            }
            fillData();
            imgMsg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPopupMenu(imgMsg);
                }
            });

//        initUserWords();
            // 初始化识别无UI识别对象
            // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
            mIat = SpeechRecognizer.createRecognizer(getContext(), mInitListener);

            // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
            // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
            mIatDialog = new RecognizerDialog(getContext(), mInitListener);
            voice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchView.setQueryHint("");
                    // 设置参数
                    setParam();
                    mIatDialog.setListener(mRecognizerDialogListener);
                    mIatDialog.show();
                }
            });

            searchView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    searchView.setQueryHint("姓名/职务/部门 文字或语音检索");
                    fillData();
                }
            });
        }

    private void initUserWords() {
        // 获取好友列表
        ArrayList<User> userInfos = (ArrayList<User>) Singleton.getInstance().getFriendlist();
        UserWords userWords = new UserWords();
        for (int i = 0; i < userInfos.size(); i++) {
            userWords.putWord(userInfos.get(i).getUsername());
            userWords.putWord(userInfos.get(i).getUnitsName());
            userWords.putWord(userInfos.get(i).getJobsName());
        }
        SpeechRecognizer mIat= SpeechRecognizer.createRecognizer(getContext(), null);
        mIat.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        //指定引擎类型
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
        int ret = mIat.updateLexicon("userword", userWords.toString(), lexiconListener);
        if(ret != ErrorCode.SUCCESS){
            Log.d(TAG,"上传用户词表失败：" + ret);
        }

    }
    //上传用户词表监听器。
    private LexiconListener lexiconListener = new LexiconListener() {
        @Override
        public void onLexiconUpdated(String lexiconId, SpeechError error) {
            if(error != null){
                Log.d(TAG,error.toString());
            }else{
                Log.d(TAG,"上传成功！");
            }
        }
    };

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(getContext(),"语音初始化失败",Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
            count++;
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            error.getPlainDescription(true);
        }

    };

    private void printResult(RecognizerResult results) {
        String text = JsonUtils.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }
        if (count %2 ==1) {
            title = resultBuffer.toString();
            searchView.setQueryHint(title);
            if (TextUtils.isEmpty(title)) {
                //清除好友列表的过滤操作
                fillData();
            } else {
                mSearchFriendsList = searchItem(title, mFriendsList);
                mAdapter.setAdapterData(mSearchFriendsList);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v instanceof DeSwitchItemView) {
            CharSequence tag = ((DeSwitchItemView) v).getText();

            if (mAdapter != null && mAdapter.getSectionIndexer() != null) {
                Object[] sections = mAdapter.getSectionIndexer().getSections();
                int size = sections.length;

                for (int i = 0; i < size; i++) {
                    if (tag.equals(sections[i])) {
                        int index = mAdapter.getPositionForSection(i);
                        mListView.setSelection(index + mListView.getHeaderViewsCount());
                        break;
                    }
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object tagObj = view.getTag();
        if (tagObj != null && tagObj instanceof DeAddressMultiChoiceAdapter.ViewHolder) {
            DeAddressMultiChoiceAdapter.ViewHolder viewHolder = (DeAddressMultiChoiceAdapter.ViewHolder) tagObj;
            String friendId = viewHolder.friend.getUserId();

            if (friendId == "★002") {
                startActivity(new Intent(getActivity(), GroupListActivity.class));
            } else if(friendId == "★003"){
                startActivity(new Intent(getActivity(), TagListActivity.class));
            } else {
                Intent intent = new Intent(getActivity(), PersonalActivity.class);
                intent.putExtra("PERSONAL", viewHolder.friend.getUserId());
                startActivityForResult(intent, 19);
            }
            return;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            //清除好友列表的过滤操作
            fillData();
        } else {
            mSearchFriendsList = searchItem(newText, mFriendsList);
            mAdapter.setAdapterData(mSearchFriendsList);
            mAdapter.notifyDataSetChanged();
        }
        return false;
    }

    private class ReceiveMessageBroadcastReciver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ACTION_DMEO_AGREE_REQUEST)) {
                updateDate();
            }
        }
    }

    private void updateDate() {
        if (mAdapter != null) {
            mAdapter = null;
        }

        mFriendsList = Singleton.getInstance().getmFriendsList();
        mFriendsList = Singleton.getInstance().sort(mFriendsList,getContext());
        mAdapter = new DeAddressMultiChoiceAdapter(getActivity(), mFriendsList);
        mListView.setAdapter(mAdapter);
        fillData();
    }



    private final void fillData() {
        if (mFriendsList != null && mFriendsList.size()>0) {
            mAdapter.setAdapterData(mFriendsList);
            mAdapter.notifyDataSetChanged();
        }else {
            mFriendsList = new ArrayList<>();
            mAdapter.setAdapterData(mFriendsList);
            mAdapter.notifyDataSetChanged();
        }
    }

    //搜索 根据某一字符匹配数据源
    public static List<Friend> searchItem(String name, List<Friend> friendslist) {
        List<Friend> searchList = new ArrayList<Friend>();
        for (int i = 0; i < friendslist.size(); i++) {
            String s = friendslist.get(i).getNickname() + friendslist.get(i).getGroupname();
            int index = s.indexOf(name);
            // 存在匹配的数据
            if (index != -1) {
                searchList.add(friendslist.get(i));
            }
        }
        return searchList;
    }

    @Override
    public void onDestroy() {
        if (mBroadcastReciver != null) {
            getActivity().unregisterReceiver(mBroadcastReciver);
        }
        super.onDestroy();
    }


    @Override
    public void onFilterFinished() {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.getMenuInflater().inflate(R.menu.de_main_menu, popupMenu.getMenu());      // menu的item点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_item1:// 群发消息
                        startActivity(new Intent(getContext(), SendGroupActivity.class));
                        break;
                    case R.id.add_item2://发起群聊
                        Intent intent = new Intent(getContext(),GroupListActivity.class);
                        intent.putExtra("group",true);
                        startActivity(intent);
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    /**
     * 参数设置
     */
    public void setParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT,"0");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/zczw/iat.wav");
    }
}
