package org.bigdata.zczw.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.bigdata.zczw.R;
import org.bigdata.zczw.entity.ApiResult;
import org.bigdata.zczw.entity.GroupInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.rong.imlib.model.Group;

/**
 * Created by Bob on 2015/1/31.
 */
public class DeGroupListAdapter extends BaseAdapter {
	private static final String TAG = DeGroupListAdapter.class.getSimpleName();
	private Context mContext;

	private List<GroupInfo> mGroupList;

	private LayoutInflater mLayoutInflater;
	private List<ApiResult> mResults;
	private ArrayList<View> mViewList;
	HashMap<String, Group> groupMap;

	OnItemButtonClick mOnItemButtonClick;

	public OnItemButtonClick getOnItemButtonClick() {
		return mOnItemButtonClick;
	}

	public void setOnItemButtonClick(OnItemButtonClick onItemButtonClick) {
		this.mOnItemButtonClick = onItemButtonClick;
	}

	public DeGroupListAdapter(Context context, List<GroupInfo> groupList) {

		mLayoutInflater = LayoutInflater.from(context);
		mContext = context;
		this.mGroupList = groupList;
		mViewList = new ArrayList<>();

	}

	@Override
	public int getCount() {
		return mGroupList.size();

	}

	@Override
	public GroupInfo getItem(int position) {
		return mGroupList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null || convertView.getTag() == null) {
			convertView = mLayoutInflater.inflate(R.layout.de_item_group, null);
			viewHolder = new ViewHolder();
			viewHolder.mGroupName = (TextView) convertView.findViewById(R.id.group_adaper_name);
			viewHolder.groupCount = (TextView) convertView.findViewById(R.id.group_adaper_count);
			viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.group_adapter_img);
			viewHolder.mSelectButton = (ImageView) convertView.findViewById(R.id.group_select);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (viewHolder != null) {
			viewHolder.mSelectButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (mOnItemButtonClick != null)
								mOnItemButtonClick.onButtonClick(position, v);
						}
					});
			viewHolder.mGroupName.setText(mGroupList.get(position).getName());
			viewHolder.groupCount.setText(mGroupList.get(position).getTotle()+"名群成员");
			viewHolder.mSelectButton.setBackgroundResource(R.drawable.de_group_chat_selector);
		}
		return convertView;
	}

	public interface OnItemButtonClick {
		public boolean onButtonClick(int position, View view);
	}

	static class ViewHolder {
		TextView mGroupName;
		TextView groupCount;
		ImageView mImageView;
		ImageView mSelectButton;
	}
}
