package com.cloud.secure;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

public class AppGlobals {
	// fields made empty, for confidentiality.
	public static final String USER_NAME = "";
	public static final String PASSWORD = "";
	public static final String S3_BUCKET_NAME = "";
	public static final String CLOUD_FRONT_PREFIX = "";
	public static final String RDS_IP="";

	public static AmazonS3 s3 = null;
	static {
		final BasicAWSCredentials credentials = new BasicAWSCredentials(
				AppGlobals.USER_NAME, AppGlobals.PASSWORD);
		s3 = new AmazonS3Client(credentials);
		s3.setRegion(Region.getRegion(Regions.US_WEST_2));
		s3.setEndpoint("s3-us-west-2.amazonaws.com");
	}

}
