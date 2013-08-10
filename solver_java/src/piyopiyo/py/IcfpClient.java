package piyopiyo.py;

import static java.net.HttpURLConnection.HTTP_OK;

import java.io.BufferedInputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import net.arnx.jsonic.JSON;

public class IcfpClient {
    private static final int HTTP_TRY_AGAIN = 429;
    private static final int MAX_RETRIES = 5;

    private static final int WAIT_SECS_BEFORE_RETRY = 20;

    private static final String URL_FORMAT =
        "http://icfpc2013.cloudapp.net/%s?auth=0169BgRBVTFW0ABEQ24ySLghrsivA51QQ9wqb0ZGvpsH1H";

    public static Problem[] myproblems() throws Exception {
        return connectApi("myproblems", new int[0], Problem[].class);
    }

    public static StatusResponse status() throws Exception {
        return connectApi("status", new int[0], StatusResponse.class);
    }

    public static Problem train(TrainRequest request) throws Exception {
        return connectApi("train", request, Problem.class);
    }

    public static GuessResponse guess(GuessRequest request) throws Exception {
        return connectApi("guess", request, GuessResponse.class);
    }

    public static EvalResponse eval(EvalRequest request) throws Exception {
        return connectApi("eval", request, EvalResponse.class);
    }

    private static <TRequest, TResponse> TResponse connectApi(
            String name, TRequest request, Class<TResponse> responseClass) throws Exception {
        HttpURLConnection conn = null;

        for (int retries = 1; retries <= MAX_RETRIES; retries++) {
            String json = IcfpJson.ICFPJSON.format(request);
            System.err.println("Sending: " + json);

            URL url = new URL(String.format(URL_FORMAT, name));
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Content-Length", Integer.toString(json.length()));
            conn.connect();

            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(json);
            writer.flush();
            writer.close();
            System.err.println("Sent: " + json);

            if (conn.getResponseCode() == HTTP_OK) {
                BufferedInputStream input = new BufferedInputStream(conn.getInputStream());
                TResponse res = IcfpJson.ICFPJSON.parse(input, responseClass);
                input.close();
                conn.disconnect();
                System.err.println("Data received.");
                return res;
            }

            if (conn.getResponseCode() != HTTP_TRY_AGAIN) {
                throw new RuntimeException(conn.getResponseCode() + " " + conn.getResponseMessage());
            }
            System.err.printf("Received %d. Retrying...%n", conn.getResponseCode());
            Thread.sleep(WAIT_SECS_BEFORE_RETRY * 1000);
        }

        throw new RuntimeException(conn.getResponseCode() + " " + conn.getResponseMessage());
    }
}
