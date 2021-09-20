package com.cannon.gui;

import com.cannon.engine.board.Move;
import com.cannon.engine.pieces.Piece;
import com.google.common.primitives.Ints;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TakenPiecesPanel extends JPanel {

    private final JPanel northPanel;
    private final JPanel southPanel;

    private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(80, 40);
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);

    public TakenPiecesPanel() {
        super(new BorderLayout());
        this.setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(1, 16));
        this.southPanel = new JPanel(new GridLayout(1, 16));
        this.add(this.northPanel, BorderLayout.NORTH);
        this.add(this.southPanel, BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECES_DIMENSION);
    }

    public void redo(final Table.MoveLog movelog) {
        this.southPanel.removeAll();
        this.northPanel.removeAll();

        final List<Piece> lightTakenPieces = new ArrayList<>();
        final List<Piece> darkTakenPieces = new ArrayList<>();

        for(final Move move : movelog.getMoves()) {
            if(move.isAttack()) {
                final Piece takenPiece = move.getAttackedPiece();
                if(takenPiece.getPieceAlliance().isLight()) {
                    lightTakenPieces.add(takenPiece);
                } else if(takenPiece.getPieceAlliance().isDark()){
                    darkTakenPieces.add(takenPiece);
                } else {
                    throw new RuntimeException("Should not reach here!");
                }
            }
        }

        Collections.sort(lightTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(final Piece p1, final Piece p2) {
                return Ints.compare(p1.getPieceValue(), p2.getPieceValue());
            }
        });

        Collections.sort(darkTakenPieces, new Comparator<Piece>() {
            @Override
            public int compare(final Piece p1, final Piece p2) {
                return Ints.compare(p1.getPieceValue(), p2.getPieceValue());
            }
        });

        for (final Piece takenPiece : lightTakenPieces) {
            try {
                final BufferedImage image = ImageIO.read(new File("art/pieces/plain/"
                        + takenPiece.getPieceAlliance().toString().substring(0, 1) + "" + takenPiece.toString()
                        + ".png"));
                final ImageIcon ic = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(new ImageIcon(ic.getImage().getScaledInstance(
                        ic.getIconWidth() - 15, ic.getIconWidth() - 15, Image.SCALE_SMOOTH)));
                this.southPanel.add(imageLabel);
            }
            catch (final IOException e) {
                e.printStackTrace();
            }
        }

        for (final Piece takenPiece : darkTakenPieces) {
            try {
                final BufferedImage image = ImageIO.read(new File("art/pieces/plain/"
                        + takenPiece.getPieceAlliance().toString().substring(0, 1) + "" + takenPiece.toString()
                        + ".png"));
                final ImageIcon ic = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(new ImageIcon(ic.getImage().getScaledInstance(
                        ic.getIconWidth() - 15, ic.getIconWidth() - 15, Image.SCALE_SMOOTH)));
                this.northPanel.add(imageLabel);

            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        validate();
    }
}
