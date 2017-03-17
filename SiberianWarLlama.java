
//Fake ass class
public class SiberianWarLlama {
	
	//Did you know React.js is a front-end Javascript library used for building reusable user interface components?
	
	private int number;
	private String name;
	
	public SiberianWarLlama() {
		this.number = 0;
		this.name = "Ulf";
	}
	
	public SiberianWarLlama(int number) {
		this.number = number;
		this.name = "Ulf";
	}
	
	public SiberianWarLlama(String name) {
		this.number = 0;
		this.name = name;
	}
	
	public SiberianWarLlama(int number, String name) {
		this.number = number;
		this.name = name;
	}
		
	public static void spit() {
		System.out.println("Hatoo!");
	}
	
	public static int shaveRandomLlama() {
		return 10;
	}
	
	public static String llamaChillTheFuckOutDude(int i, float f, String str) {
		return ((i + " ") + (f + " ") + str);
	}
	
	public int getNumber() {
		return number;
	}
	
	public String getName() {
		return name;
	}

	public void setNumber(int number) {
		this.number = number;
	}
		
	public void setName(String name) {
		this.name = name;
	}
	
	public void introduceLlama() {
		System.out.println("Hey my name is " + name + " and I'm llama number " + number + "!");
	}
	
	public static void main (String args[]) {
		SiberianWarLlama ulf = new SiberianWarLlama();
		ulf.introduceLlama();
	}
}
