package org.bigdata.zczw.utils;

import android.text.TextPaint;
import android.text.style.UnderlineSpan;

/**
 * Created by SVN on 2018/3/8.
 */

public class NoUnderlineSpan extends UnderlineSpan{
    @Override

    public void updateDrawState(TextPaint ds) {

        ds.setUnderlineText(false);

    }
}
