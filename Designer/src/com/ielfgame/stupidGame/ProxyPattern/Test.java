package com.ielfgame.stupidGame.ProxyPattern;

public class Test {
	public interface Italk {
		public void talk(String msg);
	}

	public static class People implements Italk {
		public String username;
		public String age;

		public String getName() {
			return username;
		}

		public void setName(String name) {
			this.username = name;
		}

		public String getAge() {
			return age;
		}

		public void setAge(String age) {
			this.age = age;
		}

		public People(String name1, String age1) {
			this.username = name1;
			this.age = age1;
		}

		public void talk(String msg) {
			System.out.println(msg + "!你好,我是" + username + "，我年龄是" + age);
		}
	}

	public static class TalkProxy implements Italk {
		Italk talker;

		public TalkProxy(Italk talker) {
			// super();
			this.talker = talker;
		}

		public void talk(String msg) {
			talker.talk(msg);
		}

		public void talk(String msg, String singname) {
			talker.talk(msg);
			sing(singname);
		}

		private void sing(String singname) {
			System.out.println("唱歌：" + singname);
		}
	}

	public static void main(String[] args) {
		// 不需要执行额外方法的
		Italk people1 = new People("湖海散人", "18");
		people1.talk("No ProXY Test");
		System.out.println("-----------------------------");
		// 需要执行额外方法的
		TalkProxy talker = new TalkProxy(people1);
		talker.talk("ProXY Test", "七里香");
	}
}
