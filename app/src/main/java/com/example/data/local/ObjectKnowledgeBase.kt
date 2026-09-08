package com.example.data.local

data class ObjectInformation(
    val id: String,
    val name: String,
    val category: String,
    val iconEmoji: String,
    val shortDescription: String,
    val description: String,
    val characteristics: List<String>,
    val commonUses: List<String>,
    val facts: List<String>,
    val similarObjects: List<String>
)

object ObjectKnowledgeBase {

    val categories = listOf(
        "All",
        "Electronics",
        "Food",
        "Animals",
        "Vehicles",
        "Household",
        "Sports",
        "Accessories"
    )

    private val knowledgeMap: Map<String, ObjectInformation> = listOf(
        // Electronics
        ObjectInformation(
            id = "laptop",
            name = "Laptop",
            category = "Electronics",
            iconEmoji = "💻",
            shortDescription = "Portable personal computing device with integrated screen and keyboard.",
            description = "A laptop computer combines the components of a desktop computer, including the display, speakers, keyboard, pointing device, and processor, into a single battery-powered unit.",
            characteristics = listOf("Clamshell folding form factor", "Built-in rechargeable lithium battery", "Integrated trackpad and keyboard", "High-resolution display panel"),
            commonUses = listOf("Software engineering & programming", "Digital content creation", "Remote work & video conferences", "Academic study & research"),
            facts = listOf("The Osborne 1, released in 1981, is considered the first true commercially available portable computer.", "Modern laptops use SSDs capable of transferring data at several gigabytes per second."),
            similarObjects = listOf("Cell Phone", "Keyboard", "Mouse", "TV")
        ),
        ObjectInformation(
            id = "cell phone",
            name = "Cell Phone",
            category = "Electronics",
            iconEmoji = "📱",
            shortDescription = "Handheld mobile computer and wireless telecommunication device.",
            description = "A smartphone is a portable device that combines mobile telephone functionality with ubiquitous computing capabilities, high-resolution cameras, and sensors.",
            characteristics = listOf("Capacitive multi-touch glass screen", "Biometric authentication sensors", "Multi-lens computational camera array", "Cellular and Wi-Fi connectivity"),
            commonUses = listOf("Instant voice and video communication", "Mobile internet navigation & media consumption", "Digital payments and banking", "Camera photography & video recording"),
            facts = listOf("The first handheld mobile phone call was made in 1973 by Martin Cooper of Motorola.", "Modern smartphones contain billions of transistors in a silicon chip smaller than a thumbnail."),
            similarObjects = listOf("Laptop", "Remote", "Mouse")
        ),
        ObjectInformation(
            id = "mouse",
            name = "Computer Mouse",
            category = "Electronics",
            iconEmoji = "🖱️",
            shortDescription = "Hand-held pointing device that detects two-dimensional motion.",
            description = "A computer mouse translates the motion of a user's hand into signals that position the cursor on a graphical user interface.",
            characteristics = listOf("Optical or laser motion tracking sensor", "Ergonomic tactile buttons and scroll wheel", "Low-latency wireless or USB connectivity"),
            commonUses = listOf("GUI cursor precision manipulation", "Computer gaming and creative modeling", "Document scrolling and text selection"),
            facts = listOf("Douglas Engelbart invented the computer mouse in 1964; its prototype was made of wood with two wheels."),
            similarObjects = listOf("Keyboard", "Laptop")
        ),
        ObjectInformation(
            id = "keyboard",
            name = "Keyboard",
            category = "Electronics",
            iconEmoji = "⌨️",
            shortDescription = "Typewriter-style alphanumeric input peripheral.",
            description = "A computer keyboard uses an arrangement of mechanical or membrane buttons and keys to act as mechanical levers or electronic switches.",
            characteristics = listOf("Alphanumeric key layout (e.g., QWERTY)", "Mechanical or scissor-switch actuators", "Function and modifier keys"),
            commonUses = listOf("Text input and composition", "Command-line and gaming controls", "Shortcut hotkey invocation"),
            facts = listOf("The QWERTY layout was patented in 1878 to prevent typebars on mechanical typewriters from jamming."),
            similarObjects = listOf("Laptop", "Mouse")
        ),
        ObjectInformation(
            id = "tv",
            name = "Television",
            category = "Electronics",
            iconEmoji = "📺",
            shortDescription = "Telecommunication medium for transmitting moving visual images and audio.",
            description = "Modern smart televisions feature ultra-high-definition OLED or LED panels connected to digital streaming platforms.",
            characteristics = listOf("Large flat-panel display", "Integrated digital tuner and streaming OS", "Surround audio reproduction"),
            commonUses = listOf("Broadcast television & film streaming", "Gaming display with low-latency modes", "Digital signage & presentations"),
            facts = listOf("The first electronic television was demonstrated in San Francisco in 1927 by 21-year-old Philo Farnsworth."),
            similarObjects = listOf("Laptop", "Remote")
        ),
        ObjectInformation(
            id = "remote",
            name = "Remote Control",
            category = "Electronics",
            iconEmoji = "🕹️",
            shortDescription = "Electronic device used to operate another device wirelessly from a distance.",
            description = "Remote controls primarily transmit infrared (IR) light or radio frequency (RF/Bluetooth) pulses to control electronics without wire tethers.",
            characteristics = listOf("Infrared diode transmitter or Bluetooth link", "Tactile rubber keypad", "Handheld battery-powered enclosure"),
            commonUses = listOf("Controlling TV audio channels and volume", "Navigating media player menus", "Smart home appliance operation"),
            facts = listOf("The first TV remote was created by Zenith in 1950 and was called 'Lazy Bones', connected by a long wire."),
            similarObjects = listOf("Cell Phone", "TV")
        ),

        // Food
        ObjectInformation(
            id = "apple",
            name = "Apple",
            category = "Food",
            iconEmoji = "🍎",
            shortDescription = "Sweet, edible pomaceous fruit produced by an apple tree.",
            description = "Apples are one of the most widely cultivated tree fruits in the world, renowned for their crisp texture, natural sugars, and rich dietary fiber content.",
            characteristics = listOf("Crisp outer skin in shades of red, green, or yellow", "Nutritious edible flesh surrounding small central core", "High pectin and vitamin C content"),
            commonUses = listOf("Fresh healthy snacking", "Baking pies, crumbles, and tarts", "Cider, juice, and applesauce production"),
            facts = listOf("There are over 7,500 recognized varieties of apples grown worldwide.", "Apples float in water because 25% of their volume is made of air pockets."),
            similarObjects = listOf("Banana", "Orange")
        ),
        ObjectInformation(
            id = "banana",
            name = "Banana",
            category = "Food",
            iconEmoji = "🍌",
            shortDescription = "Elongated, edible fruit botanically classified as a berry.",
            description = "Bananas grow in hanging clusters from large herbaceous flowering plants. They are prized globally for their rich potassium and quick energy yield.",
            characteristics = listOf("Peelable thick yellow skin", "Soft, sweet, starch-rich edible pulp", "Curved elongated geometry"),
            commonUses = listOf("Nutrient-dense breakfast and snacks", "Smoothies and athletic energy fuel", "Banana bread and dessert confections"),
            facts = listOf("Botanically, bananas are berries, while strawberries and raspberries are aggregate fruits.", "Bananas are naturally slightly radioactive because of their high potassium-40 isotope content."),
            similarObjects = listOf("Apple", "Orange")
        ),
        ObjectInformation(
            id = "bottle",
            name = "Bottle",
            category = "Household",
            iconEmoji = "🥤",
            shortDescription = "Rigid or semi-rigid container with a narrow neck for liquids.",
            description = "Bottles are fabricated from glass, stainless steel, or polymers to transport and preserve drinking water, beverages, and solutions securely.",
            characteristics = listOf("Narrow neck with sealed cap or stopper", "Watertight body structure", "Reusable or recyclable composition"),
            commonUses = listOf("Hydration and beverage storage", "Condiment and oil dispensing", "Chemical and pharmaceutical containment"),
            facts = listOf("Glass bottles were first produced in South East Asia around 100 BCE, and later blown by Roman glassmakers."),
            similarObjects = listOf("Cup", "Wine Glass")
        ),
        ObjectInformation(
            id = "cup",
            name = "Cup",
            category = "Household",
            iconEmoji = "☕",
            shortDescription = "Open container used to hold liquids for pouring or drinking.",
            description = "A cup or mug is designed with an open rim and often a handle, constructed from ceramic, glass, porcelain, or insulated steel.",
            characteristics = listOf("Open top cylindrical form", "Ergonomic side handle on mugs", "Heat-retaining thermal wall"),
            commonUses = listOf("Serving hot coffee, tea, and espresso", "Everyday beverage drinking", "Liquid measurement in culinary recipes"),
            facts = listOf("The earliest excavated drinking vessels were made from clay, animal horns, and hollowed wood dating back millennia."),
            similarObjects = listOf("Bottle", "Wine Glass", "Bowl")
        ),
        ObjectInformation(
            id = "book",
            name = "Book",
            category = "Household",
            iconEmoji = "📖",
            shortDescription = "Bound collection of printed, illustrated, or blank sheets.",
            description = "A book serves as an enduring medium for recording information in the form of writing or images, bound together inside protective covers.",
            characteristics = listOf("Bound spine with paper folios", "Protective hardcover or paperback binding", "Typographic printed chapters"),
            commonUses = listOf("Literature reading and education", "Reference manual and encyclopedic lookup", "Notebook journaling and sketching"),
            facts = listOf("The Diamond Sutra (868 CE) is the world's earliest known dated printed book using block printing."),
            similarObjects = listOf("Laptop")
        ),
        ObjectInformation(
            id = "chair",
            name = "Chair",
            category = "Household",
            iconEmoji = "🪑",
            shortDescription = "Piece of furniture with a raised surface supported by legs.",
            description = "Chairs are foundational furniture items designed for seating one individual, typically constructed with four legs and a supportive backrest.",
            characteristics = listOf("Horizontal seat pan and vertical backrest", "Three or four weight-bearing legs", "Ergonomic lumbar curvature"),
            commonUses = listOf("Desk work seating", "Dining and social gatherings", "Relaxation and posture support"),
            facts = listOf("Chairs were historically symbols of authority and status; ordinary citizens used benches and stools until the 16th century."),
            similarObjects = listOf("Couch", "Dining Table", "Bed")
        ),
        ObjectInformation(
            id = "couch",
            name = "Sofa / Couch",
            category = "Household",
            iconEmoji = "🛋️",
            shortDescription = "Comfortable upholstered piece of furniture for two or more people.",
            description = "A couch or sofa features spring or foam cushioning wrapped in fabric or leather, providing comfortable lounge seating in homes and offices.",
            characteristics = listOf("Long cushioned seat bench", "Upholstered armrests and back pillows", "Sturdy internal hardwood frame"),
            commonUses = listOf("Living room relaxation and reading", "Socializing and watching entertainment", "Temporary guest sleeping"),
            facts = listOf("The word 'couch' originated from the French verb 'coucher', which means to lie down."),
            similarObjects = listOf("Chair", "Bed")
        ),

        // Animals
        ObjectInformation(
            id = "dog",
            name = "Dog",
            category = "Animals",
            iconEmoji = "🐕",
            shortDescription = "Domesticated carnivorous mammal belonging to the canid family.",
            description = "Dogs were the first animals domesticated by humans over 20,000 years ago. They possess exceptional olfactory senses, agility, and social intelligence.",
            characteristics = listOf("Acute sense of smell and hearing", "Expressive tail and vocalizations", "Loyal, highly trainable pack instincts"),
            commonUses = listOf("Companion animal and household pet", "Assistance for individuals with disabilities", "Search, rescue, and security service"),
            facts = listOf("A dog's sense of smell is between 10,000 to 100,000 times more sensitive than a human's.", "Each dog's nose print is unique, similar to human fingerprints."),
            similarObjects = listOf("Cat", "Horse")
        ),
        ObjectInformation(
            id = "cat",
            name = "Cat",
            category = "Animals",
            iconEmoji = "🐈",
            shortDescription = "Small carnivorous mammal known for agility and companionship.",
            description = "Domestic cats are valued by humans for companionship and their innate ability to hunt rodents. They are renowned for their flexible bodies, night vision, and purring.",
            characteristics = listOf("Retractable sharp claws", "Exceptional balance and night vision", "Independent yet affectionate temperament"),
            commonUses = listOf("Beloved companion pet", "Natural pest and rodent controller"),
            facts = listOf("Cats can jump up to six times their height in a single bound.", "Ancient Egyptians revered cats, associating them with the goddess Bastet."),
            similarObjects = listOf("Dog", "Bird")
        ),
        ObjectInformation(
            id = "bird",
            name = "Bird",
            category = "Animals",
            iconEmoji = "🐦",
            shortDescription = "Warm-blooded feathered vertebrate with wings.",
            description = "Birds inhabit ecosystems across every continent on Earth, characterized by feathers, toothless beaked jaws, and high metabolic rates.",
            characteristics = listOf("Lightweight hollow bones", "Feather plumage providing aerodynamic lift", "Hard-shelled egg laying"),
            commonUses = listOf("Crucial seed dispersers and pollinators in wild ecology", "Companion pets (parrots, canaries)"),
            facts = listOf("Peregrine falcons can reach diving speeds over 240 mph (386 km/h).", "Hummingbirds are the only birds capable of flying backwards."),
            similarObjects = listOf("Cat")
        ),

        // Vehicles
        ObjectInformation(
            id = "car",
            name = "Car",
            category = "Vehicles",
            iconEmoji = "🚗",
            shortDescription = "Four-wheeled motor vehicle used for passenger transportation.",
            description = "Cars are powered by internal combustion engines or electric motors, utilizing advanced computer control units and active safety systems.",
            characteristics = listOf("Four pneumatic wheels with independent suspension", "Steering wheel and braking systems", "Aerodynamic steel and composite chassis"),
            commonUses = listOf("Daily commuting and urban transit", "Family road travel and errands", "Ridesharing and transport logistics"),
            facts = listOf("Karl Benz built the first modern automobile in 1886 with a single-cylinder two-stroke engine.", "Modern electric cars can accelerate from 0 to 60 mph in under 2 seconds."),
            similarObjects = listOf("Truck", "Bus", "Motorcycle", "Bicycle")
        ),
        ObjectInformation(
            id = "bicycle",
            name = "Bicycle",
            category = "Vehicles",
            iconEmoji = "🚲",
            shortDescription = "Human-powered or motor-assisted, pedal-driven vehicle.",
            description = "A bicycle has two wheels attached to a frame, one behind the other. It is the world's most energy-efficient mechanical mode of transportation.",
            characteristics = listOf("Diamond frame with handlebars and pedals", "Chain and gear drive system", "Pneumatic spoked wheels"),
            commonUses = listOf("Eco-friendly zero-emission commuting", "Cardiovascular fitness and sport", "Urban bike-share and courier transport"),
            facts = listOf("Bicycles are the most efficient means of human transport ever invented in terms of energy consumed per distance.", "There are over one billion bicycles in the world."),
            similarObjects = listOf("Motorcycle", "Car")
        ),
        ObjectInformation(
            id = "motorcycle",
            name = "Motorcycle",
            category = "Vehicles",
            iconEmoji = "🏍️",
            shortDescription = "Two or three-wheeled motor vehicle for agile road travel.",
            description = "Motorcycles deliver high power-to-weight ratios, offering nimble maneuvering through traffic and open highway touring.",
            characteristics = listOf("Handlebar controls with hand clutch and brake", "In-frame engine or electric drive unit", "High power-to-weight dynamic balance"),
            commonUses = listOf("Fast urban commuting", "Recreational touring and racing", "Rapid medical and courier delivery"),
            facts = listOf("Gottlieb Daimler and Wilhelm Maybach built the first petrol-driven motorcycle, the Reitwagen, in 1885."),
            similarObjects = listOf("Bicycle", "Car")
        ),

        // Accessories & Others
        ObjectInformation(
            id = "backpack",
            name = "Backpack",
            category = "Accessories",
            iconEmoji = "🎒",
            shortDescription = "Fabric bag carried on the back secured by two shoulder straps.",
            description = "Backpacks distribute heavy loads evenly across the shoulders and hips, engineered with durable ripstop nylon or canvas.",
            characteristics = listOf("Dual padded ergonomic shoulder straps", "Multiple compartmentalized zippered pouches", "Weather-resistant fabric construction"),
            commonUses = listOf("Carrying textbooks and school supplies", "Hiking, trekking, and outdoor exploration", "Commuter laptop and gear transport"),
            facts = listOf("The zippered backpack with nylon fabric was popularized in the 1960s by Dick Kelty and JanSport."),
            similarObjects = listOf("Handbag", "Suitcase")
        ),
        ObjectInformation(
            id = "clock",
            name = "Clock",
            category = "Household",
            iconEmoji = "⏰",
            shortDescription = "Instrument for measuring, keeping, and indicating the time.",
            description = "Clocks use quartz oscillators, atomic resonance, or mechanical escapements to track standard seconds, minutes, and hours accurately.",
            characteristics = listOf("Rotating hour, minute, and second hands or digital LCD", "Quartz or mechanical escapement timing movement", "Alarm and synchronization circuitry"),
            commonUses = listOf("Daily timekeeping and scheduling", "Morning wake-up alarms", "Decorative wall art and mantel display"),
            facts = listOf("Atomic clocks measure time using the resonant frequencies of cesium atoms, losing less than one second in 100 million years."),
            similarObjects = listOf("Cell Phone")
        ),
        ObjectInformation(
            id = "scissors",
            name = "Scissors",
            category = "Household",
            iconEmoji = "✂️",
            shortDescription = "Hand-operated shearing tool with pivoting metal blades.",
            description = "Scissors consist of a pair of sharpened metal blades pivoted so that the sharpened edges slide against each other when handles are closed.",
            characteristics = listOf("Pivoting first-class lever blades", "Ergonomic finger and thumb loops", "Hardened stainless steel cutting edges"),
            commonUses = listOf("Cutting paper, fabric, and craft materials", "Culinary food preparation and trimming", "Hairdressing and surgical applications"),
            facts = listOf("Scissors were invented around 3,000 to 4,000 years ago in ancient Mesopotamia."),
            similarObjects = listOf("Knife")
        ),
        ObjectInformation(
            id = "person",
            name = "Person",
            category = "Living",
            iconEmoji = "👤",
            shortDescription = "Human being (Homo sapiens).",
            description = "A person detected in the visual frame represents a human subject, identified by postural characteristics, facial contours, and bipedal form.",
            characteristics = listOf("Bipedal stance and biomechanics", "Expressive posture and gestures", "Complex social intelligence"),
            commonUses = listOf("Interaction, communication, creative endeavors, and collaboration"),
            facts = listOf("The human brain contains approximately 86 billion neurons, forming trillions of synaptic connections."),
            similarObjects = listOf("Teddy Bear")
        )
    ).associateBy { it.id.lowercase() }

    fun getObjectInfo(label: String): ObjectInformation {
        val key = label.trim().lowercase()
        return knowledgeMap[key] ?: createGenericObject(label)
    }

    fun getAllObjects(): List<ObjectInformation> {
        return knowledgeMap.values.toList()
    }

    fun getObjectsByCategory(category: String): List<ObjectInformation> {
        if (category.equals("All", ignoreCase = true)) return getAllObjects()
        return knowledgeMap.values.filter { it.category.equals(category, ignoreCase = true) }
    }

    private fun createGenericObject(label: String): ObjectInformation {
        val formatted = label.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        return ObjectInformation(
            id = label.lowercase(),
            name = formatted,
            category = "General",
            iconEmoji = "🔍",
            shortDescription = "Detected $formatted recognized by the ObjectLens on-device vision model.",
            description = "The $formatted is an object recognized by computer vision models trained on common real-world datasets.",
            characteristics = listOf("Identified with machine learning confidence", "Standard real-world physical object", "Recognized via on-device neural processing"),
            commonUses = listOf("Everyday interaction", "Utility and visual identification"),
            facts = listOf("Object detection locates both the class and bounding coordinates in real-time."),
            similarObjects = emptyList()
        )
    }
}
