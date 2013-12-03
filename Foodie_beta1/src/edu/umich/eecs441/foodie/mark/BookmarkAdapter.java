package edu.umich.eecs441.foodie.mark;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import edu.umich.eecs441.foodie.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import edu.umich.eecs441.foodie.database.MealEntry;

public class BookmarkAdapter extends BaseAdapter {
	private static String TAG = "BookmarkAdapter.";
	
	private final Activity activity;
	
	private final List <MealEntry> data;
	
	private final List <Bitmap> pic;
	
	private static LayoutInflater inflater = null;
	
	public BookmarkAdapter(Activity a, List<MealEntry> entryList, List<Bitmap> picList) {
		
		
		activity = a;
		data = entryList;
		pic = picList;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return new String (data.get(arg0).getPicMealName() + data.get(arg0).getPicUrl());
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int row, View arg1, ViewGroup arg2) {
	
		final int deleteValue = row;
		
		ViewHolder holder;
		
		if (arg1 == null) {
			
			arg1 = inflater.inflate(R.layout.listview_item, null);
			holder = new ViewHolder();
			
			holder.mealName = (TextView)arg1.findViewById(R.id.meal_name);
			holder.translation = (TextView)arg1.findViewById(R.id.translation);
			holder.picture = (ImageView)arg1.findViewById(R.id.list_image);
			holder.delete = (Button)arg1.findViewById(R.id.deletebutton);
			
			arg1.setTag(holder);
		
		} else {
			holder = (ViewHolder)arg1.getTag();
		}
		
		MealEntry entry = data.get(row);
		Bitmap bitmap = pic.get(row);
		holder.mealName.setText(entry.getPicMealName());
		holder.translation.setText(entry.getMealTranslation());
		holder.delete.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				new Thread(new CancelThread(data.get(deleteValue))).start();
				data.remove(deleteValue);
				pic.remove(deleteValue);
				notifyDataSetChanged();
			}
			
		});
		if (bitmap == null) {
			holder.picture.setImageResource(R.drawable.error);
		} else {
			holder.picture.setImageBitmap(bitmap);
		}
		
		return arg1;
	}
		
	class ViewHolder {
		public TextView mealName;
		public TextView translation;
		public Button delete;
		public ImageView picture;
	}
}
