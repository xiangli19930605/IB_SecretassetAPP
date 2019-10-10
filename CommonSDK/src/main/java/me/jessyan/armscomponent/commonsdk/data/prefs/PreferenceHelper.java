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
//获取USB状态
    void setUsbState(boolean b);

    boolean getUsbState();


}
