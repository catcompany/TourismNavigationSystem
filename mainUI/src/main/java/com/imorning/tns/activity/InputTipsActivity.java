package com.imorning.tns.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.imorning.tns.R;
import com.imorning.tns.adapter.InputTipsAdapter;
import com.imorning.tns.ui.map.MapFragment;
import com.imorning.tns.utils.Constants;
import com.imorning.tns.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class InputTipsActivity extends AppCompatActivity implements
        SearchView.OnQueryTextListener, Inputtips.InputtipsListener,
        OnItemClickListener, View.OnClickListener {
    private static final String TAG = "InputTipsActivity";
    private List<Tip> mCurrentTipList;
    private InputTipsAdapter mIntipAdapter;
    private ListView mInputListView;
    private String currentCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_tips);
        initSearchView();
        mInputListView = findViewById(R.id.input_tip_list);
        mInputListView.setOnItemClickListener(this);
        findViewById(R.id.input_search_back).setOnClickListener(this);
        currentCity = getIntent().getExtras().getString(MapFragment.CURRENT_CITY);
    }

    private void initSearchView() {
        // 输入搜索关键字
        SearchView searchView = findViewById(R.id.keyWord);
        searchView.setOnQueryTextListener(this);
        //设置SearchView默认为展开显示
        searchView.setIconified(false);
        searchView.onActionViewExpanded();
        searchView.setIconifiedByDefault(true);
        searchView.setSubmitButtonEnabled(false);
    }

    /**
     * 输入提示回调
     *
     * @param tipList 提示列表
     * @param rCode   状态码
     */
    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        // 正确返回
        if (rCode == 1000) {
            mCurrentTipList = tipList;
            List<String> listString = new ArrayList<>();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i).getName());
            }
            mIntipAdapter = new InputTipsAdapter(getApplicationContext(), mCurrentTipList);
            mInputListView.setAdapter(mIntipAdapter);
            mIntipAdapter.notifyDataSetChanged();
        } else {
            ToastUtil.showerror(this, rCode);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mCurrentTipList != null) {
            Tip tip = (Tip) adapterView.getItemAtPosition(i);
            Intent intent = new Intent();
            intent.putExtra(Constants.EXTRA_TIP, tip);
            setResult(MapFragment.RESULT_CODE_INPUTTIPS, intent);
            this.finish();
        }
    }

    /**
     * 按下确认键触发，本例为键盘回车或搜索键
     *
     * @param query 查询词
     * @return ...
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent intent = new Intent();
        intent.putExtra(Constants.KEY_WORDS_NAME, query);
        setResult(MapFragment.RESULT_CODE_KEYWORDS, intent);
        this.finish();
        return false;
    }

    /**
     * 输入字符变化时触发
     *
     * @param newText 输入的新词
     * @return ...
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        if (!TextUtils.isEmpty(newText)) {
            Log.d(TAG, "onQueryTextChange: " + newText);
            InputtipsQuery inputquery = new InputtipsQuery(newText, currentCity);
            Inputtips inputTips = new Inputtips(InputTipsActivity.this.getApplicationContext(), inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        } else {
            if (mIntipAdapter != null && mCurrentTipList != null) {
                mCurrentTipList.clear();
                mIntipAdapter.notifyDataSetChanged();
            }
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.input_search_back) {
            this.finish();
        }
    }
}
