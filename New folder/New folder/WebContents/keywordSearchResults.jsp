<!DOCTYPE html>

<%@ page language="java" contentType="text/html" import="edu.ucla.cs.cs144.*" %>

<%
	/* parse number of results to skip */
	int numResultsToSkip = numResultsToSkip = Integer.parseInt(request.getAttribute("numResultsToSkip").toString());
	/* we can skip number of results to return because it is always 12 lol*/
	String query = request.getAttribute("query").toString();
%>

<html>
	<head>
		<title>Search Results</title>
		<script type="text/javascript" src="autosuggest.js"></script>
        <script type="text/javascript">
            window.onload = function () {
                var oTextbox = new AutoSuggestControl(document.getElementById("txt1"), new SuggestionProvider()); 
            }
        </script>
        <link rel="stylesheet" type="text/css" href="autosuggest.css"/>
	</head>

	<body>
		<a href="/eBay/index.html">Index</a>
		<div class="box2">
			<form action="/eBay/search" method="GET">
				Keyword(s): 
				<input name="query" type="text" id="txt1" style="width:400px;"/>
				<input name="numResultsToSkip" type="hidden" value="0"/>
				<input name="numResultsToReturn" type="hidden" value="12"/>
				<input type="submit" value="Search">
			</form>
			<form action="/eBay/item" method="GET">
				Item ID: 
				<input name="id" type="text" style="width:80px;"/>
				<input type="submit" value="Search">
			</form>
		</div>
		<div class="box3">
			<%
				/* collect results in an array */
				SearchResult[] results = (SearchResult[]) request.getAttribute("results");

			 	/* keep track of max shown this page */
			 	int numResultsToShow = 0;
			 	/* cap it at 10 for this page */
			 	if (results.length > 10) {
			 		numResultsToShow = 10;
			 	}
			 	/* set it to the number of results we have found */
			 	else {
			 		numResultsToShow = results.length;
			 	}

			 	//if no results to show

			%>
			<h1>Seach Results for "<%= query %>"</h1>

			<%
				int firstnum = 0;
				if (numResultsToShow != 0) {
					firstnum = numResultsToSkip +1;
				}
			%>

			<h2>Results <%=firstnum %> - <%=numResultsToShow + numResultsToSkip %></h2>
			<%
				for (int i = 0; i < numResultsToShow; i++) {
					%>
					<a href="/eBay/item?id=<%=results[i].getItemId() %>"> <%=results[i].getName() %></a>
					</br>
					<%
				}
			%>
			</br>
			<%
				// always use numResultsToReturn > 10, because that way we know
				//if there are more results to keep querying for
				/* Show link to previous page, if any */
				if (numResultsToSkip > 0) {
					%>
					<a href="/eBay/search?query=<%=query %>&numResultsToSkip=<%=numResultsToSkip - 10 %>&numResultsToReturn=12" >Previous Page</a>
					<%
				}
				/* Show link to next page, if any */
				if (results.length - 10 > 0) {
					%>
					<a href="/eBay/search?query=<%=query %>&numResultsToSkip=<%=numResultsToSkip + 10 %>&numResultsToReturn=12">Next Page</a>
					<%
				}
			%>
		</div>
	</body>
</html>