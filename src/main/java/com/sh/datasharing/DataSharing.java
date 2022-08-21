package com.sh.datasharing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sh.datasharing.metadata.FileMetadataPublisher;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple7;
import java.math.BigInteger;

public class DataSharing {
    public static void main(String[] args) throws Exception {

        // java -Djdk.tls.namedGroups="secp256k1" -cp 'apps/*:conf/:lib/*' com.sh.datasharing.DataSharing

        FileMetadataPublisher publisher = new FileMetadataPublisher();
        String hexPrivateKey = "cf5c112c7f2a451b62c6776af7283b7cef433f6118ef335461a7139518ebad8e";
        publisher.initialize(hexPrivateKey);
        System.out.println(publisher.client.getPeers());
        // deploy contract
//        publisher.deployAssetAndRecordAddr();

        publisher.publishMetadata("/home/ubuntu/Templates/sampleFile.txt","this is the first File.");
        System.out.println("total file num:" + publisher.getTotalFileNum().toString());

//        publisher.publishMetadata("/home/ubuntu/Templates/sampleFile.txt","this is the second File.");
//        System.out.println("total file num:" + publisher.getTotalFileNum().toString());

        System.out.println("======== com.sh.datasharing.metadata of file 1: =======");
        Tuple7 result = publisher.getMetadataByFIleID(new BigInteger(String.valueOf(1)));
        System.out.println(result.toString());
        System.out.println("============================");
        // convert to JSON
        ObjectMapper mapper = new ObjectMapper();
        String metadataJson = mapper.writeValueAsString(result);
        System.out.println(metadataJson);
        System.out.println(result.getValue1());
        System.out.println(result.getValue2());
        System.out.println(result.getValue3());
        System.out.println(result.getValue4());
        System.out.println(result.getValue5());
        System.out.println(result.getValue6());
        System.out.println(result.getValue7());
        System.out.println("====================================");
        System.exit(0);


    }
}
