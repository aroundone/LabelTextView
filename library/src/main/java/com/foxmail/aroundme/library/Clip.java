package com.foxmail.aroundme.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by gzl on 1/18/17.
 */

public class Clip extends View{

    //覆盖图层画笔
    private Paint mTrianglePaint;
    private Paint mRoundRectPaint;
    //背景颜色
    private int mLabelBgColor = Color.parseColor("#ff8800");
    //背景颜色
    private int mRoundRectBg = Color.WHITE;
    //边长背景颜色
    private int mRoundRectBorderBg = Color.parseColor("#000000");

    Paint paint;
    public Clip(Context context) {
        this(context, null);
    }

    public Clip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Clip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        initTrianglePaint();
        initRoundRectPaint();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(6);
        mPaint.setTextSize(16);
        mPaint.setTextAlign(Paint.Align.RIGHT);

        mPath = new Path();
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
        mRoundRectPaint.setStrokeWidth(1);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Path pathTriangle = new Path();
        pathTriangle.moveTo(0, 50);
        pathTriangle.lineTo(0, 0);
        pathTriangle.lineTo(100, 0);
        pathTriangle.close();



        Path pathRountRect = new Path();
        //绘制圆角矩形
        //圆角矩形向四周空出边长宽度距离，不然显示会不好看
        RectF r = new RectF();
        r.left = 0;
        r.top = 0;
        r.right = 300;
        r.bottom = 100;
        pathRountRect.addRoundRect(r, 40, 40, Path.Direction.CW);

        canvas.save();

        canvas.drawPath(pathTriangle, mTrianglePaint);

        canvas.clipPath(pathRountRect, Region.Op.INTERSECT);
        canvas.drawPath(pathRountRect, mRoundRectPaint);
        canvas.restore();


        canvas.drawColor(Color.GRAY);
        canvas.save();
        canvas.translate(10, 10);
        drawScene(canvas);
        canvas.restore();

        canvas.save();
        canvas.translate(160, 10);
        canvas.clipRect(10, 10, 90, 90);
        canvas.clipRect(30, 30, 70, 70, Region.Op.DIFFERENCE);
        drawScene(canvas);
        canvas.restore();

        canvas.save();
        canvas.translate(10, 160);
        mPath.reset();
        canvas.clipPath(mPath); // makes the clip empty
        mPath.addCircle(50, 50, 50, Path.Direction.CCW);
        canvas.clipPath(mPath, Region.Op.REPLACE);
        drawScene(canvas);
        canvas.restore();

        canvas.save();
        canvas.translate(160, 160);
        canvas.clipRect(0, 0, 60, 60);
        canvas.clipRect(40, 40, 100, 100, Region.Op.UNION);
        drawScene(canvas);
        canvas.restore();

        canvas.save();
        canvas.translate(10, 310);
        canvas.clipRect(0, 0, 60, 60);
        canvas.clipRect(40, 40, 100, 100, Region.Op.XOR);
        drawScene(canvas);
        canvas.restore();

        canvas.save();
        canvas.translate(160, 310);
        canvas.clipRect(0, 0, 60, 60);
        canvas.clipRect(40, 40, 100, 100, Region.Op.REVERSE_DIFFERENCE);
        drawScene(canvas);
        canvas.restore();
    }

    private void drawScene(Canvas canvas) {
        canvas.clipRect(0, 0, 100, 100);

        canvas.drawColor(Color.WHITE);

        mPaint.setColor(Color.RED);
        canvas.drawLine(0, 0, 100, 100, mPaint);

        mPaint.setColor(Color.GREEN);
        canvas.drawCircle(30, 70, 30, mPaint);

        mPaint.setColor(Color.BLUE);
        canvas.drawText("Clipping", 100, 30, mPaint);
    }

    private Paint mPaint;
    private Path mPath;


}
