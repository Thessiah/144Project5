package edu.ucla.cs.cs144;

public class Item {
	public int id;
	public String name;
	public String currently;
	public String buyprice;
	public String firstbid;
	public String numbids;
	public String location;
	public String latitude;
	public String longitude;
	public String country;
	public String started;
	public String ends;
	public String sellerid;
	public String description;
	public String sellerrating;

	//item class that holds onto item data
	Item(int paramid, String paramname, String paramcurrently,String parambuyprice,
			String paramfirstbid, String paramnumbids, String paramlocation, 
			String paramlatitude, String paramlongitude, String paramcountry,
			String paramstarted, String paramends, String paramsellerid, 
			String paramdescription, String paramsellerrating) {
		id = paramid;
		name = paramname;
		currently = paramcurrently;
		buyprice = parambuyprice;
		firstbid = paramfirstbid;
		numbids = paramnumbids;
		location = paramlocation;
		latitude = paramlatitude;
		longitude = paramlongitude;
		country = paramcountry;
		started = paramstarted;
		ends = paramends;
		sellerid = paramsellerid;
		description = paramdescription;
		sellerrating = paramsellerrating;
	}
}