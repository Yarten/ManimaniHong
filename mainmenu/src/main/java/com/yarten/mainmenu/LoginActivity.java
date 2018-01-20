package com.yarten.mainmenu;

import android.content.Context;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.example.airbubble.BubbleToast;
import com.xiaoshq.database.DataOperation;
import com.xiaoshq.database.User;
import com.yarten.shapebutton.ButtonPanel;

public class LoginActivity extends BaseActivity
{
    private ButtonPanel buttonPanel;
    public DataOperation dataOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //region 变量初始化
        dataOperation = DataOperation.instance;
        buttonPanel = findViewById(R.id.button_panel);
        ActivityHelper.initTriangle(buttonPanel.getLeftButton(), buttonPanel, this, MainActivity.class, Gravity.RIGHT, Gravity.LEFT, R.mipmap.right);
        dataOperation.setLoginID(DataOperation.ADMIN_ID);
        //endregion

        new ViewPagerHelper(this)
                .initLayout(R.layout.page_login, R.id.bt1, new ViewPagerHelper.InitHandler() {
                    //region 登录页面变量
                    EditText ET_login_username;
                    EditText ET_login_password;
                    Button login_button;
                    CheckBox CB_login;
                    //endregion

                    @Override
                    public void onInit(View view) {
                        //region 绑定控件
                        ET_login_username = view.findViewById(R.id.login_username_edit);
                        ET_login_password = view.findViewById(R.id.login_password_edit);
                        login_button = view.findViewById(R.id.login_button);
                        CB_login = view.findViewById(R.id.login_checkbox);
                        //endregion

                        //region 是否显示密码
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
                        //endregion

                        //region 用户登录
                        login_button.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                //获取窗口高度和宽度
                                WindowManager windowManager = (WindowManager)LoginActivity.this.getSystemService(Context.WINDOW_SERVICE);
                                DisplayMetrics outMetrics = new DisplayMetrics();
                                windowManager.getDefaultDisplay().getMetrics(outMetrics);
                                int width = outMetrics.widthPixels;
                                int height = outMetrics.heightPixels;

                                //获取用户名和密码
                                String usernameInput = ET_login_username.getText().toString();
                                String passwordInput = ET_login_password.getText().toString();
                                //根据输入的用户名获取数据库中的用户
                                User user = dataOperation.getUser(usernameInput);
                                int id = 0;
                                String passwordDB = "";

                                /*
                                 *首先如果用户已存在于数据库中，则获取和用户名对应的ID和密码
                                 *  如果ID为零，证明用户不存在，弹出用户不存在的提示
                                 *  否则判断输入密码是否为空，如果为空则提示密码为空
                                 *如果输入密码不为空则判断输入密码和保存在数据库中的密码是否一致，
                                 *  如果一致则跳回mainmenu，否则弹出用户名或密码错误的提示
                                 */
                                if(user != null)
                                {
                                    id = user.userId;
                                    passwordDB = dataOperation.getUser(id).password;
                                }
                                if(id==0) {
                                    BubbleToast bubbleToast = new BubbleToast(LoginActivity.this,width/4,height/6f,"该用户不存在", Gravity.BOTTOM, 1.0f);
                                    bubbleToast.show();
                                }
                                else if(passwordInput.equals(""))
                                {
                                    BubbleToast bubbleToast = new BubbleToast(LoginActivity.this,width/6,height/3f,"密码不能为空", Gravity.BOTTOM, 1.0f);
                                    bubbleToast.show();
                                }
                                else if(passwordInput.equals(passwordDB)){
                                    Toast.makeText(LoginActivity.this, "登陆成功！", Toast.LENGTH_SHORT).show();
                                    dataOperation.setLoginID(id);
                                    ActivityHelper.startActivity(LoginActivity.this, MainActivity.class, buttonPanel);
                                }
                                else {
                                    BubbleToast bubbleToast = new BubbleToast(LoginActivity.this,width/3,height/1.7f,"用户名或密码错误", Gravity.BOTTOM, 1.0f);
                                    bubbleToast.show();
                                }

                            }
                        });
                        //endregion
                    }


                }, new ViewPagerHelper.OnTriggerListener() {
                    @Override
                    public boolean onTrigger() {
                        return true;
                    }
                })
                .initLayout(R.layout.page_register, R.id.bt2, new ViewPagerHelper.InitHandler() {
                    //region 注册页面变量
                    EditText ET_register_username;
                    EditText ET_register_password;
                    EditText ET_register_confirm;
                    Button register_button;
                    CheckBox CB_register_password;
                    //endregion

                    @Override
                    public void onInit(View view) {
                        //region 绑定控件
                        ET_register_username = view.findViewById(R.id.register_username_edit);
                        ET_register_password = view.findViewById(R.id.register_password_edit);
                        ET_register_confirm = view.findViewById(R.id.register_confirm_edit);
                        register_button = view.findViewById(R.id.register_button);
                        CB_register_password = view.findViewById(R.id.checkBox_password);
                        //endregion

                        //region 是否显示密码
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
                        //endregion

                        //region 用户注册
                        register_button.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                //获取窗口高度和宽度
                                WindowManager windowManager = (WindowManager)LoginActivity.this.getSystemService(Context.WINDOW_SERVICE);
                                DisplayMetrics outMetrics = new DisplayMetrics();
                                windowManager.getDefaultDisplay().getMetrics(outMetrics);
                                int width = outMetrics.widthPixels;
                                int height = outMetrics.heightPixels;

                                //获取用户名、密码和确认密码
                                String username = ET_register_username.getText().toString();
                                String password = ET_register_password.getText().toString();
                                String confirmPass = ET_register_confirm.getText().toString();

                                /*
                                 * 判断用户名是否为空，如果为空，则弹出用户名不能为空的提示
                                 * 判断密码是否为空，如果为空，则弹出密码不能为空的提示
                                 * 判断密码和确认密码是否相等，如果不相等，则弹出确认密码错误的提示，否则跳回mainmenu
                                 */
                                if(username.equals("")){
                                    BubbleToast bubbleToast = new BubbleToast(LoginActivity.this,width/6,height/4.3f,"用户名不能为空", Gravity.BOTTOM, 1.0f);
                                    bubbleToast.show();
                                }
                                else if(password.equals("")){
                                    BubbleToast bubbleToast = new BubbleToast(LoginActivity.this,width/6,height/3.2f,"密码不能为空", Gravity.BOTTOM, 1.0f);
                                    bubbleToast.show();
                                }
                                else if(!password.equals(confirmPass)){
                                    BubbleToast bubbleToast = new BubbleToast(LoginActivity.this,width/6,height/2.5f,"确认密码错误", Gravity.BOTTOM, 1.0f);
                                    bubbleToast.show();
                                }
                                else{
                                    int id = dataOperation.addUser(username, password);
                                    BubbleToast bubbleToast = new BubbleToast(LoginActivity.this,width/6,height/1.4f,"注册成功", Gravity.TOP, 1.0f);
                                    bubbleToast.show();
                                    dataOperation.setLoginID(id);
                                    ActivityHelper.startActivity(LoginActivity.this, MainActivity.class, buttonPanel);
                                }

                            }
                        });
                        //endregion
                    }

                }, new ViewPagerHelper.OnTriggerListener() {
                    @Override
                    public boolean onTrigger() {
                        return true;
                    }
                })
                .build(R.id.view_pager);

    }
}
