package com.cloud.secure.s3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.codec.binary.Base64;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.cloud.secure.AppGlobals;
import com.cloud.secure.pojo.PutImageRequest;

public class S3 {

	public static void put(PutImageRequest req) throws IOException {
		// Create a PutObject request
		final String key = req.getUserId() + "/" + req.getSessionId() + "/"
				+ getFileName(req);
		final String fileName = req.getImageId() + ".jpg";
		final FileOutputStream fos = new FileOutputStream(fileName);
		fos.write(Base64.decodeBase64(req.getData()));
		fos.close();
		final File file = new File(fileName);
		AppGlobals.s3.putObject(new PutObjectRequest(AppGlobals.S3_BUCKET_NAME,
				key, file).withCannedAcl(CannedAccessControlList.PublicRead));
		file.delete();
	}

	public static String getFileName(PutImageRequest req) {
		// creates the file name for the pic.
		return req.getImageId()
				+ "_"
				+ req.getSessionId()
				+ "_"
				+ req.getSnapedAt().replaceAll("[:-]", "_")
						.replaceAll(" ", "_") + ".jpg";
	}

	public static List<String> getImages(final String prefix) {
		final ListObjectsRequest req = new ListObjectsRequest()
				.withBucketName(AppGlobals.S3_BUCKET_NAME).withPrefix(prefix)
				.withDelimiter("/");
		final ObjectListing olist = AppGlobals.s3.listObjects(req);
		final TreeMap<Date, String> dateSortedMap = new TreeMap<Date, String>();
		for (final S3ObjectSummary image : olist.getObjectSummaries())
			dateSortedMap.put(image.getLastModified(), image.getKey());
		final List<String> imageList = new ArrayList<String>();
		for (final Entry<Date, String> entry : dateSortedMap.entrySet())
			imageList.add(entry.getValue().split("/")[2]);
		return imageList;
	}

}
