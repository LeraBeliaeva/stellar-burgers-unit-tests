package praktikum;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

@RunWith(Parameterized.class)
public class BurgerMovePositiveTest {

    private static final int INGREDIENTS_COUNT = 3;

    private final String scenarioName;
    private final int fromIndex;
    private final int toIndex;
    private final int[] expectedOrder;

    @Mock
    private Ingredient firstIngredient;

    @Mock
    private Ingredient secondIngredient;

    @Mock
    private Ingredient thirdIngredient;

    private Burger burger;
    private Ingredient[] allIngredients;

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    public BurgerMovePositiveTest(String scenarioName, int fromIndex, int toIndex, int[] expectedOrder) {
        this.scenarioName = scenarioName;
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
        this.expectedOrder = expectedOrder;
    }

    @Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Move first to last", 0, 2, new int[]{1, 2, 0}},
                {"Move last to first", 2, 0, new int[]{2, 0, 1}},
                {"Move middle to first", 1, 0, new int[]{1, 0, 2}},
                {"Move first to middle", 0, 1, new int[]{1, 0, 2}},
                {"Move last to middle", 2, 1, new int[]{0, 2, 1}},
                {"No move - same index", 0, 0, new int[]{0, 1, 2}},
                {"No move - same index (last)", 2, 2, new int[]{0, 1, 2}}
        });
    }

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        burger = new Burger();
        allIngredients = new Ingredient[]{firstIngredient, secondIngredient, thirdIngredient};

        for (Ingredient ingredient : allIngredients) {
            burger.addIngredient(ingredient);
        }
    }

    @Test
    public void moveIngredientShouldChangeOrder() {
        burger.moveIngredient(fromIndex, toIndex);

        List<Ingredient> result = burger.getIngredients();

        errorCollector.checkThat(
                "Size should stay the same after moving",
                result.size(),
                is(INGREDIENTS_COUNT)
        );

        errorCollector.checkThat(
                "Result size should match expected order",
                result.size(),
                is(expectedOrder.length)
        );

        for (int i = 0; i < expectedOrder.length; i++) {
            errorCollector.checkThat(
                    "Position " + i + " should contain ingredient from original index " + expectedOrder[i],
                    result.get(i),
                    is(allIngredients[expectedOrder[i]])
            );
        }
    }

    @Test
    public void moveIngredientShouldKeepSizeAndPosition() {
        burger.moveIngredient(fromIndex, toIndex);

        List<Ingredient> result = burger.getIngredients();
        Ingredient movedIngredient = allIngredients[fromIndex];

        int occurrences = 0;
        int actualPosition = -1;

        for (int i = 0; i < result.size(); i++) {
            if (result.get(i) == movedIngredient) {
                occurrences++;
                actualPosition = i;
            }
        }

        errorCollector.checkThat(
                "Moved ingredient should appear exactly once",
                occurrences,
                is(1)
        );
        errorCollector.checkThat(
                "Moved ingredient should be at position " + toIndex,
                actualPosition,
                is(toIndex)
        );
    }
}