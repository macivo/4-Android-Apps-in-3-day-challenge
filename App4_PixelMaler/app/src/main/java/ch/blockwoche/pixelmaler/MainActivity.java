package ch.blockwoche.pixelmaler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends Activity {

    private DrawingView drawingView;
    private ImageButton currentBrush;

    public void eraseClicked(View view) {
        if (view != currentBrush) {
            ImageButton imgView = (ImageButton) view;
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.selected));
            currentBrush.setImageDrawable(null);
            currentBrush = (ImageButton) view;
        }

        drawingView.setErase(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawingView = (DrawingView) findViewById(R.id.drawing);

        currentBrush = (ImageButton) findViewById(R.id.defaultColor);
        currentBrush.setImageDrawable(getResources().getDrawable(R.drawable.selected));
        String color = currentBrush.getTag().toString();
        drawingView.setColor(color);
    }

    private void onCreateNewDrawingAction() {
        AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
        newDialog.setTitle("New Drawing");
        newDialog.setMessage("Start a new drawing?");
        newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                drawingView.startNew();
                dialog.dismiss();
            }
        });
        newDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        newDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.add("New");
        menuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                onCreateNewDrawingAction();
                return true;
            }
        });

        menuItem = menu.add("Log");
        menuItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                onLogAction();
                return false;
            }
        });

        return true;
    }

    public void paintClicked(View view) {
        if (view != currentBrush) {
            ImageButton imgView = (ImageButton) view;
            String color = view.getTag().toString();
            drawingView.setColor(color);
            imgView.setImageDrawable(getResources().getDrawable(R.drawable.selected));
            currentBrush.setImageDrawable(null);
            currentBrush = (ImageButton) view;
        }
        drawingView.setErase(false);
    }

    private void onLogAction() {
        // TODO
        List<PointColor> points = drawingView.getPoints().stream().sorted(new Comparator<PointColor>() {

            @Override
            public int compare(PointColor pointColor, PointColor t1) {
                int result = Integer.compare(pointColor.getPoint().y, t1.getPoint().y);
                if ( result == 0 ) {
                    result = Integer.compare(pointColor.getPoint().x, t1.getPoint().x);
                }
                return result;
            }
        }).collect(Collectors.toList());
        System.out.println(points.toString());

        Intent intent = new Intent("ch.blockwoche.intent.LOG");
        // format depends on app, see logbook format guideline
        ArrayList<JSONObject> pointsAsArray = new ArrayList<>();
        JSONObject log = new JSONObject();
        for (PointColor pointColor : points) {
            JSONObject point = new JSONObject();
            try {
                point.put("y",pointColor.getPoint().y);
                point.put("x",pointColor.getPoint().x);
                point.put("color",pointColor.getColor());
            }catch (JSONException j){

            }
            pointsAsArray.add(point);
        }
        try {
            log.put("task", "Pixelpainter");
            log.put("pixels", new JSONArray(pointsAsArray));
        } catch (JSONException j){ }
        intent.putExtra("ch.blockwoche.logmessage", log.toString());
        try { startActivity(intent);
        } catch (ActivityNotFoundException e){ }
    }

}
