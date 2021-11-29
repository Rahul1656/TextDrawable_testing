package wu.seal.textwithimagedrawable;

import ohos.agp.components.element.Element;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.render.Canvas;
import ohos.agp.render.ColorFilter;
import ohos.agp.render.Paint;
import ohos.agp.utils.Color;
import ohos.agp.utils.TextAlignment;
import ohos.app.Context;
import ohos.media.image.PixelMap;
import com.hmos.compat.utils.ResourceUtils;

/**
 * TextWithImageDrawable.
 */
public class TextWithImageDrawable extends Element {

    /**
     * Position of image relative to text.
     */
    public enum Position {

        LEFT, TOP, RIGHT, BOTTOM
    }

    /**
     * Ellipsize Mode.
     */
    public enum EllipsizeModel {

        PRE, MID, END
    }

    private static final String SUFFIX = "…";
    private Context mContext;
    private Element mDrawable;
    private String mText;
    private String originText;
    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int paddingBottom;
    private int drawablePadding;
    private Position position = Position.LEFT;
    private Paint mTextPaint;
    private float mTextSize = 50;
    private int mTextHeight;
    private int textTopDelBaseLine;
    private int maxTextLength = Integer.MAX_VALUE;

    /**
     * Ellipsize type.
     */
    private EllipsizeModel ellipsizeModel = EllipsizeModel.END;

    public void createNativePtr(Object source) {
        // need not implement this
    }

    /**
     * Constructor.
     *
     * @param context context
     */
    public TextWithImageDrawable(Context context) {
        mContext = context;
        mTextPaint = new Paint();
        mTextPaint.setTextAlign(TextAlignment.LEFT);
        mTextPaint.setTextSize((int) mTextSize);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = Math.round(fontMetrics.descent - fontMetrics.ascent);
        textTopDelBaseLine = Math.round(-fontMetrics.ascent - fontMetrics.leading);
    }

    /**
     * Method to set the text.
     *
     * @param text text item
     */
    public void setText(String text) {
        originText = text;
        if (maxTextLength != Integer.MAX_VALUE && maxTextLength < text.length()) {
            if (ellipsizeModel == EllipsizeModel.END) {
                this.mText = text.substring(0, maxTextLength) + SUFFIX;
            } else if (ellipsizeModel == EllipsizeModel.PRE) {
                this.mText = SUFFIX + text.substring(text.length() - 3, text.length());
            } else if (ellipsizeModel == EllipsizeModel.MID) {
                int firstStringLength = maxTextLength / 2;
                this.mText = text.substring(0, firstStringLength) + SUFFIX
                        + text.substring((text.length() - (maxTextLength - firstStringLength)), text.length());
            }
        } else {
            this.mText = text;
        }
    }

    /**
     * Method to set image resource.
     *
     * @param imageRes image
     */
    public void setImageRes(int imageRes) {
        Element drawable = ResourceUtils.getDrawable(mContext, imageRes);
        setDrawable(drawable);
    }

    public void setImageBitmap(PixelMap bitmap) {
        setDrawable(new PixelMapElement(bitmap));
    }

    private void setDrawable(Element drawable) {
        drawable.setBounds(0, 0, drawable.getWidth(), drawable.getHeight());
        this.mDrawable = drawable;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public void setImagePadding(int drawablePadding) {
        this.drawablePadding = drawablePadding;
    }

    /**
     * Method to set the ellipsize type.
     *
     * @param ellipsizeModel type PRE, MID, END
     */
    public void setEllipsizeModel(EllipsizeModel ellipsizeModel) {
        this.ellipsizeModel = ellipsizeModel;
        if (originText != null) {
            setText(originText);
        }
    }

    @Override
    public void drawToCanvas(ohos.agp.render.Canvas canvas) {

        canvas.clipRect(paddingLeft, paddingTop, canvas.getLocalClipBounds().getWidth() - paddingRight,
                canvas.getLocalClipBounds().getHeight() - paddingBottom);
        int contentHeight = getHeight() - paddingTop - paddingBottom;
        int contentWidth = getWidth() - paddingLeft - paddingRight;
        if (position == Position.LEFT) {
            handleLeftImage(contentHeight, canvas);
        } else if (position == Position.RIGHT) {
            handleRightImage(contentHeight, canvas);
        } else if (position == Position.TOP) {
            handleTopImage(contentWidth, canvas);
        } else if (position == Position.BOTTOM) {
            handleBottomImage(contentWidth, canvas);
        }
    }

    private void handleBottomImage(int contentWidth, Canvas canvas) {
        if (isNotEmpty(mText)) {
            final int textLeft = Math.round(paddingLeft + (contentWidth - mTextPaint.measureText(mText)) / 2);
            final int textTop = paddingTop + textTopDelBaseLine;
            canvas.drawText(mTextPaint, mText, textLeft, textTop);
            if (mDrawable != null) {
                final int bitmapLeft = Math.round(paddingLeft + ((contentWidth - mDrawable.getWidth()) / 2));
                final int bitmapTop = paddingTop + mTextHeight + drawablePadding;
                canvas.translate(bitmapLeft, bitmapTop);
                mDrawable.drawToCanvas(canvas);
            }
        } else {
            if (mDrawable != null) {
                final int bitmapLeft = Math.round(paddingLeft + ((contentWidth - mDrawable.getWidth()) / 2));
                final int bitmapTop = paddingTop + mTextHeight + drawablePadding;
                canvas.translate(bitmapLeft, bitmapTop);
                mDrawable.drawToCanvas(canvas);
            }
        }
    }

    private void handleTopImage(int contentWidth, Canvas canvas) {
        if (mDrawable != null) {
            final int bitmapLeft = (contentWidth - mDrawable.getWidth()) / 2 + paddingLeft;
            final int bitmapTop = paddingTop;
            canvas.translate(bitmapLeft, bitmapTop);
            mDrawable.drawToCanvas(canvas);
            if (isNotEmpty(mText)) {
                final int left = Math.round(contentWidth - mTextPaint.measureText(mText)) / 2 + paddingLeft;
                final int top = paddingTop + mDrawable.getHeight() + drawablePadding + textTopDelBaseLine;
                canvas.drawText(mTextPaint, mText, left, top);
            }
        } else {
            if ((isNotEmpty(mText))) {
                final int left = Math.round(contentWidth - mTextPaint.measureText(mText)) / 2 + paddingLeft;
                final int top = paddingTop + drawablePadding + textTopDelBaseLine;
                canvas.drawText(mTextPaint, mText, left, top);
            }
        }
    }

    private void handleRightImage(int contentHeight, Canvas canvas) {
        if (isNotEmpty(mText)) {
            final int textLeft = this.paddingLeft;
            final int textTop = (contentHeight - mTextHeight) / 2 + textTopDelBaseLine + paddingTop;
            canvas.drawText(mTextPaint, mText, textLeft, textTop);
        }
        if (mDrawable != null) {
            int textWidth = Math.round(mTextPaint.measureText(mText));
            final int left = paddingLeft + textWidth + drawablePadding;
            final int top = (contentHeight - mDrawable.getHeight()) / 2 + paddingTop;
            canvas.translate(left, top);
            mDrawable.drawToCanvas(canvas);
        }
    }

    private void handleLeftImage(int contentHeight, Canvas canvas) {
        if (mDrawable != null) {
            final int bitmapTop = (contentHeight - mDrawable.getHeight()) / 2 + paddingTop;
            final int bitmapLeft = this.paddingLeft;
            canvas.translate(bitmapLeft, bitmapTop);
            mDrawable.drawToCanvas(canvas);
            if (isNotEmpty(mText)) {
                final int left = this.paddingLeft + mDrawable.getWidth() + drawablePadding;
                final int top = (contentHeight - mTextHeight) / 2 + textTopDelBaseLine + paddingTop;
                canvas.drawText(mTextPaint, mText, left, top);
            }
        } else {
            if (isNotEmpty(mText)) {
                final int left = this.paddingLeft + drawablePadding;
                final int top = (contentHeight - mTextHeight) / 2 + textTopDelBaseLine + paddingTop;
                canvas.drawText(mTextPaint, mText, left, top);
            }
        }
    }

    /**
     * Method to set max text length.
     *
     * @param maxTextLength maximum text size in characters
     */
    public void setMaxTextLength(int maxTextLength) {
        this.maxTextLength = maxTextLength;
        if (originText != null) {
            setText(originText);
        }
    }

    /**
     * Method to get the height.
     *
     * @return height of this element
     */
    @Override
    public int getHeight() {
        int drawableHeight = 0;
        if (mDrawable != null) {
            drawableHeight = mDrawable.getHeight();
        }
        if (position == Position.LEFT || position == Position.RIGHT) {
            return Math.max(drawableHeight, mTextHeight) + paddingTop + paddingBottom;
        } else if (position == Position.TOP || position == Position.BOTTOM) {
            return drawableHeight + mTextHeight + paddingTop + drawablePadding + paddingBottom;
        } else {
            throw new IllegalArgumentException("position not known as one of the [ LEFT,TOP,RIGHT,BOTTOM ]!");
        }
    }

    /**
     * Method to get the width.
     *
     * @return width of this element
     */
    @Override
    public int getWidth() {
        int drawableWidth = 0;
        int textWidth = 0;
        if (mDrawable != null) {
            drawableWidth = mDrawable.getWidth();
        }
        if (isNotEmpty(mText)) {
            textWidth = Math.round(mTextPaint.measureText(mText));
        }
        if (position == Position.LEFT || position == Position.RIGHT) {
            return drawableWidth + textWidth + paddingLeft + paddingRight + drawablePadding;
        } else if (position == Position.TOP || position == Position.BOTTOM) {
            return Math.max(drawableWidth, textWidth) + paddingLeft + paddingRight;
        } else {
            throw new IllegalArgumentException("position not known as one of the [ LEFT,TOP,RIGHT,BOTTOM ]!");
        }
    }

    /**
     * Method to set the alpha.
     *
     * @param alpha alpha value
     */
    @Override
    public void setAlpha(int alpha) {
        if (mTextPaint.getAlpha() != alpha) {
            float alphaValue = TextWithImageDrawable.changeParamToAlpha(alpha);
            mTextPaint.setAlpha(alphaValue);
        }
    }

    /**
     * Method to get opacity.
     *
     * @return alpha value
     */
    public int getOpacity() {
        return (int) mTextPaint.getAlpha();
    }

    /**
     * Method to set ColorFilter.
     *
     * @param cf ColorFilter value
     */
    public void setColorFilter(ColorFilter cf) {
        if (mTextPaint.getColorFilter() != cf) {
            mTextPaint.setColorFilter(cf);
        }
    }

    /**
     * Method to set text in pixels.
     *
     * @param textSize text size
     */
    public void setTextSize(float textSize) {
        this.mTextSize = textSize;
        mTextPaint.setTextSize((int) mTextSize);
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = Math.round(fontMetrics.descent - fontMetrics.ascent);
    }

    /**
     * Method to set text color.
     *
     * @param color color int value
     */
    public void setTextColor(int color) {
        Color hmosColor = TextWithImageDrawable.changeParamToColor(color);
        mTextPaint.setColor(hmosColor);
    }

    /**
     * Method to set the position of image (left, right, top, bottom).
     *
     * @param position position
     */
    public void setImagePosition(Position position) {
        this.position = position;
    }

    private boolean isNotEmpty(String text) {
        return text != null && !text.equals("");
    }

    public static float changeParamToAlpha(int alpha) {
        return (float) alpha / 255;
    }

    public static Color changeParamToColor(int color) {
        return (new Color(color));
    }
}
