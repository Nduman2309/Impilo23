package com.example.impilo23.util

/**
 * Automatic Image Resolver matching crop names (Tomatoes, Spinach, Carrots, Roses, Mint, Maize, Peppers, Lavender, Herbs, etc.)
 * to high-quality online web picture CDN URLs.
 */
object CropImageResolver {

    private val cropImageMap = mapOf(
        "tomato" to "https://images.unsplash.com/photo-1592841200221-a6898f307baa?auto=format&fit=crop&w=300&q=80",
        "tomatoes" to "https://images.unsplash.com/photo-1592841200221-a6898f307baa?auto=format&fit=crop&w=300&q=80",
        "basil" to "https://images.unsplash.com/photo-1618160702438-9b02ab6515c9?auto=format&fit=crop&w=300&q=80",
        "spinach" to "https://images.unsplash.com/photo-1576045057995-568f588f82fb?auto=format&fit=crop&w=300&q=80",
        "carrot" to "https://images.unsplash.com/photo-1598170845058-12ef4a45753b?auto=format&fit=crop&w=300&q=80",
        "carrots" to "https://images.unsplash.com/photo-1598170845058-12ef4a45753b?auto=format&fit=crop&w=300&q=80",
        "rosemary" to "https://images.unsplash.com/photo-1515586000433-45406d8e6662?auto=format&fit=crop&w=300&q=80",
        "mint" to "https://images.unsplash.com/photo-1628556270448-4d4e4148e1b1?auto=format&fit=crop&w=300&q=80",
        "rose" to "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=300&q=80",
        "roses" to "https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=300&q=80",
        "pepper" to "https://images.unsplash.com/photo-1563565375-f3fdfdbefa83?auto=format&fit=crop&w=300&q=80",
        "peppers" to "https://images.unsplash.com/photo-1563565375-f3fdfdbefa83?auto=format&fit=crop&w=300&q=80",
        "maize" to "https://images.unsplash.com/photo-1551754655-cd27e38d2076?auto=format&fit=crop&w=300&q=80",
        "corn" to "https://images.unsplash.com/photo-1551754655-cd27e38d2076?auto=format&fit=crop&w=300&q=80",
        "lavender" to "https://images.unsplash.com/photo-1528183429752-a97d0bf99b5a?auto=format&fit=crop&w=300&q=80",
        "lettuce" to "https://images.unsplash.com/photo-1622206151226-18ca2c9ab4a1?auto=format&fit=crop&w=300&q=80",
        "cabbage" to "https://images.unsplash.com/photo-1594282486552-05b4d80fbb9f?auto=format&fit=crop&w=300&q=80"
    )

    private const val DEFAULT_IMAGE_URL = "https://images.unsplash.com/photo-1585320806297-9794b3e4eeae?auto=format&fit=crop&w=300&q=80"

    fun getImageUrl(cropName: String): String {
        val normalized = cropName.trim().lowercase()
        for ((key, url) in cropImageMap) {
            if (normalized.contains(key)) {
                return url
            }
        }
        return DEFAULT_IMAGE_URL
    }

    fun getBotanicalName(cropName: String): String {
        val normalized = cropName.trim().lowercase()
        return when {
            normalized.contains("tomato") -> "Solanum lycopersicum"
            normalized.contains("basil") -> "Ocimum basilicum"
            normalized.contains("spinach") -> "Spinacia oleracea"
            normalized.contains("carrot") -> "Daucus carota subsp. sativus"
            normalized.contains("rosemary") -> "Salvia rosmarinus"
            normalized.contains("mint") -> "Mentha spicata"
            normalized.contains("rose") -> "Rosa rubiginosa"
            normalized.contains("pepper") -> "Capsicum annuum"
            normalized.contains("maize") || normalized.contains("corn") -> "Zea mays"
            normalized.contains("lavender") -> "Lavandula angustifolia"
            else -> "Plantae Green Sanctum"
        }
    }
}
