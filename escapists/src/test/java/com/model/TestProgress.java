package com.model;

import com.model.Coding.Gameplay.InteractItems.Puzzle;
import com.model.Coding.Gameplay.Map.Room;
import com.model.Coding.Progress.Progress;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Tests for com.model.Coding.Progress.Progress (non-getter/setter behavior)
 */
public class TestProgress {

    @Before
    public void beforeEach() {
        // nothing global to reset for Progress (it does not use singletons)
    }

    @Test
    public void testDefaultConstructorInitializesFields() {
        Progress p = new Progress();
        assertNotNull("progressId should be initialized", p.getProgressId());
        assertEquals("difficulty default", 0, p.getDifficulty());
        assertEquals("remaining time default", 0, p.getRemainingTime());
        // completed puzzles count initially zero
        assertEquals(0, p.getCompletedPuzzlesCount());
        // inventory non-null
        assertNotNull(p.getInventory());
        // achievements empty list
        assertNotNull(p.getAchievements());
        assertTrue(p.getAchievements().isEmpty());
    }

    @Test
    public void testConstructorWithIdAndCompletedPuzzlesPreservesValues() {
        UUID id = UUID.randomUUID();
        HashMap<String, HashMap<String, Boolean>> comp = new HashMap<>();
        HashMap<String, Boolean> mapForRoom = new HashMap<>();
        mapForRoom.put("puzzleA", true);
        comp.put("RoomA", mapForRoom);

        Progress p = new Progress(id, comp);

        assertEquals("progress id preserved", id, p.getProgressId());
        assertEquals("completed puzzles count", 1, p.getCompletedPuzzlesCount());
        // ensure the returned map contains our values
        HashMap<String, HashMap<String, Boolean>> returned = p.getCompletedPuzzles();
        assertTrue(returned.containsKey("RoomA"));
        assertTrue(returned.get("RoomA").get("puzzleA"));
    }

    @Test
    public void testToStringWhenNoCompletedPuzzlesShowsNone() {
        Progress p = new Progress();
        String s = p.toString();
        assertNotNull(s);
        assertTrue(s.contains("Completed Puzzles:"));
        assertTrue(s.contains("None"));
        assertTrue(s.contains("Remaining Time:"));
    }

    @Test
    public void testToStringWithCompletedPuzzlesFormatsEntries() {
        HashMap<String, HashMap<String, Boolean>> comp = new HashMap<>();
        HashMap<String, Boolean> r = new HashMap<>();
        r.put("p1", true);
        r.put("p2", false);
        comp.put("R", r);

        Progress p = new Progress(UUID.randomUUID(), comp);
        String s = p.toString();

        assertTrue(s.contains("Completed Puzzles:"));
        assertTrue(s.contains("R:"));
        assertTrue(s.contains("p1 -> yes"));
        assertTrue(s.contains("p2 -> no"));
    }

    @Test
    public void testSetCurrentRoomAlsoSetsCurrentRoomName() {
        Progress p = new Progress();
        Room mockRoom = mock(Room.class);
        when(mockRoom.getName()).thenReturn("TestRoom");

        p.setCurrentRoom(mockRoom);

        assertEquals("current room name should be set", "TestRoom", p.getCurrentRoomName());
        assertSame("current room object should be stored", mockRoom, p.getCurrentRoom());
    }

    @Test
    public void testMarkRoomCompletedAddsUniqueRooms() {
        Progress p = new Progress();
        Room r1 = mock(Room.class);
        Room r2 = mock(Room.class);

        p.markRoomCompleted(r1);
        p.markRoomCompleted(r1); // duplicate should not be added twice
        p.markRoomCompleted(r2);

        ArrayList<Room> completed = p.getCompletedRooms();
        assertEquals(2, completed.size());
        assertTrue(completed.contains(r1));
        assertTrue(completed.contains(r2));
    }

    @Test
    public void testGetCompletedRoomsReturnsCopyNotBackingList() {
        Progress p = new Progress();
        Room r = mock(Room.class);
        p.markRoomCompleted(r);

        ArrayList<Room> copy = p.getCompletedRooms();
        // mutate returned list
        copy.clear();

        // original should remain unchanged
        ArrayList<Room> after = p.getCompletedRooms();
        assertEquals(1, after.size());
        assertTrue(after.contains(r));
    }

    @Test
    public void testSetPuzzleCompletedWithBoolUpdatesMapAndCallsPuzzleAndRoom() {
        Progress p = new Progress();
        Room room = mock(Room.class);
        Puzzle puzzle = mock(Puzzle.class);

        when(room.getName()).thenReturn("RoomX");
        when(puzzle.getName()).thenReturn("PuzzleX");

        // initially no entry; call with bool true
        p.setPuzzleCompleted(room, puzzle, true);

        // verify puzzle.setIsCompleted and room.updateExits called
        verify(puzzle, times(1)).setIsCompleted(true);
        verify(room, times(1)).updateExits();

        // check that completedPuzzles contains the entry via public getter
        HashMap<String, HashMap<String, Boolean>> cp = p.getCompletedPuzzles();
        assertTrue(cp.containsKey("RoomX"));
        assertTrue(cp.get("RoomX").get("PuzzleX"));
        assertEquals(1, p.getCompletedPuzzlesCount());
    }

    @Test
    public void testSetPuzzleCompletedWithoutBoolUsesStoredValueIfPresent() {
        // prepare a Progress with an existing completedPuzzles map
        HashMap<String, HashMap<String, Boolean>> comp = new HashMap<>();
        HashMap<String, Boolean> roomMap = new HashMap<>();
        roomMap.put("pZ", true);
        comp.put("RoomZ", roomMap);

        Progress p = new Progress(UUID.randomUUID(), comp);

        Room mockRoom = mock(Room.class);
        Puzzle mockPuzzle = mock(Puzzle.class);
        when(mockRoom.getName()).thenReturn("RoomZ");
        when(mockPuzzle.getName()).thenReturn("pZ");

        // call overload that reads from progress data
        p.setPuzzleCompleted(mockRoom, mockPuzzle);

        // verify puzzle marked as completed and room update called
        verify(mockPuzzle, times(1)).setIsCompleted(true);
        verify(mockRoom, times(1)).updateExits();
    }

    @Test
    public void testSetPuzzleCompletedWithoutBoolDoesNothingIfNoEntry() {
        Progress p = new Progress();
        Room room = mock(Room.class);
        Puzzle puzzle = mock(Puzzle.class);
        when(room.getName()).thenReturn("NoRoom");
        when(puzzle.getName()).thenReturn("noPuzzle");

        // since completedPuzzles has no entry for room, the method should return early
        p.setPuzzleCompleted(room, puzzle);

        verify(puzzle, never()).setIsCompleted(anyBoolean());
        verify(room, never()).updateExits();
    }

    @Test
    public void testGetCompletedPuzzlesReturnsDeepCopy() {
        HashMap<String, HashMap<String, Boolean>> comp = new HashMap<>();
        HashMap<String, Boolean> roomMap = new HashMap<>();
        roomMap.put("a", true);
        comp.put("R", roomMap);

        Progress p = new Progress(UUID.randomUUID(), comp);
        HashMap<String, HashMap<String, Boolean>> copy = p.getCompletedPuzzles();

        // mutate returned copy
        copy.get("R").put("a", false);
        // original inside Progress should remain unchanged
        HashMap<String, HashMap<String, Boolean>> after = p.getCompletedPuzzles();
        assertTrue(after.get("R").get("a")); // still true
    }

    @Test
    public void testAllPuzzlesCompletedTrueAndFalse() {
        HashMap<String, HashMap<String, Boolean>> comp = new HashMap<>();
        HashMap<String, Boolean> r = new HashMap<>();
        r.put("p1", true);
        r.put("p2", true);
        comp.put("RoomM", r);

        Progress p = new Progress(UUID.randomUUID(), comp);

        Room roomMock = mock(Room.class);
        when(roomMock.getName()).thenReturn("RoomM");

        assertTrue("allPuzzlesCompleted should return true when all are true", p.allPuzzlesCompleted(roomMock));

        // change to have one false
        r.put("p2", false);
        // Important: Progress stores a reference to the map you passed into constructor (we didn't clone there),
        // but getCompletedPuzzles returns copies. So calling allPuzzlesCompleted should reflect the updated value.
        assertFalse("allPuzzlesCompleted should return false if a puzzle is false", p.allPuzzlesCompleted(roomMock));
    }

    @Test
    public void testHintsUsedIncrementAndSetter() {
        Progress p = new Progress();
        assertEquals(0, p.getHintsUsed());

        p.incrementHintsUsed();
        assertEquals(1, p.getHintsUsed());

        p.setHintsUsed(5);
        assertEquals(5, p.getHintsUsed());
    }

    @Test
    public void testMarkRoomCompletedWithNullDoesNothing() {
        Progress p = new Progress();
        // initial state: no completed rooms
        assertTrue(p.getCompletedRooms().isEmpty());

        // should not throw and should not add anything
        p.markRoomCompleted(null);

        assertTrue("markRoomCompleted(null) should not add anything", p.getCompletedRooms().isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testSetPuzzleCompletedWithNullRoomThrows() {
        Progress p = new Progress();
        Puzzle puzzle = mock(Puzzle.class);
        // Passing null as room should throw NPE because method dereferences room.getName()
        p.setPuzzleCompleted(null, puzzle, true);
    }

    @Test(expected = NullPointerException.class)
    public void testSetPuzzleCompletedWithNullPuzzleThrows() {
        Progress p = new Progress();
        Room room = mock(Room.class);
        when(room.getName()).thenReturn("R");
        // Passing null as puzzle should throw NPE because method dereferences puzzle.getName()
        p.setPuzzleCompleted(room, null, true);
    }

    @Test
    public void testSetCurrentRoomWithNullClearsNameAndRoom() {
        Progress p = new Progress();

        // set a room first
        Room r = mock(Room.class);
        when(r.getName()).thenReturn("SomeRoom");
        p.setCurrentRoom(r);
        assertEquals("SomeRoom", p.getCurrentRoomName());
        assertSame(r, p.getCurrentRoom());

        // now set null: should clear both currentRoom and currentRoomName (no exception)
        p.setCurrentRoom(null);
        assertNull("currentRoom should be null after setCurrentRoom(null)", p.getCurrentRoom());
        assertNull("currentRoomName should be null after setCurrentRoom(null)", p.getCurrentRoomName());
    }

    @Test
    public void testStressConcurrencyAndInvariantConsistency() throws Exception {
        final Progress p = new Progress();

        // Prepare mocks for Room and Puzzle used by all threads
        final Room room = mock(Room.class);
        when(room.getName()).thenReturn("StressRoom");
        // room.updateExits will be called repeatedly; we just verify it's callable
        doNothing().when(room).updateExits();

        final Puzzle puzzle = mock(Puzzle.class);
        when(puzzle.getName()).thenReturn("StressPuzzle");
        doNothing().when(puzzle).setIsCompleted(anyBoolean());

        // Pre-populate map sometimes so overload that reads from map sometimes has data
        HashMap<String, HashMap<String, Boolean>> initial = new HashMap<>();
        HashMap<String, Boolean> forRoom = new HashMap<>();
        forRoom.put("StressPuzzle", true);
        initial.put("StressRoom", forRoom);
        // Re-initialize progress internals to a map we can mutate concurrently by the test (Progress constructor will overwrite,
        // so use reflection to set completedPuzzles to our initial map)
        java.lang.reflect.Field compField = Progress.class.getDeclaredField("completedPuzzles");
        compField.setAccessible(true);
        compField.set(p, initial);

        final int THREADS = 40;
        final int OPS_PER_THREAD = 2000;

        final java.util.concurrent.CountDownLatch start = new java.util.concurrent.CountDownLatch(1);
        final java.util.concurrent.CountDownLatch done = new java.util.concurrent.CountDownLatch(THREADS);
        final java.util.concurrent.atomic.AtomicBoolean exceptionOccurred = new java.util.concurrent.atomic.AtomicBoolean(false);

        for (int t = 0; t < THREADS; t++) {
            new Thread(() -> {
                try {
                    start.await();
                    java.util.Random rnd = new java.util.Random();

                    for (int i = 0; i < OPS_PER_THREAD; i++) {
                        int op = rnd.nextInt(6);
                        try {
                            switch (op) {
                                case 0:
                                    // set puzzle completed to true
                                    p.setPuzzleCompleted(room, puzzle, true);
                                    break;
                                case 1:
                                    // set puzzle completed to false
                                    p.setPuzzleCompleted(room, puzzle, false);
                                    break;
                                case 2:
                                    // use overload which reads from progress map
                                    p.setPuzzleCompleted(room, puzzle);
                                    break;
                                case 3:
                                    // mark room completed (should avoid duplicates)
                                    p.markRoomCompleted(room);
                                    break;
                                case 4:
                                    // read completed puzzles (copy)
                                    HashMap<String, HashMap<String, Boolean>> snapshot = p.getCompletedPuzzles();
                                    // do a harmless mutation to the snapshot to verify it's a copy
                                    if (snapshot.containsKey("StressRoom")) {
                                        snapshot.get("StressRoom").put("zz_tmp_" + i, (i % 2 == 0));
                                    }
                                    break;
                                case 5:
                                    // call allPuzzlesCompleted (reads map)
                                    p.allPuzzlesCompleted(room);
                                    break;
                            }
                        } catch (Throwable ex) {
                            // capture any exception (NPE, CME, etc.)
                            exceptionOccurred.set(true);
                            // also fail-fast print to help debugging
                            ex.printStackTrace();
                            break;
                        }
                    }
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    exceptionOccurred.set(true);
                } finally {
                    done.countDown();
                }
            }, "stress-thread-" + t).start();
        }

        // start all threads
        start.countDown();

        // wait for completion (with timeout to avoid hanging tests)
        boolean finished = done.await(60, java.util.concurrent.TimeUnit.SECONDS);
        assertTrue("Stress threads did not finish in time; possible hang", finished);

        // fail if any worker observed an exception
        assertFalse("An exception occurred during concurrent operations (see console). This indicates a bug or race.", exceptionOccurred.get());

        // ==== Post-condition invariants ====

        // 1) All keys and values in completedPuzzles must be non-null and Boolean typed
        HashMap<String, HashMap<String, Boolean>> finalMap = p.getCompletedPuzzles();
        assertNotNull("completedPuzzles should not be null", finalMap);

        for (String roomName : finalMap.keySet()) {
            assertNotNull("roomName key should not be null", roomName);
            HashMap<String, Boolean> inner = finalMap.get(roomName);
            assertNotNull("inner map should not be null for room " + roomName, inner);
            for (String puzzleName : inner.keySet()) {
                assertNotNull("puzzleName should not be null for room " + roomName, puzzleName);
                Boolean val = inner.get(puzzleName);
                assertNotNull("puzzle completed value should not be null for " + puzzleName, val);
            }
        }

        // 2) completedPuzzlesCount should match the number of top-level keys
        assertEquals("getCompletedPuzzlesCount must reflect top-level key count",
                finalMap.size(), p.getCompletedPuzzlesCount());

        // 3) getCompletedPuzzles returns a deep copy: mutate the returned map, original should not be affected
        HashMap<String, HashMap<String, Boolean>> snapshot2 = p.getCompletedPuzzles();
        if (!snapshot2.isEmpty()) {
            String firstRoom = snapshot2.keySet().iterator().next();
            snapshot2.get(firstRoom).put("injected_test_key", true);
            HashMap<String, HashMap<String, Boolean>> after = p.getCompletedPuzzles();
            // the injected key should NOT be present in the internal map's snapshot
            assertFalse("getCompletedPuzzles must return a deep copy; mutations of the returned map must not affect internal state",
                    after.get(firstRoom).containsKey("injected_test_key"));
        }

        // 4) getCompletedRooms returns a copy (mutating returned list doesn't change internal list)
        p.markRoomCompleted(room);
        java.util.ArrayList<Room> copyRooms = p.getCompletedRooms();
        int beforeSize = copyRooms.size();
        copyRooms.clear();
        java.util.ArrayList<Room> afterRooms = p.getCompletedRooms();
        assertEquals("getCompletedRooms must return a copy (mutating returned list must not change internal list)",
                beforeSize, afterRooms.size());
    }
}
