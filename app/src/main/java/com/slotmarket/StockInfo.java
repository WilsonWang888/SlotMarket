package com.slotmarket;

import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class StockInfo {
    private String stock;
    private String symbol;
    private String exchange;
    private double value;
    private double opening;
    private String logoUrl;

    public StockInfo(){
    }

    public void updateInfo(RequestQueue queue){
        JsonObjectRequest infoRequest = new JsonObjectRequest(
                Request.Method.GET,
                "https://cloud.iexapis.com/stable/stock/" + symbol + "/quote?token=pk_5e6f01a62c7e43c2baac7d44cd2d5aa8",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            stock = response.getString("companyName");
                            value = response.getDouble("latestPrice");
                            opening = response.getDouble("open");
                            exchange = response.getString("primaryExchange");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        queue.add(infoRequest);
    }

    public String getStock() {
        return stock;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getExchange() {
        return exchange;
    }

    public double getValue() {
        return value;
    }

    public double getOpening() {
        return opening;
    }

    public String getLogoUrl() {
        return logoUrl;
    }
}
