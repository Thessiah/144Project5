package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try {
        	Element itemElement = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(AuctionSearchClient.getXMLDataForItemId(request.getParameter("id"))))).getDocumentElement();
            int id = Integer.parseInt(itemElement.getAttribute("ItemID"));
            String name = truncateString(replaceQuotes(getElementTextByTagNameNR(itemElement, "Name")), 50);
            String currently = strip(getElementTextByTagNameNR(itemElement, "Currently"));
            String buyPrice = strip(getElementTextByTagNameNR(itemElement,"Buy_Price"));
            String firstBid = strip(getElementTextByTagNameNR(itemElement, "First_Bid"));
            String numberOfBids = getElementTextByTagNameNR(itemElement, "Number_of_Bids");
            String location = truncateString(replaceQuotes(getElementTextByTagNameNR(itemElement, "Location")), 50);
            Element locationElement = getElementByTagNameNR(itemElement, "Location");
            String latitude = "";
            if (locationElement.getAttribute("Latitude") != null)
            {
                latitude = locationElement.getAttribute("Latitude");
            }
            String longitude = "";
            if (locationElement.getAttribute("Longitude") != null)
            {
                longitude = locationElement.getAttribute("Longitude");
            }
            String country = truncateString(replaceQuotes(getElementTextByTagNameNR(itemElement, "Country")), 50); 
            String started = stringToTimestamp(getElementTextByTagNameNR(itemElement, "Started"));
            String ends = stringToTimestamp(getElementTextByTagNameNR(itemElement, "Ends"));
            Element sellerElement = getElementByTagNameNR(itemElement, "Seller");
            String seller = truncateString(replaceQuotes(sellerElement.getAttribute("UserID")), 50);
            String rating = sellerElement.getAttribute("Rating");
            String description = truncateString(replaceQuotes(getElementTextByTagNameNR(itemElement, "Description")), 4000);
            Item item = new Item(id, name, currently, buyPrice, firstBid, numberOfBids, location, latitude, longitude, country, started, ends, seller, rating, description);
            
            Element categoryElements[] = getElementsByTagNameNR(itemElement, "Category");
            Vector<String> categories = new Vector<String>();
            String category = "";
            for (int i = 0; i < categoryElements.length; i++)
            {
                category = truncateString(replaceQuotes(getElementText(categoryElements[i])), 50);
                categories.add(category);
            }
            Element[] bidElements = getElementsByTagNameNR(getElementByTagNameNR(itemElement, "Bids"), "Bid");
            Vector<Bid> bids = new Vector<Bid>();
            Element bidder = null;
            String bidID = "";
            String bidTime = "";
            String bidAmount = "";
            String bidRating = "";
            String bidLocation = "";
            String bidCountry = "";
            for (int i = 0; i < bidElements.length; i++)
            {
                bidder = getElementByTagNameNR(bidElements[i], "Bidder");
                bidID = truncateString(replaceQuotes(bidder.getAttribute("UserID")), 50);
                bidTime = stringToTimestamp(getElementTextByTagNameNR(bidElements[i], "Time"));
                bidAmount = strip(getElementTextByTagNameNR(bidElements[i], "Amount"));
                bidRating = bidder.getAttribute("Rating");
                bidLocation = truncateString(replaceQuotes(getElementTextByTagNameNR(bidder, "Location")), 50);
                bidCountry = truncateString(replaceQuotes(getElementTextByTagNameNR(bidder, "Country")), 50);
                bids.add(new Bid(bidID, bidTime, bidAmount, bidRating, bidLocation, bidCountry));
            }
            Collections.sort(bids);
            request.setAttribute("item", item);
            request.setAttribute("category", categories);
            request.setAttribute("bid", bids);
    	}
        catch (Exception e)
        {
            
        }
        request.getRequestDispatcher("/getItemResults.jsp").forward(request, response);
    }
    
	static DocumentBuilder builder;
    
    static final String[] typeName = {
    "none",
    "Element",
    "Attr",
    "Text",
    "CDATA",
    "EntityRef",
    "Entity",
    "ProcInstr",
    "Comment",
    "Document",
    "DocType",
    "DocFragment",
    "Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }

    public static String stringToTimestamp(String date) throws IOException
    {
        SimpleDateFormat format_in = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        SimpleDateFormat format_out = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try
        {
            Date parsedDate = format_in.parse(date);
            return "" + format_out.format(parsedDate);
        }
        catch(ParseException pe) {
            System.err.println("Parse error");
            return "Parse Error";
        }
    }
    
    static String replaceQuotes(String p_string) {
        return p_string.replaceAll("\"", "&quot;");
    }

    static String truncateString(String p_string, int p_maxSize)
    {
        if (p_string.length() > p_maxSize)
        {
            p_string = p_string.substring(0, (p_maxSize - 5)) + "...";
        }
        return p_string;
    }	
}