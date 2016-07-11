package co.com.firefly.daviviendatrade.portfolio;

import java.io.Serializable;

/**
 * Created by toshiba on 11/07/2016.
 */
public class PortfolioEquity implements Serializable {

    private String equityName;
    private Double equityBuyingValue;
    private Double equityCurrentValue;
    private Integer equityQuantity;
    private Double equityCurrentTotal;

    public PortfolioEquity(){

    }

    public String getEquityName() {
        return equityName;
    }

    public void setEquityName(String equityName) {
        this.equityName = equityName;
    }

    public Double getEquityBuyingValue() {
        return equityBuyingValue;
    }

    public void setEquityBuyingValue(Double equityBuyingValue) {
        this.equityBuyingValue = equityBuyingValue;
    }

    public Double getEquityCurrentValue() {
        return equityCurrentValue;
    }

    public void setEquityCurrentValue(Double equityCurrentValue) {
        this.equityCurrentValue = equityCurrentValue;
    }

    public Integer getEquityQuantity() {
        return equityQuantity;
    }

    public void setEquityQuantity(Integer equityQuantity) {
        this.equityQuantity = equityQuantity;
    }

    public Double getEquityCurrentTotal() {
        return equityCurrentTotal;
    }

    public void setEquityCurrentTotal(Double equityCurrentTotal) {
        this.equityCurrentTotal = equityCurrentTotal;
    }
}
