package com.example.amir.koolak;

import android.app.Activity;
import android.content.ContentValues;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SQLiteDatabase db = openOrCreateDatabase("koolak5", MODE_PRIVATE, null);


        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='content'", null);
        if (c.getCount() == 0) {
            db.execSQL("CREATE TABLE content (id int(3), Pid int(3), type int(1), title varchar(50));");
            db.execSQL("CREATE TABLE diagram (id int(3), title text, color varchar(6), arms int(1), arm1 text, arm2 text, arm3 text, arm4 text, arm5 text, arm6 text," +
                    " et1_1 text, et1_2 text,et1_3 text, et1_4 text, et1_5 text, et2_1 text, et2_2 text,et2_3 text, et2_4, et2_5, et3_1 text, et3_2 text,et3_3 text, et3_4 text, et3_5 text," +
                    " et4_1 text, et4_2 text,et4_3 text, et4_4 text, et4_5 text, et5_1 text, et5_2 text,et5_3 text, et5_4, et5_5, et6_1 text, et6_2 text,et6_3 text, et6_4 text, et6_5 text," +
                    "sentence text);");
            db.execSQL("CREATE TABLE story (id int(3), color varchar(6), title text, content text, writer text);");
            db.execSQL("CREATE TABLE endday (id int(3), color varchar(6), content text);");
            db.execSQL("CREATE TABLE endweek (id int(3), color varchar(6), best_sentence text, time1 text, time2 text, time3 text);");

            Resources res = getResources();
            XmlResourceParser content = res.getXml(R.xml.content);
            setResources(db, content, "content");
            XmlResourceParser diagram = res.getXml(R.xml.diagram);
            setResources(db, diagram, "diagram");
            XmlResourceParser story = res.getXml(R.xml.story);
            setResources(db, story, "story");
            XmlResourceParser endDay = res.getXml(R.xml.end_day);
            setResources(db, endDay, "endday");
            XmlResourceParser endWeek = res.getXml(R.xml.end_week);
            setResources(db, endWeek, "endweek");
        }
        c.close();

    }

    private void setResources(SQLiteDatabase db, XmlPullParser content, String tableName) {
        ContentValues row = new ContentValues();
        String name, value;
        try {
            int eventType = content.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && content.getName().equals("row")) {
                    row.clear();
                    eventType = content.next();
                    while (!(eventType == XmlPullParser.END_TAG && content.getName().equals("row"))) {
                        if (eventType == XmlPullParser.START_TAG) {
                            name = content.getName();
                            eventType = content.next();
                            if (eventType == XmlPullParser.TEXT) {
                                value = content.getText();
                                row.put(name, value);
                            }
                        }
                        eventType = content.next();
                    }
                    db.insert(tableName, null, row);
                }
                eventType = content.next();
            }
        } catch (XmlPullParserException | IOException e) {
            Log.d("LEE", e.toString());
        }
    }

}


