import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TST;

import java.io.*;
import java.util.*;

public class Main
{

    public static TST<Integer> myTST = new TST<>();
    public static ArrayList<Integer> stops = new ArrayList<>();

    public static void main(String[] args)
    {
        getTST();
        //testTST();
        FileWriter myFile;
        try
        {
            myFile = new FileWriter("digraph.txt");
            BufferedWriter myWrite = new BufferedWriter(myFile);

            shortestPath(myWrite);
            myWrite.close();
        }catch (IOException except)
        {
            except.printStackTrace();
        }

    }

    public static void shortestPath(BufferedWriter myWrite) throws IOException {
      //  int stop_id_1 = getBus1();
     //   System.out.println("Thank you the stop id is: " + stop_id_1);
      //  int stop_id_2 = getBus2();
     //   System.out.println("Thank you the stop id is: " + stop_id_2);

        System.out.println("Please wait ...");

        getAmmountOfStops();
        Collections.sort(stops);

        int lengthOfList = stops.size();

        String Vertices = Integer.toString(lengthOfList);
        myWrite.write(Vertices);
        myWrite.write(" ");
    }

    public static void getAmmountOfStops()
    {
        In in;
        try {
            in = new In("transfers.txt");
            in.readLine();
            while (!in.isEmpty()) {
                String s = in.readLine();
                String[] res = s.split("[,]", 0);

                addInt(res, 0);
                addInt(res, 1);
            }
        }
        catch (IllegalArgumentException e) {
            System.out.println(e);
        }

        try {
            in = new In("stop_times.txt");
            in.readLine();
            while (!in.isEmpty()) {
                String s = in.readLine();
                String[] res = s.split("[,]", 0);

                addInt(res, 3);
            }
        }
        catch (IllegalArgumentException e) {
            System.out.println(e);
        }
    }

    public static void addInt(String[] res, int i)
    {
        String data = res[i];
        data = data.trim();
        int stop = Integer.parseInt(data);
        if(!stops.contains(stop))
            stops.add(stop);
    }

    public static int getBus1()
    {
        StdOut.print("Input your first stop: ");
        return(getBusInfo());
    }

    public static int getBus2()
    {
        StdOut.print("Input your second stop: ");
        return(getBusInfo());
    }

    public static int getBusInfo()
    {
        int finalAnswer;
        boolean success = false;
        while(!success)
        {
            Scanner myInputScanner = new Scanner(System.in);
            String key = myInputScanner.nextLine();
            key = key.toUpperCase(Locale.ROOT);
            double answer = 0;
            String myString = null;

            for (String s : myTST.keysWithPrefix(key)) {
                StdOut.println(s);
                myString = s;
                answer++;
            }

            StdOut.println();
            if (answer < 1) {
                StdOut.print("The bus stop " + key + " does not exist. Please try again: ");
            }
            else if(answer > 1){
                StdOut.print("Sorry, Can you be more specific: ");
            }
            else
            {
                String[] result = myString.split("[,]", 0);
                String result1 = result[1];
                String trimed = result1.trim();
                finalAnswer = Integer.parseInt(trimed);
                success = true;
                return(finalAnswer);
            }
        }
        return 0;
    }

    public static void testTST()
    {
        StdOut.println("The stop will be formatted as:");
        StdOut.println("stop name, stop id, stop code, stop desc, stop lat, stop_lon, zone id, stop url, location type, parent station");
        Scanner myInputScanner = new Scanner(System.in);
        StdOut.println("Enter the stop: ");
        String key = myInputScanner.nextLine();
        key = key.toUpperCase(Locale.ROOT);
        double answer = 0;

        for (String s : myTST.keysWithPrefix(key)) {
            StdOut.println(s);
            answer++;
        }

        if (answer < 1) {
            StdOut.println("The bus stop " + key + " does not exsit.");
        }
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
                    newString = res[res.length - 1] + " " +  temp;
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
        }catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        }
    }
