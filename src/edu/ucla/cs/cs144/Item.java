package edu.ucla.cs.cs144;

public class Item {
	public int id;
	public String name;
	public String currently;
	public String buyPrice;
	public String firstBid;
	public String numberOfBids;
	public String location;
	public String latitude;
	public String longitude;
	public String country;
	public String started;
	public String ends;
	public String seller;
	public String rating;
	public String description;

	Item(int p_id, String p_name, String p_currently, String p_buyPrice, String p_firstBid, String p_numberOfBids, String p_location, String p_latitude, String p_longitude, String p_country, String p_started, String p_ends, String p_seller, String p_rating, String p_description)
	{
		id = p_id;
		name = p_name;
		currently = p_currently;
		buyPrice = p_buyPrice;
		firstBid = p_firstBid;
		numberOfBids = p_numberOfBids;
		location = p_location;
		latitude = p_latitude;
		longitude = p_longitude;
		country = p_country;
		started = p_started;
		ends = p_ends;
		seller = p_seller;
		rating = p_rating;
		description = p_description;
	}
}