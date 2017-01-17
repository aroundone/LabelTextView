package com.foxmail.aroundme.library;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by gzl on 1/16/17.
 *
 * RoundRectLabelView
 */

public class RoundRectLabelView extends View {

    /**
     * 标签文字
     */
    //画笔
    private Paint mLabelTextPaint;
    //文字颜色
    private int mLabelTextColor = Color.WHITE;
    //文字大小
    private float mLabelTextSize = sp2px(8);
    //文字内容
    private String mLabelText = "Hot";
    //文字距离底部偏移量
    private float mLabelTextPaddingBottom = -dp2px(2);
    //文字和中线的间距
    private float mLabelTextPaddingCenter = 0;
    //背景颜色
    private int mLabelBgColor = Color.parseColor("#ff8800");
    //覆盖图层画笔
    private Paint mTrianglePaint;

    /**
     * 圆角矩形
     */
    //画笔
    private Paint mRoundRectPaint;
    //背景颜色
    private int mRoundRectBg = Color.WHITE;
    //边长背景颜色
    private int mRoundRectBorderBg = Color.parseColor("#b2b2b2");
    //边长宽度
    private float mRoundRectBorderWidth = dp2px(1);
    //圆角半径
    private float mRoundRectRadius = dp2px(16);

    /**
     * 内容文字
     */
    private Paint mContentPaint;
    private int mContextTextColor = Color.BLACK;
    private float mContextTextSize = sp2px(14);
    private String mContentText = "Content";
    private int mContentHeight;

    //控件总宽高
    private int width;
    private int height;

    //控件需要实现的宽高
    private double setWidth;
    private double setHeight;
    private double offsetX;

    //宽度的权重
    private float weightW = 2;
    //高度的权重
    private float weightH = 2;

    public RoundRectLabelView(Context context) {
        this(context, null);
    }

    public RoundRectLabelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundRectLabelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LabelTextView);
        //标签属性
        mLabelBgColor = typedArray.getColor(R.styleable.LabelTextView_labelBgColor, mLabelBgColor);
        mLabelTextColor = typedArray.getColor(R.styleable.LabelTextView_labelTextColor, mLabelTextColor);
        mLabelTextSize = typedArray.getDimension(R.styleable.LabelTextView_labelTextSize, mLabelTextSize);
        mLabelText = typedArray.getString(R.styleable.LabelTextView_labelText);
        mLabelTextPaddingBottom = typedArray.getDimension(R.styleable.LabelTextView_labelTextPaddingBottom, mLabelTextPaddingBottom);
        mLabelTextPaddingCenter = typedArray.getDimension(R.styleable.LabelTextView_labelTextPaddingCenter, mLabelTextPaddingCenter);
        weightW = typedArray.getFloat(R.styleable.LabelTextView_labelWidthWeight, weightW);
        weightH = typedArray.getFloat(R.styleable.LabelTextView_labelHeightWeight, weightH);

        //圆角矩形属性
        mRoundRectBg = typedArray.getColor(R.styleable.LabelTextView_roundRectBg, mRoundRectBg);
        mRoundRectBorderBg = typedArray.getColor(R.styleable.LabelTextView_roundRectBorderBg, mRoundRectBorderBg);
        mRoundRectRadius = typedArray.getDimension(R.styleable.LabelTextView_roundRectRadius, mRoundRectRadius);
        mRoundRectBorderWidth = typedArray.getDimension(R.styleable.LabelTextView_roundRectBorderWidth, mRoundRectBorderWidth);

        //内容属性
        mContentText = typedArray.getString(R.styleable.LabelTextView_contentText);
        mContextTextColor = typedArray.getColor(R.styleable.LabelTextView_contentTextColor, mContextTextColor);
        mContextTextSize = typedArray.getDimension(R.styleable.LabelTextView_contentTextSize, mContextTextSize);

        typedArray.recycle();

        if(weightW <= 0 || weightH <= 0) {
            throw new RuntimeException("labelWidthWeight or labelHeightWeight must be greater than 0");
        }

        if(mContentText == null) {
            throw new RuntimeException("please set a value with contentText in XML");
        }

        initLabelTextPaint();
        initTrianglePaint();
        initRoundRectPaint();
        initContentPaint();
        resetTextStatus();
    }

    private void initLabelTextPaint() {
        //初始化绘制标签文本的画笔
        mLabelTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLabelTextPaint.setColor(mLabelTextColor);
        mLabelTextPaint.setTextAlign(Paint.Align.CENTER);
        mLabelTextPaint.setTextSize(mLabelTextSize);
        mLabelTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mLabelTextPaint.setAntiAlias(true);

    }

    private void initTrianglePaint(){
        //初始化绘制三角形背景的画笔
        mTrianglePaint= new Paint(Paint.ANTI_ALIAS_FLAG);
        mTrianglePaint.setColor(mLabelBgColor);
    }

    private void initRoundRectPaint() {
        //绘制圆角矩形画笔
        mRoundRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRoundRectPaint.setColor(mRoundRectBorderBg);
        mRoundRectPaint.setStyle(Paint.Style.STROKE);
        mRoundRectPaint.setStrokeWidth(mRoundRectBorderWidth);
    }

    private void initContentPaint() {
        mContentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mContentPaint.setColor(mContextTextColor);
        mContentPaint.setTextAlign(Paint.Align.CENTER);
        mContentPaint.setTextSize(mContextTextSize);
    }

    private void resetTextStatus() {
        // 测量文字高度
        Rect rectLabelText = new Rect();
        mLabelTextPaint.getTextBounds(mLabelText, 0, mLabelText.length(), rectLabelText);
        Rect rectContentText = new Rect();
        mContentPaint.getTextBounds(mContentText, 0, mContentText.length(), rectContentText);
        mContentHeight = rectContentText.height();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        setWidth = width / weightW;
        setHeight = height / weightH;
        offsetX = calculationOffset(setWidth, setHeight);
    }

    @TargetApi(19)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制三角形背景
        Path pathTriangle = new Path();
        pathTriangle.moveTo(0, double2Float(setHeight));
        pathTriangle.lineTo(0, 0);
        pathTriangle.lineTo(double2Float(setWidth), 0);
        pathTriangle.close();

        Path pathRountRect = new Path();
        //绘制圆角矩形
        //圆角矩形向四周空出边长宽度距离，不然显示会不好看
        RectF r = new RectF();
        r.left = dp2px(mRoundRectBorderWidth);
        r.top = dp2px(mRoundRectBorderWidth);
        r.right = width - dp2px(mRoundRectBorderWidth);
        r.bottom = height - dp2px(mRoundRectBorderWidth);
        pathRountRect.addRoundRect(r, mRoundRectRadius, mRoundRectRadius, Path.Direction.CW);
        pathTriangle.op(pathRountRect,  Path.Op.INTERSECT);
        canvas.drawPath(pathTriangle, mTrianglePaint);
        canvas.drawPath(pathRountRect, mRoundRectPaint);

        //画Label文字
        Path pathLabelLine = new Path();
        pathLabelLine.moveTo(0, double2Float(setHeight));
        pathLabelLine.lineTo(double2Float(setWidth), 0);
        //如果等于0就使用内部计算的值，否则就用设置的值
        if(mLabelTextPaddingCenter == 0) {
            canvas.drawTextOnPath(mLabelText, pathLabelLine, -double2Float(offsetX), mLabelTextPaddingBottom, mLabelTextPaint);
        } else {
            canvas.drawTextOnPath(mLabelText, pathLabelLine, mLabelTextPaddingCenter, mLabelTextPaddingBottom, mLabelTextPaint);
        }

        //画Content文字,往下移1/2高度居中
        canvas.drawText(mContentText, width / 2, height / 2 + mContentHeight / 2, mContentPaint);
    }

    /**
     *
     *
     * 公式为：if(w > h) {
     *     (w^2 - h^2)/2 * √(w^2 + h^2)
     * } else if(w < h) {
     *     (h^2 - w^2)/2 * √(w^2 + h^2)
     * }
     * ps: w = width   h = height
     *
     *
     *
     * 计算LabelText相对偏移量
     * 相等直接返回0
     * 默认width > height，如果height > width的话内部交换
     * @param width width
     * @param height height
     * @return 计算后的值
     */
    private float calculationOffset(double width, double height) {
        if (width == height) {
            return 0;
        }
        if (width < height) {
            double temp = width;
            width = height;
            height = temp;
        }
        double molecule = Math.pow(width, 2) - Math.pow(height, 2);

        double denominator = 2 * Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));

        return double2Float(molecule / denominator);
    }

    private float double2Float(double dou) {
        return Float.parseFloat(String.valueOf(dou));
    }

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private float sp2px(float spValue) {
        final float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return spValue * scale;
    }

    public int getmLabelTextColor() {
        return mLabelTextColor;
    }

    public void setmLabelTextColor(int mLabelTextColor) {
        this.mLabelTextColor = mLabelTextColor;
    }

    public float getmLabelTextSize() {
        return mLabelTextSize;
    }

    public void setmLabelTextSize(float mLabelTextSize) {
        this.mLabelTextSize = mLabelTextSize;
    }

    public String getmLabelText() {
        return mLabelText;
    }

    public void setmLabelText(String mLabelText) {
        this.mLabelText = mLabelText;
    }

    public int getmLabelBgColor() {
        return mLabelBgColor;
    }

    public void setmLabelBgColor(int mLabelBgColor) {
        this.mLabelBgColor = mLabelBgColor;
    }

    public int getmRoundRectBg() {
        return mRoundRectBg;
    }

    public void setmRoundRectBg(int mRoundRectBg) {
        this.mRoundRectBg = mRoundRectBg;
    }

    public int getmRoundRectBorderBg() {
        return mRoundRectBorderBg;
    }

    public void setmRoundRectBorderBg(int mRoundRectBorderBg) {
        this.mRoundRectBorderBg = mRoundRectBorderBg;
    }

    public float getmRoundRectBorderWidth() {
        return mRoundRectBorderWidth;
    }

    public void setmRoundRectBorderWidth(float mRoundRectBorderWidth) {
        this.mRoundRectBorderWidth = mRoundRectBorderWidth;
    }

    public float getmRoundRectRadius() {
        return mRoundRectRadius;
    }

    public void setmRoundRectRadius(float mRoundRectRadius) {
        this.mRoundRectRadius = mRoundRectRadius;
    }

    public int getmContextTextColor() {
        return mContextTextColor;
    }

    public void setmContextTextColor(int mContextTextColor) {
        this.mContextTextColor = mContextTextColor;
    }

    public float getmContextTextSize() {
        return mContextTextSize;
    }

    public void setmContextTextSize(float mContextTextSize) {
        this.mContextTextSize = mContextTextSize;
    }

    public String getmContentText() {
        return mContentText;
    }

    public void setmContentText(String mContentText) {
        this.mContentText = mContentText;
    }

    public int getmContentHeight() {
        return mContentHeight;
    }

    public void setmContentHeight(int mContentHeight) {
        this.mContentHeight = mContentHeight;
    }
}
