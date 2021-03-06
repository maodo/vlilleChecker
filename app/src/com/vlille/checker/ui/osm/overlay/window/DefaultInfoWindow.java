package com.vlille.checker.ui.osm.overlay.window;

import org.osmdroid.views.MapView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.vlille.checker.ui.osm.overlay.ExtendedOverlayItem;

/**
 * Default implementation of InfoWindow. 
 * It handles a text and a description. 
 * It also handles optionally a sub-description and an image. 
 * Clicking on the bubble will close it. 
 * 
 * @author M.Kergall
 */
public class DefaultInfoWindow extends InfoWindow {

	static int mTitleId=0, mDescriptionId=0, mSubDescriptionId=0, mImageId=0; //resource ids

	private static void setResIds(Context context){
		String packageName = context.getPackageName(); //get application package name
		mTitleId = context.getResources().getIdentifier("id/bubble_title", null, packageName);
		mDescriptionId = context.getResources().getIdentifier("id/bubble_description", null, packageName);
		mSubDescriptionId = context.getResources().getIdentifier("id/bubble_subdescription", null, packageName);
		mImageId = context.getResources().getIdentifier("id/bubble_image", null, packageName);
	}
	
	public DefaultInfoWindow(int layoutResId, MapView mapView) {
		super(layoutResId, mapView);
		
		if (mTitleId == 0)
			setResIds(mapView.getContext());
		
		//default behaviour: close it when clicking on the bubble:
		mView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				close();
			}
		});
	}
	
	@Override public void onOpen(ExtendedOverlayItem item) {
		String title = item.getTitle();
		if (title == null)
			title = "";
		((TextView)mView.findViewById(mTitleId /*R.id.title*/)).setText(title);
		
		String snippet = item.getDescription();
		if (snippet == null)
			snippet = "";
		((TextView)mView.findViewById(mDescriptionId /*R.id.description*/)).setText(snippet);
		
		//handle sub-description, hidding or showing the text view:
		TextView subDescText = (TextView)mView.findViewById(mSubDescriptionId);
		String subDesc = item.getSubDescription();
		if (subDesc != null && !("".equals(subDesc))){
			subDescText.setText(subDesc);
			subDescText.setVisibility(View.VISIBLE);
		} else {
			subDescText.setVisibility(View.GONE);
		}

		//handle image
		ImageView imageView = (ImageView)mView.findViewById(mImageId /*R.id.image*/);
		Drawable image = item.getImage();
		if (image != null){
			imageView.setImageDrawable(image); //or setBackgroundDrawable(image)?
			imageView.setVisibility(View.VISIBLE);
		} else
			imageView.setVisibility(View.GONE);
	}

	@Override public void onClose() {
		//by default, do nothing
	}
	
}
