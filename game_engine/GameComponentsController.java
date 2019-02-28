package game_engine;

import constants.GameConstants;
import constants.MoveResult;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * This class represents the game made up of separate components in Backgammon.
 * This class creates a game made out of modular panes/nodes.
 * 
 * @teamname TeaCup
 * @author Bryan Sng, 17205050
 * @author @LxEmily, 17200573
 * @author Braddy Yeoh, 17357376
 *
 */
public class GameComponentsController extends VBox {
	private PlayerPanel topUserPnl, bottomUserPnl;
	private Bars bars;
	private Board board;
	private HomePanel leftHome, rightHome, mainHome;
	
	/**
	 * Default Constructor
	 * 		- Initializes the modular game components.
	 */
	public GameComponentsController(Player bottomPlayer, Player topPlayer) {
		super();
		initGameComponents(bottomPlayer, topPlayer);
	}
	
	/**
	 * Initializes the game by creating the components and putting them together.
	 * i.e. initializing the game layout.
	 */
	public void initGameComponents(Player bottomPlayer, Player topPlayer) {
		board = new Board();
		bars = new Bars();
		leftHome = new HomePanel();
		rightHome = new HomePanel();
		switch (Settings.getMainQuadrant()) {
			case BOTTOM_RIGHT:
			case TOP_RIGHT:
				mainHome = rightHome;
				break;
			case BOTTOM_LEFT:
			case TOP_LEFT:
				mainHome = leftHome;
				break;
		}
		
		HBox middlePart = board;
		middlePart.setMinWidth(GameConstants.getMiddlePartWidth());
		middlePart.getChildren().add(1, bars);
		middlePart.getChildren().add(0, leftHome);
		middlePart.getChildren().add(rightHome);
		
		topUserPnl = new PlayerPanel(middlePart.getMinWidth(), topPlayer);
		bottomUserPnl = new PlayerPanel(middlePart.getMinWidth(), bottomPlayer);
		
		getChildren().addAll(topUserPnl, middlePart, bottomUserPnl);
		setStyle(GameConstants.getGameColour());
		setMaxHeight(topUserPnl.getMinHeight() + middlePart.getHeight() + bottomUserPnl.getMinHeight());
	}
	
	/**
	 * Returns a boolean value indicating if the any of the homes are filled.
	 * @return the boolean value.
	 */
	public boolean isHomeFilled() {
		return mainHome.isFilled();
	}
	
	/**
	 * Moves a checker from a pip to the bar.
	 * i.e. pops a checker from one pip and push it to the bar.
	 * 
	 * @param fro, zero-based index, the pip number to pop from.
	 * @return returns a integer value indicating if the checker was moved.
	 */
	public MoveResult moveToBar(int fro) {
		MoveResult moveResult = MoveResult.NOT_MOVED;
		
		Pip[] pips = board.getPips();
		Bar bar = bars.getBar(pips[fro].top().getColor());
		// Checking if its empty is actually done by moveCheckers,
		// since this method is always called after moveCheckers.
		// so this is actually not needed, but is left here just in case.
		if (!pips[fro].isEmpty()) {
			bar.push(pips[fro].pop());
			moveResult = MoveResult.MOVED_TO_BAR;
			
			pips[fro].drawCheckers();
			bar.drawCheckers();
		}
		return moveResult;
	}
	
	/**
	 * Moves a checker from bar to a pip.
	 * i.e. pops a checker from bar and push it to a pip.
	 * 
	 * @param fromBar, color of the bar to pop from.
	 * @param to, zero-based index, the pip number to push to.
	 * @return returns a integer value indicating if the checker was moved.
	 */
	public MoveResult moveFromBar(Color fromBar, int to) {
		MoveResult moveResult = MoveResult.NOT_MOVED;
		
		Pip[] pips = board.getPips();
		Bar bar = bars.getBar(fromBar);
		if (!bar.isEmpty()) {
			if (bar.topCheckerColourEquals(pips[to])) {
				pips[to].push(bar.pop());
				moveResult = MoveResult.MOVED_FROM_BAR;
			} else {
				if (pips[to].size() == 1) {
					moveResult = MoveResult.MOVE_TO_BAR;
				}
			}
			
			pips[to].drawCheckers();
			bar.drawCheckers();
		}
		return moveResult;
	}
	
	/**
	 * Moves a checker from a pip to its home.
	 * i.e. pops a checker from bar and push it to a pip.
	 * 
	 * @param fro, zero-based index, the pip number to pop from.
	 * @return returns a integer value indicating if the checker was moved.
	 */
	public MoveResult moveToHome(int fro) {
		MoveResult moveResult = MoveResult.NOT_MOVED;
		
		Pip[] pips = board.getPips();
		if (!pips[fro].isEmpty()) {
			Home home = mainHome.getHome(pips[fro].top().getColor());
			home.push(pips[fro].pop());
			moveResult = MoveResult.MOVED_TO_HOME_FROM_PIP;

			pips[fro].drawCheckers();
			home.drawCheckers();
		}
		return moveResult;
	}
	
	/**
	 * Moves a checker from bar to its home.
	 * i.e. pops a checker from bar and push it to a pip.
	 * 
	 * @param fromBar, color of the bar to pop from.
	 * @return returns a integer value indicating if the checker was moved.
	 */
	public MoveResult moveToHome(Color fromBar) {
		MoveResult moveResult = MoveResult.NOT_MOVED;
		
		Bar bar = bars.getBar(fromBar);
		if (!bar.isEmpty()) {
			Home home = mainHome.getHome(bar.top().getColor());
			home.push(bar.pop());
			moveResult = MoveResult.MOVED_TO_HOME_FROM_BAR;

			bar.drawCheckers();
			home.drawCheckers();
		}
		return moveResult;
	}
	
	public Board getBoard() {
		return board;
	}
	public PlayerPanel getTopPlayerPanel() {
		return topUserPnl;
	}
	public PlayerPanel getBottomPlayerPanel() {
		return bottomUserPnl;
	}
}
