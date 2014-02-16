package benl.student.datacol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WeatherScreen extends FragmentActivity {
	private static final String[] WEATHER_TYPES = {
		"Rain Forecast",
		"Rain Radar", 
		"Surface Pressure", 
		"Satellite Imagery", 
	};
	private static final int[] MENU_ID = {
		R.menu.weather_screen_forecast,
		R.menu.weather_screen_rain_radar,
		R.menu.weather_screen_surface_pressure,
		R.menu.weather_screen_satellite_imagery,
	};
	private static final String[] URL_KEYS = {
		"3day",
		"5day",
		"rrHour",
		"rr8Hours",
		"surfTasman",
		"surfPacific",
		"satNZInfrared",
		"satNZVisible",
		"satAusInfrared",
	};
	private static final String[] DEFAULTS = {
		URL_KEYS[0],
		URL_KEYS[2],
		URL_KEYS[4],
		URL_KEYS[6],
	};
	private static final Map<String,String> URL_MAP = new HashMap<String,String>() {
		private static final long serialVersionUID = 3344858085612854751L;
	{
		put(URL_KEYS[0], "rainForecast3Day");
		put(URL_KEYS[1], "rainForecast7Day");
		put(URL_KEYS[2], "rainRadarNZ_2h_7min_300K");
		put(URL_KEYS[3], "rainRadarNZ_8h_hourly_300K");
		put(URL_KEYS[4], "tasmanSeaCombinedCharts");
		put(URL_KEYS[5], "swPacificChartsCombined");
		put(URL_KEYS[6], "tasmanSeaInfraredSatelliteSeries");
		put(URL_KEYS[7], "nzVisibleSatellite2Series");
		put(URL_KEYS[8], "australiaInfraredColourPlayer");
	}};
	private static final String[] PREFS_SCREEN = {
		"weatherForecast",
		"weatherRR",
		"weatherSurface",
		"weatherSatellite",
	};
	@SuppressLint("UseSparseArrays")
	private static final Map<Integer,String> MENU_MAP = new HashMap<Integer,String>() {
		private static final long serialVersionUID = 3344858085612854751L;
	{
		put(R.id.action_3Day, URL_KEYS[0]);
		put(R.id.action_5Day, URL_KEYS[1]);
		put(R.id.action_rr_hour, URL_KEYS[2]);
		put(R.id.action_rr_8hours, URL_KEYS[3]);
		put(R.id.action_surf_tasman, URL_KEYS[4]);
		put(R.id.action_surf_pacific, URL_KEYS[5]);
		put(R.id.action_sat_nz_infrared, URL_KEYS[6]);
		put(R.id.action_sat_nz_visible, URL_KEYS[7]);
		put(R.id.action_sat_aus_infrared, URL_KEYS[8]);
	}};
	
	private static final String TAG = "WeatherScreen";
	public static final String THREE_DAY = "3day";
	public static final String FIVE_DAY = "5day";
	public static final String PREFS = "weather";
	public static final String PREFS_TYPE = "weatherType";
	private static final String METSERVICE_API_URL = "http://www.metservice.com/publicData/";
	
	private ArrayList<Fragment> fragments;
	private ArrayList<String> pageNames;
	
	private int mode;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	private SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	private ViewPager mViewPager;

	
	//-------------------------------------------------------------Methods Start
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather_screen);
		setTitle("Weather");
		
		mViewPager = (ViewPager) findViewById(R.id.pager);
		
		SharedPreferences prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		mode = prefs.getInt(PREFS_TYPE, 0);
		restart(prefs.getString(PREFS_SCREEN[mode], DEFAULTS[mode]));

	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setPositions() {
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, 
				RelativeLayout.LayoutParams.MATCH_PARENT);
		
		lp.setMargins(0, getActionBar().getHeight(), 0, 0);
		mViewPager.setLayoutParams(lp);
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			//setPositions();
		}
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			highAPIOrientation(newConfig);
		}
	}
	
	//----------------------------------------------------------------------Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(MENU_ID[mode], menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		SharedPreferences prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		if (MENU_MAP.containsKey(item.getItemId()) &&
				!prefs.getString(PREFS_SCREEN[mode], DEFAULTS[mode]).equals(
						MENU_MAP.get(item.getItemId()))) {
			prefs.edit().putString(PREFS_SCREEN[mode], MENU_MAP.get(item.getItemId())).commit();
			restart(MENU_MAP.get(item.getItemId()));
			return true;
		}
		switch (item.getItemId()) {
		case R.id.action_other_weather:
			listWeatherTypes();
			return true;
		case R.id.action_switch:
			Intent intent = new Intent(this, TrafficScreen.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
			.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private void listWeatherTypes() {
		new AlertDialog.Builder(this)
        .setTitle("Select Type")
        .setItems(WEATHER_TYPES, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	if (mode == which)
            		return;
            	
            	mode = which;
        		restart(saveAndGetWeather(mode));
        		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        			highAPIMenu();
        		}
            }
        })
        .show();
	}

	private String saveAndGetWeather(int type) {
		SharedPreferences prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		prefs.edit().putInt(PREFS_TYPE, type).commit();
		return prefs.getString(PREFS_SCREEN[type], DEFAULTS[type]);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void highAPIMenu() {
		invalidateOptionsMenu();
	}
	
	//------------------------------------------------------------------End Menu
	private void addFragment(String url) {
		Fragment fragment = new ImageDisplayFragment();

		Bundle args = new Bundle();
//		args.putInt(ImageDisplayFragment.ARG_SECTION_NUMBER, fragments.size()+1);
		args.putString(ImageDisplayFragment.ARG_URL, url);
		fragment.setArguments(args);
		
		fragments.add(fragment);
	}
	
	private void restart(String type) {
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		mViewPager.setAdapter(mSectionsPagerAdapter);

		fragments = new ArrayList<Fragment>();
		pageNames = new ArrayList<String>();
		
		new DownloadWebPage().execute(METSERVICE_API_URL + URL_MAP.get(type));
	}

	public void tapped(View view) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			highAPITapped();
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void highAPITapped() {
		View v = findViewById(R.id.pager_title_strip);
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT, 
				FrameLayout.LayoutParams.MATCH_PARENT);
		RelativeLayout rl = (RelativeLayout)findViewById(R.id.rl);
		if (getActionBar().isShowing()) {
			v.setVisibility(View.GONE);
			lp.setMargins(0, 0, 0, 0);
			rl.setLayoutParams(lp);
			getActionBar().hide();
		} else {
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
			getActionBar().show();
			v.setVisibility(View.VISIBLE);
			new Handler().postDelayed(new Runnable() {
			  @Override
			  public void run() {
					lp.setMargins(0, getActionBar().getHeight(), 0, 0);
					rl.setLayoutParams(lp);
			  }
			}, 100);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		for (Fragment frag : fragments) {
			try {
				((ImageDisplayFragment) frag).urlCall.cancel(true);
			} catch (Exception e) {}
		}
	}
	
	private void retryDisplay() {
		final Button button = new Button(this);
		final TextView tv = new TextView(this);
		button.setId(1);
		
		final RelativeLayout rl = (RelativeLayout) this.findViewById(R.id.rl);
		RelativeLayout.LayoutParams buttonlp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams tvlp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		
		buttonlp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		buttonlp.addRule(RelativeLayout.CENTER_VERTICAL);
		tvlp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		tvlp.addRule(RelativeLayout.BELOW, button.getId());
		
		button.setLayoutParams(buttonlp);
		tv.setLayoutParams(tvlp);

		button.setText("Retry");
		tv.setText("Failed to connect to internet");
		
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				rl.removeView(button);
				rl.removeView(tv);
				downloadLinks();
			}
		});
		rl.addView(button);
		rl.addView(tv);
	}

	private void downloadLinks() {
		SharedPreferences prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE);
		restart(prefs.getString(PREFS_SCREEN[mode], DEFAULTS[mode]));
	}
	
	//-------------------------------------------------------------Inner Classes
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
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
			// Show 3 total pages.
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
	
		@Override
		public int getItemPosition(Object object) {
			return PagerAdapter.POSITION_NONE;
		}
	}

    private class DownloadWebPage extends AsyncTask<String, Void, ArrayList<Forecast>> {
    	@Override
    	protected void onPreExecute() {
    		super.onPreExecute();
//    		pd = ProgressDialog.show(WeatherScreen.this, "Loading", "Please wait");
    	}
    	
        @Override
        protected ArrayList<Forecast> doInBackground(String... urls) {
            ArrayList<Forecast> response = new ArrayList<Forecast>();
            String urldisplay = urls[0];
            
            try {
                String json = openWebsite(urldisplay);
                JSONArray list;
                if (mode == 2) {
					JSONObject imageData = new JSONObject(json);
					list = imageData.getJSONArray("imageData");
				} else {
					list = new JSONArray(json);
				}
                int listLength = list.length();
                for (int i = listLength-1; i >= 0; --i) {
                	JSONObject j = list.getJSONObject(i);
                	
                	Forecast forecast = new Forecast();
//                	forecast.issuedTime = j.getString("issuedTime");
//                	forecast.longDateTime = j.getString("longDateTime");
                	if (mode == 2 || (mode == 3 && !urldisplay.equals(METSERVICE_API_URL + URL_MAP.get(URL_KEYS[8])))) {
						forecast.shortDateTime = j.getString("validFromTime");
					} else {
						forecast.shortDateTime = j.getString("shortDateTime");
					}
                	forecast.url = j.getString("url");
                	response.add(forecast);
                }
            } catch (Exception e) {
                Log.e(TAG, "DownloadedWebPage");
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(ArrayList<Forecast> result) {
        	super.onPostExecute(result);
        	
//        	if (pd.isShowing()) {
//				pd.dismiss();
//			}
        	if (result.size() == 0) {
        		retryDisplay();
			}
        	for (Forecast s : result) {
        		pageNames.add(s.shortDateTime);
        		addFragment("http://www.metservice.com"+s.url);
        	}

    		mViewPager.setOffscreenPageLimit(5);
        	mSectionsPagerAdapter.notifyDataSetChanged();
        }
        
        private String openWebsite(String url) throws MalformedURLException, IOException {
        	Log.i(TAG, "Opening: " + url);
			URLConnection urlc = (new URL(url)).openConnection();
			BufferedReader bfr = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
			String line;
			StringBuffer sb = new StringBuffer();
			while((line = bfr.readLine()) != null) {
				sb.append(line);
			}
        	Log.i(TAG, "Finished url: " + url);
			return sb.toString();
        }
    }
	
    
    private class Forecast {
//    	public String issuedTime = "";
//    	public String longDateTime = "";
    	public String shortDateTime = "";
    	public String url = "";
    }
}
