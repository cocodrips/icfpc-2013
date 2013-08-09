package piyopiyo.py;

import static org.junit.Assert.*;
import static piyopiyo.py.IcfpJson.ICFPJSON;

import org.junit.Test;

public class IcfpJsonTest {
    @Test
    public void testEvalRequest() {
        EvalRequest req = new EvalRequest("hoge", new long[] { 0, 1, -1 });
        assertEquals("{\"arguments\":[\"0x0\",\"0x1\",\"0xFFFFFFFFFFFFFFFF\"]" +
                     ",\"id\":\"hoge\"}", ICFPJSON.format(req));
    }

    @Test
    public void testParseLong() {
        String message = ("{\"status\":\"ok\",\"outputs\":" +
                          "[\"0x0\",\"0x1\",\"0xFFFFFFFFFFFFFFFF\"]}");
        EvalResponse res = ICFPJSON.parse(message, EvalResponse.class);
        assertEquals(EvalResponse.Status.ok, res.status);
        assertArrayEquals(new long[] { 0,  1, -1 }, res.outputs);
    }
}
