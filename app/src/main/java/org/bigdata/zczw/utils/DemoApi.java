package org.bigdata.zczw.utils;

/**
 * demo api 请求，需要设置cookie，否则会提示 “user not login” 此处是 Demo 的接口，跟融云 SDK
 * 没有关系，此处仅为示例代码，展示 App 的逻辑
 */
public class DemoApi {
//    public static String HOST = "http://192.168.0.114:9033/";
//    public static String IMAGE = "http://150od17078.51mypc.cn:8094/";

//    public static String HOST = "http://zczw.ewonline.org:8093/";

    public static String HOST = "http://192.168.1.99:9033/";

    public static String IMAGE = "http://zczwstorage.ewonline.org:8094/images/";

    public static  String DEMO_LOGIN_EMAIL = "sso/login";
    public static  String CHANGE_PWD = "user/change";
    public static  String GET_MESSAGE_LIST = "message/findV2";
//    public static  String GET_MESSAGE_HOT_LIST = "message/findHotList";
    public static  String GET_MESSAGE_HOT_LIST = "message/findHotListV2";
    public static  String GET_MESSAGE_BY_ID = "message/findById/";
    public static  String COLLECT_MESSAGE = "collect/";
    public static  String COLLECT_LIST = "collect/list";
    public static  String DEMO_LOGOUT = "sso/logout";
    public static  String DEl_MESSAGE = "message/del/";
    public static  String COLLECT_MESSAGE_LIST = "collect/my";
    public static  String COMEENT_BY_ID = "comment/list/";
    public static  String ADD_COMEENT = "comment/v3";
    public static  String REPLY_COMEENT = "comment/replyV2";

    public static  String GET_USERINFO = "user/info";
    public static  String MESSAGE_PRASIE ="praise/v2/";
    public static  String PRASIE_LIST ="praise/list";
    public static  String MY_FRIEND = "friend/my";
    public static  String VIDEO_URL = HOST+"message/addvideo";
    public static  String TAG_LABLE0 = "label/my";
    public static  String TAG_LABLE0_SAVE = "label/save";
    public static  String TAG_LABLE0_DELETE = "label/delete";
    public static  String TAG_LABLE0_UPDATE = "label/update";
    public static  String EXAM = "exam/list";
    public static  String GROUP_LIST ="group/list";
    public static  String GROUP_CREAT = "mygroup/create";
    public static  String GROUPINFOBYID = "group/find/";
    public static  String MY_GROUPINFO_BYID = "group/findmy/";
    public static  String MY_GROUP_DISMISS = "mygroup/dismiss";//解散
    public static  String MY_GROUP_JOIN = "mygroup/join";//添加
    public static  String MY_GROUP_QUIT = "mygroup/quit";//退出，删除
    public static  String MY_GROUP_NAME = "mygroup/refresh";//改名
    public static  String FIND_ME = "aboutme/find";//查询关于我的
    public static  String ZAN_ME = "aboutme/findpmlist";//查询关于我的点赞
    public static  String COMMENT_ME = "aboutme/findcmlist";//查询关于我的评论
    public static  String QUESTION_LIST = "qa/list";//常见问题列表
    public static  String AT_ME = "aboutme/findacmlist";//
    public static  String THEME_LIST = "theme/find";//
    public static  String THEME = "theme/findByThemeId?id=";//
    public static  String THEME_COM = "theme/comment/create";//
    public static  String THEME_FLAG = "theme/findFlag";//
    public static  String FIND_MSG = "message/findFyV2";//

    public static  String ABOUT = "about";//
    public static  String SHARE = "message/share";//分享

    public static  String DETAIL_LIST = "message/findByCreateUserId";//

    public static  String COM_ALL = "comment/listall";//
    public static  String COM_REPLY = "comment/reply";//
    public static  String COM_DEL = "comment/delete";//


    public static  String PAI_LIST = "pai/list";//获取随手拍列表
    public static  String PAI_DELETE = "pai/delete";//删除随手拍
    public static  String PAI_PUBLISH = "pai/publish";//发布随手拍

    public static  String BOX_MESSAGE_LIST = "message/boxMessageList";//新增消息列表
    public static  String BOX_MESSAGE_COUNT = "message/boxUnreadCount";//未读消息数量


    public static  String TOPIC_HISTORY = "message/historyTopic";//历史使用话题
    public static  String TOPIC_SEARCH = "message/topicSearch";//话题搜索
    public static  String TOPIC_LIST = "message/topicMessageList";//话题动态列表
    public static  String TOPIC_NUM = "message/messageAndReadNum";//话题动态列表帖子数和阅读数


    public static  String REGULATION_CATALOG_LIST = "regulation/catalogList";//获取目录列表（共两级）
    public static  String REGULATION_CATALOG_ALL = "regulation/allCatalog";//获取目录列表（共两级）
    public static  String REGULATION_LIST = "regulation/list";//获取目录列表（共两级）


    public static  String MESSAGE_TAG = "message/allTag";//获取所有标签



    public static  String NEED_SHOW_ATTEND = "attendance/needShowStatistics";//是否需要查看统计
    public static  String ATTEND_MARK = "attendanceV2/addRemark";//为签到添加备注
    public static  String ATTEND_MARK_LIST = "attendanceV2/remarkList";//备注列表


    public static  String CHECK_IS_ATTEND = "attendanceV2/isAttended";//是否签到

    public static  String CHECK_STATUS = "attendanceV2/currentStatus";//当前状态
    public static  String CHECK_LEAVE = "attendanceV2/leave";//请假
    public static  String CHECK_TIAOXIU = "attendanceV2/tiaoXiu";//调休
    public static  String CHECK_XIAOJIA = "attendanceV2/xiaoJia";//销假
    public static  String CHECK_ATTEND = "attendanceV2/attend";//签到
    public static  String CHECK_ATTEND_BY_USER = "attendanceV2/listByUserId";//查看某个用户的考勤

    public static  String CHECKS_LIST = "attendanceV2/updateHistory";//修改历史记录列表

    public static  String CHECK_MSG_COUNT = "attendanceV2/unreadMsgCount";//未读消息数量
    public static  String CHECK_MSG_LIST = "attendanceV2/messageList";//消息列表


    public static  String ATTEND_LIST_DATE = "attendanceV2/showStatisticsGroupByDate";//按日期查看统计信息
    public static  String ATTEND_LIST = "attendanceV2/showStatisticsGroupByUser";//按月查看统计信息

    public static  String EXAM_LIST = "/examPaper/my";//考试列表
    public static  String EXAM_INFO = "/examPaper/joinExam";//考试详情
    public static  String EXAM_COMPOSE = "/examPaper/answer";//提交答案
    public static  String EXAM_TONGJI = "/examPaper/joinPracticeExam";//参加测试

    public static  String INTEGRAL_STATUS = "/integral/currentStatus";//用户状态
    public static  String INTEGRAL_LIST = "/integral/getPastTimeCountRank";//积分排名
    public static  String INTEGRAL_INFO = "/integral/getIntegralDetail";//积分详情

}
