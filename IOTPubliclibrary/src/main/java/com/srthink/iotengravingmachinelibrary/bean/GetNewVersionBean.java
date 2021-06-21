package com.srthink.iotengravingmachinelibrary.bean;

/**
 * @author liwanlian
 * @date 2021/6/15 16:12
 */
public class GetNewVersionBean {
    /**
     * id : 1174578282184572928
     * upgradeType : upgradeDevFirmWare
     * upgradeVersion : 1.0.2
     */

    private String id;
    private String upgradeType;
    private String upgradeVersion;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpgradeType() {
        return upgradeType;
    }

    public void setUpgradeType(String upgradeType) {
        this.upgradeType = upgradeType;
    }

    public String getUpgradeVersion() {
        return upgradeVersion;
    }

    public void setUpgradeVersion(String upgradeVersion) {
        this.upgradeVersion = upgradeVersion;
    }
}
