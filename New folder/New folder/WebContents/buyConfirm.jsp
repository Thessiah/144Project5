<!DOCTYPE html>
<html>
	<head>
		<title>Payment Confirmation</title>
		<%
			String urlreturn = "http://" + request.getServerName() + ":1448" +
								"/eBay/index.html";
		%>
	</head>

	<body>
		<a href="<%= urlreturn %>">Return to Site Index</a>
		</br>
		<h1>Congratulations!</h1>
		You have successfully purchased your item.
		</br>
		Located below is your payment confirmation.
		</br>

		<h1>Payment Confirmation</h1>
		Item Name: <%= request.getAttribute("itemName") %>
		Item ID: <%= request.getAttribute("itemID") %>
		</br>
		Price: $<%= request.getAttribute("itemBuy_Price") %>
		</br>
		Credit Card Number: <%= request.getAttribute("ccn") %>
		</br>
		Time of Purchase: <%= request.getAttribute("timestamp") %>
	</body>
</html>