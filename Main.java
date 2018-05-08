

/*
 * Copyright (c) 2018 by Perrakis Stylianos-(2401)
 * 25/4/2018
 */

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.pow;

class txtParser {
    /**
     * @param f is the file.
     * @param x List<dotData> which contains x.
     */
    private File f;
    private List<dotData> x;
    private long time;

    public txtParser() {

        this.f = null;
    }

    /**
     * Read : Reads the Integers from the file and storing it to a list.
     * My parser in order to read faster.
     * @param name
     */
    public void Read(String name) throws IOException {
        time = System.currentTimeMillis();
        try {
            f = new File(name);
        } catch (Exception e) {
            System.out.println("Error :File not Found");
            System.out.println("Location:"+name);
            System.exit(-1);
        }
        BufferedReader br = new BufferedReader(new FileReader(f));
        String line= br.readLine();
        int a  = Integer.parseInt(line);
        this.x = Stream.generate(dotData::new).limit(a).collect(Collectors.toList());
        int i = 0;
        int xx,yy;
        while (i<a) {
            line = new String(br.readLine());
            boolean xy = true;
            xx = 0;
            yy = 0;
            int digit = 0;
            for (int it=0 ; it<line.length();it++){
                char omg = line.charAt(it);
                if (omg>47&&omg<=58){
                    int ax = omg-48;
                    if (xy){
                        xx = (int) (xx*(10));
                        digit = digit+1;
                        xx +=ax;

                    }
                    else{
                        yy = (int) (yy*(10));
                        digit = digit+1;
                        yy +=ax;

                    }
                }
                else{
                    xy = !xy;
                }
            }
            x.get(i).setDatax(xx);
            x.get(i).setDatay(yy);
            i++;
        }
        time = System.currentTimeMillis() - time;
        //System.out.println((System.currentTimeMillis()-time));
    }

    public long getTime() {
        return time;
    }

    public List<dotData> getX() {
        return (ArrayList<dotData>) x;
    }


}



/*
 * Copyright (c) 2018 by Perrakis Stylianos-(2401)
 * 25/4/2018
 */
class dotData {
    /**
     * @param datax is the x value  of a point.
     * @param datay is the y value of a point.
     */
    private int datax, datay;

    public dotData() {
        this.datax = 0;
        this.datay = 0;
    }
    public dotData(dotData a) {
        this.datax = a.getDatax();
        this.datay = a.getDatay();
    }
    public int getDatax() {
        return datax;
    }

    public int getDatay() {
        return datay;
    }

    public void setDatax(int datax) {
        this.datax = datax;
    }

    public void setDatay(int datay) {
        this.datay = datay;
    }
}

/*
 * Copyright (c) 2018 by Perrakis Stylianos-(2401)
 * 25/4/2018
 */
class OpperationSkyline {
    /**
     * @param data is the ArrayList of data which contains dotData.
     * @param answer is the ArrayList of answer which contains dotData.
     * @param path is the path of the file.
     * @param time counts the time.
     */
    private ArrayList<dotData> data;//ArrayList of all points.
    private ArrayList<dotData> answer;
    private txtParser file;
    private String path; // path of the file is saved here
    private long time;

    /**
     * Constructor
     *
     * @param path is the String of the file we chose for Skyline.
     */
    public OpperationSkyline(String path) {
        file = new txtParser();
        this.path = path;
        this.answer = new ArrayList<dotData>();
        this.data = new ArrayList<dotData>();
    }

    public txtParser getFile() {
        return file;
    }

    public long getTime() {
        return time;
    }

    public ArrayList<dotData> getData() {
        return data;
    }

    public ArrayList<dotData> getAnswer() {
        return answer;
    }

    /**
     * Starting the process.
     */
    public void start() throws IOException {
        this.file.Read(path);
        data.addAll(file.getX());
        this.time = System.currentTimeMillis(); //set Timer.
        this.answer.addAll(skylineDc(data));
        this.time = System.currentTimeMillis() - time;
    }


    /**
     * skylineDC : Calculating Skyline.
     * O(log(n))
     * @param data : array of the points
     * @return ArrayList of points
     */

    private ArrayList<dotData> skylineDc(ArrayList<dotData> data) {
        int n = data.size();
        if (n <= 2 && n > 0) {
            data.remove(domination(data));
        } else if (n > 2) {
            int median = (n % 2) == 0 ? n / 2 : (n - 1) / 2;
            ArrayList left = new ArrayList(data.subList(0, median));
            ArrayList right = new ArrayList(data.subList(median, data.size()));
            return merge(skylineDc(left), skylineDc(right));
        }
        return data;
    }

    /**
     * merge : merges two lists of dotData elements.
     * O(n)
     * @param left  ArrayList<dotData>
     * @param right ArrayList<dotData>
     * @return ArrayList<dotData> merging left - right
     */
    private ArrayList<dotData> merge(ArrayList<dotData> left, ArrayList<dotData> right) {
        ArrayList<dotData> localAns = new ArrayList<>(left);
        localAns.addAll(right); //Adds the two arrays to one.
        int i = 0, j = 0, size = localAns.size(),counter=0;

        while (i < size && !localAns.isEmpty()) {

            dotData d1 = localAns.get(i), d2 = localAns.get(j);
            if (d1.getDatax() != d2.getDatax() || d1.getDatay() != d2.getDatay()) {
                if (d1.getDatax() <= d2.getDatax() && d1.getDatay() <= d2.getDatay()) {
                    localAns.remove(d2);
                    size = localAns.size();
                } else if (d2.getDatax() <= d1.getDatax() && d2.getDatay() <= d1.getDatay()) {
                    localAns.remove(d1);
                    size = localAns.size();
                    counter++;
                } else {
                    j++;
                } //domination over two.
            } else {
                if (j != i)
                    localAns.remove(d2);
                size = localAns.size();
                j++;
            }
            if (j >= size) {
                j = 0;
                if (counter==0)
                i++;
                counter =0;
            }
        }
        return localAns;
    }

    /**
     * domination : finds if a element is dominated by b and opposite.
     *
     * @param data1 type: dotData
     * @param data2 type: dotData
     * @return The data that is to be removed
     */
    private boolean domination(dotData data1, dotData data2) {
        int x1 = data1.getDatax(), x2 = data2.getDatax(), y1 = data1.getDatay(), y2 = data2.getDatay();
        if ((x1 != x2) && (y1 != y2)) {
            if (x1 <= x2 && y1 <= y2) {
                return true;
            }
        }
        return false;
    }

    /**
     * domination : finds if a element is dominated by b and opposite.
     *
     * @param data type dotData of two points
     * @return Indicates which is for remove
     */
    private dotData domination(ArrayList<dotData> data) {
        int show;
        if (data.size() > 1) {
            int x1 = data.get(0).getDatax(), x2 = data.get(1).getDatax(), y1 = data.get(0).getDatay(), y2 = data.get(1).getDatay();
            if ((x1 != x2) && (y1 != y2)) {
                if (x1 <= x2 && y1 <= y2) {
                    return data.get(1);
                } else if (x1 >= x2 && y1 >= y2) {
                    return data.get(0);
                }

            }
        }
        return null;
    }

}


/*
 * Copyright (c) 2018 by Perrakis Stylianos-(2401)
 * 25/4/2018
 */
public class Main {
    public static void main(String[] args) throws IOException {
        String input = args[0];
        input = input.substring(1,input.length()-1);
        URL location = Main.class.getProtectionDomain().getCodeSource().getLocation();
        String path = location.getPath();
        int counter = 0 ;
        path = path.substring(0, path.length() - 1);
        while(path.charAt(path.length()-1)!=(char) 47)
        {
            path = path.substring(0, path.length() - 1);
            counter = counter+1;
        }

        String name = path + "data" + File.separator + input;
        OpperationSkyline item = new OpperationSkyline(name);
        item.start();//Starting the Process
        System.out.println("Without loss of generality Skyline is :");
        for (dotData i : item.getAnswer()) {
            System.out.println(i.getDatax() + "  " + i.getDatay());
        }
        System.out.println("Execution time of Reading: " + item.getFile().getTime() + " ms");//Show total execution Time.
        System.out.println("Execution time of Skyline : " + item.getTime() + " ms");//Show total execution Time.
        System.out.println("Total time : " + (item.getFile().getTime() + item.getTime()) + " ms");
    }
}