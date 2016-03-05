package edu.ucla.cs.cs144;
import java.text.*;
import java.util.*;

public class BidInfo implements Comparable<BidInfo>{
    public String id;
    public String time;
    public String amount;
    public String rating;
    public String location;
    public String country;

    BidInfo(String paramid, String paramtime, String paramamount,
            String paramrating, String paramlocation, String paramcountry) {
        id = paramid;
        time = paramtime;
        amount = paramamount;
        rating = paramrating;
        location = paramlocation;
        country = paramcountry;
    }

    public int compareTo(BidInfo compareBidInfo) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date current = format.parse(time);
            Date compare = format.parse(compareBidInfo.time);
            //if current date is before compared date
            if (current.compareTo(compare) <= 0) {
                return 1;
            }
            else {
                return -1;
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }
}