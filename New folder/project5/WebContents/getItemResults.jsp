<!DOCTYPE html>
<%@ page import="edu.ucla.cs.cs144.*" %>
<%@ page import="java.util.*" %>

<% 
	Item item = (Item) request.getAttribute("item");
	Vector<String> category = (Vector) request.getAttribute("category");
	Vector<BidInfo> bidinfo = (Vector) request.getAttribute("bidinfo");
	String la = "1.0";
	String lo = "1.0";
	String location = "a";
	String country = "a";
	Boolean geo = true;
	if (item != null) {
		la = item.latitude;
		lo = item.longitude;
		location = item.location;
		country = item.country;
		if (item.latitude.equals("") || item.latitude.equals(" ") ||
			item.longitude.equals("") || item.longitude.equals(" ")) {
			geo = false;
			la = "34.063509";
			lo = "-118.44541";
		}
	}
%>

<html> 
	<head> 
		<%
			if (item != null) {
				%>
				<title><%= item.name %></title> 
				<%
			} 
			else {
				%>
				<title>Item Not Found</title>
				<%
			}
		%>
		<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
		<style type="text/css"> 
		  html { height: 100% } 
		  body { height: 100%; margin: 0px; padding: 0px } 
		  #map_canvas { height: 100% } 
		</style> 
		<script type="text/javascript" 
		    src="http://maps.google.com/maps/api/js?sensor=false"> 
		</script> 
		<script type="text/javascript"> 
		  	function initialize() { 
			  	var ucla = new google.maps.LatLng(34.063509, -118.44541);
			  	var uclaOptions = {
			  		zoom: 14,
			  		center: ucla,
			  		mapTypeId: google.maps.MapTypeId.ROADMAP
			  	};
			  	var map = new google.maps.Map(document.getElementById("map_canvas"),
			        uclaOptions); 

			  	var condition = <%= geo %>;
			    if (condition == true) {
			    	var latlng = new google.maps.LatLng(<%= la %>, <%= lo %>);
			    	var myOptions = {
			    		zoom: 14,
			    		center: latlng,
			    		mapTypeId: google.maps.MapTypeId.ROADMAP
			    	};
			    	map = new google.maps.Map(document.getElementById("map_canvas"),
			        myOptions); 
			        var marker = new google.maps.Marker({map: map, position:latlng});
			    }
			    else {
			    	var address = "<%= location %>, <%= country %>";
			    	var geocoder = new google.maps.Geocoder();
			    	geocoder.geocode({'address': address}, function(results, status) {
			    		if (status == google.maps.GeocoderStatus.OK) {
			    			map.setCenter(results[0].geometry.location);
			    			var marker = new google.maps.Marker({map: map, position: results[0].geometry.location});
			    		}
			    		else {
			    			address = "<%= country %>";
			    			geocoder.geocode({'address': address}, function(results, status) {
			    				if (status == google.maps.GeocoderStatus.OK) {
			    					map.setCenter(results[0].geometry.location);
			    					map.setZoom(3);
			    					var marker = new google.maps.Marker({map: map, position: results[0].geometry.location});
			    				}
			    			});
			    		}
			    	});
		  	 	}
			  } 
		</script> 
		<script type="text/javascript" src="autosuggest.js"></script>
        <script type="text/javascript">
            window.onload = function () {
                var oTextbox = new AutoSuggestControl(document.getElementById("txt1"), new SuggestionProvider()); 
            }
        </script>
        <link rel="stylesheet" type="text/css" href="autosuggest.css"/>
	</head>

	<body onload="initialize()"> 
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
				if (item != null) {
				%>
				<h1><%= item.name %></h1>
				Item ID: <%= item.id %>
				</br>
				<h2>Categories</h2>
				<%
					for (String cat : category) {
						%>
						<%= cat %>
						</br>
						<%
					}
				%>
				</br>
				<h2>Description</h2>
				<%
					if (item.description.equals("")) {
						%>
						<i>No Description</i>
						<%
					}
					else { 
					%>
					<%= item.description %>
					<%
					}
				%>
				</br>
				<h2>Seller Info</h2>
				UserID: <%= item.sellerid %>
				</br>
				Rating: <%= item.sellerrating %>
				</br>
				Location: <%= item.location %>
				</br>
				Country: <%= item.country %>
				</br>
				Latitude: 
				<% if (!geo) {
					%>
					<i>Not Available</i>
					<%
				} else {
					%>
					<%= item.latitude %>
					<%
				}
				%>
				</br>
				Longitude: 
				<% if (!geo) {
					%>
					<i>Not Available</i>
					<%
				} else {
					%>
					<%= item.longitude %>
					<%
				}
				%>
				</br>
				<h2>Auction Info</h2>
				Started: <%= item.started %>
				</br>
				Ends: <%= item.ends %>
				</br>
				<% 
					if (!(item.buyprice.equals(""))) {
						%>
						Buy Price: $<%= item.buyprice %>
						<form action="/eBay/buy">
							<input name="id" type="hidden" value="<%= item.id %>"/>
							<input type="submit" value="Buy It Now"/>
						</form>
						<%
					}
				%>
				First Bid: $<%= item.firstbid %>
				</br>
				Number of Bids: <%= item.numbids %>
				</br>
				<h2>Bid History</h2>
				<%
					if (bidinfo.size() == 0) {
						%>
						<i>No Bids Yet</i>
						</br>
						</br>
						<%
					}
					else {
						for (BidInfo bid : bidinfo) {
							%>
							<b><%= bid.id %></b> (<%= bid.rating %>, <%= bid.location %>, <%= bid.country %>)
							</br>
							<i><%= bid.time %></i>
							</br>
							Amount: $<%= bid.amount %>
							</br>
							</br>
							<%
						}
					}
				%>
				<%
				}
				else {
					%>
					<h2>Item Not Found</h2>
					</br>
					</br>
					<%
				}
				%>
		</div>
		<%
			if (item != null) {
				%>
				<div id="map_canvas" style="width:100%; height:100%">
				<%
			}
			else {
				%>
				<div id="map_canvas" style="width:100%; height:100%; visibility:hidden">
				<%
			}
		%>

		</div> 
	</body>
</html>