package com.cannon.gui;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import com.cannon.engine.player.Alliance;
import com.cannon.engine.player.Player;
import com.cannon.gui.Table.PlayerType;
import com.cannon.gui.Table.StrategyType;

public class GameSetup extends JDialog {

    private PlayerType lightPlayerType;
    private PlayerType darkPlayerType;
    private StrategyType lightStrategyType;
    private StrategyType darkStrategyType;
    private JSpinner searchDepthSpinner;
    private JSpinner timeResourcesSpinner;

    private static final String HUMAN_TEXT = "Human";
    private static final String COMPUTER_TEXT = "Computer";
    private static final String STRARTEGY_ONE_TEXT = "Strategy One";
    private static final String STRATEGY_TWO_TEXT = "Strategy Two";

    public static boolean AIplayerDark = false;
    public static boolean AIplayerLight = false;

    GameSetup(final JFrame frame, boolean modal) {
        super(frame, modal);
        final JPanel myPanel = new JPanel(new GridLayout(0, 1));
        final JRadioButton lightHumanButton = new JRadioButton(HUMAN_TEXT);
        final JRadioButton lightComputerButton = new JRadioButton(COMPUTER_TEXT);
        final JRadioButton darkHumanButton = new JRadioButton(HUMAN_TEXT);
        final JRadioButton darkComputerButton = new JRadioButton(COMPUTER_TEXT);
        final JRadioButton lightStrategy1Button = new JRadioButton(STRARTEGY_ONE_TEXT);
        final JRadioButton lightStrategy2Button = new JRadioButton(STRATEGY_TWO_TEXT);
        final JRadioButton darkStrategy1Button = new JRadioButton(STRARTEGY_ONE_TEXT);
        final JRadioButton darkStrategy2Button = new JRadioButton(STRATEGY_TWO_TEXT);

        lightHumanButton.setActionCommand(HUMAN_TEXT);
        final ButtonGroup lightGroup = new ButtonGroup();
        lightGroup.add(lightHumanButton);
        lightGroup.add(lightComputerButton);
        lightHumanButton.setSelected(true);

        final ButtonGroup darkGroup = new ButtonGroup();
        darkGroup.add(darkHumanButton);
        darkGroup.add(darkComputerButton);
        darkHumanButton.setSelected(true);

        lightStrategy1Button.setActionCommand(STRARTEGY_ONE_TEXT);
        final ButtonGroup lightStrategyGroup = new ButtonGroup();
        lightStrategyGroup.add(lightStrategy1Button);
        lightStrategyGroup.add(lightStrategy2Button);
        lightStrategy1Button.setSelected(true);

        final ButtonGroup darkStrategyGroup = new ButtonGroup();
        darkStrategyGroup.add(darkStrategy1Button);
        darkStrategyGroup.add(darkStrategy2Button);
        darkStrategy1Button.setSelected(true);

        getContentPane().add(myPanel);
        myPanel.add(new JLabel("Light"));
        myPanel.add(lightHumanButton);
        myPanel.add(lightComputerButton);
        myPanel.add(new JLabel("Dark"));
        myPanel.add(darkHumanButton);
        myPanel.add(darkComputerButton);
        myPanel.add(new JLabel("Light Strategy"));
        myPanel.add(lightStrategy1Button);
        myPanel.add(lightStrategy2Button);
        myPanel.add(new JLabel("Dark Strategy"));
        myPanel.add(darkStrategy1Button);
        myPanel.add(darkStrategy2Button);

        this.searchDepthSpinner = addLabeledSpinner(myPanel, "Search Depth", new SpinnerNumberModel(6, 0, Integer.MAX_VALUE, 1));
        this.timeResourcesSpinner = addLabeledSpinner(myPanel, "Time Resources (ms)", new SpinnerNumberModel(5000, 0, Integer.MAX_VALUE, 1000));

        final JButton cancelButton = new JButton("Cancel");
        final JButton okButton = new JButton("OK");

        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lightPlayerType = lightComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
                if(lightPlayerType == PlayerType.COMPUTER) {
                    AIplayerLight = true;
                }
                darkPlayerType = darkComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
                if(darkPlayerType == PlayerType.COMPUTER) {
                    AIplayerDark = true;
                }
                lightStrategyType = lightStrategy1Button.isSelected() ? StrategyType.StrategyOne : StrategyType.StrategyTwo;
                darkStrategyType = darkStrategy1Button.isSelected() ? StrategyType.StrategyOne : StrategyType.StrategyTwo;
                GameSetup.this.setVisible(false);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("Cancel");
                GameSetup.this.setVisible(false);
            }
        });

        myPanel.add(cancelButton);
        myPanel.add(okButton);

        setLocationRelativeTo(frame);
        pack();
        setVisible(false);
    }

    void promptUser() {
        setVisible(true);
        repaint();
    }

    public boolean isAIPlayer(final Player player) {
        if(player.getAlliance() == Alliance.LIGHT) {
            return getLightPlayerType() == PlayerType.COMPUTER;
        }
        return getDarkPlayerType() == PlayerType.COMPUTER;
    }

    PlayerType getLightPlayerType() {
        return this.lightPlayerType;
    }

    PlayerType getDarkPlayerType() {
        return this.darkPlayerType;
    }

    public boolean isStrategyOne(final Player player) {
        if(player.getAlliance() == Alliance.LIGHT) {
            return getLightStrategyType() == StrategyType.StrategyOne;
        }
        return getDarkStrategyType() == StrategyType.StrategyOne;
    }

    StrategyType getLightStrategyType() {
        return this.lightStrategyType;
    }

    StrategyType getDarkStrategyType() {
        return this.darkStrategyType;
    }

    private static JSpinner addLabeledSpinner(Container c, String label, SpinnerModel model) {
        final JLabel l = new JLabel(label);
        c.add(l);
        final JSpinner spinner = new JSpinner(model);
        l.setLabelFor(spinner);
        c.add(spinner);
        return spinner;
    }

    public int getSearchDepth() {
        return (Integer)this.searchDepthSpinner.getValue();
    }

    public int getTimeResources() {
        return (Integer)this.timeResourcesSpinner.getValue();
    }
}
