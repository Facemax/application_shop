package com.example.face.application_shoptwo.Activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.example.face.application_shoptwo.Model.JavaBean.URobotList;
import com.example.face.application_shoptwo.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.HashMap;

public class KefuActivity extends BaseActivity {
    String q;
    ArrayList<HashMap<String,Object>> chatList=null;
    String[] from={"image","text"};
    int[] to={R.id.chatlist_image_me,R.id.chatlist_text_me,R.id.chatlist_image_other,R.id.chatlist_text_other};
    int[] layout={R.layout.chat_listitem_me,R.layout.chat_listitem_other};

    public final static int OTHER=1;
    public final static int ME=0;


    protected ListView chatListView=null;
    protected Button chatSendButton=null;
     EditText editText=null;

    protected MyChatAdapter adapter=null;
   ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kefu);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        back=(ImageView)findViewById(R.id.btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




        chatList=new ArrayList<HashMap<String,Object>>();
        /*addTextToList("你好", ME);*/
        addTextToList("您好，比价机器人很高兴为您服务", OTHER);
        chatSendButton=(Button)findViewById(R.id.btn_as);
        editText=(EditText)findViewById(R.id.question);
        chatListView=(ListView)findViewById(R.id.chat_list);
        adapter=new MyChatAdapter(this,chatList,layout,from,to);
        chatSendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String myWord = null;

                /**
                 * 这是一个发送消息的监听器，注意如果文本框中没有内容，那么getText()的返回值可能为
                 * null，这时调用toString()会有异常！所以这里必须在后面加上一个""隐式转换成String实例
                 * ，并且不能发送空消息。
                 */
                q = editText.getText().toString();
                myWord = (q+ "").toString();
                if (myWord.length() == 0)
                    return;
                editText.setText("");
                addTextToList(myWord, ME);
                /**
                 * 更新数据列表，并且通过setSelection方法使ListView始终滚动在最底端
                 */
                adapter.notifyDataSetChanged();

                chatListView.setSelection(chatList.size() - 1);
                getAnswer();


            }
        });

        chatListView.setAdapter(adapter);



    }

    private void getAnswer(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://op.juhe.cn/robot/index?info="+q+"&key=b84ccebab4eb4e22e957dbae8fccce21", new AsyncHttpResponseHandler(){

            @Override
            public void onFailure(Throwable arg0, String arg1) {
                // TODO 自动生成的方法存根
                super.onFailure(arg0, arg1);
            }

            @Override
            public void onSuccess(int arg0, String arg1) {
                // TODO 自动生成的方法存根
                super.onSuccess(arg0, arg1);
                Log.i("json", arg1);
                URobotList robot = JSON.parseObject(arg1, URobotList.class);
                if (robot.getError_code()!=0){
                    addTextToList("SuKi不太懂您表达的意思喔，请详细表达~", OTHER);
                    adapter.notifyDataSetChanged();
                    chatListView.setSelection(chatList.size()-1);
                }else {
                    addTextToList(robot.getResult().getText().toString(), OTHER);
                    adapter.notifyDataSetChanged();
                    chatListView.setSelection(chatList.size()-1);
                    if (robot.getResult().getUrl()!=null){
                        addTextToList(robot.getResult().getUrl().toString(), OTHER);
                        adapter.notifyDataSetChanged();
                        chatListView.setSelection(chatList.size()-1);
                        Log.e("KEfu",""+robot.getResult().getUrl());
                    }
                }

            }

        });
    }

    protected void addTextToList(String text, int who){
        HashMap<String,Object> map=new HashMap<String,Object>();
        map.put("person",who );
        map.put("image", who==ME?R.mipmap.logo:R.mipmap.logo);
        map.put("text", text);
        chatList.add(map);
    }

    private class MyChatAdapter extends BaseAdapter {

        Context context=null;
        ArrayList<HashMap<String,Object>> chatList=null;
        int[] layout;
        String[] from;
        int[] to;



        public MyChatAdapter(Context context,
                             ArrayList<HashMap<String, Object>> chatList, int[] layout,
                             String[] from, int[] to) {
            super();
            this.context = context;
            this.chatList = chatList;
            this.layout = layout;
            this.from = from;
            this.to = to;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return chatList.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        class ViewHolder{
            public ImageView imageView=null;
           // public TextView textView=null;
            public EditText editText=null;


        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            ViewHolder holder=null;
            int who=(Integer)chatList.get(position).get("person");

            convertView= LayoutInflater.from(context).inflate(
                    layout[who==ME?0:1], null);
            holder=new ViewHolder();
            holder.imageView=(ImageView)convertView.findViewById(to[who*2+0]);
            holder.editText=(EditText)convertView.findViewById(to[who*2+1]);


            System.out.println(holder);
            System.out.println("WHYWHYWHYWHYW");
            System.out.println(holder.imageView);
            //holder.imageView.setBackgroundResource((Integer)chatList.get(position).get(from[0]));
            holder.editText.setText(chatList.get(position).get(from[1]).toString());


            return convertView;
        }

    }

}
