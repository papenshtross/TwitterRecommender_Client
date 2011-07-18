package org.linnaeus.overlay;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.WindowManager;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import org.linnaeus.R;
import org.linnaeus.activity.MainActivity;
import org.linnaeus.bean.SearchCircle;
import org.linnaeus.manager.RequestManager;
import org.linnaeus.util.LocationUtils;

import static android.graphics.Paint.Style;

/**
 * Created by IntelliJ IDEA.
 * User: Romchee
 * Date: 16/07/11
 * Time: 16:17
 * To change this template use File | Settings | File Templates.
 */
public class SearchCircleOverlay extends Overlay {

    private MainActivity view;

    private static final int INVALID_POINTER_ID = -1;
    private static final float MIN_CIRCLE_DIAMETER = 10.0f;
    private static float MAX_CIRCLE_DIAMETER;

    private Drawable mIcon;
    private float mPosX;
    private float mPosY;

    private float firstPointerStartX;
    private float firstPointerStartY;

    private float mLastTouchX;
    private float mLastTouchY;
    private int mActivePointerId = INVALID_POINTER_ID;

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    private AlertDialog.Builder ad;

    public SearchCircleOverlay(MainActivity view) {
        super();
        this.view = view;
        mIcon = view.getResources().getDrawable(R.drawable.icon);
        mIcon.setBounds(0, 0, mIcon.getIntrinsicWidth(), mIcon.getIntrinsicHeight());

        mScaleDetector = new ScaleGestureDetector(view, new ScaleListener());

        Display display = ((WindowManager) view.
                getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        MAX_CIRCLE_DIAMETER = display.getWidth();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent, MapView mapView) {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(motionEvent);

        final int action = motionEvent.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = motionEvent.getX();
                final float y = motionEvent.getY();

                firstPointerStartX = x;
                firstPointerStartY = y;
                mLastTouchX = x;
                mLastTouchY = y;
                mActivePointerId = motionEvent.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_POINTER_1_DOWN: {
                final float x = motionEvent.getX(1);
                final float y = motionEvent.getY(1);

                mPosX = (firstPointerStartX + x) / 2;
                mPosY = (firstPointerStartY + y) / 2;

                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = motionEvent.findPointerIndex(mActivePointerId);
                final float x = motionEvent.getX(pointerIndex);
                final float y = motionEvent.getY(pointerIndex);

                // Only move if the ScaleGestureDetector isn't processing a gesture.
                if (!mScaleDetector.isInProgress()) {
                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;

                    mPosX += dx;
                    mPosY += dy;

                    //view.invalidate();
                }

                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                if (mScaleFactor > MIN_CIRCLE_DIAMETER / 2) {
                    showConfirmDialog();
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = (motionEvent.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = motionEvent.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = motionEvent.getX(newPointerIndex);
                    mLastTouchY = motionEvent.getY(newPointerIndex);
                    mActivePointerId = motionEvent.getPointerId(newPointerIndex);
                }
                break;
            }
        }
        return true;
    }

    @Override
    public void draw(Canvas canvas, MapView mapV, boolean shadow) {

        Paint circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        circlePaint.setColor(0x30000000);
        circlePaint.setStyle(Style.FILL_AND_STROKE);
        canvas.drawCircle(mPosX, mPosY, mScaleFactor, circlePaint);

        circlePaint.setColor(0x99000000);
        circlePaint.setStyle(Style.STROKE);
        canvas.drawCircle(mPosX, mPosY, mScaleFactor, circlePaint);
        super.draw(canvas, mapV, shadow);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor = detector.getCurrentSpan();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(MIN_CIRCLE_DIAMETER, Math.min(mScaleFactor, MAX_CIRCLE_DIAMETER)) / 2;

            //view.invalidate();
            return true;
        }
    }

    public void showConfirmDialog() {
        if (ad == null) {
            ad = new AlertDialog.Builder(view);
            ad.setTitle(view.getString(R.string.search_circle_dialog_title));
            ad.setMessage(view.getString(R.string.search_circle_dialog_message));
            ad.setPositiveButton(view.getString(R.string.search_circle_dialog_button_trends),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            requestTrends();
                        }
                    });
            ad.setNegativeButton(view.getString(R.string.search_circle_dialog_button_cancel),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            cancelSearch();
                        }
                    });
            ad.setNeutralButton(view.getString(R.string.search_circle_dialog_button_recommend),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int arg1) {
                            requestRecommendation();
                        }
                    });
        }
        ad.show();
    }

    private void cancelSearch() {
        removeSearchOverlay();
    }

    private void requestRecommendation() {
        removeSearchOverlay();
        SearchCircle searchCircle = getSearchCircle();
        RequestManager.getInstance(view).requestRecommendation(searchCircle);
    }

    private void requestTrends() {
        removeSearchOverlay();
        SearchCircle searchCircle = getSearchCircle();
        RequestManager.getInstance(view).requestTrends(searchCircle);
    }

    private void removeSearchOverlay() {
        view.getMapView().getOverlays().remove(this);
    }

    private SearchCircle getSearchCircle() {
        SearchCircle searchCircle = new SearchCircle();
        GeoPoint circleCenter = view.getMapView().
                getProjection().fromPixels((int) mPosX, (int) mPosY);
        GeoPoint circleHoop = view.getMapView().
                getProjection().fromPixels((int) (mPosX + mScaleFactor), (int) mPosY);
        int radiusInMeters = LocationUtils.
                distanceBetweenGeoPoints(circleCenter, circleHoop);
        searchCircle.setLat(circleCenter.getLatitudeE6());
        searchCircle.setLng(circleCenter.getLongitudeE6());
        searchCircle.setDistance(radiusInMeters);
        return searchCircle;
    }
}