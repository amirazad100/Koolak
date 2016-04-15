package com.example.amir.koolak;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.amir.koolak.Item.SubCategory;
import com.example.amir.koolak.Item.SubCategory.ItemList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends Activity {

    private ArrayList<Item> mainList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SQLiteDatabase db = openOrCreateDatabase("koolak7", MODE_PRIVATE, null);


        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='content'", null);
        if (cursor.getCount() == 0) {
            dbInitializer(db);
        }
        cursor.close();

        LinearLayout mLinearListView = (LinearLayout) findViewById(R.id.linear_ListView);

        //Make array list one is for mainlist and other is for sublist
        mainList = new ArrayList<>();
        String[] columnsName = {"id", "title"};
        cursor = db.query("content", columnsName, "Pid=0", null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ArrayList<SubCategory> subCategories = new ArrayList<>();
            Cursor cursor1 = db.query("content", columnsName, "Pid=" + cursor.getInt(cursor.getColumnIndex("id")), null, null, null, null);
            cursor1.moveToFirst();
            while (!cursor1.isAfterLast()) {
                Cursor cursor2 = db.query("content", columnsName, "Pid=" + cursor1.getInt(cursor1.getColumnIndex("id")), null, null, null, null);
                cursor2.moveToFirst();
                ArrayList<ItemList> itemLists = new ArrayList<>();
                while (!cursor2.isAfterLast()) {
                    ItemList itemList = new ItemList(cursor2.getString(cursor2.getColumnIndex("title")), cursor2.getInt(cursor2.getColumnIndex("id")));
                    itemLists.add(itemList);
                    cursor2.moveToNext();
                }
                SubCategory subCategory = new SubCategory(cursor1.getString(cursor1.getColumnIndex("title")),
                        cursor1.getInt(cursor1.getColumnIndex("id")), itemLists);
                subCategories.add(subCategory);
                cursor2.close();
                cursor1.moveToNext();
            }
            Item item = new Item(cursor.getString(cursor.getColumnIndex("title")), cursor.getInt(cursor.getColumnIndex("id")), subCategories);
            mainList.add(item);
            cursor1.close();
            cursor.moveToNext();
        }
        cursor.close();

        //Adds data into first row
        for (int i = 0; i < mainList.size(); i++) {

            LayoutInflater inflater;
            inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View mLinearView = inflater.inflate(R.layout.row_first, null);

            final TextView mProductName = (TextView) mLinearView.findViewById(R.id.textViewName);
            final RelativeLayout mLinearFirstArrow = (RelativeLayout) mLinearView.findViewById(R.id.linearFirst);
            final ImageView mImageArrowFirst = (ImageView) mLinearView.findViewById(R.id.imageFirstArrow);
            final LinearLayout mLinearScrollSecond = (LinearLayout) mLinearView.findViewById(R.id.linear_scroll);

            //checkes if menu is already opened or not
            if (!mainList.get(i).clicked) {
                mLinearScrollSecond.setVisibility(View.GONE);
                if (mainList.get(i).hasChild())
                    mImageArrowFirst.setBackgroundResource(R.drawable.up);
            } else {
                mLinearScrollSecond.setVisibility(View.VISIBLE);
                mImageArrowFirst.setBackgroundResource(R.drawable.down);
            }
            //Handles onclick effect on list item
            final int finalI = i;
            mLinearFirstArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mainList.get(finalI).clicked) {
                        if (mainList.get(finalI).hasChild()) {
                            mainList.get(finalI).clicked = true;
                            mImageArrowFirst.setBackgroundResource(R.drawable.down);
                            mLinearScrollSecond.setVisibility(View.VISIBLE);
                        } else {
                            Log.d("LEE", "clicked");
                        }

                    } else {
                        mainList.get(finalI).clicked = false;
                        mImageArrowFirst.setBackgroundResource(R.drawable.up);
                        mLinearScrollSecond.setVisibility(View.GONE);
                    }
                }
            });



            final String name = mainList.get(i).getpName();
            mProductName.setText(name);

            //Adds data into second row
            for (int j = 0; j < mainList.get(i).getmSubCategoryList().size(); j++) {
                LayoutInflater inflater2;
                inflater2 = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mLinearView2 = inflater2.inflate(R.layout.row_second, null);

                TextView mSubItemName = (TextView) mLinearView2.findViewById(R.id.textViewTitle);
                final RelativeLayout mLinearSecondArrow = (RelativeLayout) mLinearView2.findViewById(R.id.linearSecond);
                final ImageView mImageArrowSecond = (ImageView) mLinearView2.findViewById(R.id.imageSecondArrow);
                final LinearLayout mLinearScrollThird = (LinearLayout) mLinearView2.findViewById(R.id.linear_scroll_third);

                //checkes if menu is already opened or not
                if (!mainList.get(i).getmSubCategoryList().get(j).clicked) {
                    mLinearScrollThird.setVisibility(View.GONE);
                    if (mainList.get(i).getmSubCategoryList().get(j).hasChild())
                        mImageArrowSecond.setBackgroundResource(R.drawable.up);
                } else {
                    mLinearScrollThird.setVisibility(View.VISIBLE);
                    mImageArrowSecond.setBackgroundResource(R.drawable.down);
                }
                //Handles onclick effect on list item
                final int finalJ = j;
                final int finalI1 = i;
                mLinearSecondArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!mainList.get(finalI1).getmSubCategoryList().get(finalJ).clicked) {
                            if (mainList.get(finalI1).getmSubCategoryList().get(finalJ).hasChild()) {
                                mainList.get(finalI1).getmSubCategoryList().get(finalJ).clicked = true;
                                mImageArrowSecond.setBackgroundResource(R.drawable.down);
                                mLinearScrollThird.setVisibility(View.VISIBLE);
                            } else {
                                Log.d("LEE", "clicked");
                            }

                        } else {
                            mainList.get(finalI1).getmSubCategoryList().get(finalJ).clicked = false;
                            mImageArrowSecond.setBackgroundResource(R.drawable.up);
                            mLinearScrollThird.setVisibility(View.GONE);
                        }
                    }
                });

                final String catName = mainList.get(i).getmSubCategoryList().get(j).getpSubCatName();
                mSubItemName.setText(catName);
                //Adds items in subcategories
                for (int k = 0; k < mainList.get(i).getmSubCategoryList().get(j).getmItemListArray().size(); k++) {
                    LayoutInflater inflater3;
                    inflater3 = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View mLinearView3 = inflater3.inflate(R.layout.row_third, null);

                    TextView mItemName = (TextView) mLinearView3.findViewById(R.id.textViewItemName);
                    final String itemName = mainList.get(i).getmSubCategoryList().get(j).getmItemListArray().get(k).getItemName();

                    mItemName.setText(itemName);

                    mLinearScrollThird.addView(mLinearView3);

                    mItemName.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d("LEE", "clicked");
                        }
                    });
                }

                mLinearScrollSecond.addView(mLinearView2);

            }

            mLinearListView.addView(mLinearView);
        }
    }

    private void dbInitializer(SQLiteDatabase db) {
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


