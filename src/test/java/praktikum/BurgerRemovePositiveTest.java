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
public class BurgerRemovePositiveTest {

    private final String scenarioName;
    private final int removeIndex;
    private final int[] expectedIndices;

    @Mock
    private Ingredient firstIngredient;

    @Mock
    private Ingredient secondIngredient;

    @Mock
    private Ingredient thirdIngredient;

    private Burger burger;

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    public BurgerRemovePositiveTest(String scenarioName, int removeIndex, int[] expectedIndices) {
        this.scenarioName = scenarioName;
        this.removeIndex = removeIndex;
        this.expectedIndices = expectedIndices;
    }

    @Parameters(name = "{0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"Remove first ingredient", 0, new int[]{1, 2}},
                {"Remove middle ingredient", 1, new int[]{0, 2}},
                {"Remove last ingredient", 2, new int[]{0, 1}}
        });
    }

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        burger = new Burger();
        burger.addIngredient(firstIngredient);
        burger.addIngredient(secondIngredient);
        burger.addIngredient(thirdIngredient);
    }

    @Test
    public void removeIngredientShouldDecreaseSizeAndRemoveCorrectIngredient() {
        Ingredient[] allIngredients = {firstIngredient, secondIngredient, thirdIngredient};

        burger.removeIngredient(removeIndex);

        List<Ingredient> result = burger.getIngredients();

        errorCollector.checkThat(
                "Size should decrease after removal",
                result.size(),
                is(expectedIndices.length)
        );

        for (int i = 0; i < expectedIndices.length; i++) {
            errorCollector.checkThat(
                    "Position " + i + " should contain ingredient at original index " + expectedIndices[i],
                    result.get(i),
                    is(allIngredients[expectedIndices[i]])
            );
        }
    }
}