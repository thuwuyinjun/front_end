
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 * Servlet implementation class choose
 */
@WebServlet("/choose.do")
public class choose extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public choose() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setContentType("text/html");
		request.getRequestDispatcher("/WEB-INF/jsp/choose.jsp").forward(request,
				response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		String location = request.getParameter("location");
		session.setAttribute("location", location);
		request.setAttribute("url", "chat.do");
		
		
		String[] character_list = get_point( location);
		String character = character_list[0];
		for (int i = 1; i<character_list.length - 1; i++)
		{
			character+="，"+character_list[i];
		}
		String char_list = character;
		char_list+="，"+character_list[character_list.length - 1];
		character+="或者"+character_list[character_list.length - 1];
		session.setAttribute("character", character);
		session.setAttribute("char_list", char_list);
		System.out.println(character);
		request.getRequestDispatcher("/WEB-INF/jsp/message.jsp")
		.forward(request, response);
		//doGet(request, response);
	}
	
	private  String [] get_point(String location)
	{
		String queryString = "select distinct ?point where {"
				+ "<http://localhost/" + location + "> <http://localhost/要点> ?point .}";
		System.out.println(queryString);
		Query queryObj = QueryFactory.create(queryString);
		String sparqlEndpoint = "http://166.111.134.166:3030/dbpedia/query";
		QueryExecution qe = QueryExecutionFactory.sparqlService(sparqlEndpoint, queryObj);
		
		String [] point_list = null;
		try{
			ResultSet literalResults = qe.execSelect();
			
			while (literalResults.hasNext())
			{
				QuerySolution qsolution = literalResults.nextSolution();
				RDFNode literalLabel = qsolution.get("point");
				point_list = literalLabel.toString().split("，");
			}
			
		}
		catch (Exception e)
		{
			System.out.println("Exception caught: " + e.toString());
		}
		return point_list;
	}

}
