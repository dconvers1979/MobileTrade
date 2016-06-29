package co.com.firefly.daviviendatrade.firebase.model;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by toshiba on 29/06/2016.
 */
@IgnoreExtraProperties
public class Equity {

    private String equity;
    private String value;
    public String uid;
    private Map<String, Boolean> stars = new HashMap<>();
    public int starCount = 0;
    public Equity(){

    }

    public Equity(String uid, String equity, String value){
        this.uid = uid;
        this.equity = equity;
        this.value = value;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("equity", equity);
        result.put("value", value);
        result.put("stars", stars);

        return result;
    }
    // [END post_to_map]

    public String getEquity() {
        return equity;
    }

    public void setEquity(String equity) {
        this.equity = equity;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Map<String, Boolean> getStars() {
        return stars;
    }

    public void setStars(Map<String, Boolean> stars) {
        this.stars = stars;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
