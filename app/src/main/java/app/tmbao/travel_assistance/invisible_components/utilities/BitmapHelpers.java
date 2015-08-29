package app.tmbao.travel_assistance.invisible_components.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;

import com.google.android.gms.maps.model.BitmapDescriptor;

import java.io.ByteArrayOutputStream;

/**
 * Created by tmbao on 8/25/2015.
 */
public class BitmapHelpers {
    public static String toBase64(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        } else
            return "";
    }

    public static Bitmap fromBase64(String encoded) {
        if (encoded.compareTo("") != 0) {
            byte[] decoded = Base64.decode(encoded, 0);
            return BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
        } else
            return null;
    }

    public static Bitmap getBitmapFromImageView(ImageView imageView) {
        try {
            return ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } catch (Exception e){
            return null;
        }
    }
}
