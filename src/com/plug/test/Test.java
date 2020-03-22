package com.plug.test;

public class Test {
	
	public static void main(String[] args) {
		int[] aes = new int[]{12,15, 255,255 ,41,12 ,70,237 ,64,0,64,6};
		//System.out.println(Integer.toBinaryString(a));
		System.out.println(toHexBinaryStr(aes));
	}
	
	public static String toHexBinaryStr(byte[] arrays) {
        StringBuffer hexBinaryStr = new StringBuffer();
        for (byte b: arrays) {
            int num = (b & 0xFF);
            String binaryStr = Integer.toBinaryString(num);
            if(binaryStr.length() > 8){
                return null;
            }
            for(int i = 0; i < (8 - binaryStr.length()); i++){
                hexBinaryStr.append("0");
            }
            for(int i = 0; i < binaryStr.length(); i++){
                hexBinaryStr.append(binaryStr.charAt(i));
            }
        }
        return hexBinaryStr.toString();
    }
	
	public static String toHexBinaryStr(int[] nums) {
        StringBuffer hexBinaryStr = new StringBuffer();
        for (int num: nums) {
            String binaryStr = Integer.toBinaryString(num);
            if(binaryStr.length() > 8){
                return null;
            }
            for(int i = 0; i < (8 - binaryStr.length()); i++){
                hexBinaryStr.append("0");
            }
            for(int i = 0; i < binaryStr.length(); i++){
                hexBinaryStr.append(binaryStr.charAt(i));
            }
        }
        return hexBinaryStr.toString();
    }
	
	public static String toBinaryStr(int array) {
		StringBuffer hexBinaryStr = new StringBuffer();
		String binaryStr = Integer.toBinaryString(array);
		if(binaryStr.length() > 8){
            return null;
        }
		for(int i = 0; i < (8 - binaryStr.length()); i++){
            hexBinaryStr.append("0");
        }
        for(int i = 0; i < binaryStr.length(); i++){
            hexBinaryStr.append(binaryStr.charAt(i));
        }
		
		return hexBinaryStr.toString();
	}

}
