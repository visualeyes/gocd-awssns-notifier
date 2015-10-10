package com.careerhub.go.plugin;

public class PluginSettings {
    private final String awsRegion;
    private final String snsTopic;
    
	public PluginSettings(String awsRegion, String snsTopic) {
		this.awsRegion = awsRegion;
		this.snsTopic = snsTopic;
	}

	public String getRegion() {
		return awsRegion;
	}

	public String getSnsTopic() {
		return snsTopic;
	}
}
