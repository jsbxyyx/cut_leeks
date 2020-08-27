package io.jsbxyyx.cutleek.domain;

import com.google.gson.annotations.SerializedName;

/**
 * @author jsbxyyx
 */
public class FundIntro {

    @SerializedName("FCODE")
    private String fcode;
    @SerializedName("SHORTNAME")
    private String shortname;

    public String getFcode() {
        return fcode;
    }

    public void setFcode(String fcode) {
        this.fcode = fcode;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }
}
