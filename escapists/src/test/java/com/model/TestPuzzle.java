package com.model;

import com.model.Coding.Gameplay.InteractItems.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit 4 tests for the Puzzle class.
 * Tests basic functionality and state management of a generic puzzle.
 * @author Tyler Norman
 */
public class TestPuzzle {

    private Puzzle puzzle;

    @Before
    public void setUp() {
        puzzle = new Puzzle("answer", "A simple riddle", "Riddle One");
    }

    @Test
    public void testUserAnswerStripsWhitespaceAndLowercases() {
        String input = "   AnSwEr  ";
        String formatted = puzzle.userAnswer(input);
        assertEquals("answer", formatted);
    }

    @Test
    public void testCheckAnswerCorrectGuess() {
        boolean result = puzzle.checkAnswer("answer");
        assertTrue("Should return true for correct guess", result);
        assertTrue("Puzzle should be marked as completed after correct guess", puzzle.getIsCompleted());
    }

    @Test
    public void testCheckAnswerIncorrectGuess() {
        boolean result = puzzle.checkAnswer("wrong");
        assertFalse("Should return false for incorrect guess", result);
        assertFalse("Puzzle should not be completed after incorrect guess", puzzle.getIsCompleted());
    }

    @Test
    public void testCheckAnswerHandlesNullAnswer() {
        Puzzle nullAnswerPuzzle = new Puzzle(null, "desc", "name");
        boolean result = nullAnswerPuzzle.checkAnswer("anything");
        assertTrue("If answer is null, should return true by design", result);
    }

    @Test
    public void testGettersReturnCorrectValues() {
        assertEquals("answer", puzzle.getAnswer());
        assertEquals("A simple riddle", puzzle.getDescription());
        assertEquals("Riddle One", puzzle.getName());
    }

    @Test
    public void testSetAndGetIsCompleted() {
        puzzle.setIsCompleted(true);
        assertTrue("Should set completion to true", puzzle.getIsCompleted());

        puzzle.setIsCompleted(false);
        assertFalse("Should set completion to false", puzzle.getIsCompleted());
    }

    @Test
    public void testGetPuzzleTypeDefault() {
        assertEquals("Default puzzle type should be GENERIC", Puzzle.PuzzleType.GENERIC, puzzle.getPuzzleType());
    }

    @Test
    public void testToStringDisplaysCorrectly() {
        String incompleteStr = puzzle.toString();
        assertTrue("Should display 'Not -_-' when incomplete", incompleteStr.contains("Not -_-"));

        puzzle.setIsCompleted(true);
        String completeStr = puzzle.toString();
        assertTrue("Should display 'Done =)' when completed", completeStr.contains("Done =)"));
    }
}
