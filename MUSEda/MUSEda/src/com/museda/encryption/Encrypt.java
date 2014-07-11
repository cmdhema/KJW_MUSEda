package com.museda.encryption;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collections;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;

import android.util.Base64;

public class Encrypt {

	public static final String table = "Aa0Bb1Cc2Dd3Ee4Ff5Gg6Hh7Ii8Jj9KkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz+/";
	
	public static String encode(String input) {

		String encodedResult="";
		String binary = "";
		ArrayList<Integer> binary8BitList = new ArrayList<Integer>();
		ArrayList<String> binary6BitList = new ArrayList<String>();
		ArrayList<Integer> tempList = new ArrayList<Integer>();
		
		for(int i=0;i<input.length();i++) {
			for (int j=0;j<8;j++)  
				tempList.add( 1 & ( (int)input.charAt(i) ) >> j );
			Collections.reverse(tempList);
			binary8BitList.addAll(tempList);
			tempList.removeAll(tempList);
		}
		
		while(binary8BitList.size() % 6 != 0)
			binary8BitList.add(0);
		
		for(int i=0;i<binary8BitList.size();i++) {
			binary += binary8BitList.get(i);
			if( ( i+1 ) % 6 == 0) {
				binary6BitList.add(binary);
				binary = "";
			}
		}
		
		for(int i=0;i<binary6BitList.size();i++) 
			encodedResult += table.charAt(Integer.valueOf(binary6BitList.get(i), 2));
		
		return encodedResult;
		
	}
	
	public static String decode(String input) {
		
		String decodedResult="";
		String binary = "";
		ArrayList<String> binary8BitList = new ArrayList<String>();
		ArrayList<Integer>  binary6BitList = new ArrayList<Integer>();
		ArrayList<Integer> tempList = new ArrayList<Integer>();
		
		for(int i=0;i<input.length();i++) {
			for (int j=0;j<6;j++)  
				tempList.add(  1 & ( table.indexOf(input.charAt(i)) >> j ) );
			
			Collections.reverse(tempList);
			binary6BitList.addAll(tempList);
			tempList.removeAll(tempList);
		}

		while(binary6BitList.size() % 8 != 0)
			binary6BitList.add(0);
//		System.out.println(binary6BitList);
		
		for(int i=0;i<binary6BitList.size();i++) {
			binary += binary6BitList.get(i);
			
			if( ( i+1 ) % 8 == 0) {
				binary8BitList.add(binary);
				binary = "";
			}
		}
		

		for(int i=0;i<binary8BitList.size();i++) {
			int asciiCode = Integer.valueOf(binary8BitList.get(i), 2);
//			System.out.println(asciiCode);
			if(asciiCode==0)
				break;
			decodedResult += Character.toString((char) asciiCode);
		}
		
		return decodedResult;
	}
	
	/**
     * ����Ű ����
     * @return
     */
    public static String key()
    {
        return "ewhapp_kjw_bunobuno";
        //return "ab_booktv_abcd0912345678";
    }
    
    /**
     * Ű��
     * 24����Ʈ�� ��� TripleDES �ƴϸ� DES
     * @return
     * @throws Exception
     */
    public static Key getKey() throws Exception {
        return (key().length() == 24) ? getKey2(key()) : getKey1(key());
    }

    /**
     * ������ ���Ű�� ������ ���� �޼��� (DES)
     * require Key Size : 16 bytes
     *
     * @return Key ���Ű Ŭ����
     * @exception Exception
     */
    public static Key getKey1(String keyValue) throws Exception {
        DESKeySpec desKeySpec = new DESKeySpec(keyValue.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        Key key = keyFactory.generateSecret(desKeySpec);
        return key;
    }
    
    /**
     * ������ ���Ű�� ������ ���� �޼��� (TripleDES)
     * require Key Size : 24 bytes
     * @return
     * @throws Exception
     */
    public static Key getKey2(String keyValue) throws Exception {
        DESedeKeySpec desKeySpec = new DESedeKeySpec(keyValue.getBytes());
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
        Key key = keyFactory.generateSecret(desKeySpec);
        return key;
    }

    /**
     * ���ڿ� ��Ī ��ȣȭ
     *
     * @param ID
     *            ���Ű ��ȣȭ�� ����ϴ� ���ڿ�
     * @return String ��ȣȭ�� ID
     * @exception Exception
     */
    public static String encrypt(String ID) throws Exception {
        if (ID == null || ID.length() == 0)
            return "";
        
        String instance = (key().length() == 24) ? "DESede/ECB/PKCS5Padding" : "DES/ECB/PKCS5Padding";
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(instance);
        cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, getKey());
        String amalgam = ID;

        byte[] inputBytes1 = amalgam.getBytes("UTF8");
        byte[] outputBytes1 = cipher.doFinal(inputBytes1);
        String outputStr1 = Base64.encodeToString(outputBytes1, 0);
        return outputStr1;
    }

    /**
     * ���ڿ� ��Ī ��ȣȭ
     *
     * @param codedID
     *            ���Ű ��ȣȭ�� ����ϴ� ���ڿ�
     * @return String ��ȣȭ�� ID
     * @exception Exception
     */
    public static String decrypt(String codedID) throws Exception {
        if (codedID == null || codedID.length() == 0)
            return "";
        
        String instance = (key().length() == 24) ? "DESede/ECB/PKCS5Padding" : "DES/ECB/PKCS5Padding";
        javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance(instance);
        cipher.init(javax.crypto.Cipher.DECRYPT_MODE, getKey());

        byte[] inputBytes1 = Base64.decode(codedID, 0);
        byte[] outputBytes2 = cipher.doFinal(inputBytes1);

        String strResult = new String(outputBytes2, "UTF8");
        return strResult;
    }
}
