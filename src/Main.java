import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;

public class Main
{

    public static TST<Integer> myTST = new TST<>();

    public static void main(String[] args) throws FileNotFoundException
    {
        getTST();

    }

    public static void getTST()
    {
        try {
            Scanner scanner = new Scanner(new File("stops.txt"));
            scanner.next();

            for (int i = 0; i < 8757; i++) {
                StringBuilder result;
                scanner.useDelimiter(",\\s*");
                String temp = scanner.next();
                result = new StringBuilder(temp);
                temp = scanner.next();
                result.append(", ").append(temp);

                scanner.reset();
                temp = scanner.next();
                String[] res = temp.split("[,]", 0);

                scanner.useDelimiter(",\\s*");
                temp = scanner.next();
                temp = temp.trim();
                String newString;
                if (res[res.length - 1].equals("WB") || res[res.length - 1].equals("EB") || res[res.length - 1].equals("SB") || res[res.length - 1].equals("NB") || res[res.length - 1].equals("FLAGSTOP")) {
                    newString = temp + " " + res[res.length - 1];
                } else {
                    newString = res[res.length - 1] + temp;
                }

                result.insert(0, newString + ", ");

                scanner.reset();
                scanner.useDelimiter(",");
                for (int j = 0; j < 6; j++) {
                    temp = scanner.next();
                    result.append(", ").append(temp);
                }

                myTST.put(result.toString(), i);

            }

            // print results
            StdOut.println("keys(\"\"):");
            for (String key : myTST.keys()) {
                StdOut.println(key + " " + myTST.get(key));
            }
            StdOut.println();

            StdOut.println("The stop will be formatted as:");
            StdOut.println("stop name, stop id, stop code, stop desc, stop lat, stop_lon, zone id, stop url, location type, parent station");
            StdOut.println("Enter the stop: ");
            String key = StdIn.readString();
            key = key.toUpperCase(Locale.ROOT);
            double answer = 0;
            for (String s : myTST.keysWithPrefix(key)) {
                StdOut.println(s);
                answer++;
            }
            if (answer < 1) {
                StdOut.println("The bus stop " + key + " does not exsit.");
            }
        }catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        }
    }
