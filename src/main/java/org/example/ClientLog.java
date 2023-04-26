package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {
    public ClientLog() {
        lg = new ArrayList<>();
        lg.add("product,amount");
    }

    private List<String> lg;

    public void log(int prodNum, int amount) {
        lg.add(prodNum + "," + amount);
    }

    public boolean exportAsCSV(File CSVFile) {
        try (PrintWriter out = new PrintWriter(CSVFile)) {
            for (String ln : lg) {
                out.println(ln);
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
