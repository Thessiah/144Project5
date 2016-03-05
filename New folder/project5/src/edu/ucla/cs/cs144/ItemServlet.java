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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.InputSource;
import org.xml.sax.ErrorHandler;

public class ItemServlet extends HttpServlet implements Servlet {

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

    /* Parses an XML formatted timestamp (old) and formats it as a SQL formatted
     * timestamp.
     */
    static String timestamp(String input) {
        /* Initialize format representing XML */
        SimpleDateFormat xmlForm = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
        /* Initialize format representing MySQL */
        SimpleDateFormat sqlForm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        /* Initialize result string */
        String result = input;

        /* Attempt to parse and reformat the input timestamp */
        try {
            result = sqlForm.format(xmlForm.parse(input));
        }
        /* Throw error message if parsing failed */
        catch (ParseException pe) {
            System.out.println("ERROR: Cannot parse \"" + input + "\"");
        }
        /* Return the result */
        return result;
    }

    /* Parses a string and replaces all instances of '"' with "&quot;"
     * This is important because otherwise SQL will interpret the '"' to
     * act as a separator when in reality it is just a part of the text. */
    static String escape(String input) {
        return input.replaceAll("\"", "&quot;");
    }

    /* Truncate a string to at most max characters, then subtract 5.
     * This is because otherwise the text may end in an escaped 
     * sequence, which would be meaningless.  The longest escape
     * sequence is "&quot;" and the largest number of characters it
     * may occupy after partial truncation is 5. 
     */    
    static String truncate(String input, int max) {
        if (input.length() > max) {
            /* Stylistic choice: append "..." to the truncated string. */
            input = input.substring(0, (max - 5)) + "...";
        }

        /* Return input (it will not have been modified if it did not
         * need any truncation). 
         */
        return input;
    }	
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try {

        	//grab id
        	String id = request.getParameter("id");

        	//parse item data from xml to object
        	String xml = AuctionSearchClient.getXMLDataForItemId(id);

        	//initialize tools
        	DocumentBuilderFactory documentbuilderfactory = DocumentBuilderFactory.newInstance();
        	DocumentBuilder documentbuilder = documentbuilderfactory.newDocumentBuilder();
        	StringReader stringreader = new StringReader(xml);
        	Document doc = documentbuilder.parse(new InputSource(stringreader));

        	//save content into variables
        	//do this just like MyParser.java so just modify all the code lol
        	Element item = doc.getDocumentElement();

        	/* Process with respect to item */
            /* itemID */
            int itemID = Integer.parseInt(item.getAttribute("ItemID"));
            /* Name */
            String itemName = truncate(escape(getElementTextByTagNameNR(item, "Name")), 50);
			/* Currently */
            String itemCurrently = strip(getElementTextByTagNameNR(item, "Currently"));
            /* Buy_Price */
            String itemBuy_Price = strip(getElementTextByTagNameNR(item,"Buy_Price"));
			/* First_Bid */
            String itemFirst_Bid = strip(getElementTextByTagNameNR(item, "First_Bid"));
            /* Number_of_Bids */
            String itemNumber_of_Bids = getElementTextByTagNameNR(item, "Number_of_Bids");
            /* Location */
            String itemLocation = truncate(escape(getElementTextByTagNameNR(item, "Location")), 80);
            /* Latitude and Longitude */
            Element e_itemLocation = getElementByTagNameNR(item, "Location");
            String itemLatitude = "";
            if (e_itemLocation.getAttribute("Latitude") != null)
                itemLatitude = e_itemLocation.getAttribute("Latitude");
            String itemLongitude = "";
            if (e_itemLocation.getAttribute("Longitude") != null)
                itemLongitude = e_itemLocation.getAttribute("Longitude");
            /* Country */
            String itemCountry = truncate(escape(getElementTextByTagNameNR(item, "Country")), 80); 
            /* Started */
            String itemStarted = timestamp(getElementTextByTagNameNR(item, "Started"));
            /* Ends */
            String itemEnds = timestamp(getElementTextByTagNameNR(item, "Ends"));
            /* SellerID */
            Element e_itemSeller = getElementByTagNameNR(item, "Seller");
            String itemSellerID = truncate(escape(e_itemSeller.getAttribute("UserID")), 50);
            /* Description */
            String itemDescription = truncate(escape(getElementTextByTagNameNR(item, "Description")), 4000);
            /* SellerRating */
            String itemSellerRating = e_itemSeller.getAttribute("Rating");
            //compile into an item object
            Item o_item = new Item(itemID, itemName, itemCurrently, itemBuy_Price, itemFirst_Bid,
            						itemNumber_of_Bids, itemLocation, itemLatitude, itemLongitude,
            						itemCountry, itemStarted, itemEnds, itemSellerID, 
            						itemDescription, itemSellerRating);

			/* Process with respect to item category */
            Element itemCategory[] = getElementsByTagNameNR(item, "Category");
            /* container for categories */
            Vector<String> o_category = new Vector<String>();
            for (int i = 0; i < itemCategory.length; i++) {
                /* Parse the current category name. */
                String itemCategoryName = truncate(escape(getElementText(itemCategory[i])), 80);
                /* Append the entry to the vector. */
                o_category.add(itemCategoryName);
            }

            /* Process with respect to bids and bidders */
            Element itemBids = getElementByTagNameNR(item, "Bids");
            Element[] itemBidArray = getElementsByTagNameNR(itemBids, "Bid");
            /* container for bidinfos */
            Vector<BidInfo> o_bidinfo = new Vector<BidInfo>();
            for (Element itemBid : itemBidArray) {
                /* Process with respect to bids */
                Element itemBidder = getElementByTagNameNR(itemBid, "Bidder");
                /* BidderID */
                String bidBidderID = truncate(escape(itemBidder.getAttribute("UserID")), 50);
                /* Time */
                String bidTime = timestamp(getElementTextByTagNameNR(itemBid, "Time"));
                /* Amount */
                String bidAmount = strip(getElementTextByTagNameNR(itemBid, "Amount"));
                /* BidderRating */
                String bidderBidderRating = itemBidder.getAttribute("Rating");
                /* Location */
                String bidderLocation = truncate(escape(getElementTextByTagNameNR(itemBidder, "Location")), 80);
                /* Country */
                String bidderCountry = truncate(escape(getElementTextByTagNameNR(itemBidder, "Country")), 80);
                /* Append the entry to BidInfo object */
                BidInfo temp_bid = new BidInfo(bidBidderID, bidTime, bidAmount, bidderBidderRating,
                								bidderLocation, bidderCountry);
                /* Append the BidInfo object to array */
                o_bidinfo.add(temp_bid);
            }
            //sort bidinfo vector
            Collections.sort(o_bidinfo);

            //set request attributes accordingly
            request.setAttribute("item", o_item);
            request.setAttribute("category", o_category);
            request.setAttribute("bidinfo", o_bidinfo);

            //session info for buy now
            //initialize session
            HttpSession itemSession = request.getSession(true);

            //set attributes for item name, id, and buy price 
            //so that it cannot be manipulated client-side
            itemSession.setAttribute("itemName", itemName);
            itemSession.setAttribute("itemID", itemID);
            itemSession.setAttribute("itemBuy_Price", itemBuy_Price);
    	}
        catch (Exception ex) {
        	ex.printStackTrace();
        }
        //forward the request
        request.getRequestDispatcher("/getItemResults.jsp").forward(request, response);
    }
}
