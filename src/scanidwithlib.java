/**
 * Created by jof on 14/02/2017.
 */

import java.util.*;
import javax.smartcardio.*;
import be.belgium.eid.*;

public class scanidwithlib {
    public static void main(String[] args) {
        try {

        } catch(Exception e) {
            System.out.println("Y a un probleme mon chou " + e.toString());
        }
        BEID_Long beLong = new BEID_Long();
        BEID_Status CardStatus;
        BEID_ID_Data idData = new BEID_ID_Data();
        BEID_Certif_Check CertCheck = new BEID_Certif_Check();
        BEID_Bytes userPicture = new BEID_Bytes();
        BEID_Address userAddress = new BEID_Address();
        BEID_VersionInfo versionInfo = new BEID_VersionInfo();

        CardStatus = eidlib.BEID_Init(null,0,0,beLong);
        if(CardStatus.getGeneral() == 0)
            System.out.println("You are been connected.");
        else
            System.out.println("Connection fail !");

        System.out.println("<Connected> Reading the card ...");
        eidlib.BEID_GetID(idData,CertCheck);

        // User

        System.out.println(idData.getName());
        System.out.println(idData.getFirstName1());
        System.out.println(idData.getBirthLocation());
        System.out.println(idData.getBirthDate());
        System.out.println(idData.getSex());
        System.out.println(idData.getNationality());
        System.out.println(idData.getNobleCondition());
        System.out.println(idData.getNationalNumber());
        System.out.println(idData.getMunicipality());
        eidlib.BEID_GetAddress(userAddress, CertCheck);
        System.out.println(userAddress.getStreet());
        System.out.println(userAddress.getZip());
        System.out.println(userAddress.getMunicipality());
        System.out.println(userAddress.getCountry());

        CardStatus = eidlib.BEID_GetPicture(userPicture, CertCheck);
        if(CardStatus.getGeneral() != 0)
            System.out.println("you are been fucked");

        /*ImageIcon ic = new ImageIcon(userPicture.getData());
        ic.getImage();//S'assurer que l'image est complètement chargée

        pnUser.setPicture(ic);*/

        // Card

        System.out.println(idData.getCardNumber());
        System.out.println(idData.getChipNumber());
        System.out.println(idData.getValidityDateBegin());
        System.out.println(idData.getValidityDateEnd());

        BEID_Bytes signedStatus = new BEID_Bytes();
        CardStatus = eidlib.BEID_GetVersionInfo(versionInfo,CertCheck.getSignatureCheck(),signedStatus);
        if(CardStatus.getGeneral() != 0)
            System.out.println("No version info available");

        System.out.println("OS number : " + String.valueOf(versionInfo.getOSNumber()));
        System.out.println("OS version : " + String.valueOf(versionInfo.getOSVersion()));
        System.out.println("Component code : " + String.valueOf(versionInfo.getComponentCode()));
        System.out.println("SoftMask number : " + String.valueOf(versionInfo.getSoftmaskNumber()));
        System.out.println("SoftMask version : " + String.valueOf(versionInfo.getSoftmaskVersion()));
        System.out.println("Applet version : " + String.valueOf(versionInfo.getAppletVersion()));
        System.out.println("Global OS version : " +String.valueOf(versionInfo.getGlobalOSVersion()));
        System.out.println("Applet interface version : " + String.valueOf(versionInfo.getAppletInterfaceVersion()));
        System.out.println("PKCS#1 support : " + String.valueOf(versionInfo.getPKCS1Support()));
        System.out.println("Key exchange version : " + String.valueOf(versionInfo.getKeyExchangeVersion()));
        System.out.println("Application life cycle : " + String.valueOf(versionInfo.getApplicationLifeCycle()));
        System.out.println("Graphical personalisation : " + String.valueOf(versionInfo.getGraphPerso()));
        System.out.println("Electrical personalisation : " + String.valueOf(versionInfo.getElecPerso()));
        System.out.println("Electrical personalisation interface : " + String.valueOf(versionInfo.getElecPersoInterface()));

        // Montre comment vérifier le code pin.
        /*
        BEID_Pin pinData = new BEID_Pin(); // OS PIN
        BEID_Long TriesLeft = new BEID_Long(); // Authentication usage.

        pinData.setId((short)1);
        pinData.setUsageCode(1);


        CardStatus = eidlib.BEID_VerifyPIN(pinData,"1234", TriesLeft);
        if(CardStatus.getGeneral() != 0)
            System.out.println("bad pin" + CardStatus.getGeneral() + " " + TriesLeft.getLong());
         */
        System.out.println(CertCheck.getCertificatesLength());
        System.out.println(CertCheck.getCertificate(0).getCertifLabel());
        System.out.println(CertCheck.getCertificate(0).getCertif());
        System.out.println("SIGNATURE : " + CertCheck.getSignatureCheck());
        eidlib.BEID_Exit();
        System.out.println("<closed> Reading finished");
    }
}
