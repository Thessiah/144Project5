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
        //get the search input
        String inputQuery = request.getParameter("query");
        int numResultsToSkip = 0;
        int numResultsToReturn = 0;
        try {
	        numResultsToSkip = Integer.parseInt(request.getParameter("numResultsToSkip"));
	        numResultsToReturn = Integer.parseInt(request.getParameter("numResultsToReturn"));
    	}
    	catch (Exception ex) {
    		
    	}

        //create a searcher
        AuctionSearchClient myClient = new AuctionSearchClient();

        //search for results
        SearchResult[] queryResults = myClient.basicSearch(inputQuery, numResultsToSkip, numResultsToReturn);

        //set attributes accordingly
        request.setAttribute("query", inputQuery);
        request.setAttribute("numResultsToSkip", numResultsToSkip);
        request.setAttribute("numResultsToReturn", numResultsToReturn);
        request.setAttribute("results", queryResults);

        //forward the request
        request.getRequestDispatcher("/keywordSearchResults.jsp").forward(request, response);
    }
}
