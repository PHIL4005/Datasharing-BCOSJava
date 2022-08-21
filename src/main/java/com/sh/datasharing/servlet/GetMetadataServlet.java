package com.sh.datasharing.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sh.datasharing.metadata.FileMetadataGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetMetadataServlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        System.out.println("===== GetMetadataServlet =====");
        String filePath = req.getParameter("filepath");
        // generate metadata
        FileMetadataGenerator metadata = new FileMetadataGenerator(filePath);
        ObjectMapper mapper = new ObjectMapper();
        // convert to JSON
        String metadataJson = mapper.writeValueAsString(metadata);
        // set response
        resp.getWriter().write(metadataJson);
    }
}
