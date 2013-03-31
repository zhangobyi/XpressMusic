package com.lq.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.foound.widget.AmazingAdapter;
import com.foound.widget.AmazingListView;
import com.lq.activity.MainContentActivity;
import com.lq.activity.R;

public class MenuFragment extends Fragment {
	AmazingListView mListView = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.amazinglistview_menu, null);
		mListView = (AmazingListView) v.findViewById(R.id.amazinglistview_menu);
		mListView.setPinnedHeaderView(LayoutInflater.from(getActivity())
				.inflate(R.layout.list_item_section, mListView, false));
		mListView.setAdapter(new SectionAdapter());
		mListView.setOnItemClickListener(new MenuItemClickListener());
		return v;
	}

	private class MenuItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Fragment newContent = null;
			switch (position) {
			// 对应res/array.xml中选项的顺序
			case 0:// TODO 本地音乐
				newContent = new ColorFragment(R.color.holo_orange_dark);
				break;
			case 1:// TODO 喜爱
				newContent = new ColorFragment(R.color.holo_green_dark);
				break;
			case 2:// TODO 收藏列表
				newContent = new ColorFragment(R.color.holo_blue_dark);
				break;
			case 3:// TODO 最近播放
				newContent = new ColorFragment(R.color.holo_purple);
				break;
			case 4:// TODO 选项
				newContent = new ColorFragment(R.color.holo_red_dark);
				break;
			case 5:// TODO 意见反馈
				newContent = new ColorFragment(R.color.holo_red_light);
				break;
			case 6:// TODO 退出
				((MainContentActivity) getActivity()).exit();
				break;
			}
			if (newContent != null)
				switchFragment(newContent);
		}
	}

	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		else if (getActivity() instanceof MainContentActivity) {
			MainContentActivity a = (MainContentActivity) getActivity();
			a.switchContent(fragment);
		}
	}

	private List<Pair<String, List<String>>> getData() {
		List<Pair<String, List<String>>> mDataList = new ArrayList<Pair<String, List<String>>>();
		Resources res = getResources();
		String[] section_titles = { res.getString(R.string.my_music),
				res.getString(R.string.other_settings) };
		String[][] menu_titles = { res.getStringArray(R.array.menu_mymusic),
				res.getStringArray(R.array.menu_othersettings) };
		for (int i = 0; i < section_titles.length; i++) {
			mDataList.add(new Pair<String, List<String>>(section_titles[i],
					Arrays.asList(menu_titles[i])));
		}
		return mDataList;
	}

	private class SectionAdapter extends AmazingAdapter {
		List<Pair<String, List<String>>> dataList = getData();

		@Override
		public int getCount() {
			int res = 0;
			for (int i = 0; i < dataList.size(); i++) {
				res += dataList.get(i).second.size();
			}
			return res;
		}

		@Override
		public String getItem(int position) {
			int c = 0;
			for (int i = 0; i < dataList.size(); i++) {
				if (position >= c
						&& position < c + dataList.get(i).second.size()) {
					return dataList.get(i).second.get(position - c);
				}
				c += dataList.get(i).second.size();
			}
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getAmazingView(int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.list_item_menu, null);
				holder = new ViewHolder();
				holder.menu_title = (TextView) convertView
						.findViewById(R.id.menu_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.menu_title.setText(getItem(position));
			return convertView;
		}

		protected void bindSectionHeader(View view, int position,
				boolean displaySectionHeader) {
			View section_item = view.findViewById(R.id.list_item_section);
			if (displaySectionHeader) {
				section_item.setVisibility(View.VISIBLE);
				TextView lSectionTitle = (TextView) view
						.findViewById(R.id.list_item_section_text);
				lSectionTitle
						.setText(getSections()[getSectionForPosition(position)]);
			} else {
				section_item.setVisibility(View.GONE);
			}
		}

		@Override
		public void configurePinnedHeader(View header, int position, int alpha) {
			TextView lSectionHeader = (TextView) header
					.findViewById(R.id.list_item_section_text);
			lSectionHeader
					.setText(getSections()[getSectionForPosition(position)]);
			// lSectionHeader.setBackgroundColor(alpha << 24 | (0xbbffbb));
			// lSectionHeader.setTextColor(alpha << 24 | (0x000000));
		}

		@Override
		public int getPositionForSection(int section) {
			if (section < 0)
				section = 0;
			if (section >= dataList.size())
				section = dataList.size() - 1;
			int c = 0;
			for (int i = 0; i < dataList.size(); i++) {
				if (section == i) {
					return c;
				}
				c += dataList.get(i).second.size();
			}
			return 0;
		}

		@Override
		public int getSectionForPosition(int position) {
			int c = 0;
			for (int i = 0; i < dataList.size(); i++) {
				if (position >= c
						&& position < c + dataList.get(i).second.size()) {
					return i;
				}
				c += dataList.get(i).second.size();
			}
			return -1;
		}

		@Override
		public String[] getSections() {
			String[] res = new String[dataList.size()];
			for (int i = 0; i < dataList.size(); i++) {
				res[i] = dataList.get(i).first;
			}
			return res;
		}

		@Override
		protected void onNextPageRequested(int page) {
			// TODO Auto-generated method stub

		}

	}

	private static class ViewHolder {
		public TextView menu_title;
	}
}
