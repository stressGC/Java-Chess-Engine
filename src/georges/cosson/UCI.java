package georges.cosson;

import java.util.*;

public class UCI {
    
    public static void uciCommunication() {
        @SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
        
        while (true)
        {
            String inputString=input.nextLine();
            if ("uci".equals(inputString))
            {
                inputUCI();
            }
            else if (inputString.startsWith("setoption"))
            {
                inputSetOption(inputString);
            }
            else if ("isready".equals(inputString))
            {
                inputIsReady();
            }
            else if ("ucinewgame".equals(inputString))
            {
                inputUCINewGame();
            }
            else if (inputString.startsWith("position"))
            {
                inputPosition(inputString);
            }
            else if (inputString.startsWith("go"))
            {
                inputGo();
            }
            else if (inputString.equals("quit"))
            {
                inputQuit();
            }
            else if ("print".equals(inputString))
            {
                inputPrint();
            }
        }
    }
    public static void inputUCI() {
        System.out.println("id name "+ Engine.ENGINE_NAME);
        System.out.println("id author " + Engine.ENGINE_AUTHOR);
        //options go here
        System.out.println("uciok");
    }
    public static void inputSetOption(String inputString) {
        //set options
    }
    public static void inputIsReady() {
         System.out.println("readyok");
    }
    public static void inputUCINewGame() {
        //add code here
    }  
    public static void inputPosition(String input) {
        input=input.substring(9).concat(" ");
        if (input.contains("startpos ")) {
            input=input.substring(9);
            BoardGeneration.importFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        }
        else if (input.contains("fen")) {
            input=input.substring(4);
            BoardGeneration.importFEN(input);
        }
        if (input.contains("moves")) {
            input=input.substring(input.indexOf("moves")+6);
            while (input.length()>0)
            {
                String moves;
                if (Engine.WhiteToMove) {
                    moves=Moves.possibleMovesW(Engine.WP,Engine.WN,Engine.WB,Engine.WR,Engine.WQ,Engine.WK,Engine.BP,Engine.BN,Engine.BB,Engine.BR,Engine.BQ,Engine.BK,Engine.EP,Engine.CWK,Engine.CWQ,Engine.CBK,Engine.CBQ);
                } else {
                    moves=Moves.possibleMovesB(Engine.WP,Engine.WN,Engine.WB,Engine.WR,Engine.WQ,Engine.WK,Engine.BP,Engine.BN,Engine.BB,Engine.BR,Engine.BQ,Engine.BK,Engine.EP,Engine.CWK,Engine.CWQ,Engine.CBK,Engine.CBQ);
                }
                algebraToMove(input,moves);
                input=input.substring(input.indexOf(' ')+1);
            }
        }
    }
    public static void inputGo() {		
    	// principe ligne 45 : https://github.com/Garee/jchess/blob/master/src/model/AI.java
    	String moves;
    	String bestMove = "";
    	
    	long start = System.currentTimeMillis();
    	
    	int bestScore = Integer.MIN_VALUE + 1;
    	
	    if (Engine.WhiteToMove) {
	        moves=Moves.possibleMovesW(Engine.WP,Engine.WN,Engine.WB,Engine.WR,Engine.WQ,Engine.WK,Engine.BP,Engine.BN,Engine.BB,Engine.BR,Engine.BQ,Engine.BK,Engine.EP,Engine.CWK,Engine.CWQ,Engine.CBK,Engine.CBQ);
	    } else {
	        moves=Moves.possibleMovesB(Engine.WP,Engine.WN,Engine.WB,Engine.WR,Engine.WQ,Engine.WK,Engine.BP,Engine.BN,Engine.BB,Engine.BR,Engine.BQ,Engine.BK,Engine.EP,Engine.CWK,Engine.CWQ,Engine.CBK,Engine.CBQ);
	    }
	    
	    for (int i = 0; i < moves.length(); i+=4) {

	    	System.out.println("====================");
	        // make the move
	    	String move = moves.substring(i,i+4);
	        long WPt=Moves.makeMove(Engine.WP, move, 'P');
	        long WNt=Moves.makeMove(Engine.WN, move, 'N');
	        long WBt=Moves.makeMove(Engine.WB, move, 'B');
	        long WRt=Moves.makeMove(Engine.WR, move, 'R');
	        long WQt=Moves.makeMove(Engine.WQ, move, 'Q');
	        long WKt=Moves.makeMove(Engine.WK, move, 'K');
	        long BPt=Moves.makeMove(Engine.BP, move, 'p');
	        long BNt=Moves.makeMove(Engine.BN, move, 'n');
	        long BBt=Moves.makeMove(Engine.BB, move, 'b');
	        long BRt=Moves.makeMove(Engine.BR, move, 'r');
	        long BQt=Moves.makeMove(Engine.BQ, move, 'q');
	        long BKt=Moves.makeMove(Engine.BK, move, 'k');
	        long EPt=Moves.makeMoveEP(Engine.WP|Engine.BP,move);
	        
	        WRt=Moves.makeMoveCastle(WRt, Engine.WK|Engine.BK, move, 'R');
	        BRt=Moves.makeMoveCastle(BRt, Engine.WK|Engine.BK, move, 'r');
	        
	        boolean CWKt=Engine.CWK;
	        boolean CWQt=Engine.CWQ;
	        boolean CBKt=Engine.CBK;
	        boolean CBQt=Engine.CBQ;
	        
	        // check for illegal move
	        if (((WKt&Moves.unsafeForWhite(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && Engine.WhiteToMove) ||
                    ((BKt&Moves.unsafeForBlack(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && !Engine.WhiteToMove)) {
	        	
		        int BASE_ALPHA = Integer.MIN_VALUE + 1;
		        int BASE_BETA = Integer.MAX_VALUE - 1;
		        
		        int score = PrincipalVariation.pvSearch(BASE_ALPHA, BASE_BETA, WPt, WNt, WBt, WRt, WQt, WKt, BPt, BNt, BBt, BRt, BQt, BKt, EPt, CWKt, CWQt, CBKt, CBQt, Engine.WhiteToMove, 0);
		        System.out.println("MOVE : " + moveToAlgebra(move) + ", SCORE = " + score);
		        
		        if(bestScore < score) {
		        	bestScore = score;
		        	bestMove = move;
		        }
            }
	    }
	    long end = System.currentTimeMillis();
	    //System.out.println(move);
	    //Perft.perftRoot(Engine.WP,Engine.WN,Engine.WB,Engine.WR,Engine.WQ,Engine.WK,Engine.BP,Engine.BN,Engine.BB,Engine.BR,Engine.BQ,Engine.BK,Engine.EP,Engine.CWK,Engine.CWQ,Engine.CBK,Engine.CBQ,!Engine.WhiteToMove, 0);
        //int score = PrincipalVariation.pvSearch(-1000,1000, Engine.WP,Engine.WN,Engine.WB,Engine.WR,Engine.WQ,Engine.WK,Engine.BP,Engine.BN,Engine.BB,Engine.BR,Engine.BQ,Engine.BK,Engine.EP,Engine.CWK,Engine.CWQ,Engine.CBK,Engine.CBQ, Engine.WhiteToMove, 0);//Minimax.computeBestMove(move);
        //System.out.println("bmove raw : " + bestMove);
        System.out.println("bestmove " +  moveToAlgebra(bestMove));
        System.out.println("found in " + (end - start) + " ms");
    }
    
    public static String moveToAlgebra(String move) {
        String append="";
        int start=0,end=0;
        if (Character.isDigit(move.charAt(3))) {//'regular' move
            start=(Character.getNumericValue(move.charAt(0))*8)+(Character.getNumericValue(move.charAt(1)));
            end=(Character.getNumericValue(move.charAt(2))*8)+(Character.getNumericValue(move.charAt(3)));
        } else if (move.charAt(3)=='P') {//pawn promotion
            if (Character.isUpperCase(move.charAt(2))) {
                start=Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(0)-'0']&Moves.RankMasks8[1]);
                end=Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(1)-'0']&Moves.RankMasks8[0]);
            } else {
                start=Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(0)-'0']&Moves.RankMasks8[6]);
                end=Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(1)-'0']&Moves.RankMasks8[7]);
            }
            append=""+Character.toLowerCase(move.charAt(2));
        } else if (move.charAt(3)=='E') {//en passant
            if (move.charAt(2)=='W') {
                start=Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(0)-'0']&Moves.RankMasks8[3]);
                end=Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(1)-'0']&Moves.RankMasks8[2]);
            } else {
                start=Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(0)-'0']&Moves.RankMasks8[4]);
                end=Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(1)-'0']&Moves.RankMasks8[5]);
            }
        }
        String returnMove="";
        returnMove+=(char)('a'+(start%8));
        returnMove+=(char)('8'-(start/8));
        returnMove+=(char)('a'+(end%8));
        returnMove+=(char)('8'-(end/8));
        returnMove+=append;
        return returnMove;
    }
    public static void algebraToMove(String input,String moves) {
        int start=0,end=0;
        int from=(input.charAt(0)-'a')+(8*('8'-input.charAt(1)));
        int to=(input.charAt(2)-'a')+(8*('8'-input.charAt(3)));
        for (int i=0;i<moves.length();i+=4) {
            if (Character.isDigit(moves.charAt(i+3))) {//'regular' move
                start=(Character.getNumericValue(moves.charAt(i+0))*8)+(Character.getNumericValue(moves.charAt(i+1)));
                end=(Character.getNumericValue(moves.charAt(i+2))*8)+(Character.getNumericValue(moves.charAt(i+3)));
            } else if (moves.charAt(i+3)=='P') {//pawn promotion
                if (Character.isUpperCase(moves.charAt(i+2))) {
                    start=Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i+0)-'0']&Moves.RankMasks8[1]);
                    end=Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i+1)-'0']&Moves.RankMasks8[0]);
                } else {
                    start=Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i+0)-'0']&Moves.RankMasks8[6]);
                    end=Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i+1)-'0']&Moves.RankMasks8[7]);
                }
            } else if (moves.charAt(i+3)=='E') {//en passant
                if (moves.charAt(i+2)=='W') {
                    start=Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i+0)-'0']&Moves.RankMasks8[3]);
                    end=Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i+1)-'0']&Moves.RankMasks8[2]);
                } else {
                    start=Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i+0)-'0']&Moves.RankMasks8[4]);
                    end=Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i+1)-'0']&Moves.RankMasks8[5]);
                }
            }
            if ((start==from) && (end==to)) {
                if ((input.charAt(4)==' ') || (Character.toUpperCase(input.charAt(4))==Character.toUpperCase(moves.charAt(i+2)))) {
                    if (Character.isDigit(moves.charAt(i+3))) {//'regular' move
                        start=(Character.getNumericValue(moves.charAt(i))*8)+(Character.getNumericValue(moves.charAt(i+1)));
                        if (((1L<<start)&Engine.WK)!=0) {Engine.CWK=false; Engine.CWQ=false;}
                        else if (((1L<<start)&Engine.BK)!=0) {Engine.CBK=false; Engine.CBQ=false;}
                        else if (((1L<<start)&Engine.WR&(1L<<63))!=0) {Engine.CWK=false;}
                        else if (((1L<<start)&Engine.WR&(1L<<56))!=0) {Engine.CWQ=false;}
                        else if (((1L<<start)&Engine.BR&(1L<<7))!=0) {Engine.CBK=false;}
                        else if (((1L<<start)&Engine.BR&1L)!=0) {Engine.CBQ=false;}
                    }
                    Engine.EP=Moves.makeMoveEP(Engine.WP|Engine.BP,moves.substring(i,i+4));
                    Engine.WR=Moves.makeMoveCastle(Engine.WR, Engine.WK|Engine.BK, moves.substring(i,i+4), 'R');
                    Engine.BR=Moves.makeMoveCastle(Engine.BR, Engine.WK|Engine.BK, moves.substring(i,i+4), 'r');
                    Engine.WP=Moves.makeMove(Engine.WP, moves.substring(i,i+4), 'P');
                    Engine.WN=Moves.makeMove(Engine.WN, moves.substring(i,i+4), 'N');
                    Engine.WB=Moves.makeMove(Engine.WB, moves.substring(i,i+4), 'B');
                    Engine.WR=Moves.makeMove(Engine.WR, moves.substring(i,i+4), 'R');
                    Engine.WQ=Moves.makeMove(Engine.WQ, moves.substring(i,i+4), 'Q');
                    Engine.WK=Moves.makeMove(Engine.WK, moves.substring(i,i+4), 'K');
                    Engine.BP=Moves.makeMove(Engine.BP, moves.substring(i,i+4), 'p');
                    Engine.BN=Moves.makeMove(Engine.BN, moves.substring(i,i+4), 'n');
                    Engine.BB=Moves.makeMove(Engine.BB, moves.substring(i,i+4), 'b');
                    Engine.BR=Moves.makeMove(Engine.BR, moves.substring(i,i+4), 'r');
                    Engine.BQ=Moves.makeMove(Engine.BQ, moves.substring(i,i+4), 'q');
                    Engine.BK=Moves.makeMove(Engine.BK, moves.substring(i,i+4), 'k');
                    Engine.WhiteToMove=!Engine.WhiteToMove;
                    break;
                }
            }
        }
    }
    public static void inputQuit() {
        System.exit(0);
    }
    public static void inputPrint() {
        BoardGeneration.drawArray(Engine.WP,Engine.WN,Engine.WB,Engine.WR,Engine.WQ,Engine.WK,Engine.BP,Engine.BN,Engine.BB,Engine.BR,Engine.BQ,Engine.BK);
    }
}