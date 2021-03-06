package com.example.sendinguser;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.provider.CalendarContract.Events;

import java.util.Calendar;
import java.util.List;

import static android.provider.CalendarContract.EXTRA_EVENT_BEGIN_TIME;
import static android.provider.CalendarContract.EXTRA_EVENT_END_TIME;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void callPhone(View view) {
        Uri callPhone = Uri.parse("tel:1821101335");
        Intent intent = new Intent(Intent.ACTION_DIAL, callPhone);
        Uri number = Uri.parse("tel:5551234");
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(intent);
    }


    public void lookMap(View view) {
        // Map point based on address
        Uri location = Uri.parse("geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California");
        // Or map point based on latitude/longitude
        // Uri location = Uri.parse("geo:37.422219,-122.08364?z=14"); // z param is zoom level
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        startActivity(mapIntent);
    }

    public void web(View view) {
        Uri webpage = Uri.parse("http://www.baidu.com");
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
        startActivity(webIntent);
    }

    public void sendEmail(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        // The intent does not have a URI, so declare the "text/plain" MIME type
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"jon@example.com"}); // recipients
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Email subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message text");
        //emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
        // You can also attach multiple items by passing an ArrayList of Uris
        startActivity(emailIntent);
    }

    public void calandar(View view) {
        Intent calendarIntent = new Intent(Intent.ACTION_INSERT, Events.CONTENT_URI);
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(2012, 0, 19, 7, 30);
        Calendar endTime = Calendar.getInstance();
        endTime.set(2012, 0, 19, 10, 30);
        calendarIntent.putExtra(EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis());
        calendarIntent.putExtra(EXTRA_EVENT_END_TIME, endTime.getTimeInMillis());
        calendarIntent.putExtra(Events.TITLE, "Ninja class");
        calendarIntent.putExtra(Events.EVENT_LOCATION, "Secret dojo");

        //验证是否存在接收Intent的应用
        List<ResolveInfo> resolveInfos = getPackageManager().queryIntentActivities(calendarIntent, 0);
        if (resolveInfos.size() > 0) {
            startActivity(calendarIntent);
        }
    }

    public void share(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);

        // Always use string resources for UI text.
        // This says something like "Share this photo with"
        String title = getResources().getString(R.string.chooser_title);
        // Create intent to show chooser
        Intent chooser = Intent.createChooser(intent, title);

        // Verify the intent will resolve to at least one activity
        ComponentName componentName = intent.resolveActivity(getPackageManager());
        Log.i(TAG, "share: " + componentName);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

    private static final String TAG = "MainActivity";

    //选择联系人返回结果
    public void pickContact(View view) {
        Intent pickContact = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContact.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContact, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                if (uri != null) {
                    Cursor cursor = getContentResolver().query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null);
                    cursor.moveToFirst();
                    int number = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String phone = cursor.getString(number);
                    cursor.close();
                    Log.i(TAG, "onActivityResult: " + phone);
                }
            }
        }
    }
}
