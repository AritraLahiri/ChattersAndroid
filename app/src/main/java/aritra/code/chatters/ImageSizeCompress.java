package aritra.code.chatters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;

public class ImageSizeCompress {


    public static byte[] compressImage(Uri imageUrl, Context context) {
        try {
            Bitmap bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUrl);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
            byte[] final_image = baos.toByteArray();
            return final_image;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
