<!DOCTYPE html>
<%@ page language="java" contentType="text/html" import="edu.ucla.cs.cs144.*" %>
<%
	int numResultsToSkip = Integer.parseInt(request.getAttribute("numResultsToSkip").toString());
	int numResultsToReturn = Integer.parseInt(request.getAttribute("numResultsToReturn").toString());
	String query = request.getAttribute("query").toString();
%>
<html>
	<head>
		<title><%= query %> | eBay</title>
		<script type="text/javascript" src="autosuggest.js"></script>
        <script type="text/javascript">
            window.onload = function ()
			{
                var suggest = new AutoSuggestControl(document.getElementById("text"), new SuggestionProvider()); 
            }
        </script>
        <link rel="stylesheet" type="text/css" href="autosuggest.css"/>
	</head>

	<body>
		<a href="/eBay/index.html">Home</a>
		<div>
			<form action="/eBay/search" method="GET">
				Keyword Search: 
				<input name="query" type="text" id="text" style="width:200px;"/>
				<input name="numResultsToSkip" type="hidden" value="0"/>
				<input name="numResultsToReturn" type="hidden" value="25"/>
				<input type="submit" value="Search">
			</form>
		</div>
		<div>
			<%
				SearchResult[] results = (SearchResult[]) request.getAttribute("results");
			%>
			<h1>Search results for "<%= query %>"</h1>
			<%
				int start = 0;
				if (numResultsToReturn != 0)
				{
					start = numResultsToSkip + 1;
				}
				int count = 0;
				if(numResultsToReturn > results.length || numResultsToReturn == 0)
				{
					count = results.length;
				}
				else
				{
					count = numResultsToReturn;
				}
			%>
			<h2>Showing results <%=start %> - <%=count + numResultsToSkip %></h2>
			<%
				for (int i = 0; i < count; i++)
				{
					%>
					<a href="/eBay/item?id=<%=results[i].getItemId() %>"> <%=results[i].getItemId() %></a> <%=results[i].getName() %>
					</br>
					<%
				}
			%>
			</br>
			<%
				if (numResultsToSkip > 0)
				{
					if(numResultsToSkip < count)
					{
						numResultsToSkip = count;
					}
					%>
					<a href="/eBay/search?query=<%=query %>&numResultsToSkip=<%=numResultsToSkip - numResultsToReturn %>&numResultsToReturn=<%=numResultsToReturn %>" >Previous</a>
					<%
				}
				if (results.length - count > 0)
				{
					%>
					<a href="/eBay/search?query=<%=query %>&numResultsToSkip=<%=numResultsToSkip + numResultsToReturn %>&numResultsToReturn=<%=numResultsToReturn %>">Next</a>
					<%
				}
			%>
		</div>
	</body>
</html>