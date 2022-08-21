package com.sh.datasharing.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sh.datasharing.metadata.FileMetadataPublisher;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple7;
import java.math.BigInteger;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PublishMetadataServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        System.out.println("===== PublishMetadataServlet =====");
        // get file path and description from HTTP Request
        String filePath = req.getParameter("filepath");
        String description = req.getParameter("description");
        //
        FileMetadataPublisher publisher = new FileMetadataPublisher();
        try {
            // get private key from cookie
            Cookie[] cookies = req.getCookies();
            String hexPrivateKey = "";
            if (cookies != null) {
                for (int i = 0; i < cookies.length; i++) {
                    Cookie cookie = cookies[i];
                    //获取cookie的名字
                    if (cookie.getName().equals("hexPrivateKey")){
                        hexPrivateKey = cookie.getValue();
                    }
                }
            }
            System.out.println("============ private key: " + hexPrivateKey);
            // initialize publisher
            publisher.initialize(hexPrivateKey);
            // publish to chain
            publisher.publishMetadata(filePath, description);
            // get current file ID, refers to the latest uploaded file.
            BigInteger fileID = publisher.getTotalFileNum();
            // get publish result
            Tuple7 result = publisher.getMetadataByFIleID(fileID);
            // convert to JSON
            ObjectMapper mapper = new ObjectMapper();
            String metadataJson = mapper.writeValueAsString(result);
            // set response
            resp.getWriter().write(metadataJson);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
