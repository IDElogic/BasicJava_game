public class BasicGame {

	public static void main(String[] args) throws InterruptedException {
		//SPAGETTI KOD
		String [][] level = new String [10][10];
		String playerMark = "O";//string tipusu helyi valtozo, inicializalva a nagy O beture
		int row = 2;
		int column = 2;
		Direction direction = Direction.RIGHT;//helyi valtozo (enum tipusa es a konstansa
		
		//palya inicializalasa
		for(int i = 0 ; i < level.length; i++) {
			for (int j = 0; j < level[i].length; j++) {
				if (i == 0 || i == 9 || j == 0 || j == 9) {//fal körben x-böl
					level[i][j] = "X ";
				} else {
					level[i][j] = " ";
				}
			}
		}
		
		for (int k = 1; k <= 100; k++) {
			if(k % 10 == 0) {
		//iranyvaltoztatas
				direction = changeDirection(direction);
			}
			switch (direction) {
			case UP:
				if (level [row - 1][column].equals(" ")) {
				row --; 
				}
				break;
			case DOWN:
				if (level [row + 1][column].equals(" ")) {
					row ++; 
					}//ami nem szoköz, az fal
				break;
			case LEFT:
				if (level [row ][column - 1].equals(" ")) {
				column --; 
				}
				break;
			case RIGHT:
				if (level [row ][column + 1].equals(" ")) {
				column ++;
				}
				break;
			}
			draw(level, playerMark, row, column);//draw metodus meghivasa
			
			System.out.println("-------");
			Thread.sleep(500);
		}
	}
	
		//palya es jatekos kirajzolasa
//		for (int i = 0; i < level.length; i++) {
//			for (int j = 0;j < level[i].length; j++) {
//				if(i == row && j == column) {		
//				System.out.print(playerMark);
//			} else {
//			   System.out.print(level[i][j]);
//			}
//			}
//			System.out.println();	
//		}
//		
//		System.out.println("-------");
//		Thread.sleep(500);
//		}
//	}
//		METODUSOK
	
		static Direction changeDirection(Direction direction) {
			switch (direction) {
			case RIGHT: 
				direction = Direction.DOWN;
				break;
			case DOWN: 
				direction = Direction.LEFT;
				break;
			case LEFT: 
				direction = Direction.UP;
				break;
			case UP: 
				direction = Direction.RIGHT;
				break;
			}
			return direction;
		}
		
		static void draw(String [][] board, String mark, int x, int y){
			for (int i = 0; i < board.length; i++) {
				for (int j = 0;j < board[i].length; j++) {
					if(i == x && j == y) {		
					System.out.print(mark);
				} else {
				   System.out.print(board[i][j]);
				}
				}
				System.out.println();	
			}			
		}
}
		

	
	

