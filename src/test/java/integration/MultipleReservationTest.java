package integration;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class MultipleReservationTest {

    private static final int CONCURRENT_REQS = Runtime.getRuntime().availableProcessors();

    @Test
    public void testRunMultipleReservations() throws Exception {
        List tasks = new ArrayList<Callable<HttpResponse>>();
        ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_REQS);
        for (int i = 0; i < CONCURRENT_REQS; i++) {
            int finalI = i;
            Callable<HttpResponse> callableTask = () -> {
                HttpUriRequest req = RequestBuilder.create("PUT").setUri("http://localhost:8080/reservations")
                        .setEntity(new StringEntity(getReservationPayload(finalI + "@email.com"), ContentType.APPLICATION_JSON)).build();
                return HttpClientBuilder.create().build().execute(req);
            };
            tasks.add(callableTask);
        }

        Map<Integer, Integer> resultMap = new HashMap<>();
        List<Future<HttpResponse>> futures = executor.invokeAll(tasks);
        for (int i = 0; i < futures.size(); i++) {
            Future<HttpResponse> future = futures.get(i);
            HttpResponse resp = future.get();
            int status = resp.getStatusLine().getStatusCode();
            Integer count = resultMap.get(status);
            if (count == null) {
                count = 0;
            }
            count++;
            resultMap.put(status, count);
        }
        int okCount = resultMap.get(200);
        int brCount = resultMap.get(400);
        Assert.assertEquals(1, okCount);
        Assert.assertEquals(CONCURRENT_REQS - 1, brCount);
    }

    private String getReservationPayload(String email) {
        return "{\n" +
                "\t\"email\": \"" + email + "\",\n" +
                "\t\"fullname\": \"Some name\",\n" +
                "\t\"from\": \"2019-12-16\",\n" +
                "\t\"to\": \"2019-12-17\"\n" +
                "}";
    }


}
