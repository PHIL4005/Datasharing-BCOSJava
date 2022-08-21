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
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple7;

public class GetUploadedMetadataByIDServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        String fileID = req.getParameter("fileID");
        BigInteger id = new BigInteger(fileID);
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
            publisher.initialize(hexPrivateKey);
            Tuple7 result = publisher.getMetadataByFIleID(id);
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
