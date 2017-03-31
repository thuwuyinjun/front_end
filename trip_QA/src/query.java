
import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;

public class query {
	
	static
    	{
		System.loadLibrary("msc");
        	System.loadLibrary("xunfei");
    	}
	public native static int TTS(String text);
	public static void main(String []args) throws IOException
	{
		
		System.out.println("欢迎来到"+args[0]);
		
		String[] key_point = get_point(args[0]);
		
		System.out.println("您想了解" + args[0] + "的那方面内容？");
		
		System.out.printf("%s", key_point[0]);
		for(int i = 1; i<key_point.length; i++)
		{
			if(i == key_point.length - 1)
			{
				System.out.printf("或者%s？", key_point[i]);
			}
			else
			{
				System.out.printf("，%s", key_point[i]);
			}
		}
		
		
		
		int i = 0;
		
		System.out.println("请输入您的选项");
		Scanner scan = new Scanner(System.in);
		String read = scan.nextLine();
		for(i = 0; i < key_point.length; i++)
		{
			if(read.equals(key_point[i]))
				break;
		}
		if(i >= key_point.length)
		{
			System.out.println("您输入有误，请重新输入");
		}
		while(i >= key_point.length)
		{
			System.out.println("请输入您的选项");
			scan = new Scanner(System.in);
			read = scan.nextLine();
			for(i = 0; i < key_point.length; i++)
			{
				if(read == key_point[i])
					break;
			}
			if(i >= key_point.length)
			{
				System.out.println("您输入有误，请重新输入");
			}
		}
		
		
		introduction(args[0], key_point[i]);
		
		
//		String queryString = "select distinct ?subject ?literal ?redirects ?typeOfOwner ?redirectsTypeOfOwner where {"
//				+ "?subject <http://www.w3.org/2000/01/rdf-schema#label> ?literal ."
//				+ "?subject <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?typeOfOwner ."
//				+ "optional { ?subject <http://dbpedia.org/ontology/wikiPageRedirects> ?redirects ."
//				+ "optional {?redirects <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?redirectsTypeOfOwner ."
//				+ "}} Filter ( !bound(?typeOfOwner) || ( (?typeOfOwner = <http://dbpedia.org/ontology/"
//				+ questionClass
//				+ ">))) . "
//				+ "?literal <bif:contains> '\""
//				+ permutation
//				+ "\"'. } limit "
//				+ limit;
//		System.out.println(queryString);
//		Query queryObj = QueryFactory.create(queryString);
//		String sparqlEndpoint = "http://dbpedia.org/sparql";
//		QueryExecution qe = QueryExecutionFactory.sparqlService(sparqlEndpoint, queryObj);
	}
	
	static String [] get_point(String location)
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

	static void introduction(String location, String point)
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
		System.out.println(output);
                int r = TTS(output);
	}
}
