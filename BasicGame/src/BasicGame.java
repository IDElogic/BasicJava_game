import java.util.Random;

public class BasicGame {
	
	//osztalyon belul,(de metodusokon kivul)Osztalyvaltozok_Static ehhez fontos	
	static final int GAME_LOOP_NUMBER = 1;//statikus meg final is: forditasi ideju konstans
	static final int HEIGHT = 40;
	static final int WIDTH = 40;
//	static final Random RANDOM = new Random	(100L);//álvéletlen számot general
	static final Random RANDOM = new Random	(103L);
	
	public static void main(String[] args) throws InterruptedException {
		//palya inicializalasa
				String [][] level = new String [HEIGHT][WIDTH];//helyi valtozo(metoduson belul van letrehozva)
				do {
				initlevel(level); //initlevel metodus meghivas(bemeneti parameterei)	
				addRandomWalls(level);	
				}while (!isPassable(level));
				
		String playerMark = "O";//string tipusu helyi valtozo, inicializalva a nagy O beture
		int [] playerStartingCoordinates = getRandomStartingCoordinates(level);
		int playerRow = playerStartingCoordinates[0];//helyi valtozo
		int playerColumn = playerStartingCoordinates[1];//helyi valtozo
		Direction playerDirection = Direction.RIGHT;//helyi valtozo (enum tipusa es a konstansa		
		
		//masik jatekos
		String enemyMark = "B";
		int [] enemyStartingCoordinates =  getRandomStartingCoordinatesAtLeastCertainDistance(level,playerStartingCoordinates, playerColumn);
		int enemyRow = enemyStartingCoordinates[0];//helyi valtozo
		int enemyColumn = enemyStartingCoordinates[1];//helyi valtozo
		Direction enemyDirection = Direction.LEFT;	
		
		//powerUp
				String powerUpMark = "$";
				int [] powerUpStartingCoordinates = getRandomStartingCoordinates(level);
				int powerUpRow = powerUpStartingCoordinates[0];//helyi valtozo
				int powerUpColumn = powerUpStartingCoordinates[1];//helyi valtozo
				boolean powerUpPresentOnLevel = false;
				int powerUpPresenceCounter = 0;
				boolean powerUpActive = false;
				int powerUpActiveCounter = 0;
		
		GameResult gameResult = GameResult.TIE;
		
		//iranyvaltoztatos logika
		for (int iterationNumber = 1; iterationNumber <= GAME_LOOP_NUMBER; iterationNumber++) {
			//jatekos leptetese
			if(powerUpActive) {
				playerDirection = changeDirectionTowards(level, playerDirection, playerRow,playerColumn, enemyRow, enemyColumn);
			}else {
				if (powerUpPresentOnLevel) {
					playerDirection = changeDirectionTowards(level, playerDirection, playerRow,playerColumn,powerUpRow, powerUpColumn);	
				}else {
					if(iterationNumber % 15 == 0) {
						playerDirection = changeDirection(playerDirection);
				}
			  }
			}	
			//mozgatast vegzo logika
			int[] playerCoordinates = makeMove(playerDirection, level, playerRow, playerColumn);//makeMove metodus meghivasa
			playerRow = playerCoordinates[0];
			playerColumn = playerCoordinates[1];
			
			//masik jatekos leptetese
//			if(iterationNumber % 10 == 0) {
//				enemyDirection = changeDirection(enemyDirection);
//			}
			if(powerUpActive) {
				Direction directionTowardsPlayer= changeDirectionTowards(level,enemyDirection,enemyRow, enemyColumn, playerRow, playerColumn);
				enemyDirection = getEscapeDirection(level, enemyRow, enemyColumn, directionTowardsPlayer);
			}else {
				enemyDirection = changeDirectionTowards(level,enemyDirection,enemyRow, enemyColumn, playerRow, playerColumn);
			}
			if(iterationNumber % 2 == 0) {
			int[] enemyCoordinates = makeMove(enemyDirection, level,enemyRow, enemyColumn);//makeMove metodus meghivasa
			enemyRow = enemyCoordinates[0];
			enemyColumn = enemyCoordinates[1];
			}
			
			
			//powerUp frissitese
			if (powerUpActive) {
				powerUpActiveCounter++;
			} else {
				powerUpPresenceCounter++;
			}
			if(powerUpPresenceCounter >= 20) {
				if (powerUpPresentOnLevel) {
					powerUpStartingCoordinates = getRandomStartingCoordinates(level);
					powerUpRow = powerUpStartingCoordinates[0];//helyi valtozo
					powerUpColumn = powerUpStartingCoordinates[1];//helyi valtozo
				}
				powerUpPresentOnLevel = !powerUpPresentOnLevel;
				powerUpPresenceCounter = 0;
			}
			if (powerUpActiveCounter >= 20) {
				powerUpActive = false;
				powerUpActiveCounter = 0;
				powerUpStartingCoordinates = getRandomStartingCoordinates(level);
				powerUpRow = powerUpStartingCoordinates[0];//helyi valtozo
				powerUpColumn = powerUpStartingCoordinates[1];//helyi valtozo
			}
			
			//	jatekos powerUp interakcio lekezelese
			if (powerUpPresentOnLevel && playerRow == powerUpRow && playerColumn == powerUpColumn) {
				powerUpActive = true;
				powerUpPresentOnLevel = false;
				powerUpPresenceCounter = 0;
				
			}
			//palya es jatekos kirajzolasa
			draw(level, playerMark, playerRow, playerColumn,enemyMark,enemyRow,enemyColumn, powerUpMark, powerUpRow, powerUpColumn, powerUpPresentOnLevel,powerUpActive );//draw metodus meghivasa
			
			//szeparator kirajzolasa es varakozas
			addSomeDelay(200L,iterationNumber);
			
			//jatekos azonos koordinatakon (elkapja az ellenfel a jatekost)
			if(playerRow == enemyRow && playerColumn == enemyColumn) {
				if (powerUpActive) {
					gameResult = GameResult.WIN;
				} else {
					gameResult = GameResult.LOSE;
				}
				break;
			}
			}
			switch (gameResult) {
			case WIN:
			  System.out.println("Gratulalok! Gyöztel! ");
			break;
			case LOSE:
			  System.out.println(" Sajnalom,vesztettel! ");
			break;
			case TIE:
				 System.out.println("Döntetlen! ");
			break;
			}
			}
	
		static boolean isPassable(String[][] level) {
			//palya lemasolasa
			String [][] levelCopy = copy(level);
			//megkeresem, hogy hol van az elsö szoköz
			outer: for (int row=0; row<HEIGHT; row++) {
				for(int column=0; column<WIDTH; column++) {
					if(" ".equals(levelCopy[row][column])) {
						levelCopy[row][column]= "*";
						break outer;
					}
				}
			}
			//*ok terjesztese a szabad helyekre
			for (int row = 0; row < HEIGHT; row++) {
				for(int column = 0; column < WIDTH; column++) {
					boolean change = false;
					if("*".equals(levelCopy[row][column])) {
						if(" ".equals(levelCopy[row-1][column])) {
							levelCopy[row-1][column] = "*";
							change = true;
						}
						if(" ".equals(levelCopy[row+1][column])) {
							levelCopy[row+1][column] = "*";
							change = true;
						}
						if(" ".equals(levelCopy[row][column-1])) {
							levelCopy[row][column-1] = "*";
							change= true;
						}
						if(" ".equals(levelCopy[row][column+1])) {
							levelCopy[row][column+1] = "*";
							change = true;
						}
					}
					if (change) {
						//palyamasolat kirajzolasa
						for (int row2 = 0; row2 < HEIGHT; row2++) {
							for (int column2 = 0;column2 < WIDTH; column2++) {
							   System.out.print(levelCopy[row2][column2]);		
							}
						System.out.println();	
						}	
					}
				}
			}		
			//program leallitasa
			System.exit(0);
			return false;
		}


		static String[][] copy(String[][] level) {
			String [][] copy = new String [HEIGHT][WIDTH];
			for (int row=0; row<HEIGHT; row++) {
				for(int column=0; column<WIDTH; column++) {
					copy[row][column] = level [row][column];
				}
			}
			return copy;
		}

		static Direction getEscapeDirection(String[][] level, int enemyRow, int enemyColumn,
			Direction directionTowardsPlayer) {
		Direction escapeDirection = getOppositeDirection(directionTowardsPlayer);
		switch (escapeDirection) {
		case UP: 
			if (level[enemyRow-1][enemyColumn].equals(" ")){
			return Direction.UP;
		}else if (level[enemyRow][enemyColumn-1].equals(" ")){
			return Direction.LEFT;
		}else if (level[enemyRow][enemyColumn+1].equals(" ")){
			return Direction.RIGHT;
		}else {
			return Direction.UP; 
		}
		case DOWN: 
			if (level[enemyRow+1][enemyColumn].equals(" ")){
				return Direction.DOWN;
			}else if (level[enemyRow][enemyColumn-1].equals(" ")){
				return Direction.LEFT;
			}else if (level[enemyRow][enemyColumn+1].equals(" ")){
				return Direction.RIGHT;
			}else {
				return Direction.DOWN; 
			}
		case RIGHT: 
			if (level[enemyRow][enemyColumn+1].equals(" ")){
				return Direction.RIGHT;
			}else if (level[enemyRow-1][enemyColumn].equals(" ")){
				return Direction.UP;
			}else if (level[enemyRow+1][enemyColumn].equals(" ")){
				return Direction.DOWN;
			}else {
				return Direction.RIGHT; 
			}
		case LEFT: 
			if (level[enemyRow][enemyColumn-1].equals(" ")){
				return Direction.LEFT;
			}else if (level[enemyRow-1][enemyColumn].equals(" ")){
				return Direction.UP;
			}else if (level[enemyRow+1][enemyColumn].equals(" ")){
				return Direction.DOWN;
			}else {
				return Direction.LEFT; 
			}
		default:
			return escapeDirection;
		}
		}

		static Direction getOppositeDirection(Direction direction) {
		 switch (direction) {
			case UP: return Direction.DOWN;
			case DOWN: return Direction.UP;
			case RIGHT: return Direction.LEFT;
			case LEFT: return Direction.RIGHT;
			default:
				return direction;
			}		
		}

/*--------METODUSOK----------------*/
		static int[]  getRandomStartingCoordinatesAtLeastCertainDistance(String[][] level, int[] playerStartingCoordinates, int distance) {
		int playerStartingRow = playerStartingCoordinates [0];
		int playerStartingColumn = playerStartingCoordinates [1];
		int randomRow;
		int randomColumn;
		int counter = 0;
		do {
			randomRow = RANDOM.nextInt(HEIGHT);
			randomColumn = RANDOM.nextInt(WIDTH);	
		} while(counter++ < 1_000 
				&& (!level[randomRow][randomColumn].equals(" ") || calculateDistance(randomRow, randomColumn, playerStartingRow, playerStartingColumn)<10));
		return new int [] {randomRow,randomColumn};
		
	}

	static int calculateDistance(int row1, int column1, int row2,
			int column2) {
		int rowDifference = Math.abs(row1 -row2);
		int columnDifference = Math.abs(column1 -column2);
		return rowDifference + columnDifference ;
	}

	static int[] getRandomStartingCoordinates(String[][] level) {
		int randomRow;
		int randomColumn;
		do {
			randomRow = RANDOM.nextInt(HEIGHT);
			randomColumn = RANDOM.nextInt(WIDTH);	
		} while(!level[randomRow][randomColumn].equals(" "));
		return new int [] {randomRow,randomColumn};
	}



	static Direction changeDirectionTowards( String [] [] level, Direction originalEnemyDirection, int enemyRow, int enemyColumn, int playerRow, int playerColumn ) {
		if(playerRow < enemyRow && level[enemyRow-1][enemyColumn].equals(" ")) {
		return Direction.UP;
		}
			if(playerRow > enemyRow && level[enemyRow+1][enemyColumn].equals(" ")) {
			return Direction.DOWN;
		}
		if(playerColumn < enemyColumn && level[enemyRow][enemyColumn -1].equals(" ")) {
			return Direction.LEFT;
			}
			if(playerColumn > enemyColumn && level[enemyRow][enemyColumn +1].equals(" ")) {
				return Direction.RIGHT;
			}
			return originalEnemyDirection;
	}
	
	//---------------METODUS OVERLOADING---------------
	//amikor pontosan ua. a neve, csak mas a parameterlistaja
	static void addRandomWalls(String [][] level) {
		addRandomWalls(level, 5, 5);
	}
	
	static void addRandomWalls(String [][] level, int numberOfHorizontalWalls,int numberOfVerticalWalls) {
	
	for (int i = 0; i < numberOfHorizontalWalls; i++) {
		addHorizontalWall(level);
	}
	for (int i = 0; i < numberOfVerticalWalls; i++) {
		addVerticalWall(level);
	}
  }
	
	static void addHorizontalWall(String [][] level) {
		
		int wallWidth = RANDOM.nextInt(WIDTH -3);	
		int wallRow = RANDOM.nextInt(HEIGHT -2) + 1;
		int wallColumn = RANDOM.nextInt(WIDTH-2 -wallWidth);
		for (int i = 0; i < wallWidth; i++ ) {
			level [wallRow][wallColumn+i]= "X";
			
		}
	}
	
	
	static void addVerticalWall(String [][] level) {
		int wallHeight = RANDOM.nextInt(HEIGHT -3);	
		int wallRow = RANDOM.nextInt(HEIGHT-2 -wallHeight);
		int wallColumn = RANDOM.nextInt(WIDTH -2) + 1;	
		for (int i = 0; i < wallHeight; i++ ) {
			level [wallRow + i][wallColumn] = "X";
			
		}
		
	}
	
							//parameter valtozok
	static void addSomeDelay(long timeout, int iterationNumber) throws InterruptedException {
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
					if (row == 0 || row == HEIGHT-1 || column == 0 || column == WIDTH-1) {//fal körben x-böl
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
		static void draw(String [][] board, String playerMark, int playerRow, int playerColumn,String enemyMark, 
				int enemyRow,int enemyColumn,String powerUpMark,int powerUpRow,int powerUpColumn,boolean powerUpPresentOnLevel, boolean powerUpActive){
			for (int row = 0; row < HEIGHT; row++) {
				for (int column = 0;column < WIDTH; column++) {
					if(row == playerRow && column == playerColumn) {		
					System.out.print(playerMark);
				} else if (row == enemyRow && column == enemyColumn){
					System.out.print(enemyMark);
				} else if (powerUpPresentOnLevel && row == powerUpRow && column == powerUpColumn){
					System.out.print(powerUpMark);
				}	else {
				   System.out.print(board[row][column]);
				}
			}
			System.out.println();	
			}
			if(powerUpActive) {
				System.out.println("A power-up activ");
			}
		}					
}
		

	
	

