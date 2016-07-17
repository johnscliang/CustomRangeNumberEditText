package cong.lance.customrangenumberedittext.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

import cong.lance.customrangenumberedittext.R;

/**
 * Created by lance on 16-7-17.
 */
public class CustomRangeNumberEditText extends EditText{

    private double minDouble;
    private double maxDouble;
    private int situation;
    private final int ALL_POSITIVE = 1;
    private final int ALL_NAGETIVE = 2;
    private final int NAGETIVE_POSITIVE = 3;

    public double getMinDouble() {
        return minDouble;
    }

    public void setMinDouble(double minDouble) {
        this.minDouble = minDouble;
    }

    public double getMaxDouble() {
        return maxDouble;
    }

    public void setMaxDouble(double maxDouble) {
        this.maxDouble = maxDouble;
    }

    public CustomRangeNumberEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttributes(context,attrs);
        init();
    }

    public CustomRangeNumberEditText(Context context) {
        super(context);
        init();
    }

    public CustomRangeNumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context,attrs);
        init();
    }

    private void parseAttributes(Context context, AttributeSet attributeSet) {
        if (attributeSet == null) return;
        //some attr setting
        setSingleLine(true);//it would change the input panel's Enter key to "complete"
        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.CustomRangeNumberEditText);
        try {
            setMaxDouble(a.getFloat(R.styleable.CustomRangeNumberEditText_maxDouble,0));
            setMinDouble(a.getFloat(R.styleable.CustomRangeNumberEditText_minDouble,0));
            if(getMaxDouble() >= 0 && getMinDouble() >= 0 ){
                situation = ALL_POSITIVE;
            }
            if(getMaxDouble() >= 0 && getMinDouble() < 0 ){
                situation = NAGETIVE_POSITIVE;
            }
            if(getMaxDouble() < 0 && getMinDouble() < 0 ){
                situation = ALL_NAGETIVE;
            }
        } catch (Exception e) {
            Log.v("parse error", "parse attributes");
        } finally {
            a.recycle();
        }
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new LimitInputConnectionWrapper(super.onCreateInputConnection(outAttrs), false);
    }

    private void  init(){
        this.setCustomSelectionActionModeCallback(new ActionModeCallBack());
        this.setLongClickable(false);
    }

    private class ActionModeCallBack implements ActionMode.Callback{

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    }

    private String preContent;//the content before input
    private String currentContent;//the content after input

    private boolean overload(CharSequence text, int newCursorPosition) {

        preContent = getText().toString();
        currentContent = preContent+text;
        StringBuffer currentStringBuffer = new StringBuffer(getText().toString());
        currentStringBuffer.insert(getSelectionStart(), text);

        Log.v("preContent",String.valueOf(preContent.isEmpty()?"empty":preContent));
        Log.v("currentContent",String.valueOf(currentContent));
        Log.v("currentStringBuffer",String.valueOf(currentStringBuffer.toString().isEmpty()?"empty":currentStringBuffer));

        if(situation == ALL_POSITIVE){
            //input the point at the first time,reject
            if(getText().toString().isEmpty()){
                if(text.toString().equals(".")){
                    return true;
                }
            }
            //can not input more than 2 points
            if(preContent.contains(".") && text.toString().equals(".")){
                return true;
            }
            //the content is the max double,reject any input
            if(!preContent.isEmpty() && !preContent.equals("-") && Double.parseDouble(preContent) == maxDouble){
                return true;
            }
            //should not star with 0
            if(preContent.equals("0") && !text.equals(".")){
                return true;
            }

            double newNumber = 0;
            try {
                newNumber = Double.parseDouble(currentStringBuffer.toString());
            }catch (Exception e){
                Log.v("parse error", "parse currentStringBuffer");
                return true;
            }
            Log.v("newNumber",String.valueOf(newNumber));
            if (newNumber > maxDouble) {
                return true;
            } else {
                return false;
            }
        }else if(situation == ALL_NAGETIVE){

            if(getText().toString().isEmpty()){
                if(text.toString().equals("-")) {
                    return false;
                }else {
                    return true;
                }
            }
            if(preContent.contains(".") && text.toString().equals(".")){
                return true;
            }
            if(!preContent.isEmpty() && !preContent.equals("-") && Double.parseDouble(preContent) == minDouble){
                return true;
            }
            if(preContent.equals("-0") && !text.equals(".")){
                return true;
            }

            double newNumber = 0;
            try {
                newNumber = Double.parseDouble(currentStringBuffer.toString());
            }catch (Exception e){
                Log.v("parse error", "parse currentStringBuffer");
                return true;
            }
            Log.v("newNumber",String.valueOf(newNumber));
            if (newNumber < minDouble) {
                return true;
            } else {
                return false;
            }


        }else if(situation == NAGETIVE_POSITIVE){

        }else {
            return false;
        }
        return false;
    }

    private class LimitInputConnectionWrapper extends InputConnectionWrapper {
        public LimitInputConnectionWrapper(InputConnection target, boolean mutable) {
            super(target, mutable);
        }




        @Override
        public boolean commitText(CharSequence text, int newCursorPosition) {
            if (overload(text, newCursorPosition)) {
                return false;
            }
            return super.commitText(text, newCursorPosition);
        }


    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if(!focused){
            long currentnumber = 0;
            try {
                currentnumber = Long.parseLong(getText().toString());
//                Log.v("currentnumber",String.valueOf(currentnumber));
                if(currentnumber < minDouble){
                    setText("");
                }
                if(currentnumber > maxDouble){
                    setText("");
                }
            }catch (Exception e){
                setText("");
            }
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }
}
