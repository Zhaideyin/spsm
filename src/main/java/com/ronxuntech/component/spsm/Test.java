package com.ronxuntech.component.spsm;

public class Test {
	public void start(AbcInterface s){
		//---
		s.done(1);
	}
	
	public static void main(String agrs[]){
		Test test=new Test();
		test.start(new AbcInterface(){

			@Override
			public void done(int s) {
				// TODO Auto-generated method stub
				System.out.println(s);
			}
			
		});
	}
}
