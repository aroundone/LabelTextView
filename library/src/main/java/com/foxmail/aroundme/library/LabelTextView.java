package com.foxmail.aroundme.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;


/**
 * Created by gzl on 1/17/17.
 *
 */

public class LabelTextView extends TextView {

    /**
     * 默认不显示，只有使用了setXXX的方法才显示
     */
    private boolean isShow = false;

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
    private String mLabelText = "Default";
    //文字距离底部偏移量
    private float mLabelTextPaddingBottom = -dp2px( 2);
    //文字和中线的间距
    private float mLabelTextPaddingCenter = Float.MAX_VALUE;
    //背景颜色
    private int mLabelBgColor = Color.BLUE;
    //覆盖图层画笔
    private Paint mTrianglePaint;
    private Path mPathTriangle;

    /**
     * 圆角矩形
     */
    //画笔
    private Paint mRoundRectPaint;
    //边长背景颜色
    private int mRoundRectBorderBg = Color.parseColor("#00000000");
    //边长宽度
    private float mRoundRectBorderWidth = dp2px(1);
    //圆角半径
    private float mRoundRectRadius = dp2px(16);
    //圆角矩形Path
    private RectF mRectFRoundRect;
    private Path mPathRoundRect;

    private Path mPathLabelLine;


    //控件需要实现的宽高
    private double setWidth;
    private double setHeight;
    private double offsetX;

    //宽度的权重
    private float weightW = 2;
    //高度的权重
    private float weightH = 2;
    //DPI
    private float scale = getContext().getResources().getDisplayMetrics().density;
    //抗锯齿
    private PaintFlagsDrawFilter paintFlagsDrawFilter;

    public LabelTextView(Context context) {
        this(context, null);
    }

    public LabelTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
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

        mRoundRectRadius = typedArray.getDimension(R.styleable.LabelTextView_roundRectRadius, mRoundRectRadius);
        mRoundRectBorderWidth = typedArray.getDimension(R.styleable.LabelTextView_roundRectBorderWidth, mRoundRectBorderWidth);
        typedArray.recycle();

        if (weightW <= 0 || weightH <= 0) {
            throw new RuntimeException("labelWidthWeight or labelHeightWeight must be greater than 0");
        }

        initLabelTextPaint();
        initTrianglePaint();
        initRoundRectPaint();
    }


    private void initLabelTextPaint() {
        //初始化绘制标签文本的画笔
        mLabelTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mLabelTextPaint.setTextAlign(Paint.Align.CENTER);
        mLabelTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mPathLabelLine = new Path();
    }

    private void initTrianglePaint() {
        //初始化绘制三角形背景的画笔
        mTrianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPathTriangle = new Path();
    }

    private void initRoundRectPaint() {
        //绘制圆角矩形画笔
        mRoundRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRoundRectPaint.setColor(mRoundRectBorderBg);
        mRoundRectPaint.setStyle(Paint.Style.STROKE);
        mRoundRectPaint.setStrokeWidth(mRoundRectBorderWidth);
        mPathRoundRect = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        setWidth = w / weightW;
        setHeight = h / weightH;
        offsetX = calculationOffset(setWidth, setHeight);

        mRectFRoundRect = new RectF();
        mRectFRoundRect.left = dp2px(mRoundRectBorderWidth) / scale;
        mRectFRoundRect.top = dp2px(mRoundRectBorderWidth) / scale;
        mRectFRoundRect.right = w - dp2px(mRoundRectBorderWidth) / scale;
        mRectFRoundRect.bottom = h - dp2px(mRoundRectBorderWidth) / scale;

        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //不展示直接返回
        if (!isShow) {
            return;
        }

        canvas.setDrawFilter(paintFlagsDrawFilter);

        mTrianglePaint.setColor(mLabelBgColor);
        mLabelTextPaint.setColor(mLabelTextColor);
        mLabelTextPaint.setTextSize(mLabelTextSize);


        mPathTriangle.moveTo(0, (float) setHeight);
        mPathTriangle.lineTo(0, 0);
        mPathTriangle.lineTo((float) setWidth, 0);
        mPathTriangle.close();

        //绘制圆角矩形
        //圆角矩形向四周空出边长宽度距离，不然显示会不好看
        mPathRoundRect.addRoundRect(mRectFRoundRect, mRoundRectRadius, mRoundRectRadius, Path.Direction.CW);

        /*
         * 区分：当API>19时采取Path.op方式
         *      当API<19采取切割canvas方式
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mPathTriangle.op(mPathRoundRect, Path.Op.INTERSECT);
            canvas.drawPath(mPathRoundRect, mRoundRectPaint);
            canvas.drawPath(mPathTriangle, mTrianglePaint);
        } else {
            canvas.save();
            canvas.clipPath(mPathRoundRect, Region.Op.INTERSECT);
            canvas.drawPath(mPathTriangle, mTrianglePaint);
            canvas.drawPath(mPathRoundRect, mRoundRectPaint);
            canvas.restore();
        }

        //画Label文字

        mPathLabelLine.moveTo(0, (float) setHeight);
        mPathLabelLine.lineTo((float) setWidth, 0);
        //如果等于0就使用内部计算的值，否则就用设置的值
        if (mLabelTextPaddingCenter == Float.MAX_VALUE) {
            canvas.drawTextOnPath(mLabelText, mPathLabelLine, (float) -offsetX, mLabelTextPaddingBottom, mLabelTextPaint);
        } else {
            canvas.drawTextOnPath(mLabelText, mPathLabelLine, mLabelTextPaddingCenter, mLabelTextPaddingBottom, mLabelTextPaint);
        }

    }

    /**
     * ps: w = width   h = height
     * <p>
     * 公式为：if(w > h) {
     * (w^2 - h^2)/[2 * √(w^2 + h^2)]
     * } else if(w < h) {
     * (h^2 - w^2)/[2 * √(w^2 + h^2)]
     * }
     * <p>
     * <p>
     * 计算LabelText相对偏移量
     * 相等直接返回0
     *
     * @param width  width
     * @param height height
     * @return 计算后的值
     */
    private float calculationOffset(double width, double height) {
        if (width == height) {
            return 0;
        }

        double molecule = Math.pow(width, 2) - Math.pow(height, 2);
        double denominator = 2 * Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
        if (denominator <= 0) {
            return 0;
        }

        return (float) (molecule / denominator);
    }

    private float double2Float(double dou) {
        return Float.parseFloat(String.valueOf(dou));
    }

    private int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        this.scale = scale;
        return (int) (dpValue * scale + 0.5f);
    }

    private float sp2px(float spValue) {
        final float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return spValue * scale;
    }

    public LabelTextView setLabelText(String mLabelText) {
        this.mLabelText = mLabelText;
        return this;
    }

    public LabelTextView setLabelTextColor(int mLabelTextColor) {
        this.mLabelTextColor = mLabelTextColor;
        return this;
    }

    public LabelTextView setLabelTextSize(float mLabelTextSize) {
        this.mLabelTextSize = mLabelTextSize;
        return this;
    }

    public LabelTextView setLabelBgColor(int mLabelBgColor) {
        this.mLabelBgColor = mLabelBgColor;
        return this;
    }

    //渲染
    public void update() {
        isShow = true;
        invalidate();
    }
}
