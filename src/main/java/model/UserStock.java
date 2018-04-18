package model;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

import java.io.IOException;
import java.math.BigDecimal;

public class UserStock {
    private String stockName;
    private Stock stock;
    private int quantity;

    public UserStock(String stockName, int quantity) {
        this.stockName = stockName;
        this.quantity = quantity;
        try {
            stock = YahooFinance.get(this.stockName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getStockName() {
        return stockName;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return stock.getQuote().getPrice().doubleValue();
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalValue() {
        return stock.getQuote().getPrice().multiply(new BigDecimal(this.quantity)).doubleValue();

    }
}
