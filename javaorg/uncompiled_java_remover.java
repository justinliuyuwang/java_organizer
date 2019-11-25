package javaorg;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TODO make the output files buffers work properly (they dont rn)
//TODO handle exceptions for finding, adding, copying files and folders
//TODO handle nonexistent directories
//TODO make sure there are no spaces in end of directory name input
//TODO make sure it can handle directories with spaces and spaces at end of input
//TODO create output text files work properly
//TODO if there are duplicates going to same package.... save elsewhere or launch both duplicates and compare.. show differences..type yes or no to go forwards and pick one... if no differences then just pick one and proceed (maybe put a copy in the duplicates)... if different then pick one and save one elsewhere with original path info of each

public class uncompiled_java_remover {

    public static void main(String[] args){
            Scanner scanner_object = new Scanner(System.in);


            System.out.println("\n");
            System.out.println("Enter full path of folder containing java files, class files, and subdirectories (e.g. /Users/justinwang/Desktop/test) : ");
            String user_input = scanner_object.nextLine();
            System.out.println("\n");
            System.out.println("You entered: "+user_input);
           // Path output_folder = Paths.get(user_input+"/x_uncompiled_removed_src");
           // System.out.println(user_input+"/x_restructured_src"+"\n");
      //     Path no_package_files = Paths.get(user_input+"/x_restructured_src/no_package_files.txt");
       //     Path packages_seen =  Paths.get(user_input+"/x_restructured_src/packages_seen.txt");

         /*   try{
                Files.createDirectories(output_folder);
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println(e+"\n");
            }*/
/*
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
*/
            try{
                recursion(user_input);
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println(e);
            }

            try{
                recursion_delete(user_input);
            }catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println(e);
        }

    }


    private static void recursion(String current_folder) throws Exception {

        Path dir = Paths.get(current_folder);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path file_path: stream) {

                //System.out.println(file_path.getFileName());

                    //FileReader file_reader = new FileReader(current_folder);

                    if (file_path.toString().contains(".java")) {

                        //Path path = Paths.get("/Users/justinwang/Desktop/test/readlines.java");
                        //write
                        //Files.write(path, "line 1\nline 2\n".getBytes());
                        //read all lines
                       // if Paths.get(current_folder) contains file_path.getFileName().toString()
                        String file_name = file_path.getFileName().toString();
                        file_name = file_name.substring(0,file_name.length()-5);
                        Pattern class_file_pattern =  Pattern.compile(file_name+"(\\$)*"+"(\\d)*"+".class");
                        boolean compiled_flag = false;
                        try (DirectoryStream<Path> second_stream = Files.newDirectoryStream(dir)){  ///go through the directory and look for class files that match the java file
                            for (Path second_file_path: second_stream){
                               // System.out.println(second_file_path.getFileName());
                                //System.out.println(class_file_pattern);
                                //String second_file_name = second_file_path.getFileName().toString().replace(".class","");
                                Matcher m = class_file_pattern.matcher(second_file_path.getFileName().toString());
                                boolean compiled = m.matches();
                                if (compiled == true){
                                    compiled_flag=true;
                                }
                                else{

                                }
                            }
                        }
                        catch (DirectoryIteratorException x) {
                            // IOException can never be thrown by the iteration.
                            // In this snippet, it can only be thrown by newDirectoryStream.
                            System.err.println(x);
                        }
                        if (compiled_flag == false){    //delete the java file if no class files could be found
                            Files.delete(file_path);
                        }

                    } //if java file


                    else if (Files.isDirectory(file_path)){
                        recursion(file_path.toString());
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

    private static void recursion_delete(String current_folder) throws Exception {

        Path dir = Paths.get(current_folder);


        try (DirectoryStream<Path> fourth_stream = Files.newDirectoryStream(dir)){

            for (Path file_path: fourth_stream) {

                if (Files.isDirectory(file_path)){
                    recursion_delete(file_path.toString());
                }   //if subdirectory

                else{
                }
            }
        }
        catch(DirectoryIteratorException x){
            System.err.println(x);
        }

        try (DirectoryStream<Path> third_stream = Files.newDirectoryStream(dir)) {
            if (!third_stream.iterator().hasNext()){
                Files.delete(dir);
            }
            else{
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


