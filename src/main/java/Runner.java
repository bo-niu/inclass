import org.apache.commons.cli.*;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class Runner {

    public static void main(String[] args) throws InterruptedException {

        Options options = new Options();

        Option tt = new Option("t", "numThreads", true, "maximum number of threads to run (numThreads - max 256)");
        tt.setRequired(true);
        options.addOption(tt);

        Option ii = new Option("i", "ip", true, "IP address of the server");
        ii.setRequired(true);
        options.addOption(ii);

        Option p = new Option("p", "port", true, "port of the server");
        p.setRequired(true);
        options.addOption(p);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        Timestamp start = new Timestamp(System.currentTimeMillis());


        int numThreads = 128;
        String ip = "localhost";
        String port = "8080";

        try {
            cmd = parser.parse(options, args);
            numThreads = Integer.parseInt(cmd.getOptionValue("numThreads"));
            ip = cmd.getOptionValue("ip");
            port = cmd.getOptionValue("port");
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }

        System.out.println("numThreads: "+ numThreads);
        System.out.println("ip: "+ ip);
        System.out.println("port: "+ port);
        System.out.println("Each thread should send " + Math.ceil(100000.0 / numThreads) + " requests");

        Thread[] threads = new Thread[numThreads];
        Worker[] workers = new Worker[numThreads];
        for (int i=0; i<numThreads; i++) {
            Worker worker = new Worker((int) Math.ceil(100000.0 / numThreads), ip, port);
            Thread t = new Thread(worker);
            t.start();
            threads[i] = t;
            workers[i] = worker;
        }

        Integer primeCount = 0;
        Integer successfulRequestCount = 0;
        Integer UnsuccessfulRequestCount = 0;
        for (int i=0; i<numThreads; i++) {
            threads[i].join();
            primeCount += workers[i].getPrimeCount();
            successfulRequestCount += workers[i].getSuccessfulRequestCount();
            UnsuccessfulRequestCount += workers[i].getUnsuccessfulRequestCount();
        }

        Timestamp end = new Timestamp(System.currentTimeMillis());
        long diff = end.getTime() - start.getTime();
        long ms = TimeUnit.MILLISECONDS.toMillis(diff);

        System.out.println("~~~~~~~~~~~~~~~~~~ Here are the result ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");
        System.out.println("Number of Prime is: " + primeCount);
        System.out.println("Percentage of Prime Number is: " + (double)primeCount / 100000.0);
        System.out.println("Number of Successful Request: " + successfulRequestCount);
        System.out.println("Number of Unsuccessful Request: " + UnsuccessfulRequestCount);
        System.out.println("Total run time (wall time) is " + ms + "ms");


    }
}
