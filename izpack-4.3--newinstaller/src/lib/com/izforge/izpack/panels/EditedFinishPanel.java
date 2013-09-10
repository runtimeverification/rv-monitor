package com.izforge.izpack.panels;

import java.awt.GridBagConstraints;

import javax.swing.JLabel;

import com.izforge.izpack.gui.IzPanelLayout;
import com.izforge.izpack.gui.LabelFactory;
import com.izforge.izpack.installer.InstallData;
import com.izforge.izpack.installer.InstallerFrame;
import com.izforge.izpack.util.Log;
import com.izforge.izpack.util.OsVersion;


public class EditedFinishPanel extends FinishPanel
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public EditedFinishPanel(InstallerFrame parent, InstallData idata)
    {
        super(parent, idata);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void panelActivate()
    {
        parent.lockNextButton();
        parent.lockPrevButton();
        parent.setQuitButtonText(parent.langpack.getString("FinishPanel.done"));
        parent.setQuitButtonIcon("done");

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridwidth = 1;
        constraints.gridheight = 4;
        if (idata.installSuccess)
        {
            // We set the information
            JLabel jLabel = LabelFactory.create(idata.langpack.getString("FinishPanel.success"),
                    parent.icons.getImageIcon("preferences"), LEADING);

            constraints.gridy = GridBagConstraints.RELATIVE;
            constraints.gridx = 0;

            Filler dummy = new Filler();
            add(dummy, constraints);

            add(jLabel, constraints);


            if (idata.uninstallOutJar != null)
            {
                // We prepare a message for the uninstaller feature
                String path = translatePath(idata.info.getUninstallerPath());


                constraints.gridx = 0;
                add(LabelFactory.create(parent.langpack
                        .getString("FinishPanel.uninst.info"), parent.icons
                        .getImageIcon("preferences"), LEADING), constraints);



                constraints.gridx = 0;
                add(LabelFactory.create("  " + path, parent.icons.getImageIcon("empty"),
                        LEADING), constraints);
            }
            
            add(IzPanelLayout.createParagraphGap());

            String path;

            String msg= "To run RV Predict with ease, "
                    + "please don't forget to add the following directory to your PATH:";

            if (OsVersion.IS_WINDOWS) {
                path = translatePath(idata.info.getUninstallerPath());
                path = path.substring(0, path.indexOf("Uninstaller"));
                path += "win";
            } else {
                path = translatePath(idata.info.getUninstallerPath());
                path = path.substring(0, path.indexOf("Uninstaller"));
                path += "unix";
            }

            JLabel jLabel2 = LabelFactory.create(msg,
                    parent.icons.getImageIcon("edit"), LEADING);
            add(jLabel2, constraints);

            JLabel jLabel3 = LabelFactory.create(path,
                    parent.icons.getImageIcon("empty"), LEADING);
            add(jLabel3, constraints);

        }
        else
        {
            add(LabelFactory.create(parent.langpack.getString("FinishPanel.fail"),
                    parent.icons.getImageIcon("stop"), LEADING), NEXT_LINE);
            constraints.gridy++;
        }
        getLayoutHelper().completeLayout(); // Call, or call not?
        Log.getInstance().informUser();
    }

}
