package com.museda.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import com.museda.PhotoData;

public class FileUtil {

	private static FileUtil instance;

	public static FileUtil getInstance() {
		if (instance == null)
			instance = new FileUtil();

		return instance;
	}

	/**
	 * 파일 복사
	 * 
	 * @param srcFile
	 *            : 복사할 File
	 * @param destFile
	 *            : 복사될 File
	 * @return
	 */
	public static boolean copyFile(File srcFile, File destFile) {
		boolean result = false;
		try {
			InputStream in = new FileInputStream(srcFile);
			try {
				result = copyToFile(in, destFile);
			} finally {
				in.close();
			}
		} catch (IOException e) {
			result = false;
		}
		return result;
	}

	/**
	 * Copy data from a source stream to destFile. Return true if succeed,
	 * return false if failed.
	 */
	public static boolean copyToFile(InputStream inputStream, File destFile) {
		try {
			OutputStream out = new FileOutputStream(destFile);
			try {
				byte[] buffer = new byte[4096];
				int bytesRead;
				while ((bytesRead = inputStream.read(buffer)) >= 0) {
					out.write(buffer, 0, bytesRead);
				}
			} finally {
				out.close();
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	public static void makeDirectory() {
		String str = Environment.getExternalStorageState();
		if (str.equals(Environment.MEDIA_MOUNTED)) {

			String dirPath = PhotoData.imageDir;
			File file = new File(dirPath);
			if (!file.exists()) // 원하는 경로에 폴더가 있는지 확인
				file.mkdirs();
		}
	}

	/**
	 * Crop된 이미지가 저장될 파일을 만든다.
	 * 
	 * @return Uri
	 */
	public static Uri createSaveCropFile(long time) {
		Uri uri;
		String url = getFilePath(time);
		uri = Uri.fromFile(new File(url));
		return uri;
	}

	public static String getFilePath(long time) {
		return PhotoData.imageDir + "/" + "tmp_" + time + ".jpg";
	}

	public static void removeImage(String path) {
		File f = new File(path);
		f.delete();
	}

	private void modifyImageSize(String uploadImagePath, String photoPath) {

		File original_file = new File(photoPath);

		File copy_file = new File(uploadImagePath);

		FileUtil.copyFile(original_file, copy_file);

		int SCALE = 2;

		try {
			BitmapFactory.Options op = new BitmapFactory.Options();
			op.inSampleSize = SCALE;
			op.inJustDecodeBounds = false;
			op.inPreferredConfig = Config.RGB_565;
			op.inDither = true;
			Bitmap bitmap = BitmapFactory.decodeFile(uploadImagePath, op);
			OutputStream os = new FileOutputStream(uploadImagePath);
			bitmap.compress(Bitmap.CompressFormat.PNG, 85, os);
			os.flush();
			os.close();

			File file = new File(uploadImagePath);
			while (file.length() > 1000 * 1024) {
				SCALE += 2;
				op.inSampleSize = SCALE;
				bitmap = BitmapFactory.decodeFile(uploadImagePath, op);
				os = new FileOutputStream(uploadImagePath);
				bitmap.compress(Bitmap.CompressFormat.PNG, 85, os);
				os.flush();
				os.close();
				file = new File(uploadImagePath);
			}

			bitmap = BitmapFactory.decodeFile(uploadImagePath, op);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void modifyImageSizeTask(String uploadPath, String originalPath) {
		new ResizeAsyncTask(uploadPath, originalPath).execute();
	}

	private class ResizeAsyncTask extends AsyncTask<Void, Void, Void> {

		String uploadImagePath, originalPath;

		public ResizeAsyncTask(String uploadPath, String originalPath) {
			this.uploadImagePath = uploadPath;
			this.originalPath = originalPath;
		}

		@Override
		protected Void doInBackground(Void... params) {

			modifyImageSize(uploadImagePath, originalPath);
			return null;

		}
	}

	public static Bitmap rotateImage(String path) {
		
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		int angle = 0;
		OutputStream os;
		
		if ( bitmap != null ) {

			Matrix m = new Matrix();
			angle = 90 + (angle % 360);
			m.setRotate(angle);

			try {

				Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);

				if (bitmap != converted) {
					bitmap = null;
					bitmap = converted;
					converted = null;
				}
				
				
				os = new FileOutputStream(path);
				bitmap.compress(Bitmap.CompressFormat.PNG, 85, os);
				os.flush();
				os.close();

			} catch (OutOfMemoryError ex) {
				ex.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return bitmap;
	}

}
