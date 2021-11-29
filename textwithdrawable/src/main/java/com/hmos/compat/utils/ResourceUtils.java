package com.hmos.compat.utils;

import ohos.agp.components.element.Element;
import ohos.agp.components.element.PixelMapElement;
import ohos.app.Context;
import ohos.global.resource.Resource;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.PixelFormat;
import ohos.media.image.common.Size;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Resource Util class.
 */
public class ResourceUtils {
    private static final String ACTION_1 = "Exception";

    private ResourceUtils() {
        throw new IllegalStateException("ResourceUtils class");
    }

    /**
     * getDrawable.
     *
     * @param context context.
     * @param mresource Resource.
     * @return drawable.
     */
    public static Element getDrawable(Context context, int mresource) {
        Element drawable = null;
        if (mresource != 0) {
            try {
                Resource resource = context.getResourceManager().getResource(mresource);
                drawable = prepareElement(resource);
            } catch (Exception e) {
                Logger.getGlobal().log(Level.INFO, ACTION_1);
            }
        }
        return drawable;
    }

    public static PixelMapElement prepareElement(Resource resource) throws IOException {
        return new PixelMapElement(preparePixelMap(resource));
    }

    /**
     * PixelMap.
     *
     * @param resource resource.
     * @return imagesource.
     * @throws IOException io.
     */
    public static PixelMap preparePixelMap(Resource resource) throws IOException {
        ImageSource.SourceOptions srcOpts = new ImageSource.SourceOptions();
        ImageSource imageSource = null;
        try {
            imageSource = ImageSource.create(readResource(resource), srcOpts);
        } catch (Exception e) {
            Logger.getGlobal().log(Level.INFO, ACTION_1);
        } finally {
            close(resource);
        }
        if (imageSource == null) {
            throw new FileNotFoundException();
        }
        ImageSource.DecodingOptions decodingOpts = new ImageSource.DecodingOptions();
        decodingOpts.desiredSize = new Size(0, 0);
        decodingOpts.desiredRegion = new ohos.media.image.common.Rect(0, 0, 0, 0);
        decodingOpts.desiredPixelFormat = PixelFormat.ARGB_8888;

        return (imageSource.createPixelmap(decodingOpts));
    }

    private static byte[] readResource(Resource resource) {
        final int bufferSize = 1024;
        final int ioEnd = -1;

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[bufferSize];
        while (true) {
            try {
                int readLen = resource.read(buffer, 0, bufferSize);
                if (readLen == ioEnd) {
                    break;
                }
                output.write(buffer, 0, readLen);
            } catch (IOException e) {
                Logger.getGlobal().log(Level.INFO, ACTION_1);
            }
        }
        return output.toByteArray();
    }

    private static void close(Resource resource) {
        if (resource != null) {
            try {
                resource.close();
            } catch (IOException e) {
                Logger.getGlobal().log(Level.INFO, ACTION_1);
            }
        }
    }
}