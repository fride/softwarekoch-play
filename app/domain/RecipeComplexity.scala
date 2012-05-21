package domain

/**
 *
 * Date: 17.05.12
 * Time: 20:30
 *
 * @author Friderici
 */

sealed trait RecipeComplexity

object Easy extends RecipeComplexity
object Normal extends RecipeComplexity
object Hard extends RecipeComplexity
