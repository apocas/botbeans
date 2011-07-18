/*
 * Copyright (c) 2010 Chris Böhme - Pinkmatter Solutions. All Rights Reserved.
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
 *  o Neither the name of Chris Böhme, Pinkmatter Solutions, nor the names of
 *    any contributors may be used to endorse or promote products derived
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

import com.pinkmatter.spi.flamingo.RibbonDefaultRolloverProvider;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import org.openide.util.lookup.ServiceProvider;
import org.pushingpixels.flamingo.api.common.CommandButtonDisplayState;
import org.pushingpixels.flamingo.api.common.JCommandButton;
import org.pushingpixels.flamingo.api.common.JCommandButtonPanel;

@ServiceProvider(service = RibbonDefaultRolloverProvider.class)
public class RecentFilesRolloverProvider extends RibbonDefaultRolloverProvider {

    private static final String[] RECENT = new String[]{"Yesterday's File.txt", "Another file.doc", "Yet another file.pdf"};

    @Override
    public void menuEntryActivated(JPanel panel) {
        panel.removeAll();
        JCommandButtonPanel openHistoryPanel = new JCommandButtonPanel(
                CommandButtonDisplayState.MEDIUM);
        String groupName = "Recent Documents";
        openHistoryPanel.addButtonGroup(groupName);
        for (String s : RECENT) {
            JCommandButton historyButton = new JCommandButton(s);
            historyButton.setHorizontalAlignment(SwingUtilities.LEFT);
            openHistoryPanel.addButtonToLastGroup(historyButton);
            historyButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                }
            });
        }
        openHistoryPanel.setMaxButtonColumns(1);
        panel.setLayout(new BorderLayout());
        panel.add(openHistoryPanel, BorderLayout.CENTER);

    }
}
