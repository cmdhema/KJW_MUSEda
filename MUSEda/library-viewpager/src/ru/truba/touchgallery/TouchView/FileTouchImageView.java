/*
 Copyright (c) 2012 Roman Truba

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 permit persons to whom the Software is furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial
 portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH
 THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package ru.truba.touchgallery.TouchView;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;

public class FileTouchImageView extends UrlTouchImageView 
{
	
	OnImageItemProcessListener listener;
	
    public FileTouchImageView(Context ctx)
    {
        super(ctx);

    }
    public FileTouchImageView(Context ctx, AttributeSet attrs)
    {
        super(ctx, attrs);
    }

    public void setUrl(String imagePath)
    {
        new ImageLoadTask().execute(imagePath);
    }
    
    //No caching load
    public class ImageLoadTask extends UrlTouchImageView.ImageLoadTask
    {
    	
        Bitmap bm = null;
        DataInputStream dis = null;
        
		@Override
        protected Bitmap doInBackground(String... strings) {
            String path = strings[0];

            long imageSize = 0;
            int descripter = 0;
            byte[] imageBuffer = null;
            
            try {
            	
				dis = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));
				imageSize = dis.readLong();
				imageBuffer = new byte[(int)imageSize];
				
				while ( descripter < imageSize ) {
					imageBuffer[descripter] = dis.readByte();
					descripter++;
//					Log.i("FileTouchImageView", imageBuffer[descripter] +"");
				}

				bm = BitmapFactory.decodeByteArray(imageBuffer, 0, (int) imageSize );
				
				dis.mark((int) imageSize);

				Log.i( "FileTouchImageView", "ImageSize : " + imageSize );
//				dis.close();
				
				return bm;
				
            } catch (FileNotFoundException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
            
            /*
            try {
            	File file = new File(path);
            	FileInputStream fis = new FileInputStream(file);
                InputStreamWrapper bis = new InputStreamWrapper(fis, 8192, file.length());
                bis.setProgressListener(new InputStreamProgressListener()
				{					
					@Override
					public void onProgress(float progressValue, long bytesLoaded,
							long bytesTotal)
					{
						publishProgress((int)(progressValue * 100));
					}
				});
                bm = BitmapFactory.decodeStream(bis);
                bis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            */
        }

		@Override
		protected void onPostExecute(Bitmap bm) {
			
			super.onPostExecute(bm);
			
			if ( bm != null ) 
				listener.onProcessSuccess(dis);
			else 
				listener.onProcessError();
			
		}
        
        
    }
    
    public interface OnImageItemProcessListener {
    	public void onProcessSuccess(DataInputStream dis);
    	public void onProcessError();
    }
    
    public void setOnImageDataProcessListener(OnImageItemProcessListener listener ) {
    	this.listener = listener;
    }
}
