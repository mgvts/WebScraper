package com.parse.service;


import com.parse.domain.ProtocolDTO;
import com.parse.domain.ResponseDTO;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class ScraperServiceImpl implements ScraperService {


    String url = "https://fundsolovki.ru/закупки/перечень-закупок/";
    String decodedUrl = URLDecoder.decode(url, StandardCharsets.UTF_8);
    List<List<String>> TYPES_OF_JOB = List.of(List.of("проведение ремонтно-реставрационных работ",
            "carrying out repair and restoration work"));


    @Override
    public Set<ResponseDTO> getVehicleByModel(String vehicleModel) {

        Set<ResponseDTO> responseDTOS = new HashSet<>();
        parseWebSite(responseDTOS);
        return responseDTOS;
    }

    private void parseWebSite(Set<ResponseDTO> responseDTOS) {
        try {
            Document document = Jsoup.connect(decodedUrl).get();
            Element ol_tag = document.getElementsByTag("ol").first();
            assert ol_tag != null;
            Elements elements = ol_tag.select("li");

            for (Element element : elements) {
                ResponseDTO responseDTO = parseElement(element);
                if (responseDTO == null) continue;
                responseDTOS.add(responseDTO);
            }
            System.out.println(responseDTOS);
        } catch (IOException e) {
            System.err.println("IOException in top " + e.getMessage());
        }
    }

    private ResponseDTO parseElement(Element element) throws IOException {
        Element main_elent_A = element.getElementsByTag("a").first();
        ResponseDTO res = new ResponseDTO();
        String title = main_elent_A.attr("title");
        if (title.equals("")) return null;
        List<String> types = parseTitle(title);
        if (types == null) return null;
        String type_en = types.get(1);

        ProtocolDTO protocolDTO = parseProtocol(element);
        res.setTitle(title);
        res.setType(type_en);
//        res.setType(href);
        return res;
    }

    private ProtocolDTO parseProtocol(Element element) {
        Elements elems = element.select("a");
        for (Element elem : elems) {
            if (elem.attr("title").toLowerCase(Locale.ROOT).contains("протокол")) {
                String href = elem.attr("href");
                if (href.equals("")) continue;
                getAns(href);

            }
        }
        return null;
    }

    private List<String> parseTitle(String title) {
        if (title.equals("")) return null;
        String res = title.toLowerCase();
        for (List<String> types : TYPES_OF_JOB) {
            if (res.contains(types.get(0))) {
                return types;
            }
        }
        return null;
    }

//todo need to encode to normal cririlic symbols now only unreadable bytecode
    private void getAns(String url) {
        try {
            OkHttpClient client = new OkHttpClient();
            String[] splitted_url = url.split("\\?");
            String[] params = splitted_url[1].split("=");
            HttpUrl.Builder urlBuilder
                    = HttpUrl.parse(splitted_url[0]).newBuilder();
            urlBuilder.addQueryParameter(params[0], params[1]);

            String urll = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(urll)
                    .build();

            Call call = client.newCall(request);

            Response response = call.execute();
            String serverAnswer = new String(response.body().bytes(), StandardCharsets.UTF_8);
            String fileName = response.header("Content-Disposition").split(" ")[1].split("=")[1];
            System.out.println(response);
            System.out.println("Content-Disposition " + fileName);
            DocService docService = new DocService(fileName);
            docService.write(serverAnswer);
            docService.getTable();


        } catch (IOException e) {
            System.err.println("IOException while execute href " + e.getMessage());
        }
    }
}