package org.linnaeus.overlay;

import android.content.Context;
import android.graphics.*;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import org.linnaeus.R;
import org.linnaeus.util.MyLocation;

/**
 * Created by IntelliJ IDEA.
 * User: Romchee
 * Date: 16/07/11
 * Time: 15:25
 * To change this template use File | Settings | File Templates.
 */
public class CurrentLocationOverlay extends Overlay {

    private Context context;

    public CurrentLocationOverlay(Context context) {
        this.context = context;
    }

    @Override
        public boolean draw(Canvas canvas, MapView mapView, boolean shadow, long when) {
            GeoPoint location = MyLocation.getLastLocation();
            if (location != null){
                Paint paint = new Paint();

                super.draw(canvas, mapView, shadow);
                // Converts lat/lng-Point to OUR coordinates on the screen.
                Point myScreenCoords = new Point();

                mapView.getProjection().toPixels(location, myScreenCoords);

                paint.setStrokeWidth(1);
                paint.setARGB(255, 255, 255, 255);
                paint.setStyle(Paint.Style.STROKE);

                Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.emo_im_cool);

                canvas.drawBitmap(bmp, myScreenCoords.x - bmp.getWidth() / 2,
                        myScreenCoords.y - bmp.getHeight() / 2, paint);
            }
            return true;
        }
    }
