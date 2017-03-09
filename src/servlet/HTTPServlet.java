package servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;
import com.sun.xml.internal.bind.v2.schemagen.xmlschema.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.processors.JsonBeanProcessor;

public class HTTPServlet extends HttpServlet {//servlet请求类

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject requestJson = new JSONObject();
		JSONObject responseJson = new JSONObject();
		requestJson = HTTPUtil.getJsonFromRequest(request);//将接受到的request转为Json对象
		
		JSONArray arr = requestJson.optJSONArray("arrs");
		int sum = 0;
		for (int i = 0; i < arr.size(); i++) {
			 sum += Integer.parseInt(arr.optString(i));
		}
		responseJson.put("sum", String.valueOf(sum));
		
		HTTPUtil.sendAPPMessage(responseJson.toString(), response);//返回结果
	}
	
	
	public static void main(String[] args) {
		JSONObject requestJson = new JSONObject();
		JSONObject responseJson = new JSONObject();
		
		String url = "http://127.0.0.1:8080/HTTPDemo/servlet/HTTPServlet";
		int []arr = {1,2,3,4,5};
		JSONArray jsonarr = JSONArray.fromObject(arr);
		requestJson.put("arrs", jsonarr);
		try {
			String requestStr = URLDecoder.decode(requestJson.toString());
			//dopost发送请求,并用responseStr接受返回结果
			String responseStr = HTTPUtil.dopost(url,requestStr);
			
			responseJson = JSONObject.fromObject(responseStr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String sum = responseJson.optString("sum");
		System.out.println(sum);
	}
	
}
