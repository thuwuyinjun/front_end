
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.lang.*;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.dom4j.DocumentException;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.noxue.dao.msg;
import com.noxue.javabean.Msg;
import com.noxue.javabean.User;
@WebServlet("/chat.do")
public class chat extends HttpServlet {

	 String [] charact_list;
	 String serve_user;
	 int QA_seq = 0;
		static
    	{
			System.setProperty("java.library.path","/home/wuyinjun/tomcat/webapps/chat/WEB-INF/classes");
        	System.loadLibrary("msc");
        	System.loadLibrary("xunfei");
    	}
		public native static int TTS(String text, String filename);

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = req.getSession();
		String name = (String) session.getAttribute("username");
		serve_user = name;
		if (name == null) {
			resp.sendRedirect("login.do");
			return;
		}
		super.service(req, resp);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		
		HttpSession session = request.getSession();
		String char_list = session.getAttribute("char_list").toString();
		charact_list = char_list.split("，");
		
		response.setContentType("text/html");
		request.getRequestDispatcher("/WEB-INF/jsp/chat.jsp").forward(request,
				response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		PrintWriter out = response.getWriter();

		String action = request.getParameter("do");
		{
			if (action.trim().equals("sendmsg")) {
				this.Send(request, response);
			} else if (action.trim().equals("getmsg")) {
				this.doGetmsg(request, response);
			}
//			} else if (action.trim().equals("onlinelist")) {
//				this.doOnline(request, response);
//			}
		}
		out.flush();
		out.close();
	}
	
	public void Send(HttpServletRequest request, HttpServletResponse response)throws IOException {
		PrintWriter out = response.getWriter();
		String message = request.getParameter("msg");
		String color = request.getParameter("color");
		HttpSession session = request.getSession();
			JSONObject json = new JSONObject();
			json.put("username", session.getAttribute("username").toString());
			SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
			json.put("color", color);
			json.put("time", df.format((new Date())));
			json.put("msg", message);
			out.println(json.toString());
	}

	public void doGetmsg(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		String message = request.getParameter("msg");
		if(message != null)
		{
		String location = session.getAttribute("location").toString();
		String color = request.getParameter("color");
		String user = "super";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		JSONObject json = new JSONObject();
		String answer;
		int i = 0;
		for(i = 0; i<charact_list.length;i++)
		{
			if(message.equals(charact_list[i]))
			{
				break;
			}
		}
		if(i < charact_list.length)
		{
			answer = introduction(location, message, json);
		}
		else
		{
			answer = "您输入的内容有误，请重新输入";
		}
		
		json.put("username",user);
		json.put("color", color);
		json.put("time", df.format((new Date())));
		json.put("msg", answer);
		json.put("serve_user",serve_user);
		out.println(json.toString());
		}
	}

//	@SuppressWarnings("unchecked")
//	public void doOnline(HttpServletRequest request,
//			HttpServletResponse response) throws IOException {
//		synchronized(this){
//		PrintWriter out  = response.getWriter();
//		ServletContext application = this.getServletContext();
//		List<User> online = null;
//		online = (List<User>) application.getAttribute("online");
//		JSONObject users = new JSONObject();
//		JSONObject user = new JSONObject();
//		Iterator<User> i = online.iterator();
//		if (i != null) {
//			while (i.hasNext()) {
//				User u = (User) i.next();
//				System.out.println(u.getId() + "  " + u.getName() + "   "
//						+ u.getEmail());
//				user.put("username", u.getName());
//				user.put("sex", u.getSex());
//				users.put(u.getId(), user);
//			}
//
//		}
//		
//		out.print(users.toString());
//		}
//	}
	
	
	String  introduction(String location, String point, JSONObject json)
	{
		String queryString = "select distinct ?introduction where {"
				+ "<http://localhost/" + location + "> <http://localhost/" + point + "> ?introduction .}";
		System.out.println(queryString);
		Query queryObj = QueryFactory.create(queryString);
		String sparqlEndpoint = "http://166.111.134.166:3030/dbpedia/query";
		QueryExecution qe = QueryExecutionFactory.sparqlService(sparqlEndpoint, queryObj);
		
		String introduction = null;
		try{
			ResultSet literalResults = qe.execSelect();
			
			while (literalResults.hasNext())
			{
				QuerySolution qsolution = literalResults.nextSolution();
				RDFNode literalLabel = qsolution.get("introduction");
				introduction = literalLabel.toString();
			}
			
		}
		catch (Exception e)
		{
			System.out.println("Exception caught: " + e.toString());
		}
		
		String output = "清华大学" + location + introduction;
		
//		File file =new File("/home/saturn/tomcat/webapps/chat/"+serve_user);    
//		//如果文件夹不存在则创建    
//		if  (!file.exists()  && !file.isDirectory())    
//		{       
//    			file.mkdir();    
//		} 
		int r = TTS(output, "/home/saturn/tomcat/webapps/chat/"+serve_user + "_"+ QA_seq +".wav");
		json.put("seq", String.valueOf(serve_user + "_" + QA_seq + ".wav"));
		QA_seq++;
		return output;
	}

}
