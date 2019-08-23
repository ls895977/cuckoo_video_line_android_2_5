package com.ailiaovideo.videoline.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Looper;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ZoomButtonsController;

import com.blankj.utilcode.util.ScreenUtils;
import com.ailiaovideo.videoline.MyApplication;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class BGViewUtil
{

    public static void setBackgroundDrawable(View view, Drawable drawable)
    {
        int paddingTop = view.getPaddingTop();
        int paddingBottom = view.getPaddingBottom();
        int paddingLeft = view.getPaddingLeft();
        int paddingRight = view.getPaddingRight();
        view.setBackgroundDrawable(drawable);
        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    public static void setBackgroundResource(View view, int resId)
    {
        int paddingTop = view.getPaddingTop();
        int paddingBottom = view.getPaddingBottom();
        int paddingLeft = view.getPaddingLeft();
        int paddingRight = view.getPaddingRight();
        view.setBackgroundResource(resId);
        view.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    public static void setBackgroundColorResId(View view, int resId)
    {
        view.setBackgroundColor(MyApplication.getInstances().getResources().getColor(resId));
    }

    public static void setTextViewColorResId(TextView textView, int resId)
    {
        textView.setTextColor(MyApplication.getInstances().getResources().getColor(resId));
    }

    @SuppressLint("NewApi")
    public static void scrollToViewY(final ScrollView sv, final int y, int delay)
    {
        if (sv != null && delay >= 0)
        {
            if (Build.VERSION.SDK_INT >= VERSION_CODES.HONEYCOMB)
            {
                SDHandlerManager.getMainHandler().postDelayed(new Runnable()
                {

                    @Override
                    public void run()
                    {
                        sv.scrollTo(0, y);
                    }
                }, delay);
            }
        }
    }

    // -------------------------layoutParams
    // LinearLayout
    public static LayoutParams getLayoutParamsLinearLayoutWW()
    {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    public static LayoutParams getLayoutParamsLinearLayoutMM()
    {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public static LayoutParams getLayoutParamsLinearLayoutMW()
    {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    public static LayoutParams getLayoutParamsLinearLayoutWM()
    {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
    }

    // RelativeLayout
    public static RelativeLayout.LayoutParams getLayoutParamsRelativeLayoutWW()
    {
        return new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    public static RelativeLayout.LayoutParams getLayoutParamsRelativeLayoutMM()
    {
        return new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
    }

    public static RelativeLayout.LayoutParams getLayoutParamsRelativeLayoutMW()
    {
        return new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    public static RelativeLayout.LayoutParams getLayoutParamsRelativeLayoutWM()
    {
        return new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
    }

    // FrameLayout
    public static FrameLayout.LayoutParams getLayoutParamsFrameLayoutWW()
    {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    public static FrameLayout.LayoutParams getLayoutParamsFrameLayoutMM()
    {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    }

    public static FrameLayout.LayoutParams getLayoutParamsFrameLayoutMW()
    {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
    }

    public static FrameLayout.LayoutParams getLayoutParamsFrameLayoutWM()
    {
        return new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);
    }

    // ViewGroup
    public static ViewGroup.LayoutParams getLayoutParamsViewGroupWW()
    {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static ViewGroup.LayoutParams getLayoutParamsViewGroupMM()
    {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public static ViewGroup.LayoutParams getLayoutParamsViewGroupMW()
    {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static ViewGroup.LayoutParams getLayoutParamsViewGroupWM()
    {
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    // ------------------------layoutInflater
    public static LayoutInflater getLayoutInflater()
    {
        return LayoutInflater.from(MyApplication.getInstances());
    }

    public static View inflate(int resource, ViewGroup root)
    {
        return getLayoutInflater().inflate(resource, root);
    }

    public static View inflate(int resource, ViewGroup root, boolean attachToRoot)
    {
        return getLayoutInflater().inflate(resource, root, attachToRoot);
    }

    public static DisplayMetrics getDisplayMetrics()
    {
        return MyApplication.getInstances().getResources().getDisplayMetrics();
    }

    public static int getScreenWidth()
    {
        DisplayMetrics metrics = getDisplayMetrics();
        return metrics.widthPixels;
    }

    public static int getScreenHeight()
    {
        DisplayMetrics metrics = getDisplayMetrics();
        return metrics.heightPixels;
    }

    /**
     * @param percent 0-1
     * @return
     */
    public static int getScreenWidthPercent(float percent)
    {
        return (int) ((float) getScreenWidth() * percent);
    }

    /**
     * @param percent 0-1
     * @return
     */
    public static int getScreenHeightPercent(float percent)
    {
        return (int) ((float) getScreenHeight() * percent);
    }

    public static float getDensity()
    {
        return MyApplication.getInstances().getResources().getDisplayMetrics().density;
    }

    public static float getScaledDensity()
    {
        return MyApplication.getInstances().getResources().getDisplayMetrics().scaledDensity;
    }

    public static int sp2px(float sp)
    {
        final float fontScale = getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale + 0.5f);
    }

    public static int dp2px(float dp)
    {
        final float scale = getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static int px2dp(float px)
    {
        final float scale = getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int getScaleHeight(int originalWidth, int originalHeight, int scaleWidth)
    {
        int result = 0;
        if (originalWidth != 0)
        {
            result = originalHeight * scaleWidth / originalWidth;
        }
        return result;
    }

    public static int getScaleWidth(int originalWidth, int originalHeight, int scaleHeight)
    {
        int result = 0;
        if (originalHeight != 0)
        {
            result = originalWidth * scaleHeight / originalHeight;
        }
        return result;
    }

    /**
     * 判断当前线程是否是主线程.
     *
     * @return
     */
    public static boolean isMainThread()
    {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * 隐藏输入法
     *
     * @param view
     */
    public static void hideInputMethod(View view)
    {
        hideInputMethod(view, MyApplication.getInstances());
    }

    public static void hideInputMethod(View view, Context context)
    {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive())
        {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 显示输入法
     *
     * @param view
     */
    public static void showInputMethod(View view)
    {
        showInputMethod(view, MyApplication.getInstances(), 0);
    }

    public static void showInputMethod(View view, long delay)
    {
        showInputMethod(view, MyApplication.getInstances(), delay);
    }

    /**
     * 显示输入法
     *
     * @param view
     * @param context
     * @param delay
     */
    public static void showInputMethod(final View view, final Context context, long delay)
    {
        if (delay < 0)
        {
            delay = 0;
        }

        SDHandlerManager.getMainHandler().postDelayed(new Runnable()
        {

            @Override
            public void run()
            {
                view.setFocusable(true);
                view.setFocusableInTouchMode(true);
                view.requestFocus();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            }
        }, delay);
    }

    /**
     * 重置listview高度，解决和scrollview嵌套问题
     *
     * @param listView
     */
    public static void resetListViewHeightBasedOnChildren(ListView listView)
    {
        int totalHeight = getListViewTotalHeight(listView);
        if (totalHeight > 0)
        {
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight;
            params.height += 5;
            listView.setLayoutParams(params);
        }
    }

    public static int getListViewTotalHeight(ListView listView)
    {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
        {
            return 0;
        }
        int totalHeight = getListViewHeightRange(listView, 0, listAdapter.getCount() - 1);
        return totalHeight;
    }

    public static int getListViewHeightRange(ListView listView, int start, int end)
    {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
        {
            return 0;
        }

        int totalHeight = 0;
        if (start >= 0 && end >= start && end < listAdapter.getCount())
        {
            for (int i = start; i <= end; i++)
            {
                View listItem = listAdapter.getView(i, null, listView);
                if (listItem != null)
                {
                    listItem.measure(0, 0);
                    int height = listItem.getMeasuredHeight();
                    int dividerHeight = listView.getDividerHeight() * (listAdapter.getCount() - 1);
                    totalHeight += (height + dividerHeight);
                }
            }
        }
        return totalHeight;
    }

    public static void measureView(View v)
    {
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
    }

    public static int getViewHeight(View view)
    {
        int height = 0;
        height = view.getHeight();
        if (height <= 0)
        {
            measureView(view);
            height = view.getMeasuredHeight();
        }
        return height;
    }

    public static int getViewHeightAll(View view)
    {
        int height = getViewHeight(view);
        MarginLayoutParams params = getViewMarginLayoutParams(view);
        if (params != null)
        {
            height = height + params.topMargin + params.bottomMargin;
        }
        return height;
    }

    public static int getViewWidth(View view)
    {
        int width = 0;
        width = view.getWidth();
        if (width <= 0)
        {
            measureView(view);
            width = view.getMeasuredWidth();
        }
        return width;
    }

    public static int getViewWidthAll(View view)
    {
        int width = getViewWidth(view);
        MarginLayoutParams params = getViewMarginLayoutParams(view);
        if (params != null)
        {
            width = width + params.leftMargin + params.rightMargin;
        }
        return width;
    }

    public static void toggleEmptyViewByList(List<? extends Object> list, View emptyView)
    {
        if (emptyView != null)
        {
            if (list != null && list.size() > 0)
            {
                hide(emptyView);
            } else
            {
                show(emptyView);
            }
        }
    }

    public static void toggleViewByList(List<? extends Object> list, View view)
    {
        if (view != null)
        {
            if (list != null && list.size() > 0)
            {
                show(view);
            } else
            {
                hide(view);
            }
        }
    }

    public static View wrapperTitle(int contentLayoutId, int titleLayoutId)
    {
        LayoutInflater inflater = LayoutInflater.from(MyApplication.getInstances());
        View contentView = inflater.inflate(contentLayoutId, null);
        View titleView = inflater.inflate(titleLayoutId, null);
        return wrapperTitle(contentView, titleView);
    }

    public static View wrapperTitle(View contentView, View titleView)
    {
        LinearLayout linAll = new LinearLayout(MyApplication.getInstances());
        linAll.setOrientation(LinearLayout.VERTICAL);
        LayoutParams paramsTitle = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        LayoutParams paramsContent = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        linAll.addView(titleView, paramsTitle);
        linAll.addView(contentView, paramsContent);
        return linAll;
    }

    public static View wrapperTitle(View contentView, int titleLayoutId)
    {
        LayoutInflater inflater = LayoutInflater.from(MyApplication.getInstances());
        View titleView = inflater.inflate(titleLayoutId, null);
        return wrapperTitle(contentView, titleView);
    }

    public static boolean setViewHeight(View view, int height)
    {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null)
        {
            if (params.height != height)
            {
                params.height = height;
                view.setLayoutParams(params);
            }
            return true;
        }
        return false;
    }

    public static boolean setViewWidth(View view, int width)
    {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null)
        {
            if (params.width != width)
            {
                params.width = width;
                view.setLayoutParams(params);
            }
            return true;
        }
        return false;
    }

    public static boolean setViewWidthHeight(View view, int width, int height)
    {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params != null)
        {
            boolean needSet = false;
            if (params.width != width)
            {
                params.width = width;
                needSet = true;
            }
            if (params.height != height)
            {
                params.height = height;
                needSet = true;
            }
            if (needSet)
            {
                view.setLayoutParams(params);
            }
            return true;
        }
        return false;
    }

    public static void addViewRule(View view, int anchorId, Integer... rules)
    {
        if (view == null || rules == null)
        {
            return;
        }
        if (!(view.getLayoutParams() instanceof RelativeLayout.LayoutParams))
        {
            return;
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        if (params == null)
        {
            return;
        }

        for (Integer item : rules)
        {
            if (anchorId != 0)
            {
                params.addRule(item, anchorId);
            } else
            {
                params.addRule(item);
            }
        }
        view.setLayoutParams(params);
    }

    public static void removeViewRule(View view, Integer... rules)
    {
        if (view == null || rules == null)
        {
            return;
        }
        if (!(view.getLayoutParams() instanceof RelativeLayout.LayoutParams))
        {
            return;
        }
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        if (params == null)
        {
            return;
        }

        for (Integer item : rules)
        {
            params.removeRule(item);
        }
        view.setLayoutParams(params);
    }

    public static boolean hide(View view)
    {
        if (view == null)
        {
            return false;
        }

        if (View.GONE != view.getVisibility())
        {
            view.setVisibility(View.GONE);
        }
        return true;
    }

    public static boolean invisible(View view)
    {
        if (view == null)
        {
            return false;
        }

        if (View.INVISIBLE != view.getVisibility())
        {
            view.setVisibility(View.INVISIBLE);
        }
        return true;
    }

    public static boolean show(View view)
    {
        if (view == null)
        {
            return false;
        }

        if (View.VISIBLE != view.getVisibility())
        {
            view.setVisibility(View.VISIBLE);
        }
        return true;
    }

    public static boolean toggleGone(View view)
    {
        boolean result = false;
        if (view != null)
        {
            int visibility = view.getVisibility();
            if (visibility == View.VISIBLE)
            {
                view.setVisibility(View.GONE);
                result = false;
            } else if (visibility == View.GONE)
            {
                view.setVisibility(View.VISIBLE);
                result = true;
            }
        }
        return result;
    }

    public static boolean toggleInvisible(View view)
    {
        boolean result = false;
        if (view != null)
        {
            int visibility = view.getVisibility();
            if (visibility == View.VISIBLE)
            {
                view.setVisibility(View.INVISIBLE);
                result = false;
            } else if (visibility == View.INVISIBLE)
            {
                view.setVisibility(View.VISIBLE);
                result = true;
            }
        }
        return result;
    }

    public static int[] getLocationInWindow(View view)
    {
        int[] location = null;
        if (view != null)
        {
            location = new int[2];
            view.getLocationInWindow(location);
        }
        return location;
    }

    public static int[] getLocationOnScreen(View view)
    {
        int[] location = null;
        if (view != null)
        {
            location = new int[2];
            view.getLocationOnScreen(location);
        }
        return location;
    }

    public static int[] getLocationOnScreenWithoutStatusBar(View view)
    {
        int[] location = getLocationOnScreen(view);
        if (location != null)
        {
            int statusBarHeight = getStatusBarHeight();
            location[1] -= statusBarHeight;
        }
        return location;
    }

    public static int getStatusBarHeight()
    {
        int result = 0;
        int resourceId = MyApplication.getInstances().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = MyApplication.getInstances().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static void setTextSizeSp(TextView view, float sizeSp)
    {
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizeSp);
    }

    public static void setWebviewZoomControlVisibility(View webview, int visibility)
    {
        try
        {
            Field field = WebView.class.getDeclaredField("mZoomButtonsController");
            field.setAccessible(true);
            ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(webview);
            mZoomButtonsController.getZoomControls().setVisibility(visibility);
            field.set(webview, mZoomButtonsController);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static boolean isFirstItemTotallyVisible(AbsListView absListView)
    {
        final Adapter adapter = absListView.getAdapter();
        if (null == adapter || adapter.isEmpty())
        {
            return true;
        } else
        {
            if (absListView.getFirstVisiblePosition() <= 1)
            {
                final View firstVisibleChild = absListView.getChildAt(0);
                if (firstVisibleChild != null)
                {
                    return firstVisibleChild.getTop() >= 0;
                }
            }
        }
        return false;
    }

    public static boolean isLastItemTotallyVisible(AbsListView absListView)
    {
        final Adapter adapter = absListView.getAdapter();
        if (null == adapter || adapter.isEmpty())
        {
            return true;
        } else
        {
            final int lastItemPosition = absListView.getCount() - 1;
            final int lastVisiblePosition = absListView.getLastVisiblePosition();
            if (lastVisiblePosition >= lastItemPosition - 1)
            {
                final int childIndex = lastVisiblePosition - absListView.getFirstVisiblePosition();
                final View lastVisibleChild = absListView.getChildAt(childIndex);
                if (lastVisibleChild != null)
                {
                    return lastVisibleChild.getBottom() <= absListView.getBottom();
                }
            }
        }
        return false;
    }

    public static void removeViewFromParent(View child)
    {
        try
        {
            ViewParent viewParent = child.getParent();
            if (viewParent != null && viewParent instanceof ViewGroup)
            {
                ViewGroup parent = (ViewGroup) viewParent;
                parent.removeView(child);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static Bitmap createViewBitmap(View view)
    {
        Bitmap bmp = null;
        if (view != null)
        {
            view.setDrawingCacheEnabled(true);
            Bitmap drawingCache = view.getDrawingCache();
            if (drawingCache != null)
            {
                bmp = Bitmap.createBitmap(drawingCache);
            }
            view.destroyDrawingCache();
        }
        return bmp;
    }

    public static Rect getWindowVisibleDisplayFrame(View view)
    {
        Rect rect = new Rect();
        if (view != null)
        {
            view.getWindowVisibleDisplayFrame(rect);
        }
        return rect;
    }

    public static int getViewXOnScreen(View view)
    {
        int[] location = new int[2];
        if (view != null)
        {
            view.getLocationOnScreen(location);
        }
        return location[0];
    }

    public static int getViewYOnScreen(View view)
    {
        int[] location = new int[2];
        if (view != null)
        {
            view.getLocationOnScreen(location);
        }
        return location[1];
    }

    public static int[] getViewLocationOnScreen(View view)
    {
        int[] location = new int[2];
        if (view != null)
        {
            view.getLocationOnScreen(location);
        }
        return location;
    }

    public static int getViewCenterXOnScreen(View view)
    {
        int x = 0;
        if (view != null)
        {
            x = getViewXOnScreen(view);
            int width = getViewWidth(view);
            x = x + width / 2;
        }
        return x;
    }

    public static int getViewCenterYOnScreen(View view)
    {
        int y = 0;
        if (view != null)
        {
            y = getViewYOnScreen(view);
            int height = getViewHeight(view);
            y = y + height / 2;
        }
        return y;
    }

    public static Rect getViewRect(View view)
    {
        Rect r = new Rect();
        if (view != null && view.getVisibility() == View.VISIBLE)
        {
            int[] location = getViewLocationOnScreen(view);
            r.left = location[0];
            r.right = r.left + getViewWidth(view);
            r.top = location[1];
            r.bottom = r.top + getViewHeight(view);
        }
        return r;
    }

    public static boolean isTouchView(View view, int x, int y)
    {
        boolean result = false;
        Rect r = getViewRect(view);
        if (r != null)
        {
            result = r.contains(x, y);
        }
        return result;
    }

    public static boolean isTouchViewY(View view, int x, int y)
    {
        boolean result = false;
        Rect r = getViewRect(view);
        if (r != null)
        {
            result = r.left < r.right && r.top < r.bottom && y >= r.top && y < r.bottom;
        }
        return result;
    }

    public static boolean isTouchViewX(View view, int x, int y)
    {
        boolean result = false;
        Rect r = getViewRect(view);
        if (r != null)
        {
            result = r.left < r.right && r.top < r.bottom && x >= r.left && x < r.right;
        }
        return result;
    }

    public static boolean isTouchView(View view, MotionEvent e)
    {
        boolean result = false;
        if (e != null)
        {
            result = isTouchView(view, (int) e.getRawX(), (int) e.getRawY());
        }
        return result;
    }

    public static void setViewMarginTop(View view, int top)
    {
        MarginLayoutParams p = getViewMarginLayoutParams(view);
        if (p != null)
        {
            p.topMargin = top;
            view.setLayoutParams(p);
        }
    }

    public static void setViewMarginLeft(View view, int left)
    {
        MarginLayoutParams p = getViewMarginLayoutParams(view);
        if (p != null)
        {
            p.leftMargin = left;
            view.setLayoutParams(p);
        }
    }

    public static void setViewMarginBottom(View view, int bottom)
    {
        MarginLayoutParams p = getViewMarginLayoutParams(view);
        if (p != null)
        {
            p.bottomMargin = bottom;
            view.setLayoutParams(p);
        }
    }

    public static void setViewMarginRight(View view, int right)
    {
        MarginLayoutParams p = getViewMarginLayoutParams(view);
        if (p != null)
        {
            p.rightMargin = right;
            view.setLayoutParams(p);
        }
    }

    public static void setViewMargin(View view, int left, int top, int right, int bottom)
    {
        MarginLayoutParams p = getViewMarginLayoutParams(view);
        if (p != null)
        {
            p.topMargin = top;
            p.leftMargin = left;
            p.bottomMargin = bottom;
            p.rightMargin = right;
            view.setLayoutParams(p);
        }
    }

    public static void setViewMargins(View view, int margins)
    {
        setViewMargin(view, margins, margins, margins, margins);
    }

    public static MarginLayoutParams getViewMarginLayoutParams(View view)
    {
        MarginLayoutParams result = null;
        if (view != null)
        {
            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params != null && params instanceof MarginLayoutParams)
            {
                result = (MarginLayoutParams) params;
            }
        }
        return result;
    }

    public static void setViewPaddingLeft(View view, int left)
    {
        setViewPadding(view, left, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void setViewPaddingTop(View view, int top)
    {
        setViewPadding(view, view.getPaddingLeft(), top, view.getPaddingRight(), view.getPaddingBottom());
    }

    public static void setViewPaddingRight(View view, int right)
    {
        setViewPadding(view, view.getPaddingLeft(), view.getPaddingTop(), right, view.getPaddingBottom());
    }

    public static void setViewPaddingBottom(View view, int bottom)
    {
        setViewPadding(view, view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), bottom);
    }

    public static void setViewPadding(View view, int left, int top, int right, int bottom)
    {
        view.setPadding(left, top, right, bottom);
    }

    public static void setViewPaddings(View view, int paddings)
    {
        setViewPadding(view, paddings, paddings, paddings, paddings);
    }

    /**
     * 显示在view的顶部靠左
     *
     * @param dialog
     * @param view
     * @param marginBottom
     * @param marginLeft
     */
    public static void showDialogTopLeft(Dialog dialog, View view, int marginBottom, int marginLeft)
    {
        if (dialog != null && view != null)
        {
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            if (params != null)
            {
                params.gravity = Gravity.BOTTOM | Gravity.LEFT;

                int[] location = getLocationOnScreen(view);
                int x = location[0] + marginLeft;
                int y = ScreenUtils.getScreenHeight() - location[1] + marginBottom;

                params.x = x;
                params.y = y;
                dialog.getWindow().setAttributes(params);
                dialog.show();
            }
        }
    }

    /**
     * 显示在view的顶部居中
     *
     * @param dialog
     * @param view
     * @param marginBottom
     * @param marginLeft
     */
    public static void showDialogTopCenter(Dialog dialog, View view, int marginBottom, int marginLeft)
    {
        if (dialog != null && view != null)
        {
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            if (params != null)
            {
                params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

                int[] location = getLocationOnScreen(view);
                int x = location[0] - ScreenUtils.getScreenWidth() / 2 + view.getWidth() / 2 + marginLeft;
                int y = ScreenUtils.getScreenHeight() - location[1] + marginBottom;

                params.x = x;
                params.y = y;
                dialog.getWindow().setAttributes(params);
                dialog.show();
            }
        }
    }

    /**
     * 显示在view的顶部靠右
     *
     * @param dialog
     * @param view
     * @param marginBottom
     * @param marginRight
     */
    public static void showDialogTopRight(Dialog dialog, View view, int marginBottom, int marginRight)
    {
        if (dialog != null && view != null)
        {
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            if (params != null)
            {
                params.gravity = Gravity.BOTTOM | Gravity.RIGHT;

                int[] location = getLocationOnScreen(view);
                int x = ScreenUtils.getScreenWidth() - location[0] - view.getWidth() + marginRight;
                int y = ScreenUtils.getScreenHeight() - location[1] + marginBottom;

                params.x = x;
                params.y = y;
                dialog.getWindow().setAttributes(params);
                dialog.show();
            }
        }
    }

    /**
     * 显示在view的底部靠左
     *
     * @param dialog
     * @param view
     * @param marginTop
     * @param marginLeft
     */
    public static void showDialogBottomLeft(Dialog dialog, View view, int marginTop, int marginLeft)
    {
        if (dialog != null && view != null)
        {
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            if (params != null)
            {
                params.gravity = Gravity.TOP | Gravity.LEFT;
                int[] location = getLocationOnScreen(view);
                int x = location[0] + marginLeft;
                int y = location[1] + view.getHeight() + marginTop - getStatusBarHeight();

                params.x = x;
                params.y = y;
                dialog.getWindow().setAttributes(params);
                dialog.show();
            }
        }
    }

    /**
     * 显示在view的底部居中
     *
     * @param dialog
     * @param view
     * @param marginTop
     * @param marginLeft
     */
    public static void showDialogBottomCenter(Dialog dialog, View view, int marginTop, int marginLeft)
    {
        if (dialog != null && view != null)
        {
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            if (params != null)
            {
                params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
                int[] location = getLocationOnScreen(view);
                int x = location[0] - ScreenUtils.getScreenWidth() / 2 + view.getWidth() / 2 + marginLeft;
                int y = location[1] + view.getHeight() + marginTop - getStatusBarHeight();

                params.x = x;
                params.y = y;
                dialog.getWindow().setAttributes(params);
                dialog.show();
            }
        }
    }

    /**
     * 显示在view的底部靠右
     *
     * @param dialog
     * @param view
     * @param marginTop
     * @param marginRight
     */
    public static void showDialogBottomRight(Dialog dialog, View view, int marginTop, int marginRight)
    {
        if (dialog != null && view != null)
        {
            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            if (params != null)
            {
                params.gravity = Gravity.TOP | Gravity.RIGHT;
                int[] location = getLocationOnScreen(view);
                int x = ScreenUtils.getScreenWidth() - location[0] - view.getWidth() + marginRight;
                ;
                int y = location[1] + view.getHeight() + marginTop - getStatusBarHeight();

                params.x = x;
                params.y = y;
                dialog.getWindow().setAttributes(params);
                dialog.show();
            }
        }
    }

    public static void showPopTop(PopupWindow pop, View view, int marginBottom)
    {
        int[] location = getLocationOnScreen(view);
        int x = location[0] - ScreenUtils.getScreenWidth() / 2 + view.getWidth() / 2;
        int y = ScreenUtils.getScreenHeight() - location[1] + marginBottom;
        pop.showAtLocation(view, Gravity.BOTTOM, x, y);
    }

    public static void showPopLeft(PopupWindow pop, View view, int marginRight)
    {
        int[] location = getLocationOnScreen(view);
        pop.showAtLocation(view, Gravity.NO_GRAVITY, location[0] - pop.getWidth() - marginRight, location[1]);
    }

    public static void showPopRight(PopupWindow pop, View view, int marginLeft)
    {
        int[] location = getLocationOnScreen(view);
        pop.showAtLocation(view, Gravity.NO_GRAVITY, location[0] + view.getWidth() + marginLeft, location[1]);
    }

    public static void wrapperPopupWindow(PopupWindow pop)
    {
        if (pop != null)
        {
            ColorDrawable dw = new ColorDrawable(0x00ffffff);
            pop.setBackgroundDrawable(dw);
            pop.setWidth(FrameLayout.LayoutParams.MATCH_PARENT);
            pop.setHeight(FrameLayout.LayoutParams.WRAP_CONTENT);
            pop.setFocusable(true);
            pop.setOutsideTouchable(true);
        }
    }

    public static void updateImageViewSize(ImageView imageView, Drawable drawable)
    {
        if (drawable != null && imageView != null)
        {
            int width = getViewWidth(imageView);
            int height = getViewHeight(imageView);
            if (width > 0)
            {
                int newHeight = getScaleHeight(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), width);
                if (height != newHeight)
                {
                    setViewHeight(imageView, newHeight);
                }
            }
        }
    }

    public static void scaleViewSize(View view, int width, int height)
    {
        if (view != null)
        {
            int viewWidth = getViewWidth(view);
            if (width > 0)
            {
                int newHeight = getScaleHeight(width, height, viewWidth);
                setViewHeight(view, newHeight);
            }
        }
    }

    public static void setViewWidthWrapContent(View view)
    {
        setViewWidth(view, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static void setViewWidthMatchParent(View view)
    {
        setViewWidth(view, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public static void setViewHeightWrapContent(View view)
    {
        setViewHeight(view, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static void setViewHeightMatchParent(View view)
    {
        setViewHeight(view, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public static void setViewWidthWeightContent(View view, float weight)
    {
        if (view != null)
        {
            ViewGroup.LayoutParams vgParams = view.getLayoutParams();
            if (vgParams instanceof LayoutParams)
            {
                LayoutParams params = (LayoutParams) vgParams;
                if (params != null)
                {
                    params.width = 0;
                    params.weight = weight;
                    view.setLayoutParams(params);
                }
            }
        }
    }

    public static void setViewHeightWeightContent(View view, float weight)
    {
        if (view != null)
        {
            ViewGroup.LayoutParams vgParams = view.getLayoutParams();
            if (vgParams instanceof LayoutParams)
            {
                LayoutParams params = (LayoutParams) vgParams;
                if (params != null)
                {
                    params.height = 0;
                    params.weight = weight;
                    view.setLayoutParams(params);
                }
            }
        }
    }

    public static void startAnimationDrawable(Drawable drawable)
    {
        if (drawable instanceof AnimationDrawable)
        {
            AnimationDrawable animationDrawable = (AnimationDrawable) drawable;
            if (!animationDrawable.isRunning())
            {
                animationDrawable.start();
            }
        }
    }

    public static void stopAnimationDrawable(Drawable drawable)
    {
        stopAnimationDrawable(drawable, 0);
    }

    public static void stopAnimationDrawable(Drawable drawable, int stopIndex)
    {
        if (drawable instanceof AnimationDrawable)
        {
            AnimationDrawable animationDrawable = (AnimationDrawable) drawable;
            animationDrawable.stop();
            animationDrawable.selectDrawable(stopIndex);
        }
    }

    /**
     * 滚动listview，当deltaY大于listview的高度的时候则该次调用只能滚动listview的高度
     *
     * @param deltaY   滚动的距离。正值：item向下移动，负值：item向上移动
     * @param listView
     */
    @TargetApi(19)
    public static void scrollListBy(int deltaY, AbsListView listView)
    {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.KITKAT)
        {
            listView.scrollListBy(-deltaY);
        } else
        {
            try
            {
                Method method = AbsListView.class.getDeclaredMethod("trackMotionScroll", int.class, int.class);
                if (method != null)
                {
                    method.setAccessible(true);
                    method.invoke(listView, deltaY, deltaY);
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static int getListViewBelowSpacing(ListView listView)
    {
        int belowSpacing = 0;

        ListAdapter adapter = listView.getAdapter();
        if (adapter != null)
        {
            int totalCount = adapter.getCount();
            if (totalCount > 0)
            {
                int lastVisiblePosition = listView.getLastVisiblePosition();
                int lastItemBottom = listView.getChildAt(listView.getChildCount() - 1).getBottom();
                int spaceBelowLastItem = lastItemBottom - listView.getHeight();
                int spaceBelowLeft = getListViewHeightRange(listView, lastVisiblePosition + 1, totalCount - 1);
                belowSpacing = spaceBelowLastItem + spaceBelowLeft;
            }
        }
        return belowSpacing;
    }



    @TargetApi(19)
    public static void setTranslucentStatus(Activity activity, boolean on)
    {
        if (activity != null)
        {
            Window win = activity.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on)
            {
                winParams.flags |= bits;
            } else
            {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }

    public static void resetView(View view)
    {
        if (view != null)
        {
            view.setAlpha(1.0f);
            view.setRotation(0.0f);
            view.setRotationX(0.0f);
            view.setRotationY(0.0f);
            view.setTranslationX(0.0f);
            view.setTranslationY(0.0f);
            view.setScaleX(1.0f);
            view.setScaleY(1.0f);
        }
    }

    public static float measureText(TextView textView, String content)
    {
        float width = 0;
        if (textView != null)
        {
            TextPaint textPaint = textView.getPaint();
            width = textPaint.measureText(content);
        }
        return width;
    }

    public static void replaceOldView(View oldView, View newView)
    {
        if (oldView != null && newView != null && oldView != newView)
        {
            ViewGroup viewGroup = (ViewGroup) oldView.getParent();
            if (viewGroup != null)
            {
                int index = viewGroup.indexOfChild(oldView);
                ViewGroup.LayoutParams params = oldView.getLayoutParams();

                removeViewFromParent(oldView);
                removeViewFromParent(newView);

                viewGroup.addView(newView, index, params);
            }
        }
    }

    public static boolean replaceView(View parent, View child)
    {
        return addView(parent, child, null, true);
    }

    public static boolean replaceView(View parent, View child, ViewGroup.LayoutParams params)
    {
        return addView(parent, child, params, true);
    }

    public static boolean addView(View parent, View child)
    {
        return addView(parent, child, null, false);
    }

    public static boolean addView(View parent, View child, ViewGroup.LayoutParams params)
    {
        return addView(parent, child, params, false);
    }

    /**
     * 添加child到parent
     *
     * @param parent
     * @param child
     * @param params
     * @param removeAllViews 添加的时候是否需要先移除parent的所有子view
     * @return
     */
    private static boolean addView(View parent, View child, ViewGroup.LayoutParams params, boolean removeAllViews)
    {
        if (parent != null && child != null)
        {
            if (parent instanceof ViewGroup)
            {
                ViewGroup vg = (ViewGroup) parent;
                if (removeAllViews)
                {
                    vg.removeAllViews();
                }
                removeViewFromParent(child);
                if (params != null)
                {
                    vg.addView(child, params);
                } else
                {
                    vg.addView(child);
                }
                return true;
            } else
            {
                throw new IllegalArgumentException("parent must be instanceof ViewGroup");
            }
        }
        return false;
    }

}
