package com.mydeerlet.im.fragmentHome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mydeerlet.im.R;
import com.mydeerlet.im.activity.ChatActivity;
import com.mydeerlet.im.api.RetrofitManager;
import com.mydeerlet.im.api.RxException;
import com.mydeerlet.im.base.BaseAdapter;
import com.mydeerlet.im.base.OnItemClickListener;
import com.mydeerlet.im.bean.HttpResult;
import com.mydeerlet.im.service.LoginService;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.SingleTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class Home1 extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.rv_home1)
    RecyclerView rvHome1;
    private MyAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home1, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new MyAdapter(getContext(), new OnItemClickListener<String>() {
            @Override
            public void onClick(View view, int position, String data) {
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("imCode",data);
                startActivity(intent);

            }
        });

        rvHome1.setAdapter(adapter);
        //添加Android自带的分割线
        rvHome1.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL));
        getData();
    }




    public void getData() {

        RetrofitManager.getInstance(getContext())
                .create(LoginService.class)
                .getList()
                .compose(this.<HttpResult<List<String>>>scheduleSingle())
                .subscribe(new Consumer<HttpResult<List<String>>>() {
                    @Override
                    public void accept(HttpResult<List<String>> setHttpResult) throws Exception {
                        Toast.makeText(getContext(), setHttpResult.getMessage(), Toast.LENGTH_SHORT).show();
                        if (setHttpResult.SUCCESS()){
                            adapter.setData(setHttpResult.getData());


                        }

                    }
                },new RxException<Throwable>());
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    class MyAdapter extends BaseAdapter<String,MyAdapter.ViewHolder >{

        public MyAdapter(Context context, OnItemClickListener<String> listener) {
            super(context, listener);
        }

        @NonNull
        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
            String string = data.get(position);
            holder.tv_name.setText(string);
        }



        public class ViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_name;

            public ViewHolder(View itemView) {
                super(itemView);
                tv_name = itemView.findViewById(R.id.tv_name);
            }
        }
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
