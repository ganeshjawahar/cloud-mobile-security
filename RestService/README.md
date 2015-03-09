RestService- MOBILE SECURITY SOLUTION IN CLOUD

RestService is a REST based back end service, developed completely in Java.

It uses Jersey library to parse and generate the JSON request and responses respectively.

Following are the Rest endpoints exposed by the service:

1. Device login:

Request-

POST 'post/loginInfo'
Content-Type: application/json
{
 "userid":"library",
 "password":"abxc3xx"
}

Response-
{
 "name": "library",
 "userid": "library",
 "clickDelayInSeconds": "20",
 "uploadIntervalInSeconds": "20",
 "password": "678",
 "message": "Login Success.",
 "code": "200"
}

2. Sign up for new device:

Request-

POST 'post/signUpInfo'
Content-Type: application/json
{
 "name":"nivas",
 "id":"admin",
 "clickDelayInSeconds":"30",
 "uploadIntervalInSeconds":"50",
 "password": "6781"
}

Response-

{
 "message": "Signup success.",
 "code": "200"
}

3. Create a session (done by uploader before initiating the actual upload):

Request-

POST 'post/createSessionInfo'
Content-Type: application/json
{
 "userId":"admin",
 "startTime":"2014-12-12 00:00:00"
}

Response-

{
 "sessionId": "7e65ec99-c952-4768-a88c-b551e4ed6837",
 "message": "Session created successfully.",
 "code": "200"
}

4. Upload the image (done by uploader after creating a session):

Request-

POST 'post/createSessionInfo'
Content-Type: application/json
{
 "userId":"admin",
 "sessionId":"7e65ec99-c952-4768-a88c-b551e4ed6837",
 "snapedAt":"2014-12-12 00:00:00",
 "location":"centre-iiith",
 "data":"base64 rep of data"
}

Response-

{
 "message": "Image Upload success.",
 "code": "200"
}

5. Get the images based on filters (request comes from SecureUI web page):

Request-

POST 'post/createSessionInfo'
Content-Type: application/json
{
 "dateStart":"2014-11-20 00:00:00",
 "dateEnd":"2014-12-21 00:55:00",
 "locationList":["centre-iiith"],
 "userList":["sh1","himalaya"]
}

Response-

{
 "userList": [
  {
   "count": 1,
   "userid": "admin",
   "imgSet": [
    {
     "count": 1,
     "day": "2014-11-12",
     "imgs": [
      {
       "location": "centre-iiith",
       "snapedAt": "2014-11-12 0:0:0",
       "imageId": "24ec2bfb-8091-42cb-a4b1-f7e7b9580226",
       "resourcePath": "https://d3iq5ru0c12fmp88.cloudfront.net/mobilesecurity/canteen_center/dc6c3872-89d6-48ff-8234-8d1245c71381/c1d9cf0f-5076-469e-afc_dc6c3872-89d6-48ff-8d1245c71381_2014_09_23_08_00_00.jpg"
      }]
    }]
  }],
 "message": "Successfully fetched records.",
  "code": "200"
}

6. Get the unique user list (request comes from SecureUI web page):

Request-

GET 'get/users'

Response-

{
 "userList": [
  "admin",
  "entrance",
  "kadamba nivas",
  "library",
  "obh north mess",
  "sh1" ],
 "message": "Success",
 "code": "200"
}

NOTE-
This project is built using Maven 3.2.3.