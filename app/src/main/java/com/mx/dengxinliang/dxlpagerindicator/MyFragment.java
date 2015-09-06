package com.mx.dengxinliang.dxlpagerindicator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by DengXinliang on 2015/9/6.
 */
public class MyFragment extends Fragment {
    private TextView textView;
    private String str;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textView = (TextView) view.findViewById(R.id.text_view);
        textView.setText(str);
    }

    public void setText(String str) {
        this.str = str;
        if (textView != null) {
            textView.setText(str);
        }
    }
}
