package com.adelaide.health.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.adelaide.health.R;

/**
 * @author Wenping(Deb) Du
 */
public class ListAdapter extends BaseAdapter {
	private MyAdapterDelegate delegate;
	private LayoutInflater inflater;
	
	public ListAdapter(Context paramContext, MyAdapterDelegate mMyAdapterDelegate) {
	    this.delegate = mMyAdapterDelegate;
	    this.inflater = LayoutInflater.from(paramContext);
	}
	
	@Override
	public boolean isEnabled(int position){
		return false;
	}
	
	@Override
	public int getCount() {
		if (this.delegate == null)
			return 0;
		else
			return this.delegate.getCount();
	}

	@Override
	public Object getItem(int arg0) {
		return Integer.valueOf(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder localViewHolder;
	    if ((convertView == null) || (convertView.getTag() == null)) {
	    	convertView = this.inflater.inflate(R.layout.item_layout, null);
	    	localViewHolder = new ViewHolder(convertView);
	    	convertView.setTag(localViewHolder);
	    } else {
	    	localViewHolder = (ViewHolder)convertView.getTag();
		    localViewHolder.cancel();
	    }
	    localViewHolder.render(position);
	    return convertView;
	}

	public static abstract interface MyAdapterDelegate {
	    public abstract int getCount();

	    /**
	     *
	     * @param icon  图标
	     * @param title  名称
		 * @param button  按钮
		 * @param paramInt  位置
	     */
	    public abstract void render(
                ImageView icon,
                ImageView flag,
                TextView title,
                TextView button,
                int paramInt);
	    /**
	     * 获取点击项
	     * @param position
	     */
	    public abstract void getChecked(int position);

		/**
		 * 获取点击项
		 * @param position
		 */
		public abstract void getItemChecked(int position);

	    /**
	     * 获取缩略图
	     * @param icon
	     * @param position
	     */
	    public abstract void getCover(ImageView icon, int position);
	}
	
	class ViewHolder {
		private RelativeLayout layout;
		private TextView title;
		private ImageView flag;
		private ImageView icon;
		private TextView button;
		private Handler handler;
		
	    private int position = -1;
	    private Runnable runnable = new Runnable() {
	    	@Override
			public void run() {
				ListAdapter.this.delegate.getCover(icon, position);
	    	}
	    };

	    public ViewHolder(View localObject) {
	    	this.layout = (RelativeLayout) localObject.findViewById(R.id.item_r_layout);
	    	this.icon = (ImageView) localObject.findViewById(R.id.item_icon);
	    	this.flag = (ImageView) localObject.findViewById(R.id.item_flag);
	    	this.title = (TextView) localObject.findViewById(R.id.item_name);
	    	this.button = (TextView) localObject.findViewById(R.id.item_detect);
	    }
	    
	    /**
	     * roll back
	     */
	    public void cancel() {
	    	if (this.handler != null)
	    		this.handler.removeCallbacks(this.runnable);
	    }

	    public void render(int paramInt) {
			ListAdapter.this.delegate.render(this.icon, this.flag, this.title,  this.button, paramInt);
	    	this.position = paramInt;
	    	this.layout.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					ListAdapter.this.delegate.getItemChecked(position);
				}
			});

	    	this.button.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					ListAdapter.this.delegate.getChecked(position);
				}
			});
	    	
	    	if (this.handler == null)
	    		this.handler = new Handler();
	    	this.handler.postDelayed(this.runnable, 0L);
	    }
	}

}
