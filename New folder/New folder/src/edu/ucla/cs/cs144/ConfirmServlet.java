package edu.ucla.cs.cs144;

import java.io.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ConfirmServlet extends HttpServlet implements Servlet {
	public ConfirmServlet() {}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//grab the secured session (4) buyInfo.jsp -> (5) ConfirmServlet.java
		HttpSession secureSession = request.getSession(true);

		//get timestamp for current time
		Date initialTimestamp = new Date();
		//format timestamp
		DateFormat desiredFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		String finalTimestamp = desiredFormat.format(initialTimestamp);

		//grab the secured session's info and put into secure request to confirm page
		//name
		String name = (String) secureSession.getAttribute("itemName");
		request.setAttribute("itemName", name);
		//id
		int id = (int) secureSession.getAttribute("itemID");
		request.setAttribute("itemID", id);
		//buy price
		String price = (String) secureSession.getAttribute("itemBuy_Price");
		request.setAttribute("itemBuy_Price", price);
		//credit card number
		String ccn = request.getParameter("ccn");
		request.setAttribute("ccn", ccn);
		//timestamp
		request.setAttribute("timestamp", finalTimestamp);

		//forward request to confirmation (5) -> (6)
		request.getRequestDispatcher("/buyConfirm.jsp").forward(request, response);
	}
}