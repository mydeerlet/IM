package com.mydeerlet.im.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mydeerlet.im.R;
import com.mydeerlet.im.adapter.MsgAdapter;
import com.mydeerlet.im.bean.HeadMsg;
import com.mydeerlet.im.bean.Msg;
import com.mydeerlet.im.bean.User;
import com.mydeerlet.im.utils.SPUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {

    @BindView(R.id.rv_msg)
    RecyclerView rvMsg;
    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.bt_send)
    Button btSend;
    private List<Msg> msgList = new ArrayList<>();
    private MsgAdapter adapter;


    private String IP = "192.168.1.240";
    private String POOL = "8888";
    private Socket so;
    private OutputStream os;
    private InputStream is;
    Gson gson = new Gson();

    private String imCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);


        imCode = getIntent().getStringExtra("imCode");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvMsg.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        rvMsg.setAdapter(adapter);
        //连接服务器
        if (imCode!=null)
        ClineSocket();

        Toast.makeText(this, imCode, Toast.LENGTH_SHORT).show();
    }


    //发送消息
    @OnClick(R.id.bt_send)
    public void onViewClicked() {

        final String content = etInput.getText().toString();
        if ("".equals(content))
            return;

        msgList.add(new Msg(content, Msg.TYPE.SENT));


        HeadMsg headMsg =new HeadMsg();
        headMsg.setImCode(imCode);
        headMsg.setMsg(content);
        final String sendMSG =  gson.toJson(headMsg);



        //如果有新消息，则设置适配器的长度（通知适配器，有新的数据被插入），并让 RecyclerView 定位到最后一行
        int newSize = msgList.size() - 1;
        adapter.notifyItemInserted(newSize);
        rvMsg.scrollToPosition(newSize);
        //清空输入框中的内容
        etInput.setText("");
        if (os == null) return;
        new Thread() {
            @Override
            public void run() {
                //首先需要计算得知消息的长度
                byte[] sendBytes = sendMSG.getBytes();
                //然后将消息的长度优先发送出去
                try {
                    os.write(sendBytes.length >> 8);
                    os.write(sendBytes.length);
                    //然后将消息再次发送出去
                    os.write(sendBytes);
                    os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    @OnClick(R.id.bt_login)
    public void login() {
        ClineSocket();
        if (os==null)return;


        User user = new User();
        user.setImCode(SPUtils.getCurrentUser(this).getImCode());
        final String message = gson.toJson(user);

        new Thread() {
            @Override
            public void run() {
                //首先需要计算得知消息的长度
                byte[] sendBytes = message.getBytes();
                //然后将消息的长度优先发送出去
                try {
                    os.write(sendBytes.length >> 8);
                    os.write(sendBytes.length);
                    //然后将消息再次发送出去
                    os.write(sendBytes);
                    os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    //获取一个连接
    public void ClineSocket() {
        new Thread() {
            @Override
            public void run() {
                try {
                    so = new Socket(IP, 8888);

                    // 3.获取一个输入流，并读取客户端信息
                    // 字节输入流
                    is = so.getInputStream();
                    // 4.获取一个输出流，向客户端输出信息,响应客户端的请求
                    // 字节输出流
                    os = so.getOutputStream();
                    //接收数据
                    byte[] bytes;
                    while (true) {
                        int first = is.read();
                        if (first == -1) {
                            break;
                        }
                        int second = is.read();
                        int length = (first << 8) + second;
                        bytes = new byte[length];
                        is.read(bytes);
                        final String msg = new String(bytes, "utf-8");

                        System.out.println(msg);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                msgList.add(new Msg(msg, Msg.TYPE.RECEIVED));
                                //如果有新消息，则设置适配器的长度（通知适配器，有新的数据被插入），并让 RecyclerView 定位到最后一行
                                int newSize = msgList.size() - 1;
                                adapter.notifyItemInserted(newSize);
                                rvMsg.scrollToPosition(newSize);
                            }
                        });
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    // 5.关闭资源
                    try {
                        if (os != null)
                            os.close();
                        if (is != null)
                            is.close();
                        if (!so.isClosed())
                            so.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();
    }


}
