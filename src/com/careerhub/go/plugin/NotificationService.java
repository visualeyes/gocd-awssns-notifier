package com.careerhub.go.plugin;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.careerhub.go.plugin.util.JSONUtils;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;

public class NotificationService {

	
	public Map<String, Object> getNotificationsInterestedIn() {
        Map<String, Object> response = new HashMap<String, Object>();
        response.put("notifications", Arrays.asList(Constants.REQUEST_STAGE_STATUS));
		return response;
	}
	
	public String sendNotification(PluginSettings settings, Map<String, Object> info) throws IOException {
        String accessKey = null;
        String secretKey = null;

        String awsRegion = settings.getRegion();
        Regions region = Regions.fromName(awsRegion);
        String topicArn = settings.getSnsTopic();

		// Could process json and parse processed json on
		// However, at this stage their is no need
		// Just pass on as is.
        String json = JSONUtils.toJSON(info);

        AmazonSNSClient snsClient = new AmazonSNSClient(new BasicAWSCredentials(accessKey, secretKey));
        snsClient.setRegion(Region.getRegion(region));

		//publish to an SNS topic
		PublishRequest publishRequest = new PublishRequest(topicArn, json);
		PublishResult publishResult = snsClient.publish(publishRequest);

    	return publishResult.getMessageId();
	}
}
