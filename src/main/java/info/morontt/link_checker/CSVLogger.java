package info.morontt.link_checker;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

class CSVLogger implements Logger {
    private CSVWriter writer;
    private String filename;

    public void init() {
        filename = "errors_";

        Format formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        filename += formatter.format(new Date());
        filename += ".csv";

        try {
            writer = new CSVWriter(new FileWriter(filename), ';');
        } catch (IOException e) {
            System.err.println("Can not open file " + filename);
            System.exit(1);
        }

        try {
            writer.writeNext(new String[]{"Link", "Referrer", "Response code"});
            writer.flush();
        } catch (IOException e) {
            System.err.println("Can not write to file " + filename);
            System.exit(1);
        }
    }

    public void write(String[] args) {
        try {
            writer.writeNext(args);
            writer.flush();
        } catch (IOException e) {
            System.err.println("Can not write to file " + filename);
            System.exit(1);
        }
    }

    public void finish() {
        try {
            writer.close();
        } catch (IOException e) {
            System.err.println("WTF?!");
        }
    }
}
