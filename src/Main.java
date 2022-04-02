import edu.princeton.cs.algs4.*;

import java.io.*;
import java.util.*;

public class Main
{

    public static TST<Integer> myTST = new TST<>();
    public static ArrayList<String> myPathFinal = new ArrayList<>();
    public static ArrayList<Integer> stops = new ArrayList<>();
    public static ArrayList<Double> routes = new ArrayList<>();
    public static ArrayList<String> arrivalTimes = new ArrayList<>();
    public static EdgeWeightedDigraph G;
    public static DijkstraSP myDijkstra;
    public static Iterable<DirectedEdge> myPath;

    public static void main(String[] args)
    {
        getTST();
        boolean programRunning = true;
        System.out.println("");
        System.out.println("Select Query: ");
        System.out.println("1. Shortest Path between two stops");
        System.out.println("2. Search for a bus stop by name ");
        System.out.println("3. Search trips with a certain arrival time");
        System.out.println("4. End the program \n");
        while(programRunning)
        {
            System.out.print("Please enter the query number: ");
            Scanner query = new Scanner(System.in);
            String Number = query.next();
            try
            {
                int queryNumber = Integer.parseInt(Number);
                if(queryNumber == 1)
                {
                    shortestPath();
                }
                else if(queryNumber == 2)
                {
                    testTST();
                }
                else if(queryNumber == 3)
                {
                    tripsWithArrivalTime();
                }
                else if(queryNumber == 4)
                {
                    programRunning = false;
                }
                else
                {
                    System.out.println("Please choose a number between 1 and 4: ");
                }
            }catch (NumberFormatException e)
            {
                System.out.println("Input was not an integer ");
            }
        }
        System.out.println("Thank you, enjoy your day !!");
    }

    public static void tripsWithArrivalTime()
    {
        boolean valid = false;
        String realMinutes = "";
        String realSeconds = "";
        while(!valid)
        {
            System.out.println("Enter arrival time of trip in format 00:00:00 :");
            Scanner userTime = new Scanner(System.in);
            String inputTime = userTime.nextLine();
            if(timeAccurate(inputTime))
            {
                System.out.println("Thank you ...");
                String[] time = inputTime.split("[:]", 0);
                int hour = Integer. parseInt(time[0]);
                int minutes = Integer. parseInt(time[1]);
                int seconds = Integer. parseInt(time[2]);
                if(minutes < 10)
                {
                    realMinutes = "0" + minutes;
                }
                else
                {
                    realMinutes = "" + minutes;
                }
                if(seconds < 10)
                {
                    realSeconds = "0" + seconds;
                }
                else
                {
                    realSeconds = "" + seconds;
                }
                String totalTime = hour + ":" + realMinutes + ":" + realSeconds;
                System.out.println("Time entered is: " + totalTime);
                getPathArrivalTime(totalTime);
                Collections.sort(arrivalTimes);
                for(int i = 0; i < arrivalTimes.size(); i++)
                {
                    String temp = arrivalTimes.get(i);
                    System.out.println(temp);
                }
                valid = true;
            }
        }
    }

    public static boolean timeAccurate(String data)
    {
        data = data.trim();
        String[] time = data.split("[:]", 0);
        if(time.length == 3)
        {
            try
            {
                int hour = Integer. parseInt(time[0]);
                int minutes = Integer. parseInt(time[1]);
                int seconds = Integer. parseInt(time[2]);
                if(hour < 0 || hour > 23)
                {
                    System.out.println("Time does not exist, must be in 24hr clock. ");
                    return false;
                }
                if(minutes < 0 || minutes > 59)
                {
                    System.out.println("Time does not exist, must be in 24hr clock. ");
                    return false;
                }
                if(seconds  < 0 || seconds > 59)
                {
                    System.out.println("Time does not exist, must be in 24hr clock. ");
                    return false;
                }
                return true;
            }catch (NumberFormatException e)
            {
                System.out.println("Input is not a valid integer");
                return false;
            }
        }
        System.out.println("Time not in correct format.");
        return false;
    }

    public static void getPathArrivalTime(String timeEntered)
    {
        In in;
        try {
            in = new In("stop_times.txt");
            in.readLine();
            while (!in.isEmpty())
            {
                String s = in.readLine();
                String[] temp = s.split("[,]", 0);

                String arrival = temp[1].trim();
                if(arrival.equals(timeEntered))
                {
                    arrivalTimes.add(s);
                }
            }
        }catch (IllegalArgumentException e)
        {
            System.out.println(e);
        }
    }

    public static void shortestPath()
    {
        getAmmountOfStops();

        System.out.println("Please wait ...");

        Collections.sort(stops);

        int lengthOfList = stops.size();

        G = new EdgeWeightedDigraph(lengthOfList);
        getAmmountOfRoutes();

        int stop_id_1 = getBus1();
        System.out.println("Thank you the stop id is: " + stop_id_1);
        stop_id_1 = stops.indexOf(stop_id_1);

        int stop_id_2 = getBus2();
        System.out.println("Thank you the stop id is: " + stop_id_2);
        stop_id_2 = stops.indexOf(stop_id_2);

        myDijkstra = new DijkstraSP(G, stop_id_1);
        myPath = myDijkstra.pathTo(stop_id_2);
        String output = myPath.toString();
        String[] res = output.split("\\s+");

        int pathLength = res.length;
        double totalCost = 0;

        for(int i = pathLength - 1; i >= 0; i--)
        {
            totalCost = totalCost + Double.parseDouble(res[i].trim());
            i = i - 1;
            String[] temp = res[i].split("->");
            int first = Integer.parseInt(temp[0].trim());
            int second = Integer.parseInt(temp[1].trim());
            int firstStop = stops.get(first);
            int secondStop = stops.get(second);
            String input = firstStop + "->" + secondStop;
            myPathFinal.add(input);
        }
        System.out.println("The total cost is " + totalCost);
        System.out.println("The path is: ");
        Collections.reverse(myPathFinal);
        System.out.println(myPathFinal);
    }

    public static void getAmmountOfRoutes()
    {
        In in;
        try {
            in = new In("transfers.txt");
            in.readLine();
            while (!in.isEmpty())
            {
                String s = in.readLine();
                String[] res = s.split("[,]", 0);

                String edge = res[0].trim();
                int edge1 = Integer.parseInt(edge);
                edge1 = stops.indexOf(edge1);

                edge = res[1].trim();
                int edge2 = Integer.parseInt(edge);
                edge2 = stops.indexOf(edge2);

                String data = res[2].trim();
                double realData = Double.parseDouble(data);
                if(realData == 0.0)
                {
                    DirectedEdge myEdge = new DirectedEdge(edge1, edge2, 2);
                    G.addEdge(myEdge);
                }
                else if(realData == 2.0)
                {
                    data = res[3].trim();
                    realData = Double.parseDouble(data);
                    realData = realData / 100;
                    DirectedEdge myEdge = new DirectedEdge(edge1, edge2, realData);
                    G.addEdge(myEdge);
                }
            }
        }catch (IllegalArgumentException e)
        {
            System.out.println(e);
        }
        try {
            in = new In("stop_times.txt");
            in.readLine();
            String temp = in.readLine();
            while (!in.isEmpty())
            {
                String s = in.readLine();
                String[] first = temp.split("[,]", 0);
                String[] second = s.split("[,]", 0);

                String edge = first[3].trim();
                int edge1 = Integer.parseInt(edge);
                edge1 = stops.indexOf(edge1);

                edge = second[3].trim();
                int edge2 = Integer.parseInt(edge);
                edge2 = stops.indexOf(edge2);

                if(first[0].equals(second[0]))
                {
                    DirectedEdge myEdge = new DirectedEdge(edge1, edge2, 1);
                    G.addEdge(myEdge);
                }
                temp = s;
            }
        }
        catch (IllegalArgumentException e)
        {
            System.out.println(e);
        }
    }


    public static void getAmmountOfStops() {
        In in;
        try {
            in = new In("stops.txt");
            in.readLine();
            while (!in.isEmpty()) {
                String s = in.readLine();
                String[] res = s.split("[,]", 0);

                addInt(res, 0);
            }
        } catch (IllegalArgumentException e) {
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
