Event Management and Menu Activity Documentation

Event Management:
The EventAdapter class is responsible for handling event creation, display, and interaction within a RecyclerView. It allows event organizers to input event details, upload images, and save data to Firestore.

Initializing RecyclerView Adapter:
    class EventAdapter(private val events: MutableList<EventModel>, private val activity: OrganizerHomeActivity) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {


Binding Event Data to Views
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
    val event = events[position]
    val binding = holder.binding

    binding.etDate.setText(event.date)
    binding.etTime.setText(event.time)
    binding.etLocation.setText(event.location)

    if (!event.imageUrl.isNullOrEmpty()) {
        Glide.with(binding.root.context)
            .load(event.imageUrl)
            .into(binding.imgEvent)

        binding.imgEvent.visibility = View.VISIBLE
        binding.btnAddImage.visibility = View.GONE
    } else {
        binding.imgEvent.visibility = View.GONE
        binding.btnAddImage.visibility = View.VISIBLE
        }
    }

Handling Event Creation
    binding.btnJoin1.setOnClickListener {
    val date = binding.etDate.text.toString()
    val time = binding.etTime.text.toString()
    val location = binding.etLocation.text.toString()
    val imageUrl = event.imageUrl

    if (date.isEmpty() || time.isEmpty() || location.isEmpty() || imageUrl.isNullOrEmpty()) {
        Toast.makeText(binding.root.context, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show()
        return@setOnClickListener
    }

    uploadEventToFirestore(event, date, time, location, imageUrl, binding)
    }


Uploading Event to Firestore
    private fun uploadEventToFirestore(
    event: EventModel,
    date: String,
    time: String,
    location: String,
    imageUrl: String?,
    binding: ItemEventBinding
) {
    val firestore = FirebaseFirestore.getInstance()

    val eventData = hashMapOf(
        "date" to date,
        "time" to time,
        "location" to location,
        "imageUrl" to imageUrl,
        "isCreated" to true
    )

    firestore.collection("events").add(eventData)
        .addOnSuccessListener { documentReference ->
            event.id = documentReference.id
            event.isCreated = true
            Toast.makeText(binding.root.context, "Event saved successfully!", Toast.LENGTH_SHORT).show()
        }
        .addOnFailureListener {
            Toast.makeText(binding.root.context, "Failed to save event", Toast.LENGTH_SHORT).show()
        }
    }



Menu Activity:
The MenuActivity class is responsible for handling user profile details and authentication actions.


Initializing Components
    private lateinit var binding: FragmentMenuBinding


Displaying User Information

    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = FragmentMenuBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val username = intent.getStringExtra("username") ?: "User"
    val role = intent.getStringExtra("role") ?: "Role"
    val email = intent.getStringExtra("email") ?: "Email"

    binding.tvUsername.text = username
    binding.tvRole.text = role
    binding.tvEmail.text = "Email: $email"
    }

Handling Profile and About Section Toggle
    binding.tvProfile.setOnClickListener {
    binding.profileDetails.visibility = if (binding.profileDetails.visibility == View.VISIBLE) View.GONE else View.VISIBLE
}

binding.tvAbout.setOnClickListener {
    binding.tvAboutContent.visibility = if (binding.tvAboutContent.visibility == View.VISIBLE) View.GONE else View.VISIBLE
}

Logout Functionality

binding.btnLogout.setOnClickListener {
    FirebaseAuth.getInstance().signOut()
    val intent = Intent(this, LoginActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    finish()
    }