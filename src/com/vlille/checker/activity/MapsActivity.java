package com.vlille.checker.activity;

import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.maps.MapActivity;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.AbstractAction;
import com.vlille.checker.R;
import com.vlille.checker.maps.VlilleMapView;
import com.vlille.checker.maps.OnPanAndZoomListener;
import com.vlille.checker.model.Station;
import com.vlille.checker.model.SetStationsInfos;
import com.vlille.checker.utils.ContextHelper;
 
/**
 * Select stations from maps.
 * It allows to select your station browsing the stations map.
 */
public class MapsActivity extends MapActivity implements InitializeActionBar {

	protected final String LOG_TAG = getClass().getSimpleName();
	protected SetStationsInfos setStationsInfos;
	protected VlilleMapView mapView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		onCreate(savedInstanceState, false);
	}
	
	public void onCreate(Bundle savedInstanceState, boolean locationEnabled) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.maps);
		
		doInitActionBar();
		
		mapView = (VlilleMapView) findViewById(R.id.mapview);
		mapView.setLocationActivated(locationEnabled);
		mapView.setOnPanListener(getOnPanListener());
		
		try {
			/**
			 * TODO: use db adapter.
			 */
			setStationsInfos = ContextHelper.parseAllStations(this);
			mapView.setMapsInformations(setStationsInfos.getMapsInfos());
		} catch (RuntimeException e) {
			Log.e(LOG_TAG, "#onCreate() exception", e);
			Toast.makeText(this, getString(R.string.error_no_connection), Toast.LENGTH_LONG);
		}	
	}

	public OnPanAndZoomListener getOnPanListener() {
		return new OnPanAndZoomListener() {
			
			@Override
			public void onZoom() {
				mapView.updateOverlays();
			}
			
			@Override
			public void onPan() {
				mapView.updateOverlays();
			}
		};
	}

	@Override
	public void onResume() {
		super.onResume();
		
		try {
			doResume();
		} catch (RuntimeException e) {
			Log.e(LOG_TAG, "#onResume() exception", e);
			Toast.makeText(this, getString(R.string.error_initialization_stations), Toast.LENGTH_LONG);
		}		
	}
	
	public void doResume() {
		mapView.setStations(getStations());
		mapView.initOverlays();
		mapView.checkDelay();
	}
	
	public List<Station> getStations() {
		return setStationsInfos.getStations();
	}	
	
	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public void doInitActionBar() {
		ActionBar actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.addAction(new RefreshAction());
	}	
	
	private class RefreshAction extends AbstractAction {

        public RefreshAction() {
            super(R.drawable.ic_menu_refresh);
        }

        @Override
        public void performAction(View view) {
        	Log.d(LOG_TAG, "Perform update overlays");
        	mapView.updateOverlays();
        }

    }

	
}
