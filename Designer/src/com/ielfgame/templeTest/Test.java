package com.ielfgame.templeTest;

public class Test {
	
	enum Type {
		
	}
	
	public static void main(String [] args) {
		System.out.println(Enum.class.isAssignableFrom(Type.class));
		System.out.println(Type.class.isAssignableFrom(Enum.class));
		
		System.out.println(Temple.class.isAssignableFrom(T.class));
		System.out.println(T.class.isAssignableFrom(Temple.class));
	} 
}

@SuppressWarnings("hiding")
class Temple<T> {
	private T t;
	Temple(T t) {
		this.t = t;
	}
	public T get() {
		return t;
	}
	public void print() {
		System.out.println(t.toString());
	} 
}

class T extends Temple<T> {
	T(T t) {
		super(t);
	} 
}