package wu.seal.textwithimagedrawable;

import ohos.agp.components.element.Element;

/**
 * BaseCombinedDrawable.
 */
public class BaseCombinedDrawable extends Element {
    private Element one;
    private Element two;
    private int relatedX;
    private int relatedY;
    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int paddingBottom;

    public void createNativePtr(Object source) {
        // need not implement this
    }

    /**
     * Constructor.
     *
     * @param one first element
     * @param two second element
     */
    public BaseCombinedDrawable(Element one, Element two) {
        this.one = one;
        this.two = two;
        if (one == null || two == null) {
            throw new IllegalArgumentException("drawable one or two can't be null!");
        }
    }

    /**
     * Set the relative offset position of the upper left corner of drawable two relative to the upper left corner of
     * drawable one.
     *
     * @param relatedX relative offset of x-axis
     * @param relatedY relative offset of y-axis
     */
    public void setRelatedPosition(int relatedX, int relatedY) {
        this.relatedX = relatedX;
        this.relatedY = relatedY;
    }

    /**
     * Method to set padding on four sides.
     *
     * @param paddingLeft   Left padding
     * @param paddingTop    Top padding
     * @param paddingRight  Right padding
     * @param paddingBottom Bottom padding
     */
    public void setPadding(int paddingLeft, int paddingTop, int paddingRight, int paddingBottom) {
        this.paddingLeft = paddingLeft;
        this.paddingTop = paddingTop;
        this.paddingRight = paddingRight;
        this.paddingBottom = paddingBottom;
    }

    @Override
    public void drawToCanvas(ohos.agp.render.Canvas canvas) {
        one.setBounds(0, 0, one.getWidth(), one.getHeight());
        two.setBounds(0, 0, two.getWidth(), two.getHeight());
        canvas.translate(paddingLeft, paddingTop);
        canvas.save();
        canvas.translate(relatedX < 0 ? Math.abs(relatedX) : 0, relatedY < 0 ? Math.abs(relatedY) : 0);
        one.drawToCanvas(canvas);
        canvas.restore();
        canvas.save();
        canvas.translate(relatedX > 0 ? Math.abs(relatedX) : 0, relatedY > 0 ? Math.abs(relatedY) : 0);
        two.drawToCanvas(canvas);
        canvas.restore();
    }

    /**
     * Method to get the width.
     *
     * @return width of this element
     */
    @Override
    public int getWidth() {
        int width = paddingLeft + paddingRight;
        width = width + one.getWidth();
        if (relatedX < 0) {
            width += Math.abs(relatedX);
        } else {
            final int del = relatedX + two.getWidth() - one.getWidth();
            width += del > 0 ? del : 0;
        }
        return width;
    }

    /**
     * Method to get the height.
     *
     * @return height of this element
     */
    @Override
    public int getHeight() {
        int height = paddingTop + paddingBottom;
        height += one.getHeight();
        if (relatedY < 0) {
            height += Math.abs(relatedY);
        } else {
            final int del = relatedY + two.getHeight() - one.getHeight();
            height += del > 0 ? del : 0;
        }
        return height;
    }

    @Override
    public void setAlpha(int alpha) {
        one.setAlpha(alpha);
        two.setAlpha(alpha);
    }
}
