package servlet;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.HttpClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;

public class HTTPUtil {
	
	
	/**
	 * 
	 * @author   aladdinet
	 * @param    request
	 * @return   JSONObject
	 */
	public static JSONObject getJsonFromRequest(HttpServletRequest request){
		StringBuffer info = new StringBuffer();
		try {
			InputStream is = request.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			byte []buffer = new byte[1024];
			int read;
			while((read = bis.read(buffer)) != -1){
				info.append(new String(buffer,0,read,"UTF-8"));
			}
			bis.close();
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String requestStr = URLDecoder.decode(info.toString());
		
		return JSONObject.fromObject(requestStr);
	}
	
	

	/**
	 * 
	 * @author   aladdinet
	 * @param    message,response
	 * @return   void
	 */
	public static void sendAPPMessage(String message,HttpServletResponse response){
		PrintWriter out = null;
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		try {
			out = response.getWriter();
			out.print(message);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			out.close();
		}
		
	}
	
	
	/**
	 * 
	 * @author   aladdinet
	 * @param    url,json
	 * @return   void
	 * @throws NoSuchAlgorithmException 
	 * @throws KeyManagementException 
	 */
	public static String  dopost(String url , String json) throws Exception{
		String responseText = null;
		
		X509TrustManager x509mgr = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] xcs, String string) {
			}
			public void checkServerTrusted(X509Certificate[] xcs, String string) {
			}
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(null, new TrustManager[] { x509mgr }, new java.security.SecureRandom());
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
				SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		
		
		CloseableHttpClient closeableHttpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		HttpPost method = new HttpPost(url);
		StringEntity entity = new StringEntity(json, "utf-8");// 解决中文乱码问题
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		method.setEntity(entity);	
		HttpResponse httpResponse = closeableHttpClient.execute(method);
		HttpEntity httpEntity2 = httpResponse.getEntity();
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			String result = EntityUtils.toString(httpEntity2);
			responseText  = result;
		}
		closeableHttpClient.close();
		
		
		
		return responseText;
	}
	
}
