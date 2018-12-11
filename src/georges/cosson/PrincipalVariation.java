package georges.cosson;

public class PrincipalVariation {
    public static int zWSearch(int beta,long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK,long EP,boolean CWK,boolean CWQ,boolean CBK,boolean CBQ,boolean WhiteToMove,int depth) {//fail-hard zero window search, returns either beta-1 or beta
        
    	int score = Integer.MIN_VALUE;
        //alpha == beta - 1
        //this is either a cut- or all-node
    	
        if (depth == Engine.searchDepth)
        {
            score = Rating.evaluate(WhiteToMove, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ);
        	//System.out.println("zWSearch : returning evaluation:"+ score +" depth = " + (depth));
            return score;
        }
        
        String moves;
        
        if (WhiteToMove) {
            moves=Moves.possibleMovesW(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ);
        } else {
            moves=Moves.possibleMovesB(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ);
        }
        
        //sortMoves();
        
        // for each moves
        for (int i=0;i<moves.length();i+=4) {
        	//System.out.println("zWSearch : evaluating move :"+ UCI.moveToAlgebra(moves.substring(i,i+4)) +" depth = " + (depth));
        	// make the move
            long WPt=Moves.makeMove(WP, moves.substring(i,i+4), 'P'), WNt=Moves.makeMove(WN, moves.substring(i,i+4), 'N'),
                    WBt=Moves.makeMove(WB, moves.substring(i,i+4), 'B'), WRt=Moves.makeMove(WR, moves.substring(i,i+4), 'R'),
                    WQt=Moves.makeMove(WQ, moves.substring(i,i+4), 'Q'), WKt=Moves.makeMove(WK, moves.substring(i,i+4), 'K'),
                    BPt=Moves.makeMove(BP, moves.substring(i,i+4), 'p'), BNt=Moves.makeMove(BN, moves.substring(i,i+4), 'n'),
                    BBt=Moves.makeMove(BB, moves.substring(i,i+4), 'b'), BRt=Moves.makeMove(BR, moves.substring(i,i+4), 'r'),
                    BQt=Moves.makeMove(BQ, moves.substring(i,i+4), 'q'), BKt=Moves.makeMove(BK, moves.substring(i,i+4), 'k'),
                    EPt=Moves.makeMoveEP(WP|BP,moves.substring(i,i+4));
            WRt=Moves.makeMoveCastle(WRt, WK|BK, moves.substring(i,i+4), 'R');
            BRt=Moves.makeMoveCastle(BRt, WK|BK, moves.substring(i,i+4), 'r');
            boolean CWKt=CWK,CWQt=CWQ,CBKt=CBK,CBQt=CBQ;
            
            if (Character.isDigit(moves.charAt(i+3))) {//'regular' move
                int start=(Character.getNumericValue(moves.charAt(i))*8)+(Character.getNumericValue(moves.charAt(i+1)));
                if (((1L<<start)&WK)!=0) {CWKt=false; CWQt=false;}
                else if (((1L<<start)&BK)!=0) {CBKt=false; CBQt=false;}
                else if (((1L<<start)&WR&(1L<<63))!=0) {CWKt=false;}
                else if (((1L<<start)&WR&(1L<<56))!=0) {CWQt=false;}
                else if (((1L<<start)&BR&(1L<<7))!=0) {CBKt=false;}
                else if (((1L<<start)&BR&1L)!=0) {CBQt=false;}
            }
            
            // ?
            if (((WKt&Moves.unsafeForWhite(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && WhiteToMove) ||
                    ((BKt&Moves.unsafeForBlack(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && !WhiteToMove)) {
            	
                score = -zWSearch(1 - beta,WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt,EPt,CWKt,CWQt,CBKt,CBQt,!WhiteToMove,depth+1);
            }
            if (score >= beta)
            {	
            	//System.out.println("zWSearch => fail-hard : beta cutoff (too good to be true) "+ score + "beta={" + beta + "}");
                return score;//fail-hard beta-cutoff
            }
        }
        //System.out.println("zWSearch => return alpha");
        return beta - 1;//fail-hard, return alpha
    }
    
    // returns the index of the first legal move
    public static int getFirstLegalMove(String moves,long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK,long EP,boolean CWK,boolean CWQ,boolean CBK,boolean CBQ,boolean WhiteToMove) {
        
    	for (int i=0;i<moves.length();i+=4) {
        	
            long WPt=Moves.makeMove(WP, moves.substring(i,i+4), 'P'), WNt=Moves.makeMove(WN, moves.substring(i,i+4), 'N'),
                    WBt=Moves.makeMove(WB, moves.substring(i,i+4), 'B'), WRt=Moves.makeMove(WR, moves.substring(i,i+4), 'R'),
                    WQt=Moves.makeMove(WQ, moves.substring(i,i+4), 'Q'), WKt=Moves.makeMove(WK, moves.substring(i,i+4), 'K'),
                    BPt=Moves.makeMove(BP, moves.substring(i,i+4), 'p'), BNt=Moves.makeMove(BN, moves.substring(i,i+4), 'n'),
                    BBt=Moves.makeMove(BB, moves.substring(i,i+4), 'b'), BRt=Moves.makeMove(BR, moves.substring(i,i+4), 'r'),
                    BQt=Moves.makeMove(BQ, moves.substring(i,i+4), 'q'), BKt=Moves.makeMove(BK, moves.substring(i,i+4), 'k');
            WRt=Moves.makeMoveCastle(WRt, WK|BK, moves.substring(i,i+4), 'R');
            BRt=Moves.makeMoveCastle(BRt, WK|BK, moves.substring(i,i+4), 'r');
            
            if (((WKt&Moves.unsafeForWhite(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && WhiteToMove) ||
                    ((BKt&Moves.unsafeForBlack(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && !WhiteToMove)) {
            	//System.out.println("first legal move : " + moves.substring(i,i+4) + ", index : " + i);
                return i;
            }
        }
    	System.out.println("NO LEGAL MOVE");
        return -1;
    }
    
    public static int pvSearch(int alpha,int beta,long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK,long EP,boolean CWK,boolean CWQ,boolean CBK,boolean CBQ,boolean WhiteToMove,int depth) {//using fail soft with negamax
        
    	//initialise variables
    	int bestScore; // keeps track of the best score computed
    	
        if (depth == Engine.searchDepth) // if we reached the maximum search depth, return the score evaluation
        {
            bestScore = Rating.evaluate(WhiteToMove, WP, WN, WB, WR, WQ, WK, BP, BN, BB, BR, BQ, BK, EP, CWK, CWQ, CBK, CBQ);
        	//System.out.println("max_depth, score : " + bestScore);
            return bestScore;
        }
        
        // contains all possible moves
        String moves;
        
        if (WhiteToMove) {
            moves = Moves.possibleMovesW(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ);
        } else {
            moves = Moves.possibleMovesB(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ);
        }
        
        //sortMoves();
        
        // first legal move from the moves
        int firstLegalMove = getFirstLegalMove(moves,WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ,WhiteToMove);
        
        //System.out.println("first legal move index :" + UCI.moveToAlgebra(moves.substring(firstLegalMove * 4, (firstLegalMove * 4) + 4)) + " depth : " + depth);
        
        if (firstLegalMove == -1)
        {
            return WhiteToMove ? Engine.MATE_SCORE : -Engine.MATE_SCORE;
        }
        
        // we make that move
        long WPt=Moves.makeMove(WP, moves.substring(firstLegalMove,firstLegalMove+4), 'P'), WNt=Moves.makeMove(WN, moves.substring(firstLegalMove,firstLegalMove+4), 'N'),
                WBt=Moves.makeMove(WB, moves.substring(firstLegalMove,firstLegalMove+4), 'B'), WRt=Moves.makeMove(WR, moves.substring(firstLegalMove,firstLegalMove+4), 'R'),
                WQt=Moves.makeMove(WQ, moves.substring(firstLegalMove,firstLegalMove+4), 'Q'), WKt=Moves.makeMove(WK, moves.substring(firstLegalMove,firstLegalMove+4), 'K'),
                BPt=Moves.makeMove(BP, moves.substring(firstLegalMove,firstLegalMove+4), 'p'), BNt=Moves.makeMove(BN, moves.substring(firstLegalMove,firstLegalMove+4), 'n'),
                BBt=Moves.makeMove(BB, moves.substring(firstLegalMove,firstLegalMove+4), 'b'), BRt=Moves.makeMove(BR, moves.substring(firstLegalMove,firstLegalMove+4), 'r'),
                BQt=Moves.makeMove(BQ, moves.substring(firstLegalMove,firstLegalMove+4), 'q'), BKt=Moves.makeMove(BK, moves.substring(firstLegalMove,firstLegalMove+4), 'k'),
                EPt=Moves.makeMoveEP(WP|BP,moves.substring(firstLegalMove,firstLegalMove+4));
        
        WRt=Moves.makeMoveCastle(WRt, WK|BK, moves.substring(firstLegalMove,firstLegalMove+4), 'R');
        BRt=Moves.makeMoveCastle(BRt, WK|BK, moves.substring(firstLegalMove,firstLegalMove+4), 'r');
        boolean CWKt=CWK,CWQt=CWQ,CBKt=CBK,CBQt=CBQ;
        
        if (Character.isDigit(moves.charAt(firstLegalMove+3))) {//'regular' move
            int start=(Character.getNumericValue(moves.charAt(firstLegalMove))*8)+(Character.getNumericValue(moves.charAt(firstLegalMove+1)));
            if (((1L<<start)&WK)!=0) {CWKt=false; CWQt=false;}
            else if (((1L<<start)&BK)!=0) {CBKt=false; CBQt=false;}
            else if (((1L<<start)&WR&(1L<<63))!=0) {CWKt=false;}
            else if (((1L<<start)&WR&(1L<<56))!=0) {CWQt=false;}
            else if (((1L<<start)&BR&(1L<<7))!=0) {CBKt=false;}
            else if (((1L<<start)&BR&1L)!=0) {CBQt=false;}
        }
        
        // evaluate the position, complete search
        // increase depth by one, switch color
        bestScore = -pvSearch(-beta,-alpha,WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt,EPt,CWKt,CWQt,CBKt,CBQt,!WhiteToMove,depth+1);
        
        Engine.moveCounter++; // to keep track on how efficient algorithm is
        
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
        
        // we set the best move index to the first move (only move searched for the moment)
        //bestMoveIndex = firstLegalMove;
        
        // loop through all subsequent moves
        for (int i = firstLegalMove+4; i < moves.length(); i += 4) {
        	
        	// represent the current move evaluated score
            int score;
            Engine.moveCounter++;
            
            // making the move
            WPt=Moves.makeMove(WP, moves.substring(i,i+4), 'P');
            WNt=Moves.makeMove(WN, moves.substring(i,i+4), 'N');
            WBt=Moves.makeMove(WB, moves.substring(i,i+4), 'B');
            WRt=Moves.makeMove(WR, moves.substring(i,i+4), 'R');
            WQt=Moves.makeMove(WQ, moves.substring(i,i+4), 'Q');
            WKt=Moves.makeMove(WK, moves.substring(i,i+4), 'K');
            BPt=Moves.makeMove(BP, moves.substring(i,i+4), 'p');
            BNt=Moves.makeMove(BN, moves.substring(i,i+4), 'n');
            BBt=Moves.makeMove(BB, moves.substring(i,i+4), 'b');
            BRt=Moves.makeMove(BR, moves.substring(i,i+4), 'r');
            BQt=Moves.makeMove(BQ, moves.substring(i,i+4), 'q');
            BKt=Moves.makeMove(BK, moves.substring(i,i+4), 'k');
            EPt=Moves.makeMoveEP(WP|BP,moves.substring(i,i+4));
            WRt=Moves.makeMoveCastle(WRt, WK|BK, moves.substring(i,i+4), 'R');
            BRt=Moves.makeMoveCastle(BRt, WK|BK, moves.substring(i,i+4), 'r');
            CWKt=CWK;
            CWQt=CWQ;
            CBKt=CBK;
            CBQt=CBQ;
            
            if (Character.isDigit(moves.charAt(i+3))) {//'regular' move
                int start=(Character.getNumericValue(moves.charAt(i))*8)+(Character.getNumericValue(moves.charAt(i+1)));
                if (((1L<<start)&WK)!=0) {CWKt=false; CWQt=false;}
                else if (((1L<<start)&BK)!=0) {CBKt=false; CBQt=false;}
                else if (((1L<<start)&WR&(1L<<63))!=0) {CWKt=false;}
                else if (((1L<<start)&WR&(1L<<56))!=0) {CWQt=false;}
                else if (((1L<<start)&BR&(1L<<7))!=0) {CBKt=false;}
                else if (((1L<<start)&BR&1L)!=0) {CBQt=false;}
            }
            
            // setting the score to zero window search
            // faster than PVSearch for the computer
            score = -zWSearch(-alpha,WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt,EPt,CWKt,CWQt,CBKt,CBQt,!WhiteToMove,depth+1);
            //System.out.println("pvSearch (zWSearch) :"+ UCI.moveToAlgebra(moves.substring(i,i+4)) + " ={" + score + "} depth={"+depth+"} alpha={" +alpha+ "}, beta={"+beta+"}");
            
            // if this move is a candidate for the best move
            if ((score > alpha) && (score < beta))
            {
                //research with window [alpha;beta]
            	// search with pvSearch
                bestScore = -pvSearch(-beta,-alpha,WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ,!WhiteToMove,depth+1);
                //System.out.print("pvSearch : candidate :"+ UCI.moveToAlgebra(moves.substring(i,i+4)) + " => " + bestScore + " vs " + score + "  //// ");
                
                // if this move is the new best move,
                // set bestMoveIndex to i
                
                if (score>alpha)
                {
                    //bestMoveIndex = i;
                    alpha = score;
                }
            }
            
            //
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
        
        //System.out.println("=========================================");
        //System.out.println("final return : " + UCI.moveToAlgebra(moves.substring((bestMoveIndex * 4),(bestMoveIndex * 4) +4)) + " : " + bestScore);
        return bestScore;
    }
}