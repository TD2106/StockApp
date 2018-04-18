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
    
}
