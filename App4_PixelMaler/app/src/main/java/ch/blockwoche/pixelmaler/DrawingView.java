package ch.blockwoche.pixelmaler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Comparator;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Die DrawingView ist für die Darstellung und Verwaltung der Zeichenfläche
 * zuständig.
 */
public class DrawingView extends View {

    private static final int GRID_SIZE = 13;

    private Path drawPath = new Path();
    private Paint drawPaint = new Paint();
    private Paint linePaint = new Paint();
    private Paint rectPaint = new Paint();
    private boolean isErasing = false;
    private String selectedColor;
    HashSet<PointColor> points = new HashSet<>();



    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        linePaint.setColor(0xFF666666);
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(1.0f);
        linePaint.setStyle(Paint.Style.STROKE);
        //default color
        selectedColor = "#FF1364b7";
    }

    @Override
    protected void onDraw(Canvas canvas) {

        final int maxX = canvas.getWidth();
        final int maxY = canvas.getHeight();

        final int stepSizeX = (int) Math.ceil((double) maxX / GRID_SIZE);
        final int stepSizeY = (int) Math.ceil((double) maxY / GRID_SIZE);

        rectPaint.setStrokeWidth(10);

        for(PointColor p : points){
            rectPaint.setColor(Color.parseColor(p.getColor()));
            int left = (p.getPoint().x*stepSizeX);
            int top = (p.getPoint().y*stepSizeX);
            int right = left+stepSizeX;
            int bottom = top+stepSizeY;
            canvas.drawRect(left,top,right,bottom,rectPaint);
        }

        // TODO Zeichne das Gitter
        for (int i = 0; i < GRID_SIZE; i++){
            canvas.drawLine(i*stepSizeX, 0, i*stepSizeX, maxY,linePaint);
        }
        for (int i = 0; i < GRID_SIZE; i++){
            canvas.drawLine(0,i*stepSizeY, maxX, i*stepSizeY,linePaint);
        }
        // Zeichnet einen Pfad der dem Finger folgt
        canvas.drawPath(drawPath, drawPaint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        int pixelX = (int)touchX/(getWidth()/13);
        int pixelY= (int)touchY/(getHeight()/13);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);

                // TODO wir müssen uns die berührten Punkte zwischenspeichern
                if(isErasing){
                    points.removeIf(point -> point.getPoint().x == pixelX && point.getPoint().y == pixelY);
                } else {
                    points.add(new PointColor(new Point(pixelX,pixelY),selectedColor));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);

                // TODO wir müssen uns die berührten Punkte zwischenspeichern
                if(isErasing){
                    points.removeIf(point -> point.getPoint().x == pixelX && point.getPoint().y == pixelY);
                } else {
                    points.add(new PointColor(new Point(pixelX,pixelY),selectedColor));
                }

                break;
            case MotionEvent.ACTION_UP:

                // TODO Jetzt können wir die zwischengespeicherten Punkte auf das
                // Gitter umrechnen und zeichnen, bzw. löschen, falls wir isErasing
                // true ist (optional)
                drawPath.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void startNew() {

        // TODO Gitter löschen
        points = new HashSet<>();
        invalidate();
    }

    public void setErase(boolean isErase) {
        isErasing = isErase;
        if (isErasing) {
            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        } else {
            drawPaint.setXfermode(null);
        }
    }

    public boolean isErasing() {
        return isErasing;
    }

    public void setColor(String color) {
        invalidate();
        drawPaint.setColor(Color.parseColor(color));
        selectedColor = color;

    }

    public HashSet<PointColor> getPoints() {
        return points;
    }
}
