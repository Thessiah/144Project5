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
    	HttpURLConnection connection = (HttpURLConnection) new URL("http://google.com/complete/search?output=toolbar&" + String.format("q=%s", URLEncoder.encode(request.getParameter("q"), "UTF-8"))).openConnection();
        connection.setRequestProperty("Accept-Charset", "UTF-8");
    	BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    	StringBuffer outputString = new StringBuffer();
    	String currString = "";
    	while ((currString = reader.readLine()) != null)
        {
    		outputString.append(currString);
    	}
    	response.setContentType("text/xml");
    	response.getWriter().println(outputString.toString());
    }
}
