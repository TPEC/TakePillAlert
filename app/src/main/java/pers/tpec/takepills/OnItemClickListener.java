package pers.tpec.takepills;


import android.view.View;

public interface OnItemClickListener {
    void set(String key, Object value);

    void click(View view, Object data);

    void longClick(View view, Object data);
}
