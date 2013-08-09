package piyopiyo.py;
import java.io.BufferedInputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class IcfpClient {
	static final String URL_FORMAT =
			"http://icfpc2013.cloudapp.net/%s?auth=0169BgRBVTFW0ABEQ24ySLghrsivA51QQ9wqb0ZGvpsH1H";

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
		String json = IcfpJson.ICFPJSON.format(request);
		System.err.println("Sending: " + json);

		URL url = new URL(String.format(URL_FORMAT, name));
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			throw new RuntimeException(conn.getResponseCode() + " " + conn.getResponseMessage());
		}

		BufferedInputStream input = new BufferedInputStream(conn.getInputStream());
		TResponse res = IcfpJson.ICFPJSON.parse(input, responseClass);

		input.close();
		conn.disconnect();
		System.err.println("Success.");

		return res;
	}
}
