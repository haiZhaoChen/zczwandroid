package org.bigdata.zczw.entity;

/**
 * Created by SVN on 2017/12/28.
 */

public class ThemeFlag {
    /**
     * id : 1
     * flag : 1
     * themeId : 14
     * themeOutline : （2017.11.25-2017.12.25）
     * themeTitle : 张承政务-立伟关注
     */

    private int id;
    private String flag;
    private int themeId;
    private String themeOutline;
    private String themeTitle;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }

    public String getThemeOutline() {
        return themeOutline;
    }

    public void setThemeOutline(String themeOutline) {
        this.themeOutline = themeOutline;
    }

    public String getThemeTitle() {
        return themeTitle;
    }

    public void setThemeTitle(String themeTitle) {
        this.themeTitle = themeTitle;
    }
}
