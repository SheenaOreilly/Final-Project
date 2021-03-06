import edu.princeton.cs.algs4.*;

import java.io.*;
import java.util.*;

public class Main
{

    public static TST<Integer> myTST = new TST<>();
    public static ArrayList<String> myPathFinal = new ArrayList<>();
    public static ArrayList<Integer> stops = new ArrayList<>();
    public static ArrayList<String> arrivalTimes = new ArrayList<>();
    public static EdgeWeightedDigraph G;
    public static DijkstraSP myDijkstra;
    public static Iterable<DirectedEdge> myPath;

    // asks user to select which program they would like to use or to exit the program
    // it will then run the code the user selected
    public static void main(String[] args)
    {
        getTST();
        getAmountOfStops();

        System.out.println();
        System.out.println("Please wait ... \n");

        Collections.sort(stops);

        int lengthOfList = stops.size();

        G = new EdgeWeightedDigraph(lengthOfList);
        getAmountOfRoutes();

        boolean programRunning = true;
        System.out.println("Select Query: ");
        System.out.println("1. Shortest Path between two stops");
        System.out.println("2. Search for a bus stop by name ");
        System.out.println("3. Search trips with a certain arrival time");
        System.out.println("4. End the program \n");
        while(programRunning)
        {
            System.out.println(" ");
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

    // Searching for all trips with a given arrival time, returning full details of all trips matching the
    //criteria (zero, one or more), sorted by trip id
    public static void tripsWithArrivalTime()
    {
        boolean valid = false;
        String realMinutes;
        String realSeconds;
        while(!valid)
        {
            System.out.println(" ");
            System.out.print("Enter arrival time of trip in format 00:00:00 :");
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
                    realMinutes = minutes + "0";
                }
                else
                {
                    realMinutes = "" + minutes;
                }
                if(seconds < 10)
                {
                    realSeconds = seconds + "0";
                }
                else
                {
                    realSeconds = "" + seconds;
                }
                String totalTime = hour + ":" + realMinutes + ":" + realSeconds;
                System.out.println("Time entered is: " + totalTime);
                System.out.println(" ");
                getPathArrivalTime(totalTime);
                Collections.sort(arrivalTimes);
                for(int i = 0; i < arrivalTimes.size(); i++)
                {
                    String temp = arrivalTimes.get(i);
                    System.out.println(temp);
                }
                if(arrivalTimes.isEmpty())
                {
                    System.out.println("No trips arriving at that time " );
                }
                valid = true;
            }
        }
    }

    //checking to see if an inputted time is a real time in the 24 hour clock
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

    //gets all the paths that arrive at a given time
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

    //Finding shortest paths between 2 bus stops (as input by the user), returning the list of stops
    //en route as well as the associated ???cost???.
    public static void shortestPath()
    {
        myPathFinal.clear();
        int stop_id_1 = getBus1();
        int journey1 = stop_id_1;
        stop_id_1 = stops.indexOf(stop_id_1);

        int stop_id_2 = getBus2();
        int journey2 = stop_id_2;
        stop_id_2 = stops.indexOf(stop_id_2);

        if(stop_id_1 != stop_id_2)
        {
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
            System.out.println();
            System.out.println("The path from " + journey1 + " to " + journey2 + " is: ");
            Collections.reverse(myPathFinal);
            System.out.println(myPathFinal);
        }
        else
        {
            System.out.println("Stop 1 and Stop 2 are the same.\n");
        }
    }

    //creates my directed edges and stores them in a edge weighted digraph
    public static void getAmountOfRoutes()
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


    //gets all the stop ids and stores them in an array list
    public static void getAmountOfStops() {
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

    // add a stop add to the list of stops
    public static void addInt(String[] res, int i)
    {
        String data = res[i];
        data = data.trim();
        int stop = Integer.parseInt(data);
        if(!stops.contains(stop))
            stops.add(stop);
    }

    //gets stop id of the first stop entered by the user
    public static int getBus1()
    {
        System.out.print("Input your first stop: ");
        return(getBusInfo());
    }

    //gets the stop id of the second stop entered by the user
    public static int getBus2()
    {
        System.out.print("Input your second stop: ");
        return(getBusInfo());
    }

    //return a stop id for a given stop name
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
                String trimmed = result1.trim();
                finalAnswer = Integer.parseInt(trimmed);
                success = true;
                return(finalAnswer);
            }
        }
        return 0;
    }

    //user enters a stop and the method will return all the stops with that name
    public static void testTST()
    {
        System.out.println(" ");
        StdOut.println("The stop will be formatted as:");
        StdOut.println("stop name, stop id, stop code, stop desc, stop lat, stop_lon, zone id, stop url, location type, parent station");
        Scanner myInputScanner = new Scanner(System.in);
        StdOut.print("Enter the stop: ");
        String key = myInputScanner.nextLine();
        key = key.toUpperCase(Locale.ROOT);
        double answer = 0;
        System.out.println(" ");

        for (String s : myTST.keysWithPrefix(key)) {
            StdOut.println(s);
            answer++;
        }

        if (answer < 1) {
            StdOut.println("The bus stop " + key + " does not exist.");
        }
    }


    // creates a TST for all the data in stops.txt
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
