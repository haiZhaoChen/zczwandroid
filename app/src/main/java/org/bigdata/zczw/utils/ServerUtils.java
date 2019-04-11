package org.bigdata.zczw.utils;


import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.apache.http.message.BasicHeader;
import org.bigdata.zczw.App;


public class ServerUtils {




    /**
     * 通过get方式 获取服务端数据
     * @param url
     * @param requestCallBack
     */
    public static void getServerDatasGet(String url,RequestCallBack<String> requestCallBack) {
        //清除缓存 默认60秒内提交返回上次成功的结果。
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存5秒,5秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(HttpRequest.HttpMethod.GET, url, params, requestCallBack);
    }

    public static void getServerDatasGet(StringCallback callback){
        String url = DemoApi.HOST + DemoApi.GET_USERINFO + "";
        OkHttpUtils.get()
                .url(url)
                .addHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN)
                .build()
                .execute(callback);
    }



    /**
     * 通过post方式 获取服务端数据
     * @param url
     * @param params
     * @param requestCallBack
     */
    public static void getServerDatasPost(String url,RequestParams params,RequestCallBack<String> requestCallBack) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.POST, url, params, requestCallBack);
    }

    public static void getServerDatasPost(String userName , String pwd, StringCallback callback){
        String url = DemoApi.HOST + DemoApi.DEMO_LOGIN_EMAIL;
        OkHttpUtils.post()
                .url(url)
                .addParams("username", userName)
                .addParams("password", pwd)
                .build()
                .execute(callback);
    }

//    public static void mGetMessageHotList( String id ,String pageSize , String type,String sameUnit,String publicScope,StringCallback requestCallBack) {
//        String url =  DemoApi.HOST + DemoApi.GET_MESSAGE_HOT_LIST ;
//        OkHttpUtils.get()
//                .url(url)
//                .addHeader("Cookie", "zw_token="+ App.ZCZW_TOKEN)
//                .addParams("id", id)
//                .addParams("pageSize", pageSize)
//                .addParams("type", type)
//                .addParams("sameUnit", sameUnit)
//                .addParams("publicScope", publicScope)
//                .build()
//                .execute(requestCallBack);
//    }

    /**
     * 修改密码接口
     * @param name  用户名:电话号码
     * @param pwd
     * @param newPwd
     * @param requestCallBack
     */
    public static void changePwd( String name,String pwd,String newPwd,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.CHANGE_PWD;
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("name", name);
        requestParams.addBodyParameter("pwd", pwd);
        requestParams.addBodyParameter("newPwd", newPwd);
        requestParams.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        getServerDatasPost(url, requestParams, requestCallBack);
    }

    /**
     * 初始化动态列表
     * */
    public static void getMessageListFirst(String tagId,String sameUnit,String publicScope, RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.GET_MESSAGE_LIST ;
//        String url = "http://192.168.0.103:9033/" + DemoApi.GET_MESSAGE_LIST ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("pageSize", "10");
        params.addQueryStringParameter("type", "1");
        params.addQueryStringParameter("sameUnit", sameUnit);
        params.addQueryStringParameter("tagId", tagId);
        params.addQueryStringParameter("publicScope", publicScope);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url, params, requestCallBack);
    }

    /**
     * 获取动态列表
     * @param requestCallBack
     */
    public static void mGetMessageList( String tagId,String id ,String pageSize , String type,String sameUnit,String publicScope,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.GET_MESSAGE_LIST ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("tagId", tagId);
        params.addQueryStringParameter("pageSize", pageSize);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("sameUnit", sameUnit);
        params.addQueryStringParameter("publicScope", publicScope);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }
 /**
     * 获取动态列表
     * @param requestCallBack
     */
    public static void mGetMessageHotList( String id ,String pageSize , String type,String sameUnit,String publicScope,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.GET_MESSAGE_HOT_LIST ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("pageSize", pageSize);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("sameUnit", sameUnit);
        params.addQueryStringParameter("publicScope", publicScope);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }


//    public static void mGetMessageHotList( String id ,String pageSize , String type,String sameUnit,String publicScope,StringCallback requestCallBack) {
//        String url =  DemoApi.HOST + DemoApi.GET_MESSAGE_HOT_LIST ;
//        OkHttpUtils.get()
//                .url(url)
//                .addHeader("Cookie", "zw_token="+ App.ZCZW_TOKEN)
//                .addParams("id", id)
//                .addParams("pageSize", pageSize)
//                .addParams("type", type)
//                .addParams("sameUnit", sameUnit)
//                .addParams("publicScope", publicScope)
//                .build()
//                .execute(requestCallBack);
//    }


    /**
     * 4.6.模糊查询动态
     * id//动态id
     * type//0为下拉刷新，1为上拉加载更多，默认为0
     * pageSize//每页显示数量（默认为20）
     * svalue//模糊条件
     * @param requestCallBack
     */
    public static void mFindMessageList( String id ,String type ,String pageSize , String svalue,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.FIND_MSG+"?id="+id+"&type="+type+"&pageSize="+pageSize+"&svalue="+svalue+"&sameUnit=" ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 个人收藏动态列表
     * @param requestCallBack
     */

    public static void getCollectList( String id ,String pageSize , String type,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.COLLECT_MESSAGE_LIST ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("pageSize",pageSize);
        params.addQueryStringParameter("type",type);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 模糊查询自己的好友列表
     * @param userId
     * @param type
     * @param userName
     * @param requestCallBack
     */
    public static void mFirends( String userId ,int type ,String userName,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.MY_FRIEND ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userId",userId);
        params.addQueryStringParameter("type", type + "");
        params.addQueryStringParameter("userName",userName);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    public static void mFirends(String userId ,int type ,String userName ,StringCallback callback){
        String url = DemoApi.HOST + DemoApi.MY_FRIEND;
        OkHttpUtils.get()
                .url(url)
                .addHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN)
                .addParams("userId",userId)
                .addParams("type", type + "")
                .addParams("userName",userName)
                .build()
                .execute(callback);
    }

    /**
     * 获取个人圈子
     *  0获取所有群组列表，
     *  1获取当前登录用户所在群组的列表，
     *  2 获取非当前登录用户所在的群组列表
     */
    public static void getMyCircle(int type,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.GROUP_LIST ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("type", type + "");
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url, params, requestCallBack);
    }

    /**
     * 根据id获取群成员信息
     * @param id
     * @param requestCallBack
     */
    public static void mGroupInfoById( String id ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.GROUPINFOBYID +id;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" +App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url, params, requestCallBack);
    }
    /**
     * 根据id获取群成员信息
     * @param id
     * @param requestCallBack
     */
    public static void myGroupInfos( String id ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.MY_GROUPINFO_BYID +id;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" +App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url, params, requestCallBack);
    }

    /**
     * 某条动态消息的评论列表
     * @param id
     * @param requestCallBack
     */
    public static void getCommentList( String id ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.COMEENT_BY_ID+"?messageId="+id+"&pageNum="+1+"&pageSize="+200;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
//        params.addQueryStringParameter("messageId",id);
//        params.addQueryStringParameter("pageNum",1);
//        params.addQueryStringParameter("pageSize",id);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 某条动态消息的评论列表
     * 参数说明:Long messageId//动态消息id
     * pageNum//当前页码（起始页为1 默认为1）
     * pageSize//每页显示数量（默认为20）

     */
    public static void getCommentList( String messageId ,int pageNum ,int pageSize ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.COM_ALL+"?messageId="+messageId+"&pageNum="+pageNum+"&pageSize="+pageSize;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }


    /**
     * 增加评论
     * @param oMId  原始动态消息id
     * @param cMId  被评论消息的id
     * @param content 评论内容
     *                String userIds// 需要@用户的id数组，以“/”分隔 例如 1/2/3
    String rangeStr // @信息的位置 ，以“/”分隔
     * @param requestCallBack
     */
    public static void addComment( String oMId ,String cMId ,String content ,String userIds,String rangeStr,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.ADD_COMEENT;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addBodyParameter("oMId", oMId);
        params.addBodyParameter("cMId", cMId);
        params.addBodyParameter("userIds", userIds);
        params.addBodyParameter("content", content);
        params.addBodyParameter("rangeStr", rangeStr);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(HttpRequest.HttpMethod.POST, url,params, requestCallBack);
    }

    /**
     * 回复评论
     * Long oMId//动态消息id
     *  Long rPId//某一评论下回复的根评论id
     *  Long PcId//被回复的评论id
     *  String content//回复内容
     */
    public static void replyComment( String oMId ,String rPId ,String PcId , String content ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.REPLY_COMEENT;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addBodyParameter("oMId", oMId);
        params.addBodyParameter("rPId", rPId);
        params.addBodyParameter("PcId", PcId);
        params.addBodyParameter("content", content);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(HttpRequest.HttpMethod.POST, url,params, requestCallBack);
    }

    /**
     * 收藏动态
     * @param id
     * @param requestCallBack
     */
    public static void collectMessage(long id, int type ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.COLLECT_MESSAGE + id +"/"+type;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 删除动态
     * @param id
     * @param requestCallBack
     */
    public static void deleteMessage(long id,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.DEl_MESSAGE + id ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 点赞接口
     * @param messageId
     * @param type
     * @param requestCallBack
     */
    public static void prasieMessage( long messageId ,int type ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.MESSAGE_PRASIE +messageId +"/" +type;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 点赞列表人员
     * @param messageId
     * @param requestCallBack
     */
    public static void prasieUserList( long messageId ,int pageNum,int pageSize ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.PRASIE_LIST+"?messageId="+messageId+"&pageNum="+pageNum+"&pageSize="+pageSize;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 收藏列表人员
     * @param messageId
     * @param requestCallBack
     */
    public static void collectUserList( long messageId ,int pageNum,int pageSize ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.COLLECT_LIST+"?messageId="+messageId+"&pageNum="+pageNum+"&pageSize="+pageSize;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 标签列表
     */
    public static void getTagLabel(RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.TAG_LABLE0;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }
    /**
     * 创建标签
     * String  userIds  该标签下的人员  （1/2/3）
     * String labelName标签名称
     */
    public static void createTagLabel(String userIds,String labelName,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.TAG_LABLE0_SAVE;
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("labelName", labelName);
        requestParams.addBodyParameter("userIds", userIds);
        requestParams.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(HttpRequest.HttpMethod.POST, url,requestParams, requestCallBack);
    }
    /**
     * 删除标签
     */
    public static void deleteTagLabel(int labelId,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.TAG_LABLE0_DELETE+"?labelId="+labelId;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }
    /**
     * 修改标签
     * int labelId//标签id
     * String labelName标签名称
     * String  userIds  该标签下的人员  （1/2/3）
     */
    public static void changeTagLabel(int labelId,String labelName,String userIds,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.TAG_LABLE0_UPDATE;
        HttpUtils httpUtils = new HttpUtils();

        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("labelId", labelId + "");
        requestParams.addBodyParameter("labelName", labelName);
        requestParams.addBodyParameter("userIds", userIds);
        requestParams.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(HttpRequest.HttpMethod.POST, url,requestParams, requestCallBack);
    }

    /**
     * 获取考试列表
     */
    public static void getExamList(RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.EXAM;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 创建群组
     * String  userIds  该标签下的人员  （1/2/3）
     * String labelName标签名称
     */
    public static void createGroup(String userIds,String name,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.GROUP_CREAT;
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("name", name);
        requestParams.addBodyParameter("userIds", userIds);
        requestParams.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(HttpRequest.HttpMethod.POST, url,requestParams, requestCallBack);
    }

    /**
     * 解散
     *groupId
     */
    public static void myGroupDismiss(String groupId,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.MY_GROUP_DISMISS;
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("groupId", groupId);
        requestParams.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(HttpRequest.HttpMethod.POST, url,requestParams, requestCallBack);
    }

    /**
     * 添加成员
     * String groupId//群id
     * String name//群名称
     * String userIds//需要继续加的群成员 例：1/2/3
     */
    public static void myGroupJoin(String groupId,String name,String userIds,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.MY_GROUP_JOIN;
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("groupId", groupId);
        requestParams.addBodyParameter("name", name);
        requestParams.addBodyParameter("userIds", userIds);
        requestParams.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(HttpRequest.HttpMethod.POST, url,requestParams, requestCallBack);
    }
    /**
     * 退群成员
     * String groupId//群id
     * String name//群名称
     * String userIds//需要继续加的群成员 例：1/2/3
     */
    public static void myGroupQuit(String groupId,String name,String userIds,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.MY_GROUP_QUIT;
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("groupId", groupId);
        requestParams.addBodyParameter("name", name);
        requestParams.addBodyParameter("userIds", userIds);
        requestParams.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(HttpRequest.HttpMethod.POST, url,requestParams, requestCallBack);
    }

    /**
     * 修改群名称
     * String groupId//群id
     * String name//群名称
     */
    public static void myGroupNAME(String groupId,String name,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.MY_GROUP_NAME;
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = new RequestParams();
        requestParams.addBodyParameter("groupId", groupId);
        requestParams.addBodyParameter("name", name);
        requestParams.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(HttpRequest.HttpMethod.POST, url,requestParams, requestCallBack);
    }

    /**
     * 查询关于我的点赞
     * id//点赞id，如果是下拉刷新就是最后发表的那条点赞的id，如果是上拉加载更多则为当前页面中最先发表的那条点赞的id，如果没此参数则加载最新发表的n条动态。
     * type//0为下拉刷新，1为上拉加载更多，默认为0
     * pageSize//每页显示数量（默认为20）
     */
    public static void getZanList(String id,String type,String pageSize,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.ZAN_ME+"?id="+id+"&type="+type+"&pageSize="+pageSize;
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = new RequestParams();
        requestParams.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(HttpRequest.HttpMethod.GET, url,requestParams, requestCallBack);
    }
/**
     * 查询关于我的评论
     * id//点赞id，如果是下拉刷新就是最后发表的那条点赞的id，如果是上拉加载更多则为当前页面中最先发表的那条点赞的id，如果没此参数则加载最新发表的n条动态。
     * type//0为下拉刷新，1为上拉加载更多，默认为0
     * pageSize//每页显示数量（默认为20）
     */
    public static void getComList(String id,String type,String pageSize,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.COMMENT_ME+"?id="+id+"&type="+type+"&pageSize="+pageSize;
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = new RequestParams();
        requestParams.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(HttpRequest.HttpMethod.GET, url,requestParams, requestCallBack);
    }

    /**
     * 常见问题列表
     */
    public static void getQuestionList(RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.QUESTION_LIST;
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = new RequestParams();
        requestParams.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(HttpRequest.HttpMethod.GET, url,requestParams, requestCallBack);
    }

    /**
     * 常见问题列表
     */
    public static void getMsgById(String id,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.GET_MESSAGE_BY_ID+id;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存5秒,5秒内直接返回上次成功请求的结果。
        RequestParams requestParams = new RequestParams();
        requestParams.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(HttpRequest.HttpMethod.GET, url,requestParams, requestCallBack);
    }

    /**
     * 常见问题列表
     */
    public static void getTrendNum(RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.FIND_ME;
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = new RequestParams();
        requestParams.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(HttpRequest.HttpMethod.GET, url,requestParams, requestCallBack);
    }

    /**
     * 常见问题列表
     */
    public static void getAtMe(String id,String type,String pageSize,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.AT_ME+"?id="+id+"&type="+type+"&pageSize="+pageSize;
        HttpUtils httpUtils = new HttpUtils();
        RequestParams requestParams = new RequestParams();
        requestParams.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(HttpRequest.HttpMethod.GET, url,requestParams, requestCallBack);
    }


    /**
     * 查询主题列表
     * @param requestCallBack
     */
    public static void getThemeList( String id ,String pageSize , String type,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.THEME_LIST+"?id="+id+"&type="+type+"&pageSize="+pageSize ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 查询主题列表
     * @param requestCallBack
     */
    public static void getTheme( String id ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.THEME + id ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 评论主题列表
     *
     * @param requestCallBack
     */
    public static void getThemeCom( String id,String content ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.THEME_COM ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addBodyParameter("id", id);
        params.addBodyParameter("content", content);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(HttpRequest.HttpMethod.POST, url,params, requestCallBack);
    }
    /**
     * 13.4.查询主题状态标志
     *
     * @param requestCallBack
     */
    public static void getThemeFlag( RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.THEME_FLAG ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 13.4.查询主题状态标志
     *
     * @param requestCallBack
     */
    public static void getAppAbout( RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.ABOUT ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 获取动态列表
     * createUserId 用户编号
     * messageId 动态编号
     * type 上拉刷新（0）、下拉加载（1）更多
     * word 查询字符串
     */
    public static void mGetDetailList( String createUserId , String messageId , String type , String word ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.DETAIL_LIST ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("createUserId", createUserId);
        params.addQueryStringParameter("messageId", messageId);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("word", word);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 评论再评论
     * 参数说明:Long oMId//动态消息id
     * Long cMId//评论id
     * Long PcId//被回复的消息id（回复的哪条消息的id）
     * String content//回复内容
     */
    public static void comReply( long oMId , long cMId ,long PcId , String content ,RequestCallBack<String> requestCallBack) {
//        http://192.168.1.126:8081/comment/reply?oMId=1477313481574687893&rPId=1506675950291323706&PcId=1506675950291323706&content=huifu2
        String url = DemoApi.HOST + DemoApi.COM_REPLY ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("oMId", oMId+"");
        params.addQueryStringParameter("cMId", cMId+"");
        params.addQueryStringParameter("PcId", PcId+"");
        params.addQueryStringParameter("content", content);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(HttpRequest.HttpMethod.POST, url,params, requestCallBack);
    }


    /**
     * 评论删除
     * commentsId	Long	评论或回复编号
     */
    public static void delcm( String commentsId ,RequestCallBack<String> requestCallBack) {
//        http://192.168.1.126:8081/comment/reply?oMId=1477313481574687893&rPId=1506675950291323706&PcId=1506675950291323706&content=huifu2
        String url = DemoApi.HOST + DemoApi.COM_DEL ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("commentsId", commentsId);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }




    /**
     * 获取随手拍列表
     * paiId	Long	随手拍编号
     * returnNew	boolean	true：返回新数据；
     *                      false：返回旧数据
     * category	Integer	类别：查询全部时，传空值
     * tag	Integer	标签：查询全部时，传空值
     */
    public static void getPaiList( String paiId ,String returnNew , String workType, String category,String tag,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.PAI_LIST ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("paiId", paiId);
        params.addQueryStringParameter("returnNew", returnNew);
        params.addQueryStringParameter("category", category);
        params.addQueryStringParameter("workType", workType);
        params.addQueryStringParameter("tag", tag);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 获取随手拍列表
     * paiId	Long	随手拍编号
     * returnNew	boolean	true：返回新数据；
     *                      false：返回旧数据
     * category	Integer	类别：查询全部时，传空值
     * tag	Integer	标签：查询全部时，传空值
     */
    public static void delPai( String id , RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.PAI_DELETE ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("id", id);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 当前状态
     */
    public static void checkStatus(RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.CHECK_STATUS ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 是否已经录入过考勤
     * 参数名称	参数类型	说明
     * attendDate	String	日期yyyy-MM-dd
     */
    public static void isAttend(String attendDate,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.CHECK_IS_ATTEND ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addBodyParameter("attendDate",attendDate);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 签到
     * 参数名称	参数类型	说明
     * attendType	Integer	类型
     * attendDate	Date	日期yyyy-MM-dd
     * otherTypeName	String	其他类型名称
     * attendSubType	Integer	默认为0
     *                          夜班人员在岗时：中班：1
     *                          白班：2
     *                          夜班：3
     *                          夜班/白班：4
     *                          所有人请假时：“事假1”“病假2”“年休假3”“婚假4”“预产假5”“产假6”“陪产假7”“哺乳假8”“延长哺乳假9”“丧假10”“工伤11”
     * latitude	String	纬度
     * longitude	String	经度
     * locationName	String	位置名称
     */
    public static void attend( int attendType , String otherTypeName , int attendSubType , String attendDate ,
                               double latitude , double longitude , String locationName
            ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.CHECK_ATTEND ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("attendType", attendType+"");
        params.addQueryStringParameter("otherTypeName", otherTypeName);
        params.addQueryStringParameter("attendDate", attendDate);
        params.addQueryStringParameter("attendSubType", attendSubType+"");
        params.addQueryStringParameter("latitude", latitude+"");
        params.addQueryStringParameter("longitude", longitude+"");
        params.addQueryStringParameter("locationName", locationName);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(HttpRequest.HttpMethod.POST, url,params, requestCallBack);
    }

    /**
     *请假
     * 参数名称	参数类型	说明
     * days	Integer	天数
     * beginDate	String	开始日期yyyy-MM-dd
     * attendSubType	Integer	请假类型
     */
    public static void leave(String days,String beginDate,int attendSubType,double latitude , double longitude , String locationName,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.CHECK_LEAVE ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addBodyParameter("days",days);
        params.addBodyParameter("beginDate",beginDate);
        params.addBodyParameter("attendSubType",attendSubType+"");
        params.addBodyParameter("latitude", latitude+"");
        params.addBodyParameter("longitude", longitude+"");
        params.addBodyParameter("locationName", locationName);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.POST, url,params, requestCallBack);
    }

    /**
     * 调休
     * 参数名称	参数类型	说明
     * days	Integer	天数
     * beginDate	String	开始日期yyyy-MM-dd
     * overtimeBeginDate	String	加班开始日期yyyy-MM-dd
     * overtimeEndDate	String	加班结束日期yyyy-MM-dd
     */
    public static void attendTiaoXiu(String days,String beginDate,String overtimeBeginDate,String overtimeEndDate,double latitude , double longitude , String locationName,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.CHECK_TIAOXIU ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addBodyParameter("days",days);
        params.addBodyParameter("beginDate",beginDate);
        params.addBodyParameter("overtimeBeginDate",overtimeBeginDate);
        params.addBodyParameter("overtimeEndDate",overtimeEndDate);
        params.addBodyParameter("latitude", latitude+"");
        params.addBodyParameter("longitude", longitude+"");
        params.addBodyParameter("locationName", locationName);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.POST, url,params, requestCallBack);
    }

    /**
     *销假
     */
    public static void checkXiaoJia(RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.CHECK_XIAOJIA ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(HttpRequest.HttpMethod.POST, url,params, requestCallBack);
    }

    /**
     *  修改历史记录列表
     *  attendDate	String	签到日期
     *  userId	Long	用户编号
     */
    public static void checksList(String userId,String attendDate,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.CHECKS_LIST ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userId", userId);
        params.addQueryStringParameter("attendDate", attendDate);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 是否需要查看统计
     */
    public static void needShowAttend(RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.NEED_SHOW_ATTEND;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 查看某个用户的考勤
     * 参数名称	参数类型	说明
     * userId	Long	用户编号
     * monthDate	String	月份yyyy-MM
     */
    public static void attendUser(String userId , String monthDate ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.CHECK_ATTEND_BY_USER;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("userId", userId);
        params.addQueryStringParameter("monthDate", monthDate);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 按日期查看统计信息
     * date	String	日期  yyyy-MM-dd格式
     */
    public static void attendDayList(String date ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.ATTEND_LIST_DATE;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("date", date);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 按月查看统计信息
     * monthDate	String	月份  yyyy-MM格式
     */
    public static void attendMonthList(String monthDate ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.ATTEND_LIST;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("monthDate", monthDate);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }


    /**
     * 查看备注列表
     * attendDate	String	备注
     * userId	Long	用户编号
     */
    public static void attendRemarkList(String userId ,String attendDate ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.ATTEND_MARK_LIST;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("attendDate", attendDate);
        params.addQueryStringParameter("userId", userId);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 为签到添加备注
     * 请求地址	post   /attendanceV2/addRemark
     * 参数名称	参数类型	说明
     * attendDate	String	签到日期
     * remark	String	文字说明
     */
    public static void attendRemark(String attendDate , String remark ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.ATTEND_MARK ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("attendDate", attendDate);
        params.addQueryStringParameter("remark", remark);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(HttpRequest.HttpMethod.POST, url,params, requestCallBack);
    }

    /**
     * 未读消息数量
     */
    public static void attendMsgCount(RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.CHECK_MSG_COUNT ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }


    /**
     * 消息列表
     * 参数名称	参数类型	说明
     * sourceType	Integer	类型1：请假2：销假3：调
     * id	Integer	消息id
     * returnNew	Boolean	是否返回新数据
     */
    public static void attendMsgList(int sourceType , String id ,String returnNew ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.CHECK_MSG_LIST ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("sourceType", sourceType+"");
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("returnNew", returnNew);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }


    /**
     * 动态分享
     * messageId
     */
    public static void getShareUrl(String messageId ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.SHARE;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("messageId", messageId);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }


    /**
     * 未读消息数量
     */
    public static void getBoxUnreadCount(RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.BOX_MESSAGE_COUNT;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }


    /**
     * 新增消息列表
     * id	Long	编号，用于分页对应MessageBox.id
     * returnNew	boolean	是否返回新数据
     */
    public static void getBoxMessageList(String id ,String returnNew ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.BOX_MESSAGE_LIST;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("returnNew", returnNew);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 历史使用话题
     */
    public static void getTopicHistory(RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.TOPIC_HISTORY;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 话题搜索
     * name	String	话题名称
     */
    public static void getTopicSearch(String name ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.TOPIC_SEARCH;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("name", name);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 话题动态列表
     * topicId	Long	话题id
     * id	Long	编号，用于分页对应MessageInfo.id
     * type	int	0为下拉刷新，1为上拉加载更多，默认为0
     * pageSize	int	每页显示数量（默认为20）
     * publicScope	int	0:未知; 1:特定; 5:部门; 10:全部，默认0     */
    public static void getTopicList(String topicId ,String topicName ,String id ,String type ,String pageSize ,String publicScope ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.TOPIC_LIST;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("topicId", topicId);
        params.addQueryStringParameter("topicName", topicName);
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("pageSize", pageSize);
        params.addQueryStringParameter("publicScope", publicScope);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 话题动态列表帖子数和阅读数
     * topicName	Long	话题id
     */
    public static void getTopicNum(String topicName ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.TOPIC_NUM;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("topicName", topicName);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 获取所有目录列表（共两级）
     */
    public static void getAllCatalog(RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.REGULATION_CATALOG_ALL;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }
    /**
     * 规章制度列表
     */
    public static void getRegulation(String catalogId ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.REGULATION_LIST;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("catalogId", catalogId);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }
    /**
     * 获取目录列表（共两级）
     */
    public static void getCatalog(String parentCatalogId ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.REGULATION_CATALOG_LIST;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("parentCatalogId", parentCatalogId);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }
    /**
     * 获取所有标签
     */
    public static void getMsgTag(RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.MESSAGE_TAG;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }
    /**
     * 阅读加分
     */
    public static void msgRead(String messageId,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.MSG_READ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("messageId",messageId);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(HttpRequest.HttpMethod.POST, url,params, requestCallBack);
    }


    /**
     * 考试列表
     *
      */
    public static void getExamPageList(RequestCallBack<String> requestCallBack){

        String url = DemoApi.HOST + DemoApi.EXAM_LIST;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }
    /**
     * 考试历史
     *
     */
    public static void getExamHistoryList(RequestCallBack<String> requestCallBack){

        String url = DemoApi.HOST + DemoApi.EXAM_HISTORY;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 获取考试试题
     *
     */
    public static void getExamPageQues(String examId,RequestCallBack<String> requestCallBack){

        String url = DemoApi.HOST + DemoApi.EXAM_INFO;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("examPaperId",examId);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 参加测试
     *
     */
    public static void getTestExamPageQues(RequestCallBack<String> requestCallBack){

        String url = DemoApi.HOST + DemoApi.EXAM_TONGJI;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }


    /**
     * 提交考试答案
     *
     */
    public static void submitAnswer(String examPaperId,String questionId, String answers,RequestCallBack<String> requestCallBack){

        String url = DemoApi.HOST + DemoApi.EXAM_COMPOSE;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("examPaperId",examPaperId);
        params.addQueryStringParameter("questionId", questionId);
        params.addQueryStringParameter("answers",answers);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 获取积分列表
     * rankType
     * 1：30天内排名
     * 2：从年初开始排名
     * type 0：公共
     * 1：养护
     * 2：收费
     * 3：机电
     * 4：信调
     * 5: 机关科室
     * 6：处属单位
     */
    public static void getScoreList(String rankType ,String type,String rankId ,RequestCallBack<String> requestCallBack) {
        String url = DemoApi.HOST + DemoApi.INTEGRAL_LIST ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0); // 设置缓存0秒,0秒内直接返回上次成功请求的结果。
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("rankType", rankType);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("rankId",rankId);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 获取用户状态，是否显示专业选项
     * @param requestCallBack
     */
    public static void getIntegralStatus(RequestCallBack<String> requestCallBack){
        String url = DemoApi.HOST + DemoApi.INTEGRAL_STATUS ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0);
        RequestParams params = new RequestParams();
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 积分详情部分
     * @param integralId
     * @param returnNew
     * @param requestCallBack
     */
    public static void getIntegralInfo(String integralId ,Boolean returnNew ,String type,RequestCallBack<String> requestCallBack){
        String url = DemoApi.HOST + DemoApi.INTEGRAL_INFO ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("integralId", integralId);
        params.addQueryStringParameter("returnNew", returnNew.toString());
        params.addQueryStringParameter("type",type);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url,params, requestCallBack);
    }

    /**
     * 评论点赞
     */
    public static void sendCommentPraise(String commentId ,String type,RequestCallBack<String> requestCallBack){
        String url = DemoApi.HOST + DemoApi.COMMENT_PRAISE ;
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.configCurrentHttpCacheExpiry(0);
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("commentId", commentId);
        params.addQueryStringParameter("type", type);
        params.setHeader(new BasicHeader("Cookie", "zw_token=" + App.ZCZW_TOKEN));
        httpUtils.send(HttpRequest.HttpMethod.POST, url,params, requestCallBack);
    }



}

