package com.sh.datasharing.metadata;

import com.sh.datasharing.fisco.bcos.contract.MetadataDepository_test01;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple7;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.fisco.bcos.sdk.crypto.CryptoSuite;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Properties;

/**
 * generate com.sh.datasharing.metadata for target file and publish to Blockchain.
 */
public class FileMetadataPublisher {
    //get config file
    public static String configFile = FileMetadataPublisher.class.getClassLoader().getResource("config-example.toml").getPath();
    static Logger logger = LoggerFactory.getLogger(FileMetadataPublisher.class);

    private BcosSDK bcosSDK;
    public Client client;
    private CryptoKeyPair cryptoKeyPair;
    // publish via this account(address)
    private String hexPrivateKey;

    public void initialize(String hexPrivateKey) throws Exception {
        bcosSDK = BcosSDK.build(configFile);
        client = bcosSDK.getClient(1);

        // 通过client获取CryptoSuite对象
//        CryptoSuite cryptoSuite = client.getCryptoSuite();
//        cryptoSuite.loadAccount("pem", "/home/ubuntu/fisco/console/account/ecdsa/0xb8f3f7f183b9436502ccaf9cd5c47276cf276c1b.pem", null);
//        cryptoKeyPair = client.getCryptoSuite().getCryptoKeyPair();

        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.SM_TYPE);
        // temporary
//        String hexPrivateKey = "cf5c112c7f2a451b62c6776af7283b7cef433f6118ef335461a7139518ebad8e";
        this.hexPrivateKey = hexPrivateKey;
        cryptoKeyPair = cryptoSuite.getKeyPairFactory().createKeyPair(hexPrivateKey);  // todo: change into variable
        client.getCryptoSuite().setCryptoKeyPair(cryptoKeyPair);

        logger.debug("create client for group1, account address is " + cryptoKeyPair.getAddress());
//        System.out.println("=== get client address: ===\n" + cryptoKeyPair.getAddress() + "\n======");
    }

    public void deployAssetAndRecordAddr() {
        try {
            MetadataDepository_test01 metadataDepository = MetadataDepository_test01.deploy(client, cryptoKeyPair);
            System.out.println(
                    " deploy Asset success, contract address is " + metadataDepository.getContractAddress());

            recordAssetAddr(metadataDepository.getContractAddress());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            System.out.println(" deploy Asset contract failed, error message is  " + e.getMessage());
        }
    }

    public void recordAssetAddr(String address) throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        prop.setProperty("dataSharing", address);
        final Resource contractResource = new ClassPathResource("contract.properties");
        FileOutputStream fileOutputStream = new FileOutputStream(contractResource.getFile());
        prop.store(fileOutputStream, "contract address");
    }

    public String loadAssetAddr() throws Exception {
        // load Asset contact address from contract.properties
        Properties prop = new Properties();
        final Resource contractResource = new ClassPathResource("contract.properties");
        prop.load(contractResource.getInputStream());

        String contractAddress = prop.getProperty("dataSharing");
        if (contractAddress == null || contractAddress.trim().equals("")) {
            throw new Exception(" load Asset contract address failed, please deploy it first. ");
        }
        logger.info(" load Asset address from contract.properties, address is {}", contractAddress);
        return contractAddress;
    }

    public void publishMetadata(String filePath, String description) throws Exception {
        try {
            String contractAddress = loadAssetAddr();
            MetadataDepository_test01 metadataDepository = MetadataDepository_test01.load(contractAddress, client, cryptoKeyPair);
            // generate com.sh.datasharing.metadata
            FileMetadataGenerator f = new FileMetadataGenerator(filePath);
            // publish to Blockchain
            TransactionReceipt result = metadataDepository.uploadMetadata(
                    f.fileName,
                    f.fileType,
                    f.size,
                    description,
                    f.metadataHash
            );
            logger.info("[INFO]publish file '" + f.fileName + "'success. Transaction receipt is: " + result.toString());
            System.out.println("===== Publish metadata for file: " + f.fileName + " success! =====");
            System.out.println("result is: \n" + result.toString());
            System.out.println("client info:\nCryptoSuite: " + client.getCryptoSuite().toString() + "\ncryptoKeyPair:" + cryptoKeyPair.getAddress());
            System.out.println("=====================\n");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public BigInteger getTotalFileNum() throws Exception {
        try {
            String contractAddress = loadAssetAddr();
            MetadataDepository_test01 metadataDepository = MetadataDepository_test01.load(contractAddress, client, cryptoKeyPair);
            return metadataDepository.getFileNums();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Tuple7 getMetadataByFIleID(BigInteger fileID) throws Exception {
        try {
            String contractAddress = loadAssetAddr();
            MetadataDepository_test01 metadataDepository = MetadataDepository_test01.load(contractAddress, client, cryptoKeyPair);
            Tuple7<String, String, BigInteger, String, String, String, String> result = metadataDepository.selectMetadata(fileID);
            return result;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
