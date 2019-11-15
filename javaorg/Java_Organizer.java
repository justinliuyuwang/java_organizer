package javaorg;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.*;
import java.util.Scanner;
import java.util.List;
import java.nio.file.StandardOpenOption;
import java.nio.charset.StandardCharsets;

import java.io.FileReader;
import javax.swing.*;

//wheres javax
//TODO make the output files buffers work properly (they dont rn)
//TODO handle exceptions for finding, adding, copying files and folders
//TODO handle nonexistent directories
//TODO make sure there are no spaces in end of directory name input
//TODO make sure it can handle directories with spaces and spaces at end of input
//TODO make sure both text files work properly

public class Java_Organizer {

    public static void main(String[] args){
            Scanner scanner_object = new Scanner(System.in);


            System.out.println("\n");
            System.out.println("Enter full path of folder containing java files and subdirectories (e.g. /Users/justinwang/Desktop/test) : ");
            String user_input = scanner_object.nextLine();
            System.out.println("\n");
            System.out.println("You entered: "+user_input);
            Path output_folder = Paths.get(user_input+"/x_restructured_src");
           // System.out.println(user_input+"/x_restructured_src"+"\n");
            Path no_package_files = Paths.get(user_input+"/x_restructured_src/no_package_files.txt");
            Path packages_seen =  Paths.get(user_input+"/x_restructured_src/packages_seen.txt");

            try{
                Files.createDirectories(output_folder);
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println(e+"\n");
            }

            try{
                Files.createFile(no_package_files);
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println(e);
            }

            try{
                Files.createFile(packages_seen);
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println(e);
            }

            try{
                recursion(user_input,user_input);
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println(e);
            }

    }


//if there are duplicates going to same package.... save elsewhere or launch both duplicates and compare.. show differences..type yes or no to go forwards and pick one... if no differences then just pick one and proceed (maybe put a copy in the duplicates)... if different then pick one and save one elsewhere with original path info of each
    private static void recursion(String current_folder, String root_folder) throws Exception {

        Path dir = Paths.get(current_folder);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path file_path: stream) {

                System.out.println(file_path.getFileName());

                    //FileReader file_reader = new FileReader(current_folder);

                    if (file_path.toString().contains(".java")) {

                        //Path path = Paths.get("/Users/justinwang/Desktop/test/readlines.java");
                        //write
                        //Files.write(path, "line 1\nline 2\n".getBytes());
                        //read all lines

                        List<String> lines = Files.readAllLines(file_path);
                        int flag;
                        flag = 0;
                        for (String line : lines) {
                            if (line.contains("package")) {

                                line = line.replace("package", "");     //make package into directory format
                                line = line.replace(" ", "");
                                line = line.replace(";", "");
                                line = line.replace(".","/");
                                System.out.println(line);
                                //  concat line to text file of packages;

                                BufferedWriter buffered_writer = Files.newBufferedWriter(Paths.get(root_folder+"/x_restructured_src/packages_seen.txt"), StandardCharsets.UTF_8, StandardOpenOption.WRITE);
                                buffered_writer.write(line+"\n");
                                buffered_writer.close();

                                flag = 1;

                               // if (Files.exists(Paths.get(root_folder+line))==false){
                               //     create it
                               // }
                               // else{
                               // }
                                //don't need to check for existing folders
                                Path new_directories = Paths.get(root_folder+"/x_restructured_src/"+line);

                                try {
                                    Files.createDirectories(new_directories);
                                }
                                catch(Exception e){
                                    System.out.println(e.getMessage());
                                    System.out.println(e);
                                }

                                String file_name = file_path.getFileName().toString();
                                Path new_file_path = Paths.get(root_folder+"/x_restructured_src/"+line+"/"+file_name);

                                try {
                                    Files.copy(file_path, new_file_path);
                                }
                                catch(Exception e){
                                    System.out.println(e.getMessage());
                                    System.out.println(e);
                                }

                                break;
                            } else {
                                // do nothing
                            }

                        }
                        if (flag == 0) {                             //if java file doesnt contain package
                            BufferedWriter buffered_writer = Files.newBufferedWriter(Paths.get(root_folder+"/x_restructured_src/no_package_files.txt"), StandardCharsets.UTF_8, StandardOpenOption.WRITE);
                            buffered_writer.write(file_path.toString()+"\n");
                            buffered_writer.close();

                            //concatn path to text file of package-less files;
                        }
                    } //if java file


                    else if (Files.isDirectory(file_path)){
                        recursion(file_path.toString(), root_folder);
                    }   //if subdirectory


                    else{
                    }                                      //if neither then do nothing

            }
        }
        catch (DirectoryIteratorException x) {
            // IOException can never be thrown by the iteration.
            // In this snippet, it can only be thrown by newDirectoryStream.
            System.err.println(x);
        }

        return;
    }


}


