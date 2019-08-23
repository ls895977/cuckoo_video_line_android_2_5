package com.ailiaovideo.videoline.drawable;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;

public class BGDrawable extends LayerDrawable
{

    private static final int DEFAULT_COLOR = Color.parseColor("#ffffff");

    private GradientDrawable drawableFirst;
    private GradientDrawable drawableSecond;

    private int strokeWidthLeft;
    private int strokeWidthTop;
    private int strokeWidthRight;
    private int strokeWidthBottom;

    private float cornerTopLeft;
    private float cornerTopRight;
    private float cornerBottomLeft;
    private float cornerBottomRight;

    public BGDrawable()
    {
        this(new GradientDrawable[]{new GradientDrawable(), new GradientDrawable()});
    }

    public BGDrawable(Drawable[] layers)
    {
        super(layers);
        init();
    }

    private void init()
    {
        drawableFirst = (GradientDrawable) this.getDrawable(0);
        drawableSecond = (GradientDrawable) this.getDrawable(1);

        drawableFirst.setShape(GradientDrawable.RECTANGLE);
        drawableSecond.setShape(GradientDrawable.RECTANGLE);
        color(DEFAULT_COLOR);
    }

    /**
     * 透明度
     *
     * @param alpha
     * @return
     */
    public BGDrawable alpha(int alpha)
    {
        setAlpha(alpha);
        return this;
    }

    /**
     * 图片颜色
     *
     * @param color
     * @return
     */
    public BGDrawable color(int color)
    {
        drawableSecond.setColor(color);
        return this;
    }

    /**
     * 边框颜色
     *
     * @param color
     * @return
     */
    public BGDrawable strokeColor(int color)
    {
        drawableFirst.setColor(color);
        return this;
    }

    /**
     * 设置圆角
     *
     * @param topLeft
     * @param topRight
     * @param bottomLeft
     * @param bottomRight
     * @return
     */
    public BGDrawable corner(float topLeft, float topRight, float bottomLeft, float bottomRight)
    {
        cornerTopLeft = topLeft;
        cornerTopRight = topRight;
        cornerBottomLeft = bottomLeft;
        cornerBottomRight = bottomRight;

        drawableFirst.setCornerRadii(new float[]{cornerTopLeft, cornerTopLeft, cornerTopRight, cornerTopRight, cornerBottomRight,
                cornerBottomRight, cornerBottomLeft, cornerBottomLeft});
        drawableSecond.setCornerRadii(new float[]{cornerTopLeft, cornerTopLeft, cornerTopRight, cornerTopRight, cornerBottomRight,
                cornerBottomRight, cornerBottomLeft, cornerBottomLeft});
        return this;
    }

    public BGDrawable cornerAll(float radius)
    {
        corner(radius, radius, radius, radius);
        return this;
    }

    public BGDrawable cornerTopLeft(float radius)
    {
        corner(radius, cornerTopRight, cornerBottomLeft, cornerBottomRight);
        return this;
    }

    public BGDrawable cornerTopRight(float radius)
    {
        corner(cornerTopLeft, radius, cornerBottomLeft, cornerBottomRight);
        return this;
    }

    public BGDrawable cornerBottomLeft(float radius)
    {
        corner(cornerTopLeft, cornerTopRight, radius, cornerBottomRight);
        return this;
    }

    public BGDrawable cornerBottomRight(float radius)
    {
        corner(cornerTopLeft, cornerTopRight, cornerBottomLeft, radius);
        return this;
    }

    /**
     * 边框宽度
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return
     */
    public BGDrawable strokeWidth(int left, int top, int right, int bottom)
    {
        strokeWidthLeft = left;
        strokeWidthTop = top;
        strokeWidthRight = right;
        strokeWidthBottom = bottom;
        setLayerInset(1, strokeWidthLeft, strokeWidthTop, strokeWidthRight, strokeWidthBottom);
        return this;
    }

    public BGDrawable strokeWidthAll(int width)
    {
        strokeWidth(width, width, width, width);
        return this;
    }

    public BGDrawable strokeWidthLeft(int width)
    {
        strokeWidth(width, strokeWidthTop, strokeWidthRight, strokeWidthBottom);
        return this;
    }

    public BGDrawable strokeWidthTop(int width)
    {
        strokeWidth(strokeWidthLeft, width, strokeWidthRight, strokeWidthBottom);
        return this;
    }

    public BGDrawable strokeWidthRight(int width)
    {
        strokeWidth(strokeWidthLeft, strokeWidthTop, width, strokeWidthBottom);
        return this;
    }

    public BGDrawable strokeWidthBottom(int width)
    {
        strokeWidth(strokeWidthLeft, strokeWidthTop, strokeWidthRight, width);
        return this;
    }

    // -------------------------------StateListDrawable

    /**
     * 获得可以根据状态变化的drawable
     *
     * @param normal     正常drawable
     * @param focus    获得焦点drawable
     * @param selected 选中drawable
     * @param pressed  按下drawable
     * @return
     */
    public static StateListDrawable getStateListDrawable(Drawable normal, Drawable focus, Drawable selected, Drawable pressed)
    {
        StateListDrawable stateListDrawable = new StateListDrawable();
        if (normal != null)
        {
            stateListDrawable.addState(getStateNormal(), normal);
        }
        if (focus != null)
        {
            stateListDrawable.addState(getStateFocus(), focus);
        }
        if (selected != null)
        {
            stateListDrawable.addState(getStateSelected(), selected);
        }
        if (pressed != null)
        {
            stateListDrawable.addState(getStatePressed(), pressed);
        }
        return stateListDrawable;
    }

    public static ColorStateList getStateListColor(int normal, int pressed)
    {
        return getStateListColor(normal, 0, 0, pressed);
    }

    /**
     * 获得可以根据状态变化的ColorStateList
     *
     * @param normal     正常颜色
     * @param focus    获得焦点颜色
     * @param selected 选中颜色
     * @param pressed  按下颜色
     * @return
     */
    public static ColorStateList getStateListColor(int normal, int focus, int selected, int pressed)
    {
        int[][] states = new int[4][];
        states[0] = getStateNormal();
        states[1] = getStateFocus();
        states[2] = getStateSelected();
        states[3] = getStatePressed();

        int[] colors = new int[4];

        int defaultColor = Color.BLACK;
        if (normal == 0)
        {
            normal = defaultColor;
        } else
        {
            defaultColor = normal;
        }

        if (focus == 0)
        {
            focus = defaultColor;
        }

        if (selected == 0)
        {
            selected = defaultColor;
        }

        if (pressed == 0)
        {
            pressed = defaultColor;
        }

        colors[0] = normal;
        colors[1] = focus;
        colors[2] = selected;
        colors[3] = pressed;

        ColorStateList colorStateList = new ColorStateList(states, colors);

        return colorStateList;
    }

    // -------------------------------States
    public static int[] getStateNormal()
    {
        return new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected, -android.R.attr.state_pressed};
    }

    public static int[] getStateFocus()
    {
        return new int[]{android.R.attr.state_focused, -android.R.attr.state_selected, -android.R.attr.state_pressed};
    }

    public static int[] getStateSelected()
    {
        return new int[]{-android.R.attr.state_focused, android.R.attr.state_selected, -android.R.attr.state_pressed};
    }

    public static int[] getStatePressed()
    {
        return new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected, android.R.attr.state_pressed};
    }

}
