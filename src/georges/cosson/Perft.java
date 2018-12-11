package georges.cosson;

public class Perft {
	
    public static String moveToAlgebra(String move)
    {
        String moveString="";
        moveString+=""+(char)(move.charAt(1)+49);
        moveString+=""+('8'-move.charAt(0));
        moveString+=""+(char)(move.charAt(3)+49);
        moveString+=""+('8'-move.charAt(2));
        return moveString;
    }
    
    static int perftTotalMoveCounter=0;
    static int perftMoveCounter=0;
    static int perftMaxDepth=2;
    
    public static void perftRoot(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK,long EP,boolean CWK,boolean CWQ,boolean CBK,boolean CBQ,boolean WhiteToMove,int depth)
    {
        String moves;
        
        if (WhiteToMove) {
            moves=Moves.possibleMovesW(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ);
        } else {
            moves=Moves.possibleMovesB(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ);
        }
        
        // For eah possible moves
        for (int i=0;i<moves.length();i+=4) {
        	
        	// We are calculating the resulting board
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
                
                /*if (((1L<<start)&(WP|BP))!=0) {
                    if (Math.abs(moves.charAt(i)-moves.charAt(i+2))==2) {
                        EPt=Moves.FileMasks8[moves.charAt(i+1)-'0'];
                    }
                }
                else */
                
                if (((1L<<start)&WK)!=0) {CWKt=false; CWQt=false;}
                else if (((1L<<start)&BK)!=0) {CBKt=false; CBQt=false;}
                else if (((1L<<start)&WR&(1L<<Moves.CASTLE_ROOKS[0]))!=0) {CWKt=false;}
                else if (((1L<<start)&WR&(1L<<Moves.CASTLE_ROOKS[1]))!=0) {CWQt=false;}
                else if (((1L<<start)&BR&(1L<<Moves.CASTLE_ROOKS[2]))!=0) {CBKt=false;}
                else if (((1L<<start)&BR&1L)!=0) {CBQt=false;}
            }
            
            if (((WKt & Moves.unsafeForWhite(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && WhiteToMove) ||
                    ((BKt&Moves.unsafeForBlack(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && !WhiteToMove)) {
                perft(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt,EPt,CWKt,CWQt,CBKt,CBQt,!WhiteToMove,depth+1);
                System.out.println(moveToAlgebra(moves.substring(i,i+4))+" "+perftMoveCounter);
                perftTotalMoveCounter+=perftMoveCounter;
                perftMoveCounter=0;
            }
        }
    }
    
    public static void perft(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK,long EP,boolean CWK,boolean CWQ,boolean CBK,boolean CBQ,boolean WhiteToMove,int depth)
    {
        if (depth < perftMaxDepth) {
        	
            String moves;
            
            if (WhiteToMove) {
                moves=Moves.possibleMovesW(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ);
            } else {
                moves=Moves.possibleMovesB(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK,EP,CWK,CWQ,CBK,CBQ);
            }
            
            for (int i=0;i<moves.length();i+=4) {
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
                    /*if (((1L<<start)&(WP|BP))!=0) {
                        if (Math.abs(moves.charAt(i)-moves.charAt(i+2))==2) {
                            EPt=Moves.FileMasks8[moves.charAt(i+1)-'0'];
                        }
                    }
                    else */if (((1L<<start)&WK)!=0) {CWKt=false; CWQt=false;}
                    else if (((1L<<start)&BK)!=0) {CBKt=false; CBQt=false;}
                    else if (((1L<<start)&WR&(1L<<63))!=0) {CWKt=false;}
                    else if (((1L<<start)&WR&(1L<<56))!=0) {CWQt=false;}
                    else if (((1L<<start)&BR&(1L<<7))!=0) {CBKt=false;}
                    else if (((1L<<start)&BR&1L)!=0) {CBQt=false;}
                }
                if (((WKt&Moves.unsafeForWhite(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && WhiteToMove) ||
                        ((BKt&Moves.unsafeForBlack(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt))==0 && !WhiteToMove)) {
                    if (depth+1==perftMaxDepth) {perftMoveCounter++;}
                    perft(WPt,WNt,WBt,WRt,WQt,WKt,BPt,BNt,BBt,BRt,BQt,BKt,EPt,CWKt,CWQt,CBKt,CBQt,!WhiteToMove,depth+1);
                }
            }
        }
    }
}