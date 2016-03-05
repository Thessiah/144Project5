package edu.ucla.cs.cs144;

import java.io.*;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BuyServlet extends HttpServlet implements Servlet {
	public BuyServlet() {}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//grab the current session
		HttpSession itemSession = request.getSession(true);

		//grab the session's attributes for the item and set them in our request
		//name
		String name = (String) itemSession.getAttribute("itemName");
		request.setAttribute("itemName", name);
		//id
		int id = (int) itemSession.getAttribute("itemID");
		request.setAttribute("itemID", id);
		//buy price
		String price = (String) itemSession.getAttribute("itemBuy_Price");
		request.setAttribute("itemBuy_Price", price);

		//dispatch
		request.getRequestDispatcher("/buyInfo.jsp").forward(request, response);
	}
}