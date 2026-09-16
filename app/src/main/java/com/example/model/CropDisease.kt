package com.example.model

enum class Severity(val label: String) {
    LOW("LOW RISK"),
    MEDIUM("MEDIUM RISK"),
    HIGH("HIGH RISK")
}

data class Crop(
    val id: String,
    val name: String,
    val icon: String
)

data class Disease(
    val id: String,
    val name: String,
    val crops: List<String>,
    val symptoms: List<String>,
    val severity: Severity,
    val treatment: List<String>,
    val prevention: List<String>
)

data class AnalysisResult(
    val disease: Disease,
    val matchCount: Int,
    val confidence: Int
)

object AgroSenseData {
    val crops = listOf(
        Crop(id = "tomato", name = "Tomato", icon = "🍅"),
        Crop(id = "rice", name = "Rice", icon = "🌾"),
        Crop(id = "wheat", name = "Wheat", icon = "🌾"),
        Crop(id = "cotton", name = "Cotton", icon = "☁️"),
        Crop(id = "potato", name = "Potato", icon = "🥔"),
        Crop(id = "chilli", name = "Chilli", icon = "🌶️"),
        Crop(id = "sugarcane", name = "Sugarcane", icon = "🎋")
    )

    val symptoms = listOf(
        "Yellow leaves",
        "Brown spots",
        "Leaf curling",
        "Wilting",
        "White powdery coating",
        "Black spots",
        "Holes in leaves",
        "Stunted growth",
        "Rotting stem",
        "Rust colored patches"
    )

    val diseases = listOf(
        Disease(
            id = "early_blight",
            name = "Early Blight",
            crops = listOf("tomato", "potato"),
            symptoms = listOf("Brown spots", "Yellow leaves", "Wilting"),
            severity = Severity.MEDIUM,
            treatment = listOf(
                "Remove and destroy infected leaves",
                "Apply copper-based fungicide",
                "Avoid overhead watering"
            ),
            prevention = listOf(
                "Rotate crops every season",
                "Ensure proper plant spacing for airflow",
                "Use disease-resistant varieties"
            )
        ),
        Disease(
            id = "powdery_mildew",
            name = "Powdery Mildew",
            crops = listOf("tomato", "chilli", "cotton"),
            symptoms = listOf("White powdery coating", "Leaf curling", "Stunted growth"),
            severity = Severity.LOW,
            treatment = listOf(
                "Spray sulfur-based fungicide",
                "Improve air circulation",
                "Remove heavily infected leaves"
            ),
            prevention = listOf(
                "Avoid excess nitrogen fertilizer",
                "Water at the base, not on leaves",
                "Plant in sunny, well-ventilated areas"
            )
        ),
        Disease(
            id = "bacterial_leaf_blight",
            name = "Bacterial Leaf Blight",
            crops = listOf("rice"),
            symptoms = listOf("Yellow leaves", "Wilting", "Brown spots"),
            severity = Severity.HIGH,
            treatment = listOf(
                "Drain excess water from field",
                "Apply copper oxychloride spray",
                "Remove infected plants immediately"
            ),
            prevention = listOf(
                "Use certified disease-free seeds",
                "Avoid excess nitrogen",
                "Maintain proper field drainage"
            )
        ),
        Disease(
            id = "rust_disease",
            name = "Rust Disease",
            crops = listOf("wheat", "sugarcane"),
            symptoms = listOf("Rust colored patches", "Yellow leaves", "Stunted growth"),
            severity = Severity.HIGH,
            treatment = listOf(
                "Apply triazole fungicide",
                "Remove volunteer plants that host rust",
                "Increase potassium fertilization"
            ),
            prevention = listOf(
                "Grow rust-resistant varieties",
                "Time sowing to avoid peak rust season",
                "Monitor fields weekly"
            )
        ),
        Disease(
            id = "leaf_curl_virus",
            name = "Leaf Curl Virus",
            crops = listOf("chilli", "tomato", "cotton"),
            symptoms = listOf("Leaf curling", "Stunted growth", "Yellow leaves"),
            severity = Severity.HIGH,
            treatment = listOf(
                "Remove and burn infected plants",
                "Control whitefly population with neem oil",
                "Use yellow sticky traps"
            ),
            prevention = listOf(
                "Use virus-resistant seed varieties",
                "Cover nursery with insect-proof net",
                "Avoid planting near infected fields"
            )
        ),
        Disease(
            id = "root_stem_rot",
            name = "Root/Stem Rot",
            crops = listOf("potato", "sugarcane", "cotton"),
            symptoms = listOf("Rotting stem", "Wilting", "Stunted growth"),
            severity = Severity.HIGH,
            treatment = listOf(
                "Improve field drainage immediately",
                "Apply Trichoderma-based bio-fungicide",
                "Remove and destroy rotted plants"
            ),
            prevention = listOf(
                "Avoid waterlogging",
                "Treat seeds with fungicide before sowing",
                "Practice crop rotation"
            )
        ),
        Disease(
            id = "leaf_spot_disease",
            name = "Leaf Spot Disease",
            crops = listOf("rice", "wheat", "cotton", "sugarcane"),
            symptoms = listOf("Black spots", "Holes in leaves", "Brown spots"),
            severity = Severity.MEDIUM,
            treatment = listOf(
                "Apply mancozeb fungicide spray",
                "Remove affected leaves",
                "Ensure balanced fertilization"
            ),
            prevention = listOf(
                "Avoid dense planting",
                "Use clean, certified seeds",
                "Rotate with non-host crops"
            )
        )
    )

    fun analyze(cropId: String, selectedSymptoms: Set<String>): List<AnalysisResult> {
        if (selectedSymptoms.isEmpty()) return emptyList()

        return diseases
            .filter { it.crops.contains(cropId) }
            .mapNotNull { disease ->
                val matchedCount = disease.symptoms.count { selectedSymptoms.contains(it) }
                if (matchedCount > 0) {
                    val confidence = ((matchedCount.toFloat() / disease.symptoms.size.toFloat()) * 100).toInt()
                    AnalysisResult(
                        disease = disease,
                        matchCount = matchedCount,
                        confidence = confidence
                    )
                } else {
                    null
                }
            }
            .sortedByDescending { it.confidence }
            .take(3)
    }
}
