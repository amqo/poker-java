/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.amqo.pokermaven;

import com.amqo.pokermaven.game.Combination;
import java.util.Arrays;
import java.util.Collection;
import org.hamcrest.BaseMatcher;
import static org.hamcrest.CoreMatchers.*;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 *
 * @author alberto
 */
@RunWith(value = Parameterized.class)
public class CombinationTest {

    private final int subItems;
    private final int items;
    private final int combinationsExpected;

    public CombinationTest(int subItems, int items, int combsExpected) {
        this.subItems = subItems;
        this.items = items;
        this.combinationsExpected = combsExpected;
    }

    @Parameters
    public static Collection<Object[]> data() {
        Object data[][] = {
            {1, 1, 1},
            {1, 2, 2},
            {2, 2, 1},
            {1, 3, 3},
            {2, 3, 3},
            {3, 3, 1},
            {1, 4, 4},
            {2, 4, 6},
            {3, 4, 4},
            {4, 4, 1},
            {1, 5, 5},
            {2, 5, 10},
            {5, 7, 21}
        };
        return Arrays.asList(data);
    }

    @Test
    public void testSize() {
        Combination instance = new Combination(subItems, items);
        int sizeResult = instance.size();
        assertEquals(subItems, sizeResult);
    }

    @Test
    public void testCombinations() {
        Combination instance = new Combination(subItems, items);
        long result = instance.combinations();
        assertEquals(combinationsExpected, result);
    }

    @Test
    public void tesHasNextFirst() {
        Combination instance = new Combination(subItems, items);
        assertTrue(instance.hasNext());
    }

    @Test
    public void testHasNextPreLast() {
        Combination instance = new Combination(subItems, items);
        int[] indexes = new int[instance.size()];
        for (int i = 0; i < combinationsExpected - 1; i++) {
            instance.next(indexes);
        }
        assertTrue(instance.hasNext());
    }

    @Test
    public void testHasNextLast() {
        Combination instance = new Combination(subItems, items);
        int[] indexes = new int[instance.size()];
        for (int i = 0; i < combinationsExpected; i++) {
            instance.next(indexes);
        }
        assertFalse(instance.hasNext());
    }

    @Test
    public void testHasNextAfterClear() {
        Combination instance = new Combination(subItems, items);
        instance.clear();
        assertTrue(instance.hasNext());
    }

    @Test
    public void testHasNextAfterClearWithNext() {
        Combination instance = new Combination(subItems, items);
        int indexes[] = new int[subItems];
        instance.next(indexes);
        instance.clear();
        assertTrue(instance.hasNext());
    }

    @Test
    public void testHasNextAfterFullLoopClear() {
        Combination instance = new Combination(subItems, items);
        int indexes[] = new int[subItems];
        for (int i = 0; i < combinationsExpected; i++) {
            instance.next(indexes);
        }
        instance.clear();
        assertTrue(instance.hasNext());
    }

    private Matcher<Integer> isBetween(final int low, final int top) {
        return new BaseMatcher<Integer>() {
            @Override
            public boolean matches(final Object item) {
                final Integer number = (Integer) item;
                return number > low && number < top;
            }

            @Override
            public void describeTo(final Description description) {
                description.appendText("isBetween should return if value between ")
                        .appendValue(low).appendText(" and ").appendValue(top);
            }
        };
    }

    @Test
    public void testNextItemsRange() {
        Combination instance = new Combination(subItems, items);
        int[] indexes = new int[instance.size()];
        for (int i = 0; i < combinationsExpected; i++) {
            instance.next(indexes);
            assertThat(indexes[0], isBetween(0, items - 1));
            for (int j = 1; j < subItems; j++) {
                assertThat(indexes[j], isBetween(indexes[j - 1] + 1, items - 1));
            }
        }
    }

    @Test
    public void testNextDontRepeat() {
        Combination instance = new Combination(subItems, items);
        int results[][] = new int[combinationsExpected][];
        for (int i = 0; i < combinationsExpected; i++) {
            results[i] = new int[subItems];
            instance.next(results[i]);
            for (int k = 0; k < i; k++) {
                assertThat(results[i], not(equalTo(results[k])));
            }
        }
    }

    @Test
    public void testNextIgnored() {
        Combination instance = new Combination(subItems, items);
        int indexes[] = new int[subItems];
        int expIndexes[] = new int[subItems];
        for (int i = 0; i < combinationsExpected; i++) {
            instance.next(indexes);
        }
        Arrays.fill(indexes, -1);
        Arrays.fill(expIndexes, -1);
        instance.next(indexes);
        assertArrayEquals(expIndexes, indexes);
    }

    @Test
    public void testNextAfterClear() {
        Combination instance = new Combination(subItems, items);
        instance.clear();
        int expectIndexes[][] = new int[combinationsExpected][];
        for (int i = 0; i < combinationsExpected; i++) {
            expectIndexes[i] = new int[subItems];
            instance.next(expectIndexes[i]);
        }
        instance.clear();
        int[] indexes = new int[subItems];
        for (int j = 0; j < combinationsExpected - 1; j++) {
            instance.next(indexes);
            assertArrayEquals(expectIndexes[j], indexes);
        }
    }
}
