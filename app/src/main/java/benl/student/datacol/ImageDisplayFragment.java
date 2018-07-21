package benl.student.datacol;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageDisplayFragment extends Fragment {
	public static final String ARG_URL = "url_code";
	private static final String TAG = "ImageDisplayFragment";
	
	private View view;
	public DisplayImageFromURL urlCall = null; 

	
	public ImageDisplayFragment() {}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_weather_screen, container, false);
			Log.i(TAG, "onCreateView: nullview");
		} else if (view.getParent() != null) {
			((ViewGroup) view.getParent()).removeView(view);
			Log.i(TAG, "onCreateView: view pulled from memory");
			return view;
		}
		
		try {
			ImageView iv = (ImageView) view.findViewById(R.id.imageview);
			DisplayImageFromURL urlCall = new DisplayImageFromURL(iv);
	        urlCall.execute(getArguments().getString(ARG_URL));
		} catch (Exception e) {
			Log.d(TAG, "onCreateView: Error...: " + e);
		}
		return view;
	}
	

	private void retryDisplay() {
		Log.d(TAG, "Dude, what's up?");
		final Button button = (Button) view.findViewById(R.id.retryButton);
		final TextView tv = (TextView) view.findViewById(R.id.text);
		

		button.setText("Retry");
		tv.setText("Failed to connect to internet");
		
		button.setVisibility(Button.VISIBLE);
		tv.setVisibility(TextView.VISIBLE);
		
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				button.setVisibility(Button.INVISIBLE);
				tv.setVisibility(TextView.INVISIBLE);
				downloadLinks();
			}
		});
	}

	private void downloadLinks() {
		View v = view.findViewById(R.id.progressBar1);
		if (v != null) v.setVisibility(View.VISIBLE);
		ImageView iv = (ImageView) view.findViewById(R.id.imageview);
		DisplayImageFromURL urlCall = new DisplayImageFromURL(iv);
        urlCall.execute(getArguments().getString(ARG_URL));
	}
	
	
    public class DisplayImageFromURL extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        public DisplayImageFromURL(ImageView bmImage) {
            this.bmImage = bmImage;
        }
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
            	Log.i(TAG, "Fragment: " + urldisplay);
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                Log.i(TAG, "Finished: " + urldisplay);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return mIcon11;

        }
        protected void onPostExecute(Bitmap result) {
        	if (result == null) {
				retryDisplay();
			} else {
				bmImage.setImageBitmap(result);
			}
			View v = view.findViewById(R.id.progressBar1);
			if (v != null) v.setVisibility(View.GONE);
        }
    }
}
