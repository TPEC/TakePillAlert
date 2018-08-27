package pers.tpec.takepills;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TPInfo {
    private static final DateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);

    private long time;
    private int amount;
    private String tag;

    public TPInfo() {
        this(new Date().getTime(), 1, "");
    }

    public TPInfo(long time, int amount, String tag) {
        this.time = time;
        this.amount = amount;
        this.tag = tag;
    }

    public String getDateString() {
        Date date = new Date(time);
        return dateFormat.format(date);
    }

    public String getAmountString() {
        return "数量：" + String.valueOf(amount);
    }

    public String getTagString() {
        if (tag.isEmpty()) {
            return "-";
        } else {
            return tag;
        }
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject object = new JSONObject();
        object.put("time", time);
        object.put("amount", amount);
        object.put("tag", tag);
        return object;
    }

    public static TPInfo from(JSONObject jsonObject) throws JSONException {
        TPInfo tpInfo = new TPInfo();
        tpInfo.setTime(jsonObject.getLong("time"));
        tpInfo.setAmount(jsonObject.getInt("amount"));
        tpInfo.setTag(jsonObject.getString("tag"));
        return tpInfo;
    }
}
