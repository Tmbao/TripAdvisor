package app.tmbao.travel_assistance.invisible_components.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import app.tmbao.travel_assistance.invisible_components.models.LandscapeResource;
import app.tmbao.travel_assistance.invisible_components.networks.Requests;


/**
 * Created by tmbao on 8/28/2015.
 */
public class MediaHelpers {
    public static String getApplicationMediaDirectory() throws IOException {
        File mediaStorageDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Trip Advisor");

        if (!mediaStorageDirectory.exists())
            if (!mediaStorageDirectory.mkdir())
                return null;

        return mediaStorageDirectory.getCanonicalPath();
    }

    public static String getDownloadedResourcesDirectory() throws IOException {
        File downloadedResourcesDirectory = new File(getApplicationMediaDirectory(), "Downloaded Resource");

        if (!downloadedResourcesDirectory.exists())
            if (!downloadedResourcesDirectory.mkdir())
                return null;

        return downloadedResourcesDirectory.getCanonicalPath();
    }

    public static String getResourceImagePath(String imageUrl) throws IOException, NoSuchAlgorithmException {
        return new File(getDownloadedResourcesDirectory(), MD5(imageUrl)).getCanonicalPath();
    }

    public static Bitmap getLandscapeResourceImage(LandscapeResource resource) throws IOException, NoSuchAlgorithmException {
        File imageFile = new File(getResourceImagePath(resource.getContent()));
        if (!imageFile.exists())
            Requests.download(resource.getContent(), getResourceImagePath(resource.getContent()));
        return BitmapFactory.decodeFile(getResourceImagePath(resource.getContent()));
    }

    public static File getLandscapeResourceFile(LandscapeResource resource) throws IOException, NoSuchAlgorithmException {
        File imageFile = new File(getResourceImagePath(resource.getContent()));
        if (!imageFile.exists())
            Requests.download(resource.getContent(), getResourceImagePath(resource.getContent()));
        return imageFile;
    }

    private static String MD5(String md5) throws NoSuchAlgorithmException {
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        byte[] array = md.digest(md5.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; ++i)
            sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
        return sb.toString();
    }
}
