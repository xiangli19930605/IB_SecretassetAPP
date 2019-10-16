package me.jessyan.armscomponent.commonsdk.data.prefs;


/**
 * @author flymegoc
 * @date 2018/3/4
 */

public interface PreferenceHelper {

    /**
     * Get night mode state
     *
     * @return if is night mode
     */
    boolean getNightModeState();

    /**
     * Set night mode state
     *
     * @param b current night mode state
     */
    void setNightModeState(boolean b);

    void setLocation(String location);

    String getLocation();

    //获取USB状态
    void setUsbState(boolean b);

    boolean getUsbState();

    void setIp(String ip);

    String getIp();

    void setPort(String port);

    String getPort();

}
