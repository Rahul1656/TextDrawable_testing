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

package com.ohos.textwithimagedrawable;

import com.hmos.compat.utils.ResourceUtils;
import ohos.aafwk.ability.delegation.AbilityDelegatorRegistry;
import ohos.agp.components.element.Element;
import ohos.app.Context;
import ohos.global.resource.NotExistException;
import ohos.global.resource.Resource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.*;

public class TextWithImageDrawableText {
    private Context context;
    @Before
    public void setUp()  {
        context = AbilityDelegatorRegistry.getAbilityDelegator().getAppContext();
    }
    @Test
    public void testBundleName() {
        final String actualBundleName = AbilityDelegatorRegistry.getArguments().getTestBundleName();
        assertEquals("com.example.textwithimagedrawable", actualBundleName);
    }
    @Test
    public void testGetDrawable()
    {
        Element element=ResourceUtils.getDrawable(context,ResourceTable.Media_icon);
        Assert.assertNotNull(element);
    }
    @Test
    public void testPixelMapElement() throws IOException, NotExistException {
        Resource resource=context.getResourceManager().getResource(ResourceTable.Media_icon);
        assertNotNull(ResourceUtils.prepareElement(resource));
    }
    @Test
    public void testpreparePixelMap()throws IOException, NotExistException{
        Resource resource=context.getResourceManager().getResource(ResourceTable.Media_icon);
        assertNotNull(ResourceUtils.preparePixelMap(resource));
    }
}