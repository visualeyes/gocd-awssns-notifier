package com.careerhub.go.plugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.RegionUtils;
import org.apache.commons.io.IOUtils;

public class ConfigurationService {
	
	public Map<String, Object> getFields() {
        final Map<String, Object> fields = new HashMap<String, Object>();

        fields.put(Constants.PLUGIN_SETTINGS_AWS_REGION_NAME, createField("Region Name", null, true, false, "0"));
        fields.put(Constants.PLUGIN_SETTINGS_AWS_TOPIC_ARN, createField("Topic ARN", null, true, false, "0"));
		
        return fields;
	}
	
	public List<Map<String, Object>> validateFields(Map<String, String> fields) {
        final List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        final String awsAccessKey = fields.get(Constants.PLUGIN_SETTINGS_AWS_ACCESS_KEY);
        final String awsSecretKey = fields.get(Constants.PLUGIN_SETTINGS_AWS_SECRET_KEY);
        final String awsRegionName = fields.get(Constants.PLUGIN_SETTINGS_AWS_REGION_NAME);
        final String awsTopicName = fields.get(Constants.PLUGIN_SETTINGS_AWS_TOPIC_ARN);

        result.add(ValidateRequiredString(awsAccessKey, "Access Key", Constants.PLUGIN_SETTINGS_AWS_ACCESS_KEY));
        result.add(ValidateRequiredString(awsSecretKey, "Secret Key", Constants.PLUGIN_SETTINGS_AWS_SECRET_KEY));

        final Map<String, Object> awsRegionNameValidation = new HashMap<String, Object>();

        ValidateRequiredString(awsRegionName, "Region Name", Constants.PLUGIN_SETTINGS_AWS_REGION_NAME, awsRegionNameValidation);

        final Region region = RegionUtils.getRegion(awsRegionName);

        if(region == null || region.isServiceSupported("sns")) {
            awsRegionNameValidation.put("key", Constants.PLUGIN_SETTINGS_AWS_REGION_NAME);
            awsRegionNameValidation.put("message", String.format("'%s' is not a valid region or doesn't support sns", "Region Name"));
        }
        
        result.add(awsRegionNameValidation);

        result.add(ValidateRequiredString(awsTopicName, "Topic ARN", Constants.PLUGIN_SETTINGS_AWS_TOPIC_ARN));

        return result;
	}

	public Map<String, Object> getTemplateSettings() throws IOException {
		final Map<String, Object> response = new HashMap<String, Object>();
        
        response.put("template", IOUtils.toString(getClass().getResourceAsStream("/plugin-settings.template.html"), "UTF-8"));
		
        return response;
	}

    private Map<String, Object> createField(String displayName, String defaultValue, boolean isRequired, boolean isSecure, String displayOrder) {
    	final Map<String, Object> fieldProperties = new HashMap<String, Object>();
    	
        fieldProperties.put("display-name", displayName);
        fieldProperties.put("default-value", defaultValue);
        fieldProperties.put("required", isRequired);
        fieldProperties.put("secure", isSecure);
        fieldProperties.put("display-order", displayOrder);
        
        return fieldProperties;
    }

    private Map<String, Object> ValidateRequiredString(String str, String displayName, String key) {
        final Map<String, Object> validation = new HashMap<String, Object>();
        ValidateRequiredString(str, displayName, key, validation);

        return  validation;
    }

    private void ValidateRequiredString(String str, String displayName, String key, Map<String, Object> validation) {
        if (str == null || str.isEmpty()) {
            validation.put("key", key);
            validation.put("message", String.format("'%s' is a required field", displayName));
        }
    }

}
