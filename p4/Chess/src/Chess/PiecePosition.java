package Chess;

public class PiecePosition {

	/**
	 * Función que recibe una fila y una columna y comprueba si dicha posición se encuentra
         * dentro del tablero (8x8).
	 * @param column indica la columna del tablero.
	 * @param row indica la fila del tablero.
	 * @return true si la posición se encuentra dentro de las 8 columnas y las 8 filas del tablero
         *       o false si no es así.
	 */
	public static boolean isAvailable(int column, int row) {
		return column >= 0 && column < 8 && row >= 0 && row < 8;
	}

	/**
	 * Función que comprueba si hay una pieza en una posición concreta del tablero y que el movimiento
         * que realice dicha pieza vaya a una casilla válida dentro del tablero.
	 * @param position indica una posición dentro del tablero (columna y fila).
	 * @param columnIncrement indica la nueva columna a la que se moverá la pieza.
         * @param rowIncrement indica la nueva fila a la que se moverá la pieza.
         * @return false si la posición inicial no contiene ninguna pieza o si la nueva posición
         *         de la pieza no se encuentra dentro del tablero llamando a la anterior función 
         *         con la posición incrementada.
         *       o true si existe una pieza en la posición inicial y además la casilla a la que se mueve 
         *         tambien está dentro del tablero. 
         * Esta función no tiene encuenta que el movimiento de la pieza sea el correspondiente o si 
         * la casilla a la que se dirije está ocupada. 
	 */
	static boolean isAvailable(PiecePosition position, int columnIncrement, int rowIncrement) {
		if (position == null)
			return false;
		
		int newColumn = position.getColumn() + columnIncrement;
		int newRow = position.getRow() + rowIncrement;
		return isAvailable(newColumn, newRow);
	}

	/**
	 * Función que recibe una posición en el tablero y comprueba que haya una pieza 
         * y obtiene laa fila y columna para llamar de nuevo a la otra función que compruebe
         * que se encuentra dentro del tablero (8x8).
	 * @param position indica una posición compuesta de fila y columna.
	 * @return true cuando la posición se encuenra dentro del tablero y contiene una ficha.
	 */
	static boolean isAvailable(PiecePosition position) {
		if (position == null)
			return false;
		return isAvailable(position.getColumn(), position.getRow());
	}

	private int column, row;

	/***
         * Función que guarda el número de columna y de fila que recibe.
         * @param column contiene un número de columna.
         * @param row contiene un número de fila.
         */
	public PiecePosition(int column, int row) {
		this.column = column;
		this.row = row;
	}
	
	/***
         * Función que devuelve el número de columna guardado.
         * @return número columna.
         */
	public int getColumn() {
		return column;
	}

	/***
         * Función que devuelve el número de fila guardado.
         * @return número fila.
         */
	public int getRow() {
		return row;
	}
	
        /***
         * Función que recibe dos valores, columna y fila y en el caso de que estos valores
         * sean correctos y estén dentro del tablero (8x8) y devuelve true.
         * @param column indica el número de columna.
         * @param row indica el número de fila.
         * @return true cuando la columna y la fila recibida están dentro de los limites del tablero.
         */
	public boolean setValues(int column, int row) {
		if (isAvailable(column, row)) {
			this.column = column;
			this.row = row;			
			return true;
		}
		return false;
	}
	
        /***
         * Función que comprueba si la posición a la que se desplaza la pieza es correcta
         * @param columnCount--> Recibe el valor de la columna a la que debe moverse
         * @param rowCount--> Recibe el valor de la fila a la que debe moverse
         * @return --> devuelve null si la posición a la que se mueve no está disponible, 
         *             o la posición final a la que la pieza se mueve
         */
	public PiecePosition getDisplacedPiece(int columnCount, int rowCount) {		
		if (!isAvailable(this, columnCount, rowCount))
			return null;
		int newColumn = getColumn() + columnCount;
		int newRow = getRow() + rowCount;
		return new PiecePosition(newColumn, newRow);
	}
	
	/***
         * Función que devuelve una posición establecida por el numero de columna y fila
         * guardados en la función setValues(int column, int row)
         * @return PiecePosition(column, row)
         */
	public PiecePosition clone() {
		return new PiecePosition(column, row);
	}
	
	/***
         * Funcion que recibe una posición y devuelve verdadero si dicha posición coincide
         * con la posición que tenemos guardada (tanto la fila como la columna). 
         * @param aPosition contiene una posición
         * @return true cuando la posición recibida es igual a la posición almacenada.
         */
	public boolean equals(PiecePosition aPosition) {
		return aPosition.getColumn() == getColumn() && aPosition.getRow() == getRow();
	}
}
