package com.falstad.circuit;

public class ImportExportDialogFactory {

    public static ImportExportDialog Create(CircuitSimulator f,
            ImportExportDialog.Action type) {
            return new ImportExportClipboardDialog(f, type);
//        if (f.applet != null) {
//            /*
//             try
//             {
//             return new ImportExportAppletDialog(f, type);
//             }
//             catch (Exception e)
//             {
//             return new ImportExportClipboardDialog(f, type);
//             }
//             */
//        } else {
//            return new ImportExportFileDialog(f, type);
//        }
    }
}
