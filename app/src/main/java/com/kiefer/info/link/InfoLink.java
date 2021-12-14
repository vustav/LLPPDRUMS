package com.kiefer.info.link;

import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.kiefer.LLPPDRUMS;

public class InfoLink extends SpannableString {

    public InfoLink(LLPPDRUMS llppdrums, String label, String key, TextView tv){
        super(label);

        View.OnClickListener listener =  new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llppdrums.getInfoManager().getInfoPopup().fireSelectCommand(key);
                llppdrums.getInfoManager().getInfoPopup().setListSVPosition(key);
            }
        };

        setSpan(new ClickableString(listener), 0, label.length(),
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);

        // This line makes the link clickable!
        makeLinksFocusable(tv);
    }

    private SpannableString makeLinkSpan(CharSequence text, View.OnClickListener listener) {
        SpannableString link = new SpannableString(text);
        link.setSpan(new ClickableString(listener), 0, text.length(),
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return link;
    }

    private void makeLinksFocusable(TextView tv) {
        MovementMethod m = tv.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            if (tv.getLinksClickable()) {
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    /*
     * ClickableString class
     */

    private static class ClickableString extends ClickableSpan {
        private View.OnClickListener mListener;
        public ClickableString(View.OnClickListener listener) {
            mListener = listener;
        }
        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }
}
