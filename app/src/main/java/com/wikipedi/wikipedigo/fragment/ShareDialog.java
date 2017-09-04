package com.wikipedi.wikipedigo.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wikipedi.wikipedigo.R;
import com.wikipedi.wikipedigo.util.Common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Hans CK on 29-Aug-17.
 */

public class ShareDialog extends DialogFragment {

	private String name, imageUrl;
	private View view;

	public ShareDialog() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle data = getArguments();
		if (data != null) {
			name = data.getString("name");
			imageUrl = data.getString("imageUrl");
		}
	}

	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.dialog_share, null);
		((TextView) view.findViewById(R.id.name)).setText(name);
		Common.getInstance().setImage(getActivity(), ((ImageView) view.findViewById(R.id.image)), imageUrl);

		Button btnShare = (Button) view.findViewById(R.id.btnShare);
		btnShare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				shareToOtherApps();
			}
		});

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		AlertDialog dialog = builder.setView(view)
			.create();
		return dialog;
	}

	private void shareToOtherApps() {
		Intent shareIntent = setupShareIntent(getLocalBitmapUri());
		shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.shared_notice));
		startActivity(Intent.createChooser(shareIntent, getString(R.string.shared_notice)));
	}

	private Intent setupShareIntent(Uri uri) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("image/*");
		shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.shared_notice) + "\n" + name);
		shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
		return shareIntent;
	}

	public Uri getLocalBitmapUri() {
		try {
			RelativeLayout screenshot = (RelativeLayout) view.findViewById(R.id.screenshot);
			screenshot.setDrawingCacheEnabled(true);
			screenshot.setDrawingCacheBackgroundColor(0xfffafafa);
			screenshot.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
			screenshot.layout(0, 0, screenshot.getMeasuredWidth(), screenshot.getMeasuredHeight());
			screenshot.buildDrawingCache(true);
			Bitmap bmp = Bitmap.createBitmap(screenshot.getDrawingCache());
			screenshot.setDrawingCacheEnabled(false);

			Uri bmpUri = null;
			try {
				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
					File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "wikipedigo_" + System.currentTimeMillis() + ".jpeg");
					FileOutputStream out = new FileOutputStream(file);
					bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
					out.close();
					bmpUri = Uri.fromFile(file);
				} else {
					File file = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "wikipedigo_" + System.currentTimeMillis() + ".jpeg");
					bmpUri = FileProvider.getUriForFile(getActivity(), "com.codepath.fileprovider", file);
					file.delete();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return bmpUri;
		} catch (ClassCastException e) {
			e.printStackTrace();
			return null;
		}
	}
}
