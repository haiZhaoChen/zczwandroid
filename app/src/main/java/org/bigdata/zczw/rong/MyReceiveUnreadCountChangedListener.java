package org.bigdata.zczw.rong;

import android.content.Context;
import android.view.View;

import org.bigdata.zczw.ui.BadgeView;

import io.rong.imkit.manager.IUnReadMessageObserver;

/**
 * Created by Administrator on 2016-4-20.
 */
public class MyReceiveUnreadCountChangedListener implements IUnReadMessageObserver {
    private BadgeView badge;
    private View target;
    private Context context;

    public MyReceiveUnreadCountChangedListener(BadgeView badge,View target, Context context) {
        this.badge = badge;
        this.target = target;
        this.context = context;
    }


    @Override
    public void onCountChanged(int count) {
        if (count > 0) {
            if (badge == null || !badge.isShown()) {
                badge = new BadgeView(context, target);//三个参数，1、this,2、当前控件（tab）3、在第几个控件上实现数字提醒
                badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
                badge.setText(count + "");
                badge.show();
            } else {
                badge.setText(count + "");
            }
        }else{
            if (badge == null || !badge.isShown()) {
                badge = new BadgeView(context, target);//三个参数，1、this,2、当前控件（tab）3、在第几个控件上实现数字提醒
                badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
            }
            badge.setVisibility(View.GONE);
        }
    }
}
