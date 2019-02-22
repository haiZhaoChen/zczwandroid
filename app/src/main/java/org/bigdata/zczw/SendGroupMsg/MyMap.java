package org.bigdata.zczw.SendGroupMsg;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/6 0006.
 */
public class MyMap implements Serializable {
    private Map<String, Boolean> booleanMap;

    public Map<String, Boolean> getBooleanMap() {
        return booleanMap;
    }

    public void setBooleanMap(Map<String, Boolean> booleanMap) {
        this.booleanMap = booleanMap;
    }
}
