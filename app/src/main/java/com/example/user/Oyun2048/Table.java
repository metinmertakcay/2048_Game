package com.example.user.Oyun2048;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

/*Bu class leaderboard kisminda bulunan karolarin olusturulmasi amaciyla olusturulmustur*/
public class Table extends FrameLayout {

    private TextView textView;

    public Table(Context context) {
        super(context);
        init();
    }

    public Table(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Table(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //Bu fonksiyon ilklendirme islemlerinin gerceklestirilmesi amaciyla olusturulmustur.
    public void init()
    {
        textView = new TextView(getContext());
        textView.setTextSize(14);
        textView.setBackgroundColor(getResources().getColor(R.color.Acik_Gri));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getResources().getColor(R.color.Black));
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1,-1);
        layoutParams.setMargins(15,15,15,15);
        addView(textView,layoutParams);
    }

    public TextView getTextView()
    {
        return textView;
    }
}