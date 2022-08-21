package com.sh.datasharing.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sh.datasharing.metadata.FileMetadataPublisher;

public class GetFileNumServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        System.out.println("===== GetFileNumServlet =====");
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
            // get total count of published files
            BigInteger cnt = publisher.getTotalFileNum();
            // convert to JSON
            ObjectMapper mapper = new ObjectMapper();
            String cntJson = mapper.writeValueAsString(cnt.intValue());
            // set response
            resp.getWriter().write(cntJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
