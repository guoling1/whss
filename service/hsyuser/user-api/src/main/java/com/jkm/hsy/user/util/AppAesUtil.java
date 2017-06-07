package com.jkm.hsy.user.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AppAesUtil {

	/**
	 * 用CBC noPadding模式下加密 返回二进制
	 *
	 * @param data 加密字符串
	 * @param encode 编码格式 例如utf-8、gbk
	 * @param secretkey 约定加密字符串
	 * @param ivkey iv向量加强加密过程
	 *
	 * @return 返回加密的二进制
	 */
	public static byte[] encryptCBC_NoPadding(String data,String encode,String secretkey,String ivkey)throws Exception{
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		int blockSize = cipher.getBlockSize();
		byte[] dataBytes = data.getBytes(encode);
		int plaintextLength = dataBytes.length;
		if (plaintextLength % blockSize != 0) {
			plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
		}
		byte[] plaintext = new byte[plaintextLength];
		System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

		SecretKeySpec keySpec = new SecretKeySpec(secretkey.getBytes(encode), "AES");
		IvParameterSpec iv = new IvParameterSpec(ivkey.getBytes(encode));
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
		byte[] result=cipher.doFinal(plaintext);
		return result;
	}


	/**
	 * 用CBC noPadding模式下加密 返回字符串
	 *
	 * @param data 加密字符串
	 * @param encode 编码格式 例如utf-8、gbk
	 * @param secretkey 约定加密字符串
	 * @param ivkey iv向量加强加密过程
	 *
	 * @return base64组成的字符串
	 */
	public static String encryptCBC_NoPaddingToBase64String(String data,String encode,String secretkey,String ivkey)throws Exception{
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		int blockSize = cipher.getBlockSize();
		byte[] dataBytes = data.getBytes(encode);
		int plaintextLength = dataBytes.length;
		if (plaintextLength % blockSize != 0) {
			plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
		}
		byte[] plaintext = new byte[plaintextLength];
		System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

		SecretKeySpec keySpec = new SecretKeySpec(secretkey.getBytes(encode), "AES");
		IvParameterSpec iv = new IvParameterSpec(ivkey.getBytes(encode));
		cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);
		byte[] result=cipher.doFinal(plaintext);
		return new BASE64Encoder().encode(result);
	}

	/**
	 * 用CBC noPadding模式下解密
	 *
	 * @param data 解密字符串base64格式的
	 * @param encode 编码格式 例如utf-8、gbk
	 * @param secretkey 约定加密字符串
	 * @param ivkey iv向量加强加密过程
	 *
	 * @return 编码格式为encode的字符串
	 */
	public static String decryptCBC_NoPaddingFromBase64String(String data,String encode,String secretkey,String ivkey)throws Exception{
		byte[] dataByte=new BASE64Decoder().decodeBuffer(data);
		return decryptCBC_NoPaddingFromByte(dataByte, encode, secretkey, ivkey);
	}

	/**
	 * 用CBC noPadding模式下解密
	 *
	 * @param dataByte 二进制
	 * @param encode 编码格式 例如utf-8、gbk
	 * @param secretkey 约定加密字符串
	 * @param ivkey iv向量加强加密过程
	 *
	 * @return 编码格式为encode的字符串
	 */
	public static String decryptCBC_NoPaddingFromByte(byte[] dataByte,String encode,String secretkey,String ivkey)throws Exception{
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		SecretKeySpec keySpec = new SecretKeySpec(secretkey.getBytes(encode), "AES");
		IvParameterSpec iv = new IvParameterSpec(ivkey.getBytes(encode));
		cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);
		return new String(cipher.doFinal(dataByte),"utf-8");
	}

	public static void main(String[] args)throws Exception{
		String data="{\"mobiles\":[\"13521691431\"],\"message\":\"您的验证码是:334455,不管任何人进行索取,请勿泄露,请及时验证!\",\"provider\":9}";
		String base64E= AppAesUtil.encryptCBC_NoPaddingToBase64String(data, "utf-8", "61243d4fa76d5a64", "1234567812345678");
		String base65D= AppAesUtil.decryptCBC_NoPaddingFromBase64String(base64E, "utf-8", "61243d4fa76d5a64", "1234567812345678");
		System.out.println("加密前---"+data);
		System.out.println("加密后---"+base64E);
		System.out.println("解密后---"+base65D);

		String x="SG+pExxbqcCVS5JiDWCmY89iubEFc18NOlaP7Np8y+4X/LjmeSv1c5naKeVs38z0\n" +
				"WEL93RD8qi7hiVrLuxjeQplenbP1Xyba9QIWFcRCoyEWuOUE1pSPENRj9B1KTeri\n" +
				"iWj7T2KCPp6k9hyE2jgjvCLTD+WU2RDeFP0EEi4IvMaEfGr0alFO4t0HWfbuWw7c";
		String y=AppAesUtil.decryptCBC_NoPaddingFromBase64String(x, "utf-8", "61243d4fa76d5a64", "1234567812345678");
		System.out.println(y);
		System.out.println(AppAesUtil.encryptCBC_NoPaddingToBase64String(y, "utf-8", "61243d4fa76d5a64", "1234567812345678"));
	}
}
