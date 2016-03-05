<!DOCTYPE html>
<%@ page import="edu.ucla.cs.cs144.*" %>
<%@ page import="java.util.*" %>
<% 
	Item item = (Item) request.getAttribute("item");
	Vector<String> categories = (Vector) request.getAttribute("category");
	Vector<Bid> bids = (Vector) request.getAttribute("bid");
	String latitude = "";
	String longitude = "";
	String location = "";
	String country = "";
	Boolean hasLatLong = true;
	if (item != null)
	{
		latitude = item.latitude;
		longitude = item.longitude;
		location = item.location;
		country = item.country;
		if (item.latitude.equals("") || item.longitude.equals(""))
		{
			hasLatLong = false;
			latitude = "0";
			longitude = "0";
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
		  body { height: 100% } 
		  #map_canvas { height: 100% } 
		</style> 
		<script type="text/javascript" 
		    src="http://maps.google.com/maps/api/js?sensor=false"> 
		</script> 
		<script type="text/javascript"> 
		  	function initialize()
			{ 
				var map = new google.maps.Map(document.getElementById("map_canvas"), {zoom: 12, mapTypeId: google.maps.MapTypeId.ROADMAP}); 
			    if (<%= hasLatLong %>)
				{
					map.setCenter(new google.maps.LatLng(<%= latitude %>, <%= longitude %>));
			        var marker = new google.maps.Marker({map: map, position:latlng});
			    }
			    else
				{
			    	var address = "<%= location %>, <%= country %>";
			    	var geocoder = new google.maps.Geocoder();
			    	geocoder.geocode({'address': address}, function(results, status)
					{
			    		if (status == google.maps.GeocoderStatus.OK)
						{
			    			map.setCenter(results[0].geometry.location);
							map.setZoom(8);
			    			var marker = new google.maps.Marker({map: map, position: results[0].geometry.location});
			    		}
			    		else
						{
			    			address = "<%= country %>";
			    			geocoder.geocode({'address': address}, function(results, status)
							{
			    				if (status == google.maps.GeocoderStatus.OK)
								{
			    					map.setCenter(results[0].geometry.location);
			    					map.setZoom(4);
			    					var marker = new google.maps.Marker({map: map, position: results[0].geometry.location});
			    				}
			    			});
			    		}
			    	});
		  	 	}
			} 
		</script> 
	</head>

	<body onload="initialize()"> 
		<a href="/eBay/index.html">Home</a>
		<div>
			<form action="/eBay/item" method="GET">
				Item ID Search: 
				<input name="id" type="text" style="width:100px;"/>
				<input type="submit" value="Search">
			</form>
		</div>
		<div>
			<%
				if (item != null)
				{
				%>
				<h1><u><%= item.id %></u> <%= item.name %></h1>
				<h2>Description</h2>
				<%
					if (item.description.equals(""))
					{
						%>
						No Description
						<%
					}
					else
					{ 
						%>
						<%= item.description %>
						<%
					}
				%>
				</br>
				<h2>Categories</h2>
				<%
					for (int i = 0; i < categories.size() - 1; i++)
					{
						%>
						<%= categories.get(i) %>, 
						<%
					}
					if(categories.size() > 0)
					{
						%>
						<%= categories.get(categories.size() - 1) %>
						<%
					}
				%>
				</br>
				
				<h2>Auction Info</h2>
				<b><%= item.seller %></b> (<%= item.rating %>)
				</br>
				Location: <%= item.location %>
				</br>
				Country: <%= item.country %>
				<% if (hasLatLong)
				{
					%>
					</br>
					Latitude: <%= item.latitude %>				
					</br>
					Longitude: <%= item.longitude %>
					<%
				}
				%>
				</br>
				</br>
				Started: <%= item.started %>
				</br>
				Ends: <%= item.ends %>
				</br>
				<% 
					if (!(item.buyPrice.equals("")))
					{
						%>
						Buy Price: $<%= item.buyPrice %>
						<form action="/eBay/buy">
							<input name="id" type="hidden" value="<%= item.id %>"/>
							<input type="submit" value="Buy It Now"/>
						</form>
						<%
					}
				%>
				First Bid: $<%= item.firstBid %>
				</br>
				Number of Bids: <%= item.numberOfBids %>
				</br>
				<h2>Bid History</h2>
				<%
					if (bids.size() == 0)
					{
						%>
						No Bids Yet
						</br>
						</br>
						<%
					}
					else
					{
						for (Bid bid : bids)
						{
							%>
							<b><%= bid.id %></b> (<%= bid.rating %>)
							</br>
							Location: <%= bid.location %>, <%= bid.country %>
							</br>
							Time: <%= bid.time %>
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
				else
				{
					%>
					<h2>Item Not Found</h2>
					</br>
					</br>
					<%
				}
				%>
		</div>
		<%
			if (item != null)
			{
				%>
				<div id="map_canvas" style="width:100%; height:100%">
				<%
			}
			else
			{
				%>
				<div id="map_canvas" style="width:100%; height:100%; visibility:hidden">
				<%
			}
		%>
		</div> 
	</body>
</html>