/*
 * //
 * //   ______     ______     __  __
 * //  /\  ___\   /\  ___\   /\_\_\_\
 * //  \ \  __\   \ \ \____  \/_/\_\/_
 * //   \ \_____\  \ \_____\   /\_\/\_\
 * //    \/_____/   \/_____/   \/_/\/_/
 * //
 * //  英斯特哈博（北京）科技有限公司
 * //
 * // -----------------------------------------------------------------------------
 * //
 * //  一、协议的许可和权利
 * //
 * //   1. 您可以在完全遵守本协议的基础上，将本软件应用于商业用途；
 * //   2. 您可以在协议规定的约束和限制范围内修改本产品源代码或界面风格以适应您的要求；
 * //   3. 您拥有使用本产品中的全部内容资料、商品信息及其他信息的所有权，并独立承担与其内容相关的
 * //      法律义务；
 * //   4. 获得商业授权之后，您可以将本软件应用于商业用途，自授权时刻起，在技术支持期限内拥有通过
 * //      指定的方式获得指定范围内的技术支持服务；
 * //
 * //  二、协议的约束和限制
 * //
 * //   1. 未获商业授权之前，禁止将本软件用于商业用途（包括但不限于企业法人经营的产品、经营性产品
 * //      以及以盈利为目的或实现盈利产品）；
 * //   2. 未获商业授权之前，禁止在本产品的整体或在任何部分基础上发展任何派生版本、修改版本或第三
 * //      方版本用于重新开发；
 * //   3. 如果您未能遵守本协议的条款，您的授权将被终止，所被许可的权利将被收回并承担相应法律责任；
 * //
 * //  三、有限担保和免责声明
 * //
 * //   1. 本软件及所附带的文件是作为不提供任何明确的或隐含的赔偿或担保的形式提供的；
 * //   2. 用户出于自愿而使用本软件，您必须了解使用本软件的风险，在尚未获得商业授权之前，我们不承
 * //      诺提供任何形式的技术支持、使用担保，也不承担任何因使用本软件而产生问题的相关责任；
 * //   3. 英斯特哈博（北京）科技有限公司不对使用本产品构建的商城中的内容信息承担责任，但在不侵犯
 * //      用户隐私信息的前提下，保留以任何方式获取用户信息及商品信息的权利；
 * //
 * //  有关本产品最终用户授权协议、商业授权与技术服务的详细内容，均由英斯特哈博（北京）科技有限公司
 * //  独家提供。英斯特哈博（北京）科技有限公司拥有在不事先通知的情况下，修改授权协议的权力，修改后
 * //  的协议对改变之日起的新授权用户生效。电子文本形式的授权协议如同双方书面签署的协议一样，具有完
 * //  全的和等同的法律效力。您一旦开始修改、安装或使用本产品，即被视为完全理解并接受本协议的各项条
 * //  款，在享有上述条款授予的权力的同时，受到相关的约束和限制。协议许可范围以外的行为，将直接违反
 * //  本授权协议并构成侵权，我们有权随时终止授权，责令停止损害，并保留追究相关责任的权力。
 * //
 * // -----------------------------------------------------------------------------
 * //
 */

package org.bigdata.zczw.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
/**
 *热门搜索中热门标签自动换行的流式布局
 */
public class HotSearchFlowView extends ViewGroup{

    //存储所有子View
    private List<List<View>> mAllChildViews = new ArrayList<List<View>>();
    //每一行的高度
    private List<Integer> mLineHeight = new ArrayList<Integer>();

    public HotSearchFlowView(Context context) {
        this(context, null);
    }
    public HotSearchFlowView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public HotSearchFlowView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //父控件传进来的宽度和高度以及对应的测量模式
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        //如果当前ViewGroup的宽高为wrap_content的情况
        int width = 0;//自己测量的 宽度
        int height = 0;//自己测量的高度
        //记录每一行的宽度和高度
        int lineWidth = 0;
        int lineHeight = 0;

        //获取子view的个数
        int childCount = getChildCount();
        for(int i = 0;i < childCount; i ++){
            View child = getChildAt(i);
            //测量子View的宽和高
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //得到LayoutParams
//            MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
            //子View占据的宽度
            int childWidth = child.getMeasuredWidth() ;
            //子View占据的高度
            int childHeight = child.getMeasuredHeight();

            //换行时候
            if(lineWidth + childWidth > sizeWidth){
                //对比得到最大的宽度
                width = Math.max(width, lineWidth);
                //重置lineWidth
                lineWidth = childWidth;
                //记录行高
                height += lineHeight;
                lineHeight = childHeight;
            }else{//不换行情况
                //叠加行宽
                lineWidth += childWidth;
                //得到最大行高
                lineHeight = Math.max(lineHeight, childHeight);
            }
            //处理最后一个子View的情况
            if(i == childCount -1){
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }

        }

        //wrap_content
        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width,
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height);

//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        mAllChildViews.clear();
        mLineHeight.clear();
        //获取当前ViewGroup的宽度
        int width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;
        //记录当前行的view
        List<View> lineViews = new ArrayList<View>();
        int childCount = getChildCount();
        for(int i = 0;i < childCount; i ++){
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();

            //如果需要换行
            if(childWidth + lineWidth + lp.leftMargin + lp.rightMargin > width){
                //记录LineHeight
                mLineHeight.add(lineHeight);
                //记录当前行的Views
                mAllChildViews.add(lineViews);
                //重置行的宽高
                lineWidth = 0;
                lineHeight = childHeight + lp.topMargin + lp.bottomMargin;
                //重置view的集合
                lineViews = new ArrayList();
            }
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
            lineViews.add(child);
        }
        //处理最后一行
        mLineHeight.add(lineHeight);
        mAllChildViews.add(lineViews);

        //设置子View的位置
        int left = 0;
        int top = 0;
        //获取行数
        int lineCount = mAllChildViews.size();
        for(int i = 0; i < lineCount; i ++){
            //当前行的views和高度
            lineViews = mAllChildViews.get(i);
            lineHeight = mLineHeight.get(i);
            for(int j = 0; j < lineViews.size(); j ++){
                View child = lineViews.get(j);
                //判断是否显示
                if(child.getVisibility() == View.GONE){
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int cLeft = left + lp.leftMargin;
                int cTop = top + lp.topMargin;
                int cRight = cLeft + child.getMeasuredWidth();
                int cBottom = cTop + child.getMeasuredHeight();
                //进行子View进行布局
                child.layout(cLeft, cTop, cRight, cBottom);
                left += child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            }
            left = 0;
            top += lineHeight;
        }

    }


    /**
     * 与当前ViewGroup对应的LayoutParams
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {

        return new MarginLayoutParams(getContext(), attrs);
    }
}
