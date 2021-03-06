package com.imorning.tns.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.imorning.senseinfohelper.bean.scenedata.PicList;
import com.imorning.senseinfohelper.bean.scenedata.PriceBean;
import com.imorning.senseinfohelper.bean.scenedata.SenseInfoContentList;
import com.imorning.senseinfohelper.utils.StringUtils;
import com.imorning.tns.R;
import com.imorning.tns.adapter.PriceAdapter;
import com.imorning.tns.databinding.ActivitySenseInfoBinding;

import java.text.MessageFormat;
import java.util.List;


/**
 * 景点详情
 *
 * @author iMorning
 */
public class SenseInfoActivity extends AppCompatActivity {

    public static final String DATA_KEY = "data";
    private static final String TAG = "SenseInfoActivity";
    private ActivitySenseInfoBinding viewBinding;
    private SenseInfoContentList senseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBinding = ActivitySenseInfoBinding.inflate(LayoutInflater.from(this));
        setContentView(viewBinding.getRoot());
        initData((SenseInfoContentList) getIntent().getExtras().get(DATA_KEY));
        initView();
    }

    /**
     * 处理传入的数据
     *
     * @param senseData 数据
     */
    private void initData(SenseInfoContentList senseData) {
        if (senseData == null) {
            Log.e(TAG, "onCreate: ", new NullPointerException());
            return;
        }
        this.senseData = senseData;
    }

    /**
     * 将数据绑定到界面上
     */
    private void initView() {
        //景点名称
        viewBinding.senseInfoName.setText(senseData.getName());
        //开放时间
        viewBinding.senseInfoTime.setText(formatInfoTime(senseData.getOpentime()));
        //优惠政策
        viewBinding.senseInfoCoupon.setText(senseData.getCoupon());
        //注意事项
        viewBinding.senseInfoTip.setText(senseData.getAttention());
        //景点简介以及地址
        if (StringUtils.isEmpty(senseData.getAddress())) {
            viewBinding.senseInfoSummary.setText(senseData.getSummary());
        } else {
            viewBinding.senseInfoSummary.setText(MessageFormat.format("{0}\n\n地址：{1}", senseData.getSummary(), senseData.getAddress()));
        }
        //图片列表
        if (senseData.getPicList().size() > 0) {
            loadPic(senseData.getPicList());
        } else {
            viewBinding.senseInfoImage.setImageResource(R.mipmap.ic_no_img);
        }
        //价格列表
        List<PriceBean> prices = senseData.getPriceList();
        if (prices != null && prices.size() > 0) {
            loadPrice(prices);
        } else {
            viewBinding.senseInfoLayoutPrice.setVisibility(View.GONE);
        }
    }

    /**
     * 处理时间中的乱码
     *
     * @param opentime 开放时间
     * @return ...
     */
    private String formatInfoTime(String opentime) {
        String time = opentime.trim();
        time = time.replaceAll("&mdash;", "-");
        return time;
    }

    /**
     * 价格列表生成并绑定到视图上
     *
     * @param prices {@link PriceBean}列表
     */
    private void loadPrice(List<PriceBean> prices) {
        PriceAdapter priceAdapter = new PriceAdapter(SenseInfoActivity.this, prices);
        viewBinding.senseInfoPrice.setAdapter(priceAdapter);
    }

    /**
     * 加载图片
     *
     * @param picList 图片列表
     */
    private void loadPic(List<PicList> picList) {
        //默认获取第一张图片
        String url = picList.get(0).getPicUrl();
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        ImageLoader imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {

            }
        });
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(viewBinding.senseInfoImage,
                R.mipmap.ic_loading, android.R.drawable.stat_notify_error);

        // Add the request to the RequestQueue.
        imageLoader.get(url, listener);
        //queue.add(listener);
    }

}