package com.mydeerlet.im.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mydeerlet.im.MainActivity;
import com.mydeerlet.im.R;
import com.mydeerlet.im.api.RetrofitManager;
import com.mydeerlet.im.api.RxException;
import com.mydeerlet.im.bean.HttpResult;
import com.mydeerlet.im.bean.User;
import com.mydeerlet.im.service.LoginService;
import com.mydeerlet.im.utils.SPUtils;
import com.tapadoo.alerter.Alerter;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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

        if (SPUtils.isLogin(this)){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
            return;
        }
    }

    @OnClick(R.id.bt_login)
    public void onViewClicked() {

        final String usre = userName.getText().toString().trim();
        String pwd = password.getText().toString().trim();

        if (TextUtils.isEmpty(usre)|| TextUtils.isEmpty(pwd)){
            Toast.makeText(this, "用户名或密码为空", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String,String> map = new HashMap<>();

        map.put("imCode",usre);
        map.put("password",pwd);




        RetrofitManager.getInstance(this)
                .create(LoginService.class)
                .login(map)
                .compose(this.<HttpResult<User>>scheduleSingle())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        Alerter.create(LoginActivity.this)
                                .setText("登录中...")
                                .enableProgress(true)
                                .setDismissable(false)
                                .disableOutsideTouch()
                                .show();
                    }
                })
                .subscribe(new Consumer<HttpResult<User>>() {
                    @Override
                    public void accept(HttpResult<User> userHttpResult) throws Exception {
                        if (userHttpResult.SUCCESS()){
                            LoginActivity.this.finish();
                            SPUtils.setCurrentUser(getApplicationContext(), (User) userHttpResult.getData());
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));

                            Toast.makeText(LoginActivity.this, userHttpResult.getMessage(), Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(LoginActivity.this, userHttpResult.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                },new RxException<Throwable>());



    }

    public <T>SingleTransformer<T,T> scheduleSingle(){
        return new SingleTransformer<T, T>() {
            @Override
            public SingleSource<T> apply(Single<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }
}
