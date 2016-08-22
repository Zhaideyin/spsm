package com.ronxuntech.component.socket.util;

public class StringUtil {
	//返回某个字符在某个串中出现的次数
	public static int countCharAtString(String str,char c){
		int len=str.length();
		int count=0;
		for(int i=0;i<len;i++){
			char s=str.charAt(i);
			if(s==c){
				count++;
			}
		}
		return count;
	}
	//拼装两个byte数组
	public static byte[] arrayApend(byte[] a,byte[] b){
		int a_len=(a==null?0:a.length);
		int b_len=(b==null?0:b.length);
		
		byte[] c=new byte[a_len+b_len];
				
		if(a_len==0&&b_len==0){
			return null;
		}else if(a_len==0){
			System.arraycopy(b, 0, c, 0, b.length);
		}else if(b_len==0){
			System.arraycopy(a, 0, c, 0, a.length);
		}else{
			System.arraycopy(a, 0, c, 0, a.length);
			System.arraycopy(b, 0, c, a.length, b.length);
		}
		return c;
	}
}
