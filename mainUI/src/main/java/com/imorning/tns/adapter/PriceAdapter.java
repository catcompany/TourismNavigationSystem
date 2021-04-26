package com.imorning.tns.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.appcompat.widget.AppCompatTextView;

import com.imorning.senseinfohelper.bean.scenedata.Price;
import com.imorning.senseinfohelper.bean.scenedata.PriceBean;
import com.imorning.tns.R;

import java.util.List;

public class PriceAdapter extends BaseAdapter {

    private final Context context;
    private final List<PriceBean> priceBeans;

    public PriceAdapter(Context context, List<PriceBean> priceBeans) {
        this.context = context;
        this.priceBeans = priceBeans;
    }

    @Override
    public int getCount() {
        return priceBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return priceBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.price_item_layout, null);
        viewHolder.tv_type = convertView.findViewById(R.id.price_item_type);
        viewHolder.tv_price = convertView.findViewById(R.id.price_item_info);
        convertView.setTag(viewHolder);

        viewHolder.tv_type.setText(this.priceBeans.get(position).getType());

        List<Price> prices = priceBeans.get(position).getEntityList();
        StringBuilder priceBuffer = new StringBuilder();
        for (Price price : prices) {
            priceBuffer.append(price.getTicketName());
            priceBuffer.append("\n零售价：");
            priceBuffer.append(price.getAmount());
            priceBuffer.append("\n建议零售价：");
            priceBuffer.append(price.getAmountAdvice());
        }
        Log.d("SenseInfoActivity", "getView: " + priceBuffer);
        viewHolder.tv_price.setText(priceBuffer.toString());
        return convertView;
    }

    static class ViewHolder {
        private AppCompatTextView tv_type;
        private AppCompatTextView tv_price;
    }
}
