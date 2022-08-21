package com.sh.datasharing.metadata;

import com.sh.datasharing.fisco.bcos.contract.HelloWorld;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.client.protocol.response.BlockNumber;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
public class HelloWorldTest {
    //get config file
    public static String configFile = HelloWorldTest.class.getClassLoader().getResource("config-example.toml").getPath();

    public HelloWorldTest() {
        System.out.print("===== first ======\n");
        // 初始化BcosSDK
        BcosSDK sdk =  BcosSDK.build(configFile);
        System.out.print("===== sdk initialized ======\n");
        // 为群组1初始化client
        Client client = sdk.getClient(Integer.valueOf(1));
        System.out.print("===== client ok ======\n");
        // 获取群组1的块高
        BlockNumber blockNumber = client.getBlockNumber();
        System.out.print("===== block number =====\n");
        // 向群组1部署HelloWorld合约
        CryptoKeyPair cryptoKeyPair = client.getCryptoSuite().getCryptoKeyPair();
        HelloWorld helloWorld = null;
        try {
            helloWorld = HelloWorld.deploy(client, cryptoKeyPair);
        } catch (ContractException e) {
            e.printStackTrace();
        }
        System.out.print("===== contract deployed ======\n");
        // 调用HelloWorld合约的get接口
        String getValue = null;
        try {
            getValue = helloWorld.get();
        } catch (ContractException e) {
            e.printStackTrace();
        }
        System.out.print("===== value: " + getValue + "=====\n");
        // 调用HelloWorld合约的set接口
        TransactionReceipt receipt = helloWorld.set("Another!");
        try {
            getValue = helloWorld.get();
        } catch (ContractException e) {
            e.printStackTrace();
        }
        System.out.print("===== second value: " + getValue + "=====\n");

        System.exit(0);



//        String filePath = "/home/ubuntu/Templates/sampleFile.txt";
//        MetadataGenerator com.sh.datasharing.metadata = new MetadataGenerator(filePath);
//        System.out.println(com.sh.datasharing.metadata.fileName);
//        System.out.println(com.sh.datasharing.metadata.fileType);
//        System.out.println(com.sh.datasharing.metadata.size);
//        System.out.println(com.sh.datasharing.metadata.metadataHash);
    }
}
