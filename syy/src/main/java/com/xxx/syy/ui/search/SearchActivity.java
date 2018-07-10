package com.xxx.syy.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.Group;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.xxx.library.base.BaseActivity;
import com.xxx.library.network.exception.ExceptionHandle;
import com.xxx.library.utils.DeviceUtil;
import com.xxx.library.utils.KeyBoardUtil;
import com.xxx.library.views.LoadingLayoutHelper;
import com.xxx.library.views.ToastHelper;
import com.xxx.syy.R;
import com.xxx.syy.entity.History;
import com.xxx.syy.entity.SearchMovieInfo;
import com.xxx.syy.entity.Subjects;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity<SearchPresenter> implements SearchContract.View {

    public final static int TAG_MOVIE = 0;
    public final static int TAG_BOOK = 1;
    public final static int TAG_MUSIC = 2;

    private EditText et_syy_search_content;
    private ViewGroup cl_syy_search_container;
    private Spinner spinner;
    private RecyclerView rv_syy_search_result;

    private int currentType = TAG_MOVIE;
    private SearchMovieAdapter searchMovieAdapter;
    private SearchHistoryAdapter historyAdapter;

    private ArrayList<Subjects> movieDatas = new ArrayList<>();
    private Group group_syy_search_history;

    private String currentQ;
    private ArrayList<History> histories = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.syy_activity_search);
        if (savedInstanceState == null) {
            et_syy_search_content = findViewById(R.id.et_syy_search_content);
            et_syy_search_content.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                    if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                        search(true);
                    }
                    return false;
                }

            });
            et_syy_search_content.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (TextUtils.isEmpty(s.toString())) {
                        group_syy_search_history.setVisibility(View.VISIBLE);
                        rv_syy_search_result.setVisibility(View.INVISIBLE);
                        presenter.getHistory();
                    }
                }
            });

            spinner = findViewById(R.id.sp_syy_search_type);
            ArrayAdapter<String> spinnerAdapter = new SpinnerAdapter(this, getResources().getStringArray(R.array.syy_search_type));
            spinner.setAdapter(spinnerAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //点击时调相应接口 若点击后type与之前相同 则不作处理
                    if (position == currentType) {
                        return;
                    }
                    currentType = position;
                    et_syy_search_content.setHint(getString(R.string.syy_search) + spinner.getItemAtPosition(position));
                    search(false);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            findViewById(R.id.iv_syy_search).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    search(true);
                }
            });
            cl_syy_search_container = findViewById(R.id.cl_syy_search_container);
            group_syy_search_history = findViewById(R.id.group_syy_search_history);
            findViewById(R.id.tv_syy_search_history_clear).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (presenter.clearHistory()) {
                        histories.clear();
                        historyAdapter.setNewData(histories);
                    }
                }
            });


            initResultRecyclerView();
        }
    }

    private void initResultRecyclerView() {
        rv_syy_search_result = findViewById(R.id.rv_syy_search_result);
        rv_syy_search_result.setAdapter(searchMovieAdapter = new SearchMovieAdapter(this, movieDatas));
        searchMovieAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);

        RecyclerView rv_syy_search_history = findViewById(R.id.rv_syy_search_history);
        rv_syy_search_history.setAdapter(historyAdapter = new SearchHistoryAdapter(histories));
        historyAdapter.openLoadAnimation();
        historyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String q = historyAdapter.getData().get(position).q;
                et_syy_search_content.setText(q);
                et_syy_search_content.setSelection(q.length());
                search(true);
            }
        });
        presenter.getHistory();
    }


    private void search(boolean isTip) {
        this.search(-1, isTip);
    }

    private void search(int requestCode, boolean isTip) {
        KeyBoardUtil.closeKeybord(et_syy_search_content, this);
        String q = et_syy_search_content.getText().toString().trim();
        if (TextUtils.isEmpty(q)) {
            if (isTip) {
                ToastHelper.showToast("大哥说点啥吧！");
            }
            return;
        }
        int type = requestCode < 0 ? currentType : requestCode;
        //TODO
        switch (type) {
            case TAG_MOVIE:
                group_syy_search_history.setVisibility(View.GONE);
                LoadingLayoutHelper.addLoadingView(cl_syy_search_container);
                presenter.searchMovies(q, TAG_MOVIE);
                currentQ = q;
                break;
            case TAG_BOOK:

                break;
            case TAG_MUSIC:

                break;
        }
    }


    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter(this);
    }

    @Override
    public void onFailure(ExceptionHandle.ResponseThrowable responseThrowable, final int requestCode) {
        LoadingLayoutHelper.addFailureView(cl_syy_search_container, null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search(requestCode, true);
            }
        });
    }

    @Override
    public void showSearchMovies(SearchMovieInfo movieInfo) {
        LoadingLayoutHelper.removeLoadingView(cl_syy_search_container);
        rv_syy_search_result.setVisibility(View.VISIBLE);
        group_syy_search_history.setVisibility(View.GONE);
        movieDatas = (ArrayList<Subjects>) movieInfo.subjects;
        searchMovieAdapter.setEmptyView(R.layout.syy_item_search_history_nodata, (ViewGroup) rv_syy_search_result.getParent());

        searchMovieAdapter.setNewData(movieDatas);
        History history = new History();
        history.q = currentQ;
        presenter.saveHistory(history);
    }

    @Override
    public void showHistory(List<History> histories) {
        historyAdapter.setNewData(this.histories = (ArrayList<History>) histories);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        KeyBoardUtil.closeKeybord(et_syy_search_content, this);
    }

    class SpinnerAdapter extends ArrayAdapter<String> {
        private Context mContext;
        private String[] mStringArray;
        private int padding;

        SpinnerAdapter(Context context, String[] mStringArray) {
            super(context, android.R.layout.simple_spinner_item, mStringArray);
            mContext = context;
            this.mStringArray = mStringArray;
            padding = DeviceUtil.dip2px(10);
        }

        @Override
        public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
            //修改Spinner展开后的字体颜色
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
            }
            TextView tv = convertView.findViewById(android.R.id.text1);
            tv.setText(mStringArray[position]);
            tv.setPadding(padding, padding, padding, padding);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            return convertView;

        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            // 修改Spinner选择后结果的字体颜色
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
            }
            TextView tv = convertView.findViewById(android.R.id.text1);
            tv.setText(mStringArray[position]);
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
            return convertView;
        }

    }
}
