package model;

import java.util.ArrayList;

public class Portfolio {
    private ArrayList<UserStock> stocks;

    public Portfolio(ArrayList<UserStock> stocks) {
        this.stocks = stocks;
    }

    public ArrayList<UserStock> getStocks() {
        return stocks;
    }

    public void addStock(UserStock stock) {
        stocks.add(stock);
    }

    public void deleteStock(String stockName) {
        for (UserStock stock : stocks) {
            if (stock.getStockName().equals(stockName)) {
                stocks.remove(stock);
                break;
            }
        }
    }

    public void editStock(String stockName, int quantity) {
        for (UserStock stock : stocks) {
            if (stock.getStockName().equals(stockName)) {
                stock.setQuanity(quantity);
                break;
            }
        }
    }

    public boolean isStockExist(String stockName) {
        for (UserStock stock : stocks) {
            if (stock.getStockName().equals(stockName)) {
                return true;
            }
        }
        return false;
    }

    public double totalWorth() {
        double result = 0;
        for (UserStock stock : stocks) {
            result += stock.getTotalValue().doubleValue();
        }
        return result;
    }
}
