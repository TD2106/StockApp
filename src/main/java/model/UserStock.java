package model;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;

public class UserStock {
    private String stockName;
    private Stock stock;
    private int quanity;
    public UserStock(String stockName,int quanity){
        this.stockName = stockName;
        this.quanity = quanity;
        try {
            stock = YahooFinance.get(this.stockName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getStockName() {
        return stockName;
    }

    public int getQuanity() {
        return quanity;
    }

    public BigDecimal getPrice(){
        return stock.getQuote().getPrice();
    }

    public void setQuanity(int quanity) {
        this.quanity = quanity;
    }

    public BigDecimal getTotalValue(){
        return stock.getQuote().getPrice().multiply(new BigDecimal(this.quanity));

    }
}
