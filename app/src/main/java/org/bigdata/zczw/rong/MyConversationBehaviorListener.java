package org.bigdata.zczw.rong;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import org.bigdata.zczw.App;
import org.bigdata.zczw.activity.PersonalActivity;
import org.bigdata.zczw.activity.UserInfoActivity;
import org.bigdata.zczw.utils.SPUtil;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;

/**
 * 对消息进行处理
 */
public class MyConversationBehaviorListener implements RongIM.ConversationBehaviorListener {

    /**
     * 当点击用户头像后执行。
     *
     * @param context           上下文。
     * @param conversationType  会话类型。
     * @param userInfo          被点击的用户的信息。
     * @return 如果用户自己处理了点击后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
     */
    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        if (SPUtil.getString(context, App.USER_ID).equals(userInfo.getUserId())) {
            context.startActivity(new Intent(context, UserInfoActivity.class));
        }else {
            Intent authorIntent = new Intent(context, PersonalActivity.class);
            authorIntent.putExtra("PERSONAL",userInfo.getUserId());
            authorIntent.putExtra("tag",1);//1聊天 0其他
            if (conversationType  == Conversation.ConversationType.GROUP) {
                authorIntent.putExtra("type",1);//1群聊 2 私聊
            }else {
                authorIntent.putExtra("type",2);//1群聊 2 私聊
            }
            context.startActivity(authorIntent);
        }
        return true;
    }

    /**
     * 当长按用户头像后执行。
     *
     * @param context          上下文。
     * @param conversationType 会话类型。
     * @param userInfo         被点击的用户的信息。
     * @return 如果用户自己处理了点击后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
     */
    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    /**
     * 当点击消息时执行。
     *
     * @param context 上下文。
     * @param view    触发点击的 View。
     * @param message 被点击的消息的实体信息。
     * @return 如果用户自己处理了点击后的逻辑，则返回 true， 否则返回 false, false 走融云默认处理方式。
     */
    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        if (message.getContent() instanceof ImageMessage) {
            Intent intent = new Intent(context, PicActivity.class);
            Uri uri = ((ImageMessage) message.getContent()).getRemoteUri();
            intent.putExtra("uri",uri);
            context.startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * 当长按消息时执行。
     *
     * @param context 上下文。
     * @param view    触发点击的 View。
     * @param message 被长按的消息的实体信息。
     * @return 如果用户自己处理了长按后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
     */
    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        return false;
    }
    /**
     * 当点击链接消息时执行。
     *
     * @param context 上下文。
     * @param link    被点击的链接。
     * @return 如果用户自己处理了点击后的逻辑处理，则返回 true， 否则返回 false, false 走融云默认处理方式。
     */
    @Override
    public boolean onMessageLinkClick(Context context, String link) {
        return false;
    }

}
