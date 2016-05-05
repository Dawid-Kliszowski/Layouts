package pl.droidsonroids.crazylayout;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class LayoutishActivity extends Activity {
    private TextView mTextFirst;
    private CardsLayout mCardsLayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layoutish);

        mTextFirst = (TextView) findViewById(R.id.text_first_card);
        mCardsLayout = (CardsLayout) findViewById(R.id.cards_layout);

        mTextFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                ((ViewGroup) mTextFirst.getParent()).removeView(mTextFirst);
                mCardsLayout.addView(mTextFirst);
            }
        });
    }
}
