package ru.talicamed.uzi_utilites;

/**
 * Created by 1 on 23.12.2015.
 * Under initial construction
 */

import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import java.io.*;

public class SimpleJPEGPrinter {

    private String printerName = "priPrinter";
    private PrintService printService = null;
    private PrintRequestAttributeSet pras = null;
    private DocFlavor flavor = DocFlavor.INPUT_STREAM.JPEG;
    private DocAttributeSet das = null;
    private FileInputStream fis = null;
    private Doc doc = null;

    public SimpleJPEGPrinter(String name){
        printerName = name;
    }

    private void GetPrinterService(){
        if (printerName.isEmpty()){
            printService = PrintServiceLookup.lookupDefaultPrintService();
        } else
        for (PrintService p:PrintServiceLookup.lookupPrintServices(null,null))
            if (p.getName().equals(printerName)) printService = p;
    }

    private void print(String filename){
        try {
            fis = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        GetPrinterService();
        CreatePrinterAttributes();
        CreateDocumentAndDocumentAttributes();
        DocPrintJob jobJPEG = printService.createPrintJob();
        try {
            jobJPEG.print(doc, pras);
        } catch (PrintException e) {
            e.printStackTrace();
        }
    }

    private void CreatePrinterAttributes(){
        pras = new HashPrintRequestAttributeSet();
        pras.add(new Copies(1));
        pras.add(new PrinterResolution(300,300,PrinterResolution.DPI));
        pras.add(PrintQuality.HIGH);
        //pras.add(new MediaPrintableArea(0, 0, 100, 100, MediaPrintableArea.MM));
        pras.add(MediaTray.TOP);
        //pras.add(new PrintQuality());
        //pras.add(new MediaSize(210, 297, Size2DSyntax.MM, MediaSizeName.ISO_A5));
    }

    private void CreateDocumentAndDocumentAttributes(){
        das = new HashDocAttributeSet();
        doc = new SimpleDoc(fis, flavor, das);
    }

    public static void main(String[] args){
        //SimpleJPEGPrinter p = new SimpleJPEGPrinter("priPrinter");
        SimpleJPEGPrinter p = new SimpleJPEGPrinter("");
        p.print("D:\\tmp\\pict.jpg");
        //p.JustForDebug();
    }

    public void JustForDebug(){

        Class[] att = printService.getSupportedAttributeCategories();
        for (Class at:att){
            System.out.println(at.getName());
            /*Class[] atvv = printService.getSupportedAttributeValues(at,flavor,null);
            for (Class atv:atvv){
                System.out.println("----" + atv.getName());*/
        }

        CopiesSupported ccs = (CopiesSupported) printService.getSupportedAttributeValues(Copies.class,null,null);
        for (int[] cc:ccs.getMembers()){
            for (int c:cc){
                System.out.print(c + " - ");
            }
            System.out.println(" ");
        }
        if (ccs.contains(3)) System.out.println("Yeeees");

        /*DocFlavor[] dff = printService.getSupportedDocFlavors();
        for (DocFlavor df:dff){
            System.out.print(df.getMediaSubtype() + " - ");
            System.out.print(df.getMediaType() + " - ");
            System.out.print(df.getMimeType() + " - ");
            System.out.print(df.getRepresentationClassName() + " - ");
            System.out.println(df.getMediaSubtype());
        }*/

        PrinterResolution[] prr = (PrinterResolution[])printService.getSupportedAttributeValues(PrinterResolution.class,null,null);
        for (PrinterResolution pr:prr){
            System.out.println(pr.toString(PrinterResolution.DPI,null));
        }
    }

}
