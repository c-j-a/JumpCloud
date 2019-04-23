package com.jc.action.service;

import com.jc.action.ActionStatistics;
import com.jc.error.Error;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

public class ActionServiceTest {

    private static final String JUMP = "jump";
    private static final String RUN = "run";
    private static final String WALK = "walk";


    /**
     * Simple test that matches the example from the assignment.
     */
    @Test
    public void simpleTest() {

        final ActionService actionService = new JsonActionService();

        Error error;

        // Add the actions
        error = actionService.addAction(getAction(JUMP, 100));
        Assert.assertNull(error);
        error = actionService.addAction(getAction(RUN, 75));
        Assert.assertNull(error);
        error = actionService.addAction(getAction(JUMP, 200));
        Assert.assertNull(error);

        // Validate the values are stored and calculated correctly
        ActionStatistics jumpStats = actionService.getActionStatistics(JUMP);
        assertResults(jumpStats, JUMP, 2, 300);

        ActionStatistics runStats = actionService.getActionStatistics(RUN);
        assertResults(runStats, RUN, 1, 75);

        // Validate the JSON
        String results = actionService.getStats();
        System.out.println(results);
        Assert.assertTrue(results.contains("\"action\":\"run\",\"avg\":75"));
        Assert.assertTrue(results.contains("\"action\":\"jump\",\"avg\":150}"));
        System.out.println();

    }

    /**
     * This will try to test the thread safety/concurrency
     * of the JsonActionService.
     */
    @Test
    public void threadedTest() {

        ThreadPoolExecutor executorPool = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);
        final ActionService actionService = new JsonActionService();

        long jumpTime1 = 100;
        long jumpTime2 = 200;
        long runTime1 = 50;
        long runTime2 = 100;
        long walkTime1 = 200;
        long walkTime2 = 300;

        // 100 Threads executing this code 1 million times
        int loops = 1000000;
        for (int i = 0; i < loops; i++) {
            executorPool.execute(() -> {
                // Mix in both reads and writes from each thread
                actionService.addAction(getAction(JUMP, jumpTime1));
                actionService.getStats();
                actionService.addAction(getAction(RUN, runTime1));
                actionService.getStats();
                actionService.addAction(getAction(WALK, walkTime1));
                actionService.getStats();
                actionService.addAction(getAction(WALK, walkTime2));
                actionService.getStats();
                actionService.addAction(getAction(RUN, runTime2));
                actionService.getStats();
                actionService.addAction(getAction(JUMP, jumpTime2));
                actionService.getStats();
            });
        }

        // Shut down the threads...
        executorPool.shutdown();
        try {
            executorPool.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Get the statistic objects to verify values
        ActionStatistics jumpStats = actionService.getActionStatistics(JUMP);
        ActionStatistics runStats = actionService.getActionStatistics(RUN);
        ActionStatistics walkStats = actionService.getActionStatistics(WALK);

        // Each loop added 2 jumps
        long expectedJumps = loops * 2;
        long expectedJumpTime = loops * (jumpTime1 + jumpTime2);

        // Each loop added 2 runs
        long expectedRuns = loops * 2;
        long expectedRunTime = loops * (runTime1 + runTime2);

        // Each loop added 2 walks
        long expectedWalks = loops * 2;
        long expectedWalkTime = loops * (walkTime1 + walkTime2);

        // Validate the values are stored and calculated correctly
        assertResults(jumpStats, JUMP, expectedJumps, expectedJumpTime);
        assertResults(runStats, RUN, expectedRuns, expectedRunTime);
        assertResults(walkStats, WALK, expectedWalks, expectedWalkTime);

        // Validate the JSON
        String results = actionService.getStats();
        System.out.println(results);
        Assert.assertTrue(results.contains("{\"action\":\"run\",\"avg\":75}"));
        Assert.assertTrue(results.contains("{\"action\":\"walk\",\"avg\":250}"));
        Assert.assertTrue(results.contains("{\"action\":\"jump\",\"avg\":150}"));
        System.out.println();
    }

    /**
     * Test for divide by zero errors on our ActionStatistics
     */
    @Test
    public void divideByZero() {
        ActionStatistics statistics = new ActionStatistics("test");
        long average = statistics.getAvg();
        Assert.assertEquals(0, average);
    }

    /**
     * Test that the ActionService returns an error
     */
    @Test
    public void badJson() {
        String bad = "{\"xaction\":\"test\", \"xtime\":100}";

        final ActionService actionService = new JsonActionService();

        // Bad JSON
        Error error = actionService.addAction(bad);
        Assert.assertNotNull(error);

        // null JSON
        error = actionService.addAction(null);
        Assert.assertNotNull(error);
    }

    private void assertResults(ActionStatistics actualStats,
                               String expectedName,
                               long expectedCount,
                               long expectedTime) {
        long expectedAvg = expectedCount == 0 ? 0 : expectedTime / expectedCount;
        System.out.println("Expected " + expectedName + ": " + expectedTime + " (time) / " +
                                   expectedCount + " (count) = " + expectedAvg + " (avg time)");
        Assert.assertEquals(expectedName, actualStats.getName());
        Assert.assertEquals(expectedCount, actualStats.getCount());
        Assert.assertEquals(expectedTime, actualStats.getTime());
        Assert.assertEquals(expectedAvg, actualStats.getAvg());
    }

    private static String getAction(String action, long time) {
        return "{\"action\":\"" + action + "\", \"time\":" + time + "}";
    }

}
