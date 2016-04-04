/*
 * Example of using renderscript for graphics processing
 *
 * Author: James Grams
 *
 * Based off of the example found here
 * https://www.youtube.com/watch?v=kybZnFQh6fE
 * Other sources
 * https://software.intel.com/sites/default/files/managed/36/bc/AndroidBasicRenderScript_0.pdf
 *
  */

package edu.gordon.cs.renderscriptexample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

public class MainActivity extends Activity {
    ImageView mImageView;
    RenderScript mRs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.imageView);
        // Create a renderscript context
        mRs = RenderScript.create(this);
        new RenderRunner().execute();
    }

    class RenderRunner extends AsyncTask<Void, Bitmap, Bitmap> {
        @Override
        protected Bitmap doInBackground(Void... params) {
            Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.cocoa);
            // Create an allocation (from the Renderscript library) from the bitmap
            Allocation alloc = Allocation.createFromBitmap(mRs, img);
            // Create a script to run our script.rs within our renderScript context
            ScriptC_script script = new ScriptC_script(mRs);
            for(int i = 0; i < 1000; i ++) {
                // Run the script's slowlyChange method on each pixel in our allocation
                script.forEach_slowlyChange(alloc, alloc);
                // Copy the allocation back to the bitmap
                alloc.copyTo(img);
                // Publish the progress to the UI thread to update the view
                publishProgress(img);
            }
            return img;
        }
        @Override
        protected void onProgressUpdate(Bitmap... bitmaps) {
            mImageView.setImageBitmap(bitmaps[0]);
        }
        @Override
        protected  void onPostExecute(Bitmap bitmap) {
            // Update the view
            mImageView.setImageBitmap(bitmap);
        }
    }
}
