package com.o2o.utils;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

    private String[] encryptPropNames = { "jdbc.username", "jdbc.password" };

    @Override
    protected String convertProperty(String propertyName, String propertyValue) {
        if (isEncryptProp(propertyName)) {
            String decryptValue = DESUtil.getDecryptString(propertyValue);
            return decryptValue;
        } else {
            return propertyValue;
        }
    }

    private boolean isEncryptProp(String propertyName) {
        for (String encryptPropName : encryptPropNames) {
            if(encryptPropName.equals(propertyName)) {
                return true;
            }
        }
        return false;
    }
}
