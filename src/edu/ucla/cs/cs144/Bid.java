package edu.ucla.cs.cs144;
import java.text.*;
import java.util.*;

public class Bid implements Comparable<Bid>
{
    public String id;
    public String time;
    public String amount;
    public String rating;
    public String location;
    public String country;

    Bid(String p_id, String p_time, String p_amount,
            String p_rating, String p_location, String p_country)
    {
        id = p_id;
        time = p_time;
        amount = p_amount;
        rating = p_rating;
        location = p_location;
        country = p_country;
    }
    
    public int compareTo(Bid compare)
    {
        try
        {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (format.parse(time).compareTo(format.parse(compare.time)) <= 0)
            {
                return 1;
            }
            else
            {
                return -1;
            }
        }
        catch (Exception e)
        {
            
        }
        return -1;
    }
}