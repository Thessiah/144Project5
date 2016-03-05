<!DOCTYPE html>
<html>
	<head>
		<title>Payment Information</title>
		<% 
			String urlsecure = "https://" + request.getServerName() + ":8443" +
								request.getContextPath() + "/confirm";

			String urlreturn = "http://" + request.getServerName() + ":1448" +
								"/eBay/index.html";
		%>
	</head>

	<body>
		<a href="<%= urlreturn %>">Return to Site Index</a>
		</br>
		<h1>Payment Information</h1>
		Item Name: <%= request.getAttribute("itemName") %>
		</br>
		Item ID: <%= request.getAttribute("itemID") %>
		</br>
		Price: $<%= request.getAttribute("itemBuy_Price") %>
		</br>
		<form action="<%= urlsecure %>" method="post">
			Credit Card Number: <input name="ccn" type="text">
			<input type="submit" value="Buy It Now" />
		</form>
	</body>
</html>