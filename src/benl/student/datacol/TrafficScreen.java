package benl.student.datacol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

public class TrafficScreen extends FragmentActivity {
	private static final String[] AUCKLAND_SH = {
		"SH1 Northern",
		"SH1 Southern", 
		"SH16 North Western", 
		"SH18 Upper Harbour", 
		"SH20 South Western", 
		"SH20A", 
		"SH20B", 
	};
	private static final String[] NZ_CITIES = {
		"Hamilton",
		"Central North Island", 
		"Wellington", 
		"Tauranga", 
		"Dunedin", 
		"Christchurch",
	};
	private static final String[][] MODE = {
		AUCKLAND_SH,
		NZ_CITIES,
	};
	
	@SuppressLint("UseSparseArrays")
	private static final Map<Integer,Integer> MENU_MAP = 
	new HashMap<Integer,Integer>() {
		private static final long serialVersionUID = 3344858085612854751L;
	{ // Auckland, Rest of New Zealand
		put(R.id.action_auckland, 0);
		put(R.id.action_nz_cities, 1);
	}};

	
	private static final String TAG = "TrafficScreen";
	public static final String PREFS = "traffic";
	public static final String PREFS_MODE = "trafficMode";
	public static final String PREFS_OPTION = "trafficOption";
	
	private ArrayList<Fragment> fragments;
	private ArrayList<String> pageNames;
	private int mode; //Auckland or NZ Cities, 0 or 1
	private int option;

	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewPager mViewPager;

	//------------------------------------------------------------Methods start
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_screen);

		mViewPager = (ViewPager) findViewById(R.id.pager);

		SharedPreferences prefs = getSharedPreferences(
				PREFS, Context.MODE_PRIVATE);
		mode = prefs.getInt(PREFS_MODE, 0);
		option = prefs.getInt(PREFS_OPTION, 0);
		Log.d(TAG, "Mode : " + mode);
		restart(MODE[mode][option]);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			highAPIOrientation(newConfig);
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		saveTrafficIndex(MODE[mode][option]);
	}

	//---------------------------------------------------------------------Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.traffic_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemNum = item.getItemId();
		switch (itemNum) {
		case R.id.action_auckland:
			listTrafficTypes(AUCKLAND_SH, 0);
			return true;
		case R.id.action_nz_cities:
			listTrafficTypes(NZ_CITIES, 1);
			return true;
		case R.id.action_refresh:
			restart(MODE[mode][option]);
			return true;
		case R.id.action_switch:
			Intent intent = new Intent(this, WeatherScreen.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
			.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void listTrafficTypes(final String[] itemList, final int mode) {
		new AlertDialog.Builder(this)
        .setTitle("Select Type")
        .setItems(itemList, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	saveTrafficIndex(MODE[TrafficScreen.this.mode][option]);
            	TrafficScreen.this.mode = mode;
            	option = which;
        		restart(itemList[which]);
            }
        })
        .show();
	}

	private void saveTrafficIndex(String item) {
		SharedPreferences prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		prefs.edit().putInt(item, mViewPager.getCurrentItem()).commit();
		prefs.edit().putInt(PREFS_MODE, mode).commit();
		prefs.edit().putInt(PREFS_OPTION, option).commit();
		return;
	}
	
	//-----------------------------------------------------------------End Menu
	private void restart(String item) {
		fragments = new ArrayList<Fragment>();
		pageNames = new ArrayList<String>();

		// Creates the fragments and calls the URLs
		insertData(TrafficCams.URL_MAP.get(mode).get(MODE[mode][option]),
				TrafficCams.NAME_MAP.get(mode).get(MODE[mode][option]));
		
		// Shouldn't be called
		for (int i = pageNames.size(); i < fragments.size(); i++) {
			pageNames.add("UNKNOWN");
			Log.e(TAG, "restart: SHOULD NOT HAVE ENTERED THIS!!!");
		}
		
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOffscreenPageLimit(5);

		SharedPreferences prefs = getSharedPreferences(
				PREFS, Context.MODE_PRIVATE);
		mViewPager.setCurrentItem(prefs.getInt(item, 0));
	}
	
	private void insertData(String[] imagename, String[] names) {
		for (int i = 0; i < imagename.length; ++i) {
			String url;
			if (mode == 0) {// Auckland
				url = "http://www.trafficnz.info/camera/"+imagename[i]+".jpg";
			} else if (option == 3) {// Tauranga
				url = "http://www.tpcams.eol.co.nz/show_cam.php?PicID="+imagename[i];
			} else {// NZ Cities
				url = "http://www.nzta.govt.nz/traffic/current-conditions/webcams/webcam-images/"+imagename[i]+".jpg";
			}					
			pageNames.add(names[i]);
			addFragment(url);
		}
	}
	
	private void addFragment(String url) {
		Fragment fragment = new ImageDisplayFragment();

		Bundle args = new Bundle();
//		args.putInt(ImageDisplayFragment.ARG_SECTION_NUMBER, fragments.size()+1);
		args.putString(ImageDisplayFragment.ARG_URL, url);
		fragment.setArguments(args);
		
		fragments.add(fragment);
	}

	public void tapped(View view) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			highAPITapped();
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void highAPITapped() {
		View v = findViewById(R.id.pager_title_strip);
		View rl = findViewById(R.id.rl);
		if (getActionBar().isShowing()) {
			v.setVisibility(View.GONE);
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.MATCH_PARENT, 
					FrameLayout.LayoutParams.MATCH_PARENT);
			lp.setMargins(0, 0, 0, 0);
			rl.setLayoutParams(lp);
			getActionBar().hide();
		} else {
			FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.MATCH_PARENT, 
					FrameLayout.LayoutParams.MATCH_PARENT);
			lp.setMargins(0, getActionBar().getHeight(), 0, 0);
			rl.setLayoutParams(lp);
			v.setVisibility(View.VISIBLE);
			getActionBar().show();
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void highAPIOrientation(Configuration newConfig) {
		final View v = findViewById(R.id.pager_title_strip);
		final View rl = findViewById(R.id.rl);
		final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT, 
				FrameLayout.LayoutParams.MATCH_PARENT);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			lp.setMargins(0, 0, 0, 0);
			rl.setLayoutParams(lp);
			v.setVisibility(View.GONE);
			getActionBar().hide();
		} else {
			new Handler().postDelayed(new Runnable() {
			  @Override
			  public void run() {
					lp.setMargins(0, getActionBar().getHeight(), 0, 0);
					rl.setLayoutParams(lp);
					v.setVisibility(View.VISIBLE);
					getActionBar().show();
			  }
			}, 100);
		}
	}

	
	public class SectionsPagerAdapter extends FragmentStatePagerAdapter {
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			try {
				return pageNames.get(position);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "loading";
		}
	}
}
