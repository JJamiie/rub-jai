package com.rashata.jamie.spend.views.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.util.GuideItem;
import com.rashata.jamie.spend.util.RubjaiPreference;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity {
    private ArrayList<GuideItem> guideItems;
    private ViewPager pager_guide;
    private RadioGroup radioGroup;
    private Button btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        setWidget();
    }

    private void setWidget() {
        setGuideItem();
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        pager_guide = (ViewPager) findViewById(R.id.pager_guide);
        btn_start = (Button) findViewById(R.id.btn_start);
        pager_guide.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                LayoutInflater inflater = getLayoutInflater();
                ViewGroup view = (ViewGroup) inflater.inflate(R.layout.guide_1, container, false);
                ImageView img_guide = (ImageView) view.findViewById(R.id.img_guide);
                TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
                TextView txt_description1 = (TextView) view.findViewById(R.id.txt_description1);
                TextView txt_description2 = (TextView) view.findViewById(R.id.txt_description2);
                txt_title.setText(guideItems.get(position).getTitle());
                txt_description1.setText(guideItems.get(position).getDescription1());
                txt_description2.setText(guideItems.get(position).getDescription2());
                if (position > 0) {
                    txt_description2.setVisibility(View.GONE);
                } else {
                    txt_description1.setVisibility(View.VISIBLE);
                    txt_description2.setVisibility(View.VISIBLE);

                }

                Glide.with(getActivity()).load(guideItems.get(position).getImgGuide()).into(img_guide);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });
        pager_guide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                radioGroup.check(radioGroup.getChildAt(position).getId());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        radioGroup.check(radioGroup.getChildAt(0).getId());
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RubjaiPreference rubjaiPreference = new RubjaiPreference(getActivity());
                rubjaiPreference.guideTour = "done";
                rubjaiPreference.update();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void setGuideItem() {
        guideItems = new ArrayList<>();
        guideItems.add(new GuideItem("ยินดีต้อนรับ", "เข้าสู่แอพพลิเคชั่นรับจ่ายที่", "เหมาะสมกับการทำงานโดยแท้จริง", R.drawable.guide1));
        guideItems.add(new GuideItem("วิธีการใช้งาน", "", "", R.drawable.guide2));
        guideItems.add(new GuideItem("วิธีการใช้งาน", "", "", R.drawable.guide3));
        guideItems.add(new GuideItem("วิธีการใช้งาน", "", "", R.drawable.guide4));
        guideItems.add(new GuideItem("วิธีการใช้งาน", "", "", R.drawable.guide5));
    }

    public Activity getActivity() {
        return this;
    }
}
