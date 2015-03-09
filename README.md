MOBILE SECURITY SOLUTION IN CLOUD

This project deals in realizing a cheaper alternative for CCTV cameras, by using android device.

The device captures the picture, uploads in a predefined interval (session) to the cloud.

Also user gets a web page to view all the images based on some filters.

Components:

1. Android Application - Secure
2. Web Application - SecureUI
3. Rest Backend service - RestService

AWS Service used:

1. S3 - Stores the captured images.
2. RDS - Store the user and session tables.
3. EC2 - Virtual server that runs tomcat.
4. EBS - Provides the block storage for the EC2 instance.
5. CloudFront - Content delivery network to boost the image retrieval.

Deployment:

Secure.apk - Deployed to a compatible android phone.
SecureUI.war - Deployed to a HTTP Server (Apache Tomcat) running in Amazon EC2.
RestService.war - Deployed to a HTTP Server (Apache Tomcat) running in Amazon EC2.
