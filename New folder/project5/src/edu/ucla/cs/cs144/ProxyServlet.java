package edu.ucla.cs.cs144;

import java.io.*;
import java.net.*;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
    	//get the query
    	String inputquery = request.getParameter("q");

        String queryParsed = String.format("q=%s", URLEncoder.encode(inputquery, "UTF-8"));

    	//create googlequery
    	URL googlequery = new URL("http://google.com/complete/search?output=toolbar&" + queryParsed);

    	//connection
    	HttpURLConnection googleconnection = (HttpURLConnection) googlequery.openConnection();
        googleconnection.setRequestProperty("Accept-Charset", "UTF-8");

    	//read the xml from the googles
    	InputStream in = googleconnection.getInputStream();
    	InputStreamReader stream = new InputStreamReader(in);
    	BufferedReader buff = new BufferedReader(stream);
    	StringBuffer result = new StringBuffer();

    	//temp string
    	String temp;

    	//for each line read
    	while ((temp = buff.readLine()) != null) {
    		//append it to the result
    		result.append(temp);
    	}

    	//set response type
    	response.setContentType("text/xml");

    	//print the results
    	PrintWriter pwriter = response.getWriter();
    	pwriter.println(result.toString());
    }
}
