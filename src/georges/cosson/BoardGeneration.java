package georges.cosson;

import java.util.*;
/*
* PIECE=WHITE/black
* pawn=P/p
* kinght (horse)=N/n
* bishop=B/b
* rook (castle)=R/r
* Queen=Q/q
* King=K/k
*/
public class BoardGeneration {
    public static void initiateStandardChess() {
        long WP=0L,WN=0L,WB=0L,WR=0L,WQ=0L,WK=0L,BP=0L,BN=0L,BB=0L,BR=0L,BQ=0L,BK=0L;
        String chessBoard[][]={
                {"r","n","b","q","k","b","n","r"},
                {"p","p","p","p","p","p","p","p"},
                {" "," "," "," "," "," "," "," "},
                {" "," "," "," "," "," "," "," "},
                {" "," "," "," "," "," "," "," "},
                {" "," "," "," "," "," "," "," "},
                {"P","P","P","P","P","P","P","P"},
                {"R","N","B","Q","K","B","N","R"}};
        arrayToBitboards(chessBoard,WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK);
    }
    public static void initiateChess960() {
        long WP=0L,WN=0L,WB=0L,WR=0L,WQ=0L,WK=0L,BP=0L,BN=0L,BB=0L,BR=0L,BQ=0L,BK=0L;
        String chessBoard[][]={
            {" "," "," "," "," "," "," "," "},
            {"p","p","p","p","p","p","p","p"},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {" "," "," "," "," "," "," "," "},
            {"P","P","P","P","P","P","P","P"},
            {" "," "," "," "," "," "," "," "}};
        //step 1:
        int random1=(int)(Math.random()*8);
        chessBoard[0][random1]="b";
        chessBoard[7][random1]="B";
        //step 2:
        int random2=(int)(Math.random()*8);
        while (random2%2==random1%2) {
            random2=(int)(Math.random()*8);
        }
        chessBoard[0][random2]="b";
        chessBoard[7][random2]="B";
        //step 3:
        int random3=(int)(Math.random()*8);
        while (random3==random1 || random3==random2) {
            random3=(int)(Math.random()*8);
        }
        chessBoard[0][random3]="q";
        chessBoard[7][random3]="Q";
        //step 4:
        int random4a=(int)(Math.random()*5);
        int counter=0;
        int loop=0;
        while (counter-1<random4a) {
            if (" ".equals(chessBoard[0][loop])) {counter++;}
            loop++;
        }
        chessBoard[0][loop-1]="n";
        chessBoard[7][loop-1]="N";
        int random4b=(int)(Math.random()*4);
        counter=0;
        loop=0;
        while (counter-1<random4b) {
            if (" ".equals(chessBoard[0][loop])) {counter++;}
            loop++;
        }
        chessBoard[0][loop-1]="n";
        chessBoard[7][loop-1]="N";
        //step 5:
        counter=0;
        while (!" ".equals(chessBoard[0][counter])) {
            counter++;
        }
        chessBoard[0][counter]="r";
        chessBoard[7][counter]="R";
        while (!" ".equals(chessBoard[0][counter])) {
            counter++;
        }
        chessBoard[0][counter]="k";
        chessBoard[7][counter]="K";
        while (!" ".equals(chessBoard[0][counter])) {
            counter++;
        }
        chessBoard[0][counter]="r";
        chessBoard[7][counter]="R";
        arrayToBitboards(chessBoard,WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK);
    }
    public static void importFEN(String fenString) {
        //not chess960 compatible
	Engine.WP=0; Engine.WN=0; Engine.WB=0;
        Engine.WR=0; Engine.WQ=0; Engine.WK=0;
        Engine.BP=0; Engine.BN=0; Engine.BB=0;
        Engine.BR=0; Engine.BQ=0; Engine.BK=0;
        Engine.CWK=false; Engine.CWQ=false;
        Engine.CBK=false; Engine.CBQ=false;
	int charIndex = 0;
	int boardIndex = 0;
	while (fenString.charAt(charIndex) != ' ')
	{
		switch (fenString.charAt(charIndex++))
		{
		case 'P': Engine.WP |= (1L << boardIndex++);
			break;
		case 'p': Engine.BP |= (1L << boardIndex++);
			break;
		case 'N': Engine.WN |= (1L << boardIndex++);
			break;
		case 'n': Engine.BN |= (1L << boardIndex++);
			break;
		case 'B': Engine.WB |= (1L << boardIndex++);
			break;
		case 'b': Engine.BB |= (1L << boardIndex++);
			break;
		case 'R': Engine.WR |= (1L << boardIndex++);
			break;
		case 'r': Engine.BR |= (1L << boardIndex++);
			break;
		case 'Q': Engine.WQ |= (1L << boardIndex++);
			break;
		case 'q': Engine.BQ |= (1L << boardIndex++);
			break;
		case 'K': Engine.WK |= (1L << boardIndex++);
			break;
		case 'k': Engine.BK |= (1L << boardIndex++);
			break;
		case '/':
			break;
		case '1': boardIndex++;
			break;
		case '2': boardIndex += 2;
			break;
		case '3': boardIndex += 3;
			break;
		case '4': boardIndex += 4;
			break;
		case '5': boardIndex += 5;
			break;
		case '6': boardIndex += 6;
			break;
		case '7': boardIndex += 7;
			break;
		case '8': boardIndex += 8;
			break;
		default:
			break;
		}
	}
	Engine.WhiteToMove = (fenString.charAt(++charIndex) == 'w');
	charIndex += 2;
	while (fenString.charAt(charIndex) != ' ')
	{
		switch (fenString.charAt(charIndex++))
		{
		case '-':
			break;
		case 'K': Engine.CWK = true;
			break;
		case 'Q': Engine.CWQ = true;
			break;
		case 'k': Engine.CBK = true;
			break;
		case 'q': Engine.CBQ = true;
			break;
		default:
			break;
		}
	}
	if (fenString.charAt(++charIndex) != '-')
	{
		Engine.EP = Moves.FileMasks8[fenString.charAt(charIndex++) - 'a'];
	}
	//the rest of the fenString is not yet utilized
    }
    public static void arrayToBitboards(String[][] chessBoard,long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK) {
        String Binary;
        for (int i=0;i<64;i++) {
            Binary="0000000000000000000000000000000000000000000000000000000000000000";
            Binary=Binary.substring(i+1)+"1"+Binary.substring(0, i);
            switch (chessBoard[i/8][i%8]) {
                case "P": WP+=convertStringToBitboard(Binary);
                    break;
                case "N": WN+=convertStringToBitboard(Binary);
                    break;
                case "B": WB+=convertStringToBitboard(Binary);
                    break;
                case "R": WR+=convertStringToBitboard(Binary);
                    break;
                case "Q": WQ+=convertStringToBitboard(Binary);
                    break;
                case "K": WK+=convertStringToBitboard(Binary);
                    break;
                case "p": BP+=convertStringToBitboard(Binary);
                    break;
                case "n": BN+=convertStringToBitboard(Binary);
                    break;
                case "b": BB+=convertStringToBitboard(Binary);
                    break;
                case "r": BR+=convertStringToBitboard(Binary);
                    break;
                case "q": BQ+=convertStringToBitboard(Binary);
                    break;
                case "k": BK+=convertStringToBitboard(Binary);
                    break;
            }
        }
        drawArray(WP,WN,WB,WR,WQ,WK,BP,BN,BB,BR,BQ,BK);
        Engine.WP=WP; Engine.WN=WN; Engine.WB=WB;
        Engine.WR=WR; Engine.WQ=WQ; Engine.WK=WK;
        Engine.BP=BP; Engine.BN=BN; Engine.BB=BB;
        Engine.BR=BR; Engine.BQ=BQ; Engine.BK=BK;
    }
    public static long convertStringToBitboard(String Binary) {
        if (Binary.charAt(0)=='0') {//not going to be a negative number
            return Long.parseLong(Binary, 2);
        } else {
            return Long.parseLong("1"+Binary.substring(2), 2)*2;
        }
    }
    public static void drawArray(long WP,long WN,long WB,long WR,long WQ,long WK,long BP,long BN,long BB,long BR,long BQ,long BK) {
        String chessBoard[][]=new String[8][8];
        for (int i=0;i<64;i++) {
            chessBoard[i/8][i%8]=" ";
        }
        for (int i=0;i<64;i++) {
            if (((WP>>i)&1)==1) {chessBoard[i/8][i%8]="P";}
            if (((WN>>i)&1)==1) {chessBoard[i/8][i%8]="N";}
            if (((WB>>i)&1)==1) {chessBoard[i/8][i%8]="B";}
            if (((WR>>i)&1)==1) {chessBoard[i/8][i%8]="R";}
            if (((WQ>>i)&1)==1) {chessBoard[i/8][i%8]="Q";}
            if (((WK>>i)&1)==1) {chessBoard[i/8][i%8]="K";}
            if (((BP>>i)&1)==1) {chessBoard[i/8][i%8]="p";}
            if (((BN>>i)&1)==1) {chessBoard[i/8][i%8]="n";}
            if (((BB>>i)&1)==1) {chessBoard[i/8][i%8]="b";}
            if (((BR>>i)&1)==1) {chessBoard[i/8][i%8]="r";}
            if (((BQ>>i)&1)==1) {chessBoard[i/8][i%8]="q";}
            if (((BK>>i)&1)==1) {chessBoard[i/8][i%8]="k";}
        }
        for (int i=0;i<8;i++) {
            System.out.println(Arrays.toString(chessBoard[i]));
        }
    }
}