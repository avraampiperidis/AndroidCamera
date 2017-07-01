package com.protectsoft.simplecam.test;

import android.widget.LinearLayout;

import com.protectsoft.simplecam.Utils.OrientationUtils;

import junit.framework.TestCase;

import org.junit.Test;

/**
 */
public class OrientationUtilsTest extends TestCase {

    @Test
    public void testGetCurrentOrientation() throws Exception {

        int expected = 0;
        for(int i =0; i <= 60; i++) {
            final int reality = OrientationUtils.getCurrentOrientation(i);
            assertEquals(expected, reality);
        }
        for(int i =301; i <= 360; i++) {
            final int reality = OrientationUtils.getCurrentOrientation(i);
            assertEquals(expected, reality);
        }

        expected = 270;
        for(int i = 61; i <= 120; i++) {
            final int reality = OrientationUtils.getCurrentOrientation(i);
            assertEquals(expected, reality);
        }

        expected = 180;
        for(int i = 121; i <= 240; i++) {
            final int reality = OrientationUtils.getCurrentOrientation(i);
            assertEquals(expected, reality);
        }

        expected = 90;
        for(int i = 241; i <= 300; i++) {
            final int reality = OrientationUtils.getCurrentOrientation(i);
            assertEquals(expected, reality);
        }

        expected = 0;
        final int reality = OrientationUtils.getCurrentOrientation(-1);
        assertEquals(expected, reality);


    }

    @Test
    public void testGetHorizontalOrVertical() throws Exception {

        int expected = LinearLayout.VERTICAL;
        for(int i =0; i <= 60; i++) {
            final int reality = OrientationUtils.getHorizontalOrVertical(i);
            assertEquals(expected, reality);
        }
        for(int i =301; i <= 360; i++) {
            final int reality = OrientationUtils.getHorizontalOrVertical(i);
            assertEquals(expected, reality);
        }

        expected = LinearLayout.HORIZONTAL;
        for(int i = 61; i <= 120; i++) {
            final int reality = OrientationUtils.getHorizontalOrVertical(i);
            assertEquals(expected, reality);
        }

        expected = LinearLayout.VERTICAL;
        for(int i = 121; i <= 240; i++) {
            final int reality = OrientationUtils.getHorizontalOrVertical(i);
            assertEquals(expected, reality);
        }

        expected = LinearLayout.HORIZONTAL;
        for(int i = 241; i <= 300; i++) {
            final int reality = OrientationUtils.getHorizontalOrVertical(i);
            assertEquals(expected, reality);
        }

        expected = LinearLayout.HORIZONTAL;
        final int reality = OrientationUtils.getHorizontalOrVertical(-1);
        assertEquals(expected, reality);

    }


}