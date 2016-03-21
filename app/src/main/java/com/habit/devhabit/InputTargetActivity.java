package com.habit.devhabit;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by qmo-i7 on 2016/3/19.
 */
public class InputTargetActivity extends AppCompatActivity {
    AlertDialog.Builder mConfirmModificationDialog;
    boolean mContentModified = false;
    Context mContext;

    EditText mInputTargetName;
    EditText mInputTargetDesc;
    EditText mInputReasons1;
    EditText mInputReasons2;
    EditText mInputReasons3;
    TextView mInputTargetStartDate;
    EditText mInputTargetSchedule;

    boolean mDialogIsShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_form_target_data);

        mConfirmModificationDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.target_dialog_content_modified_title)
                .setMessage(R.string.target_dialog_content_modified_content)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Item item = new Item(mInputTargetName.getText().toString(),
                                mInputTargetDesc.getText().toString(),
                                mInputReasons1.getText().toString(),
                                mInputReasons2.getText().toString(),
                                mInputReasons3.getText().toString(),
                                mInputTargetStartDate.getText().toString(),
                                mInputTargetSchedule.getText().toString()
                        );
                        Intent intent = new Intent();
                        Bundle b = new Bundle();
                        b.putSerializable("item", item);
                        intent.putExtras(b);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNeutralButton(R.string.leave_without_saving, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                });

        mInputTargetName = (EditText) findViewById(R.id.input_target_name);
        mInputTargetName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mContentModified = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mInputTargetDesc = (EditText) findViewById(R.id.input_target_desc);
        mInputTargetDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mContentModified = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mInputReasons1 = (EditText) findViewById(R.id.input_reasons_1);
        mInputReasons1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mContentModified = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mInputReasons2 = (EditText) findViewById(R.id.input_reasons_2);
        mInputReasons2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mContentModified = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mInputReasons3 = (EditText) findViewById(R.id.input_reasons_3);
        mInputReasons3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mContentModified = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mInputTargetStartDate = (TextView) findViewById(R.id.input_target_start_date);
        mInputTargetStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDialogIsShowing == false) {
                    mDialogIsShowing = true;
                    Calendar dateTime = Calendar.getInstance();
                    DatePickerDialog dlg;
                    dlg = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            mInputTargetStartDate.setText("" + year + "/" + monthOfYear + "/" + dayOfMonth);
                            mDialogIsShowing = false;
                        }
                    },
                            dateTime.get(Calendar.YEAR),
                            dateTime.get(Calendar.MONTH),
                            dateTime.get(Calendar.DAY_OF_MONTH));
                    dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            mDialogIsShowing = false;
                        }
                    });
                    dlg.show();
                }
            }

        });

        mInputTargetSchedule = (EditText) findViewById(R.id.input_target_schedule_form);
        mInputTargetSchedule.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mContentModified = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (checkModified() == true) {
            mConfirmModificationDialog.show();
            return;
        }
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean checkModified() {
        return mContentModified;
    }
}
