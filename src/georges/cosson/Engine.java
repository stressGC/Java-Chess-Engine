package georges.cosson;

public class Engine {
	
    static String ENGINE_NAME="LE DJO";
    static String ENGINE_AUTHOR = "Georges";
    
	// Bitboards
    static long WP=0L,WN=0L,WB=0L,WR=0L,WQ=0L,WK=0L,BP=0L,BN=0L,BB=0L,BR=0L,BQ=0L,BK=0L,EP=0L;
    
    static boolean CWK=true,CWQ=true,CBK=true,CBQ=true,WhiteToMove=true;//true=castle is possible
    
    static long UniversalWP=0L,UniversalWN=0L,UniversalWB=0L,UniversalWR=0L,
            UniversalWQ=0L,UniversalWK=0L,UniversalBP=0L,UniversalBN=0L,
            UniversalBB=0L,UniversalBR=0L,UniversalBQ=0L,UniversalBK=0L,
            UniversalEP=0L;
    
    static int searchDepth=4;
    static int moveCounter;
    static int MATE_SCORE=Integer.MAX_VALUE - 1;
    static int NULL_INT=Integer.MIN_VALUE;
    
    public static void main(String[] args) {
    	// importing default FEN string for chess
        BoardGeneration.importFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        UCI.inputPosition("position startpos moves e2e4 e7e5 c2c3");
        UCI.inputPrint();
        UCI.uciCommunication();
    }
}