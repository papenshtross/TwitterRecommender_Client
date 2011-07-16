package org.linnaeus.overlay;

import android.content.Context;
import android.graphics.*;
import android.location.Geocoder;
import android.view.MotionEvent;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.Projection;
import org.linnaeus.R;

import static android.graphics.Paint.Style;

/**
 * Created by IntelliJ IDEA.
 * User: Romchee
 * Date: 16/07/11
 * Time: 16:17
 * To change this template use File | Settings | File Templates.
 */
public class SearchCircleOverlay extends Overlay {

    private Geocoder geoCoder = null;
    private Context context;
    private int selectedLatitude;
    private int selectedLongitude;
    private GeoPoint globalGeoPoint;
    private boolean draw = false;

    public SearchCircleOverlay(Context context) {
        super();
        this.context = context;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        if (motionEvent.getAction() == MotionEvent.ACTION_POINTER_DOWN){

        }
        return super.onTouchEvent(motionEvent, mapView);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public boolean onTap(GeoPoint geoPoint, MapView mapView){
        selectedLatitude = geoPoint.getLatitudeE6();
        selectedLongitude = geoPoint.getLongitudeE6();
        globalGeoPoint = geoPoint;
        draw = true;
        return super.onTap(geoPoint,mapView);
    }

    @Override
    public void draw(Canvas canvas, MapView mapV, boolean shadow){

        if(shadow && draw){
            Projection projection = mapV.getProjection();
            Point pt = new Point();
            projection.toPixels(globalGeoPoint, pt);

            GeoPoint newGeos = new GeoPoint(selectedLatitude + (100), selectedLongitude); // adjust your radius accordingly
            Point pt2 = new Point();
            projection.toPixels(newGeos,pt2);
            float circleRadius = Math.abs(pt2.y-pt.y);

            Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

            circlePaint.setColor(0x30000000);
            circlePaint.setStyle(Style.FILL_AND_STROKE);
            canvas.drawCircle((float)pt.x, (float)pt.y, circleRadius, circlePaint);

            circlePaint.setColor(0x99000000);
            circlePaint.setStyle(Style.STROKE);
            canvas.drawCircle((float)pt.x, (float)pt.y, circleRadius, circlePaint);

            Bitmap markerBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.emo_im_cool);
            canvas.drawBitmap(markerBitmap,pt.x,pt.y-markerBitmap.getHeight(),null);

            super.draw(canvas,mapV,shadow);
        }
    }
}