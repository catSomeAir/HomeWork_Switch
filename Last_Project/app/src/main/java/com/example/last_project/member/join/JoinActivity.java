package com.example.last_project.member.join;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.last_project.CommonAlertActivity;
import com.example.last_project.MainActivity;
import com.example.last_project.R;
import com.example.last_project.conn.CommonConn;
import com.example.last_project.member.MemberVO;
import com.example.last_project.request.RequestEmptyActivity;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JoinActivity extends AppCompatActivity {

    EditText join_edt_email, join_edt_pw, join_edt_pw_check, join_edt_name, join_edt_nickname, join_edt_phone;
    Button join_btn_signup, join_btn_dialog1, join_btn_dialog2;
    ImageView join_imgv_back;
    TextView join_tv_pw_confirm, join_tv_pw_check_confirm, join_tv_email_confirm;
    Dialog join_dialog;
    String search_text = "";
    String search_div_text = "";
    boolean bool, bool_pw, bool_pw_chk, bool_email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        join_btn_signup = findViewById(R.id.join_btn_signup);
        join_edt_email = findViewById(R.id.join_edt_email);
        join_tv_email_confirm = findViewById(R.id.join_tv_email_confirm);
        join_edt_pw = findViewById(R.id.join_edt_pw);
        join_edt_pw_check = findViewById(R.id.join_edt_pw_check);
        join_edt_name = findViewById(R.id.join_edt_name);
        join_edt_nickname = findViewById(R.id.join_edt_nickname);
        join_edt_phone = findViewById(R.id.join_edt_phone);
        join_tv_pw_confirm = findViewById(R.id.join_tv_pw_confirm);
        join_tv_pw_check_confirm = findViewById(R.id.join_tv_pw_check_confirm);
        join_imgv_back = findViewById(R.id.join_imgv_back);

        join_imgv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        join_edt_email.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String email = join_edt_email.getText().toString();
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    join_tv_email_confirm.setText("????????? ????????? ????????? ????????????");
                    join_tv_email_confirm.setTextColor(Color.parseColor("red"));
                    bool = false;
//                    Toast.makeText(SignupActivity.this,"????????? ????????? ????????????",Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
                    return;
                } else {
                    join_tv_email_confirm.setText("??????????????? ????????? ???????????????");
                    join_tv_email_confirm.setTextColor(Color.parseColor("#FF102BB6"));
                    bool = true;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                if (bool == true) {
                    CommonConn conn = new CommonConn(JoinActivity.this, "email_check.me");
                    search_text = join_edt_email.getText().toString();
                    conn.addParams("email", join_edt_email.getText().toString());
                    conn.executeConn_no_dialog(new CommonConn.ConnCallback() {
                        @Override
                        public void onResult(boolean isResult, String data) {
                            if (isResult) {

                                if (data.equals("0")) {
                                    join_tv_email_confirm.setText("??????????????? ???????????????");
                                    join_tv_email_confirm.setTextColor(Color.parseColor("#FF102BB6"));
                                    bool_email = true;

                                } else {
                                    join_tv_email_confirm.setText("????????? ?????????????????? ?????? ??????????????????");
                                    join_tv_email_confirm.setTextColor(Color.parseColor("red"));
                                    bool_email = false;

                                }

                            }

                        }

                    });
                }
            }
        });


        join_edt_pw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String pw = join_edt_pw.getText().toString();
                if (!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", pw)) {
                    join_tv_pw_confirm.setText("???????????? ????????? ???????????????");
                    join_tv_pw_confirm.setTextColor(Color.parseColor("red"));
//                    Toast.makeText(JoinActivity.this,"???????????? ????????? ???????????????.",Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
                    bool_pw = false;
                    return;
                } else {
                    join_tv_pw_confirm.setText("??????????????? ?????????????????????");
                    join_tv_pw_confirm.setTextColor(Color.parseColor("#FF102BB6"));
                    bool_pw = true;
                }

                if (join_edt_pw.getText().toString().equals(join_edt_pw_check.getText().toString())) {
                    join_tv_pw_check_confirm.setText("??????????????? ???????????????");
                    join_tv_pw_check_confirm.setTextColor(Color.parseColor("#FF102BB6"));
                    join_btn_signup.setEnabled(true); // ?????? ???????????? ??????
//                    getSupportFragmentManager().beginTransaction().replace(R.id.member_join_container, new Join_UserInfo_Fragment()).commit();
                    bool_pw_chk= true;
                } else {
                    join_tv_pw_check_confirm.setText("??????????????? ????????????????????????");
                    join_tv_pw_check_confirm.setTextColor(Color.parseColor("red"));
                    bool_pw_chk= false;
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        join_edt_pw_check.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                  ???????????? ??? ??????
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //???????????? ????????? ?????? ??????
                //???????????? ?????????
                String pw = join_edt_pw_check.getText().toString();
                if (!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-zA-Z]).{8,20}$", pw)) {
                    join_tv_pw_check_confirm.setText("???????????? ????????? ???????????????");
                    join_tv_pw_check_confirm.setTextColor(Color.parseColor("red"));
                    join_btn_signup.setEnabled(false);
//                    Toast.makeText(JoinActivity.this,"???????????? ????????? ???????????????.",Toast.LENGTH_SHORT).show();
//                    dialog.dismiss();
                    return;
                }
//                if(){
//
//                }


                if (join_edt_pw.getText().toString().equals(join_edt_pw_check.getText().toString())) {
                    join_tv_pw_check_confirm.setText("??????????????? ???????????????");
                    join_tv_pw_check_confirm.setTextColor(Color.parseColor("#FF102BB6"));
                    join_btn_signup.setEnabled(true); // ?????? ???????????? ??????
//                    getSupportFragmentManager().beginTransaction().replace(R.id.member_join_container, new Join_UserInfo_Fragment()).commit();
                    bool_pw_chk= true;
                } else {
                    join_tv_pw_check_confirm.setText("??????????????? ????????????????????????");
                    join_tv_pw_check_confirm.setTextColor(Color.parseColor("red"));
                    bool_pw_chk= false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //????????? ?????? ??? ??????
                //???????????? ????????? ????????? ?????? ??? ?????? ??? -> ?????? ?????? ??? ???????????? ????????? ???????????? ?????? => ?????? ????????? ?????????
                if (join_edt_pw.getText().toString().equals(join_edt_pw_check.getText().toString())) {

                    join_tv_pw_check_confirm.setText("??????????????? ???????????????.");
                    join_tv_pw_check_confirm.setTextColor(Color.parseColor("#FF102BB6"));
                    join_btn_signup.setEnabled(true); // ?????? ???????????? ??????
                    bool_pw_chk= true;
//                    getSupportFragmentManager().beginTransaction().replace(R.id.member_join_container, new Join_UserInfo_Fragment()).commit();
                } else {
                    join_tv_pw_check_confirm.setText("??????????????? ????????????????????????.");
                    join_tv_pw_check_confirm.setTextColor(Color.parseColor("red"));
                    bool_pw_chk= false;
                }

            }
        });
        join_btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emptyEdittext()){
                    MemberVO vo = new MemberVO();
                    vo.setEmail(join_edt_email.getText() + "");
                    vo.setPw(join_edt_pw.getText() + "");
                    vo.setName(join_edt_name.getText() + "");
                    vo.setNickname(join_edt_nickname.getText() + "");
                    vo.setPhone(join_edt_phone.getText() + "");


                    CommonConn conn = new CommonConn(JoinActivity.this, "join.me");
                    conn.addParams("vo", new Gson().toJson(vo));
                    conn.executeConn(new CommonConn.ConnCallback() {
                        @Override
                        public void onResult(boolean isResult, String data) {
                            if (data != null) {
                                if (data.equals("1")) {
//                                Intent intent = new Intent(JoinActivity.this, MainActivity.class);
//                                startActivity(intent);
                                    Intent intent = new Intent(JoinActivity.this, CommonAlertActivity.class);
                                    intent.putExtra("page", "JoinActivity");
                                    startActivityForResult(intent, 0);

                                }
                            }
                        }
                    });
                }



            }
        });


        join_dialog = new

                Dialog(JoinActivity.this);
        join_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        join_dialog.setContentView(R.layout.join_dialog);
        join_imgv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showJoinDialog();
            }
        });
    }

    public boolean emptyEdittext() {
        Intent intent = new Intent(JoinActivity.this, RequestEmptyActivity.class);
        if (join_edt_email.getText().toString().length() == 0) {
            intent.putExtra("text", "???????????? ???????????? ???????????????");
            startActivity(intent);
            return false;
        } else if (join_edt_pw.getText().toString().length() == 0) {
            intent.putExtra("text", "??????????????? ???????????? ???????????????");
            startActivity(intent);
            return false;
        } else if (join_edt_pw_check.getText().toString().length() == 0) {
            intent.putExtra("text", "??????????????? ???????????? ???????????????");
            startActivity(intent);
            return false;
        } else if (join_edt_name.getText().toString().length() == 0) {
            intent.putExtra("text", "????????? ???????????? ???????????????");
            startActivity(intent);
            return false;
        } else if (join_edt_nickname.getText().toString().length() == 0) {
            intent.putExtra("text", "???????????? ???????????? ???????????????");
            startActivity(intent);
            return false;
        } else if (join_edt_phone.getText().toString().length() == 0) {
            intent.putExtra("text", "???????????? ???????????? ???????????????");
            startActivity(intent);
            return false;
        } else if( !bool){
            intent.putExtra("text", "???????????? ?????? ??????????????????");
            startActivity(intent);
            return false;
        } else if( !bool_email){
            intent.putExtra("text", "????????? ??????????????????");
            startActivity(intent);
            return false;
        } else if( !bool_pw){
            intent.putExtra("text", "???????????? ?????? ???????????????????????????");
            startActivity(intent);
            return false;
        } else if( !bool_pw_chk){
            intent.putExtra("text", "??????????????? ???????????? ????????????");
            startActivity(intent);
            return false;
        } else if( !bool_email){
            intent.putExtra("text", "????????? ??????????????????");
            startActivity(intent);
            return false;
        }

        return true;
    }

    public void showJoinDialog() {
        join_dialog.show();
        join_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        join_dialog.findViewById(R.id.join_btn_dialog1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                join_dialog.dismiss();//?????? ?????? ?????? -> ?????? ?????? ??????

            }
        });

        join_dialog.findViewById(R.id.join_btn_dialog2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                join_dialog.dismiss();
                finish();//?????? ?????? ?????? -> ?????? ?????? ??????
            }
        });

    }

    void check_validation(String email, String password) {
        // ???????????? ????????? ?????????1 : ??????, ??????????????? ??????????????? ??????.
        String val_symbol = "([0-9].*[!,@,#,^,&,*,(,)])|([!,@,#,^,&,*,(,)].*[0-9])";
        // ???????????? ????????? ?????????2 : ????????? ??????????????? ????????? ???????????? ??????????????? ??????.
        String val_alpha = "([a-z].*[A-Z])|([A-Z].*[a-z])";
        // ??????????????? ?????????
        Pattern pattern_symbol = Pattern.compile(val_symbol);
        Pattern pattern_alpha = Pattern.compile(val_alpha);

        Matcher matcher_symbol = pattern_symbol.matcher(password);
        Matcher matcher_alpha = pattern_alpha.matcher(password);

        if (matcher_symbol.find() && matcher_alpha.find()) {
            // email??? password??? ???????????? ??????
//            email_signIn(email, password);

        } else {
            Toast.makeText(this, "??????????????? ??????????????????", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            setResult(1);
            finish();
        }
    }
}