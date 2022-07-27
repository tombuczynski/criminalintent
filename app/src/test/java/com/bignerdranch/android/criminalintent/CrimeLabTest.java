package com.bignerdranch.android.criminalintent;

import com.bignerdranch.android.criminalintent.data.Crime;
import com.bignerdranch.android.criminalintent.data.CrimeLab;

import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Created by Tom Buczynski on 19.02.2022.
 */
public class CrimeLabTest {

    private CrimeLab mCrimeLab;

    @Before
    public void setUp() {
        mCrimeLab = CrimeLab.getCrimeLab();
    }

    @Test
    public void getCrimeList() {

        List<Crime> crimeList = mCrimeLab.getCrimeList();
        assertNotNull(crimeList);
//        assertEquals("List size:", 100, crimeList.size());
        mCrimeLab.populateCrimeList(200);
        assertEquals("List size:", 200, crimeList.size());

        Crime cr = crimeList.get(49);
        assertEquals("Title:", "Sprawa nr 50", cr.getTitle());
        assertFalse("Expected that crime status = not solved", cr.isSolved());

        cr = crimeList.get(42);
        assertTrue("Expected that crime status = solved", cr.isSolved());
    }

    @Test
    public void getCrime() {
        List<Crime> crimeList = mCrimeLab.getCrimeList();
        assertNotNull(crimeList);

        Crime cr1 = crimeList.get(49);
        Crime cr2 = mCrimeLab.getCrime(cr1.getId());
        assertSame("Item Id " + cr1.getId() + ":", cr1, cr2);

        Crime cr3 = crimeList.get(48);
        assertNotEquals("The same ID of different items:", cr1.getId(), cr3.getId());

        Crime cr4 = mCrimeLab.getCrime(UUID.randomUUID());
        assertNull("Nonexistent UUID:", cr4);
    }
}