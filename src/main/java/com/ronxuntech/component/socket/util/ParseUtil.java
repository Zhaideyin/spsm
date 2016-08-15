package com.ronxuntech.component.socket.util;

/**
 * 进制转换工具
 * @author train
 *
 */
public class ParseUtil {

    public static String binary2hex(String bString)  
    {  
        if (bString == null || bString.equals("") || bString.length() % 8 != 0)  
            return null;  
        StringBuffer tmp = new StringBuffer();  
        int iTmp = 0;  
        for (int i = 0; i < bString.length(); i += 4)  
        {  
            iTmp = 0;  
            for (int j = 0; j < 4; j++)  
            {  
                iTmp += Integer.parseInt(bString.substring(i + j, i + j + 1)) << (4 - j - 1);  
            }  
            tmp.append(Integer.toHexString(iTmp));  
        }  
        return tmp.toString();  
    }
    /**
     * 16进制转2进制
     * @param hexString
     * @return
     */
    public static String hex2binary(String hexString)  
    {  
    	String bString=Integer.toBinaryString(Integer.valueOf(hexString,16));
    	//左补0个数
    	int len=4-bString.length()%4;
    	len=len<4?len:0;
    	String p="";
    	for(int i=0;i<len;i++){
    		p+="0";
    	}
        return p+bString;  
    }
    /**
     * 按位转换后拼接
     * @param hexString
     * @return
     */
    public static String bcdhex2binary(String hexString){
    	String str="";
    	int len=hexString.length();
    	for(int i=0;i<len;i++){
    		str+=hex2binary(String.valueOf(hexString.charAt(i)));
    	}
    	return str;
    }
    
    /**
     * acssi 转16进制
     * @param str
     * @return
     */
    public static String stringToHex(String str){

  	  char[] chars = str.toCharArray();

  	  StringBuffer hex = new StringBuffer();
  	  for(int i = 0; i < chars.length; i++){
  	    hex.append(Integer.toHexString((int)chars[i]));
  	  }

  	  return hex.toString();
  	 }
    /**
     * 16进制转acssi
     * @param hex
     * @return
     */
    public static String hexToString(String hex){

  	  StringBuilder sb = new StringBuilder();
  	  StringBuilder temp = new StringBuilder();

  	  //49204c6f7665204a617661 split into two characters 49, 20, 4c...
  	  for( int i=0; i<hex.length()-1; i+=2 ){

  	      //grab the hex in pairs
  	      String output = hex.substring(i, (i + 2));
  	      //convert hex to decimal
  	      int decimal = Integer.parseInt(output, 16);
  	      //convert the decimal to character
  	      sb.append((char)decimal);

  	      temp.append(decimal);
  	  }

  	  return sb.toString();
  	}
    public static String intToHex(int i){
    	return Integer.toHexString(i);
    }
    public static void main(String[] args)  
    {  
    	int n1 = 14;
    	//十进制转成十六进制：
    	Integer.toHexString(n1);
    	//十进制转成八进制
    	Integer.toOctalString(n1);
    	//十进制转成二进制
    	Integer.toBinaryString(12);

    	//十六进制转成十进制
    	Integer.valueOf("0163",16).toString();
    	//十六进制转成二进制
    	Integer.toBinaryString(Integer.valueOf("FFFF",16));
    	//十六进制转成八进制
    	Integer.toOctalString(Integer.valueOf("FFFF",16));

    	//八进制转成十进制
    	Integer.valueOf("576",8).toString();
    	//八进制转成二进制
    	Integer.toBinaryString(Integer.valueOf("23",8));
    	//八进制转成十六进制
    	Integer.toHexString(Integer.valueOf("23",8));


    	//二进制转十进制
    	Integer.valueOf("0101",2).toString();
    	//二进制转八进制
    	Integer.toOctalString(Integer.parseInt("0101", 2));
    	//二进制转十六进制
    	Integer.toHexString(Integer.parseInt("0101", 2));
    	
        String hexString = "C";  
       // System.out.println(stringToHex(".:D............."));
        String bitMap128Str=(hexToString("20")); 
        System.out.println(bitMap128Str);
       
        String a="11110010001110100100010010000001100010001100000110000000000100000000000000000000000000000000000000010010000000000000000000000001";
        for(int i=0;i<a.length();i++){
        	if(a.charAt(i)=='1'){
        		//System.out.println(i+1);
        	}
        }
    }  
}
