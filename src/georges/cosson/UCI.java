package georges.cosson;

import java.util.*;

/*
 * class responsible for the communication through UCI protocol
 */
public class UCI {
    
	// UCI listener
    public static void uciCommunication() {
    	
        @SuppressWarnings("resource")
		Scanner input = new Scanner(System.in);
        
        // infinite loop to listen to incoming commands
        while (true)
        {
            String inputString=input.nextLine();
            
            /* 
             * parsing the input based on the different existing commands 
             * http://wbec-ridderkerk.nl/html/UCIProtocol.html
             */
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
    
    // function called the make sure the UCI protocol is supproted
    public static void inputUCI() {
        System.out.println("id name " + Engine.ENGINE_NAME);
        System.out.println("id author " + Engine.ENGINE_AUTHOR);
        System.out.println("uciok");
    }
    
    // function called to set options, not supported ATM
    public static void inputSetOption(String inputString) {
        //set options ?
    }
    
    // function called to make sure the engine is ready
    public static void inputIsReady() {
         System.out.println("readyok");
    }
    
    // function called when a new game is about to start, not supported ATM
    public static void inputUCINewGame() {
        //clear cache and other stuffs ?
    }  
    
    // function called when the platform updates the positions of the pieces on the board
    public static void inputPosition(String input) {
    	
        input=input.substring(9).concat(" ");
        
        if (input.contains("startpos ")) {
        	
            input=input.substring(9);
            BoardGeneration.importFEN(BoardGeneration.DEFAULT_BOARD_FEN);
            
        }
        else if (input.contains("fen")) {
        	
            input=input.substring(4);
            BoardGeneration.importFEN(input);
            
        }
        
        // if there is move to do
        if (input.contains("moves")) {
        	
            input=input.substring(input.indexOf("moves")+6);
            
            // then lets make them all
            while (input.length() > 0)
            {
                String moves;
                
                if (Engine.WhiteToMove) {
                    moves = Moves.possibleMovesW(Engine.WP, Engine.WN, Engine.WB, Engine.WR, Engine.WQ, Engine.WK, Engine.BP, Engine.BN, Engine.BB, Engine.BR, Engine.BQ, Engine.BK, Engine.EP, Engine.CWK, Engine.CWQ, Engine.CBK, Engine.CBQ);
                } else {
                    moves = Moves.possibleMovesB(Engine.WP, Engine.WN, Engine.WB, Engine.WR, Engine.WQ, Engine.WK, Engine.BP, Engine.BN, Engine.BB, Engine.BR, Engine.BQ, Engine.BK, Engine.EP, Engine.CWK, Engine.CWQ, Engine.CBK, Engine.CBQ);
                }
                
                // converts to move String
                algebraToMove(input, moves);
                input = input.substring(input.indexOf(' ') + 1);
            }
        }
    }
    
    // function called when its our turn to compute !
    public static void inputGo() {
    	
    	// principle taken from : https://github.com/Garee/jchess/blob/master/src/model/AI.java
    	
    	// contains our possibles moves
    	String moves;
    	
    	// contains our best move
    	String bestMove = "";
    	
    	// we want to know how many time our engine wil take to compute best move
    	long start = System.currentTimeMillis();
    	
    	// default best score is the worst possible
    	int bestScore = Engine.NULL_INT;
    	
    	// let's compute all possible moves depending of the current side
	    if (Engine.WhiteToMove) {
	        moves = Moves.possibleMovesW(Engine.WP, Engine.WN, Engine.WB, Engine.WR, Engine.WQ, Engine.WK, Engine.BP, Engine.BN, Engine.BB, Engine.BR, Engine.BQ, Engine.BK, Engine.EP, Engine.CWK, Engine.CWQ, Engine.CBK, Engine.CBQ);
	    } else {
	        moves = Moves.possibleMovesB(Engine.WP, Engine.WN, Engine.WB, Engine.WR, Engine.WQ, Engine.WK, Engine.BP, Engine.BN, Engine.BB, Engine.BR, Engine.BQ, Engine.BK, Engine.EP, Engine.CWK, Engine.CWQ, Engine.CBK, Engine.CBQ);
	    }
	    
	    // for each of these possible moves
	    for (int i = 0; i < moves.length(); i += 4) {

	        // make the move : we store new bitboards in temporary variables
	    	String move = moves.substring(i, i + 4);
	        long WPt = Moves.makeMove(Engine.WP, move, 'P');
	        long WNt = Moves.makeMove(Engine.WN, move, 'N');
	        long WBt = Moves.makeMove(Engine.WB, move, 'B');
	        long WRt = Moves.makeMove(Engine.WR, move, 'R');
	        long WQt = Moves.makeMove(Engine.WQ, move, 'Q');
	        long WKt = Moves.makeMove(Engine.WK, move, 'K');
	        long BPt = Moves.makeMove(Engine.BP, move, 'p');
	        long BNt = Moves.makeMove(Engine.BN, move, 'n');
	        long BBt = Moves.makeMove(Engine.BB, move, 'b');
	        long BRt = Moves.makeMove(Engine.BR, move, 'r');
	        long BQt = Moves.makeMove(Engine.BQ, move, 'q');
	        long BKt = Moves.makeMove(Engine.BK, move, 'k');
	        long EPt = Moves.makeMoveEP(Engine.WP | Engine.BP,move);
	        
	        WRt=Moves.makeMoveCastle(WRt, Engine.WK | Engine.BK, move, 'R');
	        BRt=Moves.makeMoveCastle(BRt, Engine.WK | Engine.BK, move, 'r');
	        
	        boolean CWKt = Engine.CWK;
	        boolean CWQt = Engine.CWQ;
	        boolean CBKt = Engine.CBK;
	        boolean CBQt = Engine.CBQ;
	        
	        // check for illegal move (moves where our own king is not safe)
	        if (((WKt & Moves.unsafeForWhite(WPt, WNt, WBt, WRt, WQt, WKt, BPt, BNt, BBt, BRt, BQt, BKt)) == 0 && Engine.WhiteToMove) ||
                    ((BKt & Moves.unsafeForBlack(WPt, WNt, WBt, WRt, WQt, WKt, BPt, BNt, BBt, BRt, BQt, BKt)) == 0 && !Engine.WhiteToMove)) {
	        	
		        int BASE_ALPHA = Engine.NULL_INT;
		        int BASE_BETA = Engine.MATE_SCORE;
		        int BASE_DEPTH = 0;
		        
		        int score = PrincipalVariation.pvSearch(BASE_ALPHA, BASE_BETA, WPt, WNt, WBt, WRt, WQt, WKt, BPt, BNt, BBt, BRt, BQt, BKt, EPt, CWKt, CWQt, CBKt, CBQt, Engine.WhiteToMove, BASE_DEPTH);
		        
		        // debug purpose, we print the score of each possible move
		        System.out.println("MOVE : " + moveToAlgebra(move) + ", SCORE = " + score);
		        
		        // if the current move's score is the best, save it
		        if(bestScore < score) {
		        	bestScore = score;
		        	bestMove = move;
		        }
            }
	    }
	    
	    long end = System.currentTimeMillis();
	    
	    // send the move we consider the best through UCI
        System.out.println("bestmove " +  moveToAlgebra(bestMove));
        
        // time taken for computation, debug purpose
        System.out.println("found in " + (end - start) + " ms");
    }
    
    // converts a move String to its algebra form
    public static String moveToAlgebra(String move) {
    	
    	// instantiate variables
        String append = "";
        int start = 0;
        int end = 0;
        
        // if its a "regular" move
        if (Character.isDigit(move.charAt(3))) {
            start = (Character.getNumericValue(move.charAt(0)) * 8) + (Character.getNumericValue(move.charAt(1)));
            end = (Character.getNumericValue(move.charAt(2)) * 8) + (Character.getNumericValue(move.charAt(3)));
        } 
        // if its a pawn promotion
        else if (move.charAt(3) == 'P') {
            if (Character.isUpperCase(move.charAt(2))) {
                start = Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(0) - '0'] & Moves.RankMasks8[1]);
                end = Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(1) - '0'] & Moves.RankMasks8[0]);
            } else {
                start = Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(0) - '0'] & Moves.RankMasks8[6]);
                end = Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(1) - '0'] & Moves.RankMasks8[7]);
            }
            append = "" + Character.toLowerCase(move.charAt(2));
        } 
        // if it is an en passant move
        else if (move.charAt(3) == 'E') {
            if (move.charAt(2) == 'W') {
                start = Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(0) - '0'] & Moves.RankMasks8[3]);
                end = Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(1) - '0'] & Moves.RankMasks8[2]);
            } else {
                start = Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(0) - '0'] & Moves.RankMasks8[4]);
                end = Long.numberOfTrailingZeros(Moves.FileMasks8[move.charAt(1) - '0'] & Moves.RankMasks8[5]);
            }
        }
        
        String returnMove = "";
        returnMove += (char)('a' + (start % 8));
        returnMove += (char)('8' - (start / 8));
        returnMove += (char)('a' + (end % 8));
        returnMove += (char)('8' - (end / 8));
        returnMove += append;
        
        return returnMove;
    }
    
    // converts a move algebra to its move form
    public static void algebraToMove(String input, String moves) {
    	
    	// instantiate variables
        int start = 0;
        int end = 0;
        int from = (input.charAt(0) - 'a') + (8 * ('8' - input.charAt(1)));
        int to = (input.charAt(2) - 'a') + (8 * ('8' - input.charAt(3)));
        
        // for each move
        for (int i = 0; i < moves.length(); i += 4) {
        	
        	//'regular' move
            if (Character.isDigit(moves.charAt(i + 3))) {
                start = (Character.getNumericValue(moves.charAt(i + 0)) * 8) + (Character.getNumericValue(moves.charAt(i + 1)));
                end = (Character.getNumericValue(moves.charAt(i + 2)) * 8) + (Character.getNumericValue(moves.charAt(i + 3)));
            } 
            //pawn promotion
            else if (moves.charAt(i + 3) == 'P') {
                if (Character.isUpperCase(moves.charAt(i + 2))) {
                    start = Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i + 0) - '0'] & Moves.RankMasks8[1]);
                    end = Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i + 1) - '0'] & Moves.RankMasks8[0]);
                } else {
                    start = Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i + 0) - '0'] & Moves.RankMasks8[6]);
                    end = Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i + 1) - '0'] & Moves.RankMasks8[7]);
                }
            } 
            //en passant
            else if (moves.charAt(i + 3) == 'E') {
                if (moves.charAt(i + 2) == 'W') {
                    start = Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i + 0) - '0'] & Moves.RankMasks8[3]);
                    end = Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i + 1) - '0'] & Moves.RankMasks8[2]);
                } else {
                    start = Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i + 0) - '0'] & Moves.RankMasks8[4]);
                    end = Long.numberOfTrailingZeros(Moves.FileMasks8[moves.charAt(i + 1) - '0'] & Moves.RankMasks8[5]);
                }
            }
            
            if ((start == from) && (end == to)) {
                if ((input.charAt(4) == ' ') || (Character.toUpperCase(input.charAt(4)) == Character.toUpperCase(moves.charAt(i + 2)))) {
                	
                	//'regular' move
                	if (Character.isDigit(moves.charAt(i + 3))) {
                        start = (Character.getNumericValue(moves.charAt(i)) * 8) + (Character.getNumericValue(moves.charAt(i + 1)));
                        if (((1L << start) & Engine.WK) != 0) { Engine.CWK = false; Engine.CWQ = false; }
                        else if (((1L << start) & Engine.BK) != 0) { Engine.CBK = false; Engine.CBQ = false; }
                        else if (((1L << start) & Engine.WR & (1L << 63)) != 0) {Engine.CWK = false; }
                        else if (((1L << start) & Engine.WR & (1L << 56)) != 0) {Engine.CWQ = false; }
                        else if (((1L << start) & Engine.BR & (1L << 7)) != 0) {Engine.CBK = false; }
                        else if (((1L << start) & Engine.BR & 1L) != 0) { Engine.CBQ = false; }
                    }
                    
                    String substring = moves.substring(i, i + 4);
                    
                    Engine.EP = Moves.makeMoveEP(Engine.WP | Engine.BP, substring);
                    
                    Engine.WR = Moves.makeMoveCastle(Engine.WR, Engine.WK | Engine.BK, substring, 'R');
                    Engine.BR = Moves.makeMoveCastle(Engine.BR, Engine.WK | Engine.BK, substring, 'r');
                    
                    Engine.WP = Moves.makeMove(Engine.WP, substring, 'P');
                    Engine.WN = Moves.makeMove(Engine.WN, substring, 'N');
                    Engine.WB = Moves.makeMove(Engine.WB, substring, 'B');
                    Engine.WR = Moves.makeMove(Engine.WR, substring, 'R');
                    Engine.WQ = Moves.makeMove(Engine.WQ, substring, 'Q');
                    Engine.WK = Moves.makeMove(Engine.WK, substring, 'K');
                    Engine.BP = Moves.makeMove(Engine.BP, substring, 'p');
                    Engine.BN = Moves.makeMove(Engine.BN, substring, 'n');
                    Engine.BB = Moves.makeMove(Engine.BB, substring, 'b');
                    Engine.BR = Moves.makeMove(Engine.BR, substring, 'r');
                    Engine.BQ = Moves.makeMove(Engine.BQ, substring, 'q');
                    Engine.BK = Moves.makeMove(Engine.BK, substring, 'k');
                    
                    Engine.WhiteToMove = !Engine.WhiteToMove;
                    
                    break;
                }
            }
        }
    }
    
    // function called when "quit" is received through UCI, quits the program, debug purpose
    public static void inputQuit() {
        System.exit(0);
    }
    
    // function called when "print" is received through UCI, debug purpose
    public static void inputPrint() {
        BoardGeneration.drawArray(Engine.WP, Engine.WN, Engine.WB, Engine.WR, Engine.WQ, Engine.WK, Engine.BP, Engine.BN, Engine.BB, Engine.BR, Engine.BQ, Engine.BK);
    }
}