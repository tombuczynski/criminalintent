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
        mCrimeLab = CrimeLab.getCrimeLab(null);
    }

    @Test
    public void getCrimeList() {

        assertNotNull(mCrimeLab.getCrimeList());
//        assertEquals("List size:", 100, crimeList.size());
        mCrimeLab.populateCrimeList(200);
        assertEquals("List size:", 200, mCrimeLab.getCrimeList().size());

        Crime cr = mCrimeLab.getCrimeList().get(49);
        assertEquals("Title:", "Sprawa #50", cr.getTitle());
        assertFalse("Expected that crime status = not solved", cr.isSolved());

        cr = mCrimeLab.getCrimeList().get(42);
        assertTrue("Expected that crime status = solved", cr.isSolved());
    }

    @Test
    public void getCrime() {
        assertNotNull(mCrimeLab.getCrimeList());

        Crime cr1 = mCrimeLab.getCrimeList().get(49);
        Crime cr2 = mCrimeLab.getCrime(cr1.getId());
        assertSame("Item Id " + cr1.getId() + ":", cr1, cr2);

        Crime cr3 = mCrimeLab.getCrimeList().get(48);
        assertNotEquals("The same ID of different items:", cr1.getId(), cr3.getId());

        Crime cr4 = mCrimeLab.getCrime(UUID.randomUUID());
        assertNull("Nonexistent UUID:", cr4);
    }
}