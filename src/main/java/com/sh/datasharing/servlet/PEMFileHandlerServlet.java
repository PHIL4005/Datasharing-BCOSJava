package com.sh.datasharing.servlet;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keystore.PEMKeyStore;
import org.fisco.bcos.sdk.model.CryptoType;

public class PEMFileHandlerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        System.out.println("===== PEMFileHandlerServlet =====");

        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        factory.setSizeThreshold(DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD);
        //设置临时文件的存储位置（文件大小大于吞吐量的话就必须设置这个值，比如文件大小：1GB ，一次吞吐量：1MB）
        factory.setRepository(new File(req.getServletContext().getRealPath("/WEB-INF/temp/")));
        upload.setFileSizeMax(10240);   //设置单个文件上传的大小10KB
        upload.setSizeMax(-1);
        upload.setHeaderEncoding("UTF-8");
//        Map<String,String> ret = new HashMap<>();
        String pemFilePath;
        String hexPrivateKey = "";
        try {
            List<FileItem> fileItemList = upload.parseRequest(req);
            Map<String,String> map = new HashMap<>();
            for (FileItem fileItem : fileItemList) {
                if (fileItem.isFormField()) {
                    map.put(fileItem.getFieldName(), fileItem.getString());
                }
            }
            for (FileItem fileItem : fileItemList) {
                if (!fileItem.isFormField()) {
                    String uploadPath = req.getServletContext().getRealPath("/WEB-INF/PEMfiles/");
                    System.out.println(uploadPath);
                    // 保存文件到本地
                    fileItem.write(new File(uploadPath+map.get("fileName")));
                    System.out.println(uploadPath+map.get("fileName"));
                    // get pem file real path
                    pemFilePath = uploadPath+map.get("fileName");
                    // get hex private key
                    PEMKeyStore keyTool = new PEMKeyStore(pemFilePath);
                    KeyPair keyPair = keyTool.getKeyPair();
                    CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
                    hexPrivateKey = cryptoSuite.createKeyPair(keyPair).getHexPrivateKey();
                    System.out.println("==== hexPrivateKey ==== " + hexPrivateKey);

                }
            }


            // convert to JSON
            ObjectMapper mapper = new ObjectMapper();
            String cntJson = mapper.writeValueAsString(hexPrivateKey);
            // set response
            resp.getWriter().write(cntJson);
        } catch (Exception e1) {
            e1.printStackTrace();
        }


    }
}
