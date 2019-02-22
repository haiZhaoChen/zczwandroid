package org.bigdata.zczw.fragment;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.bigdata.zczw.R;
import org.bigdata.zczw.activity.DeFriendListActivity;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.DispatchResultFragment;
import io.rong.imlib.model.Conversation;

public class DeSettingFragment extends DispatchResultFragment {
	private String targetId;
	private String targetIds;
	private Conversation.ConversationType mConversationType;

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.de_ac_friend_setting, container, false);
		Log.e("1111", "131313: ");
		init();
		return view;
	}

	private void init() {
		Intent intent = getActivity().getIntent();
		if (intent.getData() != null) {
			targetId = intent.getData().getQueryParameter("targetId");
			Log.e("11111", "init: "+targetId);
			targetIds = intent.getData().getQueryParameter("targetIds");
			final String delimiter = intent.getData().getQueryParameter("delimiter");

			if (targetId != null) {
				mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase());
			} else if (targetIds != null)
				mConversationType = Conversation.ConversationType.valueOf(intent.getData().getLastPathSegment().toUpperCase());
			Log.e("1111", "----  targetId----:" + targetId + ",targetIds----" + targetIds + ",mConversationType--" + mConversationType);

			RongContext.getInstance().setOnMemberSelectListener(
					new RongIM.OnSelectMemberListener() {

						@Override
						public void startSelectMember(Context context, Conversation.ConversationType conversationType, String s) {
							if (targetId != null)
								mConversationType = Conversation.ConversationType
										.valueOf(getActivity()
												.getIntent()
												.getData()
												.getLastPathSegment()
												.toUpperCase(Locale.getDefault()));
							startActivity(new Intent(getActivity(), DeFriendListActivity.class));
						}
					});
		}
	}

	@Override
	protected void initFragment(Uri uri) {

	}

	@Override
	public boolean handleMessage(Message msg) {
		return false;
	}
}
