package com.mydeerlet.im.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mydeerlet.im.R;
import com.mydeerlet.im.api.RetrofitManager;
import com.mydeerlet.im.api.RxException;
import com.mydeerlet.im.bean.UpdateModel;
import com.mydeerlet.im.service.LoginService;
import com.mydeerlet.im.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.user_name)
    EditText userName;
    @BindView(R.id.bt_login)
    Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_login)
    public void onViewClicked() {

        String usre = userName.getText().toString().trim();
        String pwd = password.getText().toString().trim();

        if (TextUtils.isEmpty(usre)|| TextUtils.isEmpty(pwd)){
            Toast.makeText(this, "用户名或密码为空", Toast.LENGTH_SHORT).show();
            return;
        }

        RetrofitManager.getInstance(this)
                .create(LoginService.class)
                .getMessage("北京")
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UpdateModel>() {
                    @Override
                    public void accept(UpdateModel updateModel) throws Exception {
                        LogUtils.i("aaa",updateModel.getData().getGanmao());
                    }
                },new RxException<Throwable>());


    }
}
