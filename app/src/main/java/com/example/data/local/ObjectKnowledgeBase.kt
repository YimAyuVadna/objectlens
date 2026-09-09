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

enum class LabelTier { SPECIFIC_OBJECT, NEUTRAL, GENERIC }

data class ResolvedObjectInfo(
    val info: ObjectInformation,
    val confidence: Float,
    val tier: LabelTier = LabelTier.NEUTRAL
)

object ObjectKnowledgeBase {

    val categories = listOf(
        "All",
        "Electronics",
        "Kitchenware",
        "Food",
        "Work",
        "Clothing",
        "Household",
        "Tools",
        "Sports",
        "Hobbies",
        "Nature",
        "Vehicles",
        "Personal"
    )

    private val knowledgeList: List<ObjectInformation> = listOf(
        // --- 1. Electronics & Computing ---
        ObjectInformation(
            id = "laptop",
            name = "Laptop Computer",
            category = "Electronics",
            iconEmoji = "💻",
            shortDescription = "Portable personal computing device with integrated display, keyboard, and battery.",
            description = "A laptop computer combines all the desktop components—display screen, speakers, keyboard, trackpad, and internal processor—into a portable, battery-powered clamshell enclosure.",
            characteristics = listOf("Clamshell folding hinge design", "High-resolution display panel", "Integrated scissor-switch keyboard & trackpad", "Rechargeable high-capacity lithium battery"),
            commonUses = listOf("Software engineering & programming", "Academic research & office productivity", "Video conferencing & digital communications", "Photo and video content creation"),
            facts = listOf("The Osborne 1, released in April 1981, is recognized as the world's first commercially successful portable microcomputer.", "Modern laptop solid-state drives (NVMe SSDs) transfer data at over 7,000 MB per second."),
            similarObjects = listOf("Computer Monitor", "Keyboard", "Computer Mouse", "Smartphone")
        ),
        ObjectInformation(
            id = "smartphone",
            name = "Smartphone",
            category = "Electronics",
            iconEmoji = "📱",
            shortDescription = "Handheld mobile computer combining cellular telephone communications with high-end computing.",
            description = "A modern smartphone features an edge-to-edge capacitive touch display, multi-lens computational cameras, high-speed 5G connectivity, and neural engine mobile processors.",
            characteristics = listOf("OLED or AMOLED multi-touch glass screen", "Multi-lens optical & digital camera array", "Biometric facial recognition or fingerprint scanner", "Compact pocket-sized chassis"),
            commonUses = listOf("Instant voice, text, and video calling", "Mobile web browsing and application access", "Digital contactless banking and payments", "High-resolution photography and video capture"),
            facts = listOf("The first smartphone, the IBM Simon Personal Communicator, was unveiled in 1992 and featured a resistive touchscreen.", "Today's smartphones have millions of times more computing power than the Apollo 11 guidance computers."),
            similarObjects = listOf("Tablet", "Laptop Computer", "Smartwatch")
        ),
        ObjectInformation(
            id = "tablet",
            name = "Tablet / iPad",
            category = "Electronics",
            iconEmoji = "📟",
            shortDescription = "Flat, thin portable computer with a large touchscreen interface operated by fingers or stylus.",
            description = "A tablet computer bridges the gap between smartphones and laptops, offering an immersive digital canvas for sketching, reading, web browsing, and media consumption.",
            characteristics = listOf("Large capacitive glass touchscreen (8 to 13 inches)", "Ultra-slim bezel and lightweight chassis", "Support for active pressure-sensitive styluses", "Front and rear camera modules"),
            commonUses = listOf("Digital illustration and graphic design", "E-book and academic paper reading", "Streaming movies and mobile gaming", "Point-of-sale terminal operation"),
            facts = listOf("Apple's introduction of the iPad in 2010 catalyzed the modern tablet market, selling over 300,000 units on its first launch day."),
            similarObjects = listOf("Smartphone", "Laptop Computer", "Computer Monitor")
        ),
        ObjectInformation(
            id = "keyboard",
            name = "Computer Keyboard",
            category = "Electronics",
            iconEmoji = "⌨️",
            shortDescription = "Alphanumeric input peripheral utilizing physical or membrane key switches.",
            description = "A computer keyboard translates mechanical keystrokes into electrical binary input codes, allowing users to enter text, execute system commands, and navigate interfaces.",
            characteristics = listOf("Standardized alphanumeric key layout (QWERTY, AZERTY)", "Mechanical, membrane, or scissor-switch actuators", "Tactile feedback and key travel distance", "USB wired or Bluetooth wireless connectivity"),
            commonUses = listOf("Text typing, writing, and coding", "Gaming controls and hotkey shortcut execution", "Data entry and administrative work"),
            facts = listOf("Christopher Latham Sholes patented the QWERTY typewriter layout in 1878 to physically prevent typewriter mechanical hammers from clashing."),
            similarObjects = listOf("Computer Mouse", "Laptop Computer", "Tablet / iPad")
        ),
        ObjectInformation(
            id = "mouse",
            name = "Computer Mouse",
            category = "Electronics",
            iconEmoji = "🖱️",
            shortDescription = "Handheld pointing peripheral that detects two-dimensional surface motion to guide a digital cursor.",
            description = "A computer mouse converts hand movements across a surface into directional coordinates on a graphical user interface, featuring primary buttons and a scroll wheel.",
            characteristics = listOf("Optical LED or laser tracking sensor", "Left, right, and middle clickable tactile buttons", "Rotary scroll wheel for vertical navigation", "Ergonomic palm or claw grip contoured casing"),
            commonUses = listOf("GUI desktop navigation and window management", "Precision graphic design and 3D modeling", "Competitive PC gaming aiming and controls"),
            facts = listOf("Douglas Engelbart invented the computer mouse in 1964 at SRI International; the original prototype was carved from wood with two perpendicular metal wheels."),
            similarObjects = listOf("Computer Keyboard", "Laptop Computer")
        ),
        ObjectInformation(
            id = "monitor",
            name = "Computer Monitor / Display",
            category = "Electronics",
            iconEmoji = "🖥️",
            shortDescription = "Electronic visual display panel that outputs video signals from a connected computer.",
            description = "A computer monitor renders high-definition graphics, text, and user interfaces using IPS LCD or OLED panel technologies, often mounted on adjustable desktop stands.",
            characteristics = listOf("Flat or curved panel display (24 to 34+ inches)", "High refresh rates (60Hz to 240Hz+)", "DisplayPort and HDMI digital video inputs", "Anti-glare matte or glossy screen coating"),
            commonUses = listOf("Desktop workstation visual workspace", "High-fidelity gaming and cinema viewing", "Dual-screen multitasking for productivity"),
            facts = listOf("Early computer displays were monochrome cathode-ray tubes (CRTs) displaying green phosphor characters on a black raster background."),
            similarObjects = listOf("Television", "Laptop Computer")
        ),
        ObjectInformation(
            id = "television",
            name = "Television / Smart TV",
            category = "Electronics",
            iconEmoji = "📺",
            shortDescription = "Large format audiovisual appliance designed for broadcast viewing and streaming entertainment.",
            description = "Modern smart televisions feature ultra-high-definition (4K/8K) OLED or Mini-LED display matrices with integrated operating systems that connect to wireless internet streaming services.",
            characteristics = listOf("Large format screen (40 to 85+ inches)", "Integrated stereo or Dolby Atmos speaker arrays", "Smart TV operating system with streaming apps", "Multiple HDMI eARC audio/video ports"),
            commonUses = listOf("Home theater movie and television streaming", "Console gaming on large-format screens", "Family viewing and communal entertainment"),
            facts = listOf("The first all-electronic television transmission was accomplished in San Francisco on September 7, 1927, by 21-year-old inventor Philo Farnsworth."),
            similarObjects = listOf("Computer Monitor / Display", "Remote Control")
        ),
        ObjectInformation(
            id = "headphones",
            name = "Headphones / Earbuds",
            category = "Electronics",
            iconEmoji = "🎧",
            shortDescription = "Pair of miniature electroacoustic transducers worn over or inside the ears for private audio listening.",
            description = "Headphones deliver localized sound reproduction using dynamic or planar magnetic drivers. Modern pairs feature active noise cancellation (ANC) and high-resolution wireless audio.",
            characteristics = listOf("Over-ear cushioned earcups or in-ear silicone tips", "Active noise canceling microphones and DSP chip", "Rechargeable battery and Bluetooth wireless receiver", "Padded adjustable headband or pocket charging case"),
            commonUses = listOf("Immersive music and podcast listening", "Private voice calling and video meetings", "Noise isolation during travel and work commutes"),
            facts = listOf("Nathaniel Baldwin invented the first modern pair of audio headphones by hand in his kitchen in Utah in 1910, selling early pairs to the United States Navy."),
            similarObjects = listOf("Audio Speaker", "Smartphone")
        ),
        ObjectInformation(
            id = "speaker",
            name = "Audio Speaker",
            category = "Electronics",
            iconEmoji = "🔊",
            shortDescription = "Electroacoustic transducer that converts electrical audio signals into audible sound waves.",
            description = "Speakers utilize magnetic voice coils to vibrate paper, Kevlar, or polypropylene cones, creating atmospheric sound waves for rooms, desktops, or portable outdoor listening.",
            characteristics = listOf("Acoustic speaker enclosure with woofer and tweeter", "Wired AUX/RCA or wireless Bluetooth connectivity", "Water-resistant mesh or wooden cabinet housing", "Integrated rechargeable battery on portable models"),
            commonUses = listOf("Room-filling music playback", "Home theater audio and speech clarity", "Outdoor gatherings, parties, and desktop audio"),
            facts = listOf("Alexander Graham Bell patented the first electric loudspeaker design as an integral component of his telephone in 1876."),
            similarObjects = listOf("Headphones / Earbuds", "Television / Smart TV")
        ),
        ObjectInformation(
            id = "camera",
            name = "Digital Camera",
            category = "Electronics",
            iconEmoji = "📷",
            shortDescription = "Optical instrument that captures still photographs and videos by recording light onto an electronic sensor.",
            description = "A digital camera focuses photons through high-precision glass lens elements onto a CMOS or CCD image sensor, converting optical reality into digital pixel files.",
            characteristics = listOf("Interchangeable or fixed glass optical lens", "High-resolution CMOS full-frame or APS-C sensor", "Tactile mode dials, shutter button, and optical/electronic viewfinder", "SD card slot and ergonomic rubberized handgrip"),
            commonUses = listOf("Professional portrait and landscape photography", "Cinematography and high-frame-rate video recording", "Photojournalism, travel, and family documentation"),
            facts = listOf("The first digital camera was built in 1975 by Steven Sasson at Eastman Kodak; it weighed 8 pounds (3.6 kg) and recorded 0.01 megapixel images onto a cassette tape."),
            similarObjects = listOf("Smartphone", "Tripod")
        ),
        ObjectInformation(
            id = "smartwatch",
            name = "Watch / Smartwatch",
            category = "Electronics",
            iconEmoji = "⌚",
            shortDescription = "Wearable computing or mechanical timepiece worn securely around the wrist.",
            description = "Wristwatches range from intricate mechanical gear movements to modern smartwatches equipped with optical heart rate sensors, GPS antennas, and touch displays.",
            characteristics = listOf("Curved glass or sapphire crystal dial cover", "Silicone, leather, or stainless steel link wrist strap", "Optical photoplethysmography (PPG) health sensors", "Water-resistant stainless steel or titanium case"),
            commonUses = listOf("Instant time and date keeping", "Cardiovascular fitness and workout tracking", "Haptic notifications and wrist payments"),
            facts = listOf("Patek Philippe created the first known wristwatch in 1868, intended initially as an ornamental bracelet for Countess Koscowicz of Hungary."),
            similarObjects = listOf("Smartphone", "Headphones / Earbuds")
        ),
        ObjectInformation(
            id = "remote",
            name = "Remote Control",
            category = "Electronics",
            iconEmoji = "🕹️",
            shortDescription = "Handheld electronic transmitter used to operate devices wirelessly from a distance.",
            description = "Remote controls send encoded infrared (IR) light or Bluetooth RF command sequences to televisions, audio receivers, streaming sticks, and air conditioners.",
            characteristics = listOf("Tactile rubber keypad with numbered and navigation buttons", "Front-facing infrared LED transmitter or Bluetooth module", "Battery compartment accommodating AAA or AA cells", "Lightweight ergonomic plastic casing"),
            commonUses = listOf("Adjusting audio volume and changing television channels", "Navigating streaming app menus and settings", "Operating home theater equipment"),
            facts = listOf("Zenith Radio Corporation introduced the first wireless TV remote in 1955 called 'Flashmatic', which shone visible light beams onto photo cells on the TV corners."),
            similarObjects = listOf("Television / Smart TV", "Smartphone")
        ),

        // --- 2. Kitchenware & Dining ---
        ObjectInformation(
            id = "cup",
            name = "Coffee Cup / Mug",
            category = "Kitchenware",
            iconEmoji = "☕",
            shortDescription = "Open container with an ergonomic side handle used for drinking hot coffee, tea, or cocoa.",
            description = "A coffee cup or mug is designed with heat-retaining ceramic, porcelain, glass, or double-walled stainless steel to keep hot beverages insulated while providing a comfortable handgrip.",
            characteristics = listOf("Cylindrical body with open drinking rim", "Heat-insulating side loop handle", "Glazed ceramic, stoneware, or stainless steel construction", "Stable flat circular base"),
            commonUses = listOf("Drinking hot coffee, tea, espresso, and latte", "Serving hot chocolate, soup, and cider", "Desktop hydration and morning routine companion"),
            facts = listOf("Excavated ceramic drinking cups date back thousands of years to ancient Greece and Neolithic China."),
            similarObjects = listOf("Water Bottle", "Glass Tumbler / Wine Glass", "Dinner Plate / Tableware")
        ),
        ObjectInformation(
            id = "bottle",
            name = "Water Bottle / Flask",
            category = "Kitchenware",
            iconEmoji = "🥤",
            shortDescription = "Rigid, portable liquid container with a narrow neck and leakproof closure.",
            description = "Water bottles transport drinking fluids securely using vacuum-insulated double-wall stainless steel, shatterproof Tritan plastic, or borosilicate glass with screw-on caps.",
            characteristics = listOf("Narrow neck with leak-proof cap, straw, or spout", "Vacuum-insulated double walls on thermal flasks", "Watertight silicone gasket seal", "Cylindrical body sized to fit cup holders and backpack sleeves"),
            commonUses = listOf("Daily personal hydration and water carrying", "Keeping cold drinks icy during sports and workouts", "Maintaining hot tea or coffee on long commutes"),
            facts = listOf("Vacuum-insulated bottles rely on the Dewar flask principle invented by Scottish scientist Sir James Dewar in 1892, using a vacuum layer to stop conductive and convective heat transfer."),
            similarObjects = listOf("Coffee Cup / Mug", "Glass Tumbler / Wine Glass")
        ),
        ObjectInformation(
            id = "glass",
            name = "Glass Tumbler / Wine Glass",
            category = "Kitchenware",
            iconEmoji = "🍷",
            shortDescription = "Transparent drinking vessel crafted from blown silica glass or crystal.",
            description = "Drinking glasses range from straight-sided everyday water tumblers to stemware wine goblets shaped to aerate beverages and concentrate delicate aromatic bouquets.",
            characteristics = listOf("Transparent non-porous silica glass or crystal", "Smooth rounded drinking rim", "Stem and circular foot on wine glasses", "Weighted base on rocks and highball tumblers"),
            commonUses = listOf("Drinking water, juice, iced tea, and cocktails", "Serving and savoring red, white, or sparkling wine", "Dining table service and formal settings"),
            facts = listOf("Glass blowing was invented around the 1st century BCE by Syrian craftsmen along the Mediterranean coast, revolutionizing container production."),
            similarObjects = listOf("Coffee Cup / Mug", "Water Bottle")
        ),
        ObjectInformation(
            id = "plate",
            name = "Dinner Plate / Tableware",
            category = "Kitchenware",
            iconEmoji = "🍽️",
            shortDescription = "Flat or shallow concave dishware on which cooked meals and courses are served.",
            description = "Dinner plates feature a broad flat well surrounded by a raised rim to prevent food spillage, manufactured from glazed ceramic, porcelain, stoneware, or tempered glass.",
            characteristics = listOf("Flat broad surface with slightly raised rim", "Glazed smooth non-porous ceramic finish", "Microwave and dishwasher-safe composition", "Standard 10 to 11-inch diameter for dinner service"),
            commonUses = listOf("Serving main meal courses, meat, and pasta", "Plating dining courses and shared appetizers", "Formal dinner table setting"),
            facts = listOf("Prior to the widespread adoption of ceramic plates in European courts during the 16th century, diners commonly ate from 'trenchers'—slabs of stale hard bread."),
            similarObjects = listOf("Bowl", "Cutlery & Utensils", "Coffee Cup / Mug")
        ),
        ObjectInformation(
            id = "bowl",
            name = "Bowl",
            category = "Kitchenware",
            iconEmoji = "🥣",
            shortDescription = "Round open container with curved sides used for holding soups, salads, and grains.",
            description = "A bowl is characterized by a hemispherical or curved vessel design that holds liquids, broths, and loose ingredients securely without sloshing over the edges.",
            characteristics = listOf("Deep concave hemispherical body", "Continuous curved interior with no sharp corners", "Glazed ceramic, porcelain, wood, or stainless steel", "Stable footed base ring"),
            commonUses = listOf("Eating cereal, oatmeal, soups, and broths", "Serving ramen, pasta, salads, and grain bowls", "Culinary mixing and meal prep"),
            facts = listOf("Bowls are among the oldest human culinary artifacts, with ancient ceramic examples dating back over 18,000 years discovered in Jiangxi, China."),
            similarObjects = listOf("Dinner Plate / Tableware", "Coffee Cup / Mug")
        ),
        ObjectInformation(
            id = "cutlery",
            name = "Cutlery & Utensils",
            category = "Kitchenware",
            iconEmoji = "🍴",
            shortDescription = "Handheld eating and serving instruments including forks, spoons, and table knives.",
            description = "Cutlery comprises stainless steel dining implements—tined forks for spearing, cupped spoons for liquids, and serrated knives for portioning cooked food.",
            characteristics = listOf("18/10 stainless steel or polished silver composition", "Ergonomic balanced handles", "Sharp bevels on knives and prongs on forks", "Dishwasher-safe food-grade metallurgy"),
            commonUses = listOf("Consuming meals, soups, meats, and desserts", "Dining table place settings", "Food preparation and plate serving"),
            facts = listOf("While knives and spoons have been used since prehistoric times, table forks were not widely adopted in Western Europe until the late Renaissance in 16th-century Italy."),
            similarObjects = listOf("Dinner Plate / Tableware", "Bowl", "Cookware & Kitchen Tools")
        ),
        ObjectInformation(
            id = "cookware",
            name = "Cookware & Kitchen Tools",
            category = "Kitchenware",
            iconEmoji = "🍳",
            shortDescription = "Pans, pots, skillets, and spatulas engineered for culinary heat transfer and food preparation.",
            description = "Cookware includes cast iron skillets, stainless steel saucepans, Dutch ovens, and silicone spatulas designed to conduct thermal energy evenly across cooking surfaces.",
            characteristics = listOf("High thermal conductivity (aluminum, copper, cast iron)", "Nonstick coating or seasoned patina surface", "Heat-resistant riveted handles", "Deep walls on pots, shallow flared rims on skillets"),
            commonUses = listOf("Searing meats, sauteing vegetables, and frying eggs", "Simmering sauces, boiling pasta, and braising stews", "Stirring, flipping, and whisking ingredients"),
            facts = listOf("Cast iron cookware improves with age as polymerized cooking oils create a durable, naturally non-stick seasoning layer bonded to the iron surface."),
            similarObjects = listOf("Cutlery & Utensils", "Kitchen Appliance")
        ),
        ObjectInformation(
            id = "appliance",
            name = "Kitchen Appliance",
            category = "Kitchenware",
            iconEmoji = "⚡",
            shortDescription = "Electrical device designed to automate cooking, heating, blending, or food preservation.",
            description = "Countertop kitchen appliances include blenders, air fryers, toasters, microwaves, and electric kettles that use electric motors and heating coils to streamline meal preparation.",
            characteristics = listOf("Electric heating element or high-RPM motor", "Digital timer or mechanical control dials", "Heavy-duty food-grade housing (stainless or BPA-free plastic)", "Automatic shut-off and safety lock switches"),
            commonUses = listOf("Rapid air frying, baking, and microwaving", "Pureeing smoothies, crushing ice, and blending soups", "Boiling water in seconds for tea and pour-over coffee"),
            facts = listOf("The microwave oven was discovered accidentally in 1945 by Percy Spencer while testing an active radar magnetron, which melted a peanut chocolate bar in his pocket."),
            similarObjects = listOf("Cookware & Kitchen Tools", "Coffee Cup / Mug")
        ),

        // --- 3. Food, Groceries & Produce ---
        ObjectInformation(
            id = "apple",
            name = "Apple",
            category = "Food",
            iconEmoji = "🍎",
            shortDescription = "Crisp, sweet or tart edible pome fruit grown on apple trees.",
            description = "Apples are widely cultivated fruits featuring smooth glossy skin ranging from bright red to golden yellow and green, with sweet, fiber-rich juicy flesh surrounding a seed core.",
            characteristics = listOf("Spherical shape with indented stem cavity", "Thin glossy skin (red, green, or bi-color)", "Firm, crisp, juicy interior flesh", "Star-shaped central seed carpel"),
            commonUses = listOf("Fresh raw snacking and sliced school lunches", "Baking pies, tarts, pastries, and applesauce", "Pressing sweet cider and brewing apple cider vinegar"),
            facts = listOf("Over 7,500 varieties of apples are grown worldwide; an apple float in water because 25% of its total volume is air."),
            similarObjects = listOf("Banana", "Fresh Produce", "Food & Meals")
        ),
        ObjectInformation(
            id = "banana",
            name = "Banana",
            category = "Food",
            iconEmoji = "🍌",
            shortDescription = "Elongated, curved tropical edible fruit growing in hanging hanging clusters.",
            description = "Bananas are nutritious fruits encased in a thick, easily peeled peel that turns from green to bright yellow and speckled brown as starches convert into sweet sugars.",
            characteristics = listOf("Elongated curved crescent shape", "Protective fibrous peel that peels in strips", "Soft, creamy, pale yellow edible pulp", "Grows in large hanging hands from giant herbaceous plants"),
            commonUses = listOf("On-the-go nutritious snacking and pre-workout fuel", "Blending into fruit smoothies and protein shakes", "Baking moist banana bread, pancakes, and desserts"),
            facts = listOf("Botanically, a banana is classified as a berry that grows on giant herbaceous flowering plants rather than woody trees."),
            similarObjects = listOf("Apple", "Fresh Produce")
        ),
        ObjectInformation(
            id = "produce",
            name = "Fresh Produce",
            category = "Food",
            iconEmoji = "🥗",
            shortDescription = "Fresh agricultural crops including edible vegetables, salad greens, and fruits.",
            description = "Produce encompasses fresh farm-grown vegetables and fruits—such as tomatoes, bell peppers, carrots, avocados, and broccoli—rich in vitamins, minerals, and dietary antioxidants.",
            characteristics = listOf("Natural organic shapes, vibrant skins, and textures", "High water content and crisp cellular turgor", "Nutrient-dense with dietary fiber and phytochemicals", "Requires refrigeration or cool storage to preserve freshness"),
            commonUses = listOf("Preparing crisp garden salads and raw snack platters", "Cooking savory soups, stir-fries, and roasted sides", "Juicing and wholesome culinary cooking"),
            facts = listOf("Though culinarily prepared as vegetables, tomatoes, cucumbers, avocados, and bell peppers are all botanically classified as fruits because they bear seeds."),
            similarObjects = listOf("Apple", "Banana", "Food & Meals")
        ),
        ObjectInformation(
            id = "food",
            name = "Food & Meals",
            category = "Food",
            iconEmoji = "🍕",
            shortDescription = "Cooked, baked, or prepared edible dishes providing nourishment and culinary enjoyment.",
            description = "Prepared foods include sandwiches, artisanal breads, pizza, pasta dishes, and wholesome meals prepared by combining proteins, carbohydrates, and seasoning for daily sustenance.",
            characteristics = listOf("Cooked, baked, or assembled culinary presentation", "Appetizing savory or sweet aroma and flavors", "Balanced combination of macronutrients (proteins, fats, carbs)", "Served hot, warm, or chilled on dishware"),
            commonUses = listOf("Daily dietary breakfast, lunch, and dinner nourishment", "Social dining, celebrations, and restaurant culinary enjoyment", "Quick snacking and meal fueling"),
            facts = listOf("The human tongue has approximately 10,000 taste buds that can detect five distinct primary taste sensations: sweet, sour, salty, bitter, and savory (umami)."),
            similarObjects = listOf("Fresh Produce", "Apple", "Banana", "Dinner Plate / Tableware")
        ),

        // --- 4. Work, Study & Stationery ---
        ObjectInformation(
            id = "pen",
            name = "Pen / Writing Tool",
            category = "Work",
            iconEmoji = "🖊️",
            shortDescription = "Handheld writing instrument that dispenses ink onto paper surfaces.",
            description = "Pens utilize rolling tungsten carbide balls (ballpoint and gel) or capillary nibs (fountain pens and fine-liners) to distribute ink smoothly for writing and sketching.",
            characteristics = listOf("Slender cylindrical barrel with pocket clip", "Precision rolling ball or metal nib tip", "Quick-drying black, blue, or colored ink cartridge", "Click-retractable mechanism or removable cap"),
            commonUses = listOf("Handwriting notes, letters, and signatures", "Journaling, creative writing, and planning", "Artistic sketching, drawing, and line illustration"),
            facts = listOf("László Bíró patented the first modern ballpoint pen in 1938; the British Royal Air Force widely adopted them because they didn't leak at high flight altitudes."),
            similarObjects = listOf("Notebook / Paper Stationery", "Scissors / Cutting Tool")
        ),
        ObjectInformation(
            id = "notebook",
            name = "Notebook / Paper Stationery",
            category = "Work",
            iconEmoji = "📓",
            shortDescription = "Collection of blank, ruled, or graph paper sheets bound together for writing notes.",
            description = "A notebook provides a physical analog repository for ideas, meeting minutes, journaling, and sketches, bound with spiral wire or stitched hardcovers.",
            characteristics = listOf("Ruled, grid, dotted, or blank paper pages", "Spiral wire coil or casebound stitched spine", "Durable cardstock, leatherette, or plastic cover", "Acid-free ink-resistant paper weight"),
            commonUses = listOf("Academic lecture note-taking and studying", "Daily work meeting logs and action task lists", "Personal bullet journaling and creative drafting"),
            facts = listOf("Thomas Edison was a voracious notebook user, compiling over 5 million pages of laboratory notes, sketches, and patent ideas in thousands of notebooks across his life."),
            similarObjects = listOf("Book / Publication", "Pen / Writing Tool")
        ),
        ObjectInformation(
            id = "book",
            name = "Book / Publication",
            category = "Work",
            iconEmoji = "📖",
            shortDescription = "Bound printed volume consisting of illustrated or textual pages enclosed within covers.",
            description = "A book serves as an enduring medium for recording and transmitting literature, scientific knowledge, historical accounts, and stories across generations.",
            characteristics = listOf("Bound spine with collated folios of printed text", "Rigid hardcover or flexible paperback binding", "Organized chapters, index, and numbered pages", "Typography formatted for extended legibility"),
            commonUses = listOf("Literary fiction reading and recreational enjoyment", "Academic study, reference, and university textbooks", "Historical research, philosophy, and self-education"),
            facts = listOf("The Diamond Sutra, a Buddhist text printed in China in 868 CE during the Tang dynasty, is the world's earliest known complete dated printed book."),
            similarObjects = listOf("Notebook / Paper Stationery")
        ),
        ObjectInformation(
            id = "scissors",
            name = "Scissors / Cutting Tool",
            category = "Work",
            iconEmoji = "✂️",
            shortDescription = "Hand-operated shearing instrument consisting of a pair of pivoted crossing blades.",
            description = "Scissors employ mechanical class-1 lever action to bring two sharpened stainless steel blades together, shearing through paper, cardboard, fabrics, and craft materials cleanly.",
            characteristics = listOf("Pair of pivoted sharpened stainless steel blades", "Molded ergonomic finger and thumb loop handles", "Central pivot screw adjusting blade tension", "Safety rounded or precision pointed tip"),
            commonUses = listOf("Cutting paper, cardboard, wrapping, and craft sheets", "Opening shipping boxes, packaging, and plastic bags", "Tailoring fabrics and trimming craft materials"),
            facts = listOf("Ancient Romans created cross-blade pivoted bronze and iron scissors around 100 CE, evolving from early spring-shears used in ancient Mesopotamia."),
            similarObjects = listOf("Pen / Writing Tool", "Desk Supplies / Organizers")
        ),
        ObjectInformation(
            id = "stationery",
            name = "Desk Supplies / Organizers",
            category = "Work",
            iconEmoji = "📎",
            shortDescription = "Office fasteners and desktop tools including staplers, tape, clips, and erasers.",
            description = "Desk supplies encompass desktop staplers, paper clips, Scotch tape dispensers, sticky notes, and organizers engineered to manage physical paperwork and desk workflows.",
            characteristics = listOf("Compact metal and polymer construction", "Mechanisms for binding, fastening, or dispensing", "Designed for desktop or drawer organization", "Durable spring-loaded or adhesive designs"),
            commonUses = listOf("Securing paper packets with staples or paper clips", "Taping documents, packages, and receipts", "Erasing pencil marks and organizing desktop pens"),
            facts = listOf("The Norwegian patent clerk Johan Vaaler was long credited with inventing the paperclip in 1899, though the modern double-oval gem paperclip was never patented by him."),
            similarObjects = listOf("Pen / Writing Tool", "Notebook / Paper Stationery")
        ),

        // --- 5. Clothing, Footwear & Accessories ---
        ObjectInformation(
            id = "shoe",
            name = "Shoes / Sneakers",
            category = "Clothing",
            iconEmoji = "👟",
            shortDescription = "Outer protective footwear covering the human foot with a durable sole and upper.",
            description = "Shoes protect feet from terrain impacts while providing arch support, cushioning, and grip. Athletic sneakers feature breathable knit uppers and EVA foam midsole cushioning.",
            characteristics = listOf("Treaded rubber outsole for surface traction", "Cushioned EVA foam or air-pocket midsole", "Breathable mesh, canvas, or leather upper", "Lacing system, heel counter, and padded tongue"),
            commonUses = listOf("Everyday walking, commuting, and urban mobility", "Running, athletic sports, and gym workouts", "Protecting feet from abrasion, cold, and rough terrain"),
            facts = listOf("The Areni-1 leather shoe, discovered in a cave in Armenia in 2008, is the world's oldest known leather shoe, preserved for over 5,500 years with grass insulation."),
            similarObjects = listOf("Shirt / Top", "Pants / Jeans")
        ),
        ObjectInformation(
            id = "shirt",
            name = "Shirt / Top",
            category = "Clothing",
            iconEmoji = "👕",
            shortDescription = "Upper-body textile garment including t-shirts, button-downs, polo shirts, and sweaters.",
            description = "Shirts cover the torso and arms, tailored from breathable cotton, wool, linen, or moisture-wicking synthetic fabrics designed for casual comfort or professional attire.",
            characteristics = listOf("Woven cotton, linen, polyester, or knit fabric", "Crewneck, v-neck, or buttoned point collar", "Short or long sleeves with hemmed cuffs", "Tailored fit across the shoulders and chest"),
            commonUses = listOf("Daily casual wear and base layering", "Professional office and business attire", "Gym workout moisture management"),
            facts = listOf("The t-shirt evolved in the early 20th century from one-piece undergarments called union suits, issued by the US Navy during the 1898 Spanish-American War."),
            similarObjects = listOf("Pants / Jeans", "Shoes / Sneakers", "Jacket / Outerwear")
        ),
        ObjectInformation(
            id = "pants",
            name = "Pants / Jeans",
            category = "Clothing",
            iconEmoji = "👖",
            shortDescription = "Lower-body garment extending from the waist to the ankles, covering each leg separately.",
            description = "Pants include denim jeans, chinos, joggers, and dress trousers tailored from durable cotton twill or denim to provide leg protection, mobility, and utilitarian pockets.",
            characteristics = listOf("Separate dual leg coverings reaching the ankle or knee", "Waistband with belt loops and button/zipper fly closure", "Front slash and rear patch pockets", "Durable denim, twill, or athletic stretch weave"),
            commonUses = listOf("Everyday casual wear and work attire", "Protection against outdoor weather and abrasion", "Holding wallet, keys, and phone in pockets"),
            facts = listOf("Denim blue jeans were patented in May 1873 by Levi Strauss and tailor Jacob Davis, who reinforced pocket corners with copper rivets for gold miners."),
            similarObjects = listOf("Shirt / Top", "Shoes / Sneakers")
        ),
        ObjectInformation(
            id = "jacket",
            name = "Jacket / Outerwear",
            category = "Clothing",
            iconEmoji = "🧥",
            shortDescription = "Protective outer garment worn over clothing for warmth and weather resistance.",
            description = "Jackets encompass winter puffers, leather biker jackets, denim truckers, and raincoats designed with insulated linings and water-resistant shells to shield against elements.",
            characteristics = listOf("Front zipper or button closure from hem to collar", "Insulated down, fleece, or wool thermal lining", "Windproof or water-resistant exterior shell", "Exterior hand-warmer and interior chest pockets"),
            commonUses = listOf("Shielding the body against cold wind, rain, and snow", "Layering over casual and formal outfits", "Outdoor hiking, travel, and motorcycle riding"),
            facts = listOf("The trench coat was originally developed by Thomas Burberry in 1901 using waterproof gabardine fabric for British army officers in the trenches of World War I."),
            similarObjects = listOf("Shirt / Top", "Pants / Jeans")
        ),
        ObjectInformation(
            id = "hat",
            name = "Hat / Cap",
            category = "Clothing",
            iconEmoji = "🧢",
            shortDescription = "Head covering worn for sun shade, thermal insulation, sports, or style.",
            description = "Headwear includes curved-visor baseball caps, knit winter beanies, and wide-brim sun hats designed to shield eyes from glare and protect the scalp from sun and cold.",
            characteristics = listOf("Curved front brim or 360-degree sun visor", "Fitted, snapback, or stretch-fit crown", "Knit wool, acrylic, or structured cotton twill", "Eyelet ventilation holes or embroidered logos"),
            commonUses = listOf("Shading the face and eyes from bright sunlight", "Thermal warmth for the head and ears during winter", "Team sports, athletics, and fashion accessory"),
            facts = listOf("The modern baseball cap with a structured crown and stiff visor was introduced in 1954 by New Era as the iconic 59FIFTY official on-field cap."),
            similarObjects = listOf("Shirt / Top", "Eyeglasses / Sunglasses")
        ),
        ObjectInformation(
            id = "glasses",
            name = "Eyeglasses / Sunglasses",
            category = "Clothing",
            iconEmoji = "🕶️",
            shortDescription = "Frames bearing optical lenses worn in front of the eyes for vision correction or UV glare protection.",
            description = "Eyeglasses feature prescription ophthalmic lenses or polarized dark UV-absorbing sunglass lenses held in acetate, metal, or titanium frames resting on the bridge of the nose.",
            characteristics = listOf("Pair of prescription or tinted polarized optical lenses", "Acetate, titanium, or wire metal frame chassis", "Nose pads for bridge support and temple arms resting on ears", "Integrated UV400 radiation filtering"),
            commonUses = listOf("Correcting myopia, hyperopia, and astigmatism", "Protecting eyes from bright sunlight and ultraviolet rays", "Reducing digital eye strain from computer screens"),
            facts = listOf("Inuit peoples carved walrus ivory snow goggles with narrow horizontal slits thousands of years ago to block blinding Arctic snow glare."),
            similarObjects = listOf("Hat / Cap", "Watch / Smartwatch")
        ),
        ObjectInformation(
            id = "backpack",
            name = "Backpack / Bag",
            category = "Clothing",
            iconEmoji = "🎒",
            shortDescription = "Cloth or leather sack carried on the back secured with two shoulder straps.",
            description = "Backpacks distribute heavy loads across the back and shoulders ergonomically, featuring padded straps, padded laptop sleeves, and zippered accessory compartments.",
            characteristics = listOf("Dual padded adjustable shoulder straps", "Heavy-duty ripstop nylon, canvas, or leather fabric", "Padded internal compartment for laptops and tablets", "Water bottle side pockets and zippered exterior pockets"),
            commonUses = listOf("Carrying laptops, books, and chargers to school and work", "Hiking daypacks, outdoor excursions, and camping gear", "Travel carry-on luggage and gym bag gear"),
            facts = listOf("The zippered nylon backpack was pioneered for students in the late 1960s by JanSport, transforming how schoolchildren and college students transport books."),
            similarObjects = listOf("Wallet / Cardholder", "Jacket / Outerwear")
        ),
        ObjectInformation(
            id = "wallet",
            name = "Wallet / Cardholder",
            category = "Clothing",
            iconEmoji = "👛",
            shortDescription = "Small, flat pocket case used to carry cash, identification, and credit cards.",
            description = "A wallet organizes payment cards, IDs, and banknotes in a slim pocket-sized profile crafted from full-grain leather, forged carbon fiber, or RFID-blocking aluminum.",
            characteristics = listOf("Bifold, trifold, or slim minimalist cardholder design", "Die-cut card slots for credit cards and driver's licenses", "Full-grain leather or rigid RFID-blocking aluminum plates", "Currency bill compartment or elastic money clip"),
            commonUses = listOf("Carrying payment cards, credit cards, and transit passes", "Storing cash banknotes and paper receipts", "Holding government ID, driver's license, and business cards"),
            facts = listOf("Wallets in ancient Greece were large cloth pouches called 'knapsacks' used to carry food and survival provisions rather than currency coins."),
            similarObjects = listOf("Backpack / Bag", "Smartphone")
        ),

        // --- 6. Household, Living Room & Furniture ---
        ObjectInformation(
            id = "chair",
            name = "Chair / Desk Chair",
            category = "Household",
            iconEmoji = "🪑",
            shortDescription = "Furniture designed for supporting a seated person, typically with four legs and a backrest.",
            description = "Chairs range from ergonomic office mesh task chairs with lumbar support and armrests to wooden dining chairs and comfortable living room armchairs.",
            characteristics = listOf("Horizontal seat surface elevated on legs or gas-lift piston", "Supportive vertical backrest and optional lumbar contour", "Padded upholstery, natural wood, or breathable mesh", "Sturdy base with optional caster wheels for rolling"),
            commonUses = listOf("Seating at desks, dining tables, and conference rooms", "Ergonomic support during long computer work hours", "Relaxing and reading in living spaces"),
            facts = listOf("The ergonomic office chair was pioneered in 1976 by designer Bill Stumpf with the Herman Miller Ergon chair, introducing posture-supporting contour science."),
            similarObjects = listOf("Table / Desk", "Sofa / Couch")
        ),
        ObjectInformation(
            id = "table",
            name = "Table / Desk",
            category = "Household",
            iconEmoji = "🪵",
            shortDescription = "Furniture with a flat horizontal surface supported by legs used for dining, work, or storage.",
            description = "Tables and computer desks provide a stable, elevated work plane constructed from solid hardwood, tempered glass, or engineered steel for writing, dining, and computing.",
            characteristics = listOf("Flat planar tabletop surface supported by legs or trestles", "Solid oak, walnut, engineered laminate, or metal", "Standard ergonomic working height (28 to 30 inches)", "Integrated cable management grommets or storage drawers"),
            commonUses = listOf("Dining meals and communal family gatherings", "Computer workstation setup with monitor, keyboard, and mouse", "Studying, drafting, writing, and displaying decorative items"),
            facts = listOf("The world's earliest known tables were made by ancient Egyptians around 2500 BCE, crafted from wood or stone to keep objects off the earthen floor."),
            similarObjects = listOf("Chair / Desk Chair", "Sofa / Couch")
        ),
        ObjectInformation(
            id = "couch",
            name = "Sofa / Couch",
            category = "Household",
            iconEmoji = "🛋️",
            shortDescription = "Comfortable upholstered piece of living room furniture for seating multiple people.",
            description = "A sofa features plush foam cushions wrapped in woven textile or leather, supported by a wooden frame and internal steel springs to provide relaxing communal seating.",
            characteristics = listOf("Long upholstered frame accommodating 2 to 4+ individuals", "Dense foam and down feather seating cushions", "Supportive armrests and back cushions", "Durable woven fabric, velvet, or top-grain leather upholstery"),
            commonUses = listOf("Lounging, resting, and family television watching", "Entertaining guests in living room spaces", "Relaxing, reading, and taking daytime naps"),
            facts = listOf("The word 'couch' originates from the Old French 'coucher', meaning 'to lie down', while 'sofa' stems from the Arabic 'suffah', a stone bench covered with carpets."),
            similarObjects = listOf("Chair / Desk Chair", "Bed & Bedding")
        ),
        ObjectInformation(
            id = "bed",
            name = "Bed & Bedding",
            category = "Household",
            iconEmoji = "🛏️",
            shortDescription = "Furniture piece used as a place to sleep and relax, featuring a mattress and pillows.",
            description = "A bed consists of a supportive frame and a resilient spring or memory foam mattress, dressed in soft cotton sheets, down pillows, and warm blankets for restorative sleep.",
            characteristics = listOf("Steel or solid wood platform bed frame", "Innerspring, hybrid, or memory foam mattress core", "Soft breathable cotton or linen bedsheets", "Plush sleeping pillows and insulating duvet blanket"),
            commonUses = listOf("Nightly restorative sleep and physical recovery", "Reading, relaxing, and morning wake-up routine", "Rest during illness and fatigue"),
            facts = listOf("Humans spend roughly one-third of their entire lives asleep, making a comfortable mattress one of the most used pieces of equipment in any home."),
            similarObjects = listOf("Sofa / Couch", "Chair / Desk Chair")
        ),
        ObjectInformation(
            id = "lamp",
            name = "Lamp / Lighting",
            category = "Household",
            iconEmoji = "💡",
            shortDescription = "Device for producing artificial illumination using an electric light bulb and shade.",
            description = "Lamps utilize energy-efficient LED or incandescent bulbs enclosed in fabric shades or glass diffusers to cast warm ambient, task, or reading illumination across a room.",
            characteristics = listOf("Electric light bulb socket (E26/E27 standard)", "Translucent fabric shade or frosted glass diffuser", "Weighted metal, ceramic, or wooden base stand", "Inline power switch or touch-capacitive dimmer"),
            commonUses = listOf("Desk task lighting for studying and reading", "Ambient atmospheric illumination in bedrooms and living rooms", "Nightstand bedside reading light"),
            facts = listOf("Thomas Edison's carbon filament incandescent bulb, patented in 1879, operated for 40 hours continuously, launching the modern commercial lighting era."),
            similarObjects = listOf("Table / Desk", "Clock / Watch")
        ),
        ObjectInformation(
            id = "clock",
            name = "Clock / Timepiece",
            category = "Household",
            iconEmoji = "⏰",
            shortDescription = "Instrument for measuring, displaying, and keeping track of hours, minutes, and seconds.",
            description = "Clocks track time using quartz crystal oscillators or digital microcontrollers, presenting standard time on rotating physical hands or digital LED displays.",
            characteristics = listOf("Circular analog dial with hour, minute, and second hands", "Battery-operated quartz crystal oscillation movement", "Legible numeric numerals or minimalist indices", "Wall-mounting bracket or tabletop kickstand"),
            commonUses = listOf("Displaying current local time at a glance", "Scheduling daily appointments, cooking, and deadlines", "Morning alarm clock wake-up alerts"),
            facts = listOf("Mechanical clocks with escapements emerged in European monasteries during the 13th century to alert monks to standardized daily prayer times."),
            similarObjects = listOf("Watch / Smartwatch")
        ),

        // --- 7. Hardware, Workshop & Tools ---
        ObjectInformation(
            id = "tools",
            name = "Hand Tools & Hardware",
            category = "Tools",
            iconEmoji = "🔨",
            shortDescription = "Manual and power instruments used for construction, repair, fastening, and fabrication.",
            description = "Hand tools comprise steel hammers, adjustable wrenches, crosshead screwdrivers, pliers, tape measures, and power drills used to assemble and maintain physical items.",
            characteristics = listOf("Forged carbon or vanadium steel working heads", "Insulated non-slip rubberized grip handles", "High torque and mechanical leverage design", "Corrosion-resistant chrome, black oxide, or zinc plating"),
            commonUses = listOf("Driving nails and driving screws into wood and drywall", "Tightening plumbing pipe fittings and mechanical bolts", "Household repairs, furniture assembly, and DIY projects"),
            facts = listOf("The claw hammer design with a curved dual prong for extracting stubborn nails was documented during the Roman Empire over two thousand years ago."),
            similarObjects = listOf("Scissors / Cutting Tool", "Desk Supplies / Organizers")
        ),

        // --- 8. Sports & Outdoors ---
        ObjectInformation(
            id = "sports",
            name = "Sports & Fitness Equipment",
            category = "Sports",
            iconEmoji = "⚽",
            shortDescription = "Athletic gear including balls, rackets, weights, and mats used for sports and exercise.",
            description = "Sports equipment includes inflatable leather balls, stringed graphite rackets, cast iron dumbbells, and non-slip yoga mats engineered for physical training and competitive athletics.",
            characteristics = listOf("Synthetic leather panels with machine stitching on balls", "Aerodynamic dimples or regulation inflation pressures", "Textured grip zones on rackets and dumbbells", "High-density foam shock absorption on fitness mats"),
            commonUses = listOf("Playing soccer, basketball, football, and tennis", "Strength training and cardiovascular workouts", "Gym fitness routines and outdoor recreation"),
            facts = listOf("The modern 32-panel soccer ball design (truncated icosahedron of 20 hexagons and 12 pentagons) was designed by Eigil Nielsen in 1962 for optimal flight aerodynamics."),
            similarObjects = listOf("Bicycle / Scooter", "Shoes / Sneakers")
        ),
        ObjectInformation(
            id = "bicycle",
            name = "Bicycle / Scooter",
            category = "Sports",
            iconEmoji = "🚲",
            shortDescription = "Human-powered or motor-assisted two-wheeled vehicle steered with handlebars.",
            description = "A bicycle utilizes a lightweight diamond frame, two spoked pneumatic wheels, pedals driving a chain to rear sprockets, and hand brakes for agile, zero-emission transportation.",
            characteristics = listOf("Lightweight aluminum, steel, or carbon fiber frame", "Pair of spoked pneumatic rubber tires with tread", "Pedal-driven chain drive and multi-gear derailleur", "Handlebars with brake levers and comfortable saddle"),
            commonUses = listOf("Eco-friendly urban commuting and errand running", "Cardiovascular exercise and endurance cycling", "Off-road trail riding and recreation"),
            facts = listOf("The bicycle is the most energy-efficient mechanical mode of transportation ever invented, converting over 90% of human pedaling energy into forward momentum."),
            similarObjects = listOf("Sports & Fitness Equipment", "Car / Motor Vehicle")
        ),

        // --- 9. Vehicles & Transportation ---
        ObjectInformation(
            id = "car",
            name = "Car / Motor Vehicle",
            category = "Vehicles",
            iconEmoji = "🚗",
            shortDescription = "Four-wheeled motor vehicle used for passenger transportation on roadways.",
            description = "Automobiles transport passengers and cargo using internal combustion engines or electric battery drivetrains, enclosed in a steel body equipped with advanced safety and navigation systems.",
            characteristics = listOf("Aerodynamic metal unibody chassis with four pneumatic wheels", "Internal combustion engine or electric battery pack", "Cabin with steering wheel, pedals, seats, and touchscreen console", "Headlights, taillights, windshield wipers, and airbags"),
            commonUses = listOf("Daily family commuting and highway long-distance travel", "Transporting groceries, luggage, and personal goods", "Ridesharing and commercial passenger transport"),
            facts = listOf("Karl Benz patented the Benz Patent-Motorwagen in 1886, widely acknowledged as the world's first modern practical production automobile."),
            similarObjects = listOf("Bicycle / Scooter")
        ),

        // --- 10. Music, Hobbies & Toys ---
        ObjectInformation(
            id = "guitar",
            name = "Guitar / Musical Instrument",
            category = "Hobbies",
            iconEmoji = "🎸",
            shortDescription = "Stringed musical instrument with a fretted fingerboard and resonant soundboard.",
            description = "A guitar generates harmonic melodies and chords through plucked or strummed steel or nylon strings vibrating over an acoustic wooden body or magnetic electric pickups.",
            characteristics = listOf("Fretted wooden neck with six or twelve strings", "Spruce, mahogany, or solid-body tonewood construction", "Headstock with geared tuning pegs for pitch adjustment", "Resonant hollow body with soundhole or magnetic pickups"),
            commonUses = listOf("Playing acoustic chords, rock leads, and classical fingerpicking", "Composing music, songwriting, and stage performance", "Musical education and hobby practice"),
            facts = listOf("Instruments resembling guitars have been played for over 4,000 years; the modern acoustic steel-string guitar was developed by C.F. Martin in the United States in the 1850s."),
            similarObjects = listOf("Audio Speaker", "Toy / Game / Puzzle")
        ),
        ObjectInformation(
            id = "toy",
            name = "Toy / Game / Puzzle",
            category = "Hobbies",
            iconEmoji = "🎲",
            shortDescription = "Objects designed for play, entertainment, cognitive challenges, and childhood learning.",
            description = "Toys and games include interlocking LEGO building bricks, Rubik's puzzle cubes, plush teddy bears, and board games designed to stimulate creativity and strategic thinking.",
            characteristics = listOf("Durable non-toxic ABS plastic, wood, or plush fabric", "Vibrant colors and tactile interlocking components", "Rules, mechanisms, or challenges engaging problem-solving", "Designed for children's safety and hobbyist enthusiasts"),
            commonUses = listOf("Children's play, developmental learning, and storytelling", "Family board game nights and friendly competition", "Mind-bending logic puzzle solving and display collection"),
            facts = listOf("A standard 3x3 Rubik's cube has over 43 quintillion possible state configurations (43,252,003,274,489,856,000), but any state can be solved in 20 moves or fewer."),
            similarObjects = listOf("Guitar / Musical Instrument")
        ),

        // --- 11. Nature, Plants & Pets ---
        ObjectInformation(
            id = "plant",
            name = "Houseplant / Flower",
            category = "Nature",
            iconEmoji = "🪴",
            shortDescription = "Living botanical organism cultivated indoors in a pot for decorative and air-purifying qualities.",
            description = "Houseplants such as Monsteras, Pothos, Snake plants, succulents, and blooming flowers photosynthesize natural light, adding lush greenery and calm to interior environments.",
            characteristics = listOf("Vibrant green photosynthetic leaves and stems", "Potted in soil or leca inside decorative planters", "Air-filtering botanical respiration and transpiration", "Requires routine watering and indirect sunlight exposure"),
            commonUses = listOf("Interior home decor, green aesthetics, and office styling", "Purifying indoor air and improving psychological wellbeing", "Hobby gardening, plant propagation, and botany"),
            facts = listOf("NASA's landmark 1989 Clean Air Study found that common indoor houseplants like the Snake plant and Peace lily can actively remove airborne volatile organic compounds (VOCs)."),
            similarObjects = listOf("Pet / Animal")
        ),
        ObjectInformation(
            id = "pet",
            name = "Pet / Animal",
            category = "Nature",
            iconEmoji = "🐕",
            shortDescription = "Domesticated companion animal such as a dog, cat, or bird living alongside humans.",
            description = "Domestic pets—predominantly dogs and cats—share close social bonds with humans, renowned for companionship, playful affection, acute senses, and intelligence.",
            characteristics = listOf("Soft fur coat, whiskers, and expressive tail", "Keen olfactory (scent) and auditory senses", "Playful social demeanor and vocalizations (barking, purring)", "Quadrupedal anatomy with paws and claws"),
            commonUses = listOf("Family companionship, emotional support, and friendship", "Outdoor walking, exercise, and active fetching", "Home protection and companion bonding"),
            facts = listOf("Dogs have an olfactory sense up to 100,000 times more sensitive than humans, possessing over 300 million scent receptors compared to our 6 million."),
            similarObjects = listOf("Houseplant / Flower")
        ),

        // --- 12. People & General ---
        ObjectInformation(
            id = "person",
            name = "Person / Human",
            category = "Personal",
            iconEmoji = "👤",
            shortDescription = "Human individual (Homo sapiens) recognized by posture, facial features, and form.",
            description = "A human detected in the camera frame represents a person identified by bipedal posture, facial contours, and upper-body gestures.",
            characteristics = listOf("Bipedal upright stance and skeletal biomechanics", "Expressive facial features and hand gestures", "Complex cognitive awareness and social communication"),
            commonUses = listOf("Social connection, communication, collaboration, and learning"),
            facts = listOf("The human brain contains approximately 86 billion neurons, each connected to thousands of other neurons to form trillions of synaptic pathways."),
            similarObjects = listOf("Pet / Animal")
        )
    )

    private val knowledgeMap: Map<String, ObjectInformation> = knowledgeList.associateBy { it.id.lowercase() }

    // Comprehensive synonym and alias index mapping hundreds of ML Kit labels to correct rich knowledge entries
    private val aliasMap: Map<String, String> = mapOf(
        // Electronics
        "laptop" to "laptop", "laptop computer" to "laptop", "notebook computer" to "laptop",
        "personal computer" to "laptop", "macbook" to "laptop", "computer" to "laptop",
        "cell phone" to "smartphone", "mobile phone" to "smartphone", "smartphone" to "smartphone",
        "cellular telephone" to "smartphone", "iphone" to "smartphone", "android phone" to "smartphone",
        "telephone" to "smartphone", "phone" to "smartphone",
        "tablet" to "tablet", "ipad" to "tablet", "tablet computer" to "tablet", "kindle" to "tablet",
        "keyboard" to "keyboard", "computer keyboard" to "keyboard", "typewriter" to "keyboard",
        "mouse" to "mouse", "computer mouse" to "mouse", "trackpad" to "mouse",
        "monitor" to "monitor", "computer monitor" to "monitor", "screen" to "monitor",
        "display device" to "monitor", "flat panel display" to "monitor",
        "television" to "television", "tv" to "television", "smart tv" to "television",
        "headphones" to "headphones", "earphones" to "headphones", "headset" to "headphones",
        "earbuds" to "headphones", "airpods" to "headphones", "audio equipment" to "headphones",
        "speaker" to "speaker", "loudspeaker" to "speaker", "bluetooth speaker" to "speaker",
        "soundbar" to "speaker", "smart speaker" to "speaker",
        "camera" to "camera", "digital camera" to "camera", "dslr" to "camera",
        "camera lens" to "camera", "camcorder" to "camera", "lens" to "camera",
        "watch" to "smartwatch", "smartwatch" to "smartwatch", "wristwatch" to "smartwatch",
        "analog watch" to "smartwatch",
        "remote" to "remote", "remote control" to "remote", "controller" to "remote",

        // Kitchenware
        "cup" to "cup", "coffee cup" to "cup", "mug" to "cup", "coffee" to "cup",
        "espresso" to "cup", "teacup" to "cup", "cappuccino" to "cup",
        "bottle" to "bottle", "water bottle" to "bottle", "plastic bottle" to "bottle",
        "thermos" to "bottle", "flask" to "bottle", "tumbler" to "bottle",
        "glass" to "glass", "wine glass" to "glass", "beer glass" to "glass",
        "goblet" to "glass", "drinking glass" to "glass",
        "plate" to "plate", "dish" to "plate",
        "dinner plate" to "plate", "saucer" to "plate", "platter" to "plate",
        "bowl" to "bowl", "soup bowl" to "bowl", "salad bowl" to "bowl", "cereal bowl" to "bowl",
        "fork" to "cutlery", "spoon" to "cutlery", "table knife" to "cutlery",
        "butter knife" to "cutlery", "cutlery" to "cutlery", "chopsticks" to "cutlery",
        "frying pan" to "cookware", "skillet" to "cookware", "pan" to "cookware",
        "saucepan" to "cookware", "pot" to "cookware", "wok" to "cookware",
        "spatula" to "cookware", "whisk" to "cookware", "cutting board" to "cookware",
        "microwave" to "appliance", "microwave oven" to "appliance", "toaster" to "appliance",
        "blender" to "appliance", "kettle" to "appliance", "electric kettle" to "appliance",
        "coffee maker" to "appliance", "air fryer" to "appliance",

        // Food
        "apple" to "apple", "fruit" to "apple", "red apple" to "apple", "green apple" to "apple",
        "banana" to "banana", "plantain" to "banana",
        "produce" to "produce", "vegetable" to "produce", "tomato" to "produce",
        "orange" to "produce", "lemon" to "produce", "lime" to "produce", "strawberry" to "produce",
        "avocado" to "produce", "carrot" to "produce", "onion" to "produce", "potato" to "produce",
        "cucumber" to "produce", "broccoli" to "produce", "salad" to "produce",
        "food" to "food", "bread" to "food", "sandwich" to "food", "pizza" to "food",
        "burger" to "food", "snack" to "food", "pastry" to "food", "cookie" to "food",

        // Work
        "pen" to "pen", "ballpoint pen" to "pen", "gel pen" to "pen", "pencil" to "pen",
        "marker" to "pen", "highlighter" to "pen", "writing implement" to "pen",
        "notebook" to "notebook", "spiral notebook" to "notebook", "notepad" to "notebook",
        "paper" to "notebook", "document" to "notebook", "pad" to "notebook",
        "book" to "book", "publication" to "book", "textbook" to "book", "novel" to "book",
        "magazine" to "book",
        "scissors" to "scissors", "shears" to "scissors",
        "stationery" to "stationery", "stapler" to "stationery", "paperclip" to "stationery",
        "tape" to "stationery", "eraser" to "stationery", "ruler" to "stationery",
        "office supplies" to "stationery",

        // Clothing & Footwear
        "shoe" to "shoe", "shoes" to "shoe", "sneaker" to "shoe",
        "sneakers" to "shoe", "boot" to "shoe", "sandal" to "shoe", "running shoes" to "shoe",
        "athletic shoe" to "shoe", "slippers" to "shoe",
        "shirt" to "shirt", "t-shirt" to "shirt", "tee" to "shirt", "polo" to "shirt",
        "sweater" to "shirt", "hoodie" to "shirt",
        "pants" to "pants", "jeans" to "pants", "trousers" to "pants", "shorts" to "pants",
        "slacks" to "pants", "joggers" to "pants",
        "jacket" to "jacket", "coat" to "jacket", "blazer" to "jacket",
        "hat" to "hat", "cap" to "hat", "baseball cap" to "hat", "beanie" to "hat",
        "glasses" to "glasses", "eyeglasses" to "glasses", "sunglasses" to "glasses",
        "spectacles" to "glasses", "goggles" to "glasses",
        "backpack" to "backpack", "bag" to "backpack", "handbag" to "backpack", "purse" to "backpack",
        "tote bag" to "backpack", "suitcase" to "backpack", "luggage" to "backpack",
        "wallet" to "wallet", "cardholder" to "wallet", "billfold" to "wallet",

        // Household
        "chair" to "chair", "armchair" to "chair", "office chair" to "chair",
        "desk chair" to "chair", "stool" to "chair", "seat" to "chair",
        "table" to "table", "desk" to "table", "coffee table" to "table", "dining table" to "table",
        "couch" to "couch", "sofa" to "couch", "sectional" to "couch",
        "bed" to "bed", "mattress" to "bed", "pillow" to "bed", "blanket" to "bed",
        "lamp" to "lamp", "lighting" to "lamp", "lantern" to "lamp",
        "clock" to "clock", "wall clock" to "clock", "alarm clock" to "clock",

        // Tools
        "tool" to "tools", "tools" to "tools", "hammer" to "tools",
        "wrench" to "tools", "screwdriver" to "tools", "pliers" to "tools", "saw" to "tools",
        "drill" to "tools", "power drill" to "tools", "tape measure" to "tools",

        // Sports
        "ball" to "sports", "soccer ball" to "sports", "football" to "sports",
        "basketball" to "sports", "tennis ball" to "sports", "racket" to "sports",
        "dumbbell" to "sports", "yoga mat" to "sports",
        "bicycle" to "bicycle", "bike" to "bicycle", "cycle" to "bicycle", "scooter" to "bicycle",
        "skateboard" to "bicycle",

        // Vehicles
        "car" to "car", "automobile" to "car", "truck" to "car", "van" to "car", "motorcycle" to "car",

        // Hobbies
        "guitar" to "guitar", "acoustic guitar" to "guitar", "electric guitar" to "guitar",
        "piano" to "guitar", "violin" to "guitar",
        "toy" to "toy", "doll" to "toy", "action figure" to "toy", "teddy bear" to "toy",
        "puzzle" to "toy", "rubik" to "toy", "board game" to "toy", "lego" to "toy",
        "stuffed toy" to "toy", "plush" to "toy",

        // Nature
        "plant" to "plant", "houseplant" to "plant", "flower" to "plant", "succulent" to "plant",
        "cactus" to "plant", "tree" to "plant", "leaf" to "plant", "rose" to "plant",
        "flowerpot" to "plant", "vase" to "plant",
        "dog" to "pet", "puppy" to "pet", "canine" to "pet", "cat" to "pet", "kitten" to "pet",
        "feline" to "pet", "bird" to "pet", "fish" to "pet", "pet" to "pet",
        "golden retriever" to "pet", "labrador" to "pet", "poodle" to "pet",
        "german shepherd" to "pet", "bulldog" to "pet", "beagle" to "pet",
        "tabby cat" to "pet", "siamese cat" to "pet", "persian cat" to "pet",

        // People
        "person" to "person", "human" to "person", "man" to "person", "woman" to "person",
        "child" to "person", "people" to "person",

        // Additional ML Kit common outputs that ARE real objects
        "shelf" to "table", "bookshelf" to "table", "shelving" to "table",
        "drawer" to "table", "cabinet" to "table", "dresser" to "table",
        "mirror" to "glasses", "curtain" to "bed", "rug" to "chair",
        "carpet" to "chair", "towel" to "shirt", "napkin" to "stationery",
        "jar" to "bottle", "can" to "bottle", "tin" to "bottle",
        "box" to "stationery", "crate" to "stationery", "basket" to "backpack",
        "door" to "table", "handle" to "tools",
        "umbrella" to "jacket", "fan" to "appliance",
        "refrigerator" to "appliance", "oven" to "appliance", "stove" to "appliance",
        "washing machine" to "appliance", "dishwasher" to "appliance",
        "iron" to "appliance", "vacuum cleaner" to "appliance",
        "air conditioner" to "appliance",
        "computer hardware" to "laptop", "personal computer" to "laptop",
        "desktop computer" to "laptop", "workstation" to "laptop",
        "charger" to "remote", "adapter" to "remote", "cable" to "remote",
        "usb" to "remote", "plug" to "remote", "outlet" to "remote",
        "power supply" to "remote", "battery" to "remote",
        "headband" to "hat", "scarf" to "jacket", "gloves" to "jacket",
        "tie" to "shirt", "belt" to "pants", "sock" to "shoe", "socks" to "shoe",
        "necklace" to "glasses", "bracelet" to "smartwatch", "ring" to "smartwatch",
        "earring" to "glasses", "jewelry" to "glasses", "jewellery" to "glasses",
        "coin" to "wallet", "money" to "wallet", "cash" to "wallet",
        "key" to "tools", "keys" to "tools", "lock" to "tools", "padlock" to "tools",
        "candle" to "lamp", "flashlight" to "lamp", "torch" to "lamp",
        "pillow" to "bed", "cushion" to "bed", "blanket" to "bed",
        "mattress" to "bed", "duvet" to "bed", "comforter" to "bed",
        "trash can" to "stationery", "bin" to "stationery",
        "tissue" to "stationery", "tissue box" to "stationery"
    )

    /**
     * Finds the best matching ObjectInformation from a prioritized list of ML Kit candidate labels.
     * Specific high-information classes are prioritized over broad superclasses (e.g. "Coffee cup" over "Tableware").
     */
    fun resolveBestInfo(
        cropLabels: List<Pair<String, Float>>,
        objLabels: List<Pair<String, Float>> = emptyList()
    ): ResolvedObjectInfo {
        val allCandidates = (cropLabels + objLabels).filter { it.second >= 0.15f }
        if (allCandidates.isEmpty()) {
            return ResolvedObjectInfo(getObjectInfo("object"), 0.50f)
        }

        val scored = allCandidates.map { (text, conf) ->
            val clean = text.trim().lowercase()
            val tier = classifyLabelTier(clean)
            val priorityScore = when (tier) {
                LabelTier.SPECIFIC_OBJECT -> conf + 0.25f   // Known object → big boost
                LabelTier.NEUTRAL         -> conf            // Unknown but not generic → neutral
                LabelTier.GENERIC         -> conf - 0.30f    // Scene/material/abstract → heavy penalty
            }
            Triple(text, conf, priorityScore)
        }.sortedByDescending { it.third }

        val best = scored.first()
        val info = getObjectInfo(best.first)
        val bestTier = classifyLabelTier(best.first.trim().lowercase())
        return ResolvedObjectInfo(info, best.second, bestTier)
    }

    /**
     * Checks if [phrase] appears as whole word(s) in [text].
     * E.g. "laptop" matches "gaming laptop pc", but "art" does NOT match "smartwatch",
     * and "pan" does NOT match "flat panel display".
     */
    private fun containsWord(text: String, phrase: String): Boolean {
        if (text == phrase) return true
        val pattern = "\\b" + Regex.escape(phrase) + "\\b"
        return Regex(pattern, RegexOption.IGNORE_CASE).containsMatchIn(text)
    }

    /**
     * Classifies an ML Kit label into one of three tiers:
     * - SPECIFIC_OBJECT: Maps to a known knowledge base entry (via alias or direct match)
     * - GENERIC: Scene descriptor, material, shape, abstract concept, or coarse detector category
     * - NEUTRAL: Everything else (might be a real object we just don't have in the KB)
     */
    private fun classifyLabelTier(label: String): LabelTier {
        val clean = label.trim().lowercase()

        // 1. Explicit generic/non-object label check (scene, material, shape, abstract, coarse category)
        if (clean in nonObjectLabels) return LabelTier.GENERIC

        // 2. Direct exact match in knowledgeMap or aliasMap
        if (knowledgeMap.containsKey(clean)) return LabelTier.SPECIFIC_OBJECT
        if (aliasMap.containsKey(clean)) return LabelTier.SPECIFIC_OBJECT

        // 3. Whole-word alias matching (e.g. "wireless gaming mouse" contains whole word "mouse")
        for ((alias, _) in aliasMap) {
            if (containsWord(clean, alias)) return LabelTier.SPECIFIC_OBJECT
        }

        // 4. Check if any word in the label is in nonObjectLabels (e.g. "interior design", "wood flooring")
        for (w in clean.split(" ", "_", "-")) {
            if (w.length >= 3 && w in nonObjectLabels) {
                return LabelTier.GENERIC
            }
        }

        return LabelTier.NEUTRAL
    }

    /**
     * Comprehensive set of ML Kit labels that are NOT useful object identifiers.
     * These are scene descriptors, materials, shapes, abstract concepts, activities,
     * coarse detector categories, and broad taxonomic terms.
     */
    private val nonObjectLabels: Set<String> = setOf(
        // --- ML Kit Object Detector's 5 coarse categories ---
        "fashion good", "home good", "food", "place", "plant",

        // --- Scene / Place / Environment ---
        "room", "interior design", "building", "architecture", "sky", "landscape",
        "city", "street", "floor", "flooring", "ceiling", "wall", "window",
        "outdoor", "indoor", "urban", "rural", "nature", "garden", "park",
        "beach", "mountain", "forest", "field", "road", "highway", "path",
        "hallway", "corridor", "kitchen", "bathroom", "bedroom", "living room",
        "office", "classroom", "gym", "stadium", "restaurant", "store", "shop",
        "warehouse", "garage", "basement", "attic", "balcony", "patio", "yard",
        "parking lot", "sidewalk", "bridge", "tunnel", "airport", "station",
        "underwater", "space", "night", "day", "dawn", "dusk", "sunset", "sunrise",

        // --- Material / Texture / Surface ---
        "wood", "metal", "plastic", "textile", "fabric", "leather",
        "concrete", "brick", "stone", "ceramic", "porcelain", "marble", "granite",
        "rubber", "foam", "cardboard", "aluminium", "aluminum", "steel",
        "copper", "bronze", "brass", "chrome", "silver", "gold",
        "cotton", "silk", "wool", "denim", "nylon", "polyester", "velvet",
        "linen", "canvas", "mesh", "net",

        // --- Shape / Geometry / Visual properties ---
        "rectangle", "circle", "square", "triangle", "line", "curve", "pattern",
        "stripe", "polka dot", "chevron", "grid", "spiral", "symmetry",
        "parallel", "perpendicular", "angle",

        // --- Typography / Graphics ---
        "font", "logo", "symbol", "sign", "text", "label", "brand", "emblem",
        "icon", "badge", "banner", "poster", "advertisement", "billboard",

        // --- Abstract concepts / Activities ---
        "design", "art", "event", "team", "selfie", "happy", "smile", "fun",
        "fashion", "style", "trend", "vintage", "retro", "modern", "classic",
        "luxury", "premium", "professional", "casual", "formal", "sport",
        "recreation", "leisure", "entertainment", "performance", "competition",
        "celebration", "ceremony", "party", "meeting", "conference", "workshop",
        "travel", "tourism", "adventure", "exploration", "camping", "hiking",

        // --- Broad taxonomy / Biology ---
        "organism", "vertebrate", "invertebrate", "mammal", "carnivore",
        "herbivore", "omnivore", "reptile", "amphibian", "insect", "arachnid",
        "crustacean", "mollusk", "primate", "rodent", "canid", "felid",
        "produce", "ingredient", "snack", "dish", "cuisine", "meal",
        "beverage", "dessert", "appetizer", "condiment", "seasoning", "spice",
        "herb", "grain", "cereal", "dairy", "meat", "seafood", "poultry",
        "baked goods", "confectionery", "fast food", "junk food",

        // --- Broad object categories (not specific objects) ---
        "tableware", "drinkware", "dishware", "cookware and bakeware",
        "electronic device", "consumer electronics", "technology", "gadget",
        "furniture", "clothing", "footwear", "outerwear", "accessory",
        "fashion accessory", "personal accessory",
        "tool", "hardware", "vehicle", "motor vehicle",
        "toy", "animal", "plant", "object",
        "personal protective equipment",
        "material", "equipment", "device", "machine", "appliance",
        "instrument", "apparatus", "container", "packaging", "wrapper",
        "fixture", "fitting", "component", "part", "piece", "item", "thing",

        // --- Photo / Image properties ---
        "photograph", "photo", "image", "picture", "snapshot", "portrait",
        "close-up", "macro", "wide angle", "bokeh", "depth of field",
        "exposure", "contrast", "brightness", "shadow", "highlight", "reflection",
        "transparency", "opacity", "blur", "focus", "sharpness",
        "color", "hue", "saturation", "monochrome", "black and white",
        "grayscale", "sepia",

        // --- Body / Person descriptors ---
        "face", "head", "hand", "finger", "arm", "leg", "foot", "eye",
        "nose", "mouth", "ear", "hair", "skin", "body", "torso",
        "gesture", "pose", "posture", "expression", "emotion",
        "smile", "laugh", "cry",

        // --- Miscellaneous non-objects ---
        "bonfire", "fire", "flame", "smoke", "steam", "vapor",
        "water", "ice", "snow", "rain", "cloud", "fog", "mist",
        "shadow", "darkness", "glow", "sparkle", "glitter",
        "noise", "music", "sound", "silence",
        "comfort", "warmth", "coolness", "temperature",
        "weight", "height", "length", "width", "depth", "area", "volume"
    )

    /**
     * Resolves an arbitrary object label into rich, complete ObjectInformation.
     * Uses direct lookup, alias mapping, and whole-word matching.
     */
    fun getObjectInfo(label: String): ObjectInformation {
        val rawKey = label.trim().lowercase()

        // 1. Exact ID match
        knowledgeMap[rawKey]?.let { return it }

        // 2. Direct alias mapping
        aliasMap[rawKey]?.let { targetId ->
            knowledgeMap[targetId]?.let { return it }
        }

        // 3. Whole-word alias matching: check if an alias is a distinct word/phrase in rawKey
        // Prioritize longer alias matches first (e.g. "coffee cup" before "cup")
        val matchingAlias = aliasMap.entries
            .filter { (alias, _) -> containsWord(rawKey, alias) }
            .maxByOrNull { it.key.length }

        if (matchingAlias != null) {
            knowledgeMap[matchingAlias.value]?.let { return it }
        }

        // 4. Keyword heuristic matching with word boundaries
        return when {
            containsWord(rawKey, "phone") || containsWord(rawKey, "cellphone") || containsWord(rawKey, "iphone") -> knowledgeMap["smartphone"]!!
            containsWord(rawKey, "laptop") || containsWord(rawKey, "macbook") || containsWord(rawKey, "chromebook") -> knowledgeMap["laptop"]!!
            containsWord(rawKey, "monitor") || containsWord(rawKey, "screen") || containsWord(rawKey, "display") -> knowledgeMap["monitor"]!!
            containsWord(rawKey, "headphones") || containsWord(rawKey, "earphones") || containsWord(rawKey, "headset") || containsWord(rawKey, "earbuds") || containsWord(rawKey, "airpods") -> knowledgeMap["headphones"]!!
            containsWord(rawKey, "cup") || containsWord(rawKey, "mug") || containsWord(rawKey, "teacup") -> knowledgeMap["cup"]!!
            containsWord(rawKey, "bottle") || containsWord(rawKey, "flask") || containsWord(rawKey, "thermos") -> knowledgeMap["bottle"]!!
            containsWord(rawKey, "plate") || containsWord(rawKey, "saucer") || containsWord(rawKey, "platter") -> knowledgeMap["plate"]!!
            containsWord(rawKey, "shoe") || containsWord(rawKey, "shoes") || containsWord(rawKey, "sneaker") || containsWord(rawKey, "sneakers") || containsWord(rawKey, "boot") || containsWord(rawKey, "boots") -> knowledgeMap["shoe"]!!
            containsWord(rawKey, "shirt") || containsWord(rawKey, "t-shirt") || containsWord(rawKey, "jacket") || containsWord(rawKey, "hoodie") || containsWord(rawKey, "sweater") || containsWord(rawKey, "coat") -> knowledgeMap["shirt"]!!
            containsWord(rawKey, "pants") || containsWord(rawKey, "jeans") || containsWord(rawKey, "trousers") || containsWord(rawKey, "shorts") -> knowledgeMap["pants"]!!
            containsWord(rawKey, "chair") || containsWord(rawKey, "armchair") || containsWord(rawKey, "stool") -> knowledgeMap["chair"]!!
            containsWord(rawKey, "table") || containsWord(rawKey, "desk") -> knowledgeMap["table"]!!
            containsWord(rawKey, "couch") || containsWord(rawKey, "sofa") -> knowledgeMap["couch"]!!
            containsWord(rawKey, "pen") || containsWord(rawKey, "pencil") || containsWord(rawKey, "marker") || containsWord(rawKey, "highlighter") -> knowledgeMap["pen"]!!
            containsWord(rawKey, "book") || containsWord(rawKey, "notebook") || containsWord(rawKey, "textbook") -> knowledgeMap["book"]!!
            containsWord(rawKey, "plant") || containsWord(rawKey, "flower") || containsWord(rawKey, "houseplant") || containsWord(rawKey, "cactus") || containsWord(rawKey, "succulent") -> knowledgeMap["plant"]!!
            containsWord(rawKey, "dog") || containsWord(rawKey, "puppy") || containsWord(rawKey, "cat") || containsWord(rawKey, "kitten") || containsWord(rawKey, "pet") -> knowledgeMap["pet"]!!
            containsWord(rawKey, "tool") || containsWord(rawKey, "tools") || containsWord(rawKey, "drill") || containsWord(rawKey, "hammer") || containsWord(rawKey, "wrench") || containsWord(rawKey, "screwdriver") || containsWord(rawKey, "pliers") -> knowledgeMap["tools"]!!
            containsWord(rawKey, "car") || containsWord(rawKey, "automobile") || containsWord(rawKey, "vehicle") || containsWord(rawKey, "truck") -> knowledgeMap["car"]!!
            containsWord(rawKey, "bicycle") || containsWord(rawKey, "bike") || containsWord(rawKey, "scooter") -> knowledgeMap["bicycle"]!!
            else -> createGenericObject(label)
        }
    }

    fun getAllObjects(): List<ObjectInformation> {
        return knowledgeList
    }

    fun getObjectsByCategory(category: String): List<ObjectInformation> {
        if (category.equals("All", ignoreCase = true)) return getAllObjects()
        return knowledgeList.filter { it.category.equals(category, ignoreCase = true) }
    }

    private fun createGenericObject(label: String): ObjectInformation {
        val cleanLabel = label.replace("pre_", "").replace("_", " ").trim()
        val formatted = cleanLabel.split(" ").joinToString(" ") { word ->
            word.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        }

        val (cat, emoji) = guessCategoryAndEmoji(cleanLabel.lowercase())

        return ObjectInformation(
            id = label.lowercase(),
            name = formatted,
            category = cat,
            iconEmoji = emoji,
            shortDescription = "$formatted identified in your field of view.",
            description = "The $formatted is a real-world object detected by ObjectLens, classified under $cat with machine learning vision recognition.",
            characteristics = listOf("Physical everyday object", "Recognized via on-device optical processing", "Classified into the $cat collection"),
            commonUses = listOf("Everyday interaction and utility", "Identified through on-device visual analysis"),
            facts = listOf("ObjectLens classifies thousands of real-world objects using local neural vision processing."),
            similarObjects = emptyList()
        )
    }

    private fun guessCategoryAndEmoji(l: String): Pair<String, String> {
        return when {
            l.contains("phone") || l.contains("laptop") || l.contains("tech") || l.contains("cable") || l.contains("audio") || l.contains("gadget") -> "Electronics" to "💻"
            l.contains("cup") || l.contains("mug") || l.contains("dish") || l.contains("plate") || l.contains("pan") || l.contains("pot") || l.contains("kitchen") -> "Kitchenware" to "🍽️"
            l.contains("food") || l.contains("fruit") || l.contains("apple") || l.contains("bread") || l.contains("snack") -> "Food" to "🍎"
            l.contains("shoe") || l.contains("shirt") || l.contains("jean") || l.contains("jacket") || l.contains("wear") -> "Clothing" to "👟"
            l.contains("chair") || l.contains("table") || l.contains("lamp") || l.contains("bed") || l.contains("couch") -> "Household" to "🛋️"
            l.contains("pen") || l.contains("book") || l.contains("paper") || l.contains("note") -> "Work" to "📓"
            l.contains("tool") || l.contains("drill") || l.contains("hammer") || l.contains("wrench") -> "Tools" to "🔨"
            l.contains("ball") || l.contains("sport") || l.contains("fitness") -> "Sports" to "⚽"
            l.contains("plant") || l.contains("flower") || l.contains("tree") -> "Nature" to "🪴"
            l.contains("dog") || l.contains("cat") || l.contains("pet") || l.contains("bird") -> "Nature" to "🐕"
            l.contains("car") || l.contains("bike") || l.contains("vehicle") -> "Vehicles" to "🚗"
            else -> "General" to "🔍"
        }
    }
}
