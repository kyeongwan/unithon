package unithon.here;

import org.json.JSONObject;

import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;


public class HTTP_Json extends AsyncTask<JSONObject, String, String> {

	private String ServerIP;
	private String data;

	HTTP_Json() {

	}

	public void setServerURL(String url){
		this.ServerIP = url;
	}

	public String SendJsonMsg(JSONObject jObj) {

		String url = ServerIP;

		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(url);
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 3000);
		HttpConnectionParams.setSoTimeout(params, 3000);
		post.setHeader("Content-type", "application/json; charset=utf-8");

		try {
			StringEntity se;
			se = new StringEntity(jObj.toString());
			HttpEntity he = se;
			post.setEntity(he);

		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		try {

			HttpResponse response = client.execute(post);

			BufferedReader bufReader =

			new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"));

			String line = null;
			String result = "";

			while ((line = bufReader.readLine()) != null) {
				result += line;
			}
			return result;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return "Error" + e.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return "Error" + e.toString();
		}
	}

	@Override
	protected String doInBackground(JSONObject... arg0) {
		// TODO Auto-generated method stub
		return SendJsonMsg(arg0[0]);
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		this.data = result;
	}

	public String getData() {
		return this.data;
	}

}
