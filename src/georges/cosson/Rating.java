package georges.cosson;

public class Rating {
	
	// pieces values
	static int PAWN_VALUE = 100;
	static int KNIGHT_VALUE = 325;
	static int BISHOP_VALUE = 330;
	static int ROOK_VALUE = 500;
	static int QUEEN_VALUE = 900;
	static int KING_VALUE = 20000;
	
	/*
	 * given a state of board, returns an estimator of the score
	 * 
	 * simply counts the pieces remaining on both sides, and computes the difference between them
	 */
	
    public static int evaluate(boolean whiteTurn, long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK,long EP,boolean CWK,boolean CWQ,boolean CBK,boolean CBQ) {
    	
    	int score = 0;
    	score += whiteTurn ? getEvaluation(WP, BP, PAWN_VALUE) : -getEvaluation(WP, BP, PAWN_VALUE);
    	score += whiteTurn ? getEvaluation(WN, BN, KNIGHT_VALUE) : -getEvaluation(WN, BN, KNIGHT_VALUE);
    	score += whiteTurn ? getEvaluation(WB, BB, BISHOP_VALUE) : -getEvaluation(WB, BB, BISHOP_VALUE);
    	score += whiteTurn ? getEvaluation(WR, BR, ROOK_VALUE) : -getEvaluation(WR, BR, ROOK_VALUE);
    	score += whiteTurn ? getEvaluation(WQ, BQ, QUEEN_VALUE) : -getEvaluation(WQ, BQ, QUEEN_VALUE);
    	score += whiteTurn ? getEvaluation(WK, BK, KING_VALUE) : -getEvaluation(WK, BK, KING_VALUE);

    	return score;
    }
    
    // returns the difference of score based on a type of piece
    static int getEvaluation(Long W, Long B, int valuePerCapita) {
    	int scoreW = Long.bitCount(W);
    	int scoreB = Long.bitCount(B);
    	return (scoreW - scoreB) * valuePerCapita;
    }

}