package io.jsbxyyx.cutleek.domain;

import java.util.Objects;

public class Stock {

    private String code;
    private String name;
    private String buyPrice;
    private String now;
    private String close; // 昨收
    private String open; // 今开
    private String change;//涨跌
    private String changePercent;
    private String time;

    private String myChange; // 累计盈亏率

    public Stock() {
    }

    public Stock(String code) {
        this.code = code;
        this.name = "--";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(String buyPrice) {
        this.buyPrice = buyPrice;
    }

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(String changePercent) {
        this.changePercent = changePercent;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMyChange() {
        return myChange;
    }

    public void setMyChange(String myChange) {
        this.myChange = myChange;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stock bean = (Stock) o;
        return Objects.equals(code, bean.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

}