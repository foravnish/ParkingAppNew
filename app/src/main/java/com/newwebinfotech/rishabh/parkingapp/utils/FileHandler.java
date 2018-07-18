package com.newwebinfotech.rishabh.parkingapp.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.newwebinfotech.rishabh.parkingapp.Config;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class FileHandler {


	public String createFolder(String outputPath, String filename) {
		File SecretDirectory = new File(Environment.getExternalStorageDirectory() + "/Parking/Media/"+ outputPath);
		if (!SecretDirectory.exists()) {
			SecretDirectory.mkdirs();
		}
		String dir = new File(SecretDirectory, filename).toString();
		return dir;
	}

	public String createFolderOnRoot(String outputPath, String filename)
	{
		File SecretDirectory = new File(Environment.getExternalStorageDirectory() + "/Parking/"+ outputPath);
		if (!SecretDirectory.exists()) {
			SecretDirectory.mkdirs();
		}
		String dir = new File(SecretDirectory, filename).toString();
		return dir;
	}

	public void copyFile(String srcPath, String destPath) {   //USED FOR DOWNLOADING & SAVE IMAGE FRO SERVER AND TO APP FOLDER.
		InputStream in = null;
		OutputStream out = null;
		try {

			in = new FileInputStream(srcPath);
			out = new FileOutputStream(destPath);

			byte[] buffer = new byte[1024];
			int read;
			while ((read = in.read(buffer)) != -1) {
				out.write(buffer, 0, read);
			}
			in.close();
			in = null;

			// write the output file (You have now copied the file)
			out.flush();
			out.close();
			out = null;

		} catch (FileNotFoundException fnfe1) {
			Log.e("tag", fnfe1.getMessage());
		} catch (Exception e) {
			Log.e("tag", e.getMessage());
		}
	}

	public String doFileUpload(String filepath,String Tag)
	{
		// USED FOR DOWNLOADING AUDIO,VIDEO FILES FROM SERVER & SAVE IT TO APP FOLDER.
		String response = "error";
		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		DataInputStream inStream = null;
		String existingFileName = filepath;// Environment.getExternalStorageDirectory().getAbsolutePath()
		// + "/mypic.png";
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		String responseFromServer = "";
		String urlString="";

		try {

			// ------------------ CLIENT REQUEST--------------------//


		 if (Tag.equalsIgnoreCase("TAG_IMAGE")) {
				urlString = Config.BASE_URL + "send_image.php";

			}

			FileInputStream fileInputStream=null;
			try {
				 fileInputStream = new FileInputStream(new File(existingFileName));

			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

           if(fileInputStream==null)
            {

			return null ;
            }

			// open a URL connection to the Servlet
			URL url = new URL(urlString);
			// Open a HTTP connection to the URL
			conn = (HttpURLConnection) url.openConnection();
			
	 	//tanuj sir..
		//	conn.setFixedLengthStreamingMode((int)sourceFile.length());
			
			// Allow Inputs
			conn.setDoInput(true);
			// Allow Outputs
			conn.setDoOutput(true);
			// Don't use a cached copy.
			conn.setUseCaches(false);
			// Use a post method.
//			conn.setChunkedStreamingMode(1024);
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(40000);
			conn.setReadTimeout(40000);
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type",	"multipart/form-data;boundary=" + boundary);
			dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + lineEnd);
			dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
					+ existingFileName + "\"" + lineEnd);
			dos.writeBytes(lineEnd);
			// create a buffer of maximum size
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
			// read file and write it into form...
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			try {
			while (bytesRead > 0) {

				try {
                    dos.write(buffer, 0, bufferSize);
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                    response = "outofmemoryerror";
                    return response;
                }
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			}
			} catch (Exception e) {
	            e.printStackTrace();
	            response = "error";
	            return response;
	        }

			// send multipart form data necesssary after file data...
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
			// close streams
			Log.e("Debug", "File is written");
			fileInputStream.close();
			dos.flush();
			dos.close();

		} catch (MalformedURLException ex) {
			Log.e("Debug", "error: " + ex.getMessage(), ex);
		} catch (IOException ioe) {
			Log.e("Debug", "error: " + ioe.getMessage(), ioe);
		}

		// ------------------ read the SERVER RESPONSE-------------------
		String str = null;
		

		try {

			inStream = new DataInputStream(conn.getInputStream());

			while ((str = inStream.readLine()) != null)
			{

				Log.i("Debug", "Server Response " + str);

				responseFromServer = str;
			}

			inStream.close();


		} catch (IOException ioex) {
			Log.e("Debug", "error: " + ioex.getMessage(), ioex);
		}
		return responseFromServer;
		
	}
	public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = { "_data" };
			Cursor cursor = null;

			try {
				cursor = context.getContentResolver().query(uri, projection, null, null, null);
				int column_index = cursor.getColumnIndexOrThrow("_data");
				if (cursor.moveToFirst()) {
					return cursor.getString(column_index);
				}
			} catch (Exception e) {
				// Eat it
			}
		}
		else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}

		return null;
	}


	private static String getPathDeprecated(Context ctx, Uri uri)
		{
			if( uri == null ) {
				return null;
			}
			String[] projection = { MediaStore.Images.Media.DATA };
			Cursor cursor = ctx.getContentResolver().query(uri, projection, null, null, null);
			if( cursor != null ){
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				cursor.moveToFirst();
				return cursor.getString(column_index);
			}
			return uri.getPath();
		}

		public static String getSmartFilePath(Context ctx, Uri uri){

		if (Build.VERSION.SDK_INT < 19)
		{
			return getPathDeprecated(ctx, uri);
		}
		return  FileHandler.getPath(ctx, uri);
	}

		@SuppressLint("NewApi")
		public static String getPath(final Context context, final Uri uri)
		{
			final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
			// DocumentProvider
			if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
				// ExternalStorageProvider
				if (isExternalStorageDocument(uri)) {
					final String docId = DocumentsContract.getDocumentId(uri);
					final String[] split = docId.split(":");
					final String type = split[0];

					if ("primary".equalsIgnoreCase(type)) {
						return Environment.getExternalStorageDirectory() + "/" + split[1];
					}

					// TODO handle non-primary volumes
				}
				// DownloadsProvider
				else if (isDownloadsDocument(uri)) {
					final String id = DocumentsContract.getDocumentId(uri);
					final Uri contentUri = ContentUris.withAppendedId(
							Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

					return getDataColumn(context, contentUri, null, null);
				}
				// MediaProvider
				else if (isMediaDocument(uri)) {
					final String docId = DocumentsContract.getDocumentId(uri);
					final String[] split = docId.split(":");
					final String type = split[0];

					Uri contentUri = null;
					if ("image".equals(type)) {
						contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
					} else if ("video".equals(type)) {
						contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
					} else if ("audio".equals(type)) {
						contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
					}

					final String selection = "_id=?";
					final String[] selectionArgs = new String[] {
							split[1]
					};

					return getDataColumn(context, contentUri, selection, selectionArgs);
				}
			}
			// MediaStore (and general)
			else if ("content".equalsIgnoreCase(uri.getScheme())) {
				return getDataColumn(context, uri, null, null);
			}
			// File
			else if ("file".equalsIgnoreCase(uri.getScheme())) {
				return uri.getPath();
			}

			return null;
		}


		public static String getDataColumn(Context context, Uri uri, String selection,
			String[] selectionArgs) {
		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = {
				column
		};

		try {
			cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
					null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}



		public static boolean isExternalStorageDocument(Uri uri)
		{
		return "com.android.externalstorage.documents".equals(uri.getAuthority());
	}


		public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri.getAuthority());
	}


		public static boolean isMediaDocument(Uri uri)
		{
		return "com.android.providers.media.documents".equals(uri.getAuthority());
	    }



	public void deleteOldFileFromParking(String oldFilePath
	){
		File file = new File(oldFilePath);
		boolean deleted = file.delete();

	}




}
