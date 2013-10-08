package com.izforge.izpack.panels;

import java.io.*;

import com.izforge.izpack.installer.InstallData;
import com.izforge.izpack.installer.InstallerFrame;
import com.izforge.izpack.util.Debug;
import com.izforge.izpack.util.IoHelper;
import com.izforge.izpack.util.OsVersion;


public class SafeTargetPanel extends TargetPanel {
	
    private static final long serialVersionUID = 3256240616341429170L;

    public SafeTargetPanel(InstallerFrame parent, InstallData idata)
    {
        super(parent, idata);
    }

    // The original isWriteable defined in PathInputPanel.java is problematic.
    // It tries to create a file rather than a directory. In c:\, creating a file
    // is not allowed while creating a directory is allowed.
    @Override
    public boolean isWriteable()
    {
        File existParent = IoHelper.existingParent(new File(pathSelectionPanel.getPath()));
        if (existParent == null)
        {
            return false;
        }

        // On windows we cannot use canWrite because
        // it looks to the dos flags which are not valid
        // on NT or 2k XP or ...
        if (OsVersion.IS_WINDOWS)
        {
            File tmpFile;
            try
            {
                // Creating a temporary directory looks ugly.
                // I rather create javamop2.1 directory and then remove it.
                // If that directory exists, the caller will ask if the user wants to overwrite it.
                String path = pathSelectionPanel.getPath();
                File dir = new File(path);
                if (dir.exists())
                    return true;

                if (dir.mkdir())
                    dir.delete();
                else
                    return false;
            }
            catch (Exception e)
            {
                Debug.trace(e.toString());
                return false;
            }
            return true;
        }
        return existParent.canWrite();
    }

}
