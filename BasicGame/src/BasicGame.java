public class BasicGame {
	
	//osztalyon belul,(de metodusokon kivul)Osztalyvaltozok_Static ehhez fontos	
	static int gameLoopNumber = 100;
	static int height = 15;
	static int width = 15;

	public static void main(String[] args) throws InterruptedException {
		
		
		String playerMark = "O";//string tipusu helyi valtozo, inicializalva a nagy O beture
		int playerRow = 2;//helyi valtozo
		int playerColumn = 2;//helyi valtozo
		Direction playerDirection = Direction.RIGHT;//helyi valtozo (enum tipusa es a konstansa		
		
		//masik jatekos
		String enemyMark = "-";
		int enemyRow = 7;
		int enemyColumn = 4;
		Direction enemyDirection = Direction.LEFT;	
			
		//palya inicializalasa
		String [][] level = new String [height][width];//helyi valtozo(metoduson belul van letrehozva)
		initlevel(level); //initlevel metodus meghivas(bemeneti parameterei)
		
		//iranyvaltoztatos logika
		for (int iterationNumber = 1; iterationNumber <= gameLoopNumber; iterationNumber++) {
			//jatekos leptetese
			if(iterationNumber % 15 == 0) {
				playerDirection = changeDirection(playerDirection);
			}
			//mozgatast vegzo logika
			int[] playerCoordinates = makeMove(playerDirection, level, playerRow, playerColumn);//makeMove metodus meghivasa
			playerRow = playerCoordinates[0];
			playerColumn = playerCoordinates[1];
			
			//masik jatekos leptetese
			if(iterationNumber % 10 == 0) {
				enemyDirection = changeDirection(enemyDirection);
			}
			int[] enemyCoordinates = makeMove(enemyDirection, level,enemyRow, enemyColumn);//makeMove metodus meghivasa
			enemyRow = enemyCoordinates[0];
			enemyColumn = enemyCoordinates[1];
			
			//palya es jatekos kirajzolasa
			draw(level, playerMark, playerRow, playerColumn,enemyMark,enemyRow,enemyColumn);//draw metodus meghivasa
			
			//szeparator kirajzolasa es varakozas
			addSomeDelay(200L,iterationNumber);
		}
	}
		
/*--------METODUSOK----------------*/
									//parameter valtozo
	private static void addSomeDelay(long timeout, int iterationNumber) throws InterruptedException {
		System.out.println("-----------" +  iterationNumber +"--------------");
		Thread.sleep(timeout);
	}
	
	
	static int[] makeMove(Direction direction, String [][]level,int row, int column){
		//mozgatast vegzo logika
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
		return new int [] {row,column};
	}
	
	    // ez csak egy metodus definicio, elöbb meg kell hivni!
		static void initlevel(String[][]level){
			//palya inicializalasa, az a logika, ami szoközökkel tölti fel a palyat
			for(int row = 0 ; row < level.length; row++) {
				for (int column = 0; column < level[row].length; column++) {
					if (row == 0 || row == height-1 || column == 0 || column == width-1) {//fal körben x-böl
						level[row][column] = "X ";
					} else {
						level[row][column] = " ";
					}
				}
			}			
		}
	
		static Direction changeDirection(Direction direction) {
			                              /*egyetlen bemeneti parameter, tipus es nev, parametervaltozo*/
			/*a metodus neve ele a visszteresi ertek tipusa kerul*/	
			switch (direction) {
			case RIGHT: 
				return  Direction.DOWN;
				
			case DOWN: 
				return Direction.LEFT;
			
			case LEFT: 
				return Direction.UP;
				
			case UP: 
				return Direction.RIGHT;
				
			}
			return direction;
		}
		
		//palya es jatekos kirajzolasa
		static void draw(String [][] board, String playerMark, int playerRow, int playerColumn,String enemyMark, int enemyRow,int enemyColumn){
			for (int row = 0; row < height; row++) {
				for (int column = 0;column < width; column++) {
					if(row == playerRow && column == playerColumn) {		
					System.out.print(playerMark);
				} else if (row == enemyRow && column == enemyColumn){
					System.out.print(enemyMark);
				}	else {
				   System.out.print(board[row][column]);
				}
			}
			System.out.println();	
			}			
		}
}
		

	
	

