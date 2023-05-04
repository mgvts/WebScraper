package com.parse.service;


import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

public class DocService {

    String fileName;
    String DATA_PATH = "D:\\GeoParser\\data\\";

    public DocService(String fileName) {
        this.fileName = fileName;

    }

    public void write(String text) {
        try (FileWriterWithEncoding file = new FileWriterWithEncoding(new File(DATA_PATH + fileName), StandardCharsets.UTF_8)) {
            file.write(text);
        } catch (IOException e) {
            System.err.println("some problems while writing file " + e.getMessage());
        }
    }

    public void getTable() {
        try {

            File[] data = new File(DATA_PATH).listFiles();
            for (File file : data) {
                XWPFDocument xdoc = new XWPFDocument(OPCPackage.openOrCreate(file));
                List<XWPFTable> tables = xdoc.getTables();
                for (XWPFTable table : tables) {
                    for (XWPFTableRow row : table.getRows()) {
                        for (XWPFTableCell cell : row.getTableCells()) {
                            System.out.println(cell.getText());
                            String sFieldValue = cell.getText();
                            if (sFieldValue.matches("Whatever you want to match with the string") || sFieldValue.matches("Approved")) {
                                System.out.println("The match as per the Document is True");
                            }
//					System.out.println("\t");
                        }
                        System.out.println(" ");
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
