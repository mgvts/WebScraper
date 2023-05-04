import com.parse.service.DocService;
import okhttp3.*;

import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;

public class test {
    private static String url = "https://fundsolovki.ru/wp-content/plugins/download-attachments/includes/download.php";

    public static void main(String[] args) throws IOException {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(url).newBuilder();
        urlBuilder.addQueryParameter("id", "615");

        String urll = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(urll)
                .build();

        Call call = client.newCall(request);

        Response response = call.execute();
        String serverAnswer = new String(response.body().bytes(), StandardCharsets.UTF_8);
        System.out.println(serverAnswer);
        String fileName = response.header("Content-Disposition").split(" ")[1].split("=")[1];
        System.out.println(response);
//            System.out.println("|||||||||||||||||||||||||||||||serverAnswer" + );
        System.out.println("Content-Disposition " + fileName);
        DocService docService = new DocService(fileName);
        docService.write(serverAnswer);
        docService.getTable();
    }
}
