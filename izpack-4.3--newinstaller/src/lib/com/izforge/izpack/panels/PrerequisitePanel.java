/*
 * IzPack - Copyright 2001-2008 Julien Ponge, All Rights Reserved.
 * 
 * http://izpack.org/
 * http://izpack.codehaus.org/
 * 
 * Copyright 2002 Jan Blok
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *     
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.izforge.izpack.panels;

import com.izforge.izpack.gui.IzPanelLayout;
import com.izforge.izpack.gui.LabelFactory;
import com.izforge.izpack.installer.InstallData;
import com.izforge.izpack.installer.InstallerFrame;
import com.izforge.izpack.installer.IzPanel;
import com.izforge.izpack.installer.IzPanel.Filler;
import com.izforge.izpack.util.*;

import javax.swing.*;

import java.awt.*;

/**
 * The AspectJ panel class.
 *
 * @author Choonghwan Lee
 */
public class PrerequisitePanel extends IzPanel
{
    private static final long serialVersionUID = 3257848714914902589L;

    private HTMLPane labelMessage;

    private PrerequisiteWorker worker;

    protected JLabel tipLabel;

    private boolean validated = false;

    public PrerequisitePanel(InstallerFrame parent, InstallData idata)
    {
        this(parent, idata, new IzPanelLayout());
    }

    public PrerequisitePanel(InstallerFrame parent, InstallData idata, LayoutManager2 layout)
    {
        super(parent, idata, layout);
        tipLabel = new JLabel(getDefaultLabelText());
        tipLabel.setIcon(parent.icons.getImageIcon("tip"));
        add(tipLabel, IzPanelLayout.getDefaultConstraint(FULL_LINE_CONTROL_CONSTRAINT));
        this.worker = new PrerequisiteWorker(idata);
        this.initializePanel();
    }

    private void initializePanel()
    {
        String msg = this.getCheckingMessage();
        this.labelMessage = new HTMLPane(msg);
        add(this.labelMessage);
        add(IzPanelLayout.createParagraphGap());

        getLayoutHelper().completeLayout();
    }

    private String getDefaultLabelText() {
        return "<html><br /><br />Testing Z3<br />"
                + "(This may take a while)....<br /><br /></html>";
    }


    private String getDefaultMessage()
    {
        String msg = "<br />"; 
        msg += "In order to run RV Predict successfully, "
                + "you need to make sure Z3 (<a href='http://z3.codeplex.com/'>" 
                +  "http://z3.codeplex.com/</a>) is installed and "
                + "it is in PATH.<br /><br />";
        return msg;
    }

    private String getCheckingMessage()
    {
        String msg = this.getDefaultMessage();
        //msg += " The installer is now checking these ...";
        return msg;
    }

    public void panelActivate()
    {
        super.panelActivate();

        String msg = this.getCheckingMessage();
        this.labelMessage.setContentHTML(msg);

        this.validated = false;

        this.parent.blockGUI();
        this.worker.startThread();
    }

    public void handleCompletion(boolean z3)
    {
        this.showResult(z3);

        this.validated = true;
        this.parent.releaseGUI();
    }

    public boolean isValidated()
    {
        return this.validated;
    }

    private void showResult(boolean z3)
    {
        String result = this.getDefaultMessage();
        String label;

        result += "<br /><br />";

        if (z3) {
            label = "<html><br /><br />The installer has successfully found "
                    + "Z3.<br /><br /><br /></html>";
            //            tipLabel = LabelFactory.create(label,
            //                    parent.icons.getImageIcon("preferences"), LEADING);
            tipLabel.setIcon(parent.icons.getImageIcon("preferences")); 
            tipLabel.setText(label);
        }
        else {
            label = "<html><br /><br />Z3 cannot be found. "
                    + "Either it is not installed or it is not in PATH. "
                    + "<br />Please make sure you install it before you proceed.<br /><br /><br /></html>";
            //            tipLabel = LabelFactory.create(label,
            //                    parent.icons.getImageIcon("stop"), LEADING);
            tipLabel.setIcon(parent.icons.getImageIcon("stop"));
            tipLabel.setText(label);
        }

        this.labelMessage.setContentHTML(result);
    }

    class PrerequisiteWorker implements Runnable
    {
        protected VariableSubstitutor vs;

        public PrerequisiteWorker(InstallData idata)
        {
            this.vs = new VariableSubstitutor(idata.getVariables());
        }

        public void startThread()
        {
            Thread thread = new Thread(this, "prerequisite");
            thread.start();
        }

        public void run()
        {
            boolean z3 = false;
            try {
                z3 = this.checkZ3();
            }
            catch (Exception e) {
            }
            finally {
                PrerequisitePanel.this.handleCompletion(z3);
            }
        }

        private boolean checkZ3(){
            String[] params;
            if (OsVersion.IS_WINDOWS) {
                String[] p = { "cmd", "/c", "z3.exe", "/h" };
                params = p;
            }
            else {
                String[] p = { "z3", "-h" };
                params = p;
            }

            String[] output = new String[2];

            FileExecutor fe = new FileExecutor();
            fe.executeCommand(params, output);

            if (output[0].indexOf("z3") >= 0)
                return true;
            return false;
        }
    }
}

// vim:expandtab:sts=4:sw=4

