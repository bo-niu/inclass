import com.google.gson.Gson;

import java.io.IOException;

public class Worker implements Runnable {

    private final PrimeHttpClient client = new PrimeHttpClient();
    private Integer numPostRequest;
    private String ip;
    private String port;
    private Integer primeCount;
    private Integer unsuccessfulRequestCount;
    private Integer successfulRequestCount;

    public Worker(Integer numPostRequest, String ip, String port) {
        this.numPostRequest = numPostRequest;
        this.ip = ip;
        this.port = port;
        this.primeCount = 0;
        this.unsuccessfulRequestCount = 0;
        this.successfulRequestCount = 0;
    }

    public Integer getPrimeCount() {
        return primeCount;
    }

    public Integer getUnsuccessfulRequestCount() {
        return unsuccessfulRequestCount;
    }

    public Integer getSuccessfulRequestCount() {
        return successfulRequestCount;
    }

    @Override
    public void run() {
        for (int i=0; i<numPostRequest; i++) {
//            String url = "http://" + ip + ":" + port + "/SkiResorts_war_exploded/skiers/2/seasons/1/days/1/skiers/"
            String url = "http://" + ip + ":" + port + "/prime/"
                    + RandomNumberGenerator.getRandomNumberBetween(1, 20000);
            try {
                if (client.getPrimeRequest(url)) {
                    successfulRequestCount += 1;
                    if (client.isPrime()) {
                        primeCount += 1;
                    }
                } else {
                    unsuccessfulRequestCount += 1;
                }
            } catch (IOException | InterruptedException e) {
                unsuccessfulRequestCount += 1;
                e.printStackTrace();
            }
        }
    }
}
