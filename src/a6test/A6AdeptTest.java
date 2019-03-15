/* JUnit Test for a6adept
 * Written by: Ethan Koch
 * March 15, 2017
 */
package a6test;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import a6.*;

//The most tests are self-explanatory so I did not comment on them.
public class A6AdeptTest {
	// Used many times as a valid belt
	private Belt correctBelt;
	private int correctBeltSize;

	// Used to as a belt with no plates
	private Belt emptyBelt;
	
	// Used to test Belt constructor exceptions
	private Belt zeroLengthBelt;
	private Belt negativeLengthBelt;
	private int zeroBeltSize;
	private int negativeBeltSize;

	// Used to test indices outside of size
	private int indexNegativeTen;
	private int indexNinetyFive;

	// Will use these plates in various tests
	private Plate plate1;
	private Plate plate2;
	private Plate plate3;
	private Plate plate4;
	
	// Used to ensure you cannot set a null plate
	private Plate emptyPlate;

	// Different types of sushi to put on plates
	private Sushi nigiri;
	private Sushi sashimi;
	private Sushi roll;

	// Roll ingredients
	private YellowtailPortion salmon = new YellowtailPortion(.75);
	private SeaweedPortion seaweed = new SeaweedPortion(.3);
	private TunaPortion tuna = new TunaPortion(1.2);
	private RicePortion rice = new RicePortion(.6);
	private IngredientPortion[] roll_ingredients;

	// Belt iterators to be used, one will always be used as an empty belt
	private Iterator<Plate> beltIter;
	private Iterator<Plate> emptyBeltIter;

	// Initialize constants, colors, belts, and plates to be used
	@Before
	public void setUp() throws PlatePriceException,BeltPlateException {
		correctBeltSize = 20;
		zeroBeltSize = 0;
		negativeBeltSize = -2;
		indexNegativeTen = -10;
		indexNinetyFive = 95;

		correctBelt = new BeltImpl(correctBeltSize);
		emptyBelt = new BeltImpl(correctBeltSize);

		roll_ingredients = new IngredientPortion[]{salmon,seaweed,tuna,rice};

		nigiri = new Nigiri(Nigiri.NigiriType.CRAB);
		sashimi = new Sashimi(Sashimi.SashimiType.EEL);
		roll = new Roll("CoolRoll",roll_ingredients);

		plate1 = new RedPlate(nigiri);
		plate2 = new GreenPlate(sashimi);
		plate3 = new BluePlate(roll);
		plate4 = new GoldPlate(roll, 6.0);
		emptyPlate = null;
	}

	// Beginning of tests for a6adept
	@Test
	public void testSetPlateNearestToPosition() throws BeltFullException,BeltPlateException {
		for (int i = 0; i < correctBelt.getSize();i++) {
			correctBelt.clearPlateAtPosition(i);
		}
		correctBelt.setPlateAtPosition(plate1,1);
		correctBelt.setPlateAtPosition(plate2,2);
		correctBelt.setPlateAtPosition(plate3,3);
		correctBelt.setPlateAtPosition(plate4,4);

		correctBelt.setPlateNearestToPosition(plate1,1);
		assertEquals(plate1,correctBelt.getPlateAtPosition(5));

		correctBelt.setPlateNearestToPosition(plate2,7);
		assertEquals(plate2,correctBelt.getPlateAtPosition(7));
	}

	// Cannot set a plate on a full belt
	@Test(expected=BeltFullException.class)
	public void testFullBeltSetPlateNearestToPosition() throws BeltFullException,BeltPlateException {
		// I often use this loop to ensure there are no plates I forgot to clear
		for (int i = 0; i < correctBelt.getSize();i++) {
			correctBelt.clearPlateAtPosition(i);
		}
		for (int i = 0; i < correctBelt.getSize();i++) {
			correctBelt.setPlateAtPosition(plate1,i);
		}

		correctBelt.setPlateNearestToPosition(plate2,0);
		correctBelt.setPlateNearestToPosition(plate2,80);
	}

	@Test
	public void testBeltIterator() throws BeltPlateException {
		// I often use this loop to ensure there are no plates I forgot to clear
		for (int i = 0; i < correctBelt.getSize();i++) {
			correctBelt.clearPlateAtPosition(i);
		}
		correctBelt.setPlateAtPosition(plate1,1);
		correctBelt.setPlateAtPosition(plate2,2);
		correctBelt.setPlateAtPosition(plate3,3);
		correctBelt.setPlateAtPosition(plate4,4);

		emptyBelt.iterator();
		emptyBelt.iteratorFromPosition(0);
		emptyBelt.iteratorFromPosition(10);
		emptyBelt.iteratorFromPosition(-10);
		emptyBelt.iteratorFromPosition(100);

		Iterator<Plate> from_default = correctBelt.iterator();
		Iterator<Plate> from_0 = correctBelt.iteratorFromPosition(0);
		Iterator<Plate> from_100 = correctBelt.iteratorFromPosition(100);
		Iterator<Plate> from_10 = correctBelt.iteratorFromPosition(10);
		Iterator<Plate> from_neg10 = correctBelt.iteratorFromPosition(-10);

		for (int i=0; i<4; i++) {
			assertEquals(from_0.next(), from_default.next());
		}
		for (int i=0; i<4; i++) {
			assertEquals(from_0.next(), from_100.next());
		}
		for (int i=0; i<4; i++) {
			assertEquals(from_10.next(), from_neg10.next());
		}
		
		Iterator<Plate> from_2 = correctBelt.iteratorFromPosition(2);
		// from_0 iterator should be about to server plate1 from position 1
		// calling next() should make it synched with from_2
		from_0.next();
		
		for (int i=0; i<4; i++) {
			assertEquals(from_0.next(), from_2.next());
		}
	}

	@Test
	public void testHasNext() throws BeltPlateException  {
		// I often use this loop to ensure there are no plates I forgot to clear
		for (int i = 0; i < correctBelt.getSize();i++) {
			correctBelt.clearPlateAtPosition(i);
		}
		correctBelt.setPlateAtPosition(plate1,1);
		correctBelt.setPlateAtPosition(plate2,2);
		correctBelt.setPlateAtPosition(plate3,3);
		correctBelt.setPlateAtPosition(plate4,4);

		beltIter = correctBelt.iterator();
		assertEquals(true, beltIter.hasNext());

		emptyBeltIter = emptyBelt.iterator();
		assertEquals(false, emptyBeltIter.hasNext());

		for (int i = 0; i < correctBelt.getSize();i++) {
			correctBelt.clearPlateAtPosition(i);
		}
		for (int i = 0; i < correctBelt.getSize();i++) {
			correctBelt.setPlateAtPosition(plate1,i);
		}
		beltIter = correctBelt.iterator();
		assertEquals(true, beltIter.hasNext());
	}
	
	@Test
	public void testNext() throws BeltPlateException {
		// I often use this loop to ensure there are no plates I forgot to clear
		for (int i = 0; i < correctBelt.getSize();i++) {
			correctBelt.clearPlateAtPosition(i);
		}
		correctBelt.setPlateAtPosition(plate1,1);
		correctBelt.setPlateAtPosition(plate2,2);
		correctBelt.setPlateAtPosition(plate3,3);
		correctBelt.setPlateAtPosition(plate4,4);
		
		beltIter = correctBelt.iterator();
		assertEquals(correctBelt.getPlateAtPosition(1),beltIter.next());
		assertEquals(correctBelt.getPlateAtPosition(2),beltIter.next());
		assertEquals(correctBelt.getPlateAtPosition(3),beltIter.next());
		assertEquals(correctBelt.getPlateAtPosition(4),beltIter.next());	
	}
	
	// Cannot go to next if there is no next item, should throw a NoSuchElementException
	@Test(expected=RuntimeException.class)
	public void testNoElementNext() {
		emptyBeltIter = emptyBelt.iterator();
		emptyBeltIter.next();
		
		emptyBeltIter = emptyBelt.iteratorFromPosition(1);
		emptyBeltIter.next();
		
		for (int i = 0; i < correctBelt.getSize();i++) {
			correctBelt.clearPlateAtPosition(i);
		}
		beltIter = correctBelt.iterator();
		beltIter.next();
	}
		
	@Test
	public void testRotate() throws BeltPlateException {
		for (int i = 0; i < correctBelt.getSize();i++) {
			correctBelt.clearPlateAtPosition(i);
		}
		correctBelt.setPlateAtPosition(plate1,1);
		correctBelt.setPlateAtPosition(plate2,-4);
		correctBelt.setPlateAtPosition(plate3,3);
		correctBelt.setPlateAtPosition(plate4,64);
		
		correctBelt.rotate();
		
		assertEquals(plate1,correctBelt.getPlateAtPosition(2));
		assertEquals(plate2,correctBelt.getPlateAtPosition(-3));
		assertEquals(plate3,correctBelt.getPlateAtPosition(4));
		assertEquals(plate4,correctBelt.getPlateAtPosition(65));
		
		emptyBelt.rotate();
	}
	// End of tests for a6adept
}