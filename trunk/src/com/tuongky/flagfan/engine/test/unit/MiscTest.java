package com.tuongky.flagfan.engine.test.unit;

import junit.framework.Assert;

import org.junit.Test;

import com.tuongky.utils.Misc;


public class MiscTest {

	@Test
	public void ff2sqr() {
		int actual = Misc.ff2sqr("e0");
		int expected = 199;
		Assert.assertEquals(expected, actual);
	}

	@Test
	public void sqr2ff() {
		String actual = Misc.sqr2ff(199);
		String expected = "e0";
		Assert.assertEquals(expected, actual);
	}

}
