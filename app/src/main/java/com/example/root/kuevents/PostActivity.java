package com.example.root.kuevents;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class PostActivity extends AppCompatActivity {

    private ImageButton mSelectImage;
    private EditText mTitle;
    private EditText mDesc;
    private TextView mStartDate;
    private TextView mEndDate;
    private TextView mStartTime;
    private TextView mEndTime;
    private Button mPost;
    private Uri mimageUri = null;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private DatePickerDialog.OnDateSetListener kDateSetListener;
    private static final int GALLERY_REQUEST=1;
    static final int DIALOG_ID1 = 0;
    static final int DIALOG_ID2 = 1;
    int hour_x;
    int minute_x;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgress;

    private static final String TAG = "PostActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        mSelectImage=(ImageButton) findViewById(R.id.imageSelect);
        mTitle=(EditText) findViewById(R.id.TitleField);
        mDesc=(EditText) findViewById(R.id.DescField);
        mStartDate = (TextView) findViewById(R.id.startDateField);
        mEndDate = (TextView) findViewById(R.id.endDateField);
        mEndTime = (TextView) findViewById(R.id.endDateField);
        mPost = (Button) findViewById(R.id.post_btn);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Events");
        mProgress= new ProgressDialog(this);

        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent( Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog= new DatePickerDialog(
                        PostActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month +1;
                Log.d(TAG, "onDateSet: dd/mm/yy " + day + "/" + month + "/" + year );
                String date = day + "/" + month + "/" + year;
                mStartDate.setText(date);

            }
        };

        mEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog= new DatePickerDialog(
                        PostActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        kDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        kDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month +1;
                Log.d(TAG, "onDateSet: dd/mm/yy " + day + "/" + month + "/" + year );
                String date = day + "/" + month + "/" + year;
                mEndDate.setText(date);

            }
        };

        showTimePickerDialog();

        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPosting();
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id){
        switch (id){
            case DIALOG_ID1:
                return  new TimePickerDialog(PostActivity.this, mTimeSetListener, hour_x,minute_x, false);
            case DIALOG_ID2:
                return  new TimePickerDialog(PostActivity.this, kTimeSetListener, hour_x, minute_x, false);
            default:
                return null;
        }
    }

    private void showTimePickerDialog(){
        mStartTime = (TextView) findViewById(R.id.startTimeField);
        mEndTime = (TextView) findViewById(R.id.endTimeField);
        mStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_ID1);
            }
        });
        mEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_ID2);
            }
        });
    }

    protected TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            hour_x = i;
            minute_x= i1;
            String startTime = hour_x+ ":" + minute_x;
            mStartTime.setText(startTime);

        }
    };

    protected TimePickerDialog.OnTimeSetListener kTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int i, int i1) {
            hour_x = i;
            minute_x = i1;
            String endTime = hour_x+ ":" + minute_x;
            mEndTime.setText(endTime);
        }
    };


    private void startPosting(){
        mProgress.setMessage("Posting...");


        final String title_value = mTitle.getText().toString().trim();
        final String desc_value = mDesc.getText().toString().trim();
        final String startdate_value = mStartDate.getText().toString().trim();
        final String enddate_value = mEndDate.getText().toString().trim();
        final String starttime_value = mStartTime.getText().toString().trim();
        final String endtime_value= mEndTime.getText().toString().trim();

        if (!TextUtils.isEmpty(title_value) && !TextUtils.isEmpty(desc_value) && mimageUri!=null){
            mProgress.show();
            StorageReference filepath = mStorage.child("Event Images").child(mimageUri.getLastPathSegment());
            filepath.putFile(mimageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl=taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost = mDatabase.push();

                    newPost.child("title").setValue(title_value);
                    newPost.child("desc").setValue(desc_value);
                    newPost.child("image").setValue(downloadUrl.toString());
                    newPost.child("start_date").setValue(startdate_value);
                    newPost.child("end_date").setValue(enddate_value);
                    newPost.child("start_time").setValue(starttime_value);
                    newPost.child("end_time").setValue(endtime_value);



                    mProgress.dismiss();


                  /*  events fragment = new events();
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.replace( R.id.events, fragment);
                    transaction.commit();*/
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GALLERY_REQUEST && resultCode==RESULT_OK){
            mimageUri = data.getData();
            mSelectImage.setImageURI(mimageUri);
        }

    }
}
