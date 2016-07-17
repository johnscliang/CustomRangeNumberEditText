# CustomRangeNumberEditText
An custom EditText in Android which could specify an custom range of number.一个可以自定义输入范围的Android EditText控件

# USAGE

## step 1,add the attrs
```xml

    <?xml version="1.0" encoding="utf-8"?>
    <resources>

        <declare-styleable name="CustomRangeNumberEditText">
            <attr name="maxDouble" format="float"/>
            <attr name="minDouble" format="float"/>
        </declare-styleable>

    </resources>

```

## step 2,use the CustomRangeNumberEditText.java in your Layout xml
```xml

    <RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:customrange="http://schemas.android.com/apk/res-auto"
        xmlns:tools="http://schemas.android.com/tools"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:paddingBottom="@dimen/activity_vertical_margin"
        android:paddingLeft="@dimen/activity_horizontal_margin"
        android:paddingRight="@dimen/activity_horizontal_margin"
        android:paddingTop="@dimen/activity_vertical_margin"
        tools:context="cong.lance.customrangenumberedittext.MainActivity">

        <cong.lance.customrangenumberedittext.view.CustomRangeNumberEditText
            android:layout_width="200dp"
            android:layout_height="wrap_content"
            android:layout_centerInParent="true"
            customrange:minDouble="-20"
            customrange:maxDouble="25" />

        <EditText
            android:layout_width="200dp"
            android:layout_height="wrap_content" />
    </RelativeLayout>

```
Remember to change the package name to yours.<br/>
And add the```xmlns:customrange="http://schemas.android.com/apk/res-auto"```

# API
* setMinDouble(double minDouble)
* setMaxDouble(double maxDouble)

# xml attrs
* customrange:minDouble=""
* customrange:maxDouble=""

# Bug
if you find any bug,let's get it fixed.thx :)

