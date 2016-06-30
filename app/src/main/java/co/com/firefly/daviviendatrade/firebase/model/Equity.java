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
    private Map<String, Boolean> stars = new HashMap<>();
    private int starCount = 0;
    private String percentaje;
    private String spread;
    public Equity(){

    }

    public Equity(String equity, String value, String percentaje, String spread){
        this.equity = equity;
        this.value = value;
        this.percentaje = percentaje;
        this.spread = spread;
    }

    // [START post_to_map]
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("equity", equity);
        result.put("value", value);
        result.put("stars", stars);
        result.put("spread", spread);
        result.put("percentaje", percentaje);

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

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public String getPercentaje() {
        return percentaje;
    }

    public void setPercentaje(String percentaje) {
        this.percentaje = percentaje;
    }

    public String getSpread() {
        return spread;
    }

    public void setSpread(String spread) {
        this.spread = spread;
    }
}
