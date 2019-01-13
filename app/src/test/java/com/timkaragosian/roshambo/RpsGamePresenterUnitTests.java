package com.timkaragosian.roshambo;

import android.text.TextUtils;

import com.timkaragosian.roshambo.Model.RpsGame;
import com.timkaragosian.roshambo.Presenter.RpsGamePresenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the RpsGamePresenter Class
 */
public class RpsGamePresenterUnitTests {
    RpsGame mRpsGame;
    RpsGamePresenter mRpsGamePresenter;

    RpsGamePresenter.OnGameStateChangedListener listener = new RpsGamePresenter.OnGameStateChangedListener() {
        @Override
        public void onGameStateChanged(RpsGame rpsGame) {

        }
    };

    @Before
    public void setup() {
        mRpsGame = new RpsGame();
        mRpsGamePresenter = new RpsGamePresenter(listener);
    }

    @Test
    public void handleRoundCountdownTest() {
        mRpsGamePresenter.handleRoundCountdown(3);
        mRpsGame = mRpsGamePresenter.getGameState();

        assertEquals("GET READY!", mRpsGame.getCountDownDisplayValue());
        assertEquals(0, mRpsGame.getPlayerThrowValue());
        assertEquals(0, mRpsGame.getComputerThrowValue());

        assertTrue(mRpsGame.getIsGamePhaseCountDown());
        assertEquals(3, mRpsGame.getCountDownSeconds());

        assertFalse(mRpsGame.getHasPlayerHasThrownThisRound());
        assertFalse(mRpsGame.getCanPlayerMakeLegalMove());
    }

    @Test
    public void setPlayerThrowTestIllegalMove() {
        mRpsGamePresenter = spy(mRpsGamePresenter);
        mRpsGamePresenter.mCountdownHandler = spy(mRpsGamePresenter.mCountdownHandler);
        doNothing().when(mRpsGamePresenter.mCountdownHandler).removeCallbacksAndMessages(null);

        mRpsGame.setCanPlayerMakeLegalMove(false);
        mRpsGamePresenter.setGameState(mRpsGame);
        mRpsGamePresenter.setPlayerThrow(1);
        mRpsGame = mRpsGamePresenter.getGameState();

        assertEquals(4, mRpsGame.getPlayerThrowValue());
        assertEquals(R.drawable.illegal_move, mRpsGame.getPlayerThrowImage());
        assertEquals("Illegal Move! Thrown Too Early!", mRpsGame.getPlayerMoveDescription());

        assertNull(mRpsGame.getComputerMoveDescription());
        assertEquals(0, mRpsGame.getComputerThrowImage());
        assertEquals(0, mRpsGame.getComputerThrowValue());

        assertTrue(mRpsGame.getHasPlayerHasThrownThisRound());
        assertTrue(mRpsGame.getIsGamePhaseRoundComplete());
        assertFalse(mRpsGame.getIsGamePhaseCountDown());
        assertFalse(mRpsGame.getCanPlayerMakeLegalMove());
    }

    @Test
    public void setPlayerThrowTestRockThrow() {
        mRpsGamePresenter = spy(mRpsGamePresenter);
        mRpsGamePresenter.mCountdownHandler = spy(mRpsGamePresenter.mCountdownHandler);
        doNothing().when(mRpsGamePresenter.mCountdownHandler).removeCallbacksAndMessages(null);

        mRpsGame.setCanPlayerMakeLegalMove(true);
        mRpsGamePresenter.setGameState(mRpsGame);
        mRpsGamePresenter.setPlayerThrow(1);
        mRpsGame = mRpsGamePresenter.getGameState();

        assertEquals(1, mRpsGame.getPlayerThrowValue());
        assertEquals(R.drawable.rock_image, mRpsGame.getPlayerThrowImage());
        assertEquals("Rock", mRpsGame.getPlayerMoveDescription());

        assertNotNull(mRpsGame.getComputerMoveDescription());
        assertTrue(mRpsGame.getComputerThrowImage() != 0);
        assertTrue(mRpsGame.getComputerThrowValue() != 0);

        assertTrue(mRpsGame.getHasPlayerHasThrownThisRound());
        assertTrue(mRpsGame.getIsGamePhaseRoundComplete());
        assertFalse(mRpsGame.getIsGamePhaseCountDown());
        assertFalse(mRpsGame.getCanPlayerMakeLegalMove());
    }

    @Test
    public void setPlayerThrowTestPaperThrow() {
        mRpsGamePresenter = spy(mRpsGamePresenter);
        mRpsGamePresenter.mCountdownHandler = spy(mRpsGamePresenter.mCountdownHandler);
        doNothing().when(mRpsGamePresenter.mCountdownHandler).removeCallbacksAndMessages(null);

        mRpsGame.setCanPlayerMakeLegalMove(true);
        mRpsGamePresenter.setGameState(mRpsGame);
        mRpsGamePresenter.setPlayerThrow(2);
        mRpsGame = mRpsGamePresenter.getGameState();

        assertEquals(2, mRpsGame.getPlayerThrowValue());
        assertEquals(R.drawable.paper, mRpsGame.getPlayerThrowImage());
        assertEquals("Paper", mRpsGame.getPlayerMoveDescription());

        assertNotNull(mRpsGame.getComputerMoveDescription());
        assertTrue(mRpsGame.getComputerThrowImage() != 0);
        assertTrue(mRpsGame.getComputerThrowValue() != 0);

        assertTrue(mRpsGame.getHasPlayerHasThrownThisRound());
        assertTrue(mRpsGame.getIsGamePhaseRoundComplete());
        assertFalse(mRpsGame.getIsGamePhaseCountDown());
        assertFalse(mRpsGame.getCanPlayerMakeLegalMove());
    }

    @Test
    public void setPlayerThrowTestScissorsThrow() {
        mRpsGamePresenter = spy(mRpsGamePresenter);
        mRpsGamePresenter.mCountdownHandler = spy(mRpsGamePresenter.mCountdownHandler);
        doNothing().when(mRpsGamePresenter.mCountdownHandler).removeCallbacksAndMessages(null);

        mRpsGame.setCanPlayerMakeLegalMove(true);
        mRpsGamePresenter.setGameState(mRpsGame);
        mRpsGamePresenter.setPlayerThrow(3);
        mRpsGame = mRpsGamePresenter.getGameState();

        assertEquals(3, mRpsGame.getPlayerThrowValue());
        assertEquals(R.drawable.scissors, mRpsGame.getPlayerThrowImage());
        assertEquals("Scissors", mRpsGame.getPlayerMoveDescription());

        assertNotNull(mRpsGame.getComputerMoveDescription());
        assertTrue(mRpsGame.getComputerThrowImage() != 0);
        assertTrue(mRpsGame.getComputerThrowValue() != 0);

        assertTrue(mRpsGame.getHasPlayerHasThrownThisRound());
        assertTrue(mRpsGame.getIsGamePhaseRoundComplete());
        assertFalse(mRpsGame.getIsGamePhaseCountDown());
        assertFalse(mRpsGame.getCanPlayerMakeLegalMove());
    }

    @Test
    public void determineWinnerIllegalMoveTest() {
        mRpsGamePresenter = spy(mRpsGamePresenter);
        mRpsGamePresenter.mCountdownHandler = spy(mRpsGamePresenter.mCountdownHandler);
        doNothing().when(mRpsGamePresenter.mCountdownHandler).removeCallbacksAndMessages(null);

        mRpsGame.setCanPlayerMakeLegalMove(false);
        mRpsGame.setComputerScore(0);
        mRpsGame.setPlayerScore(0);

        mRpsGamePresenter.setGameState(mRpsGame);
        mRpsGamePresenter.setPlayerThrow(1);

        mRpsGame = mRpsGamePresenter.getGameState();

        assertTrue(mRpsGame.getHasComputerWon());
        assertFalse(mRpsGame.getHasPlayerWon());

        assertEquals(0, mRpsGame.getPlayerScore());
        assertEquals(1, mRpsGame.getComputerScore());
    }

    @Test
    public void determineWinnerPlayerWinsTest() {
        mRpsGamePresenter = spy(mRpsGamePresenter);
        mRpsGamePresenter.mCountdownHandler = spy(mRpsGamePresenter.mCountdownHandler);
        doNothing().when(mRpsGamePresenter.mCountdownHandler).removeCallbacksAndMessages(null);

        doNothing().when(mRpsGamePresenter).computerThrowsMove();

        mRpsGame.setComputerThrowValue(3);
        mRpsGame.setCanPlayerMakeLegalMove(true);
        mRpsGame.setComputerScore(0);
        mRpsGame.setPlayerScore(0);

        mRpsGamePresenter.setGameState(mRpsGame);
        mRpsGamePresenter.setPlayerThrow(1);

        mRpsGame = mRpsGamePresenter.getGameState();

        assertFalse(mRpsGame.getHasComputerWon());
        assertTrue(mRpsGame.getHasPlayerWon());

        assertEquals(1, mRpsGame.getPlayerScore());
        assertEquals(0, mRpsGame.getComputerScore());
    }

    @Test
    public void determineWinnerComputerWinsTest() {
        mRpsGamePresenter = spy(mRpsGamePresenter);
        mRpsGamePresenter.mCountdownHandler = spy(mRpsGamePresenter.mCountdownHandler);
        doNothing().when(mRpsGamePresenter.mCountdownHandler).removeCallbacksAndMessages(null);

        doNothing().when(mRpsGamePresenter).computerThrowsMove();

        mRpsGame.setComputerThrowValue(2);
        mRpsGame.setCanPlayerMakeLegalMove(true);
        mRpsGame.setComputerScore(0);
        mRpsGame.setPlayerScore(0);

        mRpsGamePresenter.setGameState(mRpsGame);
        mRpsGamePresenter.setPlayerThrow(1);

        mRpsGame = mRpsGamePresenter.getGameState();

        assertTrue(mRpsGame.getHasComputerWon());
        assertFalse(mRpsGame.getHasPlayerWon());

        assertEquals(0, mRpsGame.getPlayerScore());
        assertEquals(1, mRpsGame.getComputerScore());
    }

    @Test
    public void computerThrowsMoveTest() {
        mRpsGamePresenter.computerThrowsMove();
        mRpsGame = mRpsGamePresenter.getGameState();

        assertTrue(mRpsGame.getComputerThrowValue() == 1 || mRpsGame.getComputerThrowValue() == 2 || mRpsGame.getComputerThrowValue() == 3);
        assertTrue(mRpsGame.getComputerThrowImage() != 0);
        assertNotNull(mRpsGame.getComputerMoveDescription());
    }

    @Test
    public void setupCountdownHandlerTest() {
        mRpsGamePresenter.setupCountdownHandler(1);
        mRpsGame = mRpsGamePresenter.getGameState();

        assertEquals(1, mRpsGame.getCountDownSeconds());
        assertFalse(mRpsGame.getIsGamePhaseRoundComplete());
        assertFalse(mRpsGame.getHasPlayerWon());
        assertFalse(mRpsGame.getHasComputerWon());
    }

    @Test
    public void setupThrowNowHandlerTest() {
        mRpsGamePresenter = spy(mRpsGamePresenter);
        mRpsGamePresenter.mCountdownHandler = spy(mRpsGamePresenter.mCountdownHandler);
        doNothing().when(mRpsGamePresenter.mCountdownHandler).removeCallbacksAndMessages(null);

        mRpsGamePresenter.setupThrowNowHandler();
        mRpsGame = mRpsGamePresenter.getGameState();

        assertTrue(mRpsGame.getCanPlayerMakeLegalMove());
        assertEquals("THROW NOW!", mRpsGame.getCountDownDisplayValue());
    }

    @Test
    public void playerIllegalMoveTooLateTest() {
        mRpsGamePresenter.playerIllegalMoveTooLate();
        mRpsGame = mRpsGamePresenter.getGameState();

        assertTrue(mRpsGame.getHasComputerWon());
        assertFalse(mRpsGame.getHasPlayerWon());
        assertEquals(1, mRpsGame.getComputerScore());
        assertTrue(mRpsGame.getIsGamePhaseRoundComplete());
        assertFalse(mRpsGame.getIsGamePhaseCountDown());
        assertFalse(mRpsGame.getCanPlayerMakeLegalMove());
        assertEquals(0, mRpsGame.getComputerThrowImage());
        assertEquals(R.drawable.illegal_move, mRpsGame.getPlayerThrowImage());
        assertEquals("Illegal Move! No Move Thrown!", mRpsGame.getPlayerMoveDescription());
        assertEquals(4, mRpsGame.getPlayerThrowValue());
    }
}