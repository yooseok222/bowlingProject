package kr.kosa.bowl;

public class Run {
	
	public static void main(String[] args) {

		Menu me = new Menu();
		me.start();
		
		Game g = new Game();
		g.start(2, 1);


		Manager m = new Manager();
		m.validateManager();
		
	}

}
