package com.example.data.local

import android.content.Context
import com.example.ml.VisualFeatureExtractor
import org.json.JSONArray

/**
 * Rich pre-learned library of 315 real-world objects across 7 categories.
 * Provides instant out-of-the-box recognition for common everyday items.
 */
object PrepopulatedLearnedObjects {

    data class SeedItem(
        val id: String,
        val name: String,
        val category: String,
        val r: Float,
        val g: Float,
        val b: Float,
        val h: Float,
        val s: Float,
        val v: Float,
        val tex: Float
    )

    private val SEED_ITEMS = listOf(
        SeedItem("airpods", "Apple AirPods", "Electronics", 0.96f, 0.96f, 0.96f, 0.0f, 0.03f, 0.98f, 0.15f),
        SeedItem("over_ear_headphones", "Over-Ear Headphones", "Electronics", 0.15f, 0.15f, 0.18f, 0.6f, 0.1f, 0.2f, 0.7f),
        SeedItem("wired_earphones", "In-Ear Wired Earphones", "Electronics", 0.85f, 0.85f, 0.88f, 0.6f, 0.05f, 0.9f, 0.45f),
        SeedItem("smartwatch", "Apple Watch / Smartwatch", "Electronics", 0.12f, 0.12f, 0.14f, 0.0f, 0.0f, 0.18f, 0.55f),
        SeedItem("fitness_tracker", "Fitness Tracker Band", "Electronics", 0.18f, 0.18f, 0.2f, 0.0f, 0.0f, 0.22f, 0.4f),
        SeedItem("mech_keyboard", "Mechanical Keyboard", "Electronics", 0.22f, 0.22f, 0.25f, 0.65f, 0.2f, 0.35f, 0.9f),
        SeedItem("membrane_keyboard", "Computer Keyboard", "Electronics", 0.7f, 0.7f, 0.72f, 0.0f, 0.02f, 0.75f, 0.85f),
        SeedItem("ergonomic_mouse", "Ergonomic Mouse", "Electronics", 0.18f, 0.18f, 0.2f, 0.0f, 0.0f, 0.22f, 0.45f),
        SeedItem("gaming_mouse", "Gaming Mouse", "Electronics", 0.15f, 0.15f, 0.22f, 0.7f, 0.3f, 0.3f, 0.6f),
        SeedItem("macbook", "MacBook / Laptop", "Electronics", 0.75f, 0.75f, 0.78f, 0.6f, 0.05f, 0.8f, 0.6f),
        SeedItem("gaming_laptop", "Gaming Laptop", "Electronics", 0.15f, 0.15f, 0.18f, 0.0f, 0.2f, 0.25f, 0.75f),
        SeedItem("pc_tower", "Desktop PC Tower", "Electronics", 0.12f, 0.12f, 0.15f, 0.65f, 0.2f, 0.2f, 0.8f),
        SeedItem("monitor", "Computer Monitor", "Electronics", 0.1f, 0.1f, 0.12f, 0.0f, 0.0f, 0.15f, 0.5f),
        SeedItem("iphone", "Smartphone / iPhone", "Electronics", 0.1f, 0.1f, 0.12f, 0.0f, 0.0f, 0.15f, 0.5f),
        SeedItem("android_phone", "Android Smartphone", "Electronics", 0.2f, 0.25f, 0.35f, 0.6f, 0.4f, 0.4f, 0.52f),
        SeedItem("tablet_ipad", "iPad / Tablet", "Electronics", 0.15f, 0.15f, 0.18f, 0.6f, 0.05f, 0.25f, 0.4f),
        SeedItem("kindle_ereader", "Kindle / E-Reader", "Electronics", 0.82f, 0.82f, 0.8f, 0.12f, 0.05f, 0.85f, 0.55f),
        SeedItem("game_controller", "Game Controller / Gamepad", "Electronics", 0.25f, 0.25f, 0.3f, 0.65f, 0.25f, 0.35f, 0.8f),
        SeedItem("nintendo_switch", "Nintendo Switch", "Electronics", 0.35f, 0.4f, 0.5f, 0.55f, 0.6f, 0.6f, 0.75f),
        SeedItem("handheld_console", "Handheld Gaming Console", "Electronics", 0.2f, 0.2f, 0.22f, 0.0f, 0.0f, 0.25f, 0.7f),
        SeedItem("vr_headset", "VR Headset", "Electronics", 0.9f, 0.9f, 0.92f, 0.0f, 0.02f, 0.95f, 0.65f),
        SeedItem("tv_remote", "TV Remote Control", "Electronics", 0.14f, 0.14f, 0.16f, 0.0f, 0.0f, 0.18f, 0.75f),
        SeedItem("streaming_stick", "Streaming TV Stick", "Electronics", 0.12f, 0.12f, 0.12f, 0.0f, 0.0f, 0.15f, 0.35f),
        SeedItem("dslr_camera", "DSLR Camera", "Electronics", 0.12f, 0.12f, 0.12f, 0.0f, 0.0f, 0.15f, 0.85f),
        SeedItem("action_camera", "Action Camera / GoPro", "Electronics", 0.2f, 0.2f, 0.22f, 0.6f, 0.1f, 0.25f, 0.6f),
        SeedItem("camera_lens", "Camera Lens", "Electronics", 0.1f, 0.1f, 0.12f, 0.0f, 0.0f, 0.15f, 0.8f),
        SeedItem("tripod", "Camera Tripod", "Electronics", 0.25f, 0.25f, 0.28f, 0.0f, 0.0f, 0.3f, 0.7f),
        SeedItem("smart_speaker", "Smart Speaker / Echo", "Electronics", 0.35f, 0.35f, 0.38f, 0.6f, 0.1f, 0.4f, 0.65f),
        SeedItem("bluetooth_speaker", "Portable Bluetooth Speaker", "Electronics", 0.2f, 0.45f, 0.7f, 0.58f, 0.7f, 0.75f, 0.6f),
        SeedItem("soundbar", "Audio Soundbar", "Electronics", 0.15f, 0.15f, 0.16f, 0.0f, 0.0f, 0.18f, 0.55f),
        SeedItem("power_bank", "Power Bank / Battery Pack", "Electronics", 0.25f, 0.25f, 0.28f, 0.0f, 0.0f, 0.3f, 0.3f),
        SeedItem("wall_charger", "Wall Power Adapter", "Electronics", 0.92f, 0.92f, 0.92f, 0.0f, 0.0f, 0.95f, 0.25f),
        SeedItem("usbc_cable", "USB-C Cable", "Electronics", 0.88f, 0.88f, 0.88f, 0.0f, 0.0f, 0.9f, 0.5f),
        SeedItem("lightning_cable", "Lightning Charging Cable", "Electronics", 0.92f, 0.92f, 0.92f, 0.0f, 0.0f, 0.95f, 0.45f),
        SeedItem("hdmi_cable", "HDMI Cable", "Electronics", 0.15f, 0.15f, 0.18f, 0.6f, 0.15f, 0.2f, 0.55f),
        SeedItem("flash_drive", "USB Flash Drive", "Electronics", 0.6f, 0.6f, 0.65f, 0.6f, 0.1f, 0.7f, 0.4f),
        SeedItem("external_hdd", "External Hard Drive", "Electronics", 0.25f, 0.28f, 0.32f, 0.6f, 0.2f, 0.35f, 0.45f),
        SeedItem("microsd_card", "MicroSD Memory Card", "Electronics", 0.8f, 0.2f, 0.15f, 0.02f, 0.8f, 0.85f, 0.6f),
        SeedItem("wifi_router", "Wi-Fi Router", "Electronics", 0.18f, 0.18f, 0.2f, 0.0f, 0.0f, 0.22f, 0.65f),
        SeedItem("webcam", "Computer Webcam", "Electronics", 0.15f, 0.15f, 0.18f, 0.0f, 0.0f, 0.2f, 0.55f),
        SeedItem("usb_mic", "USB Desktop Microphone", "Electronics", 0.3f, 0.3f, 0.35f, 0.65f, 0.15f, 0.4f, 0.75f),
        SeedItem("studio_mic", "Studio XLR Microphone", "Electronics", 0.7f, 0.7f, 0.72f, 0.0f, 0.0f, 0.75f, 0.8f),
        SeedItem("audio_interface", "Audio Interface / Mixer", "Electronics", 0.8f, 0.15f, 0.15f, 0.0f, 0.8f, 0.85f, 0.75f),
        SeedItem("drone", "Drone / Quadcopter", "Electronics", 0.8f, 0.8f, 0.82f, 0.6f, 0.05f, 0.85f, 0.85f),
        SeedItem("calculator", "Pocket Calculator", "Electronics", 0.25f, 0.25f, 0.28f, 0.0f, 0.0f, 0.3f, 0.7f),
        SeedItem("alarm_clock", "Digital Alarm Clock", "Electronics", 0.15f, 0.15f, 0.18f, 0.33f, 0.3f, 0.4f, 0.65f),
        SeedItem("power_strip", "Surge Protector Power Strip", "Electronics", 0.9f, 0.9f, 0.9f, 0.0f, 0.0f, 0.95f, 0.6f),
        SeedItem("wireless_charger", "Wireless Charging Pad", "Electronics", 0.2f, 0.2f, 0.22f, 0.0f, 0.0f, 0.25f, 0.3f),
        SeedItem("drone_remote", "Drone Remote Controller", "Electronics", 0.3f, 0.3f, 0.35f, 0.6f, 0.15f, 0.4f, 0.8f),
        SeedItem("walkie_talkie", "Walkie Talkie", "Electronics", 0.85f, 0.7f, 0.15f, 0.13f, 0.8f, 0.88f, 0.7f),
        SeedItem("coffee_mug", "Coffee Mug", "Household", 0.85f, 0.8f, 0.75f, 0.1f, 0.15f, 0.88f, 0.35f),
        SeedItem("tea_cup", "Tea Cup & Saucer", "Household", 0.95f, 0.92f, 0.9f, 0.1f, 0.05f, 0.96f, 0.3f),
        SeedItem("stanley_cup", "Stanley Cup / Tumbler", "Household", 0.45f, 0.65f, 0.6f, 0.45f, 0.35f, 0.68f, 0.4f),
        SeedItem("water_bottle", "Water Bottle", "Household", 0.3f, 0.5f, 0.75f, 0.58f, 0.55f, 0.8f, 0.45f),
        SeedItem("plastic_bottle", "Disposable Water Bottle", "Household", 0.75f, 0.85f, 0.9f, 0.55f, 0.15f, 0.92f, 0.35f),
        SeedItem("glass_tumbler", "Glass Drinking Tumbler", "Household", 0.85f, 0.9f, 0.92f, 0.55f, 0.08f, 0.95f, 0.25f),
        SeedItem("wine_glass", "Wine Glass", "Household", 0.9f, 0.92f, 0.95f, 0.58f, 0.05f, 0.96f, 0.4f),
        SeedItem("beer_mug", "Beer Glass Mug", "Household", 0.88f, 0.75f, 0.3f, 0.12f, 0.65f, 0.9f, 0.35f),
        SeedItem("dinner_plate", "Ceramic Dinner Plate", "Household", 0.92f, 0.92f, 0.92f, 0.0f, 0.0f, 0.95f, 0.2f),
        SeedItem("salad_bowl", "Salad Bowl", "Household", 0.65f, 0.45f, 0.25f, 0.08f, 0.6f, 0.68f, 0.35f),
        SeedItem("soup_bowl", "Soup Bowl", "Household", 0.9f, 0.88f, 0.85f, 0.1f, 0.06f, 0.92f, 0.25f),
        SeedItem("serving_platter", "Serving Platter Tray", "Household", 0.8f, 0.8f, 0.82f, 0.0f, 0.0f, 0.85f, 0.3f),
        SeedItem("fork", "Dinner Fork", "Household", 0.8f, 0.8f, 0.82f, 0.0f, 0.0f, 0.85f, 0.7f),
        SeedItem("spoon", "Soup Spoon", "Household", 0.82f, 0.82f, 0.84f, 0.0f, 0.0f, 0.86f, 0.5f),
        SeedItem("table_knife", "Butter Knife", "Household", 0.8f, 0.8f, 0.82f, 0.0f, 0.0f, 0.85f, 0.6f),
        SeedItem("chopsticks", "Wooden Chopsticks", "Household", 0.7f, 0.5f, 0.3f, 0.08f, 0.55f, 0.72f, 0.4f),
        SeedItem("chef_knife", "Chef Kitchen Knife", "Household", 0.75f, 0.75f, 0.78f, 0.0f, 0.0f, 0.8f, 0.65f),
        SeedItem("bread_knife", "Serrated Bread Knife", "Household", 0.78f, 0.78f, 0.8f, 0.0f, 0.0f, 0.82f, 0.7f),
        SeedItem("cutting_board", "Wooden Cutting Board", "Household", 0.75f, 0.55f, 0.35f, 0.08f, 0.5f, 0.78f, 0.4f),
        SeedItem("frying_pan", "Frying Pan / Skillet", "Household", 0.2f, 0.2f, 0.22f, 0.0f, 0.0f, 0.25f, 0.45f),
        SeedItem("saucepan", "Cooking Saucepan / Pot", "Household", 0.75f, 0.75f, 0.78f, 0.0f, 0.0f, 0.8f, 0.5f),
        SeedItem("dutch_oven", "Enamelled Dutch Oven", "Household", 0.8f, 0.2f, 0.2f, 0.0f, 0.75f, 0.85f, 0.4f),
        SeedItem("baking_sheet", "Metal Baking Sheet", "Household", 0.6f, 0.6f, 0.62f, 0.0f, 0.0f, 0.65f, 0.5f),
        SeedItem("muffin_pan", "Muffin Baking Tray", "Household", 0.4f, 0.4f, 0.42f, 0.0f, 0.0f, 0.45f, 0.65f),
        SeedItem("spatula", "Cooking Spatula", "Household", 0.2f, 0.2f, 0.22f, 0.0f, 0.0f, 0.25f, 0.5f),
        SeedItem("whisk", "Wire Kitchen Whisk", "Household", 0.78f, 0.78f, 0.8f, 0.0f, 0.0f, 0.82f, 0.8f),
        SeedItem("kitchen_tongs", "Cooking Tongs", "Household", 0.75f, 0.75f, 0.78f, 0.0f, 0.0f, 0.8f, 0.65f),
        SeedItem("ladle", "Soup Ladle", "Household", 0.75f, 0.75f, 0.78f, 0.0f, 0.0f, 0.8f, 0.55f),
        SeedItem("peeler", "Vegetable Peeler", "Household", 0.3f, 0.7f, 0.35f, 0.35f, 0.6f, 0.72f, 0.6f),
        SeedItem("can_opener", "Can Opener", "Household", 0.65f, 0.65f, 0.68f, 0.0f, 0.0f, 0.7f, 0.75f),
        SeedItem("corkscrew", "Wine Bottle Corkscrew", "Household", 0.7f, 0.7f, 0.72f, 0.0f, 0.0f, 0.75f, 0.7f),
        SeedItem("blender", "Kitchen Blender", "Household", 0.8f, 0.85f, 0.9f, 0.55f, 0.15f, 0.92f, 0.6f),
        SeedItem("food_processor", "Food Processor", "Household", 0.85f, 0.85f, 0.88f, 0.0f, 0.05f, 0.9f, 0.65f),
        SeedItem("electric_kettle", "Electric Kettle", "Household", 0.8f, 0.8f, 0.82f, 0.0f, 0.0f, 0.85f, 0.5f),
        SeedItem("toaster", "Bread Toaster", "Household", 0.78f, 0.78f, 0.8f, 0.0f, 0.0f, 0.82f, 0.6f),
        SeedItem("microwave", "Microwave Oven", "Household", 0.88f, 0.88f, 0.88f, 0.0f, 0.0f, 0.92f, 0.65f),
        SeedItem("air_fryer", "Air Fryer", "Household", 0.15f, 0.15f, 0.18f, 0.0f, 0.0f, 0.2f, 0.55f),
        SeedItem("espresso_machine", "Espresso Coffee Machine", "Household", 0.75f, 0.75f, 0.78f, 0.0f, 0.0f, 0.8f, 0.75f),
        SeedItem("french_press", "French Press Coffee Maker", "Household", 0.5f, 0.4f, 0.3f, 0.08f, 0.4f, 0.55f, 0.6f),
        SeedItem("drip_coffee", "Drip Coffee Maker", "Household", 0.2f, 0.2f, 0.22f, 0.0f, 0.0f, 0.25f, 0.65f),
        SeedItem("paper_towels", "Paper Towel Roll", "Household", 0.95f, 0.95f, 0.95f, 0.0f, 0.0f, 0.98f, 0.35f),
        SeedItem("dish_soap", "Dish Soap Bottle", "Household", 0.2f, 0.6f, 0.85f, 0.56f, 0.75f, 0.88f, 0.45f),
        SeedItem("kitchen_sponge", "Kitchen Sponge", "Household", 0.9f, 0.85f, 0.2f, 0.15f, 0.8f, 0.92f, 0.5f),
        SeedItem("salt_shaker", "Salt Shaker", "Household", 0.92f, 0.92f, 0.92f, 0.0f, 0.0f, 0.95f, 0.35f),
        SeedItem("pepper_grinder", "Pepper Grinder Mill", "Household", 0.45f, 0.3f, 0.18f, 0.07f, 0.6f, 0.48f, 0.55f),
        SeedItem("olive_oil", "Olive Oil Bottle", "Food", 0.65f, 0.65f, 0.2f, 0.17f, 0.7f, 0.7f, 0.4f),
        SeedItem("honey_jar", "Honey Jar", "Food", 0.85f, 0.6f, 0.15f, 0.11f, 0.82f, 0.88f, 0.35f),
        SeedItem("peanut_butter", "Peanut Butter Jar", "Food", 0.75f, 0.5f, 0.25f, 0.08f, 0.65f, 0.78f, 0.45f),
        SeedItem("jam_jar", "Strawberry Jam Jar", "Food", 0.75f, 0.15f, 0.2f, 0.98f, 0.8f, 0.78f, 0.4f),
        SeedItem("cereal_box", "Cereal Box", "Food", 0.85f, 0.45f, 0.15f, 0.07f, 0.8f, 0.88f, 0.75f),
        SeedItem("milk_carton", "Milk Carton Bottle", "Food", 0.92f, 0.92f, 0.95f, 0.58f, 0.05f, 0.96f, 0.45f),
        SeedItem("soda_can", "Soda Can", "Food", 0.85f, 0.15f, 0.15f, 0.0f, 0.85f, 0.88f, 0.65f),
        SeedItem("beer_bottle", "Beer Glass Bottle", "Food", 0.4f, 0.3f, 0.15f, 0.09f, 0.62f, 0.45f, 0.45f),
        SeedItem("juice_box", "Fruit Juice Box", "Food", 0.9f, 0.6f, 0.15f, 0.1f, 0.82f, 0.92f, 0.65f),
        SeedItem("energy_drink", "Energy Drink Can", "Food", 0.2f, 0.75f, 0.85f, 0.52f, 0.75f, 0.88f, 0.7f),
        SeedItem("red_apple", "Red Apple", "Food", 0.85f, 0.12f, 0.15f, 0.98f, 0.85f, 0.88f, 0.25f),
        SeedItem("green_apple", "Green Apple", "Food", 0.45f, 0.8f, 0.2f, 0.28f, 0.75f, 0.82f, 0.25f),
        SeedItem("banana", "Yellow Banana", "Food", 0.92f, 0.85f, 0.18f, 0.15f, 0.8f, 0.95f, 0.3f),
        SeedItem("orange_fruit", "Fresh Orange", "Food", 0.95f, 0.55f, 0.1f, 0.08f, 0.88f, 0.95f, 0.3f),
        SeedItem("lemon", "Yellow Lemon", "Food", 0.95f, 0.9f, 0.15f, 0.16f, 0.85f, 0.96f, 0.3f),
        SeedItem("lime", "Green Lime", "Food", 0.35f, 0.75f, 0.2f, 0.29f, 0.75f, 0.78f, 0.3f),
        SeedItem("strawberry", "Fresh Strawberry", "Food", 0.88f, 0.15f, 0.2f, 0.98f, 0.82f, 0.9f, 0.5f),
        SeedItem("avocado", "Avocado", "Food", 0.25f, 0.35f, 0.18f, 0.26f, 0.5f, 0.38f, 0.45f),
        SeedItem("tomato", "Ripe Red Tomato", "Food", 0.88f, 0.2f, 0.15f, 0.01f, 0.82f, 0.9f, 0.25f),
        SeedItem("cucumber", "Green Cucumber", "Food", 0.25f, 0.55f, 0.22f, 0.32f, 0.6f, 0.58f, 0.35f),
        SeedItem("carrot", "Orange Carrot", "Food", 0.95f, 0.45f, 0.1f, 0.06f, 0.88f, 0.95f, 0.35f),
        SeedItem("onion", "Yellow Onion", "Food", 0.78f, 0.6f, 0.35f, 0.1f, 0.55f, 0.8f, 0.35f),
        SeedItem("potato", "Russet Potato", "Food", 0.6f, 0.48f, 0.32f, 0.09f, 0.45f, 0.62f, 0.3f),
        SeedItem("bell_pepper", "Bell Pepper", "Food", 0.85f, 0.15f, 0.15f, 0.0f, 0.85f, 0.88f, 0.35f),
        SeedItem("bread_loaf", "Loaf of Bread", "Food", 0.78f, 0.6f, 0.35f, 0.09f, 0.55f, 0.8f, 0.45f),
        SeedItem("ballpoint_pen", "Ballpoint Pen", "Work", 0.15f, 0.25f, 0.65f, 0.62f, 0.7f, 0.68f, 0.5f),
        SeedItem("gel_pen", "Gel Rollerball Pen", "Work", 0.12f, 0.12f, 0.15f, 0.0f, 0.0f, 0.18f, 0.5f),
        SeedItem("fountain_pen", "Fountain Pen", "Work", 0.2f, 0.2f, 0.25f, 0.65f, 0.2f, 0.3f, 0.65f),
        SeedItem("wooden_pencil", "Wooden No. 2 Pencil", "Work", 0.95f, 0.75f, 0.15f, 0.12f, 0.82f, 0.95f, 0.45f),
        SeedItem("mech_pencil", "Mechanical Pencil", "Work", 0.3f, 0.3f, 0.35f, 0.6f, 0.15f, 0.38f, 0.55f),
        SeedItem("highlighter", "Yellow Highlighter Marker", "Work", 0.85f, 0.95f, 0.15f, 0.18f, 0.85f, 0.96f, 0.4f),
        SeedItem("sharpie", "Permanent Sharpie Marker", "Work", 0.12f, 0.12f, 0.15f, 0.0f, 0.0f, 0.18f, 0.45f),
        SeedItem("whiteboard_marker", "Dry Erase Marker", "Work", 0.2f, 0.45f, 0.75f, 0.58f, 0.7f, 0.78f, 0.45f),
        SeedItem("eraser", "Rubber Pencil Eraser", "Work", 0.95f, 0.7f, 0.75f, 0.97f, 0.25f, 0.96f, 0.25f),
        SeedItem("pencil_sharpener", "Pencil Sharpener", "Work", 0.4f, 0.7f, 0.85f, 0.55f, 0.5f, 0.88f, 0.55f),
        SeedItem("spiral_notebook", "Spiral Notebook", "Work", 0.88f, 0.88f, 0.85f, 0.12f, 0.08f, 0.9f, 0.6f),
        SeedItem("hardcover_journal", "Hardcover Journal", "Work", 0.2f, 0.22f, 0.28f, 0.62f, 0.3f, 0.3f, 0.45f),
        SeedItem("legal_pad", "Yellow Legal Writing Pad", "Work", 0.95f, 0.9f, 0.4f, 0.15f, 0.55f, 0.95f, 0.5f),
        SeedItem("sticky_notes", "Sticky Notes / Post-it", "Work", 0.95f, 0.92f, 0.25f, 0.16f, 0.8f, 0.95f, 0.2f),
        SeedItem("index_cards", "Index Flash Cards", "Work", 0.95f, 0.95f, 0.95f, 0.0f, 0.0f, 0.98f, 0.35f),
        SeedItem("clipboard", "Hardboard Clipboard", "Work", 0.65f, 0.48f, 0.32f, 0.08f, 0.5f, 0.68f, 0.45f),
        SeedItem("folder", "Two-Pocket File Folder", "Work", 0.2f, 0.5f, 0.8f, 0.58f, 0.75f, 0.82f, 0.35f),
        SeedItem("binder", "Three-Ring Binder", "Work", 0.9f, 0.9f, 0.92f, 0.0f, 0.0f, 0.95f, 0.45f),
        SeedItem("envelope", "Mailing Envelope", "Work", 0.95f, 0.92f, 0.88f, 0.1f, 0.08f, 0.96f, 0.3f),
        SeedItem("scissors", "Scissors", "Work", 0.65f, 0.2f, 0.2f, 0.0f, 0.7f, 0.7f, 0.65f),
        SeedItem("stapler", "Desktop Stapler", "Work", 0.2f, 0.2f, 0.22f, 0.0f, 0.0f, 0.25f, 0.55f),
        SeedItem("staple_remover", "Staple Remover Tool", "Work", 0.15f, 0.15f, 0.18f, 0.0f, 0.0f, 0.2f, 0.6f),
        SeedItem("paper_clips", "Box of Paper Clips", "Work", 0.7f, 0.7f, 0.75f, 0.6f, 0.08f, 0.78f, 0.75f),
        SeedItem("binder_clips", "Black Binder Clips", "Work", 0.15f, 0.15f, 0.18f, 0.0f, 0.0f, 0.2f, 0.65f),
        SeedItem("tape_dispenser", "Scotch Tape Dispenser", "Work", 0.2f, 0.2f, 0.22f, 0.0f, 0.0f, 0.25f, 0.5f),
        SeedItem("glue_stick", "Glue Stick", "Work", 0.9f, 0.9f, 0.92f, 0.0f, 0.02f, 0.95f, 0.35f),
        SeedItem("white_out", "Correction Tape / White-Out", "Work", 0.35f, 0.65f, 0.85f, 0.56f, 0.6f, 0.88f, 0.45f),
        SeedItem("ruler", "Ruler (12 inch / 30cm)", "Work", 0.85f, 0.85f, 0.88f, 0.6f, 0.05f, 0.9f, 0.6f),
        SeedItem("desk_lamp", "Desk Study Lamp", "Household", 0.8f, 0.8f, 0.82f, 0.15f, 0.1f, 0.85f, 0.5f),
        SeedItem("desk_organizer", "Desk Pen Organizer Caddy", "Work", 0.22f, 0.22f, 0.25f, 0.0f, 0.0f, 0.28f, 0.65f),
        SeedItem("mousepad", "Desk Mouse Pad", "Work", 0.15f, 0.15f, 0.18f, 0.0f, 0.0f, 0.2f, 0.35f),
        SeedItem("laptop_stand", "Aluminum Laptop Stand", "Work", 0.75f, 0.75f, 0.78f, 0.6f, 0.05f, 0.8f, 0.5f),
        SeedItem("paper_shredder", "Office Paper Shredder", "Work", 0.2f, 0.2f, 0.22f, 0.0f, 0.0f, 0.25f, 0.6f),
        SeedItem("novel_book", "Hardcover Book / Novel", "Work", 0.45f, 0.25f, 0.2f, 0.03f, 0.55f, 0.48f, 0.55f),
        SeedItem("paperback_book", "Paperback Novel", "Work", 0.8f, 0.75f, 0.65f, 0.11f, 0.2f, 0.82f, 0.6f),
        SeedItem("magazine", "Glossy Magazine", "Work", 0.85f, 0.5f, 0.4f, 0.05f, 0.5f, 0.88f, 0.8f),
        SeedItem("newspaper", "Newspaper", "Work", 0.8f, 0.8f, 0.78f, 0.12f, 0.05f, 0.82f, 0.85f),
        SeedItem("comic_book", "Comic Book / Manga", "Work", 0.88f, 0.4f, 0.2f, 0.05f, 0.75f, 0.9f, 0.85f),
        SeedItem("textbook", "Academic Textbook", "Work", 0.25f, 0.4f, 0.65f, 0.6f, 0.6f, 0.68f, 0.65f),
        SeedItem("bookmark", "Reading Bookmark", "Work", 0.85f, 0.65f, 0.25f, 0.11f, 0.7f, 0.88f, 0.55f),
        SeedItem("toothbrush", "Manual Toothbrush", "Personal", 0.3f, 0.65f, 0.85f, 0.56f, 0.65f, 0.88f, 0.55f),
        SeedItem("electric_toothbrush", "Electric Toothbrush", "Personal", 0.92f, 0.92f, 0.95f, 0.6f, 0.05f, 0.96f, 0.5f),
        SeedItem("toothpaste", "Toothpaste Tube", "Personal", 0.25f, 0.55f, 0.85f, 0.58f, 0.7f, 0.88f, 0.45f),
        SeedItem("dental_floss", "Dental Floss Container", "Personal", 0.9f, 0.9f, 0.92f, 0.6f, 0.05f, 0.95f, 0.35f),
        SeedItem("mouthwash", "Mouthwash Bottle", "Personal", 0.2f, 0.75f, 0.7f, 0.48f, 0.75f, 0.8f, 0.4f),
        SeedItem("soap_bar", "Bar of Soap", "Personal", 0.92f, 0.88f, 0.82f, 0.1f, 0.12f, 0.94f, 0.25f),
        SeedItem("liquid_hand_soap", "Liquid Hand Soap Pump", "Personal", 0.8f, 0.85f, 0.9f, 0.55f, 0.12f, 0.92f, 0.4f),
        SeedItem("body_wash", "Body Wash Bottle", "Personal", 0.35f, 0.55f, 0.75f, 0.58f, 0.5f, 0.78f, 0.45f),
        SeedItem("shampoo", "Shampoo Bottle", "Personal", 0.25f, 0.6f, 0.7f, 0.52f, 0.65f, 0.75f, 0.45f),
        SeedItem("conditioner", "Hair Conditioner Bottle", "Personal", 0.9f, 0.85f, 0.88f, 0.9f, 0.08f, 0.92f, 0.4f),
        SeedItem("hairbrush", "Paddle Hairbrush", "Personal", 0.2f, 0.2f, 0.22f, 0.0f, 0.0f, 0.25f, 0.7f),
        SeedItem("hair_comb", "Hair Comb", "Personal", 0.15f, 0.15f, 0.18f, 0.0f, 0.0f, 0.2f, 0.75f),
        SeedItem("hairdryer", "Hair Blow Dryer", "Personal", 0.22f, 0.22f, 0.25f, 0.0f, 0.0f, 0.28f, 0.6f),
        SeedItem("hair_straightener", "Hair Straightener / Flat Iron", "Personal", 0.18f, 0.18f, 0.2f, 0.0f, 0.0f, 0.22f, 0.55f),
        SeedItem("shaving_razor", "Shaving Razor", "Personal", 0.3f, 0.6f, 0.8f, 0.57f, 0.6f, 0.82f, 0.6f),
        SeedItem("electric_shaver", "Electric Foil Shaver", "Personal", 0.25f, 0.25f, 0.3f, 0.65f, 0.15f, 0.35f, 0.65f),
        SeedItem("shaving_cream", "Shaving Cream Foam Can", "Personal", 0.85f, 0.85f, 0.9f, 0.6f, 0.08f, 0.92f, 0.45f),
        SeedItem("deodorant", "Deodorant Stick", "Personal", 0.2f, 0.45f, 0.75f, 0.58f, 0.7f, 0.78f, 0.4f),
        SeedItem("perfume_bottle", "Perfume / Cologne Bottle", "Personal", 0.85f, 0.88f, 0.92f, 0.58f, 0.08f, 0.94f, 0.45f),
        SeedItem("face_cream", "Facial Moisturizer Tub", "Personal", 0.9f, 0.9f, 0.92f, 0.0f, 0.02f, 0.94f, 0.3f),
        SeedItem("sunscreen", "Sunscreen Lotion Tube", "Personal", 0.95f, 0.8f, 0.2f, 0.13f, 0.8f, 0.96f, 0.4f),
        SeedItem("lip_balm", "Lip Balm / Chapstick", "Personal", 0.3f, 0.65f, 0.85f, 0.56f, 0.65f, 0.88f, 0.35f),
        SeedItem("hand_sanitizer", "Hand Sanitizer Bottle", "Personal", 0.75f, 0.85f, 0.9f, 0.52f, 0.2f, 0.92f, 0.3f),
        SeedItem("nail_clippers", "Nail Clippers", "Personal", 0.78f, 0.78f, 0.8f, 0.0f, 0.0f, 0.82f, 0.65f),
        SeedItem("tweezers", "Metal Tweezers", "Personal", 0.75f, 0.75f, 0.78f, 0.0f, 0.0f, 0.8f, 0.55f),
        SeedItem("cotton_swabs", "Box of Cotton Swabs (Q-tips)", "Personal", 0.92f, 0.92f, 0.95f, 0.58f, 0.05f, 0.96f, 0.65f),
        SeedItem("tissue_box", "Facial Tissue Box", "Personal", 0.8f, 0.85f, 0.9f, 0.58f, 0.12f, 0.92f, 0.45f),
        SeedItem("wet_wipes", "Pack of Wet Wipes", "Personal", 0.85f, 0.9f, 0.95f, 0.58f, 0.1f, 0.96f, 0.4f),
        SeedItem("band_aid", "First Aid Band-Aid Box", "Personal", 0.85f, 0.4f, 0.3f, 0.03f, 0.65f, 0.88f, 0.55f),
        SeedItem("pill_bottle", "Prescription Medicine Bottle", "Personal", 0.85f, 0.55f, 0.15f, 0.09f, 0.82f, 0.88f, 0.45f),
        SeedItem("vitamins", "Vitamin Supplement Bottle", "Personal", 0.9f, 0.9f, 0.92f, 0.12f, 0.05f, 0.94f, 0.45f),
        SeedItem("thermometer", "Digital Medical Thermometer", "Personal", 0.92f, 0.92f, 0.95f, 0.58f, 0.05f, 0.96f, 0.45f),
        SeedItem("contact_lens_case", "Contact Lens Case", "Personal", 0.9f, 0.9f, 0.92f, 0.58f, 0.05f, 0.95f, 0.4f),
        SeedItem("contact_solution", "Contact Lens Solution Bottle", "Personal", 0.85f, 0.9f, 0.95f, 0.58f, 0.1f, 0.96f, 0.4f),
        SeedItem("face_mask", "Medical Face Mask", "Personal", 0.6f, 0.8f, 0.92f, 0.55f, 0.35f, 0.94f, 0.35f),
        SeedItem("tshirt", "Casual T-Shirt", "Clothing", 0.85f, 0.85f, 0.88f, 0.6f, 0.05f, 0.9f, 0.4f),
        SeedItem("dress_shirt", "Button-Down Dress Shirt", "Clothing", 0.92f, 0.92f, 0.95f, 0.6f, 0.05f, 0.96f, 0.55f),
        SeedItem("polo_shirt", "Polo Shirt", "Clothing", 0.2f, 0.4f, 0.7f, 0.6f, 0.7f, 0.72f, 0.5f),
        SeedItem("hoodie", "Fleece Hoodie / Sweatshirt", "Clothing", 0.4f, 0.4f, 0.45f, 0.65f, 0.12f, 0.48f, 0.5f),
        SeedItem("sweater", "Knit Wool Sweater", "Clothing", 0.7f, 0.55f, 0.4f, 0.08f, 0.4f, 0.72f, 0.6f),
        SeedItem("jeans", "Denim Blue Jeans", "Clothing", 0.25f, 0.38f, 0.6f, 0.6f, 0.58f, 0.62f, 0.65f),
        SeedItem("chino_pants", "Chino Khaki Pants", "Clothing", 0.75f, 0.65f, 0.48f, 0.1f, 0.35f, 0.78f, 0.45f),
        SeedItem("sweatpants", "Gym Sweatpants / Joggers", "Clothing", 0.35f, 0.35f, 0.38f, 0.6f, 0.08f, 0.4f, 0.45f),
        SeedItem("shorts", "Casual Shorts", "Clothing", 0.3f, 0.5f, 0.4f, 0.4f, 0.4f, 0.52f, 0.45f),
        SeedItem("winter_jacket", "Puffer Winter Jacket", "Clothing", 0.15f, 0.15f, 0.18f, 0.6f, 0.15f, 0.2f, 0.55f),
        SeedItem("leather_jacket", "Leather Biker Jacket", "Clothing", 0.12f, 0.12f, 0.14f, 0.0f, 0.0f, 0.16f, 0.6f),
        SeedItem("denim_jacket", "Denim Jean Jacket", "Clothing", 0.3f, 0.45f, 0.65f, 0.6f, 0.5f, 0.68f, 0.65f),
        SeedItem("raincoat", "Yellow Raincoat", "Clothing", 0.95f, 0.85f, 0.15f, 0.14f, 0.85f, 0.96f, 0.35f),
        SeedItem("blazer", "Suit Blazer Jacket", "Clothing", 0.15f, 0.2f, 0.35f, 0.62f, 0.55f, 0.38f, 0.6f),
        SeedItem("dress", "Women's Casual Dress", "Clothing", 0.85f, 0.25f, 0.4f, 0.95f, 0.7f, 0.88f, 0.5f),
        SeedItem("skirt", "Casual Skirt", "Clothing", 0.2f, 0.2f, 0.25f, 0.65f, 0.2f, 0.28f, 0.45f),
        SeedItem("sneakers", "Running Shoes / Sneakers", "Clothing", 0.3f, 0.3f, 0.35f, 0.6f, 0.15f, 0.4f, 0.75f),
        SeedItem("canvas_shoes", "Canvas Sneakers / Converse", "Clothing", 0.18f, 0.18f, 0.2f, 0.0f, 0.0f, 0.22f, 0.65f),
        SeedItem("dress_shoes", "Leather Dress Shoes (Oxfords)", "Clothing", 0.12f, 0.12f, 0.12f, 0.0f, 0.0f, 0.15f, 0.6f),
        SeedItem("high_heels", "High Heel Pumps", "Clothing", 0.85f, 0.15f, 0.2f, 0.98f, 0.8f, 0.88f, 0.55f),
        SeedItem("winter_boots", "Winter Snow Boots", "Clothing", 0.45f, 0.35f, 0.25f, 0.08f, 0.45f, 0.48f, 0.7f),
        SeedItem("sandals", "Beach Flip-Flops / Sandals", "Clothing", 0.25f, 0.65f, 0.75f, 0.53f, 0.65f, 0.78f, 0.45f),
        SeedItem("slippers", "Cozy Home Slippers", "Clothing", 0.65f, 0.55f, 0.5f, 0.08f, 0.25f, 0.68f, 0.5f),
        SeedItem("baseball_cap", "Baseball Cap", "Clothing", 0.2f, 0.3f, 0.65f, 0.62f, 0.68f, 0.68f, 0.6f),
        SeedItem("beanie", "Knit Winter Beanie", "Clothing", 0.35f, 0.35f, 0.38f, 0.6f, 0.1f, 0.4f, 0.65f),
        SeedItem("sun_hat", "Straw Sun Hat", "Clothing", 0.85f, 0.75f, 0.5f, 0.11f, 0.4f, 0.88f, 0.55f),
        SeedItem("scarf", "Winter Neck Scarf", "Clothing", 0.7f, 0.25f, 0.25f, 0.0f, 0.65f, 0.72f, 0.65f),
        SeedItem("gloves", "Pair of Winter Gloves", "Clothing", 0.2f, 0.2f, 0.22f, 0.0f, 0.0f, 0.25f, 0.55f),
        SeedItem("leather_belt", "Leather Waist Belt", "Clothing", 0.3f, 0.2f, 0.15f, 0.06f, 0.5f, 0.32f, 0.5f),
        SeedItem("necktie", "Silk Necktie", "Clothing", 0.2f, 0.3f, 0.6f, 0.6f, 0.65f, 0.62f, 0.65f),
        SeedItem("bowtie", "Bowtie", "Clothing", 0.12f, 0.12f, 0.15f, 0.0f, 0.0f, 0.18f, 0.6f),
        SeedItem("socks", "Pair of Athletic Socks", "Clothing", 0.9f, 0.9f, 0.92f, 0.0f, 0.02f, 0.95f, 0.45f),
        SeedItem("wallet", "Leather Wallet", "Personal", 0.45f, 0.28f, 0.16f, 0.07f, 0.65f, 0.48f, 0.5f),
        SeedItem("bifold_wallet", "Slim Bifold Wallet", "Personal", 0.18f, 0.18f, 0.2f, 0.0f, 0.0f, 0.22f, 0.45f),
        SeedItem("coin_purse", "Coin Purse Pouch", "Personal", 0.75f, 0.3f, 0.45f, 0.92f, 0.6f, 0.78f, 0.5f),
        SeedItem("handbag", "Women's Leather Handbag", "Clothing", 0.5f, 0.32f, 0.2f, 0.07f, 0.6f, 0.52f, 0.55f),
        SeedItem("tote_bag", "Canvas Tote Bag", "Clothing", 0.88f, 0.85f, 0.78f, 0.1f, 0.12f, 0.9f, 0.45f),
        SeedItem("backpack", "Backpack / Schoolbag", "Personal", 0.25f, 0.28f, 0.35f, 0.6f, 0.3f, 0.38f, 0.7f),
        SeedItem("briefcase", "Messenger Bag / Briefcase", "Personal", 0.35f, 0.25f, 0.18f, 0.07f, 0.5f, 0.38f, 0.6f),
        SeedItem("gym_bag", "Duffel Gym Bag", "Personal", 0.22f, 0.22f, 0.25f, 0.65f, 0.15f, 0.28f, 0.65f),
        SeedItem("suitcase", "Rolling Luggage Suitcase", "Personal", 0.25f, 0.4f, 0.65f, 0.6f, 0.6f, 0.68f, 0.6f),
        SeedItem("sunglasses", "UV Sunglasses", "Personal", 0.1f, 0.1f, 0.1f, 0.0f, 0.0f, 0.12f, 0.6f),
        SeedItem("reading_glasses", "Reading Spectacles", "Personal", 0.25f, 0.25f, 0.3f, 0.6f, 0.15f, 0.35f, 0.75f),
        SeedItem("analog_watch", "Analog Wristwatch", "Personal", 0.75f, 0.65f, 0.45f, 0.1f, 0.4f, 0.78f, 0.7f),
        SeedItem("necklace", "Jewelry Chain Necklace", "Personal", 0.92f, 0.8f, 0.3f, 0.13f, 0.68f, 0.94f, 0.75f),
        SeedItem("office_chair", "Office Ergonomic Desk Chair", "Household", 0.2f, 0.2f, 0.22f, 0.0f, 0.0f, 0.25f, 0.7f),
        SeedItem("armchair", "Living Room Armchair", "Household", 0.45f, 0.45f, 0.48f, 0.6f, 0.08f, 0.5f, 0.6f),
        SeedItem("sofa", "Living Room Sofa / Couch", "Household", 0.5f, 0.5f, 0.52f, 0.6f, 0.05f, 0.55f, 0.55f),
        SeedItem("dining_table", "Wooden Dining Table", "Household", 0.65f, 0.45f, 0.28f, 0.08f, 0.58f, 0.68f, 0.45f),
        SeedItem("coffee_table", "Living Room Coffee Table", "Household", 0.7f, 0.52f, 0.35f, 0.08f, 0.5f, 0.72f, 0.4f),
        SeedItem("nightstand", "Bedside Nightstand", "Household", 0.85f, 0.85f, 0.85f, 0.0f, 0.0f, 0.9f, 0.45f),
        SeedItem("bookshelf", "Wooden Bookshelf", "Household", 0.55f, 0.38f, 0.22f, 0.08f, 0.6f, 0.58f, 0.75f),
        SeedItem("floor_lamp", "Living Room Floor Lamp", "Household", 0.85f, 0.85f, 0.88f, 0.15f, 0.05f, 0.9f, 0.5f),
        SeedItem("table_lamp", "Bedside Table Lamp", "Household", 0.9f, 0.88f, 0.82f, 0.12f, 0.1f, 0.92f, 0.45f),
        SeedItem("ceiling_fan", "Ceiling Fan", "Household", 0.85f, 0.85f, 0.88f, 0.0f, 0.0f, 0.9f, 0.6f),
        SeedItem("wall_clock", "Analog Wall Clock", "Household", 0.9f, 0.9f, 0.92f, 0.0f, 0.0f, 0.94f, 0.65f),
        SeedItem("picture_frame", "Framed Wall Picture", "Household", 0.5f, 0.35f, 0.22f, 0.08f, 0.55f, 0.52f, 0.7f),
        SeedItem("wall_mirror", "Wall Hanging Mirror", "Household", 0.88f, 0.9f, 0.92f, 0.58f, 0.05f, 0.94f, 0.4f),
        SeedItem("cushion", "Throw Pillow / Cushion", "Household", 0.85f, 0.65f, 0.25f, 0.11f, 0.7f, 0.88f, 0.45f),
        SeedItem("blanket", "Throw Blanket", "Household", 0.6f, 0.65f, 0.75f, 0.6f, 0.2f, 0.78f, 0.5f),
        SeedItem("bed_pillow", "Sleeping Bed Pillow", "Household", 0.95f, 0.95f, 0.95f, 0.0f, 0.0f, 0.98f, 0.25f),
        SeedItem("mattress", "Bed Mattress", "Household", 0.92f, 0.92f, 0.94f, 0.6f, 0.02f, 0.95f, 0.35f),
        SeedItem("area_rug", "Living Room Area Rug", "Household", 0.65f, 0.55f, 0.45f, 0.08f, 0.3f, 0.68f, 0.7f),
        SeedItem("curtains", "Window Curtains", "Household", 0.75f, 0.75f, 0.78f, 0.6f, 0.05f, 0.8f, 0.5f),
        SeedItem("house_keys", "House Door Keys", "Personal", 0.6f, 0.6f, 0.65f, 0.6f, 0.1f, 0.7f, 0.8f),
        SeedItem("padlock", "Brass Padlock", "Household", 0.85f, 0.75f, 0.25f, 0.13f, 0.7f, 0.88f, 0.65f),
        SeedItem("hammer", "Claw Hammer", "Household", 0.55f, 0.4f, 0.25f, 0.08f, 0.55f, 0.58f, 0.7f),
        SeedItem("screwdriver_phillips", "Phillips Screwdriver", "Household", 0.8f, 0.2f, 0.15f, 0.01f, 0.8f, 0.85f, 0.6f),
        SeedItem("screwdriver_flat", "Flathead Screwdriver", "Household", 0.25f, 0.5f, 0.8f, 0.58f, 0.7f, 0.82f, 0.6f),
        SeedItem("wrench", "Adjustable Crescent Wrench", "Household", 0.75f, 0.75f, 0.78f, 0.0f, 0.0f, 0.8f, 0.65f),
        SeedItem("pliers", "Combination Pliers", "Household", 0.7f, 0.3f, 0.2f, 0.03f, 0.7f, 0.75f, 0.7f),
        SeedItem("tape_measure", "Tape Measure", "Household", 0.95f, 0.85f, 0.15f, 0.14f, 0.85f, 0.96f, 0.65f),
        SeedItem("handsaw", "Hand Saw", "Household", 0.7f, 0.5f, 0.3f, 0.08f, 0.55f, 0.72f, 0.75f),
        SeedItem("power_drill", "Electric Cordless Power Drill", "Household", 0.2f, 0.55f, 0.35f, 0.38f, 0.65f, 0.58f, 0.75f),
        SeedItem("spirit_level", "Spirit Level Tool", "Household", 0.85f, 0.9f, 0.15f, 0.18f, 0.85f, 0.94f, 0.65f),
        SeedItem("flashlight", "LED Flashlight / Torch", "Household", 0.18f, 0.18f, 0.2f, 0.0f, 0.0f, 0.22f, 0.55f),
        SeedItem("utility_knife", "Utility Knife / Box Cutter", "Household", 0.85f, 0.2f, 0.15f, 0.01f, 0.82f, 0.88f, 0.6f),
        SeedItem("duct_tape", "Roll of Duct Tape", "Household", 0.75f, 0.75f, 0.78f, 0.0f, 0.0f, 0.8f, 0.45f),
        SeedItem("extension_cord", "Heavy Duty Extension Cord", "Household", 0.95f, 0.55f, 0.1f, 0.08f, 0.9f, 0.96f, 0.6f),
        SeedItem("stepladder", "Folding Step Ladder", "Household", 0.75f, 0.75f, 0.78f, 0.0f, 0.0f, 0.8f, 0.7f),
        SeedItem("broom_dustpan", "Broom & Dustpan", "Household", 0.35f, 0.65f, 0.55f, 0.45f, 0.45f, 0.68f, 0.65f),
        SeedItem("floor_mop", "Floor Cleaning Mop", "Household", 0.3f, 0.55f, 0.8f, 0.58f, 0.62f, 0.82f, 0.6f),
        SeedItem("vacuum_cleaner", "Upright Vacuum Cleaner", "Household", 0.25f, 0.45f, 0.75f, 0.58f, 0.65f, 0.78f, 0.7f),
        SeedItem("roomba", "Robotic Vacuum (Roomba)", "Household", 0.18f, 0.18f, 0.2f, 0.0f, 0.0f, 0.22f, 0.5f),
        SeedItem("trash_can", "Kitchen Trash Can", "Household", 0.75f, 0.75f, 0.78f, 0.0f, 0.0f, 0.8f, 0.45f),
        SeedItem("soccer_ball", "Soccer Ball / Football", "Household", 0.9f, 0.9f, 0.9f, 0.0f, 0.0f, 0.95f, 0.85f),
        SeedItem("basketball", "Basketball", "Household", 0.88f, 0.45f, 0.15f, 0.06f, 0.82f, 0.9f, 0.65f),
        SeedItem("baseball", "Baseball", "Household", 0.92f, 0.92f, 0.92f, 0.0f, 0.0f, 0.95f, 0.55f),
        SeedItem("tennis_ball", "Tennis Ball", "Household", 0.8f, 0.92f, 0.2f, 0.2f, 0.78f, 0.94f, 0.5f),
        SeedItem("tennis_racket", "Tennis Racket", "Household", 0.25f, 0.55f, 0.8f, 0.58f, 0.68f, 0.82f, 0.8f),
        SeedItem("badminton_racket", "Badminton Racket", "Household", 0.75f, 0.75f, 0.78f, 0.0f, 0.0f, 0.8f, 0.8f),
        SeedItem("golf_ball", "Golf Ball", "Household", 0.95f, 0.95f, 0.95f, 0.0f, 0.0f, 0.98f, 0.4f),
        SeedItem("golf_club", "Golf Club (Iron/Driver)", "Household", 0.75f, 0.75f, 0.78f, 0.0f, 0.0f, 0.8f, 0.7f),
        SeedItem("yoga_mat", "Roll-up Yoga Mat", "Household", 0.65f, 0.35f, 0.75f, 0.8f, 0.55f, 0.78f, 0.45f),
        SeedItem("dumbbell", "Hand Dumbbell Weight", "Household", 0.18f, 0.18f, 0.2f, 0.0f, 0.0f, 0.22f, 0.55f),
        SeedItem("kettlebell", "Cast Iron Kettlebell", "Household", 0.15f, 0.15f, 0.18f, 0.0f, 0.0f, 0.2f, 0.6f),
        SeedItem("resistance_band", "Fitness Resistance Band", "Household", 0.85f, 0.2f, 0.25f, 0.98f, 0.75f, 0.88f, 0.35f),
        SeedItem("jump_rope", "Fitness Jump Rope", "Household", 0.2f, 0.2f, 0.25f, 0.6f, 0.2f, 0.28f, 0.55f),
        SeedItem("bicycle", "Bicycle / Bike", "Household", 0.25f, 0.5f, 0.75f, 0.58f, 0.65f, 0.78f, 0.85f),
        SeedItem("bicycle_helmet", "Bicycle Safety Helmet", "Personal", 0.25f, 0.6f, 0.85f, 0.56f, 0.7f, 0.88f, 0.65f),
        SeedItem("skateboard", "Skateboard", "Household", 0.7f, 0.5f, 0.35f, 0.08f, 0.5f, 0.72f, 0.75f),
        SeedItem("rollerblades", "Rollerblades / Inline Skates", "Household", 0.25f, 0.25f, 0.3f, 0.65f, 0.18f, 0.32f, 0.75f),
        SeedItem("tent", "Camping Tent", "Household", 0.25f, 0.55f, 0.4f, 0.42f, 0.55f, 0.58f, 0.6f),
        SeedItem("sleeping_bag", "Outdoor Sleeping Bag", "Household", 0.2f, 0.45f, 0.65f, 0.6f, 0.68f, 0.68f, 0.55f),
        SeedItem("camping_lantern", "Camping Lantern Light", "Household", 0.8f, 0.35f, 0.2f, 0.04f, 0.75f, 0.82f, 0.65f),
        SeedItem("acoustic_guitar", "Acoustic Guitar", "Household", 0.65f, 0.4f, 0.2f, 0.08f, 0.7f, 0.68f, 0.7f),
        SeedItem("electric_guitar", "Electric Guitar", "Household", 0.85f, 0.15f, 0.2f, 0.98f, 0.8f, 0.88f, 0.75f),
        SeedItem("ukulele", "Ukulele", "Household", 0.7f, 0.48f, 0.28f, 0.08f, 0.6f, 0.72f, 0.65f),
        SeedItem("piano_keyboard", "Synthesizer / Piano Keyboard", "Household", 0.2f, 0.2f, 0.22f, 0.0f, 0.0f, 0.25f, 0.85f),
        SeedItem("violin", "Violin with Bow", "Household", 0.6f, 0.32f, 0.16f, 0.07f, 0.72f, 0.62f, 0.7f),
        SeedItem("drum_sticks", "Wooden Drum Sticks", "Household", 0.85f, 0.75f, 0.55f, 0.11f, 0.35f, 0.88f, 0.4f),
        SeedItem("rubiks_cube", "Rubik's Cube", "Household", 0.75f, 0.5f, 0.2f, 0.1f, 0.8f, 0.8f, 0.95f),
        SeedItem("board_game", "Board Game Box", "Household", 0.85f, 0.35f, 0.2f, 0.04f, 0.75f, 0.88f, 0.8f),
        SeedItem("playing_cards", "Deck of Playing Cards", "Household", 0.92f, 0.92f, 0.92f, 0.0f, 0.0f, 0.95f, 0.75f),
        SeedItem("chess_set", "Chess Board & Pieces", "Household", 0.75f, 0.6f, 0.4f, 0.09f, 0.45f, 0.78f, 0.85f),
        SeedItem("jigsaw_puzzle", "Jigsaw Puzzle Box", "Household", 0.7f, 0.6f, 0.45f, 0.1f, 0.35f, 0.72f, 0.8f),
        SeedItem("teddy_bear", "Teddy Bear Plush Toy", "Household", 0.65f, 0.45f, 0.28f, 0.08f, 0.55f, 0.68f, 0.5f),
        SeedItem("lego_bricks", "LEGO Building Bricks", "Household", 0.85f, 0.2f, 0.15f, 0.0f, 0.82f, 0.88f, 0.85f),
        SeedItem("action_figure", "Action Figure Toy", "Household", 0.35f, 0.55f, 0.75f, 0.6f, 0.55f, 0.78f, 0.8f),
        SeedItem("model_car", "Die-Cast Model Toy Car", "Household", 0.85f, 0.15f, 0.15f, 0.0f, 0.85f, 0.88f, 0.75f),
    )

    fun createVectorFromParams(
        r: Float, g: Float, b: Float,
        h: Float, s: Float, v: Float,
        tex: Float
    ): String {
        val raw = FloatArray(48)
        raw[0] = r * 0.85f; raw[1] = g * 0.85f; raw[2] = b * 0.85f
        raw[3] = h; raw[4] = s * 0.8f; raw[5] = v * 0.85f
        raw[6] = tex * 0.6f; raw[7] = tex * 0.5f

        raw[8] = r * 0.90f; raw[9] = g * 0.90f; raw[10] = b * 0.90f
        raw[11] = h; raw[12] = s * 0.85f; raw[13] = v * 0.80f
        raw[14] = tex * 0.7f; raw[15] = tex * 0.6f

        raw[16] = r; raw[17] = g; raw[18] = b
        raw[19] = h; raw[20] = s; raw[21] = v
        raw[22] = tex; raw[23] = tex

        raw[24] = 0.40f; raw[25] = 0.38f; raw[26] = 0.35f
        raw[27] = 0.08f; raw[28] = 0.15f; raw[29] = 0.42f
        raw[30] = 0.08f; raw[31] = 0.08f

        val dominantBin = ((h.coerceIn(0f, 0.99f)) * 16).toInt().coerceIn(0, 15)
        val binWeight = s * v
        raw[32 + dominantBin] = binWeight * 0.65f
        raw[32 + ((dominantBin + 1) % 16)] = binWeight * 0.20f
        raw[32 + ((dominantBin + 15) % 16)] = binWeight * 0.15f

        val normalized = VisualFeatureExtractor.normalizeVector(raw)
        return VisualFeatureExtractor.serializeVector(normalized)
    }

    private fun createVector(item: SeedItem): String {
        return createVectorFromParams(item.r, item.g, item.b, item.h, item.s, item.v, item.tex)
    }

    fun loadFromAssets(context: Context): List<LearnedObjectEntity> {
        val inputStream = context.assets.open("prepopulated_objects.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(jsonString)
        val now = System.currentTimeMillis()
        val list = ArrayList<LearnedObjectEntity>(jsonArray.length())

        for (i in 0 until jsonArray.length()) {
            val arr = jsonArray.getJSONArray(i)
            val id = arr.getString(0)
            val name = arr.getString(1)
            val cat = arr.getString(2)
            val r = arr.getDouble(3).toFloat()
            val g = arr.getDouble(4).toFloat()
            val b = arr.getDouble(5).toFloat()
            val h = arr.getDouble(6).toFloat()
            val s = arr.getDouble(7).toFloat()
            val v = arr.getDouble(8).toFloat()
            val tex = arr.getDouble(9).toFloat()

            val vector = createVectorFromParams(r, g, b, h, s, v, tex)
            list.add(
                LearnedObjectEntity(
                    id = "pre_$id",
                    name = name,
                    category = cat,
                    featureVector = vector,
                    sampleCount = 3,
                    createdAt = now,
                    lastRecognizedAt = now,
                    notes = "Pre-learned community object"
                )
            )
        }
        return list
    }

    fun getAll(context: Context? = null): List<LearnedObjectEntity> {
        if (context != null) {
            try {
                val assetList = loadFromAssets(context)
                if (assetList.isNotEmpty()) {
                    return assetList
                }
            } catch (e: Exception) {
                android.util.Log.e("PrepopulatedObjects", "Failed to load objects from assets", e)
            }
        }
        val now = System.currentTimeMillis()
        return SEED_ITEMS.map { item ->
            LearnedObjectEntity(
                id = "pre_${item.id}",
                name = item.name,
                category = item.category,
                featureVector = createVector(item),
                sampleCount = 3,
                createdAt = now,
                lastRecognizedAt = now,
                notes = "Pre-learned community object"
            )
        }
    }
}
