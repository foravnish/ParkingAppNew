package com.newwebinfotech.rishabh.parkingapp.utils;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class RequestHandler {
	static String response = null;
	public final static int GET = 1;
	public final static int POST = 2;
	public class Request extends AsyncTask<Object , Void , Void>{

		@Override
		protected Void doInBackground(Object... params) {
			String url = (String) params[0];
	            HttpEntity entity = (HttpEntity) params[1];
			try{
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpEntity httpEntity = null;
				HttpResponse httpResponse = null;
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(entity);
				httpResponse = httpClient.execute(httpPost);
				
				httpEntity = httpResponse.getEntity();
				response = EntityUtils.toString(httpEntity);
				
				
			}catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			return null;

				

				
			
		}
	
	}
	public RequestHandler()
	{

	}
	public String makeServiceCall(String url,int method)
	{
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;
			if (method == POST) {
				HttpPost httpPost = new HttpPost(url);
				httpResponse = httpClient.execute(httpPost);

			} 
			else if (method == GET) {
				HttpGet httpGet = new HttpGet(url);
				httpResponse = httpClient.execute(httpGet);
			}
			httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}

	public String makePostServiceCall(String url, HttpEntity entity){
		try{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpEntity httpEntity = null;
		HttpResponse httpResponse = null;
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(entity);

			HttpParams httpParameters = new BasicHttpParams();
// Set the timeout in milliseconds until a connection is established.
			int timeoutConnection = 90000;
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
// Set the default socket timeout (SO_TIMEOUT)
// in milliseconds which is the timeout for waiting for data.
			int timeoutSocket = 90000;
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		httpResponse = httpClient.execute(httpPost);
		
		httpEntity = httpResponse.getEntity();
		response = EntityUtils.toString(httpEntity);
		
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			//Here Connection TimeOut excepion
			//Toast.makeText(, "Your connection timedout", 10000).show();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}


	public String makePostServiceCall1(String url, List<NameValuePair> nameValuePairs) {
		response=null;
		if (nameValuePairs.size() <= 0)
			return response;
		HttpEntity entity = null;
		try {
			entity = new UrlEncodedFormEntity(nameValuePairs);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}


		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(entity);
			httpResponse = httpClient.execute(httpPost);
			int responsecode=httpResponse.getStatusLine().getStatusCode();
			httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);


		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}
	public void makePostServiceCall1(String url, HttpEntity entity){
		try{
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpEntity httpEntity = null;
		HttpResponse httpResponse = null;
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(entity);
		httpResponse = httpClient.execute(httpPost);
		
//		httpEntity = httpResponse.getEntity();
//		response = EntityUtils.toString(httpEntity);
//		
		}catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	}
	
	

	


}
