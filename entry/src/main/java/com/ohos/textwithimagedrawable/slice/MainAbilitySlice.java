/*
 * Copyright (C) 2020-21 Application Library Engineering Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ohos.textwithimagedrawable.slice;

import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.agp.components.element.Element;
import ohos.agp.components.element.PixelMapElement;
import ohos.agp.render.Canvas;
import com.ohos.textwithimagedrawable.ResourceTable;
import wu.seal.textwithimagedrawable.BaseCombinedDrawable;
import wu.seal.textwithimagedrawable.TextWithImageDrawable;

/**
 * Sample slice to display the view with TextWithImageDrawable.
 */
public class MainAbilitySlice extends AbilitySlice {

    private String mText = "text";

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        Image blueIcon = new Image(this);
        blueIcon.setPixelMap(ResourceTable.Media_icon);
        PixelMapElement iconLeft = new PixelMapElement(blueIcon.getPixelMap());
        Element drawableLeft = iconLeft.getCurrentElement();

        Image blackIcon = new Image(this);
        blackIcon.setPixelMap(ResourceTable.Media_icon_bw);//here
        PixelMapElement iconRight = new PixelMapElement(blackIcon.getPixelMap());
        Element drawableRight = iconRight.getCurrentElement();

        final int drawablePadding = 50;

        BaseCombinedDrawable baseCombinedDrawable = new BaseCombinedDrawable(drawableLeft, drawableRight);
        baseCombinedDrawable.setRelatedPosition(drawableLeft.getWidth() + drawablePadding, 0);
        Image combinedImg = (Image) findComponentById(ResourceTable.Id_combinedImage);
        combinedImg.addDrawTask(new Component.DrawTask() {
            @Override
            public void onDraw(Component component, Canvas canvas) {
                baseCombinedDrawable.drawToCanvas(canvas);
            }
        });
        combinedImg.invalidate();

        TextWithImageDrawable drawLeft = new TextWithImageDrawable(this);
        initDrawable(drawablePadding, drawLeft, mText, TextWithImageDrawable.Position.LEFT);
        Image imgTxtLeft = (Image) findComponentById(ResourceTable.Id_leftImage);
        imgTxtLeft.addDrawTask(new Component.DrawTask() {
            @Override
            public void onDraw(Component component, Canvas canvas) {
                drawLeft.drawToCanvas(canvas);
            }
        });
        imgTxtLeft.invalidate();

        TextWithImageDrawable drawRight = new TextWithImageDrawable(this);
        initDrawable(drawablePadding, drawRight, mText, TextWithImageDrawable.Position.RIGHT);
        Image imgTxtRight = (Image) findComponentById(ResourceTable.Id_rightImage);
        imgTxtRight.addDrawTask(new Component.DrawTask() {
            @Override
            public void onDraw(Component component, Canvas canvas) {
                drawRight.drawToCanvas(canvas);
            }
        });
        imgTxtRight.invalidate();

        TextWithImageDrawable drawTop = new TextWithImageDrawable(this);
        initDrawable(drawablePadding, drawTop, mText, TextWithImageDrawable.Position.TOP);
        Image imgTxtTop = (Image) findComponentById(ResourceTable.Id_topImage);
        imgTxtTop.addDrawTask(new Component.DrawTask() {
            @Override
            public void onDraw(Component component, Canvas canvas) {
                drawTop.drawToCanvas(canvas);
            }
        });
        imgTxtTop.invalidate();

        TextWithImageDrawable drawBottom = new TextWithImageDrawable(this);
        initDrawable(drawablePadding, drawBottom, mText, TextWithImageDrawable.Position.BOTTOM);
        Image imgTxtBottom = (Image) findComponentById(ResourceTable.Id_bottomImage);
        imgTxtBottom.addDrawTask(new Component.DrawTask() {
            @Override
            public void onDraw(Component component, Canvas canvas) {
                drawBottom.drawToCanvas(canvas);
            }
        });
        imgTxtBottom.invalidate();
    }

    private void initDrawable(int drawablePadding, TextWithImageDrawable drawable, String text,
                              TextWithImageDrawable.Position position) {
        drawable.setText(text);
        drawable.setImagePosition(position);
        drawable.setImagePadding(drawablePadding);
        drawable.setImageRes(ResourceTable.Media_icon);
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
