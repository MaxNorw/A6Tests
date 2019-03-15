/* JUnit Test for a6novice
 * Written by: Ethan Koch
 * March 15, 2017
 */
package a6test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import a6.*;
import a6.Nigiri.NigiriType;
import a6.Sashimi.SashimiType;

//The tests are self-explanatory so I did not comment on them.
public class A6NoviceTest {
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

	// Test out of range index exception
	private int outOfRange;

	// Initialize constants, colors, belts, and plates to be used
	@Before
	public void setUp() throws PlatePriceException,BeltPlateException {
		correctBeltSize = 11;
		zeroBeltSize = 0;
		negativeBeltSize = -2;
		outOfRange = 100;

		correctBelt = new BeltImpl(correctBeltSize);
		emptyBelt = new BeltImpl(correctBeltSize);

		roll_ingredients = new IngredientPortion[]{salmon,seaweed,tuna,rice};

		nigiri = new Nigiri(NigiriType.CRAB);
		sashimi = new Sashimi(SashimiType.EEL);
		roll = new Roll("CoolRoll",roll_ingredients);

		plate1 = new RedPlate(nigiri);
		plate2 = new GreenPlate(sashimi);
		plate3 = new BluePlate(roll);
		plate4 = new GoldPlate(roll, 6.0);
		emptyPlate = null;		

		correctBelt.setPlateAtPosition(plate1,2);
		correctBelt.setPlateAtPosition(plate2,4);
		correctBelt.setPlateAtPosition(plate3,6);
		correctBelt.setPlateAtPosition(plate4,8);
	}
	// Beginning of a6novice tests

	// The belt constructor should throw an exception in each case
	@Test(expected=IllegalArgumentException.class)
	public void testBeltConstructor() {
		zeroLengthBelt = new BeltImpl(zeroBeltSize);
		negativeLengthBelt = new BeltImpl(negativeBeltSize);
	}

	@Test
	public void testGetSize() {
		assertEquals(correctBeltSize, correctBelt.getSize());
		assertEquals(correctBeltSize, emptyBelt.getSize());
		assertEquals(1, new BeltImpl(1).getSize());
	}

	@Test
	public void testGetPlateAtPosition() {
		assertEquals(plate1, correctBelt.getPlateAtPosition(2));
		assertEquals(plate2, correctBelt.getPlateAtPosition(4));
		assertEquals(plate3, correctBelt.getPlateAtPosition(6));
		assertEquals(plate4, correctBelt.getPlateAtPosition(8));
	}

	@Test
	public void testSetPlateAtPosition() throws BeltPlateException {
		correctBelt.setPlateAtPosition(plate1,3);
		correctBelt.setPlateAtPosition(plate2,5);
		correctBelt.setPlateAtPosition(plate3,7);
		correctBelt.setPlateAtPosition(plate4,9);
	}
		
	// Should not be able to set an empty plate
	@Test(expected=IllegalArgumentException.class)
	public void testEmptySetPlateAtPosition() throws BeltPlateException {
		correctBelt.setPlateAtPosition(emptyPlate,1);
		correctBelt.setPlateAtPosition(null,11);		
	}

	// Cannot put a plate down where another already exists
	@Test(expected=BeltPlateException.class)
	public void testPlateExistsSetPlateAtPosition() throws BeltPlateException{
		correctBelt.setPlateAtPosition(plate1,2);
		correctBelt.setPlateAtPosition(plate2,4);
		correctBelt.setPlateAtPosition(plate3,6);
		correctBelt.setPlateAtPosition(plate4,8);	
	}

	@Test
	public void testClearPlateAtPosition() {
		correctBelt.clearPlateAtPosition(0);
		correctBelt.clearPlateAtPosition(1);
		correctBelt.clearPlateAtPosition(2);
		correctBelt.clearPlateAtPosition(3);
		correctBelt.clearPlateAtPosition(4);
		correctBelt.clearPlateAtPosition(5);
	}
	
	@Test
	public void testRemovePlateAtPosition() throws BeltPlateException {
		correctBelt.clearPlateAtPosition(6);
		correctBelt.clearPlateAtPosition(7);
		correctBelt.clearPlateAtPosition(8);
		correctBelt.clearPlateAtPosition(9);

		correctBelt.setPlateAtPosition(plate3,6);
		correctBelt.setPlateAtPosition(plate1,7);
		correctBelt.setPlateAtPosition(plate4,8);
		correctBelt.setPlateAtPosition(plate2,9);
		
		correctBelt.removePlateAtPosition(6);
		correctBelt.removePlateAtPosition(7);
		correctBelt.removePlateAtPosition(8);
		correctBelt.removePlateAtPosition(9);
	}
		
	// Cannot remove a plate that does not exist, expect a NoSuchElementException
	@Test(expected=RuntimeException.class)
	public void testNoElementRemovePlateAtPosition() {
		emptyBelt.removePlateAtPosition(1);
		emptyBelt.removePlateAtPosition(2);
		emptyBelt.removePlateAtPosition(3);
		emptyBelt.removePlateAtPosition(4);
	}
	// End of a6novice tests
}