package youareell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import controllers.IdController;
import controllers.MessageController;
import controllers.ServerController;
import controllers.TransactionController;

// URLShell is a Console view for youareell.YouAreEll.
public class URLShell {

    private List<String> commandInput;

    public List<String> getCommandInput() {
        return commandInput;
    }

    public static void prettyPrint(String output) {
        // yep, make an effort to format things nicely, eh?
        System.out.println(output);
    }

    public static void main(String[] args) throws java.io.IOException {
        new URLShell().run();
    }

    public void run() throws IOException {
        YouAreEll urll = new YouAreEll(new TransactionController(new MessageController(ServerController.shared()), 
        new IdController(ServerController.shared())));
        
        String commandLine;
        BufferedReader console = new BufferedReader
                (new InputStreamReader(System.in));

        ProcessBuilder pb = new ProcessBuilder();
        List<String> history = new ArrayList<String>();
        int index = 0;
        //we break out with <ctrl c>
        while (true) {
            //read what the user enters
            System.out.println("\u001B[35mWelcome to YouAreEll \n" +
                    "If you need help, then type Help \n" +
                    "\nType in your commands: \u001B[0m ");
            commandLine = console.readLine();

            //input parsed into array of strings(command and arguments)
            String[] commands = commandLine.split(" ");
            List<String> list = new ArrayList<String>();
            commandInput = list;

            //if the user entered a return, just loop again
            if (commandLine.equals(""))
                continue;
            if (commandLine.equals("exit")) {
                System.out.println("\n\u001B[33mBye!\n");
                break;
            }

            //loop through to see if parsing worked
            for (int i = 0; i < commands.length; i++) {
                //System.out.println(commands[i]); //***check to see if parsing/split worked***
                list.add(commands[i]);

            }
            //System.out.print(list); //***check to see if list was added correctly***
            history.add(commandLine);
            try {
                //display history of shell with index
                if (list.get(list.size() - 1).equals("history")) {
                    for (String s : history)
                        System.out.println((index++) + " " + s);
                    continue;
                }

                // Specific Commands.

                // ids

                if (list.get(0).equals("help") || list.get(0).equals("Help")){
                    String s = "\u001B[32mID help: \u001B[0m\n" +
                            "Register an ID:\n" +
                            "ids your_name your_github_handle \n" +
                            "\n" +
                            "List of all IDs: \n" +
                            "ids\n" +
                            "\n" +
                            "Update existing ID: \n" +
                            "ids your_updated_name your_github_handle \n" +
                            "\n" +
                            "\u001B[32mMessaging help:\u001B[0m\n" +
                            "Send a message: \n" +
                            "messages your_github_handle their_github_handle type your message\n" +
                            "\n" +
                            "See messages directed at user:\n" +
                            "messages your_github_handle\n" +
                            "\n" +
                            "See global messages\n" +
                            "messages\n";
                    URLShell.prettyPrint(s);
                    continue;
                }

                if (list.get(0).equals("ids") && list.size() == 3) {
                    String results = urll.postOrPutId(list.get(1), list.get(2));
                    URLShell.prettyPrint(results);
                    continue;
                }

                if (list.get(0).equals("ids")) {
                    String results = urll.get_ids();
                    URLShell.prettyPrint(results);
                    continue;
                }

                if (list.get(0).equals("messages") && list.size() == 2){
                    String results = urll.get_messages(list.get(1));
                    URLShell.prettyPrint(results);
                    continue;
                }

                if (list.get(0).equals("messages") && (list.size() > 3)){
                    StringBuilder sb = new StringBuilder();
                    for (int i =3; i< list.size(); i++){
                        sb.append(list.get(i) + " ");
                    }

                    String results = urll.postMessage(list.get(1), list.get(2), sb.toString());
                    URLShell.prettyPrint(results);
                    continue;
                }

                // messages
                if (list.get(0).equals("messages")) {
                    String results = urll.get_messages(null);
                    URLShell.prettyPrint(results);
                    continue;
                }
                // you need to add a bunch more.

                //!! command returns the last command in history
                if (list.get(list.size() - 1).equals("!!")) {
                    pb.command(history.get(history.size() - 2));

                }//!<integer value i> command
                // there is BUG in this code, can you find it?
                else if (list.get(list.size() - 1).charAt(0) == '!') {
                    int b = Character.getNumericValue(list.get(list.size() - 1).charAt(1));
                    if (b <= history.size())//check if integer entered isn't bigger than history size
                        pb.command(history.get(b));
                } else {
                    pb.command(list);
                }

                // // wait, wait, what curiousness is this?
                // Process process = pb.start();

                // //obtain the input stream
                // InputStream is = process.getInputStream();
                // InputStreamReader isr = new InputStreamReader(is);
                // BufferedReader br = new BufferedReader(isr);

                // //read output of the process
                // String line;
                // while ((line = br.readLine()) != null)
                //     System.out.println(line);
                // br.close();


            } finally {
                // System.out.println("Input Error, Please try again!");
            }

            //catch ioexception, output appropriate message, resume waiting for input
            // catch (IOException e) {
            //     System.out.println("Input Error, Please try again!");
            // }
            // So what, do you suppose, is the meaning of this comment?
            /** The steps are:
             * 1. parse the input to obtain the command and any parameters
             * 2. create a ProcessBuilder object
             * 3. start the process
             * 4. obtain the output stream
             * 5. output the contents returned by the command
             */

        }


    }

}