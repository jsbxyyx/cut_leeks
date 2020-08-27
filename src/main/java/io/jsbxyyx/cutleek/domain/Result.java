package io.jsbxyyx.cutleek.domain;

import com.google.gson.annotations.SerializedName;

/**
 * @author jsbxyyx
 */
public class Result<T> {

    @SerializedName("Datas")
    private T datas;
    @SerializedName("ErrCode")
    private Integer errCode;
    @SerializedName("ErrMsg")
    private String errMsg;
    @SerializedName("TotalCount")
    private Integer totalCount;
    @SerializedName("Expansion")
    private Object expansion;

    public T getDatas() {
        return datas;
    }

    public void setDatas(T datas) {
        this.datas = datas;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Object getExpansion() {
        return expansion;
    }

    public void setExpansion(Object expansion) {
        this.expansion = expansion;
    }
}
