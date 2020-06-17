package com.gapbe.base;


import org.apache.commons.lang3.StringUtils;

import java.io.FileInputStream;
import java.util.Properties;

public enum Constants {
    CREATE_CONSIGNMENT,
    GET_CONSIGNMENT_FOR_PARTNER,
    ECOM_PLATFORM_AUTH,
    ECOM_API_AUTH,
    DELIVERY_API_AUTH,
    GET_CONSIGNMENT_VIA_DELIVERY_API,
    RIDER_STATUS_UPDATE,
    COLLECTOR_PASSCODE,
    COLLECTOR_LOGIN,
    COLLECT_CONSIGNMENT,
    CONSIGNMENT_HANDOVER;

    private Properties properties;

    Constants() {
        properties = new Properties();
        try {
            if (StringUtils.isEmpty(System.getProperty("env"))) {
                properties.load(new FileInputStream("src/test/resources/config/stage.cfg"));
            } else {
                properties.load(
                        new FileInputStream("src/test/resources/config/" + System.getProperty("env") + ".cfg"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String value() {
        return properties.getProperty(this.name());
    }

}
