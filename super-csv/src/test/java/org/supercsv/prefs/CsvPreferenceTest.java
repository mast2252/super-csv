/*
 * Copyright 2007 Kasper B. Graversen
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.supercsv.prefs;

import org.junit.Assert;
import org.junit.Test;
import org.supercsv.encoder.DefaultCsvEncoder;
import org.supercsv.quote.AlwaysQuoteMode;
import org.supercsv.quote.NormalQuoteMode;

import static org.junit.Assert.*;
import static org.supercsv.prefs.CsvPreference.*;

/**
 * Tests the CsvPreference class.
 *
 * @author James Bassett
 */
public class CsvPreferenceTest {

	/**
	 * Tests the constant values.
	 */
	@Test
	public void testConstants() {
		assertEquals('"', STANDARD_PREFERENCE.getQuoteChar());
		assertEquals(",", STANDARD_PREFERENCE.getDelimiterSymbols());
		assertEquals("\r\n", STANDARD_PREFERENCE.getEndOfLineSymbols());

		assertEquals('"', EXCEL_PREFERENCE.getQuoteChar());
		assertEquals(",", EXCEL_PREFERENCE.getDelimiterSymbols());
		assertEquals("\n", EXCEL_PREFERENCE.getEndOfLineSymbols());

		assertEquals('"', EXCEL_NORTH_EUROPE_PREFERENCE.getQuoteChar());
		assertEquals(";", EXCEL_NORTH_EUROPE_PREFERENCE.getDelimiterSymbols());
		assertEquals("\n", EXCEL_NORTH_EUROPE_PREFERENCE.getEndOfLineSymbols());

		assertEquals('"', TAB_PREFERENCE.getQuoteChar());
		assertEquals("\t", TAB_PREFERENCE.getDelimiterSymbols());
		assertEquals("\n", TAB_PREFERENCE.getEndOfLineSymbols());
	}

	/**
	 * Tests a custom CsvPreference with default optional values.
	 */
	@Test
	public void testCustomPreferenceWithDefaults() {
		final CsvPreference custom = new Builder('"', ",", "\n").build();
		assertEquals('"', custom.getQuoteChar());
		assertEquals(",", custom.getDelimiterSymbols());
		assertEquals("\n", custom.getEndOfLineSymbols());
		assertFalse(custom.isSurroundingSpacesNeedQuotes());
		assertNull(custom.getCommentMatcher());
		assertTrue(custom.getEncoder() instanceof DefaultCsvEncoder);
		assertTrue(custom.getQuoteMode() instanceof NormalQuoteMode);
		assertEquals('"', custom.getQuoteEscapeChar());
	}

	/**
	 * Tests a custom CsvPreference with supplied optional values.
	 */
	@Test
	public void testCustomPreference() {
		final CsvPreference custom = new Builder('"', ",", "\n")
				.surroundingSpacesNeedQuotes(true)
				.useEncoder(new DefaultCsvEncoder())
				.useQuoteMode(new AlwaysQuoteMode())
				.setQuoteEscapeChar('\\')
				.build();
		assertEquals('"', custom.getQuoteChar());
		assertEquals(",", custom.getDelimiterSymbols());
		assertEquals("\n", custom.getEndOfLineSymbols());
		assertTrue(custom.isSurroundingSpacesNeedQuotes());
		assertTrue(custom.getEncoder() instanceof DefaultCsvEncoder);
		assertTrue(custom.getQuoteMode() instanceof AlwaysQuoteMode);
		assertEquals('\\', custom.getQuoteEscapeChar());
	}

	/**
	 * Tests a custom CsvPreference based on an existing constant with default optional values.
	 */
	@Test
	public void testCustomPreferenceBasedOnExistingWithDefaults() {
		final CsvPreference custom = new Builder(EXCEL_PREFERENCE).build();
		assertEquals(EXCEL_PREFERENCE.getQuoteChar(), custom.getQuoteChar());
		assertEquals(EXCEL_PREFERENCE.getDelimiterSymbols(), custom.getDelimiterSymbols());
		assertEquals(EXCEL_PREFERENCE.getEndOfLineSymbols(), custom.getEndOfLineSymbols());
		assertEquals(EXCEL_PREFERENCE.isSurroundingSpacesNeedQuotes(), custom.isSurroundingSpacesNeedQuotes());
		assertEquals(EXCEL_PREFERENCE.getEncoder(), custom.getEncoder());
		assertEquals(EXCEL_PREFERENCE.getQuoteMode(), custom.getQuoteMode());
		assertEquals(EXCEL_PREFERENCE.getQuoteEscapeChar(), custom.getQuoteEscapeChar());
	}

	/**
	 * Tests a custom CsvPreference based on an existing constant with supplied optional values.
	 */
	@Test
	public void testCustomPreferenceBasedOnExisting() {
		final CsvPreference custom = new Builder(EXCEL_PREFERENCE).surroundingSpacesNeedQuotes(true)
			.useEncoder(new DefaultCsvEncoder()).useQuoteMode(new AlwaysQuoteMode()).build();
		assertEquals(EXCEL_PREFERENCE.getQuoteChar(), custom.getQuoteChar());
		assertEquals(EXCEL_PREFERENCE.getDelimiterSymbols(), custom.getDelimiterSymbols());
		assertEquals(EXCEL_PREFERENCE.getEndOfLineSymbols(), custom.getEndOfLineSymbols());
		assertTrue(custom.isSurroundingSpacesNeedQuotes());
		assertTrue(custom.getEncoder() instanceof DefaultCsvEncoder);
		assertTrue(custom.getQuoteMode() instanceof AlwaysQuoteMode);
		assertEquals(EXCEL_PREFERENCE.getQuoteEscapeChar(), custom.getQuoteEscapeChar());
	}

	/**
	 * Tests a custom CsvPreference with identical quote and delimiter chars (should throw an exception).
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testCustomPreferenceWithInvalidQuoteAndDelimeterChars() {
		new Builder('|', "|", "\n").build();
	}

	@Test
	public void testCustomPreferenceWithSameDelimiterAndQuoteEscapeChar() {
		Builder builder = new Builder('"', ",", "\n").setQuoteEscapeChar(',');

		try {
			builder.build();
			Assert.fail();
		} catch (IllegalArgumentException e) {
			assertTrue(e.getMessage().contains("must not be the same character"));
		}
	}

	/**
	 * Tests construction with null end of line symbols (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testConstructorWithNullEolSymbols() {
		new Builder('"', ",", null).build();
	}

	/**
	 * Tests construction with null end of line symbols (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testSkipCommentsWithNullCommentMatcher() {
		new Builder(EXCEL_PREFERENCE).skipComments(null).build();
	}

	/**
	 * Tests construction with null encoder (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testUseEncoderWithNull() {
		new Builder(EXCEL_PREFERENCE).useEncoder(null).build();
	}

	/**
	 * Tests construction with null quote mode (should throw an exception).
	 */
	@Test(expected = NullPointerException.class)
	public void testUseQuoteModeWithNull() {
		new Builder(EXCEL_PREFERENCE).useQuoteMode(null).build();
	}
}
