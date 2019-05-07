package io.server.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 
 * @author Teek
 *
 */
public class Referals {

	private static final DynamicArray<String> REFERED = new DynamicArray<>(1000);

    public static void load() throws Exception {
    	REFERED.clear();
        File file = new File("data/referals.txt");
        System.out.println(file.getAbsolutePath());
        if(file.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while((line = br.readLine()) != null) {
                if(line.isEmpty())
                    continue;
                REFERED.add(line);
            }
            br.close();
        }
    }

    public static void addToList(String CID) {
        REFERED.add(CID);
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter("data/referals.txt"));
            int highestIndex = REFERED.length();
            for(int index = 0; index < highestIndex; index++) {
                String s = REFERED.get(index);
                if(s != null) {
                    bw.write(s);
                    bw.newLine();
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            if(bw != null) {
                try {
                    bw.close();
                } catch(IOException e) {
                    e.printStackTrace();
                }
                bw = null;
            }
        }
    }
    
    public static boolean hasRefered(String CID) {
        return REFERED.contains(CID);
    }

}
