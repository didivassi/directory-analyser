package academy.mindswap;

import java.io.*;

public class DirectoryAnalyser {

    private BufferedReader consoleReader;
    private BufferedWriter writer;

    public void createStreams() throws IOException {
        consoleReader = new BufferedReader(new InputStreamReader(System.in));
        writer = new BufferedWriter(new FileWriter("resources/list.txt"));
    }

    public void writeDownFiles() throws IOException {
        if (checkNullableStreams()){
            System.out.println(Messages.NO_STREAMS);
            return;
        }

        File directory = askForDirectory();
        System.out.println(Messages.LISTING_MESSAGE);

        for (File directoryFile : directory.listFiles()) {
            writer.write(directoryFile.getName());
            writer.newLine();
            writer.flush();
        }
    }

    public static class FilterFilesByStringName implements FilenameFilter {

        private String startingLetters;

        public FilterFilesByStringName(String startingLetters) {
            this.startingLetters = startingLetters;
        }
        @Override
        public boolean accept(File dir, String name) {
            //System.out.println(name);
            return name.startsWith(startingLetters);
        }
    }

    public static class FilterFilesByStringNameNotDirs implements FileFilter {

        private String startingLetters;

        public FilterFilesByStringNameNotDirs(String startingLetters) {
            this.startingLetters = startingLetters;
        }
        @Override
        public boolean accept(File file) {
            //System.out.println(name);
            return file.getName().startsWith(startingLetters) && !file.isDirectory();
        }
    }

    public void filterFilesByName() throws IOException{
        if (checkNullableStreams()){
            System.out.println(Messages.NO_STREAMS);
            return;
        }
        File directory=askForDirectory();
        String fileToFilter = getUserInput("What's the filename to filter");
       // File[] names=directory.listFiles(new FilterFilesByStringName(fileToFilter));
        File[] names=directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().startsWith(fileToFilter);
            }
        });

        assert names!=null;
        if(names.length==0){
            System.out.printf(Messages.FILE_DOES_NOT_EXIST_CONTAINING_STRING,fileToFilter);
            return;
        }
        for (File name:names) {
            System.out.println(name.getName());
        }
    }

    public void filterFilesByName2() throws IOException{
        if (checkNullableStreams()){
            System.out.println(Messages.NO_STREAMS);
            return;
        }
        File directory=askForDirectory();
        String fileToFilter = getUserInput("What's the filename to filter");
        File[] names=directory.listFiles(new FilterFilesByStringNameNotDirs(fileToFilter));

        assert names!=null;
        if(names.length==0){
            System.out.printf(Messages.FILE_DOES_NOT_EXIST_CONTAINING_STRING,fileToFilter);
            return;
        }
        for (File name:names) {
                System.out.println(name.getName());
        }
    }


    public void checkFilePresence() throws IOException {
        if (checkNullableStreams()){
            System.out.println(Messages.NO_STREAMS);
            return;
        }

        String userInput = getUserInput(Messages.FILE_QUESTION);

        if (new File(userInput).exists()) {
            System.out.println(Messages.FILE_EXISTS);
            return;
        }

        System.out.println(Messages.FILE_DOES_NOT_EXIST);
    }

    public void createFile() throws IOException {
        if (checkNullableStreams()){
            System.out.println(Messages.NO_STREAMS);
            return;
        }

        File directory = askForDirectory();

        String secondUserInput = getUserInput(Messages.FILE_CREATION_QUESTION);

        boolean newFile = new File(directory.getPath() + "/" + secondUserInput).createNewFile();

        System.out.println(newFile ? Messages.FILE_CREATED : Messages.FILE_NOT_CREATED);
    }

    private File askForDirectory() throws IOException {
        String userInput = getUserInput(Messages.DIRECTORY_QUESTION);
        File dir = new File(userInput);

        while (checkIfNotDirectory(dir)) {
            userInput = getUserInput(Messages.DIRECTORY_QUESTION);
            dir = new File(userInput);
        }

        return dir;
    }

    private String getUserInput(String message) throws IOException {
        System.out.println(message);
        return consoleReader.readLine();
    }

    private boolean checkIfNotDirectory(File file) {
        if (!file.isDirectory()) {
            System.out.println(Messages.NOT_DIRECTORY_MESSAGE);
            return true;
        }
        return false;
    }

    public void closeStreams() throws IOException {

        if (consoleReader != null) {
            consoleReader.close();
        }

        if (writer != null) {
            writer.close();
        }
    }

    private boolean checkNullableStreams() {
        return writer == null || consoleReader == null;
    }
}
