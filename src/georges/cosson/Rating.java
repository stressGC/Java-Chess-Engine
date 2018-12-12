package georges.cosson;

import java.util.Random;

public class Rating {
	//static int score = 0;
	static int max = 200;
	static int min = 0;
	static int[] scores = {55, 50, 40, 37, 39, 45, 11, -2, 2, 7, 59};
	static Random rand = new Random(); 
	
	static int PAWN_VALUE = 100;
	static int KNIGHT_VALUE = 325;
	static int BISHOP_VALUE = 330;
	static int ROOK_VALUE = 500;
	static int QUEEN_VALUE = 900;
	static int KING_VALUE = 20000;
	
    public static int evaluate(boolean whiteTurn, long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK,long EP,boolean CWK,boolean CWQ,boolean CBK,boolean CBQ) {
    	//int score = rand.nextInt(max - min + 1) + min;
    	//score++;
    	/*
    	if ((WK & Moves.unsafeForWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK)) == 0 && whiteTurn) {
    		System.out.println("==");
    		return -Engine.MATE_SCORE;
    	}
    	if ((WK & Moves.unsafeForWhite(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK)) == 0 && !whiteTurn) {
    		return Engine.MATE_SCORE;
    	}
    	if ((BK & Moves.unsafeForBlack(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK)) == 0 && !whiteTurn) {
    		return -Engine.MATE_SCORE;
    	}
    	if ((BK & Moves.unsafeForBlack(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK)) == 0 && whiteTurn) {
    		return Engine.MATE_SCORE;
    	}*/
    	
    	
    	int score = 0;
    	score += whiteTurn ? getEvaluation(WP, BP, PAWN_VALUE) : -getEvaluation(WP, BP, PAWN_VALUE);
    	score += whiteTurn ? getEvaluation(WN, BN, KNIGHT_VALUE) : -getEvaluation(WN, BN, KNIGHT_VALUE);
    	score += whiteTurn ? getEvaluation(WB, BB, BISHOP_VALUE) : -getEvaluation(WB, BB, BISHOP_VALUE);
    	score += whiteTurn ? getEvaluation(WR, BR, ROOK_VALUE) : -getEvaluation(WR, BR, ROOK_VALUE);
    	score += whiteTurn ? getEvaluation(WQ, BQ, QUEEN_VALUE) : -getEvaluation(WQ, BQ, QUEEN_VALUE);
    	//System.out.println("whiteTurn={"+whiteTurn+"}" + " score = " + score + ", whiteP={" + numberOfOnes(WP) + "} " + ", blackP={" + numberOfOnes(BP) + "} ");
    	//if (((WKt&Moves.unsafeForWhite(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && WhiteToMove) ||
        //((BKt&Moves.unsafeForBlack(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && !WhiteToMove)) {
    	
    	//int indexRnd = rand.nextInt(scores.length);
        //return scores[indexRnd];
    	return score;
    }
    
    static int numberOfOnes(long aa) {
    	return Long.bitCount(aa);
    }
    
    static int getEvaluation(Long W, Long B, int valuePerCapita) {
    	int scoreW = numberOfOnes(W);
    	int scoreB = numberOfOnes(B);
    	int difference = scoreW - scoreB;
    	return difference * valuePerCapita;
    }

}