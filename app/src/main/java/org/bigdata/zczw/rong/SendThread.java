package org.bigdata.zczw.rong;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.ImageMessage;

/**
 * Created by darg on 2016/9/20.
 */
public class SendThread extends Thread {
    private ArrayList<Message> msgList;
    private HashMap<String,String> userSelect;//群发用户list

    public SendThread(ArrayList<Message> msgList,HashMap<String,String> userSelect){
        this.msgList = msgList;
        this.userSelect = userSelect;
    }

    @Override
    public void run() {
        super.run();

        for (int i = 0; i < msgList.size(); i++) {
            synchronized (this) {
                Message message = msgList.get(i);
                if (message.getContent() instanceof ImageMessage) {
                    for (String key : userSelect.keySet()) {
                        sendImg(key, message);
                        try {//发送之间延迟300ms
                            sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }else{
                    for (String key : userSelect.keySet()) {
                        sendMsg(key, message);
                        try {//发送之间延迟300ms
                            sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void sendMsg(String userId, Message message) {
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().sendMessage(io.rong.imlib.model.Conversation.ConversationType.PRIVATE,
                    userId,
                    message.getContent(),
                    "",
                    "",
                    new RongIMClient.SendMessageCallback() {
                        @Override
                        public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {

                        }

                        @Override
                        public void onSuccess(Integer integer) {

                        }
                    },
                    new RongIMClient.ResultCallback<Message>() {
                        @Override
                        public void onSuccess(Message message) {

                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
        }
    }

    private void sendImg(String userId, Message message){
        if (RongIM.getInstance() != null) {
            RongIM.getInstance().sendImageMessage(Conversation.ConversationType.PRIVATE,
                    userId,
                    message.getContent(),
                    null,
                    null,
                    new RongIMClient.SendImageMessageCallback() {
                        @Override
                        public void onAttached(Message message) {

                        }

                        @Override
                        public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                            Log.e("1111", "onError: " + errorCode.getValue());
                        }

                        @Override
                        public void onSuccess(Message message) {

                        }

                        @Override
                        public void onProgress(Message message, int i) {

                        }
                    });
        }
    }
}
