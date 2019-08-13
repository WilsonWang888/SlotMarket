package com.slotmarket;

public class StockSymbol {
    private String symbol;
    private String exchange;
    private boolean enabled;

    public StockSymbol(String symbol, String exchange){
        this.symbol = symbol;
        this.exchange = exchange;
        this.enabled = true;
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
