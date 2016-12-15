package com.meetingroom.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.meetingroom.utils.Tools;

import java.util.ArrayList;
import java.util.List;

import itutorgroup.h2h.R;

public class ActionSheetDialogForView {
	private RelativeLayout container;
	private Context context;
	private TextView txt_title;
	private TextView txt_cancel;
	private LinearLayout lLayout_content;
	private ScrollView sLayout_content;
	private boolean showTitle = false;
	private List<SheetItem> sheetItemList;
	public View view;
	public ActionSheetDialogForView(Context context,RelativeLayout container) {
		this.container = container;
		this.context = context;
	}

	@SuppressWarnings("deprecation")
	public ActionSheetDialogForView builder() {
		// 获取Dialog布局
		view = LayoutInflater.from(context).inflate(
				R.layout.view_actionsheet, null);

		// 设置Dialog最小宽度为屏幕宽度
		view.setMinimumWidth(Tools.getAtyWidth(context)/3);

		// 获取自定义Dialog布局中的控件
		sLayout_content = (ScrollView) view.findViewById(R.id.sLayout_content);
		lLayout_content = (LinearLayout) view
				.findViewById(R.id.lLayout_content);
		txt_title = (TextView) view.findViewById(R.id.txt_title);
		txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
		txt_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		float scale = context.getResources().getDisplayMetrics().density;
		int height = (int) (30 * scale + 0.5f);
		txt_cancel.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, height));
		txt_cancel.setTextSize(12);
		return this;
	}

	public ActionSheetDialogForView setTitle(String title) {
		showTitle = true;
		txt_title.setVisibility(View.VISIBLE);
		txt_title.setText(title);
		return this;
	}


	/**
	 * 
	 * @param strItem
	 *            条目名称
	 * @param color
	 *            条目字体颜色，设置null则默认蓝色
	 * @param listener
	 * @return
	 */
	public ActionSheetDialogForView addSheetItem(String strItem, SheetItemColor color,
                                                 OnSheetItemClickListener listener) {
		if (sheetItemList == null) {
			sheetItemList = new ArrayList<SheetItem>();
		}
		sheetItemList.add(new SheetItem(strItem, color, listener));
		return this;
	}

	/** 设置条目布局 */
	@SuppressWarnings("deprecation")
	private void setSheetItems() {
		if (sheetItemList == null || sheetItemList.size() <= 0) {
			return;
		}
		lLayout_content.removeAllViews();
		int size = sheetItemList.size();

		// TODO 高度控制，非最佳解决办法
		// 添加条目过多的时候控制高度
		if (size >= 5) {
			LayoutParams params = (LayoutParams) sLayout_content
					.getLayoutParams();
			params.height = Tools.getAtyHeight(context)/3 ;
			sLayout_content.setLayoutParams(params);
		}

		// 循环添加条目
		for (int i = 1; i <= size; i++) {
			final int index = i;
			SheetItem sheetItem = sheetItemList.get(i - 1);
			final String strItem = sheetItem.name;
			SheetItemColor color = sheetItem.color;
			final OnSheetItemClickListener listener = (OnSheetItemClickListener) sheetItem.itemClickListener;

			TextView textView = new TextView(context);
			textView.setText(strItem);
			textView.setTextSize(12);
			textView.setGravity(Gravity.CENTER);

			// 背景图片
			if (size == 1) {
				if (showTitle) {
					textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
				} else {
					textView.setBackgroundResource(R.drawable.actionsheet_single_selector);
				}
			} else {
				if (showTitle) {
					if (i >= 1 && i < size) {
						textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
					} else {
						textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
					}
				} else {
					if (i == 1) {
						textView.setBackgroundResource(R.drawable.actionsheet_top_selector);
					} else if (i < size) {
						textView.setBackgroundResource(R.drawable.actionsheet_middle_selector);
					} else {
						textView.setBackgroundResource(R.drawable.actionsheet_bottom_selector);
					}
				}
			}

			// 字体颜色
			if (color == null) {
				textView.setTextColor(Color.parseColor(SheetItemColor.Blue
						.getName()));
			} else {
				textView.setTextColor(Color.parseColor(color.getName()));
			}

			// 高度
			float scale = context.getResources().getDisplayMetrics().density;
			int height = (int) (30 * scale + 0.5f);
			textView.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, height));

			// 点击事件
			textView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					listener.onClick(index,strItem);
				}
			});

			lLayout_content.addView(textView);
		}
	}
	public boolean isShowing(){
		return container.getVisibility()==View.VISIBLE;
	}
	public void dismiss(){
		container.removeAllViews();
		container.setVisibility(View.GONE);
	}
	public void show() {
		setSheetItems();
		container.removeAllViews();
		container.addView(view);
		container.setVisibility(View.VISIBLE);
	}
	public interface OnSheetItemClickListener {
		void onClick(int which, String srcItem);
	}

	public class SheetItem {
		String name;
		OnSheetItemClickListener itemClickListener;
		SheetItemColor color;

		public SheetItem(String name, SheetItemColor color,
				OnSheetItemClickListener itemClickListener) {
			this.name = name;
			this.color = color;
			this.itemClickListener = itemClickListener;
		}
	}

	public enum SheetItemColor {
		Blue("#037BFF"), Red("#FD4A2E"),Black("#000000");

		private String name;

		private SheetItemColor(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}
	public void setItemString(int position,String itemString){
		sheetItemList.get(position).name = itemString;
	}
}
