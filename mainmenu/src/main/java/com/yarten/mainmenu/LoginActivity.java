package com.yarten.mainmenu;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.yarten.shapebutton.ButtonPanel;

public class LoginActivity extends BaseActivity
{
    private ButtonPanel buttonPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonPanel = findViewById(R.id.button_panel);

        ActivityHelper.initTriangle(buttonPanel.getLeftButton(), buttonPanel, this, MainActivity.class, Gravity.RIGHT, Gravity.LEFT, R.mipmap.right);

        new ViewPagerHelper(this)
                .initLayout(R.layout.page_login, R.id.bt1, new ViewPagerHelper.InitHandler() {
                    EditText ET_login_username;
                    EditText ET_login_password;
                    Button login_button;
                    CheckBox CB_login;

                    @Override
                    public void onInit(View view) {
                        ET_login_username = view.findViewById(R.id.login_username_edit);
                        ET_login_password = view.findViewById(R.id.login_password_edit);
                        login_button = view.findViewById(R.id.login_button);
                        CB_login = view.findViewById(R.id.login_checkbox);

                        CB_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if (b) {
                                    ET_login_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                } else {
                                    ET_login_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                }
                            }
                        });
                    }
                }, new ViewPagerHelper.OnTriggleListener() {
                    @Override
                    public boolean onTriggle() {
                        return true;
                    }
                })
                .initLayout(R.layout.page_register, R.id.bt2, new ViewPagerHelper.InitHandler() {
                    EditText ET_register_username;
                    EditText ET_register_password;
                    EditText ET_register_confirm;
                    Button register_button;
                    CheckBox CB_register_password;
                    CheckBox CB_register_confirm;

                    @Override
                    public void onInit(View view) {
                        ET_register_username = view.findViewById(R.id.register_username_edit);
                        ET_register_password = view.findViewById(R.id.register_password_edit);
                        ET_register_confirm = view.findViewById(R.id.register_confirm_edit);
                        register_button = view.findViewById(R.id.register_button);
                        CB_register_password = view.findViewById(R.id.checkBox_password);
                        CB_register_confirm = view.findViewById(R.id.login_checkbox);

                        CB_register_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                if (b) {
                                    ET_register_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                    ET_register_confirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                                } else {
                                    ET_register_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                    ET_register_confirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                }
                            }

                        });
                    }
                }, new ViewPagerHelper.OnTriggleListener() {
                    @Override
                    public boolean onTriggle() {
                        return true;
                    }
                })
                .build(R.id.view_pager);
    }
}
