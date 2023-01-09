import java.util.*;
import javax.swing.*;
import java.io.*;
import java.nio.file.*;

public class grading_main {
    //location to save the file
    public static String location = "c:/test/final_project-Copy/text files/";

    public static void addStudent(String className) {
        Scanner scn = new Scanner(System.in);
        double[] hw = {};
        double hwTotal = 0;
        double[] test = {};
        double testTotal = 0;
        double overall = 0;
        String name;
        String id;
        String letterG;
        int hwReturned;
        int testTaken;
        
        System.out.println("Enter student's name: ");
        name = scn.nextLine();

        System.out.println("Enter student's id: ");
        id = scn.nextLine();

        System.out.println("How many homeworks returned?");
        hwReturned = scn.nextInt();
        if (hwReturned <= 100) {
            if (hwReturned != 0){
                double[] hwGrades = new double [hwReturned];
                for (int i = 0; i<hwReturned; i++) {
                    System.out.println("Enter the grade of homework returned " + (i+1)+": ");
                    hwGrades[i] = scn.nextDouble();                
                }
                
                hw = hwGrades.clone();
                for (double i: hwGrades){
                    hwTotal += i;
                }
            } else {
                System.out.println("Invalid input.");
            }
        } else {
            System.out.println("Error. The score should be under 100.");
        }

        System.out.println("How many tests taken? ");
        testTaken = scn.nextInt();
        if(testTaken <= 100) {
            if (testTaken != 0) {
                double[] testGrade = new double [testTaken];
                for (int i = 0; i < testTaken; i++) {
                    System.out.println("Enter the grade of test taken " + (i+1) + ": ");
                    testGrade[i] = scn.nextDouble();
                }
                test = testGrade.clone();
                for (double i: testGrade) {
                    testTotal += i;
                }
            } else {
                System.out.println("Invalid input.");
            }
        } else {
            System.out.println("Error. The score should be under 100.");
        }
        
        overall = ((hwTotal/hwReturned) + (testTotal/testTaken)) / 2; 
        letterG = letterGrade(overall);

        createFile(className, name, id, hw, test, overall, letterG);
    }

    public static void createFile (String className, String name, String id, double[] hw, double[] test, double overall, String letterG) {
        //create className.txt
        File courseFile;
        courseFile = new File(location + className + ".txt");   
        try {
            if(!courseFile.exists()){
                FileWriter writer = new FileWriter(courseFile, true); 
                writer.write("Name / ID\n");
                writer.write(name + " " + id + "\n");
                writer.close();
            } else {
                FileWriter writer = new FileWriter(courseFile, true); 
                writer.write(name + " " + id + "\n");
                writer.close(); 
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error!");
        }
        
        //create className_studentName.txt
        File studentFile;
        studentFile = new File(location + className + "_" + name + ".txt");   
        
        //write id
        try {
            FileWriter writer = new FileWriter(studentFile, true); 
            writer.write("Name: " + name + "\n");
            writer.write("ID: " + id + "\n");
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error!");
        }

        //write hw grades
        try {
            FileWriter writer = new FileWriter(studentFile, true); 
            writer.write("\nGrade: \n");
            for (int i = 0; i < hw.length; i++){
                int numbering = i+1;
                writer.write("Homework " + numbering + " : " + hw[i] + "\n");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error writing hw1!");
        }

        //write test grades
        try {
            FileWriter writer = new FileWriter(studentFile, true); 
            writer.write("\n");
            for (int i = 0; i < test.length; i++){
                int numbering = i+1;
                writer.write("Test " + numbering + " : " + test[i] + "\n");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error writing test!");
        }

        //write overall grade
        try {
            FileWriter writer = new FileWriter(studentFile, true); 
            writer.write("\nThe overall grade of this course is: " + overall + "\n");
            writer.write("The letter grade is: " + letterG);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("error writing test!");
        }
    }

    public static void updateStudent(String className, String sName, String id) {
        Scanner scn = new Scanner(System.in);
        String newID = "";

        System.out.println("Update(enter the number)\n1. name\n2. ID");
        String desire = scn.nextLine();

        String update1 = "";
        //update student name
        if(desire.equals("1")){
            System.out.println("Enter the new Student's name: ");
            sName = scn.nextLine();
            update1 = sName + " " + id;
        } 
        // update student id
        else if (desire.equals("2")){
            System.out.println("Enter the new Student's id: ");
            newID = scn.nextLine();
            update1 = sName + " " + newID;
        } else {
            System.out.println("Error! Invalid input");
        }

        try {
            //update the className.txt file
            File courseFile = new File(location + className + ".txt");
            File tempFile = new File(location + className + "_temp.txt");
            BufferedReader reader = new BufferedReader(new FileReader(courseFile));
            FileWriter writer = new FileWriter(tempFile, true); 
            String currentLine;
            String target = sName + " " + id;
            while ((currentLine = reader.readLine()) != null){
                if (!(currentLine.equals(target))){
                    writer.write(currentLine + "\n");
                } else {
                    writer.write(update1 + "\n");
                }
            }
            writer.close(); 
            reader.close(); 

            //delete the old className.txt and create new
            courseFile.delete();
            Path source = Paths.get(location + className + "_temp.txt");
            Files.move(source, source.resolveSibling(className+".txt"));
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error with delete the record in " + className + " file.");
        }

        //change className_studentName.txt
        try {
            //update the className_studentName.txt file
            File studentFile = new File(location + className + "_" + sName + ".txt");
            File tempFile = new File(location + className + "_" + sName + "_temp.txt");
            BufferedReader reader = new BufferedReader(new FileReader(studentFile));
            FileWriter writer = new FileWriter(tempFile, true); 
            
            String currentLine;
            String target = "";
            String update2 = "";
            if (desire.equals("1")){
                target = "Name: " + sName;
                update2 = "Name: " + sName;
            } else if (desire.equals("2")){
                target = "ID: " + id;
                update2 = "ID: " + newID;
            } else {
                System.out.println("Error! Invalid input");
            }

            while ((currentLine = reader.readLine()) != null){
                if (!(currentLine.equals(target))){
                    writer.write(currentLine + "\n");
                } else {
                    writer.write(update2 + "\n");
                }
            }
            writer.close(); 
            reader.close(); 

            //delete the old className_studentName.txt and create new
            studentFile.delete();
            Path source = Paths.get(location + className + "_" + sName + "_temp.txt");
            Files.move(source, source.resolveSibling(className + "_" + sName + ".txt"));
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error with delete the record in " + className + "_" + sName + " file.");
        }

    }

    public static void updateGrade(String className, String sName, String id){
        Scanner scn = new Scanner(System.in);
        String currentLine;
        double newHWOverall = 0;
        double newTestOverall = 0;
        double overall = 0;
        String targetGrade = "The overall grade of this course is: ";
        String targetLetter = "The letter grade is: ";

        ArrayList<Integer> num_hw = new ArrayList<Integer>();
        ArrayList<Double> g_hw = new ArrayList<Double>();
        num_hw.add(1);

        ArrayList<Integer> num_test = new ArrayList<Integer>();
        ArrayList<Double> g_test = new ArrayList<Double>();
        num_test.add(1);

        File studentFile = new File(location + className + "_" + sName + ".txt");
        File tempFile = new File(location + className + "_" + sName + "_temp.txt");
        BufferedReader reader;
        FileWriter writer;
            
        System.out.println("Enter the number\n1. Homework grade\n2. Test grade");
        String desire = scn.nextLine();
        
        //update hw grade
        if (desire.equals("1")){
            System.out.println("Enter the Homework number to change (example: Homework 1 -> 1)");
            String HWTarget = scn.nextLine();
            System.out.println("Enter the new grade: ");
            double newHWGrade = scn.nextDouble();
            
            try{
                String target = "Homework " + HWTarget + " : ";
                String update = "Homework " + HWTarget + " : " + newHWGrade;
                reader = new BufferedReader(new FileReader(studentFile));
                writer = new FileWriter(tempFile, true); 
                while ((currentLine = reader.readLine()) != null){
                    //if the line start with "Homework n : "
                    if (currentLine.startsWith("Homework ")){
                        if (!(currentLine.startsWith(target))) {
                            writer.write(currentLine + "\n");
                            //if n's first number is 1 (only for first time)
                            if (num_hw.get(num_hw.size()-1) == 1){
                                String temp = currentLine.substring(currentLine.length() - 4);
                                g_hw.add(Double.parseDouble(temp));
                                num_hw.add(num_hw.size() + 1);
                            }
                            //if n's first number is not 1.  
                            else if (num_hw.get(num_hw.size()-1) > 1){
                                String temp = currentLine.substring(currentLine.length() - 4);
                                g_hw.add(Double.parseDouble(temp));
                                num_hw.add(num_hw.size() + 1);
                            }
                        } else {
                            writer.write(update + "\n");
                            if (num_hw.get(num_hw.size()-1) == 1){
                                String temp = update.substring(update.length() - 4);
                                g_hw.add(Double.parseDouble(temp));
                                num_hw.add(num_hw.size() + 1);
                            } else if (num_hw.get(num_hw.size()-1) > 1){
                                String temp = update.substring(update.length() - 4);
                                g_hw.add(Double.parseDouble(temp));
                                num_hw.add(num_hw.size() + 1);
                            }
                        }
                    } 
                    //if the line starts with "Test n : "
                    else if (currentLine.startsWith("Test ")) {
                        if (num_test.get(num_test.size()-1) == 1){
                            writer.write(currentLine + "\n");
                            String temp = update.substring(update.length() - 4);
                            g_test.add(Double.parseDouble(temp));
                            num_test.add(num_test.size() + 1);
                        } else if (num_test.get(num_test.size()-1) > 1){
                            writer.write(currentLine + "\n");
                            String temp = update.substring(update.length() - 4);
                            g_test.add(Double.parseDouble(temp));
                            num_test.add(num_test.size() + 1);
                        }
                    }
                    //if the line does not start with "Homework n : " or "Test n : "
                    else {
                        if (currentLine.startsWith(targetGrade)){
                            double HWtotal = 0;
                            double testTotal = 0;

                            for (int t = 0; t < g_hw.size(); t++){
                                HWtotal += g_hw.get(t);
                            }
                            int HWDividen = num_hw.size() - 1;
                            newHWOverall = HWtotal/Double.valueOf(HWDividen);
                            System.out.println("hw total " + HWtotal);
                            System.out.println("hw overall " + newHWOverall);

                            for (int t = 0; t < g_test.size(); t++){
                                testTotal += g_test.get(t);
                            }
                            int testDividen = num_test.size() - 1;
                            newTestOverall = testTotal/Double.valueOf(testDividen);

                            overall = (newTestOverall + newHWOverall)/2;
                            writer.write(targetGrade + overall + "\n");
                            System.out.println("The new overall grade is: " + overall);
                        } else if (currentLine.startsWith(targetLetter)) {
                            String letterG = letterGrade(overall);
                            writer.write(targetLetter + letterG + "\n");
                        } else {
                            writer.write(currentLine + "\n");
                        }
                        
                    }
                }
                writer.close(); 
                reader.close(); 
    
                //delete the old className_studentName.txt and create new
                studentFile.delete();
                Path source = Paths.get(location + className + "_" + sName + "_temp.txt");
                Files.move(source, source.resolveSibling(className + "_" + sName + ".txt"));
            } catch(Exception e) {
                e.printStackTrace();
                System.out.println("Error with delete the record in " + className + "_" + sName + " file.");
            }
        }

        //update test grade
        else if (desire.equals("2")){
            System.out.println("Enter the Test number to change/n(example: Teset 1 -> 1)");
            String TestTarget = scn.nextLine();
            System.out.println("Enter the new grade: ");
            double newTestGrade = scn.nextDouble();

            try{
                String target = "Test " + TestTarget + " : ";
                String update = "Test " + TestTarget + " : " + newTestGrade;
                reader = new BufferedReader(new FileReader(studentFile));
                writer = new FileWriter(tempFile, true); 
                while ((currentLine = reader.readLine()) != null){
                    //if the line start with "Test n : "
                    if (currentLine.startsWith("Test ")){
                        if (!(currentLine.startsWith(target))) {
                            writer.write(currentLine + "\n");
                            //if n's first number is 1 (only for first time)
                            if (num_test.get(num_test.size()-1) == 1){
                                String temp = currentLine.substring(currentLine.length() - 4);
                                g_test.add(Double.parseDouble(temp));
                            }
                            //if n's first number is not 1.  
                            else if (num_test.get(num_test.size()-1) > 1){
                                String temp = currentLine.substring(currentLine.length() - 4);
                                g_test.add(Double.parseDouble(temp));
                                num_test.add(num_test.size() + 1);
                            }
                        } else {
                            writer.write(update + "\n");
                            if (num_test.get(num_test.size()-1) == 1){
                                String temp = update.substring(update.length() - 4);
                                g_test.add(Double.parseDouble(temp));
                                num_test.add(num_test.size() + 1);
                            } else if (num_test.get(num_test.size()-1) > 1){
                                String temp = update.substring(update.length() - 4);
                                g_test.add(Double.parseDouble(temp));
                                num_test.add(num_test.size() + 1);
                            }
                        }
                    } 
                    //if the line starts with "Homework n : "
                    else if (currentLine.startsWith("Homework ")) {
                        if (num_hw.get(num_hw.size()-1) == 1){
                            writer.write(currentLine + "\n");
                            String temp = update.substring(update.length() - 4);
                            g_hw.add(Double.parseDouble(temp));
                            num_hw.add(num_hw.size() + 1);
                        } else if (num_hw.get(num_hw.size()-1) > 1){
                            writer.write(currentLine + "\n");
                            String temp = update.substring(update.length() - 4);
                            g_hw.add(Double.parseDouble(temp));
                            num_hw.add(num_hw.size() + 1);
                        }
                    } 
                    
                     
                    //if the line does not start with "Homework n : " and "Test n : "
                    else {
                        if (currentLine.startsWith(targetGrade)){
                            double HWtotal = 0;
                            double testTotal = 0;

                            for (int t = 0; t < g_hw.size(); t++){
                                HWtotal += g_hw.get(t);
                            }
                            int HWDividen = num_hw.size() - 1;
                            newHWOverall = HWtotal/Double.valueOf(HWDividen);

                            for (int t = 0; t < g_test.size(); t++){
                                testTotal += g_test.get(t);
                            }
                            int testDividen = num_test.size() - 1;
                            newTestOverall = testTotal/Double.valueOf(testDividen);

                            overall = (newTestOverall + newHWOverall)/2;
                            writer.write(targetGrade + overall + "\n");
                            System.out.println("The new overall grade is:" + overall);
                        } else if (currentLine.startsWith(targetLetter)) {
                            String letterG = letterGrade(overall);
                            writer.write(targetLetter + letterG + "\n");
                        } else {
                            writer.write(currentLine + "\n");
                        }
                    }
                }
                writer.close(); 
                reader.close(); 
    
                //delete the old className_studentName.txt and create new
                studentFile.delete();
                Path source = Paths.get(location + className + "_" + sName + "_temp.txt");
                Files.move(source, source.resolveSibling(className + "_" + sName + ".txt"));
            } catch(Exception e) {
                e.printStackTrace();
                System.out.println("Error with delete the record in " + className + "_" + sName + " file.");
            }
        }
    }

    public static void deleteStudent(String className) {
        Scanner scn = new Scanner(System.in);
        System.out.println("Enter the Student's name to delete in " + className + " class: ");
        String sName = scn.nextLine();

        System.out.println("Enter the Student's ID: ");
        String id = scn.nextLine();

        //delete className_studentName.txt 
        File studentFile = new File(location + className + "_" + sName + ".txt");
        if (studentFile.delete()) {
            System.out.println("The " + className + "_" + sName + " is deleted.");
        } else {
            System.out.println("Unable to delete the student file");
        }

        //delete the record in className.txt
        File courseFile = new File(location + className + ".txt");
        File tempFile = new File(location + className + "_temp.txt");

        try {
            BufferedReader reader = new BufferedReader(new FileReader(courseFile));
            FileWriter writer = new FileWriter(tempFile, true); 

            String currentLine;
            String target = sName + " " + id;
            while ((currentLine = reader.readLine()) != null){
                if (!(currentLine.equals(target))){
                    writer.write(currentLine + "\n");
                }
            }
            writer.close(); 
            reader.close(); 

            //delete the old className.txt and create new
            courseFile.delete();
            Path source = Paths.get(location + className + "_temp.txt");
            Files.move(source, source.resolveSibling(className+".txt"));
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("Error with delete the record in " + className + " file.");
        }
    }

    public static void searchStudent(String className) {
        Scanner scn = new Scanner(System.in);
        System.out.println("Return the name of student looking for: ");
        String sName = scn.nextLine();

        File file = new File(location + className + "_" + sName + ".txt");
        try{
            Scanner reader = new Scanner(file);
            while (reader.hasNextLine()) {
                System.out.println(reader.nextLine());
            } 
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error! Such file does not exist");
        }
    }

    public static void viewClassOverall(String className) {
        ArrayList<Integer> num_student = new ArrayList<Integer>();
        ArrayList<Double> grade = new ArrayList<Double>();
        ArrayList<String> sName = new ArrayList<String>();
        num_student.add(1);

        double overall = 0;
        double total = 0;
        String currentLine;
        File courseFile = new File(location + className + ".txt");
        try {
            BufferedReader cReader = new BufferedReader(new FileReader(courseFile));
            while ((currentLine = cReader.readLine()) != null) {
                if(!(currentLine.equals("Name / ID"))) {
                    sName.add(currentLine.split(" ")[0]);
                    System.out.println(currentLine.split(" ") [0]);
                }
            }
            //read all student file of className course and get the total grade of the student
            for (int i = 0; i < sName.size(); i++) {
                String currentName = sName.get(i);
                File studentFile = new File(location + className + "_"+ currentName + ".txt");
                BufferedReader sReader = new BufferedReader(new FileReader(studentFile));
                while ((currentLine = sReader.readLine()) != null) {
                    if(currentLine.startsWith("The overall grade of this course is: ")){
                        String temp = currentLine.substring(currentLine.lastIndexOf(" ") + 1);
                        grade.add(Double.parseDouble(temp));
                    }
                }
            }
            //add all the student's overall grade
            for (int i = 0; i < grade.size(); i++){
                total += grade.get(i);
            }
            overall = total / sName.size();
            String letterG = letterGrade(overall);
            System.out.println("The average of overall grade of this course is: " + overall);
            System.out.println("The average of letter grade is: " + letterG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String letterGrade(double d) {
        String letter = "";
        if(100.0 >= d && d > 93.0) {
            letter = "A";
        }
        else if (93.0 >= d && d > 90.0) {
            letter = "A-";
        }
        else if (90.0 >= d && d > 87.0) {
            letter = "B+";
        }
        else if (87.0 >= d && d > 83.0) {
            letter = "B";
        }
        else if (83.0 >= d && d > 80.0) {
            letter = "B-";
        }
        else if (80.0 >= d && d > 77.0) {
            letter = "C+";
        }
        else if (77.0 >= d && d > 73.0) {
            letter = "C";
        }
        else if (73.0 >= d && d > 70.0) {
            letter = "C-";
        }
        else if (70.0 >= d && d > 67.0) {
            letter = "D+";
        }
        else if (67.0 >= d && d > 63.0) {
            letter = "D";
        }
        else if (63.0 >= d && d > 60.0) {
            letter = "D-";
        }
        else {
            letter = "F";
        }
        System.out.println("The letter grade is : " + letter);
        return letter;
    }

    public static void main(String[] args){
        Scanner scn = new Scanner(System.in);
        String UserChoice = "";
        String className = "";

        while(true){
            System.out.println("Choose class from the list (Use the number):\n"+
            "1. CSET2200\n" + 
            "2. CSET3200\n" +
            "3. CSET4250");
            className = scn.nextLine();
            if(className.equals("1")){
                className ="CSET2200";
            } else if (className.equals("2")){
                className = "CSET3200";
            } else if (className.equals("3")) {
                className = "CSET4250";
            } else {
                System.out.println("Invalid input");
                System.exit(0);
            }
    
            while (!className.equals("")){
                System.out.println("Choose option below to edit "+className+" :\n"+
                    "1. Add student\n" +
                    "2. Update Record\n" + 
                    "3. Search Record\n" + 
                    "4. Delete Record\n" +
                    "5. View class Overall\n" +
                    "6. Change class\n" +
                    "x. Exit");
                UserChoice = scn.nextLine();
                switch(UserChoice){
                    case "1":
                        addStudent(className);
                        break;
                    case "2": 
                        System.out.println("Enter the Student's name to change: ");
                        String sName = scn.nextLine();
                        System.out.println("Enter the Student's ID: ");
                        String id = scn.nextLine();
                        System.out.println("1. Update Student/ID\n2. Update grade");
                        String s = scn.nextLine();
                        if(s.equals("1")){
                            updateStudent(className, sName, id);
                        } else if (s.equals("2")){
                            updateGrade(className, sName, id);
                        }
                        
                        break;
                    case "3": 
                        searchStudent(className);
                        break;
                    case "4":
                        deleteStudent(className);
                        break;
                    case "5":
                        viewClassOverall(className);
                        break;
                    case "6":
                        className = "";
                        break;
                    case "x":
                        System.exit(0);
                }
            }    
        }
            
    }
}