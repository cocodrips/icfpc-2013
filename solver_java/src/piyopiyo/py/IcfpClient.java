package piyopiyo.py;
import java.io.BufferedInputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class IcfpClient {
	static final String URL_FORMAT =
			"http://icfpc2013.cloudapp.net/%s?auth=0169BgRBVTFW0ABEQ24ySLghrsivA51QQ9wqb0ZGvpsH1H";

	public static Problem train(TrainRequest request) throws Exception {
		String reqJson = IcfpJson.ICFPJSON.format(request);
		HttpURLConnection conn = connectApi("train", reqJson);
		BufferedInputStream input = new BufferedInputStream(conn.getInputStream());
		Problem res = IcfpJson.ICFPJSON.parse(input, Problem.class);
		input.close();
		conn.disconnect();
		return res;
	}

	public static GuessResponse guess(GuessRequest request) throws Exception {
		String reqJson = IcfpJson.ICFPJSON.format(request);
		HttpURLConnection conn = connectApi("guess", reqJson);
		BufferedInputStream input = new BufferedInputStream(conn.getInputStream());
		GuessResponse res = IcfpJson.ICFPJSON.parse(input, GuessResponse.class);
		input.close();
		conn.disconnect();
		return res;
	}

	public static EvalResponse eval(EvalRequest request) throws Exception {
		String reqJson = IcfpJson.ICFPJSON.format(request);
		HttpURLConnection conn = connectApi("eval", reqJson);
		BufferedInputStream input = new BufferedInputStream(conn.getInputStream());
		EvalResponse res = IcfpJson.ICFPJSON.parse(input, EvalResponse.class);
		input.close();
		conn.disconnect();
		return res;
	}

	private static HttpURLConnection connectApi (String name, String json) throws Exception {
		URL url = new URL(String.format(URL_FORMAT, name));
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Content-Length", Integer.toString(json.length()));
		conn.connect();

		OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
		writer.write(json);
		writer.flush();
		writer.close();

		if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
			new Exception();
		}
		return conn;
	}
}
