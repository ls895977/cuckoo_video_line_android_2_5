package com.ailiaovideo.videoline.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ailiaovideo.videoline.R;

import static com.ailiaovideo.videoline.widget.GradeShowLayout.GradeType.bgWidget;

/**
 * 等级显示器控件
 * Created by jiahengfei on 2018/1/19 0019.
 */

public class GradeShowLayout extends FrameLayout{
    //定义资源
    private int bg_white_gray = R.drawable.bg_gray_broder;
    private int bg_pink_pink = R.drawable.bg_pink_num;
    private int bg_maintone_maintone = R.drawable.bg_org_num;

    private int vImg_white = R.drawable.wealth_label_white;
    private int mImage_white = R.drawable.charm_label_white;

    private int widgetRes;

     //数据
    private String grade = "0";
    private int sex = 0;//1男2女

    //控件
    private LinearLayout bgLayout;
    private ImageView img;
    private TextView number;

    //渲染的属性
    private int textColor;//字体颜色
    private int imgRes;//图片
    private int bgRes;//背景色

    /**
     * 等级枚举类
     */
    public enum GradeType{
        broderGray,
        bgPink,
        bgMaintone,
        bgWidget//自定义
     }

    /**
     * 获取一个等级显示器
     * @param context 上下文
     * @param gradeType 显示样式类型
     * @param grade 等级
     * @param sex 性别
     */
    public GradeShowLayout(@NonNull Context context,GradeType gradeType,String grade,int sex) {
        super(context);
        this.grade = grade;
        this.sex = sex;
        init(context,gradeType);
        doSet();
    }

    /**
     * 获取一个自定义背景的等级显示器
     * @param context 上下文
     * @param grade 等级
     * @param widgetRes 背景资源id
     * @param sex 性别
     */
    public GradeShowLayout(@NonNull Context context,String grade,int widgetRes,int sex) {
        super(context);
        this.sex = sex;
        this.widgetRes = widgetRes;
        this.grade = grade;
        init(context,bgWidget);
        doSet();
    }

    /**
     * 智能化判断
     * @param context 上下文
     * @param grade 等级
     * @param sex 性别
     */
    public GradeShowLayout(@NonNull Context context,String grade,int sex) {
        super(context);
        this.sex = sex;
        if (grade == null){
            this.grade = "0";
        }else{
            this.grade = grade;
        }
        init(context,true);
        doSet();
    }


    /**
     * 初始化操作
     * @param context 上下文
     * @param gradeType 等级style类型
     */
    private void init(Context context,GradeType gradeType){
        init(context,false);
        //选择颜色渲染
        switch (gradeType){
            case bgPink:
                bgPink(context);
                break;
            case bgMaintone:
                bgMaintone(context);
                break;
            case broderGray:
                broderGray(context);
                break;
            case bgWidget:
                bgWidget(context);
                break;
        }

    }

    /**
     * 智能初始化
     * @param context 上下文
     */
    private void init(Context context,boolean isGoOn){
        LayoutInflater.from(context).inflate(R.layout.widget_gradeshow_layout,this);
        bgLayout = findViewById(R.id.grades_layout);
        img = findViewById(R.id.grades_img);
        number = findViewById(R.id.grades_number);
        //判断是否智能化处理
        if (isGoOn){
            goOn(context);
        }
    }

    /**
     * 智能化处理
     * @param context 上下文
     */
    private void goOn(Context context){
        imgRes = (sex == 1 ? vImg_white : mImage_white);
        textColor = context.getResources().getColor(R.color.white);
        //智能化渲染
        if ("0".equals(grade)){
            //等级为0
            bgRes = bg_white_gray;
        }else{
            bgRes = (sex == 1 ? bg_maintone_maintone : bg_pink_pink);
        }
    }

    //灰底灰边
    private void broderGray(Context context){
        imgRes = (sex == 0 ? vImg_white : mImage_white);
        bgRes = bg_white_gray;
        textColor = context.getResources().getColor(R.color.white);
    }

    //红底
    private void bgPink(Context context){
        imgRes = (sex == 0 ? vImg_white : mImage_white);
        bgRes = bg_pink_pink;
        textColor = context.getResources().getColor(R.color.white);
    }

    //黄底
    private void bgMaintone(Context context){
        bgRes = bg_maintone_maintone;
        imgRes = (sex == 0 ? vImg_white : mImage_white);
        textColor = context.getResources().getColor(R.color.white);
    }

    //自定义背景##不要为白色
    private void bgWidget(Context context){
        bgRes = widgetRes;
        imgRes = (sex == 0 ? vImg_white : mImage_white);
        textColor = context.getResources().getColor(R.color.white);
    }

    /**
     * 设置操作
     */
    private void doSet() {
        bgLayout.setBackgroundResource(bgRes);
        img.setImageResource(imgRes);
        number.setText(""+grade);
        number.setTextColor(textColor);
    }

    /**
     * 获取当前控件
     * @return bgLayout
     */
    public LinearLayout getLayout(){
        return bgLayout;
    }

}
