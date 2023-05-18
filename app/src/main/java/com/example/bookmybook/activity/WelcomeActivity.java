package com.example.bookmybook.activity;

import static com.example.bookmybook.R.color.transparent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bookmybook.R;
import com.example.bookmybook.others.PreferenceManager;

public class WelcomeActivity extends AppCompatActivity {

    PreferenceManager preferenceManager;
    LinearLayout Layout_bars;
    TextView[] bottomBars;
    int[] screens;
    ImageView Skip, ivNext1, ivNext2, ivNext3;
    ViewPager vp;
    MyViewPagerAdapter myvpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        vp = (ViewPager) findViewById(R.id.view_pager);
        Layout_bars = (LinearLayout) findViewById(R.id.layoutBars);
        Skip = (ImageView) findViewById(R.id.skip);
        ivNext1 = (ImageView) findViewById(R.id.ivNext1);
        ivNext2 = (ImageView) findViewById(R.id.ivNext2);
        ivNext3 = (ImageView) findViewById(R.id.ivNext3);
        screens = new int[]{
                R.layout.welcome_activity1,
                R.layout.welcome_activity2,
                R.layout.welcome_activity3,
        };
        myvpAdapter = new MyViewPagerAdapter();
        vp.setAdapter(myvpAdapter);
        preferenceManager = new PreferenceManager(this);
        vp.addOnPageChangeListener(viewPagerPageChangeListener);
        if (!preferenceManager.FirstLaunch()) {
            launchMain();
            finish();
        }
        ColoredBars(0);
    }

    public void next(View v) {
        int i = getItem(+1);
        if (i < screens.length) {
            vp.setCurrentItem(i);
        } else {
            launchMain();
        }
    }

    public void skip(View view) {
        launchMain();
    }

    private void ColoredBars(int thisScreen) {
        int[] colorsInactive = getResources().getIntArray(R.array.dot_on_page_not_active);
        int[] colorsActive = getResources().getIntArray(R.array.dot_on_page_active);
        bottomBars = new TextView[screens.length];

        Layout_bars.removeAllViews();
//        Layout_bars.setBackgroundColor(Color.parseColor("#00ffffff"));

        for (int i = 0; i < bottomBars.length; i++) {
            bottomBars[i] = new TextView(this);
            bottomBars[i].setTextSize(100);
            bottomBars[i].setText(Html.fromHtml("&#175"));
            Layout_bars.addView(bottomBars[i]);
            bottomBars[i].setTextColor(colorsInactive[thisScreen]);
            bottomBars[i].setBackgroundColor(getResources().getColor(transparent));

        }
        if (bottomBars.length > 0)
            bottomBars[thisScreen].setTextColor(colorsActive[thisScreen]);
    }

    private int getItem(int i) {
        return vp.getCurrentItem() + i;
    }

    private void launchMain() {
        preferenceManager.setFirstTimeLaunch(false);
        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
        finish();
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            ColoredBars(position);

//            if (position == screens.length - 3) {
//                Skip.setVisibility(View.VISIBLE);
//                ivNext1.setVisibility(View.GONE);
//                ivNext2.setVisibility(View.GONE);
//                ivNext3.setVisibility(View.VISIBLE);
//            }

            if (position == screens.length - 2) {
                Skip.setVisibility(View.VISIBLE);
                ivNext1.setVisibility(View.GONE);
                ivNext2.setVisibility(View.VISIBLE);
                ivNext3.setVisibility(View.GONE);
            }

            if (position == screens.length - 1) {
                Skip.setVisibility(View.VISIBLE);
                ivNext1.setVisibility(View.GONE);
                ivNext2.setVisibility(View.GONE);
                ivNext3.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater inflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(screens[position], container, false);
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return screens.length;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View v = (View) object;
            container.removeView(v);
        }

        @Override
        public boolean isViewFromObject(View v, Object object) {
            return v == object;
        }
    }


}