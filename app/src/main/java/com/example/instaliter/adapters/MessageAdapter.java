package com.example.instaliter.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instaliter.R;
import com.example.instaliter.Message;
import android.content.Context;


import java.util.ArrayList;
import java.util.List;

public class MessageAdapter  extends BaseAdapter{

        List<Message> messages = new ArrayList<Message>();
        Context context;

        public MessageAdapter(Context context) {
            this.context = context;
        }


        public void add(Message message) {
            this.messages.add(message);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return messages.size();
        }

        @Override
        public Object getItem(int i) {
            return messages.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            MessageViewHolder holder = new MessageViewHolder();
            LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            Message message = messages.get(i);

            if (message.isBelongsToCurrentUser())
            {
                convertView = messageInflater.inflate(R.layout.my_mess_layout, null);
                holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
                convertView.setTag(holder);
                holder.messageBody.setText(message.getMessageText());
            }
            else
                {
                convertView = messageInflater.inflate(R.layout.their_mess_layout, null);
                holder.avatar = (ImageView) convertView.findViewById(R.id.userPhotoMessage);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.messageBody = (TextView) convertView.findViewById(R.id.message_body);
                convertView.setTag(holder);

                Glide.with(context).load(message.getRecieverThumbnail()).into(holder.avatar);
                holder.name.setText(message.getRecieverName());
                holder.messageBody.setText(message.getMessageText());
            }

            return convertView;
        }
    }

class MessageViewHolder {
    public ImageView avatar;
    public TextView name;
    public TextView messageBody;
}
