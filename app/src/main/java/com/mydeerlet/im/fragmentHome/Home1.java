package com.mydeerlet.im.fragmentHome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mydeerlet.im.R;
import com.mydeerlet.im.activity.ChatActivity;
import com.mydeerlet.im.base.BaseAdapter;
import com.mydeerlet.im.base.OnItemClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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

            }
        });
        rvHome1.setAdapter(adapter);


        getData();

        startActivity(new Intent(getContext(), ChatActivity.class));

    }




    public void getData() {

//        RetrofitManager.getInstance(getContext())
//                .create(LoginService.class);

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
            return null;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }

}
