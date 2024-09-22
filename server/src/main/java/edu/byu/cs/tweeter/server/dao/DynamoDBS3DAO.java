package edu.byu.cs.tweeter.server.dao;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.util.Base64;

public class DynamoDBS3DAO implements S3DAO {

    /**
     * Uploads an image to AWS S3 and returns the URL of the uploaded image.
     *
     * This method takes a base64 encoded string of an image and an alias,
     * converts the string to a byte array, and uploads it to an S3 bucket.
     * The image is stored in the bucket with public read access. This method
     * is typically used for uploading user profile images as part of the
     * user registration or update process.
     *
     * @param imageString The base64 encoded string of the image to be uploaded.
     * @param alias       The unique identifier for the image in the S3 bucket,
     *                    typically the username or a UUID.
     * @return            The URL of the uploaded image in the S3 bucket.
     * @throws            RuntimeException If the image upload process fails.
     */
    public String uploadImageToS3(String imageString, String alias) {
        AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withRegion("us-east-1") // Set to your S3 region
                .build();

        byte[] byteArray = Base64.getDecoder().decode(imageString);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(byteArray.length);
        metadata.setContentType("image/jpeg");

        PutObjectRequest request = new PutObjectRequest(
                "ruilinfirstbucket", // Your bucket name
                alias,
                new ByteArrayInputStream(byteArray),
                metadata
        ).withCannedAcl(CannedAccessControlList.PublicRead);

        s3.putObject(request);

        return "https://ruilinfirstbucket.s3.us-east-1.amazonaws.com/" + alias;
    }
}
