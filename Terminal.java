package com.company;
import java.io.IOException;
import java.io.File;
import java.nio.file.*;
import java.util.Scanner;
import java.io.*;


public class Terminal {
    Parser parser = new Parser();
    File currentDirectory;

    public Terminal() {
        currentDirectory = new File(System.getProperty("user.home"));

    }

    public String makeAbsolute(String sourcePath) {
        File file = new File(sourcePath);
        String fullPath;
        if (!file.isAbsolute()) {
            fullPath = currentDirectory.getAbsolutePath() + "\\" + sourcePath;
        }else{
            fullPath = sourcePath;
        }
        return fullPath;
    }

    public void cd(){
        currentDirectory = new File(System.getProperty("user.home"));
    }

    public void cd(String sourcePath)throws IOException{
        if(sourcePath.equals("..")){
            String parent = currentDirectory.getParent();
            File file = new File(parent);
            currentDirectory = file.getAbsoluteFile();
        }
        else{
            File file = new File(makeAbsolute(sourcePath));
            if(!file.exists()){
                throw new NoSuchFileException(file.getAbsolutePath(),null,"does not exist");
            }
            if(file.isFile()){
                System.out.println("Can't cd into file");
            }
            else currentDirectory = file.getAbsoluteFile();
        }
    }

    public String pwd()
    {
        return currentDirectory.getAbsolutePath();
    }

    public void ls(){
        String[] arr =  currentDirectory.list();
        if (arr == null) throw new AssertionError();
        for (String s : arr) {
            System.out.println(s);
        }

    }

    public void lsRev(){
        String[] arr =  currentDirectory.list();
        assert arr != null;
        int n = arr.length;
        for(int i = n-1;i >= 0;i--){
            System.out.println(arr[i]);
        }

    }

    public void makeDir(String newDir){

        File file = new File(makeAbsolute(newDir));

        if (file.mkdir()) System.out.println("Directory has been created successfully");

        else System.out.println("Directory cannot be created");
    }

    public void DeleteDir(String DirPath){
        if(DirPath.equals("*"))
        {
            File directory;
            File file = new File(pwd());
            String[] fileList = file.list();
            for(String str : fileList != null ? fileList : new String[0]) {
                directory = new File(makeAbsolute(str));
                if(directory.isDirectory() && directory.delete()) {
                    System.out.println(str+ "Deleted");
                }
            }
        }
        else{
            try {
                File file = new File(makeAbsolute(DirPath));

                if(file.delete()) {
                    System.out.println("Directory Deleted");
                }
                else {
                    System.out.println("Can not Delete this Directory");
                }
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    public void CreateFile(String filepath){

        try {
            File file = new File(makeAbsolute(filepath));
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void copyFile(String sFile,String dFile) throws IOException {
        Path sourceFile = Paths.get(makeAbsolute(sFile));
        Path destinationFile = Paths.get(makeAbsolute(dFile));
        Files.copy(sourceFile, destinationFile);
        System.out.println("Copy finish...");
    }

    public void copyDir(String sDir,String dDir) throws IOException {
        Path sourceDirectory = Paths.get(makeAbsolute(sDir));
        Path destinationDirectory = Paths.get(makeAbsolute(dDir));
        Files.copy(sourceDirectory, destinationDirectory);
        System.out.println("Copy finish...");
    }

    public void RemoveFile(String filepath){

        File file = new File(makeAbsolute(filepath));
        if (file.delete()) {
            System.out.println("File deleted successfully");
        } else {
            System.out.println("Failed to delete the file");
        }

    }

    public void getFileContent(String filepath){
        try {
            File file = new File(makeAbsolute(filepath));
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    public void getFileContent(String filepath, String SecondFilepath) throws IOException {

        File file = new File(makeAbsolute(filepath));
        Scanner myReader = new Scanner(file);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            System.out.println(data);
        }
        myReader.close();

        File file1 = new File(makeAbsolute(SecondFilepath));
        myReader = new Scanner(file1);
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            System.out.println(data);
        }
        myReader.close();
    }

    public void chooseCommandAction() throws IOException {
        while (true)
        {
            Scanner input = new Scanner(System.in);
            String command = input.nextLine().toLowerCase().trim();

            if(parser.parse(command))
            {
                if(parser.getCommandName().equals("echo") && parser.args.length==2)
                {
                    System.out.println(parser.getArgs()[1]);
                }
                else if(parser.getCommandName().equals("pwd") && parser.args.length == 1){
                    System.out.println(pwd());
                }
                else if(parser.getCommandName().equals("cd") && parser.args.length==1){
                    cd();
                }
                else if(parser.getCommandName().equals("cd") && parser.args.length==2){
                    cd(parser.getArgs()[1]);
                }
                else if(command.equals("ls") && parser.args.length==1){
                    ls();
                }
                else if(command.equals("ls-r") && parser.args.length==1){
                    lsRev();
                }
                else if(parser.getCommandName().equals("mkdir")){
                    for (int i=1; i<parser.getArgs().length;i++)
                    {
                        makeDir(parser.getArgs()[i]);
                    }
                }
                else if(parser.getCommandName().equals("rmdir") && parser.args.length==2){
                    DeleteDir(parser.getArgs()[1]);
                }
                else if(parser.getCommandName().equals("rm") && parser.args.length==2){
                    RemoveFile(parser.getArgs()[1]);
                }
                else if(parser.getCommandName().equals("cp") && parser.args.length==3){
                    copyFile(parser.getArgs()[1],parser.getArgs()[2]);
                }
                else if(parser.getCommandName().equals("cp-r") && parser.args.length==3){
                    copyDir(parser.getArgs()[1],parser.getArgs()[2]);
                }
                else if(parser.getCommandName().equals("touch") && parser.args.length==2){
                    CreateFile(parser.getArgs()[1]);
                }
                else if(parser.getCommandName().equals("cat")){
                    if (parser.getArgs().length == 2)
                        getFileContent(parser.getArgs()[1]);
                    else if (parser.getArgs().length == 3)
                        getFileContent(parser.getArgs()[1], parser.getArgs()[2]);
                }
                else if(parser.getCommandName().equals("exit") && parser.args.length==1){
                    break;
                }
                else
                {
                    System.out.println("is not recognized as an internal or external command");
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {

        Terminal mainTerminal = new Terminal();
        mainTerminal.chooseCommandAction();

    }
}
