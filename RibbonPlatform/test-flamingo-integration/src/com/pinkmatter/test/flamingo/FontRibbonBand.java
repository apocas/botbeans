/*
 * Copyright (c) 2005-2010 Flamingo Kirill Grouchnikov. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of Flamingo Kirill Grouchnikov nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.pinkmatter.test.flamingo;

import com.pinkmatter.api.flamingo.ResizableIcons;
import javax.swing.JComboBox;
import org.pushingpixels.flamingo.api.common.CommandToggleButtonGroup;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButtonStrip;
import org.pushingpixels.flamingo.api.common.JCommandToggleButton;
import org.pushingpixels.flamingo.api.common.RichTooltip;
import org.pushingpixels.flamingo.api.common.icon.ResizableIcon;
import org.pushingpixels.flamingo.api.ribbon.JFlowRibbonBand;
import org.pushingpixels.flamingo.api.ribbon.JRibbonComponent;

/**
 *
 * @author Chris
 */
public class FontRibbonBand extends JFlowRibbonBand {

    public FontRibbonBand() {
        super("Font", null);
        JComboBox fontCombo = new JComboBox(new Object[]{
                    "Calibri   ", "Columbus   ",
                    "Consolas  ", "Cornelius   ",
                    "Cleopatra   ", "Cornucopia   ",
                    "Candella   ", "Cambria   "});
        JRibbonComponent fontComboWrapper = new JRibbonComponent(fontCombo);
        addFlowComponent(fontComboWrapper);

        JComboBox sizeCombo = new JComboBox(new Object[]{"10  ","11  ","12  ","14  "});
        JRibbonComponent sizeComboWrapper = new JRibbonComponent(sizeCombo);
        addFlowComponent(sizeComboWrapper);

        JCommandButtonStrip indentStrip = new JCommandButtonStrip();

        JCommandButton indentLeftButton = new JCommandButton("",
                getIcon("indent_left.gif"));
        indentStrip.add(indentLeftButton);

        JCommandButton indentRightButton = new JCommandButton("",
                getIcon("indent_right.gif"));
        indentStrip.add(indentRightButton);

        addFlowComponent(indentStrip);

        JCommandButtonStrip styleStrip = new JCommandButtonStrip();

        JCommandToggleButton styleBoldButton = new JCommandToggleButton("",
                getIcon("bold.gif"));
        styleBoldButton.getActionModel().setSelected(true);
        styleBoldButton.setActionRichTooltip(new RichTooltip("Bold", "Make the selected text bold"));
        styleStrip.add(styleBoldButton);

        JCommandToggleButton styleItalicButton = new JCommandToggleButton("",
                getIcon("italics.gif"));
        styleItalicButton.setActionRichTooltip(new RichTooltip("Italic", "Italicise the selected text"));
        styleStrip.add(styleItalicButton);

        JCommandToggleButton styleUnderlineButton = new JCommandToggleButton(
                "", getIcon("underline.gif"));
        styleUnderlineButton.setActionRichTooltip(new RichTooltip("Underline", "Underline the selected text"));
        styleStrip.add(styleUnderlineButton);

        JCommandToggleButton styleStrikeThroughButton = new JCommandToggleButton(
                "", getIcon("strikethrough.gif"));
        styleStrikeThroughButton.setActionRichTooltip(new RichTooltip("Strikethrough", "Strike the selected text"));
        styleStrip.add(styleStrikeThroughButton);

        addFlowComponent(styleStrip);

        JCommandButtonStrip alignStrip = new JCommandButtonStrip();
        CommandToggleButtonGroup alignGroup = new CommandToggleButtonGroup();

        JCommandToggleButton alignLeftButton = new JCommandToggleButton("",
                getIcon("justify_left.gif"));
        alignLeftButton.getActionModel().setSelected(true);
        alignGroup.add(alignLeftButton);
        alignStrip.add(alignLeftButton);

        JCommandToggleButton alignCenterButton = new JCommandToggleButton("",
                getIcon("justify_center.gif"));
        alignGroup.add(alignCenterButton);
        alignStrip.add(alignCenterButton);

        JCommandToggleButton alignRightButton = new JCommandToggleButton("",
                getIcon("justify_right.gif"));
        alignGroup.add(alignRightButton);
        alignStrip.add(alignRightButton);

        JCommandToggleButton alignFillButton = new JCommandToggleButton("",
                getIcon("justify_justify.gif"));
        alignGroup.add(alignFillButton);
        alignStrip.add(alignFillButton);

        addFlowComponent(alignStrip);
    }

    private static ResizableIcon getIcon(String name) {
        return ResizableIcons.fromResource("com/pinkmatter/test/flamingo/resources/"+name);
    }
}
