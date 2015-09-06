package com.mx.dengxinliang.dxlpagerindicator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.mx.dengxinliang.library.SimplePagerIndicator;
import com.mx.dengxinliang.library.SimplePagerIndicator.SimplePagerIndicatorDrawer;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SimplePagerIndicatorDrawer {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        final List<MyFragment> fragments = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            MyFragment fragment = new MyFragment();
            fragment.setText(String.valueOf(i));
            fragments.add(fragment);
        }

        FragmentManager manager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentPagerAdapter(manager) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });

        SimplePagerIndicator rectIndicator = (SimplePagerIndicator) findViewById(R.id.rect_indicator);
        rectIndicator.setViewPager(viewPager);

        SimplePagerIndicator circleIndicator = (SimplePagerIndicator) findViewById(R.id.circle_indicator);
        circleIndicator.setViewPager(viewPager);

        SimplePagerIndicator customDrawerIndicator = (SimplePagerIndicator) findViewById(R.id.test_custom_drawer_indicator);
        customDrawerIndicator.setViewPager(viewPager);
        customDrawerIndicator.setDrawer(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void draw(Canvas canvas, Paint paint, int measureWidth, int measureHeight, int currentPosition, int currentPositionOffset) {
        paint.setColor(getResources().getColor(android.R.color.holo_red_dark));
        canvas.drawText(getString(R.string.hello_world), 10 + currentPositionOffset, 20 + currentPosition, paint);
//        canvas.drawRect(currentPosition, currentPositionOffset, measureWidth, measureHeight, paint);
    }
}
