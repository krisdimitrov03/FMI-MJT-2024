package bg.sofia.uni.fmi.mjt.cooking.http.response;

public record RecipeDTO(
    String label,
    String[] dietLabels,
    String[] healthLabels,
    double totalWeight,
    String[] cuisineType,
    String[] mealType,
    String[] dishType,
    String[] ingredientLines
) {
}