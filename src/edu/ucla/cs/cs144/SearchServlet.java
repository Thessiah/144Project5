package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String query = request.getParameter("query");
        int numResultsToSkip = 0;
        int numResultsToReturn = 0;
        try
        {
	        numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
	        numResultsToReturn = Integer.parseInt(request.getParameter("numResultsToReturn"));
    	}
    	catch (Exception e)
        {
    		
    	}
        SearchResult[] results = new AuctionSearchClient().basicSearch(query, numResultsToSkip, 99999);
        
        if(numResultsToReturn == 0)
        {
            numResultsToReturn = results.length;
        }
        request.setAttribute("query", query);
        request.setAttribute("numResultsToSkip", numResultsToSkip);
        request.setAttribute("numResultsToReturn", numResultsToReturn);
        request.setAttribute("results", results);
        request.getRequestDispatcher("/keywordSearchResults.jsp").forward(request, response);
    }
}
