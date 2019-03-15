/* JUnit Test for a6jedi
 * Written by: Ethan Koch
 * March 15, 2017
 */
package a6test;

import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import a6.*;

// The tests are self-explanatory so I did not comment on them.
public class A6JediTest {
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

	// Used to test price threshold iterator
	private double max_price;

	// Will use these plates in various, early tests
	private Plate plate1;
	private Plate plate2;
	private Plate plate3;
	private Plate plate4;

	// Will use these plates for color filtered iterator
	private Plate redPlate;
	private Plate greenPlate;
	private Plate bluePlate;
	private Plate goldPlate;

	// Used to ensure you cannot set a null plate
	private Plate emptyPlate;

	// Used for price threshold iterator
	private Plate expensivePlate;
	private Plate cheapPlate;

	// Used for color filtered iterator
	private Plate.Color red;
	private Plate.Color blue;

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
		max_price = 10.0;

		red = Plate.Color.RED;
		blue = Plate.Color.BLUE;

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
		redPlate = new RedPlate(nigiri);
		greenPlate = new GreenPlate(sashimi);
		bluePlate = new BluePlate(roll);
		goldPlate = new GoldPlate(roll, 7.0);
		emptyPlate = null;
		expensivePlate = new GoldPlate(nigiri, 75.0);
		cheapPlate = new GoldPlate(nigiri, 6.0);
	}

	// Beginning of tests for a6jedi

	// We should now have functionality for remove
	@Test
	public void testRemove() throws BeltPlateException {
		for (int i = 0; i < correctBelt.getSize();i++) {
			correctBelt.clearPlateAtPosition(i);
		}
		correctBelt.setPlateAtPosition(plate1,1);
		correctBelt.setPlateAtPosition(plate2,2);
		correctBelt.setPlateAtPosition(plate3,3);
		correctBelt.setPlateAtPosition(plate4,4);

		beltIter = correctBelt.iterator();
		for (int i=0; i < 3; i++) {
			beltIter.next();
			beltIter.remove();
		}

		assertEquals(null,correctBelt.getPlateAtPosition(1));
		assertEquals(null,correctBelt.getPlateAtPosition(2));
		assertEquals(null,correctBelt.getPlateAtPosition(3));
		assertEquals(plate4,correctBelt.getPlateAtPosition(4));
	}

	@Test
	public void testPriceThresholdBeltIterator() throws BeltPlateException {
		for (int i = 0; i < correctBelt.getSize();i++) {
			correctBelt.clearPlateAtPosition(i);
		}
		correctBelt.setPlateAtPosition(expensivePlate, 10);
		correctBelt.setPlateAtPosition(cheapPlate, 5);

		beltIter = correctBelt.iteratorFromPosition(5, max_price);
		assertEquals(true,beltIter.hasNext());
		assertEquals(cheapPlate,beltIter.next());
		assertEquals(cheapPlate,beltIter.next());

		beltIter = correctBelt.iterator(max_price);
		assertEquals(true,beltIter.hasNext());
		assertEquals(cheapPlate,beltIter.next());
		assertEquals(cheapPlate,beltIter.next());
	}

	/* Ensure NoSuchElementException is thrown when
	 * there's no plate to iterate over (that meets price specifications)
	 */
	@Test(expected=RuntimeException.class)
	public void testNoNextElementPriceThresholdIterator() throws BeltPlateException {
		for (int i = 0; i < correctBelt.getSize();i++) {
			correctBelt.clearPlateAtPosition(i);
		}
		correctBelt.setPlateAtPosition(expensivePlate, 10);

		beltIter = correctBelt.iterator(max_price);
		beltIter.next();	

		beltIter = correctBelt.iteratorFromPosition(15,max_price);
		beltIter.next();

		emptyBeltIter = emptyBelt.iterator(max_price);
		emptyBeltIter.next();
	}

	@Test
	public void testColorFilteredBeltIterator() throws BeltPlateException {
		for (int i = 0; i < correctBelt.getSize();i++) {
			correctBelt.clearPlateAtPosition(i);
		}
		correctBelt.setPlateAtPosition(redPlate,1);
		correctBelt.setPlateAtPosition(bluePlate,3);
		correctBelt.setPlateAtPosition(greenPlate,5);
		correctBelt.setPlateAtPosition(goldPlate,7);
		correctBelt.setPlateAtPosition(bluePlate,9);

		beltIter = correctBelt.iteratorFromPosition(5,red);
		assertEquals(true,beltIter.hasNext());
		assertEquals(redPlate,beltIter.next());
		assertEquals(redPlate,beltIter.next());

		beltIter = correctBelt.iterator(blue);
		assertEquals(true,beltIter.hasNext());
		assertEquals(bluePlate,beltIter.next());
		assertEquals(bluePlate,beltIter.next());
	}

	/* Ensure NoSuchElementException is thrown when
	 * there's no plate to iterate over (that meets color specifications)
	 */
	@Test(expected=RuntimeException.class)
	public void testNoNextElementColorFilteredIterator() throws BeltPlateException {
		for (int i = 0; i < correctBelt.getSize();i++) {
			correctBelt.clearPlateAtPosition(i);
		}
		correctBelt.setPlateAtPosition(greenPlate,5);
		correctBelt.setPlateAtPosition(goldPlate,7);

		beltIter = correctBelt.iterator(red);
		beltIter.next();

		beltIter = correctBelt.iteratorFromPosition(15,blue);
		beltIter.next();

		emptyBeltIter = emptyBelt.iterator(red);
		emptyBeltIter.next();
	}
	// End of tests for a6jedi
}