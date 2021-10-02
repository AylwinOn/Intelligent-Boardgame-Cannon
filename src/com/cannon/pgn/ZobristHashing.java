package com.cannon.pgn;


import com.cannon.engine.board.Board;
import com.cannon.engine.board.BoardUtils;

import java.security.SecureRandom;


public class ZobristHashing {
    private long[][] zobristBoard;
    private long zobristDarkMove;

    public ZobristHashing() {
        SecureRandom random = new SecureRandom();
        zobristBoard = new long[100][2];
        for(int piece = 0; piece < 2; piece++) {
            for(int tile = 0; tile < BoardUtils.NUM_TILES; tile++) {
                zobristBoard[tile][piece] = random.nextLong();
            }
        }
        zobristDarkMove = random.nextLong();
    }

    public long getHash(final Board board) {
        long zHash = 0L;
        for(int tile = 0; tile < BoardUtils.NUM_TILES; tile++) {
            if(!board.getTile(tile).isTileOccupied()) {
                continue;
            }
            final String tileText = board.getTile(tile).toString();

            int piece = 0;
            if(tileText == "s") {
                piece = 0;
            } else if(tileText == "S") {
                piece = 1;
            }
            zHash ^= zobristBoard[tile][piece];
        }

        if (!board.currentPlayer().getAlliance().isLight()) {
            zHash ^= zobristDarkMove;
        }
        return zHash;
    }
}
