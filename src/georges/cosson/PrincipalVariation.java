package georges.cosson;

/*
 * implementation of the principal variation search algorithm (= NegaScout)
 */

public class PrincipalVariation {
	
	/*
	 * zero/null window search algorithm
	 * returns either beta-1 or beta
	 */
    public static int zWSearch(int beta, long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, long EP, boolean CWK, boolean CWQ, boolean CBK, boolean CBQ, boolean WhiteToMove, int depth) {
        
    	// instantiate score to the worst value possible
    	int score = Engine.NULL_INT;
    	
        // we consider alpha == beta - 1
    	
        if (depth == Engine.searchDepth)
        {
            score = Rating.evaluate(WhiteToMove, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ);
            return score;
        }
        
        // will contain all possible moves
        String moves;
        
        // compute possible moves based on player color
        if (WhiteToMove) {
            moves = Moves.possibleMovesW(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ);
        } else {
            moves = Moves.possibleMovesB(WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ);
        }
        
        /*
         * big improvement can be done there:
         * sorting the moves
         */
        
        // for each move
        for (int i = 0; i < moves.length(); i += 4) {
        	
        	// make the move
        	String substring = moves.substring(i,i+4);
            long WPt = Moves.makeMove(WP, substring, 'P'), WNt = Moves.makeMove(WN, substring, 'N'),
                    WBt = Moves.makeMove(WB, substring, 'B'), WRt = Moves.makeMove(WR, substring, 'R'),
                    WQt = Moves.makeMove(WQ, substring, 'Q'), WKt = Moves.makeMove(WK, substring, 'K'),
                    BPt = Moves.makeMove(BP, substring, 'p'), BNt = Moves.makeMove(BN, substring, 'n'),
                    BBt = Moves.makeMove(BB, substring, 'b'), BRt = Moves.makeMove(BR, substring, 'r'),
                    BQt = Moves.makeMove(BQ, substring, 'q'), BKt = Moves.makeMove(BK, substring, 'k'),
                    EPt = Moves.makeMoveEP(WP|BP,moves.substring(i,i+4));
            
            WRt = Moves.makeMoveCastle(WRt, WK | BK, substring, 'R');
            BRt = Moves.makeMoveCastle(BRt, WK | BK, substring, 'r');
            
            boolean CWKt = CWK, CWQt = CWQ, CBKt = CBK, CBQt = CBQ;
            
            // check if 'regular' move
            if (Character.isDigit(moves.charAt(i + 3))) {
                int start = (Character.getNumericValue(moves.charAt(i)) * 8) + (Character.getNumericValue(moves.charAt(i + 1)));
                if (((1L << start) & WK) != 0) { CWKt = false; CWQt = false; }
                else if (((1L << start) & BK) != 0) { CBKt = false; CBQt = false; }
                else if (((1L << start) & WR & (1L << 63)) != 0) { CWKt=false; }
                else if (((1L << start) & WR & (1L << 56)) != 0) { CWQt=false; }
                else if (((1L << start) & BR & (1L << 7)) != 0) { CBKt=false; }
                else if (((1L << start) & BR & 1L) != 0) { CBQt = false; }
            }
            
            // check for illegal moves
            if (((WKt & Moves.unsafeForWhite(WPt, WNt, WBt, WRt, WQt, WKt, BPt, BNt, BBt, BRt, BQt, BKt)) == 0 && WhiteToMove) ||
                    ((BKt & Moves.unsafeForBlack(WPt, WNt, WBt, WRt, WQt, WKt, BPt, BNt, BBt, BRt, BQt, BKt)) == 0 && !WhiteToMove)) {
            	
            	// zero-window search on the move
                score = -zWSearch(1 - beta, WPt, WNt, WBt, WRt, WQt, WKt, BPt, BNt, BBt, BRt, BQt, BKt, EPt, CWKt, CWQt, CBKt, CBQt, !WhiteToMove, depth + 1);
            }
            
            // fail-hard beta-cutoff
            if (score >= beta)
            {	
                return score;
            }
        }
        
        //fail-hard, return alpha
        return beta - 1;
    }
    
    // returns the index of the first legal move
    public static int getFirstLegalMove(String moves, long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, long EP, boolean CWK, boolean CWQ, boolean CBK, boolean CBQ, boolean WhiteToMove) {
        
    	// for each move
    	for (int i = 0; i < moves.length(); i += 4) {
        	
    		// make the move
    		String substring = moves.substring(i, i + 4);
            long WPt = Moves.makeMove(WP, substring, 'P'), WNt = Moves.makeMove(WN, substring, 'N'),
                    WBt = Moves.makeMove(WB, substring, 'B'), WRt = Moves.makeMove(WR, substring, 'R'),
                    WQt = Moves.makeMove(WQ, substring, 'Q'), WKt = Moves.makeMove(WK, substring, 'K'),
                    BPt = Moves.makeMove(BP, substring, 'p'), BNt = Moves.makeMove(BN, substring, 'n'),
                    BBt = Moves.makeMove(BB, substring, 'b'), BRt = Moves.makeMove(BR, substring, 'r'),
                    BQt = Moves.makeMove(BQ, substring, 'q'), BKt = Moves.makeMove(BK, substring, 'k');
            WRt = Moves.makeMoveCastle(WRt, WK | BK, substring, 'R');
            BRt = Moves.makeMoveCastle(BRt, WK | BK, substring, 'r');
            
            if (((WKt & Moves.unsafeForWhite(WPt, WNt, WBt, WRt, WQt, WKt, BPt, BNt, BBt, BRt, BQt, BKt)) == 0 && WhiteToMove) ||
                    ((BKt & Moves.unsafeForBlack(WPt, WNt, WBt, WRt, WQt, WKt, BPt, BNt, BBt, BRt, BQt, BKt)) == 0 && !WhiteToMove)) {
                return i;
            }
        }
    	
    	// no legal move
        return -1;
    }
    
    /*
     * principal variation search algorithm
     */
    public static int pvSearch(int alpha, int beta, long WP, long WN, long WB, long WR, long WQ, long WK, long BP, long BN, long BB, long BR, long BQ, long BK, long EP, boolean CWK, boolean CWQ, boolean CBK, boolean CBQ, boolean WhiteToMove, int depth) {
        
    	//initialise variables
    	int bestScore; // keeps track of the best score computed
    	
    	// if we reached the maximum search depth, return the score evaluation
        if (depth == Engine.searchDepth)
        {
            bestScore = Rating.evaluate(WhiteToMove, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ);
            return bestScore;
        }
        
        // contains all possible moves
        String moves;
        
        // computes all possibles moves depending on the player color 
        if (WhiteToMove) {
            moves = Moves.possibleMovesW(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ);
        } else {
            moves = Moves.possibleMovesB(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ);
        }
        
        /*
         * big improvement can be done there:
         * sorting the moves
         */
        
        // first legal move from the moves
        int firstLegalMove = getFirstLegalMove(moves,WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ,WhiteToMove);
        
        // if no move is possible, then its checkmate
        if (firstLegalMove == -1)
        {
            return WhiteToMove ? Engine.MATE_SCORE : -Engine.MATE_SCORE;
        }
        
        // we make that move
        String substring = moves.substring(firstLegalMove,firstLegalMove+4);
        
        long WPt = Moves.makeMove(WP, substring, 'P'), WNt = Moves.makeMove(WN, substring, 'N'),
                WBt = Moves.makeMove(WB, substring, 'B'), WRt = Moves.makeMove(WR, substring, 'R'),
                WQt = Moves.makeMove(WQ, substring, 'Q'), WKt = Moves.makeMove(WK, substring, 'K'),
                BPt = Moves.makeMove(BP, substring, 'p'), BNt = Moves.makeMove(BN, substring, 'n'),
                BBt = Moves.makeMove(BB, substring, 'b'), BRt = Moves.makeMove(BR, substring, 'r'),
                BQt = Moves.makeMove(BQ, substring, 'q'), BKt = Moves.makeMove(BK, substring, 'k'),
                EPt = Moves.makeMoveEP(WP | BP, substring);
        
        WRt = Moves.makeMoveCastle(WRt, WK | BK, substring, 'R');
        BRt = Moves.makeMoveCastle(BRt, WK | BK, substring, 'r');
        
        boolean CWKt = CWK, CWQt = CWQ, CBKt = CBK, CBQt = CBQ;
        
        //'regular' move
        if (Character.isDigit(moves.charAt(firstLegalMove + 3))) {
            int start = (Character.getNumericValue(moves.charAt(firstLegalMove)) * 8) + (Character.getNumericValue(moves.charAt(firstLegalMove + 1)));
            if (((1L << start) & WK) != 0) { CWKt = false; CWQt = false; }
            else if (((1L << start) & BK) != 0) { CBKt = false; CBQt = false; }
            else if (((1L << start) & WR & (1L << 63)) != 0) { CWKt = false; }
            else if (((1L << start) & WR & (1L << 56)) != 0) { CWQt = false; }
            else if (((1L << start) & BR & (1L << 7)) != 0) { CBKt = false; }
            else if (((1L << start) & BR & 1L) != 0) { CBQt = false; }
        }
        
        // evaluate the position, complete search
        // increase depth by one, switch player color
        bestScore = -pvSearch(-beta,-alpha,WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt,EPt,CWKt,CWQt,CBKt,CBQt,!WhiteToMove,depth+1);
        
        // keep track on how efficient algorithm is
        Engine.moveCounter++;
        
        // if this move produces checkmate
        if (Math.abs(bestScore) == Engine.MATE_SCORE)
        {
        	// then its the best move for sure
            return bestScore;
        }
        
        if (bestScore > alpha)
        {
            if (bestScore >= beta)
            {
                //This is a refutation move
                //It is not a PV move
                //However, it will usually cause a cutoff so it can
                //be considered a best move if no other move is found
                return bestScore;
            }
            alpha = bestScore;
        }
        
        // loop through all subsequent moves
        for (int i = firstLegalMove + 4; i < moves.length(); i += 4) {
        	
        	// represent the current move evaluated score
            int score;
            Engine.moveCounter++;
            
            // making the move
            String moveSubstring = moves.substring(i, i + 4);
            WPt = Moves.makeMove(WP, moveSubstring, 'P');
            WNt = Moves.makeMove(WN, moveSubstring, 'N');
            WBt = Moves.makeMove(WB, moveSubstring, 'B');
            WRt = Moves.makeMove(WR, moveSubstring, 'R');
            WQt = Moves.makeMove(WQ, moveSubstring, 'Q');
            WKt = Moves.makeMove(WK, moveSubstring, 'K');
            BPt = Moves.makeMove(BP, moveSubstring, 'p');
            BNt = Moves.makeMove(BN, moveSubstring, 'n');
            BBt = Moves.makeMove(BB, moveSubstring, 'b');
            BRt = Moves.makeMove(BR, moveSubstring, 'r');
            BQt = Moves.makeMove(BQ, moveSubstring, 'q');
            BKt = Moves.makeMove(BK, moveSubstring, 'k');
            EPt = Moves.makeMoveEP(WP|BP, moveSubstring);
            WRt = Moves.makeMoveCastle(WRt, WK|BK, moveSubstring, 'R');
            BRt = Moves.makeMoveCastle(BRt, WK|BK, moveSubstring, 'r');
            CWKt = CWK;
            CWQt = CWQ;
            CBKt = CBK;
            CBQt = CBQ;
            
            //'regular' move
            if (Character.isDigit(moves.charAt(i+3))) {
                int start = (Character.getNumericValue(moves.charAt(i)) * 8) + (Character.getNumericValue(moves.charAt(i + 1)));
                if (((1L << start) & WK) != 0) { CWKt = false; CWQt = false; }
                else if (((1L << start) & BK) != 0) { CBKt = false; CBQt = false; }
                else if (((1L << start) & WR & (1L <<63)) != 0) { CWKt = false; }
                else if (((1L << start) & WR & (1L <<56)) != 0) { CWQt = false; }
                else if (((1L << start) & BR & (1L <<7)) != 0) { CBKt= false; }
                else if (((1L << start) & BR & 1L) != 0) { CBQt=false; }
            }
            
            // setting the score to zero window search
            // faster than PVSearch for the computer
            score = -zWSearch(-alpha, WPt, WNt, WBt, WRt, WQt, WKt, BPt, BNt, BBt, BRt, BQt, BKt, EPt, CWKt, CWQt, CBKt, CBQt, !WhiteToMove, depth + 1);
            
            // if this move is a candidate for the best move
            if ((score > alpha) && (score < beta))
            {
                //research with window [alpha;beta]
                bestScore = -pvSearch(-beta, -alpha, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ, !WhiteToMove, depth + 1);
                
                if (score > alpha)
                {
                    alpha = score;
                }
            }
            
            if ((score != Engine.NULL_INT) && (score > bestScore))
            {
                if (score >= beta)
                {
                    return score;
                }

                bestScore = score;
                
                if (Math.abs(bestScore) == Engine.MATE_SCORE)
                {
                    return bestScore;
                }
            }
        }

        return bestScore;
    }
}